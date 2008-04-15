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

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

public class PatientMessage extends Message {
	
	public static final String DUE_DATE = "due";
	public static final String CREATED_DATE = "created";
    public static final String MESSAGE_STATUS_ID = "messageStatusID";
    public static final String PATIENT_ID = "patientID";
    public static final String NOTE = "note";
    
    
    public static class Status {
    	public static final int OPEN = 1;
    	public static final int RECOMMENDATION = 2;
    	public static final int SUSPECT = 3;
    	public static final int DUE = 4;
    	public static final int OVERDUE = 5;
    	public static final int PENDING = 6;
    	public static final int CLOSED = 7;
    	
    }
    public PatientMessage(JSONObject json) throws JSONException {
    	super(json);
    }
    public PatientMessage(int patientID, int messageID,String message,String note) {
    	super();
    	Date now = new Date(System.currentTimeMillis());
    	Date due = new Date(System.currentTimeMillis());
    	setCreateDate(now);
    	setDueDate(due);
    	setMessageStatusId(Status.OPEN);
    	setPatientID(patientID);
    	setId(messageID);
    	setName(message);
    	setNote(note);
    }
    
    public PatientMessage(Cursor cursor) {
    	super(cursor);
    }
    
	public PatientMessage(ResultSet results) {
		super(results);
	}
	
	protected void init() {
		super.init();
		registerLong(DUE_DATE);
		registerInteger(MESSAGE_STATUS_ID);
		registerInteger(PATIENT_ID);
	}
	
	public Date getDueDate() {
		return new Date(getLong(DUE_DATE));
	}
	
	public void setDueDate(Date date) {
		setLong(DUE_DATE, date.getTime());
	}
	public Date getCreateDate() {
		return new Date(getLong(CREATED_DATE));
	}
	
	public void setCreateDate(Date date) {
		setLong(CREATED_DATE, date.getTime());
	}
	public void setMessageStatusId(int messageStatusID) {
		setInteger(MESSAGE_STATUS_ID,messageStatusID);
	}
	public String getNote() {
		return getString(NOTE);
	}
	
	public void setNote(String note) {
		setString(NOTE, note);
	}
	
	public Integer getMessageStatusId() {
		return getInteger(MESSAGE_STATUS_ID);
	}
	
	public void setMessageStatusId(Integer messageStatusId) {
		setInteger(MESSAGE_STATUS_ID, messageStatusId);
	}	
	public Integer getPatientID() {
		return getInteger(PATIENT_ID);
	}
	
	public void setPatientID(Integer patientId) {
		setInteger(PATIENT_ID, patientId);
	}
	

}
