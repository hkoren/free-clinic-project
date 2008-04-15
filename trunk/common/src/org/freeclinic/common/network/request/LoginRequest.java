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


public class LoginRequest extends ClinicRequest {
	
	private static final String USERNAME= "u";
	private static final String PASSWORD= "p";
	
	public LoginRequest() {
		super(RequestType.LOGIN);
	}
	
	public LoginRequest(JSONObject json) throws JSONException {
		super(json);
	}
	
	public LoginRequest(String userName, String password) {
		super(RequestType.LOGIN);
		setString(USERNAME, userName);
		setString(PASSWORD, password);
	}
	
	@Override
	protected void init() {
		super.init();
		System.err.println("Registering Login Request Fields!\n");
		registerString(USERNAME);
		registerString(PASSWORD);
	}
	
	public String getUserName() {
		return getString(USERNAME);
	}
	
	public void setUserName(String userName) {
		setString(USERNAME, userName);
	}
	
	public String getPassword() {
		return getString(PASSWORD);
	}
	
	public void setPassword(String password) {
		setString(PASSWORD, password);
	}
	
	public String toString() {
		return "Username: " + getUserName() + " Password : " + getPassword();
	}
	
}
