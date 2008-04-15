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

import java.util.HashMap;

import android.content.Context;

/**
 * @author Cameron Esfahani
 *
 * A simple directory of element tags associated with
 * element Ids. Implemented to avoid id conflicts.
 */
public class IdDirectory {

	private HashMap<String, Integer> directory;
	private Context ctx;
	private int nextId;
	
	/**
	 * protected constructor
	 * Use IdDirectoryFactory for instance.
	 * 
	 * @param context
	 */
	protected IdDirectory(Context context) {
		ctx = context;
		directory = new HashMap<String, Integer>();
		nextId = 1;
	}
	
	/**
	 * Retrieves an element's Id given its tag. This
	 * utility makes the numeric Ids invisible to the
	 * programmer for the purpose of avoiding Id
	 * conflicts. 
	 * 
	 * @param tag
	 * @return
	 */
	protected int getId(String tag) {
		if(directory.containsKey(tag)) {
			return directory.get(tag);
		}
		int id = nextId++;
		directory.put(tag, id);
		return id;
	}
	
	/**
	 * Constructs tags 
	 * 
	 * @param tags
	 */
	protected Integer createId() {
		String tag = new Integer(nextId).toString();
		return getId(tag);
	}
	
}
