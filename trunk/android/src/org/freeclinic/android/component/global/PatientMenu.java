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
import org.freeclinic.android.component.activity.MessageAddActivity;
import org.freeclinic.android.component.activity.PatientActivity;
import org.freeclinic.android.component.connection.ClinicConnection;
import org.freeclinic.common.info.Message;

import android.content.Intent;
import android.view.Menu;
import android.view.SubMenu;
import android.view.Menu.Item;

public class PatientMenu {
	public static final int ADD = 100;
	private PatientActivity context;
	
	public PatientMenu(PatientActivity context) {
		this.context = context;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Logoff Menu
		SubMenu addMenu = menu.addSubMenu(0, ADD, R.string.add,R.drawable.add);
		addMenu.add(0,R.id.prescribe,R.string.prescribe);
		addMenu.add(0,R.id.refer,R.string.refer);
		addMenu.add(0,R.id.treat,R.string.treat);
		addMenu.add(0,R.id.note,R.string.note);
		addMenu.add(0,R.id.diagnose,R.string.diagnose);
		addMenu.add(0,R.id.test,R.string.test);

		return true;
	}
	
	public boolean onOptionsItemSelected(Item item) {
		
		switch (item.getId())
		{
			case R.id.prescribe:
				context.startSubActivity(MessageAddActivity.patientMessageIntent(context, Message.Type.PRESCRIPTION,context.getPatientID()), 0);
				break;
			case R.id.refer:
				context.startSubActivity(MessageAddActivity.patientMessageIntent(context, Message.Type.REFERRAL, context.getPatientID()), 0);
				break;
			case R.id.treat:
				context.startSubActivity(MessageAddActivity.patientMessageIntent(context, Message.Type.TREATMENT, context.getPatientID()), 0);
				break;
			case R.id.note:
				context.startSubActivity(MessageAddActivity.patientMessageIntent(context, Message.Type.NOTATION, context.getPatientID()), 0);
				break;
			case R.id.diagnose:
				context.startSubActivity(MessageAddActivity.patientMessageIntent(context, Message.Type.DIAGNOSIS, context.getPatientID()), 0);
				break;
			case R.id.test:
				context.startSubActivity(MessageAddActivity.patientMessageIntent(context, Message.Type.LAB_TEST, context.getPatientID()), 0);
				break;			
		}
		return true;
	}
}
