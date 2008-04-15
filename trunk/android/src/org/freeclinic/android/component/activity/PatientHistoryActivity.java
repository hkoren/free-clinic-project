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

import java.util.Arrays;
import java.util.Comparator;

import org.freeclinic.android.R;
import org.freeclinic.android.service.FCPService;
import org.freeclinic.common.info.Message;
import org.freeclinic.common.info.PatientMessage;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PatientHistoryActivity extends ClinicListActivity {
	/**
	 * This method displays the PatientMessages associated with
	 * a given Patient ID in an ordered list with sort options.
	 */
	private static final String TAG  = "PatientHistory";
	private int patientId;
	PatientMessage[] patientMessages;
	
	public static final int SORT_DATE = 0;
	public static final int SORT_TYPE = 1;
	public static final int SORT_NAME = 2;
	
	private boolean[] mExpanded;
	String[] messageTitle;
	String[] messageContent;
	Button all,referrals,diagnoses,labs,prescriptions,treatments,date,type,name;
	SpeechListAdapter sAdapter;
	HistoryConnection hConnection;
	
	static class compare_date implements Comparator{
		public int compare(Object a, Object b){
			return ((PatientMessage)a).getCreateDate().compareTo(((PatientMessage)b).getCreateDate());
		}	
	}
	
	static class compare_type implements Comparator{
		public int compare(Object a, Object b){
			return (new Integer(((PatientMessage)a).getTypeID())).compareTo(new Integer(((PatientMessage)b).getTypeID()));
		}
	}
	
	static class compare_name implements Comparator{
		public int compare(Object a, Object b){
			return ((PatientMessage)a).getName().compareTo(((PatientMessage)b).getName());
		}
	}
	
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.patient_history);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
            patientId = extras.getInt("patientID");
            Log.e(TAG,"Patient Id Is: "+patientId);
        }
		else{
			Log.e(TAG,"Patient Id Unavailable");
			patientId = -1;
		}
		hConnection = new HistoryConnection();
		this.serviceMethod(hConnection);
		
		Button all = (Button)findViewById(R.id.all);
		Button referrals= (Button)findViewById(R.id.referrals);
		Button diagnoses= (Button)findViewById(R.id.diagnoses);
		Button labs= (Button)findViewById(R.id.labs);
		Button prescriptions= (Button)findViewById(R.id.prescriptions);
		Button treatments= (Button)findViewById(R.id.treatments);
		Button date= (Button)findViewById(R.id.date);
		Button type= (Button)findViewById(R.id.type);
		Button name= (Button)findViewById(R.id.name);
		
		all.setOnClickListener(new ChangeMessageListener(Message.Type.ALL));
		referrals.setOnClickListener(new ChangeMessageListener(Message.Type.REFERRAL));
		diagnoses.setOnClickListener(new ChangeMessageListener(Message.Type.DIAGNOSIS));
		labs.setOnClickListener(new ChangeMessageListener(Message.Type.LAB_TEST));
		prescriptions.setOnClickListener(new ChangeMessageListener(Message.Type.PRESCRIPTION));
		treatments.setOnClickListener(new ChangeMessageListener(Message.Type.TREATMENT));
		
		// Sort Buttons
		date.setOnClickListener(new ChangeSortListener(SORT_DATE));
		type.setOnClickListener(new ChangeSortListener(SORT_TYPE));
		name.setOnClickListener(new ChangeSortListener(SORT_NAME));
		
		
		
	}
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {    
    	/**
    	 * Handles the expansion of list elements.
    	 */
       ((SpeechListAdapter)getListAdapter()).toggle(position);
    }
    
    private String getMessageTypeName(int message){
    	switch(message) {
		case Message.Type.PRESCRIPTION:
			return "Perscription";
		case Message.Type.REFERRAL:
			return "Referal";
		case Message.Type.TREATMENT:
			return "Treatment";
		case Message.Type.NOTATION:
			return "Note";
		case Message.Type.DIAGNOSIS:
			return "Diagnosis";
		default:
			return "Other";
		}
    }
    
	/**
	 * Take the newly populated patient messages object and update the list view
	 */
	private void setMessages(){
		if (patientMessages != null) {
			if(patientMessages.length==0){
				messageTitle = new String[1];
				messageTitle[0] = "No Messages";
				messageContent = new String[1];
				messageTitle[0] = "";
				mExpanded = new boolean[1];
				mExpanded[0] = false;
				return;
			}
			messageTitle = new String[patientMessages.length];
			messageContent = new String[patientMessages.length];
			mExpanded = new boolean[patientMessages.length];
			for(int i = 0; i < patientMessages.length; i++){
				messageTitle[i] = getMessageTypeName(patientMessages[i].getTypeID())
				+" - "+ patientMessages[i].getName();	
				Log.e(TAG, "Adding Date: "+patientMessages[i].getCreateDate());
				messageContent[i] = "Date: " + patientMessages[i].getCreateDate() + "\nNotes: ";
				mExpanded[i] = false;
			}
		}
		else
		{
			Log.e(TAG, "Null Patient Messages");
		}
	}
	
	private void announceAdapter(){
		//ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
         //       android.R.layout.simple_list_item_1, messages);
		//MessageListView lview = (MessageListView)findViewById(R.id.list);
		//lview.setAdapter(mAdapter);
		sAdapter = new SpeechListAdapter(this);
    	setListAdapter(sAdapter);
	}
	
	/**
	 * Filter the MessageListAdapter to a particular type, or 0 for all
	 * @param messageTypeID
	 */
    public void setMessageTypeID(int messageTypeID) 
    {
    	hConnection.updateList(messageTypeID);
    	// TODO: Filter the MessageListAdapter to a particular type 0=all
    }
    
    public void setSort(int sortID)
    {
    	// TODO: Change sort order of message list
    	switch(sortID){
    	case(PatientHistoryActivity.SORT_TYPE):
	    	Arrays.sort(patientMessages, new compare_type());
    		break;
    	case(PatientHistoryActivity.SORT_DATE):
	    	Arrays.sort(patientMessages, new compare_date());
    		break;
    	case(PatientHistoryActivity.SORT_NAME):
	    	Arrays.sort(patientMessages, new compare_name());
    	}
    	setMessages();
    	announceAdapter();
    }

	private class HistoryConnection implements ServiceConnection{
		private FCPService service; 
        public void onServiceConnected(ComponentName className, IBinder iservice)
        {
        	Log.e("ServiceConnection", "Service Connected. Getting Patients...");
        	service =  bindService(iservice);
        	//patientMessages = service.getPatientMessages(patientId, Message.DIAGNOSIS);
        	patientMessages = service.getPatientMessages(patientId);
        	if(patientMessages ==  null){
        		Log.e(TAG,"Error: patient messges null");
        	}
        	else
        	{
        		setMessages();
        		announceAdapter();
        	}
        	//PatientCheckInView.this.addContent();
        }
        
        public void updateList(int messageId){
        	patientMessages = service.getPatientMessages(patientId, messageId);
        	if(patientMessages ==  null){
        		Log.e(TAG,"Error: patient messges null");
        	}
        	else
        	{
        		setMessages();
        		sAdapter.notifyDataSetChanged();
        	}
        }

        public void onServiceDisconnected(ComponentName className)
        {

        }
        
	}

	/**
	 * 
	 * @author Henry
	 *
	 */
	class ChangeMessageListener implements View.OnClickListener {
		private int messageTypeID;
		public ChangeMessageListener(int messageTypeID)
		{
			this.messageTypeID = messageTypeID;
		}
		public void onClick(View view) {
			setMessageTypeID(messageTypeID);
		}
	}
	class ChangeSortListener implements View.OnClickListener {
		private int sortID;
		public ChangeSortListener(int sortID)
		{
			this.sortID = sortID;
		}
		public void onClick(View view) {
			setSort(sortID);
		}
	}
	
	
	
    private class SpeechListAdapter extends BaseAdapter {
        public SpeechListAdapter(Context context)
        {
            mContext = context;
        }

        
        /**
         * The number of items in the list is determined by the number of speeches
         * in our array.
         * 
         * @see android.widget.ListAdapter#getCount()
         */
        public int getCount() {
            return messageTitle.length;
        }

        /**
         * Since the data comes from an array, just returning
         * the index is sufficent to get at the data. If we
         * were using a more complex data structure, we
         * would return whatever object represents one 
         * row in the list.
         * 
         * @see android.widget.ListAdapter#getItem(int)
         */
        public Object getItem(int position) {
            return position;
        }

        /**
         * Use the array index as a unique id.
         * @see android.widget.ListAdapter#getItemId(int)
         */
        public long getItemId(int position) {
            return position;
        }

        /**
         * Make a SpeechView to hold each row.
         * @see android.widget.ListAdapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            HistoryView sv;
            if (convertView == null) {
                sv = new HistoryView(mContext, messageTitle[position], messageContent[position], mExpanded[position]);
            } else {
                sv = (HistoryView)convertView;
                sv.setTitle(messageTitle[position]);
                sv.setDialogue(messageContent[position]);
                sv.setExpanded(mExpanded[position]);
            }
            
            return sv;
        }

        public void toggle(int position) {
            mExpanded[position] = !mExpanded[position];
            notifyDataSetChanged();
        }
        
        /**
         * Remember our context so we can use it when constructing views.
         */
        private Context mContext;
        
    }
    private class HistoryView extends LinearLayout {
        public HistoryView(Context context, String title, String dialogue, boolean expanded) {
            super(context);
            
            this.setOrientation(VERTICAL);
            
            // Here we build the child views in code. They could also have
            // been specified in an XML file.
            
            mTitle = new TextView(context);
            mTitle.setText(title);
            mTitle.setBackground(R.drawable.fcp_button);
            mTitle.setTextColor(R.drawable.list_color);
            //mTitle.setTextColor(R.color.solid_white);
            addView(mTitle, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            
            mDialogue = new TextView(context);
            mDialogue.setText(dialogue);
            mDialogue.setBackground(R.color.solid_white);
            mDialogue.setTextColor(R.drawable.list_color);
            addView(mDialogue, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            
            mDialogue.setVisibility(expanded ? VISIBLE : GONE);
        }
        
        /**
         * Convenience method to set the title of a SpeechView
         */
        public void setTitle(String title) {
            mTitle.setText(title);
        }
        
        /**
         * Convenience method to set the dialogue of a SpeechView
         */
        public void setDialogue(String words) {
            mDialogue.setText(words);
        }
        
        /**
         * Convenience method to expand or hide the dialogue
         */
        public void setExpanded(boolean expanded) {
            mDialogue.setVisibility(expanded ? VISIBLE : GONE);
        }
        
        private TextView mTitle;
        private TextView mDialogue;
    }
}

