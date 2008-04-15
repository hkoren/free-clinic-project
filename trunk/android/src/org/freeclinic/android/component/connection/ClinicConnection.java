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
package org.freeclinic.android.component.connection;

import org.freeclinic.android.service.FCPService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
/**
 * A connection from an Android Activity to the Free Clinic Projet Server by way of
 * the Android service.   This is a layer of abstraction built on top of ServiceConnection
 * to facilitate the communication between a handset client and a centralized server.
 * 
 * @author Henry, Cameron
 *
 */
public abstract class ClinicConnection implements ServiceConnection {
	public static final String TAG ="ClinicConnection";
	protected FCPService service;
	protected Context context;
	
	public void serviceMethod() {
		context.bindService(new Intent(context,FCPService.class), this, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * Initialize a connection between the Activity and the Server
	 * @param context Where you are calling this from, probably an activity
	 * @param serviceConnection the object that extends ServiceConnection, most likely a ClinicConnection
	 */
	public static void serviceMethod(Context context,ServiceConnection serviceConnection) {
		context.bindService(new Intent(context,FCPService.class), serviceConnection, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * 
	 * @param service
	 * @return
	 */
	protected FCPService bindService(IBinder service) {
		return ((FCPService.LocalBinder)service).getService();
	}

	/**
	 * Construct a connection from the supplied context which could be an Activity
	 * @param context the context the connection is being established from
	 */
	public ClinicConnection(Context context) {
		this.context = context;
		serviceMethod();
	}
	
	/**
	 * We override the onServiceConnected method so that it reacts based on the behavior of the server
	 */
	@Override
	public void onServiceConnected(ComponentName className, IBinder iservice) {
		
		service = bindService(iservice);
		
		if (service.isKilled())
		{
			Log.e(TAG,"Service Killed");
			onKilled();
		}
		else if (service.isLoggedIn()) // || className.equals(LoginActivity.LoginConnection.class)
		{
			Log.e(TAG,"Performing Action");
			action();
		}
		else
		{
			Log.e(TAG,"Not Logged on!");
			accessDenied();
		}
	}
	/**
	 * We override the onServiceDisconnected method so that it reacts based on the behavior of the server
	 */
	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		serviceConnectionLost();
	}

	/**
	 * Overriede this method to perform something when the user lacks the credentials to perform a certain
	 * action on the server.
	 */
	protected void accessDenied()
	{
		
	}
	
	/**
	 * Override this method to have an action when the FCPService-Server  IPC fails 
	 */
	protected void serverConnectionLost() {
	}

	/**
	 * Override this method to have an action when the Activity-FCPService IPC fails 
	 */
	protected void serviceConnectionLost() {
		
	}

	/**
	 * If the device has been marked on the server as stolen, this method is called
	 */
	protected void onKilled() {
		
	}
	/**
	 * Give a reference to the FCPservice
	 * @return the Android Free Clinic Project Service.
	 */
	protected FCPService getService() {
		return service;
	}
	
	/**
	 * This is the only method you are forced to override, this is what is performed
	 * when the connection to the FCPService is established.  You have access to the 
	 * "service" member inside here which you can call methods on to interact with
	 * persistent components of the application.
	 */
	protected abstract void action();
}
