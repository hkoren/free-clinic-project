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

import org.freeclinic.android.R;
import org.freeclinic.android.component.connection.ClinicConnection;
import org.freeclinic.android.component.global.PatientMenu;
import org.freeclinic.common.info.Message;
import org.freeclinic.common.info.Patient;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class PatientActivity extends ClinicActivity {
	private static final String TAG = "PatientActivity";
	private static final int STARTPATIENTID=1;
	private static final int FIRSTNAME_INDEX = 1;
	private static final int MIDDLENAME_INDEX = 2;
	private static final int LASTNAME_INDEX = 1;
	// Menu options
	private static final int REFRESH = 1;
	private static final int DETAILS = 2;
	
	//Content Provider
	private static final String CONTENT_PROVIDER = "content://org.freeclinic.Message/add/";
	
	private Patient patient;
	private PatientMenu patientMenu;
	private Cursor mCursor;
	private Uri mURI;
	private int patientID;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		patientMenu = new PatientMenu(this);
		// Start by telling them we're loading the record;
		informLoading();
		
		// Fire up service 
		final Intent intent = getIntent();
		mURI = intent.getData();
		Log.e(TAG, "Got URI " + mURI);
		
		// Parse the patient ID out of the URI
		patientID = Integer.parseInt(mURI.getPathSegments().get(1));
		
		// Load the record
		loadRecord();

	}

	@Override
	public void onContentChanged() {
		// TODO Auto-generated method stub
		super.onContentChanged();
	}

	public PatientActivity() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.e(TAG, "Starting Patient Activity!");
		informLoading();
	}

	public void displayView() {
		
	}
	
	/**
	 * Create Menu for Patient
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		patientMenu.onCreateOptionsMenu(menu);
		menu.add(0,REFRESH,R.string.patient_refresh);
		menu.add(0,DETAILS,R.string.details);
		return super.onCreateOptionsMenu(menu);
	}
	
	  /**
     * Called when a menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(Menu.Item item) {
    	super.onOptionsItemSelected(item);
    	patientMenu.onOptionsItemSelected(item);
        switch (item.getId()) {
        case REFRESH:
            loadRecord();
            return true;   
        case DETAILS:
        	this.showDetails();
        }
        return super.onOptionsItemSelected(item);
    }  /**
     * Called when a menu item is selected.
     */

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "Resuming patient " + patient);
		//Patient patient = null;
		/*if (mCursor != null) {
			
			mCursor.first();
//			patient = new Patient(mCursor);
			Log.e(TAG, "Got Patient data named " + mCursor.getString(FIRSTNAME_INDEX));
		}*/
		// Start by telling them we're loading the record;
		//informLoading();
		
		
		// Load the record
		loadRecord();

	}
	
	/**
	 * Inform user of loading of patient record
	 */
	private void informLoading() {
		setContentView(R.layout.patient_loading);
	}
	
	/**
	 * Retrieve the patient from the service
	 */
	private void loadRecord() {
		// Tell the service to update data
		ClinicConnection.serviceMethod(this, new PatientConnection(this));
	}
	
	
	/**
	 * Retrieve a patient object from the server by utilizing a ServiceConnection to 
	 * the FCPService
	 * 
	 * @author Henry
	 *
	 */
