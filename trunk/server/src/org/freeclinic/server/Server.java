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
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
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
 * A Java server implementing a SSLServerSocketFactory to establish
 * connections between clients.
 * <br>
 * Each new client connection creates a new thread of type ServerThread,
 * which implements all the functionality of the server.
 */
public class Server {
	public static final String logfile = "/var/log/freeclinic.log";
	
	private DateFormat df = DateFormat.getDateInstance();
	private Date time;
	
	// for use with custom keyStore and trustStore
	public static String keyStream = ".//foobar";
	private static String trustStorePassword = "foobar"; // NOTE: get from user input
	private static String keyStorePassword = "foobar"; // NOTE: get from user input
	
	private int port = 8064;
	
	private SSLServerSocketFactory sslserversocketfactory;
	private SSLServerSocket sslserversocket;
	private SSLSocket socket;
	
	public Server() {
		init();
	}
	
	public String getTimeTag() {
		time.setTime(System.currentTimeMillis());
		return df.format(time);
	}
	
	public void init() {
		try
		{
			time = new Date();
			
			sslserversocketfactory = getCustomSSLServerSocketFactory();
			sslserversocket = (SSLServerSocket)sslserversocketfactory.createServerSocket(port);
			sslserversocket.setNeedClientAuth(false);
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
			log("Error: " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			log("Error: " + e.getMessage());
		}
	}
	
	/**
	 * Prints to the server log file
	 * 
	 * @param error A string to write to the log file
	 */
	public synchronized void log(String error) {
		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(logfile, true));
			out.write(error);
			out.close();
		}
		catch (IOException e)
		{
			System.err.println("Cannot write to log file '" + logfile + "'");
		}
	}
	
	/**
	 * The Main server instance
	 * @param arstring parameters that the server takes
	 */
	public static void main(String [] arstring)
	{
		Thread thread;

		Server server = new Server();
		System.err.println("*****\nFCP Server Ready...\n*****");
		while (true)
		{
			try {
				server.socket = (SSLSocket)server.sslserversocket.accept();
				System.err.println("Connection made by " + server.socket.getInetAddress());

				thread = new Thread(new ServerThread(server.socket, server));
				thread.start();	
			}
			catch(SocketException e)
			{
				System.err.println(e.getMessage());
				server.log("Error: " + e.getMessage());
			
				break;
			}
			catch(IOException e)
			{
				System.err.println(e.getMessage());
				server.log("Error: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Function to create a new SSLServerSocketFactory which utilizes a custom KeyStore 
	 * and TrustStore. Calls two helper functions getTrustManagers(...) and getKeyManagers(...).
	 * 
	 * @return Returns a new SSLServerSocketFactory
	 */
	private SSLServerSocketFactory getCustomSSLServerSocketFactory()
	{
		try
		{
			TrustManager[] trustManager = getTrustManagers(keyStream, keyStream, trustStorePassword);
			KeyManager[] keyManager = getKeyManagers(keyStream, keyStorePassword);
			
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(keyManager, trustManager, null);
			
			SSLServerSocketFactory ssf = (SSLServerSocketFactory)context.getServerSocketFactory();
			return ssf;
		}
		catch (NoSuchAlgorithmException e)
		{
			System.err.println(e.getMessage());
			log("Error: " + e.getMessage());
		}
		catch (KeyManagementException e)
		{
			System.err.println(e.getMessage());
			log("Error: " + e.getMessage());
		}

		return null;
	}
	private TrustManager[] getTrustManagers(String trustStoreLocation, String keyStoreLocation, String trustStorePassword)
	{
		try
		{
			TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

			FileInputStream trustStoreFile = new FileInputStream(trustStoreLocation); // load TrustStore
			
			KeyStore key = KeyStore.getInstance(KeyStore.getDefaultType());
			FileInputStream keyStoreFile = new FileInputStream(keyStoreLocation);
			key.load(keyStoreFile, keyStorePassword.toCharArray());
			keyStoreFile.close();		
			
			key.load(trustStoreFile, trustStorePassword.toCharArray());
			
			trustStoreFile.close(); // close FileIO

			trustFactory.init(key); // initialize TrustManagerFactory
			return trustFactory.getTrustManagers(); // get TrustStore and return it
		}
		catch (NoSuchAlgorithmException e)
		{
			System.err.println(e.getMessage());
			log("Error: " + e.getMessage());		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
			log("Error: " + e.getMessage());
		}
		catch (CertificateException e)
		{
			System.err.println(e.getMessage());
			log("Error: " + e.getMessage());
		}
		catch (KeyStoreException e)
		{
			System.err.println(e.getMessage());
			log("Error: " + e.getMessage());
		}
		return null; // error 
	}
	
	
	private KeyManager[] getKeyManagers(String keyStoreLocation, String keyStorePassword)
	{
		try
		{
			KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			
			FileInputStream keyStoreFile = new FileInputStream(keyStoreLocation);
			
			KeyStore key = KeyStore.getInstance(KeyStore.getDefaultType());
			
			key.load(keyStoreFile, keyStorePassword.toCharArray());
			
			keyStoreFile.close();

			keyFactory.init(key, keyStorePassword.toCharArray());

			return keyFactory.getKeyManagers();
		}
		catch (NoSuchAlgorithmException e)
		{
			System.err.println(e.getMessage());
			log("Error: " + e.getMessage());
		}
		catch (KeyStoreException e)
		{
			System.err.println(e.getMessage());
			log("Error: " + e.getMessage());
		}
		catch (UnrecoverableKeyException e)
		{
			System.err.println(e.getMessage());
			log("Error: " + e.getMessage());
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
			log("Error: " + e.getMessage());
		}
		catch (CertificateException e)
		{
			System.err.println(e.getMessage());
			log("Error: " + e.getMessage());
		}
		return null;
	}
}
