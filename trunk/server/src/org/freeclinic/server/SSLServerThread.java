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
package org.freeclinic.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.net.ssl.SSLSocket;

import org.freeclinic.common.info.NotAuthorizedException;
import org.freeclinic.common.info.Patient;
import org.freeclinic.common.info.Vitals;
import org.freeclinic.common.network.request.LoginRequest;
import org.freeclinic.common.network.request.MessageSearchRequest;
import org.freeclinic.common.network.request.MessageUpdateRequest;
import org.freeclinic.common.network.request.PatientMessagesRequest;
import org.freeclinic.common.network.request.PatientRequest;
import org.freeclinic.common.network.request.PutVitalsRequest;
import org.freeclinic.common.network.request.RegistrationRequest;
import org.freeclinic.common.network.request.RequestType;
import org.freeclinic.common.network.request.VitalsRequest;
import org.freeclinic.common.network.response.AcknowledgedResponse;
import org.freeclinic.common.network.response.LoginFailureResponse;
import org.freeclinic.common.network.response.LoginSuccessResponse;
import org.freeclinic.common.network.response.MessageListResponse;
import org.freeclinic.common.network.response.NotAuthorizedResponse;
import org.freeclinic.common.network.response.PatientListResponse;
import org.freeclinic.common.network.response.PatientMessageListResponse;
import org.freeclinic.common.network.response.UnknownRequestResponse;
import org.freeclinic.common.network.response.VitalsResponse;
import org.freeclinic.common.type.ClinicRequest;
import org.freeclinic.common.type.ClinicResponse;
import org.freeclinic.common.type.DataPacket;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Brian Visser, Cameron Esfahani, Henry Koren
 * 
 * Receive requests from a client over a secured socket return a response over the connection
 */
public class SSLServerThread implements Runnable
{
	private SSLSocket socket;
	DataOutputStream outputStream;
	DataInputStream inputStream;
	Database data;
	private String ip;
	private Server server;
	private boolean authorized = false;
	
	// Constructors
	
	public SSLServerThread()
	{
		init();
		socket = null;
	}

	public SSLServerThread(SSLSocket socket, Server server)
	{
		init();
		this.socket = socket;
		this.server = server;
		ip = socket.getInetAddress().toString();
	}
	
	/**
	 * Write a message to the log
	 * @param error string containing the error/debug info
	 */
	private void debug(String error) {
		String log="[" + ip + " - " + server.getTimeTag() + "] " + error + "\n";
		server.log(log);
		System.err.println(log);
	}

	/**
	 * Initialize the connection
	 */
	private void init() {
		data = new Database();
	}
	
	
	/**
	 * Main server connection loop.  This receives are request from the socket and interprets a response to it.
	 */
	@Override
	public void run()
	{
		//ObjectOutputStream objectOutputStream = null;
		outputStream = null;
		inputStream = null;

		/*
		 * ObjectOutputStream must be created before ObjectInputStream
		 * because the ObjectInputStream constructor blocks while reading
		 * a header from the stream source.
		 */

		ClinicRequest request = null;
		ClinicResponse result = null;
		
		DataPacket requestDataPacket = new DataPacket(0,"");
		
		try {
			
//			java.security.cert.Certificate[] serverCerts = socket.getSession().getPeerCertificates();
//			System.err.println(serverCerts.toString());
			
			//objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());
			inputStream = new DataInputStream(socket.getInputStream());
                        debug("Streams Connected");
 			
		}
		catch (IOException e)
		{
			debug("Stream Connection IO Exception: " + e.getMessage());
			e.printStackTrace();
		}
		// Loop on requests coming from the server
		while (requestDataPacket != null)
		{
			
			// Read the request from the client
			requestDataPacket = readRequest();
			
			// Handle the request
			result = handleRequest(requestDataPacket);
			
			// If no result has been set, the request must be unknown
			if (result == null && requestDataPacket != null) {
				result = new UnknownRequestResponse();
				debug("Unknown Request: " + requestDataPacket);
			}

			// Now send the response to the client
			outputResponse(result);
		}
		debug("E");
		
		//inputStream.close();
		//outputStream.flush();
		//outputStream.close();
	}
	/**
	 * Read an object in from the stream
	 * @param data string data for object (usually json)
	 * @return type of response (ResponseType)
	 */
	private DataPacket readRequest()
	{
		DataPacket output = null;
		int requestType=0;
		try {

			// 1. Read request length integer from the input stream
			int len = inputStream.readInt();
			debug("Length: " + len +  " Read.  Reading type.");

			// 2. Read request type integer from the input stream
			requestType = inputStream.readInt();
			debug("Type: " + requestType );
			
			// 3. Read the object string (probably json)
			String object = inputStream.readUTF();
			//byte[] requestData = new byte[len]; 
			//inputStream.read(requestData);
			
			output = new DataPacket(requestType, object);
			
		}
		catch (IOException e) {
			debug("Read IO exception: " + e.getMessage());
		}
		return output;
	}
	
