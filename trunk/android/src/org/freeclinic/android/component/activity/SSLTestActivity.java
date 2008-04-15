/*	
 
  	Free Clinic Project
 
 	Copyright (c) 2007, 2008  Free Clinic Project  
 		A project by students of the University of California San Diego.  All rights reserved.
	
	This file is part of the Free Clinic Project.

    The Free Clinic Project is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    The Free Clinic Project is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Free Clinic Project.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeclinic.android.component.activity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.freeclinic.android.R;
import org.freeclinic.android.service.FCPService;
import org.freeclinic.common.network.exception.DisconnectException;
import org.freeclinic.common.network.exception.ServerTimeoutException;
import org.freeclinic.common.network.response.LoginFailureResponse;
import org.freeclinic.common.network.response.LoginSuccessResponse;
import org.freeclinic.common.network.response.MessageListResponse;
import org.freeclinic.common.network.response.NotAuthorizedResponse;
import org.freeclinic.common.network.response.PatientListResponse;
import org.freeclinic.common.network.response.PatientMessageListResponse;
import org.freeclinic.common.network.response.ResponseType;
import org.freeclinic.common.network.response.UnknownRequestResponse;
import org.freeclinic.common.network.response.VitalsResponse;
import org.freeclinic.common.type.ClinicRequest;
import org.freeclinic.common.type.ClinicResponse;
import org.freeclinic.common.type.DataPacket;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * 
 * @author Brian Visser
 *
 */

public class SSLTestActivity extends Activity {
	private static final String TAG="SSL";
	private int port = 8066; // ssl port
	private String host = "freeclinicproject.org";
	private FCPService service;
	private int tries;
	private int interval;
	private int minRetry = 5;
	private int maxRetry = 10;

