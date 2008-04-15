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
package org.freeclinic.android;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.freeclinic.android.component.activity.CheckInActivity;
import org.freeclinic.android.component.activity.ClinicActivity;
import org.freeclinic.android.component.activity.LoginActivity;
import org.freeclinic.android.component.activity.MessageUpdateActivity;
import org.freeclinic.android.component.activity.PatientActivity;
import org.freeclinic.android.component.activity.RegistrationActivity;
import org.freeclinic.android.component.activity.SettingsActivity;
import org.freeclinic.android.component.global.ClinicMenu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FreeClinicActivity extends ClinicActivity {
	
	protected ClinicMenu clinicMenu;

	public static final String TAG="FreeClinic";
	
	public static final int SETTINGS = 1;
	public static final int LOGIN = 2;
	public static final int LOGOFF = 3;
	public static final int LOGINATTEMPT = 4;
	public static final int ACTIVE = 5;
	public static final int INACTIVE = 6;
	public static final int HELP = 7;
	public static final int PATIENT = 8;
	public static final int REGISTERING = 9;
	
	public static final String STATE = "STATE";
	protected long m_State;
	
	@Override
	public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        try {
        	String outFilename = "/data/data/org.freeclinic.android/databases/fcpdata.db";
            File file = new File(outFilename);
            if(!file.exists()) {
            	Log.d(TAG, "Database does not exist, preparing to copy.");
            	
	            // Open the file
	            InputStream in = getAssets().open("fcpdata.db");
	            Log.d(TAG, "Opened asset file, fcpdata.db successfully.");
	            // Open the output file
	            
	            OutputStream out = new FileOutputStream(outFilename);
	
	            // Transfer bytes from the input file to the output file
	            byte[] buf = new byte[1024];
	            int len;
	            while ((len = in.read(buf)) > 0) {
	                out.write(buf, 0, len);
	            }
	
	            // Close the streams
	            out.close();
	            in.close();
	            
	            Log.d(TAG, "Database file constructed.");
            }
        } catch (IOException e) {
        	Log.e(TAG, "Database Initialization IO Error");
        }
        
        clinicMenu = new ClinicMenu(this);
        Log.d(TAG,"Starting Free Clinic");
        m_State = icicle != null ? icicle.getLong("state") : LOGIN;
        
        if (icicle != null) {
        	m_State = icicle.getLong(STATE);
        }
        else {
        	m_State = LOGIN;
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		clinicMenu.onCreateOptionsMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (m_State == LOGIN) {
			menu.setGroupShown(0, false);
		}
		else {
			menu.setGroupShown(0, true);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(Item item) {
		clinicMenu.onOptionsItemSelected(item);

		switch (item.getId()) {
			case(SETTINGS):
				this.startSubActivity(new Intent(this,SettingsActivity.class), 0);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onFreeze(Bundle outState) {
		super.onFreeze(outState);
		outState.putLong(STATE, m_State);
	}
	@Override
	protected void onResume() {
		super.onResume();
		
		if (m_State == LOGIN) {
			this.startSubActivity(new Intent(this,LoginActivity.class), 0);
			m_State = LOGINATTEMPT;
		}
		else if(m_State == LOGINATTEMPT) {
			this.finish();
		}
		else if (m_State == ACTIVE) {
			this.startActivity(new Intent(this, CheckInActivity.class));
			m_State = INACTIVE;
		}
		else if (m_State == INACTIVE) {
			this.finish();
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
	}
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onStop() {
		super.onStop();
		this.onPause();
	}

	/**
	 * Generate a patient URI
	 * @param patientID
	 * @return
	 */
	public static Uri patientURI(int patientID) {
		String output = "content://org.freeclinic.Patient/patients/" + patientID;
		Log.e(TAG, "creating patient URI: " + output);
		return Uri.parse(output);
	}
	
	/**
	 * Generate an intent to access a certain patient by ID number
	 * @param context
	 * @param patientID
	 * @return
	 */
	public static Intent patientIntent(Context context, int patientID) {
		Intent intent = new Intent(Intent.VIEW_ACTION);
		intent.addLaunchFlags(Intent.NEW_TASK_LAUNCH);
		intent.setData(patientURI(patientID));
		intent.setClass(context, PatientActivity.class);
		return intent;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, String data, Bundle extras) {
		super.onActivityResult(requestCode, resultCode, data, extras);
		if (m_State == LOGINATTEMPT && resultCode == 1)  { // Login Success
			m_State = ACTIVE;
		}
	}


}