	/**
	 * generate a Response given the request data
	 * 
	 * @param requestType integer type of request
	 * @param object string data for object (usually json)
	 * @return Response object (extends Response)
	 */
	private ClinicResponse handleRequest(DataPacket dataPacket) {
		ClinicResponse output = null;
		JSONObject json = null;
		debug("Parsing: " + dataPacket);
		// Convert the string data to json
		if (dataPacket == null) {
			debug("Null Data Packet");
			return null;
		}
		else
		{
			// Encode the Json object which will be turned into whatever kind of request it is
			try {
				json = new JSONObject(dataPacket.getData()); 
			} catch(JSONException e) {
				debug("Json Exception: " + e.getMessage());
				e.printStackTrace();
			}
			
			// First make sure they login before they can do anything else
			if (!authorized && dataPacket.getTypeID() != RequestType.LOGIN && false)
				output = new NotAuthorizedResponse();
			else
			{				// based on type of request, generate a different response object
				switch(dataPacket.getTypeID()) {
					case RequestType.LOGIN:
						output = authorize(json);
						break;
					case RequestType.KEEP_ALIVE:
						output = new AcknowledgedResponse();
						break;
					case RequestType.GET_PATIENT:
						output = getPatient(json);
						break;
					case RequestType.GET_PATIENT_MESSAGES:
						output = getPatientMessages(json);
						break;
					case RequestType.LOGOUT:
					case RequestType.CLOSE:
					case RequestType.DBUPDATE:
					case RequestType.PERSON:
					case RequestType.MESSAGE_SEARCH:
						output = messageSearch(json);
					case RequestType.MESSAGE_UPDATE:
						output = messageUpdate(json);
						break;
					case RequestType.SHOW_CHECKINS:
						output = new PatientListResponse(data.getCheckedInPatients());
						break;
					case RequestType.PUT_VITALS:
						output = putVitals(json);
						break;
					case RequestType.GET_VITALS:
						output = getVitals(json);
						break;
					case RequestType.REGISTRATION:
						output = registerDevice(json);
						break;
					default:
						output = new UnknownRequestResponse();
						break;
					}
				}
			return output;
		}
	}
	