	private SSLSocket socket;
	private DataOutputStream outputStream; 
	private DataInputStream inputStream;
	
	
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.ssl_test);
		Button connect = (Button)findViewById(R.id.connect);
		connect.setOnClickListener(new ConnectOnClickListener());
	}
	
	private class ConnectOnClickListener implements View.OnClickListener {
    	public void onClick(View v) {    		
    		try
    		{
    			Log.d(TAG, "SSL Client Test Started 8");
  			
    			// Create a trust manager that does not validate certificate chains
    			// TrustManager trusts all certificates
    			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
    				public java.security.cert.X509Certificate[] getAcceptedIssuers()
    				{
    					return null;
    				}
    				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
    				{
    				}
    				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
    				{
    				}
    			}
    			}; 
    			
    			SSLContext sc = SSLContext.getInstance("TLS");
    			sc.init(null, trustAllCerts, new java.security.SecureRandom());
    			SSLSocketFactory sslsocketfactory = (SSLSocketFactory)sc.getSocketFactory(); 
    			Log.d(TAG, "SSLSocketFactory Created");

    			socket = (SSLSocket)sslsocketfactory.createSocket(host, port);
    			Log.d(TAG, "Socket creation finished");
    		}
    		catch (NoSuchAlgorithmException e)
    		{
    			Log.e(TAG, e.getMessage());
    			throw(new RuntimeException(e));
    		}
    		catch (KeyManagementException e)
    		{
    			Log.e(TAG, e.getMessage());
    			throw(new RuntimeException(e));
    		}
			catch (java.net.ConnectException e)
			{
				Log.e(TAG, "cannot connect to " + host + ":" + Integer.toString(port) + " - " + e.getMessage());

				throw(new RuntimeException(e));
			}
			catch (UnknownHostException e)
			{
				Log.e(TAG, "Unknown Host - " + this + ": " + e.getMessage());
				// Code for retrying
				Log.e(TAG, "Login failed, waiting...");
				if (tries++ <= minRetry)
				{
					int divider = 1;
					try {
						for (int period = 0; period < divider; period++)
						{
//							if (cancel)
//								return false;
							Thread.sleep(interval/divider);
						}
					}
					catch (InterruptedException ie)	{}
				}
				else if (tries++ <= maxRetry)
				{
					Log.e(TAG, "Trying more slowly");

					tries = 0;
					interval = interval * 2; // double wait time?
				}
				else
				{
					// fail - stop trying
					throw new ServerTimeoutException();
				}
				Log.e(TAG, "Done waiting, attempt " + tries + " to login");
//				this.connect();
				// end retry connection code
//				throw(new RuntimeException(e));
			}
			catch (javax.net.ssl.SSLException e)
			{
				Log.e(TAG, "SSL Socket Connection Error");
				throw(new RuntimeException(e));
			}
			catch (IOException e)
			{
				Log.e(TAG, "Socket IO Error: " + e.getMessage());

				throw(new RuntimeException(e));
			}

			
			try {
				/*
				 * ObjectOutputStream must be created before ObjectInputStream
				 * because the ObjectInputStream constructor blocks while reading
				 * a header from the stream source.
				 */
				Log.d(TAG, "Opening Output/Input Streams");
				outputStream = new DataOutputStream(socket.getOutputStream());
				inputStream = new DataInputStream(socket.getInputStream());
				Log.d(TAG, "Connected To Server");
				
				
				Log.d(TAG, "Sending data to server");
				outputStream.writeUTF("TEST test TEST");
			}	
			catch (java.io.EOFException e)
			{
				Log.e(TAG, "Unexpected EOF: " + e.getMessage());
				throw(new RuntimeException(e));
			}
			catch (java.io.IOException e)
			{
				Log.e(TAG, "Stream IO Error: " + e.getMessage());
				throw(new RuntimeException(e));
			}
		}
	}
	public ClinicResponse request(ClinicRequest request) throws DisconnectException {
		DataPacket responsePacket = null;
		Log.e(TAG, "Request called");
		if (!socket.isConnected())
		{
			Log.e(TAG, "Disconnection Detected");
			service.showDisconnection();
		}
		else{	
			try {
				// WRITE THE REQUEST
//				Log.e(TAG, "about to write request");
				writeRequest(request);
//				Log.e(TAG, "done writing request");
				// READ THE RESPONSE
//				Log.e(TAG, "about to read response");
				responsePacket = readResponse();
//				Log.e(TAG, "read response");
			}
			catch (StreamCorruptedException e) {
				Log.e(TAG, "Stream Corrupted: " + e.getMessage());
			}
			catch (IOException e) {
				Log.e(TAG, "IO Exception: " + e.getMessage());
//				connect();
			}
		}
		// Now figure out what the response is and generate the appropriate object
		ClinicResponse output=null;
		try {
			output = generateResponse(responsePacket);
		}
		catch(JSONException e) {
			Log.e(TAG, "JSON Exception: " + e.getMessage());
			throw new RuntimeException(e);
		}
		return output;	
	}
	
	/**
	 * Write a request to the server network stream
	 * @param request the request you want to write
	 * @param requestType reference to integer type of request
	 * @param requestLen reference to length of request
	 * @returns the type of the request written
	 * @throws IOException
	 * @throws StreamCorruptedException
	 */
	private int 
	writeRequest(ClinicRequest request) 
	throws IOException,StreamCorruptedException {	
		if (request != null) {
			String data=null;
			try {
				data = request.JSON().toString();
			} catch(JSONException e) {
				Log.e(TAG, "Error serializing request json: " + e.getMessage());
			}
			byte[] dataPacket = data.getBytes();

			int requestLen = dataPacket.length;
			// 1. Write the length the response object 
			Log.e(TAG, "Writing Request Length " + requestLen);
			outputStream.writeInt(requestLen);
	
			// 2. Write the type of the request object 
			int requestType=request.getRequestCode();
			Log.e(TAG, "Writing Request Type " + requestType);
			outputStream.writeInt(request.getRequestCode());
			
			// 3. Write the data for the request object
			try {
				Log.e(TAG, "Writing Request " + request.JSON());
			}
			catch(JSONException e) {}
			
			//outputStream.write(dataPacket);
			outputStream.writeUTF(data);
			Log.e(TAG, "Request Written");
			return requestType;

		}
		else {
			Log.e(TAG, "Null request, that's bad!");
			return 0;
		}
	}
	/**
	 * Read a response from the server
	 * @param data the string data for the response (probably JSON)
	 * @return the type of response
	 * @throws IOException
	 * @throws StreamCorruptedException
	 */
	private DataPacket 
	readResponse() 
	throws IOException,StreamCorruptedException{
		DataPacket output;
		Log.e(TAG,"Reading Response");
		// 4. Read the length of the response
		int responseLen = inputStream.readInt();
		Log.e(TAG,"Response Length: " + responseLen);

		// 5. Read the response type 
		int responseType = inputStream.readInt();
		Log.e(TAG,"Response Type: " + responseType);
		// 6. Read the String Data
		//byte[] responsePacket = new byte[responseLen];
		String data = inputStream.readUTF();
		//inputStream.read(responsePacket);
		
		//String data = new String(responsePacket);
				
		// Create data packet
		output = new DataPacket(responseType, data);
		Log.e(TAG,"Read Data: " + output);
		
		return output;
	}
	/**
	 * Determine hat kind of response to have to a request
	 * @param responseType what kind of object the string data describes
	 * @param response the string data of the object
	 * @return a object that extends the Response class
	 * @throws DisconnectException if the server terminates
	 * @throws JSONException if the object data is malformed
	 */
	private ClinicResponse 
	generateResponse(DataPacket dataPacket) 
	throws DisconnectException, JSONException {
		ClinicResponse output = null;
		Log.e(TAG, "Recieved response: " + dataPacket);
		
		// Encode this response to JSON
		JSONObject json = new JSONObject(dataPacket.getData());
		
		
		if (dataPacket.getTypeID() != 0) {
			try {			
				switch(dataPacket.getTypeID()) {
				case ResponseType.LOGIN_FAILURE:
					output = new LoginFailureResponse();
					break;
					
				case ResponseType.LOGIN_SUCCESS:
					output= new LoginSuccessResponse();
					break;
					
				case ResponseType.PATIENT:
					output = new PatientListResponse(json);
					break;
				case ResponseType.CHECKIN_LIST:
					Log.e(TAG,"Patient List recieved: " + json.toString());

					output = new PatientListResponse(json);
					break;
				case ResponseType.ACKNOWLEDGED:
					Log.e(TAG,"KeepAlive Acknowledged");
					break;
				case ResponseType.NOT_AUTHORIZED:
					
					return new NotAuthorizedResponse();
					
				case ResponseType.KILL:
					// TODO: Implement KILL
					throw new RuntimeException("Not Implemented");
				
				case ResponseType.MEASUREMENT_LIST:
					// TODO: Implement MEASURMENT_LIST
					throw new RuntimeException("Not Implemented");
					
				case ResponseType.DEMOGRAPHIC_LIST:
					// TODO: Implement DEMOGRAPHIC_LIST
					throw new RuntimeException("Not Implemented");
				
					
				case ResponseType.MESSAGE_LIST:
					throw new RuntimeException("Not Implemented");
				case ResponseType.PATIENT_MESSAGE_LIST:
					output = new PatientMessageListResponse(json);
					break;
				case ResponseType.MESSAGE_UPDATE:
					output = new MessageListResponse(json);
					break;
					
				case ResponseType.REGISTRATION_FAILURE:
					throw new RuntimeException("Not Able to Register Device...\n" +
								"Please Try Again.");
				case ResponseType.REGISTRATION_SUCCESS:
					//output = new RegistrationSuccessResponse(json);
					break;

				case ResponseType.VITALS:
					return new VitalsResponse(json);
					
				case ResponseType.UNKNOWN_REQUEST:
					return new UnknownRequestResponse();
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
				Log.e(TAG,"Exception " + e.getMessage());
			}
		}
		else {
			Log.e(TAG, "Null response from server: " + output);
			throw new DisconnectException();
		}
		return output;
	}
	
	
//	private SSLSocketFactory getCustomSSLSocketFactory()
//	{
//		// get KeyStore managers and TrustStore managers
//		Log.e(TAG, "creating custom factory");
//		keyStream = this.service.getResources().openRawResource(myFile);
//		TrustManager[] trustManager = getTrustManagers(keyStream, this.service.getResources().openRawResource(myFile), trustStorePassword);
//		//Log.e(TAG, trustManager.toString());
//		
//		keyStream = this.service.getResources().openRawResource(myFile);
//		KeyManager[] keyManager = getKeyManagers(keyStream, keyStorePassword);
//
//		if (trustManager == null) // error
//			Log.e(TAG, "getTrustManager returned null");
//		if (keyManager == null) // error
//			Log.e(TAG, "getKeyManager returned null");
//			
//		try
//		{
////			SSLContext context = SSLContext.getInstance("SSL"); // SSL type does not work?
//			SSLContext context = SSLContext.getInstance("TLS");
//			context.init(keyManager, trustManager, null);
//			
//			SSLSocketFactory ssf = (SSLSocketFactory)context.getSocketFactory();
//			return ssf;
//		}
//		catch (Exception e)
//		{
//			Log.e(TAG, "Exception in getCustomFactory: " + e.getMessage());
//		}
//
//		return null;
//	}
//	private TrustManager[] getTrustManagers(InputStream trustStoreLocation, InputStream keyStoreLocation, String trustStorePassword)
//	{
//		try
//		{
//			TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//			if (trustFactory == null)
//				Log.e(TAG, "trustFactory == null");
//
//			InputStream trustStoreFile = trustStoreLocation; // load TrustStore
//			if (trustStoreFile == null)
//				Log.e(TAG, "trustStoreFile == null");
//			
//			
//			KeyStore key = KeyStore.getInstance(KeyStore.getDefaultType());
//			InputStream keyStoreFile = keyStoreLocation;
//			key.load(keyStoreFile, keyStorePassword.toCharArray());
//			keyStoreFile.close();	
////			KeyStore key = KeyStore.getInstance(KeyStore.getDefaultType()); // type BKS?
////			KeyStore key = KeyStore.getInstance("JKS");
//			Log.e(TAG, "KeyStore type: " + KeyStore.getDefaultType());
//			if (key == null)
//				Log.e(TAG, "key == null");
//			
//			
//			key.load(trustStoreFile, trustStorePassword.toCharArray());
//			
//			trustStoreFile.close(); // close FileIO
//
//			trustFactory.init(key); // initialize TrustManagerFactory
//			return trustFactory.getTrustManagers(); // get TrustStore and return it
//		}
//		catch (NoSuchAlgorithmException e)
//		{
//			Log.e(TAG, "getTrustManager: NoSuchAlgorithmException " + e.getMessage());
//		}
//		catch (IOException e)
//		{
//			Log.e(TAG, "getTrustManager: IOException " + e.getMessage());
//		}
//		catch (java.security.cert.CertificateException e)
//		{
//			Log.e(TAG, "getTrustManager: Certificate " + e.getMessage());
//		}
//		catch (java.security.KeyStoreException e)
//		{
//			Log.e(TAG, "getTrustManager: KeyStore " + e.getMessage());
//		}
//
//
//		return null; // error
//	}
//	private static KeyManager[] getKeyManagers(InputStream keyStoreLocation, String keyStorePassword)
//	{
//		try
//		{
//			KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//			if (keyFactory == null)
//				Log.e(TAG, "keyFactory == null");
//			
//			InputStream keyStoreFile = keyStoreLocation;
//			if (keyStoreFile == null)
//				Log.e(TAG, "keyStoreFile == null");
//			
//			KeyStore key = KeyStore.getInstance(KeyStore.getDefaultType());
////			KeyStore key = KeyStore.getInstance("jks");
//			if (key == null)
//				Log.e(TAG, "key == null");
//			
//			key.load(keyStoreFile, keyStorePassword.toCharArray());
//			
//			keyStoreFile.close();
//
//			keyFactory.init(key, keyStorePassword.toCharArray());
//
//			return keyFactory.getKeyManagers();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		return null;
//	}
}
