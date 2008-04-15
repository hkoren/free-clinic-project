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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.freeclinic.common.info.Person;
import org.freeclinic.common.type.ClinicRequest;

/** 
 * @author Brian Visser, Cameron Esfahani
 * 
 * Class to test EchoServerMaster.java.
 * Once run, user inputs Person Id integers to fetch from MockDB.java
 * Valid test inputs currently are "1" and "2".
 * 
 * 
 * 
 * ********************************************
 * IMPORTANT NOTE: this version is now out of date. USE
 * 					CustomClient.java
 * CustomClient no longer needs the Java VM arguments (the -Djavax....)
 * to run. all you do is "java CustomClient" after compile
 * ********************************************
 * 
 */
public class EchoClient
{
	private static final int RETRY_MAX = 3;
	private static int retryCount = 0;
	
	private static int port = 8064;
	private static String host = "localhost";
	
	
	public static void main(String [] arstring)
	{
		connect();
	}
	
	/*
	 * Function for user the change the host and port
	 * 
	 * @param	newHost	Host specified by the user
	 * @param	newPort	Port specified by the user
	 */
	public static void connect(String newHost, int newPort)
	{
		port = newPort;
		host = newHost;
		connect();
	}
	
	/*
	 * Function to connect via SSL Socket to the specified server.
	 * Creates ObjectOutputStream to stream request objects over the
	 * network.
	 * 
	 * Note: ObjectOutputStream must be created before ObjectInputStream
	 * because the ObjectInputStream constructor blocks while reading
	 * a header from the stream source.
	 */
	private static void connect()
	{
		try
		{
			SSLSocketFactory sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
			SSLSocket sslsocket = (SSLSocket)sslsocketfactory.createSocket(host, port);

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

			/*
			 * ObjectOutputStream must be created before ObjectInputStream
			 * because the ObjectInputStream constructor blocks while reading
			 * a header from the stream source.
			 */
			ObjectOutputStream outputStream = new ObjectOutputStream(sslsocket.getOutputStream());
			ObjectInputStream inputStream = new ObjectInputStream(sslsocket.getInputStream());


			String string = null;

			while ((string = bufferedReader.readLine()) != null)
			{
				Integer requestId = new Integer(string);

				//outputStream.writeObject(new ClinicRequest(RequestType.PERSON, requestId));
				// TODO: Whats going on here?
				outputStream.writeObject(new ClinicRequest(requestId));

				Person response = (Person) inputStream.readObject();

				System.err.println("You just requested " + response.getFirstName());
			}
		}
		catch (java.net.ConnectException e)
		{
			if (++retryCount < RETRY_MAX)
			{
				System.err.println("Error: Connection Failed. Attemting retry.");
				connect();				
			}
			else
			{
				System.err.println("Error: Connection Failed. Exit.");
				System.exit(0);
			}

		}
		catch (java.io.EOFException e)
		{
			System.err.println("Error: EOF Exception");
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
}
