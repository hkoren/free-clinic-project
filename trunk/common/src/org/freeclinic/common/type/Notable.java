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
package org.freeclinic.common.type;


/**
 * @author Cameron Esfahani, Trent Tai, Henry Koren
 * 
 * Anything that can have associated notes will be a subclass
 * of this. Notes can be urgent or non-urgent, which may 
 * affect how they are displayed.
 *
 */
public abstract class Notable { //extends ClinicInfo {
	/*public static final String NOTE = "note";
	public static final String URGENT = "urgent";

	protected void init()
	{
		registerString(NOTE);
		registerEBoolean(URGENT);
	}
	
	public Notable() {
		super();
		setBoolean(URGENT,false);
	}
	public Notable(String note, boolean urgent) {
		super();
		setNote(note);
		setUrgent(urgent);
	}
	public Notable(JSONObject json) throws JSONException {
		super(json);
	}
	public Notable(ResultSet rs)  {
		super(rs);
	}
	public String getNote() {
		return getString(NOTE);
	}
	public void setNote(String note) {
		setString(NOTE,note);
	}
	public boolean isUrgent() {
		return getEBoolean(URGENT).get();
	}
	public void setUrgent(boolean urgent) {
		setEBoolean(URGENT, new EBoolean(urgent));
	}
	*/
	
}
