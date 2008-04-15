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
package org.freeclinic.server;

import java.util.HashMap;

import org.freeclinic.common.info.Person;
import org.freeclinic.common.type.EString;

public class MockDB {

	private static MockDB s_instance;
	
	private HashMap<Integer, Person> personDB;
	
	public static MockDB getInstance() {
		if(s_instance == null) {
			s_instance = new MockDB();
		}
		return s_instance;
	}
	
	private MockDB() {
		personDB = new HashMap<Integer, Person>();
		
		Person cameron = new Person();
		Person brian = new Person();
		cameron.setFirstName("Cameron");
		brian.setFirstName("Brian");
		
		personDB.put(1, cameron);
		personDB.put(2, brian);
	}
	
	public Person getPerson(int id) {
		return personDB.get(new Integer(id));
	}
	
}
