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

import android.content.Context;

/**
 * @author Cameron Esfahani
 *
 * This class allows for unique tag and Id generation. This is
 * done by creating an Ident object whenever an element needs
 * to be identified either by tag or Id. Keep these Idents as
 * private member fields, and initialize them on View construction.
 */
public class Ident {

	private String tag;
	private int id;
	
	/**
	 * Construct a new ident.
	 * 
	 * @param ctx Context
	 */
	public Ident(Context ctx) {
		tag = IdDirectoryFactory.getInstance().getIdDirectory(ctx).createId().toString();
		id = new Integer(tag);
	}

	/**
	 * Get the Ident's tag
	 * 
	 * @return Tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Get the Ident's Id
	 * 
	 * @return Id
	 */
	public int getId() {
		return id;
	}
	
}
