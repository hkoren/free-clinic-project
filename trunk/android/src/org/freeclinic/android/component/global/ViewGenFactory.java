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
 * Factory 
 */
public class ViewGenFactory {
	
	private static ViewGenFactory s_instance;
	private HashMap<Context, ViewGen> viewGenMap;
	
	/**
	 * Constructor
	 */
	private ViewGenFactory() {
		viewGenMap = new HashMap<Context, ViewGen>();
	}
	
	/**
	 * Singleton
	 * 
	 * @return			Instance of ViewGenFactory
	 */
	public static ViewGenFactory getInstance() {
		if(s_instance == null) {
			s_instance = new ViewGenFactory();
		}
		return s_instance;
	}
	
	/**
	 * Only one instance of a ViewGen for each Context
	 * 
	 * @param ctx		Context to create generated views with
	 * @return			Instance of ViewGen utility.
	 */
	public ViewGen getViewGen(Context ctx) {
		if(viewGenMap.containsKey(ctx)) {
			return viewGenMap.get(ctx);
		}
		ViewGen vg = new ViewGen(ctx);
		viewGenMap.put(ctx, vg);
		return vg;
	}
	
}
