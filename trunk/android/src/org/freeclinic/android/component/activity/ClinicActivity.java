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

import android.app.Activity;
import android.content.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.Menu.Item;
import android.widget.Toast;

public abstract class ClinicActivity extends Activity {
	public static final String TAG = "ClinicActivity";
	protected ClinicMenu clinicMenu;
	protected Resources resources;
	protected long mState;
	protected boolean isLoggedOn = false;

	protected FCPService service = null;
	

	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		resources = getResources();
		clinicMenu = new ClinicMenu(this);
		
		//ClinicConnection.serviceMethod(this, new StartConnection(this));
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
