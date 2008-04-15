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
package org.freeclinic.android.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.freeclinic.android.component.activity.ClinicActivity;
import org.freeclinic.common.network.exception.ClinicException;
import org.freeclinic.common.network.exception.DisconnectException;
import org.freeclinic.common.network.exception.ServerTimeoutException;
import org.freeclinic.common.network.request.KeepAliveRequest;
import org.freeclinic.common.network.request.LoginRequest;
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

import android.util.Log;

/**
 * @author Brian Visser, Henry Koren, Brent Longstaff
 *
 */
public class Client
{
	private static final String TAG="Client";
	private int port = 8064;

	private String host = "freeclinicproject.org";
	private static final long KEEPALIVE_INTERVAL = 600000;	// Keepalives every 10 minutes
	private FCPService service;
	private int tries;
	private int interval;
	private Timer timer = null;
	private int minRetry = 5;
	private int maxRetry = 10;
	private boolean connSuccess = false;
	private LoginRequest loginRequest = null;

	private	SSLSocket socket;
	private BufferedOutputStream outputStream; 
	private BufferedInputStream inputStream;

	/**
	 * Function for user the change the host and port
	 * 
	 * @param	newHost	Host specified by the user
	 * @param	newPort	Port specified by the user
	 */
	public Client(FCPService context, String newHost, int newPort)
	{
		this.service = context;
		port = newPort;
		host = newHost;
		
		interval = 10000;
		//context.showAlert("Connecting", "we are connecting", "ok", true);
	}
//	private static boolean cancel = false;
//	public static void cancelConnect()
//	{
//		cancel = true;
//	}
	/**
	 * Function to connect via SSL Socket to the specified server.
	 * Creates ObjectOutputStream to stream request objects over the
	 * network.
	 * <br>
	 * <b>Note</b>: ObjectOutputStream must be created before ObjectInputStream
	 * because the ObjectInputStream constructor blocks while reading
	 * a header from the stream source.
	 */
	public boolean connect()
	{
//		cancel = false;
		ClinicActivity activity = service.getClinicActivity();
//		activity.findViewById(id);
		
		connSuccess = false;
		while (!connSuccess)// && !cancel)
		{
			try
			{
				// Start timer if it hasn't started
				if (timer == null)
				{
					Log.d(TAG, "Starting timer");
					timer = new Timer(true);
					TimerTask task = new KeepAlive();
					timer.scheduleAtFixedRate(task, 0, KEEPALIVE_INTERVAL);
				}


				// end timer code

				Log.d(TAG, "Attempting Connection");
				Timer socketTimer = new Timer(true);
//				timer.scheduleAtFixedRate(socketTask, 0, 5000);
				

				
    			Log.d(TAG, "Creating new TrustManager");
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
    			Log.d(TAG, "SSLSocketFactory creation finished");

    			socket = (SSLSocket)sslsocketfactory.createSocket(host, port);
    			Log.d(TAG, "Socket creation finished");				
				
//				socket = SocketFactory.getDefault().createSocket(host, port); // non-ssl default connection

				connSuccess = true;
				Log.d(TAG, "Made connection");
			}
    		catch (NoSuchAlgorithmException e)
    		{
    			connSuccess = false;
    			
    			Log.e(TAG, e.getMessage());
    			throw(new RuntimeException(e));
    		}
    		catch (KeyManagementException e)
    		{
    			connSuccess = false;
    			
    			Log.e(TAG, e.getMessage());
    			throw(new RuntimeException(e));
    		}
			catch (java.net.ConnectException e)
			{
				connSuccess = false;
				
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
					connSuccess = false;
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
				connSuccess = false;
				
				Log.e(TAG, "SSL Socket Connection Error");
				throw(new RuntimeException(e));
			}
			catch (IOException e)
			{
				connSuccess = false;
				
				Log.e(TAG, "Socket IO Error: " + e.getMessage());
				throw(new RuntimeException(e));
			}

			
		}
		try {
			/*
			 * ObjectOutputStream must be created before ObjectInputStream
			 * because the ObjectInputStream constructor blocks while reading
			 * a header from the stream source.
			 */
			outputStream = new BufferedOutputStream(socket.getOutputStream(), 16000);
			inputStream = new BufferedInputStream(socket.getInputStream(), 16000);
//			objectInputStream = new ObjectInputStream(socket.getInputStream());

			Log.d(TAG, "Connected To Server");
			return true;

		}	
		catch (java.io.EOFException e)
		{
			connSuccess = false;
			
			Log.e(TAG, "Unexpected EOF: " + e.getMessage());
			throw(new RuntimeException(e));
		}
		catch (java.io.IOException e)
		{
			connSuccess = false;
			
			Log.e(TAG, "Stream IO Error: " + e.getMessage());
			throw(new RuntimeException(e));
		}
		
		
	}
	/**
	 * Send a request to the server
	 * @param request the object that extends type Request you wish to send
	 * @return the response object that the server generates
	 * @throws DisconnectException if the server connection is severed
	 */
	public ClinicResponse request(ClinicRequest request) throws DisconnectException {
		DataPacket responsePacket = null;
		Log.d(TAG, "Request called");
		if (socket == null || !socket.isConnected())
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
			catch (ClinicException e) {
				
			}
			catch (StreamCorruptedException e) {
				Log.e(TAG, "Stream Corrupted: " + e.getMessage());
			}
			catch (IOException e) {
				Log.e(TAG, "IO Exception: " + e.getMessage());
				connect();
			}
		}
		// Now figure out what the response is and generate the appropriate object
		ClinicResponse output = null;
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
			Log.d(TAG, "Writing Request Length " + requestLen);
			// (I know, I know, they should be bitwise and's... I'll get to it)
			outputStream.write(requestLen >> 24);
			outputStream.write((requestLen << 8) >> 24);
			outputStream.write((requestLen << 16) >> 24);
			outputStream.write((requestLen << 24) >> 24);
			// 2. Write the type of the request object 
			int requestType = request.getRequestCode();
			Log.d(TAG, "Writing Request Type " + requestType);
			outputStream.write(request.getRequestCode());
			
