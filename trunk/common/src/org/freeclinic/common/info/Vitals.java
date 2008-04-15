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

public class Vitals extends ClinicInfo {

	public static final String ID = "_id";
	public static final String TEMP = "TEMP";
	public static final String TEMP_TIME = "TEMP_TIME";
	public static final String HR = "HR";
	public static final String HR_TIME = "HR_TIME";
	public static final String SYST = "SYST";
	public static final String SYST_TIME = "SYST_TIME";
	public static final String DIAS = "DIAS";
	public static final String DIAS_TIME = "DIAS_TIME";
	public static final String RESP = "RESP";
	public static final String RESP_TIME = "RESP_TIME";

	public Vitals(Double temp, Date temp_time,
				Double hr, Date hr_time,
				Double syst, Date syst_time,
				Double dias, Date dias_time,
				Double resp, Date resp_time) {
		super();
		setTemp(temp);
		setTempTime(temp_time);
		setHr(hr);
		setHrTime(hr_time);
		setSyst(syst);
		setSystTime(syst_time);
		setDias(dias);
		setDiasTime(dias_time);
		setResp(resp);
		setRespTime(resp_time);
	}
	
	public Vitals(Double temp, Double hr, Double syst, Double dias, Double resp) {
		super();
		Date now = new Date(System.currentTimeMillis());
		setTemp(temp);
		setTempTime(now);
		setHr(hr);
		setHrTime(now);
		setSyst(syst);
		setSystTime(now);
		setDias(dias);
		setDiasTime(now);
		setResp(resp);
		setRespTime(now);
	}
	
	public Vitals(ResultSet results) {
		super(results);
	}
	
	public Vitals(JSONObject json) throws JSONException {
		super(json);
	}
	
	public Vitals(Cursor cursor) {
		super(cursor);
	}
	
	@Override
	protected void init() {
		registerInteger(ID);
		registerDouble(TEMP);
		registerLong(TEMP_TIME);
		registerDouble(HR);
		registerLong(HR_TIME);
		registerDouble(SYST);
		registerLong(SYST_TIME);
		registerDouble(DIAS);
		registerLong(DIAS_TIME);
		registerDouble(RESP);
		registerLong(RESP_TIME);
	}

	public Double getTemp() {
		return getDouble(TEMP);
	}

	public void setTemp(Double temp) {
		setDouble(TEMP, temp);
	}

	public Date getTempTime() {
		return new Date(getLong(TEMP_TIME));
	}

	public void setTempTime(Date temp_time) {
		setLong(TEMP_TIME, temp_time.getTime());
	}

	public Double getHr() {
		return getDouble(HR);
	}

	public void setHr(Double hr) {
		setDouble(HR, hr);
	}

	public Date getHrTime() {
		return new Date(getLong(HR_TIME));
	}

	public void setHrTime(Date hr_time) {
		setLong(HR_TIME, hr_time.getTime());
	}

	public Double getSyst() {
		return getDouble(SYST);
	}

	public void setSyst(Double syst) {
		setDouble(SYST, syst);
	}

	public Date getSystTime() {
		return new Date(getLong(SYST_TIME));
	}

	public void setSystTime(Date syst_time) {
		setLong(SYST_TIME, syst_time.getTime());
	}

	public Double getDias() {
		return getDouble(DIAS);
	}

	public void setDias(Double dias) {
		setDouble(DIAS, dias);
	}

	public Date getDiasTime() {
		return new Date(getLong(DIAS_TIME));
	}

	public void setDiasTime(Date dias_time) {
		setLong(DIAS_TIME, dias_time.getTime());
	}

	public Double getResp() {
		return getDouble(RESP);
	}

	public void setResp(Double resp) {
		setDouble(RESP, resp);
	}

	public Date getRespTime() {
		return new Date(getLong(RESP_TIME));
	}

	public void setRespTime(Date resp_time) {
		setLong(RESP_TIME, resp_time.getTime());
	}
}
