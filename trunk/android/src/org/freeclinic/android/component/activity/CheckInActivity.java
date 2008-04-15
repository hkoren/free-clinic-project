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

import org.freeclinic.android.FreeClinicActivity;
import org.freeclinic.android.R;
import org.freeclinic.android.component.connection.ClinicConnection;
import org.freeclinic.common.info.Patient;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu.Item;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This displays a list of Patients which are checked in to the clinic
 * 
 * @author Trent,Henry
 *
 */
public class CheckInActivity extends ClinicListActivity{
	private Patient[] checkedInPatients;
	private String[] patientNames;
	private String[] patientComplaints;
	private int[] idList;
	private static final int REFRESH=10;
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.patient_list_layout);

        // Use an existing ListAdapter that will map an array
        // of strings to TextViews
        getPatients();
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id){
    	Log.e(TAG, "Position Load: " + idList[position]);
    	if(idList[position]==-1){
    		return;
    	}
		this.startActivity(FreeClinicActivity.patientIntent(this, idList[position]));
    }
    
    private void getPatients() {
    	ClinicConnection.serviceMethod(this, new PatientConnection());
    }
    
    public void announceAdapter(){
    	//ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
       //         android.R.layout.simple_list_item_1, patientNames);
    	setListAdapter(new PatientListAdapter(this));
    	
    }
    
    private void setPatientNames(){
    	if (checkedInPatients==null)
    	{
    		Log.e(TAG, "ERROR NO PATINETS RETURNED");
    		return;
    	}
    	if(checkedInPatients.length==0){
    		patientNames = new String[1];
    		idList = new int[1];
    		patientComplaints = new String[1];
    		patientNames[0] = "No Patients Available";
    		idList[0] = -1;
    		patientComplaints[0] = "";
    		return;
    	}
    	patientNames = new String[checkedInPatients.length];
    	patientComplaints = new String[checkedInPatients.length];
    	idList = new int[checkedInPatients.length];
    	Log.e(TAG, "Size is: " + checkedInPatients.length);
    	for(int i = 0; i < checkedInPatients.length; i++){
    		Log.e(TAG, "Adding: " + checkedInPatients[i].getName());
    		patientNames[i] = checkedInPatients[i].getName();
    		patientComplaints[i] = checkedInPatients[i].getComplaint();
    		//idList[i] = Integer.parseInt(checkedInPatients[i]._ID);
    		idList[i] = checkedInPatients[i].getID();
    	}
    }
    
    private class PatientConnection extends ClinicConnection {
    	public PatientConnection()
    	{
    		super(CheckInActivity.this);
    	}
    	public void action()
        {
        	Log.e("ServiceConnection", "Service Connected. Getting Patients...");
        	Patient[] patientList = service.getCheckInList();
        	Log.e(TAG,"Patient list: " + patientList);
    		checkedInPatients = patientList;
    		setPatientNames();
    		announceAdapter();
        	//PatientCheckInView.this.addContent();
        }
    }
    
    
    
    private class PatientListAdapter extends BaseAdapter {
        public PatientListAdapter(Context context)
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
           	if (checkedInPatients==null)
        	{
        		Log.e(TAG, "ERROR NO PATINET NAMES");
        		return 0;
        	}
            return patientNames.length;
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
            patientRowView sv;
            if (convertView == null) {
                sv = new patientRowView(mContext, patientNames[position], patientComplaints[position]);
            } else {
                sv = (patientRowView)convertView;
                sv.setTitle(patientNames[position]);
            }
            
            return sv;
        } 
        /**
         * Remember our context so we can use it when constructing views.
         */
        private Context mContext;
        
    }
    
    
    private class patientRowView extends LinearLayout {
        public patientRowView(Context context, String title, String complaint) {
            super(context);
            
            this.setOrientation(VERTICAL);
            
            // Here we build the child views in code. They could also have
            // been specified in an XML file.
            
            mTitle = new TextView(context);
            mTitle.setText(title);
            mTitle.setBackground(R.drawable.list_header);
            mTitle.setTextColor(R.drawable.list_color);
            mTitle.setTextSize(20);
            //mTitle.setTextColor(new ColorStateList(Color.BLACK));
            //mTitle.setTextColor(R.color.solid_white);
            addView(mTitle, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            mComplaint = new TextView(context);
            mComplaint.setText(complaint);
            mComplaint.setTextColor(R.drawable.list_color);
            mComplaint.setBackground(R.color.light_blue);
            mComplaint.setTextSize(15);
            //mTitle.setTextColor(new ColorStateList(Color.BLACK));
            //mTitle.setTextColor(R.color.solid_white);
            addView(mComplaint, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            LinearLayout spacer = new LinearLayout(context);
            spacer.setBackground(R.color.dark_red);
            addView(spacer, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 3));
        }
        
        public void setTitle(String title) {
            mTitle.setText(title);
        }
        
        private TextView mTitle, mComplaint;
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		Boolean res = super.onCreateOptionsMenu(menu);
		Item logoff = menu.add(0, REFRESH, R.string.refresh);
		logoff.setIcon(R.drawable.refresh);
		
		return res;
		
	}

	@Override
	public boolean onOptionsItemSelected(Item item) {
		// TODO Auto-generated method stub
		boolean res = super.onOptionsItemSelected(item);
		if (!res) {
			if (item.getId() == REFRESH)
			{
				getPatients();
			}
		}
		return res;
	}
	
}