			// 3. Write the data for the request object
			try {
				Log.d(TAG, "Writing Request " + request.JSON());
			}
			catch(JSONException e)
			{
				Log.e(TAG, e.getMessage());
			}
			
			//outputStream.write(dataPacket);
			byte buffer[] = new byte[data.length()];
			for(int i = 0; i < data.length(); i++) {
				buffer[i] = (byte)(data.charAt(i));
			}
			outputStream.write(buffer);
			outputStream.flush();
			Log.d(TAG, "Request Written");
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
		Log.d(TAG,"Reading Response");
		// 4. Read the length of the response
		int responseLen = 0;
		responseLen |= inputStream.read();
		responseLen <<= 8;
		responseLen |= inputStream.read() << 16;
		responseLen <<= 8;
		responseLen |= inputStream.read() << 8;
		responseLen <<= 8;
		responseLen |= inputStream.read();
		Log.d(TAG,"Response Length: " + responseLen);

		// 5. Read the response type 
		int responseType = inputStream.read();
		Log.d(TAG,"Response Type: " + responseType);
		// 6. Read the String Data
		//byte[] responsePacket = new byte[responseLen];
		int byteCount = inputStream.available();
		byte[] buffer = new byte[byteCount];
		if(inputStream.read(buffer) != byteCount)
			Log.e(TAG, "Buffer overflow occured during transaction with Server.");
		String data = "";
		for(int i = 0; i < byteCount; i++) {
			data += (char) buffer[i];
		}
		//inputStream.read(responsePacket);
		
		//String data = new String(responsePacket);
				
		// Create data packet
		output = new DataPacket(responseType, data);
		Log.d(TAG,"Read Data: " + output);
		
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
		Log.d(TAG, "Recieved response: " + dataPacket);
		
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
					Log.d(TAG,"KeepAlive Acknowledged");
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
					Log.e(TAG, "Received Message List");
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
				throw new RuntimeException("Client Object " + dataPacket.getTypeID() + " failed to decode " + e + " - ");
			}
		}
		else {
			Log.e(TAG, "Null response from server: " + output);
			throw new DisconnectException();
		}
		return output;
	}
	
	public String toString() {
		return "Connection to " + this.host + ":" + Integer.toString(this.port);
	}
	
	private class KeepAlive extends TimerTask {
    	private static final String TAG =  "Keep Alive Timer 1";
    	private static final String host = "freeclinicproject.org";
    	private static final int port = 8064;
    	private KeepAliveRequest req;

    	public KeepAlive()
    	{
    		super();
    		req = new KeepAliveRequest();
    	}
    	
    	@Override
    	public void run() {
    		// Do a request to make sure the connection is still active
    		Log.e(TAG, "Keep Alive: Tick!");
    		request(req);
    	}
    }
}
