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

public class RequestType {
	public static final int GENERIC = 0;
	public static final int PERSON = 1;
	public static final int LOGIN = 2;
	public static final int PATIENT = 3;
	public static final int CLOSE = 4;
	public static final int DBUPDATE = 5;
	public static final int LOGOUT = 6;
	public static final int PATIENT_LIST = 7;
	public static final int MESSAGE_UPDATE = 8;
	public static final int MESSAGE_SEARCH = 15;
	public static final int SHOW_CHECKINS = 9;
	public static final int GET_PATIENT = 10;
	public static final int KEEP_ALIVE = 11;
	public static final int GET_VITALS = 12;
	public static final int PUT_VITALS = 13;
	public static final int REGISTRATION = 14;
	public static final int ADD_PATIENT_MESSAGE = 16;
	public static final int UPDATE_PATIENT_MESSAGE = 17;
	public static final int GET_PATIENT_MESSAGES = 18;
	public static final int GET_PATIENT_MEASUREMENTS = 19;
}
