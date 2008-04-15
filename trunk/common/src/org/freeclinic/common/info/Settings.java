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
package org.freeclinic.common.info;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Settings {
	private int port;
	private String server;
	
	public Settings(int port, String server)
	{
		this.port = port;
		this.server = server;
	}
	
	public static Settings readIni(FileInputStream fis)
	{
		BufferedInputStream instr = new BufferedInputStream(fis);
		BufferedReader in = new BufferedReader(new InputStreamReader(instr));
		try {
			int port = Integer.parseInt(in.readLine());
			String server = in.readLine();
			return new Settings(port,server);
		}
		catch(IOException e)
		{
			return null;
		}
	}
	
	public boolean writeIni(FileOutputStream fos)
	{
		BufferedOutputStream outstr = new BufferedOutputStream(fos);
		PrintWriter out = new PrintWriter(outstr);
		out.write(port + "\n");
		out.write(server + "\n");
		out.close();
		return true;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
}