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
import org.freeclinic.android.component.view.PatientDetailsView;
import org.freeclinic.common.info.Patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.widget.TextView;

public class PatientDetailsActivity extends ClinicActivity{
	
	private Patient patient;
	private int patientID;
	
	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        final Intent intent = getIntent();
		
		setContentView(R.layout.patient_details);
		
		// Parse the patient ID out of the URI 
		patientID = intent.getExtras().getInt("patientID");
		
		//this.setContentView(new PatientDetailsView(this,patient));
		ClinicConnection.serviceMethod(this, new PatientDetailsConnection(this, patientID));
		
	}
	
	public void fill(){
		TextView name, dob, gender, hphone, wphone, cell, address;
		name = (TextView)findViewById(R.id.name);
		dob = (TextView)findViewById(R.id.birth);
		gender = (TextView)findViewById(R.id.gender);
		hphone = (TextView)findViewById(R.id.hphone);
		wphone = (TextView)findViewById(R.id.wphone);
		cell = (TextView)findViewById(R.id.cell);
		address = (TextView)findViewById(R.id.address);
		if(patient.getName() != null);
			name.setText("Name: " + patient.getName());
		if(patient.getDateOfBirth() != null)
			dob.setText("Date of Birth: " + patient.getDateOfBirth());
		if(patient.getGender() != null)
			gender.setText("Gender: " + patient.getGender());
		if(patient.getHomeNum() != null)
			hphone.setText("Home Phone: " + patient.getHomeNum());
		if(patient.getWorkNum() != null)
			wphone.setText("Work Phone: " + patient.getWorkNum());
		if(patient.getCellNum() != null)
			cell.setText("Cell Phone: " + patient.getCellNum());
		if(patient.getAddress() != null)
			address.setText("Address: " + patient.getAddress());
	}
	
	public class PatientDetailsConnection extends ClinicConnection {
		
		public PatientDetailsConnection(Context context, int patientID) {
			super(context);
		}
		
	    public void action() {
	    	patient = service.getPatient(patientID);
	    	fill();
	    }
	}
	
}
