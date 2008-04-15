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
/**
 * @author Henry Koren
 * Database connection class
 */
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;

import org.freeclinic.common.info.Measurement;
import org.freeclinic.common.info.Message;
import org.freeclinic.common.info.NotAuthorizedException;
import org.freeclinic.common.info.Patient;
import org.freeclinic.common.info.PatientMessage;
import org.freeclinic.common.info.Vitals;
import org.freeclinic.common.network.request.AddPatientMessageRequest;
import org.freeclinic.common.network.request.LoginRequest;
import org.freeclinic.common.network.request.MessageSearchRequest;
import org.freeclinic.common.network.request.PatientMeasurementsRequest;
import org.freeclinic.common.network.request.PatientMessagesRequest;
import org.freeclinic.common.network.request.PutVitalsRequest;
import org.freeclinic.common.network.request.RegistrationRequest;
import org.freeclinic.common.network.response.MessageListResponse;
import org.json.JSONException;

import com.twmacinta.util.MD5;

public class Database {
	private Connection conn;

	public Database()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("error: "+e.getMessage());
		}
		connect();
	}

	/**
	 * Connect to the database
	 */
	private void connect() {
		try {
			conn = 
				DriverManager.getConnection("jdbc:mysql://freeclinicproject.org/clinic?" + 
				"user=root&password=srfcdbpw");

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	/**
	 * Execute a query returning a result set
	 * @param sql
	 * @return
	 */
	public ResultSet exec(String sql) {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			// or alternatively, if you don't know ahead of time that
			// the query will be a SELECT...

			if (stmt.execute(sql)) {
				rs = stmt.getResultSet();
			}
		} 
		catch (SQLException e)
		{
			System.err.println("Error opening patient - " + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e)
		{
			System.err.println("error: " + e.getMessage());
		}

		return rs;
	
	}
	
	/**
	 * Get the list of patients that are checked in
	 * @return
	 */
	public Patient[] getCheckedInPatients() {
		Vector<Patient> v = new Vector<Patient>();
		ResultSet pr = null;
		String sql = "SELECT c._id as checkInID, c.complaint, c.complaintMessageID, p.* FROM checkIns c join patients p on p._id = c.patientID WHERE date > NOW() - INTERVAL 1 DAY ORDER BY date ASC";
		System.err.println(sql);
		try {
			pr = exec(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				while (pr.next())
				{
					System.out.println("adding  Patient");
					Patient p = new Patient(pr);
					v.add(p);
					System.out.println("Patient: " + p);
				}
				pr.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		Patient[] output = new Patient[v.size()];
		v.toArray(output);
		return output;
	}
	
	/**
	 * Retrieve a patient record
	 * @param patientID
	 * @return
	 * @throws NotAuthorizedException
	 */
	public Patient getPatient(int patientID) throws NotAuthorizedException
	{
		ResultSet pr = null;
		Patient p = null;
		String sql = "select max(c._id) as checkinId, p._id, p.SSN, p.dateOfBirth, p.gender, p.firstName, p.middleName, p.lastName, p.address1,p.address2, p.city, p.state, p.zip, p.phoneNumber, p.emergencyName, p.emergencyPhoneNumber,p.emergencyRelationship, p.createDate, p.modifiedDate, p.createdBy, c.complaint,c.complaintMessageID from patients p left outer join checkIns c on c.patientID = p._id where p._id=" + Integer.toString(patientID);
		System.err.println(sql);
		try {
			pr = exec(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (pr.next())
				{
					p = new Patient(pr);
						pr.close();					
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return p;
		
	}
	/**
	 * Return the latest vitals for the patient
	 * @param patientID
	 * @return
	 */
	public Vitals getVitals(int patientID)
	{
		ResultSet vr = null;
		Vitals v = null;
		String sql = "call getVitals(" + Integer.toString(patientID) + ");";
		System.err.println(sql);
		try {
			vr = exec(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (vr.next())
				{
					v = new Vitals(vr);
					vr.close();					
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return v;
	}
	/**
	 * get a list of messages from the database based on a search
	 * @param msr Message Search Request
	 * @return
	 */
	public Message[] getMessages(MessageSearchRequest msr) {
		Vector<Message> v = new Vector<Message>();
		ResultSet mr = null;
		String sql = "SELECT * from messages where name like '%" + msr.getQuery() + "%' and messageTypeID = " + msr.getMessageTypeID();
		System.err.println(sql);
		try {
			mr = exec(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				while (mr.next())
				{
					Message m = new Message(mr);
					v.add(m);
					System.out.println("Message: " + m );
				}
				mr.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		Message[] output = new Message[v.size()];
		v.toArray(output);
		return output;
	}
	public PatientMessage[] getPatientMessages(PatientMessagesRequest pmr) {
		Vector<Message> v = new Vector<Message>();
		ResultSet res = null;
		String sql = "SELECT m._id, m.messageTypeID, m.name, m.categoryID, m.icd9, m.shortName, m.modified, pm.patientID, pm.created, pm.due, " +
				"pm.userID, pm.messageStatusID from patientMessages pm inner join messages m on m._id=pm.messageID " +
				"where pm.patientID = " + pmr.getPatientID() + pmr.getTypeWhere() + " order by due desc ";
		System.err.println(sql);
		try {
			res = exec(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				while (res.next())
				{
					PatientMessage m = new PatientMessage(res);
					v.add(m);
					System.out.println("Patient Message: " + m );
				}
				res.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		PatientMessage[] output = new PatientMessage[v.size()];
		v.toArray(output);
		return output;
	}
	
	public Message[] getMessages(Date lastUpdate) {
		Vector<Message> v = new Vector<Message>();
		ResultSet mr = null;
		String sql = "SELECT * from messages";
		System.err.println(sql);
		try {
			mr = exec(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				while (mr.next())
				{
					Message m = new Message(mr);
					v.add(m);
					System.out.println("Message: " + m );
				}
				mr.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		Message[] output = new Message[v.size()];
		v.toArray(output);
		return output;
		
	}
	public Measurement[] getPatientMeasurements(PatientMeasurementsRequest pmr) {
		Vector<Measurement> v = new Vector<Measurement>();
		ResultSet mr = null;
		int patientID = pmr.getPatientID();
		String sql = "SELECT * from Measurements where patientID = " + patientID + " order by timestamp";
		System.err.println(sql);
		try {
			mr = exec(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				while (mr.next())
				{
					Measurement m = new Measurement(mr);
					v.add(m);
					System.out.println("Measurement: " + m );
				}
				mr.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		Measurement[] output = new Measurement[v.size()];
		v.toArray(output);
		return output;
		
	}
	/**
	 * Primary test interface
	 * @param args
	 */
	public static void main(String args[]){
		Database d = new org.freeclinic.server.Database();
		//patientTest(d);
		//messageTest(d);
		patientMessageTest(d);
	}
	
	
	/**
	 * Test patient getting
	 * @param d
	 */
//	private static void patientTest(Database d) {
//		Patient[] patients =  d.getCheckedInPatients();
//
//		for (Patient p : patients) {
//			System.out.println("Patient: " + p);
//		}
//		PatientListResponse plr = new PatientListResponse(patients);
//		try {
//			System.out.println(plr.JSON().toString());
//		}
//		catch (JSONException e){
//			System.err.println("Json exception: " + e);
//		}
//	}
	
	public boolean deviceRegistration(RegistrationRequest rr){
		ResultSet mr = null;
		String checkSql = "select * from android_devices where IMEI = '" + rr.getImei() + "' or phoneNumber = '" + rr.getPhoneNum() + "' and isKilled =1";
		try {
			mr = exec(checkSql);
		}
		
		finally {
			try {
				if (!mr.last())
				{
					// There are killed devices with this IMEI or phone number 
					return false;
				}
				
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		
		// Insert this registration
		String sql = "insert into android_devices(IMEI,isActivated,isKilled,phoneNumber) values('"+ rr.getImei() + "',0,0, '" + rr.getPhoneNum() + "')";
		try {
			mr = exec(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public String insertMeasurementSQL(int patientID, Date timestamp, int type, float value)
	{
		return "insert into measurements(patientID,timestamp,measurementTypeID,value) values(" + patientID + ", '" + timestamp + "', " + type + "," + value + ")";
	}
	public boolean putVitals(PutVitalsRequest pvr){
		Vitals v = pvr.getArray()[0];
		int patientID = pvr.getPatientID();
		java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
		// Temp
		try {
			conn.createStatement().execute(insertMeasurementSQL(patientID,now,Measurement.Type.TEMPERATURE,v.getTemp().floatValue()));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// Heart Rate
		try {
			conn.createStatement().execute(insertMeasurementSQL(patientID,now,Measurement.Type.HEART_RATE,v.getHr().floatValue()));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// Systolic BP
		try {
			conn.createStatement().execute(insertMeasurementSQL(patientID,now,Measurement.Type.SYSTOLIC_BLOOD_PRESSURE,v.getSyst().floatValue()));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// Diastolic BP
		try {
			conn.createStatement().execute(insertMeasurementSQL(patientID,now,Measurement.Type.DIASTOLIC_BLOOD_PRESSURE,v.getDias().floatValue()));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// Respiration
		try {
			conn.createStatement().execute(insertMeasurementSQL(patientID,now,Measurement.Type.RESPIRATION,v.getResp().floatValue()));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	
	/**
	 * add a patient message
	 * @param req
	 * @return
	 */
	public boolean putPatientMessage(AddPatientMessageRequest req){
		PatientMessage pm = req.getArray()[0];
		int patientID = req.getPatientID();
		java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
		// Temp
		try {
			String sql = "insert into patientMessages(patientID,messageID,created,due,messageStatusID)values("+patientID+","+pm.getId()+",'"+(new java.sql.Date(pm.getCreateDate().getTime()))+"','"+(new java.sql.Date(pm.getDueDate().getTime()))+"'," + pm.getMessageStatusId() + ");";
			System.err.println("sql query: " + sql);
			conn.createStatement().execute(sql);
		}
		catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		System.err.println("returning from putPatientMessage");
		return true;
	}
		
		
	/**
	 * Test message getting
	 * @param d
	 */
	private static void messageTest(Database d) {
		Message[] messages =  d.getMessages(new Date(System.currentTimeMillis()));

		for (Message m : messages) {
			System.out.println("Message: " + m);
		}
		MessageListResponse mur = new MessageListResponse(messages);
		try {
			System.out.println(mur.JSON().toString());
		}
		catch (JSONException e){
			System.err.println("Json exception: " + e);
		}
	}
	
	private static void patientMessageTest(Database d) {
		PatientMessage[] messages =  d.getPatientMessages(new PatientMessagesRequest(31));

		for (Message m : messages) {
			System.out.println("Message: " + m);
		}
		MessageListResponse mur = new MessageListResponse(messages);
		try {
			System.out.println(mur.JSON().toString());
		}
		catch (JSONException e){
			System.err.println("Json exception: " + e);
		}
	}

	//public ResultSet

	public boolean login(LoginRequest lr) {
		String sql = "select pass from users where email='" + lr.getUserName() +"'";
		String passHash = null;
		/*MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(lr.getPassword().getBytes());
		passHash = Integer.toHexString(digest.hashCode());*/
		
		MD5 md5 = new MD5();
		try {
			md5.Update(lr.getPassword().toString(), null);
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    passHash = md5.asHex();
		    
			
		try {
			ResultSet r = exec(sql);
			if (r.next())
			{
				String dbpass = r.getString("pass");
				System.err.println("Comparing " + dbpass + " to " + passHash);
				if (r.getString("pass").compareTo(passHash) == 0) {
					System.err.println("Password Accepted!");
					return true;
				}
				else {
					System.err.println("Passwords do not match");
				}
			}
			else
			{
				System.err.println("No user " + lr.getUserName());

			}
		}
		catch (SQLException e)
		{
			System.err.println("your password: \"" + lr.getPassword() + "\" "  + e.getMessage() + "\nsql: " + sql);
			e.printStackTrace();
		}
		return false;

	}
	
}
