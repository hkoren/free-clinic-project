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
 * Factory to produce IdDirectory Objects. Produces one for
 * each unique Context passed in.
 *
 */
public class IdDirectoryFactory {
	
	private static IdDirectoryFactory s_instance;
	private HashMap<Context, IdDirectory> idDirectoryMap;
	
	private IdDirectoryFactory() {
		idDirectoryMap = new HashMap<Context, IdDirectory>();
	}
	
	protected static IdDirectoryFactory getInstance() {
		if(s_instance == null) {
			s_instance = new IdDirectoryFactory();
		}
		return s_instance;
	}
	
	protected IdDirectory getIdDirectory(Context ctx) {
		if(idDirectoryMap.containsKey(ctx)) {
			return idDirectoryMap.get(ctx);
		}
		IdDirectory id = new IdDirectory(ctx);
		idDirectoryMap.put(ctx, id);
		return id;
	}
	
}
