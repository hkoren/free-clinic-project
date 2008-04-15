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
import org.freeclinic.common.info.Message;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MessageAddActivity extends PatientActivity {
	private static final String TAG = "MessageAddActivity";
	private Button prescribe,refer,treat,note,diagnose,test;
	private Uri mURI;
	private int patientID;
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.add_message);
		
		connectAddButtons();
		
		final Intent intent = getIntent();
		mURI = intent.getData();
		Log.e(TAG, "Got URI " + mURI);
		
		// Parse the patient ID out of the URI
		patientID = Integer.parseInt(mURI.getPathSegments().get(1));
		
	}
	
	
	private void connectAddButtons() {
		prescribe = initMessageButton(R.id.prescribe);
		refer     = initMessageButton(R.id.refer);
		treat     = initMessageButton(R.id.treat);
		note      = initMessageButton(R.id.note);
		diagnose  = initMessageButton(R.id.diagnose);
		treat     = initMessageButton(R.id.test);		
	}
	
	private Button initMessageButton(int id) {
		Button b = (Button)findViewById(id);
		b.setOnClickListener(new AddMessageOnClickListener(id));
		return b;
	}
	/**
	 * Indicate that the user wishes to create a Message
	 * @param type The TypeID of the message to create
	 */
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
	public static Uri patientMessageURI(int patientID, int messageID) {
		String output = "content://org.freeclinic.Message/add/"  + patientID + "/" + messageID;
		Log.e(TAG, "creating patient URI: " + output);
		return Uri.parse(output);
	}
	public static Intent patientMessageIntent(Context context, int patientID, int messageID) {
		Intent intent = new Intent(Intent.VIEW_ACTION);
		intent.addLaunchFlags(Intent.NEW_TASK_LAUNCH);
		intent.setData(patientMessageURI(patientID, messageID));
		intent.setClass(context, PatientMessageActivity.class);
		return intent;
	}
	
	private class AddMessageOnClickListener implements View.OnClickListener
	{
		int type;
		public AddMessageOnClickListener(int messageTypeID)
		{
			this.type = messageTypeID;
		}
		public void onClick(View v) {
			messageAdd(type);
		}
	};
	
	public void selectMessage(long messageType) {
		
	}

}
