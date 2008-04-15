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

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class Message extends ClinicInfo {
	
	public static final String ID = "_id";
	public static final String TYPE_ID = "messageTypeID";
	public static final String NAME = "name";
	public static final String CREATED_DATE = "created";
	public static final String MODIFIED_DATE = "modified";

	public static class Type {
		public static final int ALL = 0;
		public static final int DIAGNOSIS = 1;
		public static final int REFERRAL = 2;
		public static final int PRESCRIPTION = 3;
		public static final int NOTATION = 4;
		public static final int PATIENT_ASSISTANCE = 5;
		public static final int TREATMENT = 6;
		public static final int LAB_TEST = 7;
	}
	
	public Message() {
		super();
	}
	
	public Message(int typeId, String name) {
		super();
		setInteger(TYPE_ID, typeId);
		setString(NAME, name);
	}

	public Message(JSONObject json) throws JSONException {
		super(json);
	}
	
	public Message(Cursor cursor) {
		super(cursor);
	}
	
	public Message(ResultSet results) {
		super(results);
	}
	
	@Override
	protected void init() {
		registerInteger(ID);
		registerInteger(TYPE_ID);
		registerString(NAME);
		registerLong(CREATED_DATE);
		registerLong(MODIFIED_DATE);
	}

	public void setId(int id) {
		setInteger(ID, id);
	}
	public int getId() {
		return getInteger(ID);
	}
	
	public int getTypeID() {
		return getInteger(TYPE_ID);
	}
	
	public String getName() {
		return getString(NAME);
	}
	
	public void setName(String name)
	{
		setString(NAME,name);
	}
	public Date getCreateDate() {
		return new Date(getLong(CREATED_DATE));
	}
	
	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(NAME, getString(NAME));
		values.put(TYPE_ID, getInteger(TYPE_ID));
		values.put(CREATED_DATE, getLong(CREATED_DATE));
		values.put(MODIFIED_DATE, getLong(MODIFIED_DATE));
		return values;
	}
	
}
