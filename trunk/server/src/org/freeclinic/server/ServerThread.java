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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.net.ssl.SSLSocket;

import org.freeclinic.common.info.NotAuthorizedException;
import org.freeclinic.common.info.Patient;
import org.freeclinic.common.info.Vitals;
import org.freeclinic.common.network.request.AddPatientMessageRequest;
import org.freeclinic.common.network.request.LoginRequest;
import org.freeclinic.common.network.request.MessageSearchRequest;
import org.freeclinic.common.network.request.MessageUpdateRequest;
import org.freeclinic.common.network.request.PatientMeasurementsRequest;
import org.freeclinic.common.network.request.PatientMessagesRequest;
import org.freeclinic.common.network.request.PatientRequest;
import org.freeclinic.common.network.request.PutVitalsRequest;
import org.freeclinic.common.network.request.RegistrationRequest;
import org.freeclinic.common.network.request.RequestType;
import org.freeclinic.common.network.request.VitalsRequest;
import org.freeclinic.common.network.response.AcknowledgedResponse;
import org.freeclinic.common.network.response.LoginFailureResponse;
import org.freeclinic.common.network.response.LoginSuccessResponse;
import org.freeclinic.common.network.response.MeasurementListResponse;
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
public class ServerThread implements Runnable
{
	private SSLSocket socket; 
	private BufferedOutputStream outputStream;
	private BufferedInputStream inputStream;
	private Database data;
	private String ip;
	private Server server;
	private boolean authorized = false;
	private boolean done = false;
	
	// Constructors
	
	public ServerThread()
	{
		init();
		socket = null;
		done = false;
	}

	public ServerThread(SSLSocket socket, Server server)
	{
		init();
		this.socket = socket;
		this.server = server;
		ip = socket.getInetAddress().toString();
	}
	
	/**
	 * Write a message to the server log
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
	 * Main server connection loop.  This receives are request from the socket and
	 * interprets a response to it.
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
			outputStream = new BufferedOutputStream(socket.getOutputStream(), 16000);
			inputStream = new BufferedInputStream(socket.getInputStream(), 16000);            
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
			System.out.println("Response: " + result.getResponseCode());
			// Now send the response to the client
			outputResponse(result);

			if (done) // LOGOFF
			{
				done = false;
				return;
			}
		
		}
		debug("Closing thread");
		
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
			int len = 0;
			len |= inputStream.read();
			len <<= 8;
			len |= inputStream.read() << 16;
			len <<= 8;
			len |= inputStream.read() << 8;
			len <<= 8;
			len |= inputStream.read();
			debug("Length: " + len +  " Read.  Reading type.");

			// 2. Read request type integer from the input stream
			requestType = inputStream.read();
			debug("Type: " + requestType );
			
			// 3. Read the object string (probably json)
			//String object = inputStream.read();
			//byte[] requestData = new byte[len]; 
			//inputStream.read(requestData);
			int byteCount = inputStream.available();
			byte[] buffer = new byte[byteCount];
			if(inputStream.read(buffer) != byteCount)
				debug("Buffer overflow occured during transaction with Client.");
			String data = "";
			for(int i = 0; i < byteCount; i++) {
				data += (char) buffer[i];
			}

			output = new DataPacket(requestType, data);
			
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
			debug("here");
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
				debug("handling response code: " + dataPacket.getTypeID());
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
					case RequestType.ADD_PATIENT_MESSAGE:
						output = addPatientMessage(json);
						break; 
					case RequestType.GET_PATIENT_MEASUREMENTS:
						output = getPatientMeasurements(json);
						break;
					case RequestType.LOGOUT:
						done = true; // signal done thread
						output = new AcknowledgedResponse();
						break;
					case RequestType.CLOSE:
						break;
					case RequestType.DBUPDATE:
						break;
					case RequestType.PERSON:
						break;
					case RequestType.MESSAGE_SEARCH:
						output = messageSearch(json);
						break;
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
			String data = null;
			try {
				// OUTPUT THE RESPONSE
				//outputPacket = (response.JSON().toString()).getBytes();
				data = response.JSON().toString();
			}
			catch (JSONException e)
			{
				debug("JSON Exception: " + e.getMessage());
			}
			if (data != null)
			{
				try {
					int responseLen = data.length();
					outputStream.write(responseLen >> 24);
					outputStream.write((responseLen << 8) >> 24);
					outputStream.write((responseLen << 16) >> 24);
					outputStream.write((responseLen << 24) >> 24);
					outputStream.write(response.getResponseCode());
					//outputStream.write(outputPacket);
					//outputStream.flush();
					//outputStream.write(data);
					byte buffer[] = new byte[data.length()];
					for(int i = 0; i < data.length(); i++) {
						buffer[i] = (byte)(data.charAt(i));
					}
					outputStream.write(buffer);
					outputStream.flush();
					debug("Responding Code: " + response.getResponseCode() + " Length: " + responseLen + " Object: " + data);
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
	 * Put Message
	 */
	private ClinicResponse addPatientMessage(JSONObject json) {
		ClinicResponse output = null;
		AddPatientMessageRequest req;
		try {
			req = new AddPatientMessageRequest(json);
			data.putPatientMessage(req);
			output = new AcknowledgedResponse();
		}
		catch(JSONException e) {
			throw new MalformedRequestException("JSON exception", e);
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
			data.putVitals(pvr);
			output = new AcknowledgedResponse();
		}
		catch(JSONException e) {
			throw new MalformedRequestException("JSON exception", e);
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
			vr = new VitalsRequest(json);
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
	private ClinicResponse getPatientMeasurements(JSONObject json) {
		ClinicResponse output = null;
		PatientMeasurementsRequest pmr = null;
		try {
			pmr = new PatientMeasurementsRequest(json);
			output = new MeasurementListResponse(data.getPatientMeasurements(pmr));
		}
		catch (JSONException e) {
			
		}
		return output;
	}
	
}
