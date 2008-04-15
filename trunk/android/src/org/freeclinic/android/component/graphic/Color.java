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
package org.freeclinic.android.component.graphic;

public class Color {
	public int r;
	public int g;
	public int b;
	
	public Color(){
		this.r=0;
		this.g=0;
		this.b=0;
	}
	
	public Color(int r,int g, int b){
		this.r=r;
		this.g=g;
		this.b=b;
	}
	
	public void set(int r,int g, int b){
		this.r=r;
		this.g=g;
		this.b=b;
		return;
	}
	
	public int getRed(){
		return this.r;
	}
	
	public int getGreen(){
		return this.g;
	}

	public int getBlue(){
		return this.b;
	}
}
