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
import org.freeclinic.common.info.Settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends ClinicActivity {
	private ClinicConnection connection;
	private static final String TAG="SettingsActivity";
	private Button save,cancel;
	private EditText host,port; 
	private Settings settings;
	public void displayView() {
		
	}

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.settings);
		
		connectViews();
		
		save.setOnClickListener(new SaveSettingsOnClickListener());		
		cancel.setOnClickListener(new CancelOnClickListener());
		
		readSettings();
	}
	
	private void readSettings() {
		connection = new SettingsReadConnection();
	}

	private void connectViews() {
		save = (Button)findViewById(R.id.save);
		cancel = (Button)findViewById(R.id.cancel);
		host = (EditText)findViewById(R.id.host);
		port = (EditText)findViewById(R.id.port);
	}
	
	private class SaveSettingsOnClickListener implements View.OnClickListener {
		public void onClick(View v)
		{
			if (saveSettings())
				finish();
		}
	}
	
	private boolean saveSettings() {
		String hostStr = host.getText().toString();
		int portInt = 0;
		try
		{
			portInt = Integer.parseInt(port.getText().toString());
		}
		catch (NumberFormatException e)
		{
			
			port.requestFocus();
		}
		
		if (hostStr.compareTo("") == 0)
		{
			host.requestFocus();
		}
		settings.setPort(portInt);
		settings.setServer(hostStr);
		service.setSettings(settings);
		return service.saveSettings();
	}
	

	private class CancelOnClickListener implements View.OnClickListener {
		public void onClick(View v)
		{
			finish();
		}
	}
    private class SettingsReadConnection extends ClinicConnection {
		public SettingsReadConnection() {
			super(SettingsActivity.this);
		}
        public void action()
        {
        	settings = service.getSettings();
        	host.setText(settings.getServer());
        	port.setText(Integer.toString(settings.getPort()));
        }

    };
}
