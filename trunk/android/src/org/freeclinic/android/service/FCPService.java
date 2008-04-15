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

import java.io.FileNotFoundException;

import org.freeclinic.android.FreeClinicActivity;
import org.freeclinic.android.R;
import org.freeclinic.android.component.activity.ClinicActivity;
import org.freeclinic.common.info.Message;
import org.freeclinic.common.info.Patient;
import org.freeclinic.common.info.PatientMessage;
import org.freeclinic.common.info.Settings;
import org.freeclinic.common.info.Vitals;
import org.freeclinic.common.network.exception.DisconnectException;
import org.freeclinic.common.network.exception.ServerTimeoutException;
import org.freeclinic.common.network.request.AddPatientMessageRequest;
import org.freeclinic.common.network.request.LoginRequest;
import org.freeclinic.common.network.request.LogoutRequest;
import org.freeclinic.common.network.request.MessageUpdateRequest;
import org.freeclinic.common.network.request.PatientMessagesRequest;
import org.freeclinic.common.network.request.PatientRequest;
import org.freeclinic.common.network.request.PutVitalsRequest;
import org.freeclinic.common.network.request.RegistrationRequest;
import org.freeclinic.common.network.request.ShowCheckInsRequest;
import org.freeclinic.common.network.request.VitalsRequest;
import org.freeclinic.common.network.response.AcknowledgedResponse;
import org.freeclinic.common.network.response.LoginFailureResponse;
import org.freeclinic.common.network.response.LoginSuccessResponse;
import org.freeclinic.common.network.response.MessageListResponse;
import org.freeclinic.common.network.response.PatientListResponse;
import org.freeclinic.common.network.response.PatientMessageListResponse;
import org.freeclinic.common.network.response.VitalsResponse;
import org.freeclinic.common.type.ClinicRequest;
import org.freeclinic.common.type.ClinicResponse;
import org.json.JSONException;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

//TODO: Add simple class description.
/**
 * @author Henry Koren, Brian Visser
 *
 */
public class FCPService extends Service implements Runnable {
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}
	
	private ClinicActivity currentClinicActivity;
	private static final String TAG =  "FCPService"; 
	
	private final String host = "freeclinicproject.org"; 
	private final int port = 8064;
//	private final int port = 8066;
	private boolean connected = false;
	private boolean killed = false;
	private boolean isLoggedIn = false;
	private static Client client;
	private Patient currentPatient;
	private static final String settingsFileName="settings.ini";
	public Settings settings;
	
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        public FCPService getService() {
            return FCPService.this;
        }
    }
    
    public Settings getSettings()
    {
    	if (settings==null)
    		try {
    			settings = Settings.readIni(openFileInput(settingsFileName));
    		} catch(FileNotFoundException e) {}
    	
    	if (settings==null)				// Default settings
    		settings = new Settings(port,host);
    	return settings;
    }
    public void setSettings(Settings settings)
    {
    	this.settings = settings;
    }
    
    public boolean saveSettings() {
    	if (settings!=null) {
    		try {
    			return settings.writeIni(openFileOutput(settingsFileName, MODE_PRIVATE));
    		}
    		catch (FileNotFoundException e)
    		{
    			return false;
    		}
    	}
    	else return false;
    }
    
	private Client client() {

		if (client==null)
		{
			// Connect to Client
			Log.e(TAG, "Connecting Client");
			client = new Client(this, host, port);
	    			
			connected = client.connect();
			Log.e(TAG, "Client " + client.toString() + " Connected");
		}
		return client;
	}
    static final int GET_PID_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION;


    private final IBinder mBinder = new LocalBinder();

	/**
	 * Define remote service
	 */