//    private class PatientConnection implements ServiceConnection {
//        public void onServiceConnected(ComponentName className, IBinder iservice)
//        {
//        	//public PatientConnection(int patientID)
//        	Log.e("ServiceConnection", "Service Connected. Logging in...");
//        	service = bindService(iservice);
//        	Patient patient = service.getPatient(patientID);
//    		if(patient != null) {
//    			// Now patient has been retrieved
//    			
//    			Log.e(TAG, "Now loading patient..." + patient);
//    			// Connect to content provider
//    			showPatient(patient);	
//    		}
//    		else {
//    			patientCannotBeLoaded();
//    		}
//        }
//
//        public void onServiceDisconnected(ComponentName className)
//        {
//
//        }
//    };
    private class PatientConnection extends ClinicConnection {
    	public PatientConnection(Context context) {
    		super(context);
    	}
        public void action()
        {
        	//public PatientConnection(int patientID)
        	Log.e(TAG, "Service Connected. Logging in...");
        	
        	Patient patient = service.getPatient(patientID);
    		if(patient != null) {
    			// Now patient has been retrieved
    			
    			Log.e(TAG, "Now loading patient..." + patient);
    			// Connect to content provider
    			showPatient(patient);	
    		}
    		else {
    			patientCannotBeLoaded();
    		}
        }
    };
    /**
     * Inform the user that the patient record cannot be loaded 
     */
    private void patientCannotBeLoaded() {
    	
    }

	/**
	 * Display a patient
	 * @param patient
	 */
	public void showPatient(Patient patient)
	{
		Button prescribe,refer,treat,note,diagnose,test;
		Log.e(TAG,"Showing Patient " + patient);
		this.setContentView(R.layout.patient);
		//this.setContentView(new PatientView(this, patient));
		
		// Name
		TextView name = (TextView)findViewById(R.id.name);
		name.setText(patient.getName());
		// Subtitle
		String basicInfo = "";
		float age = patient.getAge();
		if (age != -1)
			basicInfo += age + " year old ";
		basicInfo += patient.getGender();
		if (age == -1)
			basicInfo += ", age unknown";
		TextView subtitle = (TextView)findViewById(R.id.subtitle);
		subtitle.setText(basicInfo);
		toast(basicInfo);
		
		TextView complaint = (TextView)findViewById(R.id.complaint);
		complaint.setText("Complaint: " + patient.getComplaint());
		Log.e(TAG,"Complaint: " + patient.getComplaint());
		
		Button historyButton = (Button)findViewById(R.id.history_button);
		historyButton.setOnClickListener(new HistoryOnClickListener());
		
		Button detailsButton = (Button)findViewById(R.id.details);
		detailsButton.setOnClickListener(new DetailsOnCLickListener());
		
		Button visualizeButton = (Button)findViewById(R.id.visualize_button);
		visualizeButton.setOnClickListener(new VisualizeOnClickListener());
		
		// Allergies
		Button allergiesButton = (Button)findViewById(R.id.allergies_button);
		allergiesButton.setOnClickListener(new AllergiesOnClickListener());
		
		// Vitals
		Button vitalsButton = (Button)findViewById(R.id.vitals_button);
		vitalsButton.setText("Collect Vitals");
		vitalsButton.setOnClickListener(new VitalsOnClickListener());
		
		// Allergies
		
		allergiesButton = (Button)findViewById(R.id.allergies_button);
		allergiesButton.setText("Allergies");
		allergiesButton.setOnClickListener(new AllergiesOnClickListener());
		
		//Set up message buttons
		prescribe = initMessageButton(R.id.prescribe);
		refer     = initMessageButton(R.id.refer);
		treat     = initMessageButton(R.id.treat);
		note      = initMessageButton(R.id.note);
		diagnose  = initMessageButton(R.id.diagnose);
		//treat     = initMessageButton(R.id.test);
		
		// Message Add
		//Button addButton = (Button)findViewById(R.id.add);
		//addButton.setOnClickListener(new AddOnClickListener());
		
		// Exit
		Button exitButton = (Button)findViewById(R.id.exit);
		exitButton.setOnClickListener(new ExitOnClickListener());	
	}
	
	private Button initMessageButton(int id) {
		Button b = (Button)findViewById(id);
		b.setOnClickListener(new AddMessageOnClickListener(id));
		return b;
	}
	
	private void messageAdd(int type) {
		switch(type) {
		case R.id.prescribe:
			startSubActivity(patientMessageIntent(this, Message.Type.PRESCRIPTION, patientID), 0);
			break;
		case R.id.refer:
			startSubActivity(patientMessageIntent(this, Message.Type.REFERRAL, patientID), 0);
			break;
		case R.id.treat:
			startSubActivity(patientMessageIntent(this, Message.Type.TREATMENT, patientID), 0);
			break;
		case R.id.note:
			startSubActivity(patientMessageIntent(this, Message.Type.NOTATION, patientID), 0);
			break;
		case R.id.diagnose:
			startSubActivity(patientMessageIntent(this, Message.Type.DIAGNOSIS, patientID), 0);
			break;
		case R.id.test:
			startSubActivity(patientMessageIntent(this, Message.Type.LAB_TEST, patientID), 0);
			break;
		}
		
	}
	public static Intent patientMessageIntent(Context context, int patientID, int messageID) {
		Intent intent = new Intent(Intent.VIEW_ACTION);
		intent.addLaunchFlags(Intent.NEW_TASK_LAUNCH);
		intent.setData(patientMessageURI(patientID, messageID));
		intent.setClass(context, PatientMessageActivity.class);
		return intent;
	}
	public static Uri patientMessageURI(int patientID, int messageID) {
		String output = "content://org.freeclinic.Message/add/"  + patientID + "/" + messageID;
		Log.e(TAG, "creating patient URI: " + output);
		return Uri.parse(output);
	}
	/**
	 * Add A message to the users record
	 * This will call an activity that will have the doctor choose the type of message they wish
	 * to search for and then be able search through the message database.  The activity should then call back.
	 */
	public void addMessage()
	{
		Intent intent = new Intent(this, MessageAddActivity.class);
		intent.setData(Uri.parse(CONTENT_PROVIDER + patientID)); 
		super.startSubActivity(intent, 0);
	}
	
	/**
	 * Start the vitals collection, this should call back with the vitals information, which should 
	 * change the state of the vitals Collection button
	 */
	public void vitals()
	{
		Intent intent = new Intent(this, VitalsActivity.class);
		intent.putExtra("patientID", patientID);
		super.startSubActivity(intent, 0);
		
	}

	/**
	 * Start the allergies activity for this patient
	 */
	public void allergies()
	{
		Intent intent = new Intent(this, AllergiesActivity.class);
		intent.putExtra("patientID", patientID);
		super.startSubActivity(intent, 0);
	}

	public void showDetails()
	{
		Intent intent = new Intent(this, PatientDetailsActivity.class);
		Log.e(TAG, "Sending patient Id: " + patientID);
		intent.putExtra("patientID", patientID);
		super.startSubActivity(intent, 0);
		//this.setContentView(new PatientDetailsView(this,patient));
	}

	public void showHistory()
	{
		//this.setContentView(new PatientHistoryView(this,patient));
		Intent intent = new Intent(this, PatientHistoryActivity.class);
		Log.e(TAG, "Sending patient Id: " + patientID);
		intent.putExtra("patientID", patientID);
		super.startSubActivity(intent, 0);
	}
	
	public void showVisualization()
	{
		//this.setContentView(new PatientHistoryView(this,patient));
		Intent intent = new Intent(this, ChartActivity.class);
		Log.e(TAG, "Sending patient Id: " + patientID);
		intent.putExtra("patientID", patientID);
		super.startSubActivity(intent, 0);
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e(TAG,"pausing...");
	}
	

	// ON CLICK LISTENERS
	
	class VitalsOnClickListener implements View.OnClickListener {
		public void onClick(View view) {
			vitals();
		}
	}
	class AllergiesOnClickListener implements View.OnClickListener {
		public void onClick(View view) {
			allergies();
		}
	}
	class AddOnClickListener implements View.OnClickListener {
		public void onClick(View view) {
			addMessage();
		}
	}
	class DetailsOnCLickListener implements View.OnClickListener 
	{	
		public void onClick(View view) {
			showDetails();	
		}
	}
	class HistoryOnClickListener implements View.OnClickListener 
	{	
		public void onClick(View view) {
			showHistory();	
		}
	}
	class VisualizeOnClickListener implements View.OnClickListener
	{
		public void onClick(View view) {
			showVisualization();	
		}
	}
	class ExitOnClickListener implements View.OnClickListener 
	{	
		public void onClick(View view) {
			finish();
		}
	}
	class AddMessageOnClickListener implements View.OnClickListener
	{
		int type;
		public AddMessageOnClickListener(int messageTypeID)
		{
			this.type = messageTypeID;
		}
		public void onClick(View v) {
			messageAdd(type);
		}
	}
	public int getPatientID() {
		return patientID;
	}

	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}

}	

