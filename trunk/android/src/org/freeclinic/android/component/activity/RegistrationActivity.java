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

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class RegistrationActivity extends ClinicActivity {
	private Button close;
	private Button regDevice;
	private TextView status;
	public void displayView() {
		
	}

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setContentView(R.layout.registration);
		status = (TextView)findViewById(R.id.status);
		registerView();
		regDevice = (Button)findViewById(R.id.register);
		regDevice.setOnClickListener(new RegisterDeviceOnClickListener());
		
		close = (Button)findViewById(R.id.close);
		close.setOnClickListener(new CloseRegistration());
	}
//	private void startRegistration(){
//		
//		TextView dReg = (TextView)findViewById(R.id.device_reg);
//		
//	}
//	
	
	private void registerView(){
		close = (Button)findViewById(R.id.close);
	}
	private class CloseRegistration implements View.OnClickListener {
		public void onClick(View v)
		{
			finish();
			
		}
	}
	private class RegisterDeviceOnClickListener implements View.OnClickListener {
		public void onClick(View v)
		{
			/**
			 * For the purpose of this demo, this method is written to exit on registration
			 */
			finish();
			//ClinicConnection.serviceMethod(RegistrationActivity.this, new RegistrationConnection());
			
		}
	}
	
	private boolean isDeviceRegistered() {
		return true;
	}
	

	/**
	 * Add A message to the users record
	 * This will call an activity that will have the doctor choose the type of message they wish
	 * to search for and then be able search through the message database.  The activity should then call back.
	 */
	public void register()
	{
		Intent intent = new Intent(this, RegistrationActivity.class);
		super.startSubActivity(intent, 0);
	}
	
	
	private class RegistrationConnection extends ClinicConnection {
		public RegistrationConnection() {
			super(RegistrationActivity.this);
		}

        public void action()
        {
        	//public PatientConnection(int patientID)
        	Log.e("ServiceConnection", "Service Connected. Registering...");
 
        	String imei = android.os.SystemProperties.get(android.telephony.TelephonyProperties.PROPERTY_IMEI); 
        	String phone = android.os.SystemProperties.get(android.telephony.TelephonyProperties.PROPERTY_LINE1_NUMBER);
        	
        	
    		if(service.registrationRequest(imei, phone)) {
    			// Now patient has been retrieved
    			
    			status.setText(R.string.registering);
    			Log.e(TAG, "Now Registering Device..." + phone);	
    		}
    		else {
    			
    		}
        }

    };
}
