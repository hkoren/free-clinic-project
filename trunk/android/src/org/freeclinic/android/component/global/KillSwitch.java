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


import android.content.Context;

/*********************
 * 
 * 
 * @author Ryan Hertzog
 * This class is the fail safe in the event the phone is stolen/lost.
 * 
 * 
 */
public class KillSwitch {
	
	
	//some private vars
	private int id;
	private String tag;
	
	
	public KillSwitch(){
		lockoutScreen();
		getLocalInfo();
		
		
	}
	// locks out user
	public void lockoutScreen(){
		// disable buttons
		// Send lockout screen that informs them to call
	}
	// if customer gets phone. new login.
	public void recoverLogin(){
		//display new frame
	}

	public void getLocalInfo(){
		//get the patient ID that was in local mem
		//Ident locInfo = new Ident();
		//id = locInfo.getId();
		//tag = locInfo.getTag();
	}
	//
	public void murder(){
		//delete local patient info
	}

	//if phone as gps get the location
	public void whereAmI(){
		//local var = get.location()
	}
	
}
