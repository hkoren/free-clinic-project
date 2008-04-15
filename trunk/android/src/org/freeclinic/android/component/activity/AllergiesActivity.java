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
import java.util.ArrayList;

import org.freeclinic.android.R;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu.Item;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * AllergiesActivity
 * 
 * Contains the domain logic associated with the Allergy Activity.
 * 
 * @author Joe Nasca
 */
public class AllergiesActivity extends ListActivity {

	// static members
	private static final String[] ALLERGIES = { "penicillin", "latex",
			"pollen", "asprin", "cat dander", "peanut butter", "bee stings",
			"animal dander", "mold", "nickel", "metals", "anticonvulsants",
			"sulfa drugs", "novocain", "insulin", "local", "anesthetics",
			"iodine", "chocolate", "strawberries", "wheat", "codeine",
			"ibuprofen", "nonsteroidal antiinflammatory drugs", "indomethacin",
			"tetracyclin", "phenytoin", "dilantin", "carbamazepine",
			"tegretol", "eggs", "soybeans", "shellfish", "sinusitis",
			"antisera", "antibiotics", "opiates", "primaquine", "vancomycin" };
	private static final int REMOVE_ID = Menu.FIRST;

	// local members
	private AllergyAdapter allergyAdapter;
	private AddButtonListener listener;
	private AutoCompleteTextView textView;
	private int last = -1;	// last selected item index; nothing selected yet

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Use a custom layout file
		setContentView(R.layout.patient_allergies);
		getListView().setEmptyView(findViewById(R.id.empty));
		getListView().setItemsCanFocus(true);

		// Setup the auto-complete text view
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ALLERGIES);
		textView = (AutoCompleteTextView) findViewById(R.id.autotext);
		textView.setAdapter(adapter);
		textView.setCompletionHint("Allergy Database");

		// Setup the patient's allergy list adapter
		allergyAdapter = new AllergyAdapter(this);
		setListAdapter(allergyAdapter);

		// Wire up the add button to add a new allergy
		Button add = (Button) findViewById(R.id.add);
		listener = new AddButtonListener();
		add.setOnClickListener(listener);

	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, REMOVE_ID, "Remove");
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(Item item) {
		switch (item.getId()) {
		case REMOVE_ID:
			if (last >= 0 && last < allergyAdapter.getCount()) {
				// valid array index
				allergyAdapter.remove(last);
				last = -1;
				//allergyAdapter.notifyDataSetChanged();
			}
			return super.onOptionsItemSelected(item);
		default:
			return true;
		}
	}
	
	protected void onListItemClick(ListView l, View v, int pos, long id) {
		last = (int)id;
	}

	/** add button listener **/
	class AddButtonListener implements View.OnClickListener {
		public void onClick(View view) {
			allergyAdapter.add(textView.getText().toString().trim());
			textView.setText("");
		}
	}

	/**
	 * A simple adapter for managing the data behind this List.
	 */
	class AllergyAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<String> patientAllergies = new ArrayList<String>();

		public AllergyAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return patientAllergies.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			allergyView v = new allergyView(mContext, patientAllergies.get(position), position);
			return v;
		}

		public void clear() {
			patientAllergies.clear();
			notifyDataSetChanged();
		}

		public void remove(int remi) {
			patientAllergies.remove(remi);
			notifyDataSetChanged();
		}

		public void add(String str) {
			patientAllergies.add(str);
			notifyDataSetChanged();
		}
	}
	
	private class RemoveListener implements View.OnClickListener {
		private int remNum;
		public RemoveListener(int num){
			super();
			remNum = num;
		}
		public void onClick(View view) {
			allergyAdapter.remove(remNum);
		}
	}
	
	private class allergyView extends LinearLayout{
		private TextView mTitle;
		private Button rem;
		public allergyView(Context cxt, String title, int position){
			super(cxt);
			this.setOrientation(HORIZONTAL);
            
            // Here we build the child views in code. They could also have
            // been specified in an XML file.
			this.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			this.setBackground(R.drawable.list_header);
            mTitle = new TextView(cxt);
            mTitle.setText(title);
            rem = new Button(cxt);
            rem.setText("Remove");
            rem.setTextColor(R.color.red);
            rem.setBackground(R.drawable.fcp_button);
            rem.setOnClickListener(new RemoveListener(position));
            addView(mTitle, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            addView(rem, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
	}
}
