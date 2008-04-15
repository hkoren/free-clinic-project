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

import org.freeclinic.common.info.Measurement;
import org.freeclinic.common.type.ClinicArrayResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class MeasurementListResponse extends ClinicArrayResponse<Measurement> {
	
	public static final String TAG = "MeasurementListResponse";
	
	public MeasurementListResponse() {
		super(ResponseType.PATIENT_MEASUREMENT_LIST);
	}
	
	public MeasurementListResponse(Measurement[] measurements) {
		super(ResponseType.PATIENT_MEASUREMENT_LIST, measurements);
	}
	
	public MeasurementListResponse(JSONObject json) throws JSONException {
		super(json);
	}
	
	@Override
	public void unmarshallArray(JSONObject json) throws JSONException {
		int size = json.getInt(ClinicArrayResponse.ARRAY);
		super.m_itemArray = new Measurement[size];
		Log.e(TAG, size + " patient measurements");
		for (int i=0; i < size; i++)
		{
			super.m_itemArray[i] = new Measurement(json.getJSONObject(ClinicArrayResponse.ARRAY + "@_" + i));
			Log.e(TAG, "Adding Measurement" + m_itemArray[i]);
		}	
	}

}

