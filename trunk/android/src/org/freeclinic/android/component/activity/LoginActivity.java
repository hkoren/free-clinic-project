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

//import java.util.Timer;
//import java.util.TimerTask;

import org.freeclinic.android.R;
import org.freeclinic.android.component.connection.ClinicConnection;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Display an activity asking for the user to log on
 * 
 * @author Henry,Cameron,Trent
 *
 */

public class LoginActivity extends ClinicActivity {
	/**
	 * This activity creates the login screen and handles the input to the server
	 * which is then processed in FreeClinicActivity. 
	 */
	private static final String TAG = "LoginActivity";
	EditText userField,passField;
	TextView userPrompt, passPrompt;
	private String username,password;

	Button login, register;
	public LoginActivity() {
		super();
	}
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setContentView(R.layout.login);
		
		initLogin();
		
	}
	
	
	
	/**
	 * Create the connections to the GUI components
	 */
	private void initLogin() {
		/**
		 * This binds the button in the layout onto their respective onClickListeners
		 */
		login = (Button)findViewById(R.id.login);
		login.setOnClickListener(new LoginOnClickListener());
		register = (Button)findViewById(R.id.register);
		register.setOnClickListener(new RegistrationOnClickListener());
	}
	/**
	 * Forward user to device registration routines
	 */
	private void goRegister() {
		/**
		 * Starts the register activity.
		 */
		this.startSubActivity(new Intent(this,RegistrationActivity.class), 0);
	}
	
	/**
	 * Listener for clicks on register button 
	 *
	 */
	private class RegistrationOnClickListener implements View.OnClickListener {
		public void onClick(View v) {
			goRegister();
		}
	}
	/**
	 * This click listener handles login attempts
	 * @author Cameron Esfahani
	 *
	 */
	private class LoginOnClickListener implements View.OnClickListener {
		
		/**
		 * This thread is to make the application remain active while it is attempting to login
		 * problem is not in this line, it returns fast so this need not have another thread
		 * @author Henry, Brent
		 *
		 */
		private class ServiceThread extends Thread implements Runnable  {
			ClinicConnection connection;
			public void run() {
	    		ClinicConnection.serviceMethod(LoginActivity.this, new LoginConnection());
			}
		}
		/**
		 * Note that the following onClick() handler method's View parameter
		 * is in fact the button which was clicked, triggering the event.
		 */
    	public void onClick(View v) {
    		AlertDialog dialog = 
    		new AlertDialog.Builder(LoginActivity.this)
    		.setMessage("Logging in. Please wait...")
    		.setNeutralButton(R.string.cancel,
    				new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int whichButton) {
    				Log.e(TAG, "Login cancelled by user");
    			}
    		})
    		.show();  		
    		
    		/*
    		 * Because the passed in View was the button, and the button is
    		 * a deep descendant of the TableLayout (the form), we use the
    		 * getRootView() method to retrieve the TableLayout object. From
    		 * this we are able to retrieve any descendant who's ID was explicitly
    		 * set.
    		 */
    		EditText userField = (EditText)findViewById(R.id.username);
    		username=userField.getText().toString();
    		
    		passPrompt = (TextView)findViewById(R.id.pass_prompt);
    		passField = (EditText)findViewById(R.id.password);
    		Editable pass = passField.getText();
    		password=pass.toString();
    		
    		// Server connection and authentication will be called from here.
    		Log.e(TAG, "Binding Service");
    		ServiceThread loginThread = new ServiceThread();
    		loginThread.start();
    		Log.e(TAG, "started loginthread which will call servicemethod on loginconnection");
    	}
	}
//	private boolean cancelled;
//	private void cancelThread()
//	{
////		this.service.cancelLogin();
//		cancelled = true;
//	}
	/**
	 * Initiate a connection to log on to the system
	 */
    protected class LoginConnection extends ClinicConnection implements ServiceConnection {
    	LoginThread loginThread;

		public LoginConnection() {
			super(LoginActivity.this);
		}
    	
    	@Override
		public void onServiceConnected(ComponentName name, IBinder iservice) {
    		super.onServiceConnected(name, iservice);
    		/**
    		 * This method is called once the connection to the service is established
    		 * and checks the login information.
    		 */
    		service = bindService(iservice);
    		
    		Log.e(TAG, "Service Connected. Logging in...");
        	handler = new Handler();
        	
        	loginThread = new LoginThread();

        	action();
        	Log.e(TAG, "Login thread started");   
		}

    	protected void action() {
        	loginThread.start();
    	}
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			super.onServiceDisconnected(arg0);
			service = null;
		}

		private class LoginThread extends Thread implements Runnable  {
			
    		public static final String TAG="LoginThread";
    		
			public void run() {
				if (service.login(username, password)) {
					handler.post(
							new Runnable() { 
								public void run() { 
									Log.e(TAG,"login success");
									service = null;
									notifyLogin();
								} 
							}
					); 

				}
				else {
					handler.post(
							new Runnable() { 
								public void run() { 
									Log.e(TAG,"login failure");
									service = null;
									notifyFailure();
								} 
							}
					);
	    		}
				
			}
		}
    	Handler handler;
        
    	private void notifyLogin() {
    		/**
    		 * Signal a successful login.
    		 */
    		CharSequence message = "Access Granted for " + username;
    		Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT);
    		LoginActivity.this.setResult(1);
    		LoginActivity.this.finish();
    		
    	}
    	private void notifyFailure() {
    		/**
    		 * Signal a failed login.
    		 */
    		LoginActivity.this.toast("Invalid Password for " + username);
    		passPrompt.setTextColor(resources.getColor(R.color.red));
    		passField.setHint("please try again");
    		passField.setText("");
    	}
    	private void showEnterPassword() {
    		if (passPrompt != null)
    		{
    			passPrompt.setTextColor(resources.getColor(R.color.black));
        		passField.setHint("enter your password here");
    		}
    		else
    		{
    			Log.e(TAG,"null pass Prompt");
    		}
    	}

		@Override
		protected void accessDenied() {
			// TODO Auto-generated method stub
			super.accessDenied();
			notifyFailure();
		}
	}
}
