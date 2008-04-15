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
package org.freeclinic.common.network.request;

import org.freeclinic.common.info.PatientMessage;
import org.freeclinic.common.info.Vitals;
import org.freeclinic.common.type.ClinicArrayRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class AddPatientMessageRequest extends ClinicArrayRequest<PatientMessage> {
	
	public static final String PATIENT_MESSAGE = "PATIENT_MESSAGE";
	public static final String PATIENTID = "patientID";
	
	public void init() {
		super.init();
		registerInteger(PATIENTID);
	}
	
	public PatientMessage getPatientMessage() {
		return super.first();
	}

	public AddPatientMessageRequest(int patientID, PatientMessage patientMessage) {
		super(RequestType.ADD_PATIENT_MESSAGE, new PatientMessage[] {patientMessage});
		setInteger(PATIENTID, patientID);
	}
	
	public AddPatientMessageRequest(JSONObject json) throws JSONException {
		super(json);
	}
	
	@Override
	public void unmarshallArray(JSONObject json) throws JSONException {
		int size = json.getInt(ARRAY);
		m_itemArray = new PatientMessage[size];
		for (int i=0; i < size; i++) 
			m_itemArray[i] = new PatientMessage(getArrayJSON(json,i));
	}
	public int getPatientID() {
		return getInteger(PATIENTID);
	}
}

