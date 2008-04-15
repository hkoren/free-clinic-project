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
package org.freeclinic.android.component.global;

import org.freeclinic.android.R;

import android.content.Context;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

/**
 * @author Cameron Esfahani
 *
 * A ViewGen allows commonly used views to be generated, and
 * therefore improves ease of future changes.
 */
public class ViewGen {

	private Context ctx;
	
	/**
	 * Constructor
	 * 
	 * @param context	Context to construct generated views with
	 */
	protected ViewGen(Context context) {
		ctx = context;
	}
	
	/**
	 * This creates a header with title and logout button
	 * 
	 * @param text
	 * @return
	 */
	public RelativeLayout createHeader(String text) {
		RelativeLayout header = new RelativeLayout(ctx);
		TextView title = createTitle(text);
		header.addView(title);
		
		Button logoutButton = new Button(ctx);
		logoutButton.setText(R.string.logout);
		logoutButton.setOnClickListener(new Listeners.LogoutOnClickListener());
		
		LayoutParams logoutButtonLayout =
			new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		logoutButtonLayout.addRule(RelativeLayout.ALIGN_TOP);
		logoutButtonLayout.addRule(RelativeLayout.ALIGN_WITH_PARENT_RIGHT);
		
		header.addView(logoutButton, logoutButtonLayout);
		
		LayoutParams headerLayout =
			new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		headerLayout.addRule(RelativeLayout.ALIGN_TOP);
		
		header.setLayoutParams(headerLayout);
		
		return header;
	}
	
	/**
	 * This method creates a general use title.
	 * 
	 * @param text  	Text to be used for title.
	 * @return			Uniform style title.
	 */
	public TextView createTitle(String text) {
		TextView title = new TextView(ctx);
		title.setText(text);
		title.setTextSize(24);
		title.setPadding(10, 0, 0, 10);
		title.setTextColor(ctx.getResources().getColor(R.color.royal_blue));
		
		LayoutParams titleLayout = 
        	new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
        			LayoutParams.WRAP_CONTENT);
        titleLayout.addRule(RelativeLayout.ALIGN_TOP);
        titleLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
        
        title.setLayoutParams(titleLayout);
        
		return title;
	}
}
