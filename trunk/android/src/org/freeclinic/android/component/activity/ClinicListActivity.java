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

import org.freeclinic.android.component.global.ClinicMenu;
import org.freeclinic.android.service.FCPService;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.Resources;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.Menu.Item;
import android.widget.Toast;


public abstract class ClinicListActivity extends ListActivity {
	public static final String TAG = "ClinicActivity";
	public static final int SETTINGS = 1;
	public static final int LOGOFF = 3;
	public static final int LOGON = 2;
	protected Resources resources;
	protected long mState;
	protected boolean isLoggedOn = false;
	protected ClinicMenu clinicMenu;

	protected FCPService service = null;
	


	
	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		clinicMenu = new ClinicMenu(this);
		resources = getResources();
	}
	

	public void serviceMethod(ServiceConnection connection) {
		bindService(new Intent(this,FCPService.class), connection, Context.BIND_AUTO_CREATE);
	}
	
	public FCPService bindService(IBinder service) {
		return ((FCPService.LocalBinder)service).getService();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		clinicMenu.onCreateOptionsMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(Item item) {
		clinicMenu.onOptionsItemSelected(item);
		return super.onOptionsItemSelected(item);
	}
	
	public FCPService getService() {
		return service;
	}
	protected void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT);
	}
	protected void toast(int message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT);
	}
    
	//public abstract void displayView();
}
