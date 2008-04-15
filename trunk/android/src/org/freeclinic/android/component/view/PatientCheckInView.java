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
package org.freeclinic.android.component.view;

import org.freeclinic.android.FreeClinicActivity;
import org.freeclinic.android.component.activity.ClinicActivity;
import org.freeclinic.android.component.connection.ClinicConnection;
import org.freeclinic.android.component.global.Ident;
import org.freeclinic.android.component.global.ViewGenFactory;
import org.freeclinic.common.info.Patient;

import android.app.ListActivity;
import android.content.ComponentName;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * @author Trent Tai
 *
 */
public class PatientCheckInView extends RelativeLayout {
	public static String TAG = "PatientCheckIn";
	private Ident headerIdent;
	private Ident patientRowIdent;
	private Ident patientIdent;
	private Ident patientPromptIdent;
	private Ident patientFieldIdent;
	private Ident openButtonIdent;
	private ClinicActivity context;
	private Patient[] checkedInPatients;
	
	private class PatientList extends ListActivity {
		private ClinicActivity mContext;
		private PatientRow[] patientList;
		
	    public PatientList(FreeClinicActivity context) {
	    	Log.e(TAG,"Creating Patient List");
	    	mContext = context;
	        // Use our own list adapter
	        setListAdapter(new PatientListAdapter(mContext));
	    }
	    
	    public PatientList(ClinicActivity context, Patient[] patients) {
	    	Log.e(TAG,"Creating Patient List");
	    	mContext = context;
	    	if(patients==null){
	    		Log.e(TAG,"Patients is NULL");
	    	}
	    	else {
		        // Use our own list adapter
		    	patientList = new PatientRow[patients.length];
		    	for(int i=0; i<patients.length; i++){
		    		Log.e(TAG,"Adding Patient "+ i);
		    		patientList[i] = new PatientRow(mContext, patients[i]);
		    	}
		    	PatientListAdapter pAdapter = new PatientListAdapter(mContext);
		        setListAdapter(pAdapter);
	    		
	    	}
	    }
	    
	    //will represent one row of the list
	    private class PatientRow extends RelativeLayout{
	    	Patient patient;
	    	public PatientRow(ClinicActivity context){
	    		super(context);
	    		Log.e(TAG,"Creating new row");
	    	}
	    	public PatientRow(ClinicActivity context, Patient mPatient){
	    		super(context);
	    		patient = mPatient;
	    		Log.e(TAG,"Creating new row");
	    	}
	    }
	    
	    //used to implement the list
	    private class PatientListAdapter extends BaseAdapter{
	    	
	    	private ClinicActivity lContext;
	    	PatientListAdapter(ClinicActivity context){
	    		lContext = context;
	    		Log.e(TAG,"Starting new List Adapter");
	    	}
	    	public int getCount(){
	    		return patientList.length;
	    	}
	    	public View getView(int position, View convertView, ViewGroup parent) {		
	            if (convertView == null) {
	            	return patientList[position];
	            } else {
	                PatientRow ret=  (PatientRow)convertView;
	                return ret;
	            }
	    	}
	    	public Object getItem(int position){
	    		//will want to return a row of Patient Data
	    		return patientList[position];
	    	}
	    	public long getItemId(int position){
	    		//we will want to return a unique indent
	    		return position;
	    	}
	    }
		
	}
	
    private class PatientConnection extends ClinicConnection {
    	public PatientConnection() {
    		super(PatientCheckInView.this.context);
    	}
        public void action()
        {
        	Log.e("ServiceConnection", "Service Connected. Getting Patients...");
        	//context.bindService(iservice);
        	Patient[] patientList = service.getCheckInList();
        	Log.e(TAG,"Patient list: " + patientList);
    		setPatientList(patientList);
        	PatientCheckInView.this.addContent();
        }

        public void onServiceDisconnected(ComponentName className)
        {

        }
    }
	
    public void setPatientList(Patient[] newList){
    	Log.e(TAG, "Setting Up Patient List...");
    	if(newList == null){
    		Log.e(TAG,"New List is Null!!!");
    	}
    	checkedInPatients= newList;
    }
    
