package org.freeclinic.server;
/**
 * Indicate that a malformed request has been recieved by the server 
 * @author Henry
 *
 */
public class MalformedRequestException extends RuntimeException {
	public MalformedRequestException(int type,String loc,Exception e) {
		super("Request ID " + type + " Malformed at "+loc+" Exception:" +e);
	}
	public MalformedRequestException(String loc,Exception e) {
		super("Request Malformed at "+loc+" Exception:" +e);
	}
}
