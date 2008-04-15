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
import org.freeclinic.common.info.Vitals;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VitalsActivity extends ClinicActivity {
	GetServiceConnection gsc;
	SaveServiceConnection ssc;	
	
	private static final String TAG = "VitalsActivity";
	private EditText temp,hr,syst,dias,resp;
	private int patientID;
	Toast toast;
	Vitals vitals;
	
	@Override
	protected void onCreate(Bundle icicle) {
		
		super.onCreate(icicle);
		setContentView(R.layout.vitals);

		
		Intent intent = this.getIntent();
		patientID = intent.getIntExtra("patientID", 0);

		connectResources();
		//fetchPreviousVitals();

	}
	
	/**
	 * Connect to the GUI elements
	 */
	private void connectResources()
	{
		// Set local variables
		temp = (EditText)findViewById(R.id.temp);
		hr = (EditText)findViewById(R.id.hr);
		syst = (EditText)findViewById(R.id.syst);
		dias = (EditText)findViewById(R.id.dias);
		resp = (EditText)findViewById(R.id.dias);
		
		// Connect to layout
		Button cancel = (Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(new CancelListener());
		Button save = (Button)findViewById(R.id.save);
		save.setOnClickListener(new SaveListener());
	}
	/**
	 * Show last values for vitals
	 */
	private void fetchPreviousVitals()
	{
		gsc = new GetServiceConnection();
	}
	private void displayPreviousVitals()
	{
		if (vitals != null)
		{
			
		}
	}
	
	/**
	 * Write the vitals to the service
	 */
	
	private boolean saveVitals()
	{
		// Validate the vitals
		boolean valid=true;
		View badField;
		double itemp, ihr, isyst, idias, iresp;
		// Temperature
		if(temp.getText().toString().equals("")){
			itemp = -1;
		}
		else{
			try { itemp = Float.parseFloat(temp.getText().toString()); }  
			catch (NumberFormatException e) { 
				notifyError(temp, R.string.temp_invalid);
				return false;
			}
		}
		// Heart Rate
		if(hr.getText().toString().equals("")){
			ihr = -1;
		}
		else{
			try { ihr   = Float.parseFloat(hr.getText().toString()); }    
			catch (NumberFormatException e) { 
				notifyError(hr, R.string.hr_invalid);
				return false;
			}
		}
		// Systolic Blood Pressure
		if(syst.getText().toString().equals("")){
			isyst = -1;
		}
		else{
			try { isyst = Float.parseFloat(syst.getText().toString()); }  
			catch (NumberFormatException e) { 
				notifyError(syst, R.string.syst_invalid);
				return false;
			}
		}
		// Diastolic Blood Pressure
		if(dias.getText().toString().equals("")){
			idias = -1;
		}
		else{
			try { idias = Float.parseFloat(dias.getText().toString()); }  
			catch (NumberFormatException e) {
				notifyError(dias, R.string.dias_invalid);
				return false;
			}
			}
		
		// Respiration
		if(resp.getText().toString().equals("")){
			iresp = -1;
		}
		else{
			try { iresp = Float.parseFloat(resp.getText().toString()); }  
			catch (NumberFormatException e) {			
				notifyError(resp, R.string.resp_invalid);
				return false;
			}
		}
		
		Vitals out = new Vitals(itemp, ihr, isyst, idias, iresp);
						
		ssc = new SaveServiceConnection(out);
		return valid;
	}

	/**
	 * Inform the user that one of the entries is invalid
	 * @param v
	 * @param errorRes
	 */
	private void notifyError(View v, int errorRes)
	{
		Toast.makeText(this, errorRes, Toast.LENGTH_SHORT).show();
		Log.e(TAG,"Error with " + errorRes);
		v.requestFocus();
	}

	private class GetServiceConnection extends ClinicConnection {
		public GetServiceConnection() {
			super(VitalsActivity.this);
		}
		public void action()
		{
			VitalsActivity.this.vitals =  service.getVitals(patientID);
			displayPreviousVitals();
		}
	}
    /**
     * Connect to service and save this vital information
     * @author Henry
     *
     */
	private class SaveServiceConnection extends ClinicConnection 
	{
		private Vitals out;
		
		public SaveServiceConnection(Vitals vitals)
		{
			super(VitalsActivity.this);
			out=vitals;
		}

		public void action() {
			if (service.putVitals(patientID, out))
			{
				toast(R.string.vitals_rec);
			}
			else
			{
				toast(R.string.vitals_err);
			}
		}
		public void onServiceDisconnected(ComponentName className)
		{
		}
	}
	
	private class CancelListener implements View.OnClickListener {
		
		public void onClick(View v) {
			finish();
		}
	}
	private class SaveListener implements View.OnClickListener {
		
		public void onClick(View v) {
			// TODO: Save the vitals to the database
			if (saveVitals()) 
				finish();
		}
	}
//	private class VitalKeyPressListener implements View.OnKeyListener {
//		private EditText next;
//		private EditText prev;
//		public VitalKeyPressListener(EditText prev,EditText next) {
//			this.prev = prev;
//			this.next = next;
//		}
//		public boolean onKey(View v, int code, KeyEvent e) {
//			int keyCode =  e.getKeyCode();
//
//			// If they hit enter, go on to the next vital
//			if (code == KeyEvent.KEYCODE_NEWLINE)
//			{
//				v.requestFocus();
//				return true;
//				
//			}
//			return false;
//		}
//	}

}