	/**
	 * Write a response object back to the client over the socket connection
	 * @param response the object to return to the client
	 */
	private int outputResponse(ClinicResponse response)
	{

		if (response != null)
		{
			//byte[] outputPacket = null;
			String json = null;
			try {
				// OUTPUT THE RESPONSE
				//outputPacket = (response.JSON().toString()).getBytes();
				json = response.JSON().toString();
			}
			catch (JSONException e)
			{
				debug("JSON Exception: " + e.getMessage());
			}
			if (json != null)
			{
				try {
					int responseLen = json.length();
					outputStream.writeInt(responseLen);
					outputStream.writeInt(response.getResponseCode());
					//outputStream.write(outputPacket);
					//outputStream.flush();
					outputStream.writeUTF(json);
					debug("Responding Code: " + response.getResponseCode() + " Length: " + responseLen + " Object: " + json);
					return 1;
				}
				catch (java.net.SocketException e) {
					debug("Socket exception from " + this);
					throw new RuntimeException(e);
				}
				catch (java.io.EOFException e)
				{
					debug("Unexpected Disconnect");
					e.printStackTrace();
				}
				catch (java.io.IOException e)
				{
					debug("IO Exception handling output: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		else
		{
			debug("NULL RESPONSE!!!");
		}
		return 0;
	}
	
	/**
	 * Authorize the user
	 */
	public ClinicResponse authorize(JSONObject json) {
		ClinicResponse output;
		LoginRequest lr = null;
		try {
			lr = new LoginRequest(json);
		}
		catch(JSONException e) {
			debug("JSON exception: " + e.getMessage());
		}
		if (data.login(lr))
		{																
			authorized = true;												// Successful Login
			output = new LoginSuccessResponse();							
		}
		else
		{				// Login Failure
			authorized = false;
			output = new LoginFailureResponse();
		}
		return output;
	}
	/**
	 * 
	 * @param json
	 * @return
	 */
	private ClinicResponse registerDevice(JSONObject json){
		ClinicResponse output = null;
		RegistrationRequest rr;
		try{
			rr = new RegistrationRequest(json);
			if (data.deviceRegistration(rr))
			{
				output = new AcknowledgedResponse();
			}
			else
			{
				output = new NotAuthorizedResponse();
			}
		}
		catch(JSONException e) {
			debug("JSON exception: " + e.getMessage());
		}
		return output;
	}
	
	
	/**
	 * Get the patient
	 */
	private ClinicResponse getPatient(JSONObject json) {
		ClinicResponse output = null;
		PatientRequest pr;
		try {
			pr = new PatientRequest(json);
			Patient p = data.getPatient(pr.getPatientID());
			Patient[] parr = new Patient[1];
			parr[0] = p;
			output = new PatientListResponse(parr);
		}
		catch(JSONException e) {
			debug("JSON exception: " + e.getMessage());
		}
		catch (NotAuthorizedException e)
		{
			debug("Not Authorized" + e.getMessage());
			e.printStackTrace();
			output = new NotAuthorizedResponse();
		}
		return output;
	}
	/**
	 * Put Vitals
	 */
	private ClinicResponse putVitals(JSONObject json) {
		ClinicResponse output = null;
		PutVitalsRequest pvr;
		try {
			pvr = new PutVitalsRequest(json);
			pvr.getArray();
			output = new AcknowledgedResponse();
		}
		catch(JSONException e) {
			debug("JSON exception: " + e.getMessage());
		}
		return output;
	}
	
	/**
	 * Get Vitals
	 */
	private ClinicResponse getVitals(JSONObject json) {
		ClinicResponse output = null;
		VitalsRequest vr;
		try {
			// TODO: Fix this
			// vr = new VitalsRequest(json);
			vr = new VitalsRequest(0);
			int patientID = vr.getPatientID();
			Vitals v = data.getVitals(patientID);
			debug("Vitals: " + v);
			output = new VitalsResponse(v);
		}
		catch(Exception e) {
		//catch(JSONException e) {
			debug("JSON exception: " + e);
		}
		return output;
	}
	/**
	 * Update Messages
	 */
	private ClinicResponse messageUpdate(JSONObject json) {
		ClinicResponse output = null;
		MessageUpdateRequest mur = null;
		try {
			mur = new MessageUpdateRequest(json);							
		}
		catch (JSONException e)
		{
			debug("JSON exception: " + e);
		}
		if (mur != null) {
			output = new MessageListResponse(data.getMessages(mur.getLastUpdate()));
		}
		return output;
	}
	private ClinicResponse messageSearch(JSONObject json) {
		ClinicResponse output = null;
		MessageSearchRequest msr = null;
		try {
			msr = new MessageSearchRequest(json);
			output = new MessageListResponse(data.getMessages(msr));
		}
		catch (JSONException e) {
			
		}
		return output;
	}
	private ClinicResponse getPatientMessages(JSONObject json) {
		ClinicResponse output = null;
		PatientMessagesRequest pmr = null;
		try {
			pmr = new PatientMessagesRequest(json);
			output = new PatientMessageListResponse(data.getPatientMessages(pmr));
		}
		catch (JSONException e) {
			
		}
		return output;
	}
	
}