//	private final IBinder mBinder = new LocalBinder() {
		
	public boolean login(String username, String password) {
		ClinicRequest request = new LoginRequest(username, password);
		Log.e(TAG, "Login Request: " + request);
		Client client = client();
		Log.e(TAG, "Logging in to client " + client);
		ClinicResponse result = null;
		boolean success = false;
		try {
			Log.e(TAG, "Sending request " + request);
			result = client.request(request);
			Log.e(TAG, "Client Request successful");
		}
		catch(DisconnectException e) { 
			Log.e(TAG, "Error!  Disconnection Exception!  Reconnecting...");
			
			while (!success)
			{
				try
				{
					client.connect();
					success = true;
				}
				catch(ServerTimeoutException ste)
				{
					// Loop will continue
					try 
					{
						Thread.sleep(60000); // waits a minute after client.connect times out after several tries.
					}
					catch (InterruptedException ie)	{}
				}
			}
//			return login(username, password); // recursive call not used for reconnect attempts anymore
		}
		if (result instanceof LoginSuccessResponse)
		{
			Log.e(TAG, "Login Success");
			return isLoggedIn = true;
		}
		else if (result instanceof LoginFailureResponse)
		{
			Log.e(TAG, "Login Failed");
			return isLoggedIn = false;
		}

		Log.e(TAG, "Result of login is " + result );
		return false;
	}
	
	
	public void logout() {
		Log.e(TAG, "Logging out");
		
		ClinicRequest request = new LogoutRequest();
		Client client = client();
		try {
			Log.e(TAG, "Sending logout request " + request);
			client.request(request);
			Log.e(TAG, "Client logout request successful");
		}
		catch (DisconnectException e)
		{
			Log.e(TAG, "Logout Error");
		}
		
		isLoggedIn = false;
		connected = false;
		
		// go to login screen
	}
	public boolean isLoggedIn()
	{
		return isLoggedIn;
	}
	
	public boolean registrationRequest(String imei, String phone) {
		ClinicRequest request = new RegistrationRequest(imei, phone);
		ClinicResponse result;
		try{
			result = client().request(request);
			if (result != null){
				if(result instanceof AcknowledgedResponse){
					
					return true;
				}else{
					return false;
				}
			}else{
				Log.e(TAG,"Error: Result of list null");
				return false;
			}
			
		}
		catch(DisconnectException e) { 
			Log.e(TAG,"Disconnect!");
			return false;
		}
	}
	/**
	 * Retrieve a list of currently checked in patients from the server 
	 * @return
	 */
	public Patient[] getCheckInList() {
		ShowCheckInsRequest request = new ShowCheckInsRequest();
		ClinicResponse result;
		Patient[] output = null;
		try {
			result = client().request(request);
			if (result != null)
			{
				if (result instanceof PatientListResponse)
				{
					PatientListResponse pr = (PatientListResponse)result;
					output = pr.getArray();
				}
				else{
					Log.e(TAG,"Error: not an instance of patient list");
				}
			}
			else{
				Log.e(TAG,"Error: Result of list null");
			}
		}
		catch(DisconnectException e) { 
			Log.e(TAG,"Disconnect!");
			return null;
		}
		return output;
	}
	public boolean putVitals(int patientID, Vitals vitals)
	{
		ClinicResponse result;
		PutVitalsRequest request = new PutVitalsRequest(patientID, vitals);
		try {
			Log.e(TAG, "Vitals put request" + request.JSON());
		} catch (JSONException e) {}
		try {
			result = client.request(request);
			if (result instanceof AcknowledgedResponse)
			{
				return true;
			}
		}
		catch(DisconnectException e) { 
			Log.e(TAG,"Disconnect!");
		}
		return false;
	}
	public boolean putPatientMessage(int patientID, PatientMessage patientMessage)
	{
		ClinicResponse result;
		AddPatientMessageRequest request = new AddPatientMessageRequest(patientID, patientMessage);
		try {
			Log.e(TAG, "Message put request" + request.JSON());
		} catch (JSONException e) {}
		try {
			result = client.request(request);
			if (result instanceof AcknowledgedResponse)
			{
				return true;
			}
		}
		catch(DisconnectException e) { 
			Log.e(TAG,"Disconnect!");
		}
		return false;
	}

	public Vitals getVitals(int patientID) {
		VitalsRequest request = new VitalsRequest(patientID);
		ClinicResponse result;
		Vitals output = null;
		try {
			result = client().request(request);
			if (result != null)
			{
				if (result instanceof VitalsResponse)
				{
					VitalsResponse vr = (VitalsResponse)result;
					output = vr.getArray()[0];
				}
			}
		}
		catch(DisconnectException e) { 
			Log.e(TAG,"Disconnect!");
			return null;
		}
		return output;
	}
	
	public PatientMessage[] getPatientMessages(int patientId, int messageId){
		PatientMessagesRequest request = new PatientMessagesRequest(patientId, messageId);
		ClinicResponse result;
		PatientMessage[] output = null;
		try {
			result = client().request(request);
			if (result != null)
			{
				if (result instanceof PatientMessageListResponse)
				{
					PatientMessageListResponse pl = (PatientMessageListResponse)result;
					output = pl.getArray();
				}
			}
			else{
				Log.e(TAG, "The result of MessageRequest was null");
			}
		}
		catch(DisconnectException e) { 
			Log.e(TAG,"Disconnect!");
			return null;
		}
		return output;
	}
	
	public PatientMessage[] getPatientMessages(int patientId){
		PatientMessagesRequest request = new PatientMessagesRequest(patientId);
		ClinicResponse result;
		PatientMessage[] output = null;
		try {
			result = client().request(request);
			if (result != null)
			{
				if (result instanceof PatientMessageListResponse)
				{
					PatientMessageListResponse pl = (PatientMessageListResponse)result;
					output = pl.getArray();
				}
			}
			else{
				Log.e(TAG, "The result of MessageRequest was null");
			}
		}
		catch(DisconnectException e) { 
			Log.e(TAG,"Disconnect!");
			return null;
		}
		return output;
	}
	
	public Patient getPatient(int patientID) {
		// Create request for this patient
		PatientRequest request = new PatientRequest(patientID);
		Log.e(TAG,"New Patient Request: " + request);
		ClinicResponse result;
		if (patientID == 0) {
			Log.e(TAG,"Patient zero is not very interesting...");
			return null;
		}
		try {
			// Send request to server
			result = client().request(request);
			if (result != null) {
				
				// Interpret REsponse
				if (result instanceof PatientListResponse)
				{
					// load patient response
					PatientListResponse pr = (PatientListResponse)result;
				
					Patient patient = pr.getArray()[0];
					Log.e(TAG, "Recieved patient " + patient);
					
					// Retrieve the content values from the patient object
					//ContentValues values = patient.getContentValues();
					
					//Uri uri = getContentResolver().insert(Patient.CONTENT_URI, values);
					//Log.e(TAG, "Patient posted: " + patient);
					currentPatient = patient;
					return patient;
				}
				else {
					Log.e(TAG,"Unintelligible response: " + result);
					return null;
				}
			}
			else return null;
		}
		catch(DisconnectException e) { 
			Log.e(TAG,"Disconnect!");
			return null;
		}
	
	}
	public boolean updateMessages() {
		ClinicResponse result = client().request(new MessageUpdateRequest());
		if (result instanceof MessageListResponse) {
			MessageListResponse mur = (MessageListResponse)result;
			ContentResolver cr = this.getContentResolver();
			Message[] messages = mur.getArray();
			for (int i = 0; i < messages.length; i++) {
				Log.e(TAG, messages[i].toString());
				cr.insert(Uri.parse("content://org.freeclinic.provider/message"), messages[i].getValues());
			} 
			return true;
		}
		else
		{
			Log.e(TAG,"unrecognized message type: " + result.getResponseCode() + " result: " + result);
			return false;
		}
		
	}
	
	public boolean storePatient(int patientID) {
		return true;
	}
	public boolean isKilled() {
		return killed;
	}
	public void kill() {
		
	}
	
	public boolean startService() {
		return true;
	}
		
	//};
	
	@Override
	protected void onCreate() {		
		//super.onCreate();
		Log.e(TAG,"Starting Free Clinic Project Service");		
        init();

	}
	
	private void init() {
		// client();
		showNotification();
		getSettings();
		Log.e(TAG,"Settings retrieved: " + settings);
		if (saveSettings())
			Log.e(TAG,"Settings saved!");
		else
			Log.e(TAG,"Settings not saved!");
		
	}

	private void showNotification() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // This is who should be launched if the user selects our persistent
        // notification.
        Intent intent = new Intent(Intent.MAIN_ACTION);
        intent.setClassName("org.freeclinic.android","FreeClinic");
		
        // Display a notification about us starting.
        Toast.makeText(this, getText(R.string.clinic_service_label), Toast.LENGTH_SHORT);
