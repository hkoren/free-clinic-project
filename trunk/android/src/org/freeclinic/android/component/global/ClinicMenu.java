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
package org.freeclinic.android.component.global;

import org.freeclinic.android.FreeClinicActivity;
import org.freeclinic.android.R;
import org.freeclinic.android.component.connection.ClinicConnection;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.Menu.Item;

public class ClinicMenu {
	public static final int SETTINGS = 1;
	public static final int LOGOFF = 3;
	public static final int LOGON = 2;
	public static final int HELP = 4;
	private Context context;
	
	public ClinicMenu(Context context) {
		this.context = context;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Logoff Menu
		Item logoff = menu.add(0, LOGOFF, R.string.logout);
		logoff.setIcon(R.drawable.lock);
		
		// Help menu
		Item help = menu.add(0, HELP, R.string.help);
		help.setIcon(R.drawable.question);
		return true;
	}
	
	private Intent helpIntent()
	{
		Intent intent = new Intent(Intent.VIEW_ACTION);
		intent.addLaunchFlags(Intent.NEW_TASK_LAUNCH);
		intent.setData(Uri.parse("http://freeclinicproject.org/"));
		return intent;
	}
	
	public boolean onOptionsItemSelected(Item item) {
		
		switch (item.getId())
		{
			case(LOGOFF):
				ClinicConnection.serviceMethod(context, new LogoutConnection());
				
				break;
			case(HELP):

				context.startActivity(helpIntent());
				break;
				
		}
		return true;
	}
	private class LogoutConnection extends ClinicConnection
	{
		public LogoutConnection()
		{
			super(ClinicMenu.this.context);
		}
		public void action()
		{
			service.logout();
			context.startActivity(new Intent(context,FreeClinicActivity.class));		}
	}

	
}
