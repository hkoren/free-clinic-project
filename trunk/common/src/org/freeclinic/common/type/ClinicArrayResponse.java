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

import java.util.Iterator;

import org.freeclinic.common.network.exception.InvalidClinicVariableException;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ClinicArrayResponse<T extends ClinicInfo> extends ClinicResponse {

	protected static final String ARRAY = "array";
	
	protected T[] m_itemArray;
	
	public ClinicArrayResponse() {
		super();
	}
	
	public ClinicArrayResponse(int responseCode) {
		super(responseCode);
	}
	
	public ClinicArrayResponse(int responseCode, T[] itemArray) {
		this(responseCode);
		m_itemArray = itemArray;
	}
	
	public ClinicArrayResponse(JSONObject json) throws JSONException {
		super(json);
	}
	
	public abstract void unmarshallArray(JSONObject json) throws JSONException;
	
	@Override
	public void unmarshallJSON(JSONObject json) throws JSONException {
		unmarshallArray(json);
		Iterator keyItr = json.keys();
		Object currId;
		Integer dataType;
		while(keyItr.hasNext()) {
			currId = keyItr.next();
			dataType = m_fieldTypeMap.get(currId);
			if(currId.equals(ARRAY) || currId.toString().startsWith(ARRAY + "@_"))
				continue;
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
		}
	}
	
	@Override
	public JSONObject JSON() throws JSONException {
		JSONObject output = super.JSON();
		output.put(ARRAY, m_itemArray.length);
		for(int i = 0; i < m_itemArray.length; i++)
			output.put(ARRAY + "@_" + i, m_itemArray[i].JSON());		
		return output;
	}
	
	public T[] getArray() {
		return m_itemArray;
	}
	
}
