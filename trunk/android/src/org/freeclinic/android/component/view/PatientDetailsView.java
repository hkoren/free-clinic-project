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
package org.freeclinic.android.component.view;

import org.freeclinic.android.component.global.Ident;
import org.freeclinic.common.info.Patient;
import org.freeclinic.common.info.Person;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PatientDetailsView extends RelativeLayout {
	private Ident addressRowId;
	private Ident phoneRowId;
	private TextView address;
	private TextView phoneNumbers;
	
	// TODO: Initialize the person somehow
	private Person person;
	private Context context;
	public PatientDetailsView(Context context, Patient patient)
	{
		super(context);
		this.context = context;
		init();
	}
	private void init() {

		showAddress();
		showPhones();
	}			
	

	private void showAddress() {
		addressRowId = new Ident(context);
		address = new TextView(context);
		address.setText("Address\n");
		//address.setTypeface(BOLD);

		//addressRow displays address
		TableRow addressRow = new TableRow(context);
		addressRow.setId(addressRowId.getId());

		TextView line1 = new TextView(context);
		try { line1.setText(person.getAddress().toString()); } catch(NullPointerException e) { }

		TextView line2 = new TextView(context);
		try { line2.setText(person.getCity().toString() + ", "); } catch(NullPointerException e) { }

		TextView line3 = new TextView(context);	
		line3.setText(person.getState() + " " + person.getZipCode() );


		addressRow.addView(address);
		addressRow.addView(line1);
		addressRow.addView(line2);
		addressRow.addView(line3);
		
	    LayoutParams addressRowLayout = 
	    	new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	    //addressRowLayout.addRule(RelativeLayout.POSITION_TO_RIGHT, addressRowId.getId());
		addView(addressRow, addressRowLayout);
	}

	private void showPhones() {
		phoneRowId = new Ident(context);
		phoneNumbers = new TextView(context);
		phoneNumbers.setText("Phone Numbers\n");
		//Phone_Nums.setTypeface(BOLD);

	    //phoneRow displays various phone numbers
		TableRow phoneRow = new TableRow(context);
		phoneRow.setId(phoneRowId.getId());
	
		TextView homeNum = new TextView(context);
		homeNum.setText("Home: " + person.getHomeNum());
		//homeNum.setWidth(phoneRowWidth);
	
		TextView cellNum = new TextView(context);
		cellNum.setText("Cell: " + person.getCellNum());
		//cellNum.setWidth(phoneRowWidth);
	
		TextView workNum = new TextView(context);
		workNum.setText("Work: " + person.getWorkNum());
		//workNum.setWidth(phoneRowWidth);
	
		phoneRow.addView(phoneNumbers);
		phoneRow.addView(homeNum);
		phoneRow.addView(cellNum);
		phoneRow.addView(workNum);
	    LayoutParams phoneRowLayout = 
	    	new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		addView(phoneRow, phoneRowLayout);
	}
}