/*        mNM.notifyWithText(R.string.clinic_service_started,
                   getText(R.string.clinic_service_started),
                   NotificationManager.LENGTH_SHORT,
                   new Notification(
                       R.drawable.cadu,
                       getText(R.string.clinic_service_label),
                       intent,
                       null, 
                       null));*/
	}
	
	public void showDisconnection() {
        

        // This is who should be launched if the user selects our persistent
        // notification.
        Intent intent = new Intent(Intent.MAIN_ACTION);
        intent.setClass(this, FreeClinicActivity.class);
		
        // Display a notification about us starting.
        Toast.makeText(this, R.string.clinic_service_disconnected, Toast.LENGTH_SHORT);
        /*mNM.notifyWithText(R.string.clinic_service_disconnected,
                   getText(R.string.clinic_service_disconnected),
                   NotificationManager.LENGTH_SHORT,
                   new Notification(
                       R.drawable.cadu_discon,
                       getText(R.string.clinic_service_label),
                       intent,
                       null, 
                       null));*/
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onStart(int startId, Bundle arguments) {
		// TODO Auto-generated method stub
		super.onStart(startId, arguments);
		
	}

    private NotificationManager mNM;
    
    public void run() {
        // This class binds to the Android
    //	xmppDataSender = XmppDataSender.getXmppDataSender(this);
    }
    public void setClinicActivity(ClinicActivity clinicActivity) {
    	currentClinicActivity = clinicActivity;
    }
    public ClinicActivity getClinicActivity() {
    	return currentClinicActivity;
    }
}
