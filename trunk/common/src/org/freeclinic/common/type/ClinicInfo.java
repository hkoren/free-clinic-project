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
package org.freeclinic.common.type;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

public abstract class ClinicInfo extends ClinicItem {
	
	public ClinicInfo() {
		super();
	}
	
	public ClinicInfo(JSONObject json) throws JSONException {
		super(json);
	}
	
	public ClinicInfo(Cursor cursor) {
		super();
		Integer dataType;
		for(String currId : m_fieldTypeMap.keySet()) {
			dataType = m_fieldTypeMap.get(currId);
			switch(dataType) {
				case BOOLEAN_FIELD:
					setBoolean(currId, new Boolean((cursor.getInt(cursor.getColumnIndex(currId))) != 0 ? true : false));
					break;
				case DOUBLE_FIELD:
					setDouble(currId, new Double(cursor.getDouble(cursor.getColumnIndex(currId))));
					break;
				case INTEGER_FIELD:
					setInteger(currId, new Integer(cursor.getInt(cursor.getColumnIndex(currId))));
					break;
				case LONG_FIELD:
					setLong(currId, new Long(cursor.getLong(cursor.getColumnIndex(currId))));
					break;
				case STRING_FIELD:
					setString(currId, new String(cursor.getString(cursor.getColumnIndex(currId))));
					break;
			}
		}
	}
	
	public ClinicInfo(ResultSet results) {
		super();
		Integer dataType;
		for(String currId : m_fieldTypeMap.keySet()) {
			dataType = m_fieldTypeMap.get(currId);
			switch(dataType) {
				case BOOLEAN_FIELD:
					try { 
						setBoolean(currId, new Boolean(results.getBoolean(currId)));
					} catch (SQLException e) {
						System.err.println("Info Construction Error: " + e);
					}
				case DOUBLE_FIELD:
					try {
						setDouble(currId, new Double(results.getDouble(currId)));
					} catch (SQLException e) {
						System.err.println("Info Construction Error: " + e);
					}
					break;
				case INTEGER_FIELD:
					try {
						setInteger(currId, new Integer(results.getInt(currId)));
					} catch (SQLException e) {
						System.err.println("Info Construction Error: " + e);
					}
					break;
				case LONG_FIELD:
					try {
						setLong(currId, new Long(results.getLong(currId)));
					} catch (SQLException e) {
						System.err.println("Info Construction Error: " + e);
					}
					break;
				case STRING_FIELD:
					try {
						setString(currId, results.getString(currId));
					} catch (SQLException e) {
						System.err.println("Info Construction Error: " + e);
					}
					break;
			}
		}
	}
	
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		Integer dataType;
		for(String currId : m_fieldTypeMap.keySet()) {
			dataType = m_fieldTypeMap.get(currId);
			switch(dataType) {
				case BOOLEAN_FIELD:
					values.put(currId, getBoolean(currId));
				case DOUBLE_FIELD:
					values.put(currId, getDouble(currId));
					break;
				case INTEGER_FIELD:
					values.put(currId, getInteger(currId));
					break;
				case LONG_FIELD:
					values.put(currId, getLong(currId));
					break;
				case STRING_FIELD:
					values.put(currId, getString(currId));
					break;
			}
		}
		return values;
	}
	
	protected abstract void init();

}
