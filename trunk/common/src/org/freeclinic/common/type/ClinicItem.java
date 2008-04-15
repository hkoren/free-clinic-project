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

import java.util.HashMap;
import java.util.Iterator;

import org.freeclinic.common.network.exception.InvalidClinicTypeException;
import org.freeclinic.common.network.exception.InvalidClinicVariableException;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ClinicItem implements JSONSerializable {
	
	protected static final int BOOLEAN_FIELD = 1;
	protected static final int DOUBLE_FIELD = 2;
	protected static final int INTEGER_FIELD = 3;
	protected static final int LONG_FIELD = 4;
	protected static final int STRING_FIELD = 5;
	
	protected HashMap<String, Integer> m_fieldTypeMap;
	
	protected HashMap<String, Boolean> m_BooleanFieldMap;
	protected HashMap<String, Double> m_DoubleFieldMap;
	protected HashMap<String, Integer> m_IntegerFieldMap;
	protected HashMap<String, Long> m_LongFieldMap;
	protected HashMap<String, String> m_StringFieldMap;
	
	public ClinicItem() {
		m_fieldTypeMap = new HashMap<String, Integer>();
		init();
	}
	
	public ClinicItem(JSONObject json) throws JSONException {
		this();
		unmarshallJSON(json);
	}
	
	protected abstract void init();
	
	public JSONObject JSON() throws JSONException {
		JSONObject json = new JSONObject();
		
		Integer dataType;
		for(String currId : m_fieldTypeMap.keySet()) {
			dataType = m_fieldTypeMap.get(currId);
			try {
				switch(dataType) {
					case BOOLEAN_FIELD:
						json.put(currId.toString(), m_BooleanFieldMap.get(currId).booleanValue());
						break;
					case DOUBLE_FIELD:
						json.put(currId.toString(), m_DoubleFieldMap.get(currId).doubleValue());
						break;
					case INTEGER_FIELD:
						json.put(currId.toString(), m_IntegerFieldMap.get(currId).intValue());
						break;
					case LONG_FIELD:
						json.put(currId.toString(), m_LongFieldMap.get(currId).longValue());
						break;
					case STRING_FIELD:
						json.put(currId.toString(), m_StringFieldMap.get(currId));
						break;
					default:
						throw new InvalidClinicTypeException(dataType);
				} 
			} catch(NullPointerException e) {
				System.err.println("Exception encountered with " + currId);
			}
		}
		
		return json;
	}
	
	public void unmarshallJSON(JSONObject json) throws JSONException {
		Iterator keyItr = json.keys();
		Object currId;
		Integer dataType;
		while(keyItr.hasNext()) {
			currId = keyItr.next();
			dataType = m_fieldTypeMap.get(currId);
			try {
				switch(dataType) {
					case BOOLEAN_FIELD:
						setBoolean(currId.toString(), new Boolean(json.getBoolean(currId.toString())));
						break;
					case DOUBLE_FIELD:
						setDouble(currId.toString(), new Double(json.getDouble(currId.toString())));
						break;
					case INTEGER_FIELD:
						setInteger(currId.toString(), new Integer(json.getInt(currId.toString())));
						break;
					case LONG_FIELD:
						setLong(currId.toString(), new Long(json.getLong(currId.toString())));
						break;
					case STRING_FIELD:
						setString(currId.toString(), new String(json.getString(currId.toString())));
						break;
					default:
						throw new InvalidClinicVariableException(currId.toString());
				}
			} catch(NullPointerException e) {
				throw new InvalidClinicVariableException(currId.toString());
			}
		}
	}
	
	public Boolean getBoolean(String id) {
		return m_BooleanFieldMap.get(id);
	}
	
	public Double getDouble(String id) {
		return m_DoubleFieldMap.get(id);
	}
	
	public Integer getInteger(String id) {
		return m_IntegerFieldMap.get(id);
	}
	
	public Long getLong(String id) {
		return m_LongFieldMap.get(id);
	}
	
	public String getString(String id) {
		return m_StringFieldMap.get(id);
	}
	
	protected boolean setBoolean(String id, Boolean data) {
		if(m_BooleanFieldMap.put(id, data) != null)
			return true;
		else
			return false;
	}
	
	protected boolean setDouble(String id, Double data) {
		if(m_DoubleFieldMap.put(id, data) != null)
			return true;
		else
			return false;
	}
	
	protected boolean setInteger(String id, Integer data) {
		if(m_IntegerFieldMap.put(id, data) != null)
			return true;
		else
			return false;
	}
	
	protected boolean setLong(String id, Long data) {
		if(m_LongFieldMap.put(id, data) != null)
			return true;
		else
			return false;
	}
	
	protected boolean setString(String id, String data) {
		if(m_StringFieldMap.put(id, data) != null)
			return true;
		else
			return false;
	}

	protected boolean registerBoolean(String id) {
		if(m_fieldTypeMap.put(id, BOOLEAN_FIELD) != null)
			return false;
		if(m_BooleanFieldMap == null)
			m_BooleanFieldMap = new HashMap<String, Boolean>();	
		return true;
	}
	
	protected boolean registerDouble(String id) {
		if(m_fieldTypeMap.put(id, DOUBLE_FIELD) != null)
			return false;
		if(m_DoubleFieldMap == null)
			m_DoubleFieldMap = new HashMap<String, Double>();
		return true;
	}
	
	protected boolean registerInteger(String id) {
		if(m_fieldTypeMap.put(id, INTEGER_FIELD) != null)
			return false;
		if(m_IntegerFieldMap == null)
			m_IntegerFieldMap = new HashMap<String, Integer>();
		return true;
	}
	
	protected boolean registerLong(String id) {
		if(m_fieldTypeMap.put(id, LONG_FIELD) != null)
			return false;
		if(m_LongFieldMap == null)
			m_LongFieldMap = new HashMap<String, Long>();
		return true;
	}
	
	protected boolean registerString(String id) {
		if(m_fieldTypeMap.put(id, STRING_FIELD) != null)
			return false;
		if(m_StringFieldMap == null)
			m_StringFieldMap = new HashMap<String, String>();
		return true;
	}
	
	public String toString() {
		String message = "";
		Integer dataType;
		for(String currId : m_fieldTypeMap.keySet()) {
			dataType = m_fieldTypeMap.get(currId);
			message += "\t" + currId + " : ";
			switch(dataType) {
				case BOOLEAN_FIELD:
					message += getBoolean(currId);
					break;
				case DOUBLE_FIELD:
					message += getDouble(currId);
					break;
				case INTEGER_FIELD:
					message += getInteger(currId);
					break;
				case LONG_FIELD:
					message += getLong(currId);
					break;
				case STRING_FIELD:
					message += getString(currId);
					break;
				default:
					throw new InvalidClinicTypeException(dataType);
					
			}
		}
		return message;
	}
}