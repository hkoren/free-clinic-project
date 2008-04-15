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
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.freeclinic.common.info.Person;
import org.freeclinic.common.network.request.RequestType;
import org.freeclinic.common.type.ClinicRequest;

import android.os.Parcel;

/**
 * @author Brian Visser 
 *
 * VM Arguments are no longer needed. see link before for more information
 * http://www.ibm.com/developerworks/java/library/j-customssl/
 * 
 * Class to test EchoServerMaster.java.
 * Once run, user inputs Person Id integers to fetch from MockDB.java
 * Valid test inputs currently are "1" and "2".
 * 
 * Compile Instructions: javac *.java
 * Run Instruction: java CustomClient
 */
public class CustomClient
{
	private static final int RETRY_MAX = 3;
	private static int retryCount = 0;

	private static int port = 8064;
	private static String host = "localhost";

	private static String trustStoreLocation = "C:\\foobar";
	private static String trustStorePassword = "foobar"; // NOTE: get from user input
	private static String keyStoreLocation = "C:\\foobar";
	private static String keyStorePassword = "foobar"; // NOTE: get from user input

	public static void main(String [] arstring)
	{
		CustomClient client = new CustomClient();
		client.connect();
	}
	
	public CustomClient()
	{
		//nothing;
	}

	/*
	 * Function for user the change the host and port
	 * 
	 * @param	newHost	Host specified by the user
	 * @param	newPort	Port specified by the user
	 */
	public void connect(String newHost, int newPort)
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
	private void connect()
	{
		try
		{
			SSLSocketFactory sslsocketfactory = getCustomSSLSocketFactory();

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
			Parcel parcel;
			ClinicRequest request;
			Integer requestId;
			while ((string = bufferedReader.readLine()) != null)
			{
				parcel = Parcel.obtain();
								
				requestId = new Integer(string);
				//request = new ClinicRequest(RequestType.PERSON, requestId);
				// TODO: Whats going on here?
				request = new ClinicRequest(requestId);


				outputStream.writeObject(request);
				//outputStream.writeObject(request);

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

	protected static SSLSocketFactory getCustomSSLSocketFactory()
	{
		// get KeyStore managers and TrustStore managers
		TrustManager[] trustManager = getTrustManagers(trustStoreLocation, trustStorePassword);
		KeyManager[] keyManager = getKeyManagers(keyStoreLocation, keyStorePassword);

		if (trustManager == null) // error
			return null;
		if (keyManager == null) // error
			return null;
			
		try
		{
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(keyManager, trustManager, null);
			
			SSLSocketFactory ssf = context.getSocketFactory();
			return ssf;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	protected static TrustManager[] getTrustManagers(String trustStoreLocation, String trustStorePassword)
	{
		try
		{
			TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

			FileInputStream trustStoreFile = new FileInputStream(trustStoreLocation); // load TrustStore
			KeyStore key = KeyStore.getInstance("jks");
			key.load(trustStoreFile, trustStorePassword.toCharArray());
			
			trustStoreFile.close(); // close FileIO

			trustFactory.init(key); // initialize TrustManagerFactory

			return trustFactory.getTrustManagers(); // get TrustStore and return it
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null; // error
	}
	protected static KeyManager[] getKeyManagers(String keyStoreLocation, String keyStorePassword)
	{
		try
		{
			KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

			FileInputStream keyStoreFile = new FileInputStream(keyStoreLocation);
			KeyStore key = KeyStore.getInstance("jks");
			key.load(keyStoreFile, keyStorePassword.toCharArray());
			
			keyStoreFile.close();

			keyFactory.init(key, keyStorePassword.toCharArray());

			return keyFactory.getKeyManagers();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
