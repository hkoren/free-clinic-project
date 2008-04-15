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
package org.freeclinic.common.network.request;

import org.freeclinic.common.type.ClinicRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationRequest extends ClinicRequest {

	private static final String IMEI = "i";
	private static final String PHONE_NUM = "p";
	
	public RegistrationRequest() {
		super(RequestType.REGISTRATION);
	}
	public RegistrationRequest(JSONObject json) throws JSONException {
		super(json);
	}

	public RegistrationRequest(String imei, String phoneNum) {
		super(RequestType.REGISTRATION);
		setString(IMEI, imei);
		setString(PHONE_NUM, phoneNum); 
	}
	
	@Override
	public void init() {
		super.init();
		registerString(IMEI);
		registerString(PHONE_NUM);
	}
	
	public String getImei(){
		return getString(IMEI);
	}
	public String getPhoneNum(){
		return getString(PHONE_NUM);
	}
	
	public String toString() {
		String retString = "Registered IMEI: " + getImei() + "\n" 
							+ "Registed with Phone Number: " + getPhoneNum();
		return retString;
		
	}

}
