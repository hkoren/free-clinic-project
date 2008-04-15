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
package org.freeclinic.android.component.activity;

import java.util.StringTokenizer;

import org.freeclinic.android.R;
import org.freeclinic.android.component.connection.ClinicConnection;
import org.freeclinic.common.info.Message;
import org.freeclinic.common.info.Patient;
import org.freeclinic.common.info.PatientMessage;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PatientMessageActivity extends ClinicActivity {
	private static final String TAG = "PatientMessageActivity";
	private static final int STARTPATIENTID=1;
	private static final int FIRSTNAME_INDEX = 1;
	private static final int MIDDLENAME_INDEX = 2;
	private static final int LASTNAME_INDEX = 1;
	// Menu options
	private static final int REFRESH = 1;
	private static final int DETAILS = 2;
	
	private Uri mURI;
	private int patientID;
	private int MessageTypeId;
	private AutoCompleteTextView aTextView;
	private EditText noteText;
	
	private Button add;
	private Button cancel;

	public String uriString;
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		// Fire up service 
		final Intent intent = getIntent();
		mURI = intent.getData();
		Log.e(TAG, "Got URI " + mURI);
		
		setContentView(R.layout.patient_message_edit);
		
		aTextView = (AutoCompleteTextView) findViewById(R.id.autotext);
		noteText = (EditText) findViewById(R.id.noteText);
		// Parse the patient ID out of the URI 
		patientID = Integer.parseInt(mURI.getPathSegments().get(2));
		TextView patientIDView  = (TextView)findViewById(R.id.patientViewID);
		patientIDView.setText("Patient Id: " + patientID);
		
		TextView messageTag = (TextView)findViewById(R.id.messageTag);
		
		MessageTypeId = Integer.parseInt(mURI.getPathSegments().get(1));
		
		switch(MessageTypeId) {
		case Message.Type.PRESCRIPTION:
			messageTag.setText("Perscription: ");
			break;
		case Message.Type.REFERRAL:
			messageTag.setText("Refer To: ");
			break;
		case Message.Type.TREATMENT:
			messageTag.setText("Treatment: ");
			break;
		case Message.Type.NOTATION:
			messageTag.setText("Title: ");
			break;
		case Message.Type.DIAGNOSIS:
			messageTag.setText("Diagnosis: ");
			break;
		default:
			messageTag.setText("Default: " + MessageTypeId);
		break;
		}
		
		ContentResolver content = getContentResolver();
		
		uriString = "content://org.freeclinic.provider/message/type/"+MessageTypeId;
		Log.e(TAG,"Starting Query");
		Cursor c = content.query(Uri.parse(uriString), null, null, null, null);
		//Cursor c = managedQuery(Uri.parse(uriString), null, null, null);
		if(c==null){
			Log.e(TAG, "Top cursor null");
		}
		else{
			Log.e(TAG, "Top cursor not null");
		}
		//AutoCompleteTextView autoText = (AutoCompleteTextView)findViewById(R.id.autotext);
		//autoText.setText(MessageTypeId);
		MessageListAdapter adapter = new MessageListAdapter(c,this,uriString);
		
		aTextView.setAdapter(adapter);
		//setListAdpater(adapter);
		initAddNotes();
	}

	private void initAddNotes() {
		add = (Button)findViewById(R.id.add_note_button);
		add.setOnClickListener(new AddMessageOnClickListener());
		
		cancel = (Button)findViewById(R.id.cancel_note_button);
		cancel.setOnClickListener(new CancelNotesOnClickListener());
	}
	@Override
	public void onContentChanged() {
		// TODO Auto-generated method stub
		super.onContentChanged();
	}

	public PatientMessageActivity() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * Create Menu for Patient
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0,DETAILS,R.string.details);
		return super.onCreateOptionsMenu(menu);
	}
	

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	/**
	 * Add A message to the users record
	 * This will call an activity that will have the doctor choose the type of message they wish
	 * to search for and then be able search through the message database.  The activity should then call back.
	 */
	public void addMessage(Message message)
	{
		Toast.makeText(this, "Message Added",Toast.LENGTH_SHORT);
		finish();
	}
	


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e(TAG,"pausing...");
	}
	
	private class AddMessageOnClickListener implements View.OnClickListener {
		
		public void onClick(View v) {
			String note = noteText.getText().toString();
			String message = aTextView.getText().toString();
			int pId = patientID;
			int mId;
			StringTokenizer st = new StringTokenizer(message, ":");
			if(st.countTokens()<2){
				mId = 0;
			}
			else{
				try{
					mId = Integer.parseInt(st.nextToken());
				}
				catch(Exception e){
					mId = 0;
				}
			}
			Log.e(TAG, "Message Type: " + mId);
			
			ClinicConnection.serviceMethod(PatientMessageActivity.this,new AddMessageConnection(PatientMessageActivity.this, pId, mId, note,message));
			finish();
		}
	}
		
	private class CancelNotesOnClickListener implements View.OnClickListener {
		
		public void onClick(View v) {
			finish();
		}
	}
	
	private static class MessageListAdapter
		extends CursorAdapter implements Filterable {
		private String mUriString;
		public MessageListAdapter(Cursor c, Context context, String uriString) {
		    super(c, context);
		    mUriString = uriString;
		    mContent = context.getContentResolver();
		}
		
		
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			Message m = new Message(cursor);
			String message = m.getId()+": "+m.getName();
		    MessageView view = new MessageView(context,message);
		    //view.setText(cursor, 5);
		    return view;
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			Message m = new Message(cursor);
			String message = m.getId()+": "+m.getName();
		    ((MessageView) view).setText(message);
		}
		
		@Override
		protected String convertToString(Cursor cursor) {
			Message m = new Message(cursor);
			String message = m.getId()+": "+m.getName();
		    return message;
		}
		

        @Override
        protected Cursor runQuery(CharSequence constraint) {
            //StringBuilder buffer = null;
        	String buffer = null;
            String[] args = null;
            if (constraint != null && !constraint.toString().equals("")) {
                //buffer = new StringBuilder();
                //buffer.append("UPPER(");
                //buffer.append(Message.NAME);
                //buffer.append(") GLOB ?");
                buffer = "UPPER("+Message.NAME+") LIKE '%" + constraint.toString().toUpperCase()+"%'";
                //args = new String[] { constraint.toString().toUpperCase() + "*" };
            }
            Cursor ret = mContent.query(Uri.parse(mUriString), null,
                    buffer == null ? null : buffer, args,
                    null);
            return ret;
        }
		private ContentResolver mContent;        
		}
	
	private static class MessageView extends LinearLayout{
		private TextView mTitle;
		public MessageView(Context cxt, String title){
			super(cxt);
			this.setOrientation(HORIZONTAL);
            
            // Here we build the child views in code. They could also have
            // been specified in an XML file.
			this.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			//this.setBackground(R.drawable.list_header);
            mTitle = new TextView(cxt);
            mTitle.setText(title);
            addView(mTitle, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		public void setText(String text){
			mTitle.setText(text);
		}
	}
	public class AddMessageConnection extends ClinicConnection {
		PatientMessage patientMessage;
		PatientMessageActivity pma;
		
		
		public AddMessageConnection(Context context,int patientID, int messageID, String notes, String message) {
			super(context);
			patientMessage = new PatientMessage(patientID,messageID,message,notes);
		}
		
		
	    public void action()
	    {
	    	service.putPatientMessage(patientID, patientMessage);
	    	addMessage(patientMessage);
	    }
	}

	
}