    public Patient[] getPatientList(){
    	return checkedInPatients;
    }
	public PatientCheckInView(ClinicActivity context) {
		super(context);
		this.context = context;
		mContext = context;
		
		ClinicConnection.serviceMethod(this.getContext(), new PatientConnection());
		//context.bindService(new Intent(context, 
			//	FCPService.class), new PatientConnection(), Context.BIND_AUTO_CREATE);

         //implements with ListView
        /*    for(int i=0; i<3 ; i++){
   //     	Log.e(TAG,"Count is " + i);
	        TableRow patientRow = new TableRow(getContext());
	        patientRowIdent = new Ident(context);
	        patientFieldIdent = new Ident(context);
	        patientPromptIdent = new Ident(context);
	        
	        patientRow.setId(patientRowIdent.getId());
	        TextView patientPrompt = new TextView(getContext());
	        patientPrompt.setText("Patient ID:");
	        patientPrompt.setPadding(10, 0, 30, 0);
	        
	        EditText patientId = new EditText(getContext());
	        patientId.setTag(patientFieldIdent.getTag());
	        patientId.setWidth(75);
	        patientId.setHint("id#");
	        patientId.setText("31"+i);
	        
	        patientRow.addView(patientPrompt);
	        patientRow.addView(patientId);
	        
	        LayoutParams patientRowLayout = 
	        	new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	        			LayoutParams.WRAP_CONTENT);
	        patientRowLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
	        patientRowLayout.addRule(RelativeLayout.POSITION_BELOW, prevIdent.getId());
	        this.addView(patientRow, patientRowLayout);
	        
	        prevIdent = patientRowIdent;
        }*/
        // Construct submit button

	}
	
	//This class adds the objects below the header, which have to wait for
	//the connection to load
	
	public void addContent(){
		headerIdent = new Ident(context);
		patientIdent = new Ident(context);
		openButtonIdent = new Ident(context);
		
        RelativeLayout header = 
        	ViewGenFactory.getInstance().getViewGen(context).createHeader("Patient Check-In");
        header.setId(headerIdent.getId());
        
        this.addView(header);
         // Construct user row
        
        Ident prevIdent = headerIdent;
        Patient[] patients = getPatientList();
        
		for (Patient p : patients) {
			Log.e(TAG, "Patient: " + p);
		}
		PatientList PatientListView = new PatientList(context, patients);
        
        LayoutParams PatientListLayout = 
        	new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
        PatientListLayout.addRule(RelativeLayout.POSITION_BELOW, prevIdent.getId());
        PatientListView.getListView().setId(patientIdent.getId());
        this.addView(PatientListView.getListView(), PatientListLayout);
        prevIdent = patientIdent;
        
        Button openButton = new Button(getContext());
        openButton.setId(openButtonIdent.getId());
        openButton.setText("Open");
        openButton.setOnClickListener(new OpenOnClickListener());
        
        LayoutParams openButtonLayout = 
        	new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
        openButtonLayout.addRule(RelativeLayout.POSITION_BELOW, prevIdent.getId());
        this.addView(openButton, openButtonLayout);		
	}

	private class OpenOnClickListener implements View.OnClickListener {
		
		private Ident openSuccessNotifIdent;
		private Ident openFailedNotifIdent;
		private int patientID;
		
		/**
		 * Note that the following onClick() handler method's View parameter
		 * is in fact the button which was clicked, triggering the event.
		 */
    	public void onClick(View v) {
    		
    		openSuccessNotifIdent = new Ident(v.getContext());
    		openFailedNotifIdent = new Ident(v.getContext());
    		
    		/*
    		 * Because the passed in View was the button, and the button is
    		 * a deep descendant of the TableLayout (the form), we use the
    		 * getRootView() method to retrieve the TableLayout object. From
    		 * this we are able to retrieve any descendant who's ID was explicitly
    		 * set.
    		 */
    		EditText patientField = (EditText) (v.getRootView().findViewWithTag(patientFieldIdent.getTag()));
    		Editable patient = patientField.getText();
    		patientID=Integer.parseInt(patient.toString());
    		
    		
    		// Server connection and authentication will be called from here.
    		//Log.e("LoginView", "Binding Service");
    		//context.bindService(new Intent(context,FCPService.class), null, new PatientConnection(), Context.BIND_AUTO_CREATE);
    		Log.e(TAG,"Opening Patient " + patientID);
    		context.startActivity(FreeClinicActivity.patientIntent(context, patientID));
    		

    	}
    	/*
        private class PatientConnection implements ServiceConnection {
            public void onServiceConnected(ComponentName className, IBinder iservice)
            {
            	service = IFCPService.Stub.asInterface((IBinder)iservice);
            	patientOpened();
            }

            public void onServiceDisconnected(ComponentName className)
            {

            }
        };
    	private void patientOpened() {
    		NotificationManager.from(context).notifyWithText(openSuccessNotifIdent.getId(), 
    				"Opening Paitent" + patientID, 
    				NotificationManager.LENGTH_SHORT, null);
    		
    		context.setState(FreeClinic.PATIENT);
    	}*/
	}
}