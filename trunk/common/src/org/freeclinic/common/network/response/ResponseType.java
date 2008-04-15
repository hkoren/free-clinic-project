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
package org.freeclinic.common.network.response;

public class ResponseType {
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_FAILURE = 2;
	public static final int CHECKIN_LIST = 3;
	public static final int PATIENT = 4;
	public static final int MESSAGE_LIST = 5;
	public static final int MEASUREMENT_LIST = 6;
	public static final int DEMOGRAPHIC_LIST = 7;
	public static final int NOT_AUTHORIZED = 8;
	public static final int KILL = 9;
	public static final int UNKNOWN_REQUEST = 10;
	public static final int ACKNOWLEDGED = 11;
	public static final int VITALS = 12;
	public static final int MESSAGE_UPDATE = 13;
	public static final int REGISTRATION_SUCCESS = 14;
	public static final int REGISTRATION_FAILURE = 15;
	public static final int PATIENT_MESSAGE_LIST = 16;
	public static final int PATIENT_MEASUREMENT_LIST = 17;
}
