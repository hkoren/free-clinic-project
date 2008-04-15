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
package org.freeclinic.common.info;

import java.sql.ResultSet;

import org.freeclinic.common.type.ClinicInfo;
import org.freeclinic.common.type.EDate;
import org.freeclinic.common.type.EFloat;
import org.freeclinic.common.type.EInteger;
import org.json.JSONException;
import org.json.JSONObject;

public class Measurement extends ClinicInfo {
	
	public static final String ID = "_id";
	public static final String TYPEID = "measurementTypeID";
	public static final String VALUE = "value";
	public static final String TIMESTAMP = "timestamp";
	
	public class Type {
		public static final int SYSTOLIC_BLOOD_PRESSURE = 1;
		public static final int DIASTOLIC_BLOOD_PRESSURE = 2;
		public static final int HEART_RATE = 3;
		public static final int TEMPERATURE = 4;
		public static final int RESPIRATION = 5;
		public static final int BLOOD_GLUCOSE = 6;
	}
	
	public Measurement(int typeID, float value) {
		super();
		setInteger(TYPEID, new Integer(typeID));
		setDouble(VALUE, new Double(value));
		setLong(TIMESTAMP, System.currentTimeMillis());
	}
	
	public Measurement(JSONObject json) throws JSONException {
		super(json);
	}
	
	public Measurement(ResultSet results) {
		super(results);
	}
	
	protected void init() {
		registerInteger(TYPEID);
		registerDouble(VALUE);
		registerLong(TIMESTAMP);
	}
}
