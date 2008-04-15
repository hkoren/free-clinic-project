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

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;



/**
 * 
 * @author Brian Visser, Henry Koren
 *
 */

//public class SSLServer { // EITHER extend Server, or create SSLServerThread.java
public class SSLServer extends Server {
	public static final String logfile = "/var/log/freeclinic.log";
	Database database;
	DateFormat df =  DateFormat.getDateInstance();
	Date time;
	
	public static String keyStream = ".//foobar";
	private static String trustStorePassword = "foobar"; // NOTE: get from user input
	private static String keyStorePassword = "foobar"; // NOTE: get from user input
	
	public SSLServer() {
		init();
	}
	
	public String getTimeTag() {
		time.setTime(System.currentTimeMillis());
		return df.format(time);
	}
	
	public void init() {
		database = new Database();
		time = new Date();
		
	}
	
	public void log(String error) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(logfile, true));
			out.write(error);
			out.close();
			} catch (IOException e) {
				System.err.println("Cannot write to log file '" + logfile + "'");
			} 
	}
	
	/**
	 * The Main server instance
	 * @param arstring parameters that the server takes
	 */
	public static void main(String [] arstring)
	{
		int port = 8066; // SSL Socket
		try
		{
//			SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
			SSLServerSocketFactory sslserversocketfactory = getCustomSSLServerSocketFactory();
			
			SSLServerSocket sslserversocket = (SSLServerSocket)sslserversocketfactory.createServerSocket(port);
			sslserversocket.setNeedClientAuth(false);
					 
			SSLSocket socket;
			Thread thread;
			Server server = new SSLServer();
			
			System.err.println("*****\nSSLServer Ready... "); 
			while (true)
			{
				try {
					System.err.print("\tWaiting for connection on port " + port + " ... ");
	
					socket = (SSLSocket)sslserversocket.accept();
	
					System.err.println("Connection made by " + socket.getInetAddress());

					thread = new Thread(new SSLServerThread(socket,server));
					thread.start();	
				}
				catch(java.net.SocketException e)
				{
					e.printStackTrace();
					break;
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}

		}
		catch (java.io.EOFException e)
		{
			System.err.println("Unexpected Client Disconnect");
		}
		catch (Exception exception)
		{
			System.err.println("Error: " +exception.getMessage());
			exception.printStackTrace();
			System.exit(0);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static SSLServerSocketFactory getCustomSSLServerSocketFactory()
	{
		// get KeyStore managers and TrustStore managers
		TrustManager[] trustManager = getTrustManagers(keyStream, keyStream, trustStorePassword);
		KeyManager[] keyManager = getKeyManagers(keyStream, keyStorePassword);

//		if (trustManager == null) // error
//			Log.e(TAG, "getTrustManager returned null");
//		if (keyManager == null) // error
//			Log.e(TAG, "getKeyManager returned null");
			
		try
		{
//			SSLContext context = SSLContext.getInstance("SSL"); // SSL type does not work?
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(keyManager, trustManager, null);
			
			SSLServerSocketFactory ssf = (SSLServerSocketFactory)context.getServerSocketFactory();
			return ssf;
		}
		catch (Exception e)
		{
//			Log.e(TAG, "Exception in getCustomFactory: " + e.getMessage());
		}

		return null;
	}
	private static TrustManager[] getTrustManagers(String trustStoreLocation, String keyStoreLocation, String trustStorePassword)
	{
		try
		{
			TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//			if (trustFactory == null)
//				Log.e(TAG, "trustFactory == null");

			FileInputStream trustStoreFile = new FileInputStream(trustStoreLocation); // load TrustStore
//			if (trustStoreFile == null)
//				Log.e(TAG, "trustStoreFile == null");
			
			
			KeyStore key = KeyStore.getInstance(KeyStore.getDefaultType());
			FileInputStream keyStoreFile = new FileInputStream(keyStoreLocation);
			key.load(keyStoreFile, keyStorePassword.toCharArray());
			keyStoreFile.close();		
			
			
			
			
			
//			KeyStore key = KeyStore.getInstance("JKS");
//			Log.e(TAG, "KeyStore type: " + KeyStore.getDefaultType());
//			if (key == null)
//				Log.e(TAG, "key == null");
			
			
			key.load(trustStoreFile, trustStorePassword.toCharArray());
			
			trustStoreFile.close(); // close FileIO

			trustFactory.init(key); // initialize TrustManagerFactory
			return trustFactory.getTrustManagers(); // get TrustStore and return it
		}
		catch (NoSuchAlgorithmException e)
		{
//			Log.e(TAG, "getTrustManager: NoSuchAlgorithmException " + e.getMessage());
			System.err.println(e.getMessage());
		}
		catch (IOException e)
		{
//			Log.e(TAG, "getTrustManager: IOException " + e.getMessage());
			System.err.println(e.getMessage());
		}
		catch (java.security.cert.CertificateException e)
		{
//			Log.e(TAG, "getTrustManager: Certificate " + e.getMessage());
			System.err.println(e.getMessage());
		}
		catch (java.security.KeyStoreException e)
		{
//			Log.e(TAG, "getTrustManager: KeyStore " + e.getMessage());
			System.err.println(e.getMessage());
		}


		return null; // error
	}
	
	
	private static KeyManager[] getKeyManagers(String keyStoreLocation, String keyStorePassword)
	{
		try
		{
			KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//			if (keyFactory == null)
//				Log.e(TAG, "keyFactory == null");
			
			FileInputStream keyStoreFile = new FileInputStream(keyStoreLocation);
//			if (keyStoreFile == null)
//				Log.e(TAG, "keyStoreFile == null");
			
			KeyStore key = KeyStore.getInstance(KeyStore.getDefaultType());
//			KeyStore key = KeyStore.getInstance("jks");
//			if (key == null)
//				Log.e(TAG, "key == null");
			
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
