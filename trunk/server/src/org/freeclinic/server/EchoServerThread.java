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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.net.ssl.SSLSocket;

import org.freeclinic.common.network.request.LoginRequest;
import org.freeclinic.common.network.request.RequestType;
import org.freeclinic.common.type.ClinicRequest;
import org.freeclinic.common.type.ClinicResponse;

/** 
 * @author Brian Visser, Cameron Esfahani
 * 
 * For use with EchoServerMaster.java
 * 
 * Class for new connection behavior for the SSL server.
 * Each connection creates a new thread with this defined behavior.
 */
public class EchoServerThread implements Runnable
{
	private SSLSocket sslsocket;
	private Database database;

	public EchoServerThread()
	{
		sslsocket = null;
		database = null;
	}

	public EchoServerThread(SSLSocket socket)
	{
		sslsocket = socket;
		database = null;
	}

	@Override
	public void run()
	{
		ObjectOutputStream outputStream;
		ObjectInputStream inputStream;

		try
		{
			/*
			 * ObjectOutputStream must be created before ObjectInputStream
			 * because the ObjectInputStream constructor blocks while reading
			 * a header from the stream source.
			 */
			outputStream = new ObjectOutputStream(sslsocket.getOutputStream());
			inputStream = new ObjectInputStream(sslsocket.getInputStream());


			ClinicRequest request; // cast from incoming message
			int requestType; // incoming message type
			while ((request = (ClinicRequest)inputStream.readObject()) != null) // wait for incoming message
			{
				requestType = request.getRequestCode();

				if (requestType == RequestType.LOGIN)
				{
					LoginRequest lr = (LoginRequest) request;
					database = new Database();
					Integer result;

					if (database.login(lr))
					{
						result = 1;
					}
					else
					{
						result = 0;
					}
					
					// TODO: This was here @ revision 1000
					//outputStream.writeObject(new ClinicResponse(result));
					//outputStream.flush();
				}
				else if(request.getRequestCode() == RequestType.PERSON) {
					outputStream.writeObject(MockDB.getInstance().getPerson(request.getRequestCode()));
					outputStream.flush();
				}
			}

			//inputStream.close();
			//outputStream.flush();
			//outputStream.close();
		}
		catch (java.io.EOFException e)
		{
			// just end thread
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
}