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

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/** 
 * @author Brian Visser, Cameron Esfahani
 * 
 * Compile Instructions: javac *.java
 * Run Instructions: java -Djavax.net.ssl.keyStore=LOCATION
 * 						  -Djavax.net.ssl.keyStorePassword=PASSWORD EchoServerMaster
 * 					Where LOCATION is the location of the keyStore certificates and
 * 					PASSWORD is the password associated with the keyStore certificate.
 * 					The keyStore provided (foobar) in this package has password "foobar",
 * 					with no quotes.
 * 
 * Main java files for the SSL server. Server has the ability to obtain multiple connections.
 * Thread behavior defined in EchoServerThread.java
 */
public class EchoServerMaster
{
	public static void main(String [] arstring)
	{
		int port = 8064;
		Thread thread;
		
		try
		{
			SSLServerSocketFactory sslserversocketfactory =
				(SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
			SSLServerSocket sslserversocket =
				(SSLServerSocket)sslserversocketfactory.createServerSocket(port);

			SSLSocket sslsocket;
			

			System.err.println("*****\nServer Ready... ");
			while (true)
			{
				System.err.print("\tWaiting for Connection... ");

				sslsocket = (SSLSocket)sslserversocket.accept();

				System.err.println("Connection made by " + sslsocket.getInetAddress());

				thread = new Thread(new EchoServerThread(sslsocket));
				thread.start();
			}

		}
		catch (javax.net.ssl.SSLHandshakeException e)
		{
			System.err.println("Invalid Connection!");
		}
		catch (java.io.EOFException e)
		{
			System.err.println("Unexpected Client Disconnect");
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
}

