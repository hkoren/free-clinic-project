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
package org.freeclinic.android.component.provider;

import java.util.Date;
import java.util.List;

import org.freeclinic.common.info.Message;
import org.freeclinic.common.info.Patient;
import org.freeclinic.common.info.PatientMessage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class ClinicProvider extends ContentProvider {

	public static final Uri CONTENT_URI = Uri.parse("content://org.freeclinic.provider");
	
	private SQLiteDatabase mDB;
	private Context context;
	
	private static final String TAG = "FCPProvider";
    private static final String DATABASE_NAME = "fcpdata.db";
    private static final int DATABASE_VERSION = 6;

    private static final int PATIENT = 1;
    private static final int PATIENT_ID = 2;
    private static final int PATIENT_MEASUREMENT = 3;
    private static final int PATIENT_MEASUREMENT_ID = 4;
    private static final int PATIENT_MEASUREMENT_TYPE = 5;
    private static final int PATIENT_MESSAGE = 6;
    private static final int PATIENT_MESSAGE_ID = 7;
    private static final int PATIENT_MESSAGE_TYPE = 8;
    private static final int MESSAGE = 9;
    private static final int MESSAGE_ID = 10;
    private static final int MESSAGE_TYPE = 11;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
    
    private static final UriMatcher URL_MATCHER;
    static {
        URL_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URL_MATCHER.addURI("org.freeclinic.provider", "paitent", PATIENT);
        URL_MATCHER.addURI("org.freeclinic.provider", "patient/#", PATIENT_ID);
        URL_MATCHER.addURI("org.freeclinic.provider", "patient/#/measurement", PATIENT_MEASUREMENT);
        URL_MATCHER.addURI("org.freeclinic.provider", "patient/#/measurement/#", PATIENT_MEASUREMENT_ID);
        URL_MATCHER.addURI("org.freeclinic.provider", "patient/#/measurement/type/#", PATIENT_MEASUREMENT_TYPE);
        URL_MATCHER.addURI("org.freeclinic.provider", "patient/#/message", PATIENT_MESSAGE);
        URL_MATCHER.addURI("org.freeclinic.provider", "patient/#/message/#", PATIENT_MESSAGE_ID);
        URL_MATCHER.addURI("org.freeclinic.provider", "patient/#/message/type/#", PATIENT_MESSAGE_TYPE);
        URL_MATCHER.addURI("org.freeclinic.provider", "message", MESSAGE);
        URL_MATCHER.addURI("org.freeclinic.provider", "message/#", MESSAGE_ID);
        URL_MATCHER.addURI("org.freeclinic.provider", "message/type/#", MESSAGE_TYPE);
    }
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		 int count;
        long rowId = 0;
        switch (URL_MATCHER.match(uri)) {
	        case PATIENT:
	            count = mDB.delete("patients", where, whereArgs);
	            break;
	        case PATIENT_ID:
	            String segment = uri.getPathSegments().get(1);
	            rowId = Long.parseLong(segment);
	            count = mDB.delete("notes", "_id="
	                    + segment + (!TextUtils.isEmpty(where) ? " AND (" + where
	                                    + ')' : ""), whereArgs);
	            break;

	        default:
	            throw new IllegalArgumentException("Unknown URL " + uri);
        }

	        getContext().getContentResolver().notifyChange(uri, null);
	        return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		SQLiteOpenHelper dbHelper = new DatabaseHelper();
        context = getContext();
        mDB = dbHelper.openDatabase(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e(TAG, "START PROVIDER");
        return (mDB == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        List<String> segments;
        switch (URL_MATCHER.match(uri)) {
	        case PATIENT:
	            qb.setTables("patients");
	            break;
	        case PATIENT_ID:
	            qb.setTables("patients");
	            qb.appendWhere(Patient.ID + "=" + uri.getPathSegments().get(1));
	            break;
	        case PATIENT_MESSAGE_TYPE:
	        	qb.setTables("patientMessages");
	        	segments = uri.getPathSegments();
	        	Integer patientID = new Integer(segments.get(1));
	        	Integer messageTypeID = new Integer(segments.get(4));
	        	qb.appendWhere(PatientMessage.PATIENT_ID + "=" + patientID);
	        	qb.appendWhere(PatientMessage.TYPE_ID + "=" + messageTypeID);
	        	break;
	        case MESSAGE_TYPE:
	        	qb.setTables("messages");
	        	segments = uri.getPathSegments();
	        	Integer messageTypeNum = new Integer(segments.get(2));
	        	qb.appendWhere(Message.TYPE_ID + "=" + messageTypeNum);
	        	break;
	        case MESSAGE_ID:
		        qb.setTables("messages");
		        segments = uri.getPathSegments();
		        Integer messageIdNum = new Integer(segments.get(1));
		        qb.appendWhere(Message.ID + "=" + messageIdNum);
		        break;	
	        default:
	            throw new IllegalArgumentException("Unknown URL " + uri);
        }

        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
        	// TODO: Find a place to put the orders
            orderBy = "modified DESC";
        } else {
            orderBy = sortOrder;
        }
        if(mDB==null){
        	Log.e(TAG,"The Database is NULL");
        }
        Cursor c = //mDB.rawQuery("select * from messages", null);
        	qb.query(mDB, projection, selection, selectionArgs, "", "", orderBy);
        
        
        
        if (!c.first()) {
        	// No patient was found so we need to retrieve it from the server
        	Log.e(TAG, "No items returned from query");
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
