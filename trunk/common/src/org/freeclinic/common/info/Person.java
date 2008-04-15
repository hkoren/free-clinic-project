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
import java.util.Date;

import org.freeclinic.common.type.ClinicInfo;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

/**
 * @author Cameron Esfahani, Trent Tai, Henry Koren
 * 
 * A general person, contains basic information.
 *
 */
public class Person extends ClinicInfo {
  
	// Declare Keys
	public static final String ID = "_id";
	public static final String TITLE = "title";
	public static final String FIRSTNAME = "firstName";
	public static final String MIDDLENAME = "middleName";
	public static final String LASTNAME = "lastName";
	public static final String SUFFIX="suffix";
	public static final String GENDER="gender";
	public static final String DATEOFBIRTH="dateOfBirth";
	public static final String HOMENUM="homeNum";
	public static final String WORKNUM="workNum";
	public static final String CELLNUM="cellNum";
	public static final String ADDRESS="address";
	public static final String STATE="state";
	public static final String CITY="city";
	public static final String ZIPCODE="zipCode";
	public static final String CREATED_DATE = "created";
    public static final String MODIFIED_DATE = "modified";

    public Person() {
    	super();
    }
    
    public Person(JSONObject json) throws JSONException {
    	super(json);
    }
    
    public Person(Cursor cursor) {
    	super(cursor);
    }
    
    public Person(ResultSet results) {
    	super(results);
    }
    
	protected void init() {
		registerInteger(ID);
		registerString(TITLE);
		registerString(FIRSTNAME);
		registerString(MIDDLENAME);
		registerString(LASTNAME);
		registerString(SUFFIX);
		registerString(GENDER);
		registerLong(DATEOFBIRTH);
		registerInteger(HOMENUM);
		registerInteger(WORKNUM);
		registerInteger(CELLNUM);
		registerString(ADDRESS);
		registerString(STATE);
		registerString(CITY);
		registerInteger(ZIPCODE);
		registerLong(CREATED_DATE);
		registerLong(MODIFIED_DATE);
	}
		
	// Getters / Setters
	
	public Integer getID() {
		return getInteger(ID);
	}
	
	public void setID(Integer id) {
		setInteger(ID, id);
	}
	
	public String getTitle() {
		return getString(TITLE);
	}

	public void setTitle(String title) {
		setString(TITLE, title);
	}

	public String getFirstName() {
		return getString(FIRSTNAME);
	}

	public void setFirstName(String firstName) {
		setString(FIRSTNAME, firstName);
	}

	public String getMiddleName() {
		return getString(MIDDLENAME);
	}

	public void setMiddleName(String middleName) {
		setString(MIDDLENAME, middleName);
	}

	public String getLastName() {
		return getString(LASTNAME);
	}

	public void setLastName(String lastName) {
		setString(LASTNAME, lastName);
	}

	public String getSuffix() {
		return getString(SUFFIX);
	}

	public void setSuffix(String suffix) {
		setString(SUFFIX, suffix);
	}

	public String getGender() {
		return getString(GENDER);
	}

	public void setGender(String gender) {
		setString(GENDER, gender);
	}

	public Date getDateOfBirth() {
		return new Date(getLong(DATEOFBIRTH));
	}

	public void setDateOfBirth(Date dateOfBirth) {
		setLong(DATEOFBIRTH, dateOfBirth.getTime());
	}

	public Integer getHomeNum() {
		return getInteger(HOMENUM);
	}

	public void setHomeNum(Integer homeNum) {
		setInteger(HOMENUM, homeNum);
	}

	public Integer getWorkNum() {
		return getInteger(WORKNUM);
	}

	public void setWorkNum(Integer workNum) {
		setInteger(WORKNUM, workNum);
	}

	public Integer getCellNum() {
		return getInteger(CELLNUM);
	}

	public void setCellNum(Integer cellNum) {
		setInteger(CELLNUM, cellNum);
	}

	public String getAddress() {
		return getString(ADDRESS);
	}

	public void setAddress(String address) {
		setString(ADDRESS, address);
	}

	public String getState() {
		return getString(STATE);
	}

	public void setState(String state) {
		setString(STATE, state);
	}

	public String getCity() {
		return getString(CITY);
	}

	public void setCity(String city) {
		setString(CITY, city);
	}

	public Integer getZipCode() {
		return getInteger(ZIPCODE);
	}

	public void setZipCode(Integer zipCode) {
		setInteger(ZIPCODE, zipCode);
	}
	
	public String toString() {
		return getLastName() + ", " + getFirstName();
	}
	
	/**
	 * Determine age of person
	 * @return age in years
	 */
	public float getAge() {
		Date ddob = getDateOfBirth();
		if (ddob != null)
		{
			long age = System.currentTimeMillis() - ddob.getTime();
			float ageInYears = age / 31557600000L;
			return ageInYears;
		}
		return -1;
	}
	
	public String getName() {
		return getLastName() + ", " + 
			getFirstName() + " " + 
			getMiddleName();
	}
}
