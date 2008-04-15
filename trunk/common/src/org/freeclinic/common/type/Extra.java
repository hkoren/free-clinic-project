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

public abstract class Extra<E> implements JSONSerializable {
//	private static String TAG="Extra";
	protected static String NUGGET="n";
	protected E nugget;
	
	public Extra(E nug) {
		nugget = nug;
	}

	
	public JSONObject JSON() throws JSONException {
		JSONObject output = new JSONObject();
		return output;
	}

	public void unmarshallJSON(JSONObject json) throws JSONException {

	}
	
	public Extra(JSONObject json) throws JSONException {
		this.unmarshallJSON(json);
	}
	public E get() {
		return nugget;
	}
	
	public void set(E in) {
		nugget = in;
	}
	public String toString()
	{
		return nugget != null ? nugget.toString() : null;
	}

}
