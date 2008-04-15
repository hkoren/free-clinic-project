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

import org.json.JSONException;
import org.json.JSONObject;
/**
 * JSONSerializable Interface.  Classes which implement this interface are able to be converted
 * to an ASCII representation using the JSON format.  We chose JSON over XML because it is smaller, 
 * serializes quicker, and it is included with the Android Application stack natively.  
 * 
 * Here the methods JSON() and unmarshalJSON() should work to undo eachother so that ideally:
 * 
 *  JSON().equals(unmarshalJSON(JSON())
 *  
 *  
 * @author Henry
 *
 */
public interface JSONSerializable {
	/**
	 * Turn the members of the object into a JSONObject representation
	 * @return a JSONObject containing all the information about the object.
	 * @throws JSONException
	 */
	public JSONObject JSON() throws JSONException;
	
	/**
	 * 
	 * @param json The JSONObject containing this objects information.
	 * @throws JSONException
	 */
	public void unmarshallJSON(JSONObject json) throws JSONException;
}