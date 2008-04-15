package org.freeclinic.common.network.request;

import org.freeclinic.common.info.Vitals;
import org.freeclinic.common.type.ClinicArrayRequest;
import org.json.JSONException;
import org.json.JSONObject;

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
public class PutVitalsRequest extends ClinicArrayRequest<Vitals> {
	
	public static final String PATIENTID = "patientID";
	
	public PutVitalsRequest() {
		super(RequestType.PUT_VITALS);
	}
	
	public PutVitalsRequest(Integer patientID, Vitals vitals) {
		super(RequestType.PUT_VITALS, new Vitals[] {vitals});
		setInteger(PATIENTID, patientID);
	}
	
	public PutVitalsRequest(Integer patientID, Vitals[] vitals) {
		super(RequestType.PUT_VITALS, vitals);
		setInteger(PATIENTID, patientID);
	}
	
	public PutVitalsRequest(JSONObject json) throws JSONException {
		super(json);
	}
	
	@Override
	public void init() {
		super.init();
		registerInteger(PATIENTID);
	}
	
	@Override
	public void unmarshallArray(JSONObject json) throws JSONException {
		int size = json.getInt(ClinicArrayRequest.ARRAY);
		m_itemArray = new Vitals[size];
		for (int i=0; i < size; i++)
			m_itemArray[i] = new Vitals(json.getJSONObject(ClinicArrayRequest.ARRAY + "@_" + i));
	}
	
	public Integer getPatientID() {
		return getInteger(PATIENTID);
	}
	
	public void setPatientID(Integer patientID) {
		setInteger(PATIENTID, patientID);
	}
}
