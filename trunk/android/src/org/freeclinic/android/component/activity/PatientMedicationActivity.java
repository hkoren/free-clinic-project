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

//import org.freeclinic.android.component.activity.PatientHistoryActivity.HistoryConnection;
import org.freeclinic.android.service.FCPService;
import org.freeclinic.common.info.Message;
import org.freeclinic.common.info.PatientMessage;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ArrayAdapter;

public class PatientMedicationActivity extends ClinicListActivity {
	private static final String TAG  = "PatientMedication";
	private int patientId;
	PatientMessage[] patientMessages;
	String[] messages;
	
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		//setContentView(R.layout.patient_history);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
            patientId = extras.getInt("patientID");
            Log.e(TAG,"Patient Id Is: "+patientId);
        }
		else{
			Log.e(TAG,"Patient Id Unavailable");
			patientId = -1;
		}
		this.serviceMethod(new MedicationConnection());
	}
	
	private void setMessages(){
		if (patientMessages != null) {
			if(patientMessages.length==0){
				messages = new String[1];
				messages[0] = "No Messages";
				return;
			}
			messages = new String[patientMessages.length];
			for(int i = 0; i < patientMessages.length; i++){
				//messages[i] = patientMessages[i].toString();
				messages[i] = patientMessages[i].getName();
				
			}
		}
		else
		{
			Log.e(TAG, "Null Patient Messages");
		}
	}
	
	private void announceAdapter(){
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, messages);
		
    	setListAdapter(mAdapter);
	}
	
	private class MedicationConnection implements ServiceConnection{
		private FCPService service; 
        public void onServiceConnected(ComponentName className, IBinder iservice)
        {
        	Log.e("ServiceConnection", "Service Connected. Getting Patients...");
        	service =  bindService(iservice);
        	patientMessages = service.getPatientMessages(patientId, Message.Type.PRESCRIPTION);
        	if(patientMessages ==  null){
        		Log.e(TAG,"Error: patient messges null");
        	}
        	else
        	{
        		setMessages();
        		announceAdapter();
        	}
        	//PatientCheckInView.this.addContent();
        }

        public void onServiceDisconnected(ComponentName className)
        {

        }
	}
}

