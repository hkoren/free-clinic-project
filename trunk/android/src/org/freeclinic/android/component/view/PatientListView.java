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
package org.freeclinic.android.component.view;

import org.freeclinic.android.FreeClinicActivity;
import org.freeclinic.android.component.global.Ident;
import org.freeclinic.android.component.global.ViewGenFactory;
import org.freeclinic.android.service.IFCPService;

import android.widget.RelativeLayout;

/**
 * @author David Cun
 */

public class PatientListView extends RelativeLayout  {
		
	public static String TAG = "PatientList";
	public IFCPService service = null; 
	private Ident headerIdent;
	private Ident patientRowIdent;
	private Ident patientPromptIdent;
	private Ident patientFieldIdent;
	private Ident openButtonIdent;
	private FreeClinicActivity context;
	
	public PatientListView(FreeClinicActivity context) {
		super(context);
		this.context = context;
		headerIdent = new Ident(context);
		patientRowIdent = new Ident(context);
		patientFieldIdent = new Ident(context);
		patientPromptIdent = new Ident(context);
		openButtonIdent = new Ident(context);
		
        RelativeLayout header = 
        	ViewGenFactory.getInstance().getViewGen(context).createHeader("Patient List");
        header.setId(headerIdent.getId());
        
	}
	
	
}