//@@author A0126536E
package com.taskboard.main.formatvalidator;

/**
 * This class validates a String if it is of a supported time format and 
 * converts it to the default time format hh:mm.
 * @author Alvian Prasetya
 */
public class TimeFormatValidator implements FormatValidator {

	private static final String AM = "am";
	private static final String PM = "pm";
	private static final String COLON = ":";
	private static final String DOT = ".";
	
	public TimeFormatValidator() {
		
	}
	
	public boolean isValidFormat(String token) {
		token = token.toLowerCase();
		if (token.indexOf(AM) == 1 && token.length() == 3) {
			return true;
		} else if (token.indexOf(AM) == 2 && token.length() == 4) {
			return true;
		} else if (token.indexOf(PM) == 1 && token.length() == 3) {
			return true;
		} else if (token.indexOf(PM) == 2 && token.length() == 4) {
			return true;
		} else if (token.indexOf(COLON) == 1 && token.length() == 4) {
			return true;
		} else if (token.indexOf(COLON) == 2 && token.length() == 5) {
			return true;
		} else if (token.indexOf(DOT) == 1 && token.length() == 4) {
			return true;
		} else if (token.indexOf(DOT) == 2 && token.length() == 5) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toDefaultFormat(String token) {
		String hh = "";
		String mm = "";
		
		token = token.toLowerCase();
		if (token.indexOf(AM) == 1 && token.length() == 3) {
			hh = "0" + token.substring(0, token.indexOf(AM));
			// 12am is 00:00
			if (hh.equals("12")) {
				hh = "00";
			}
			mm = "00";
		} else if (token.indexOf(AM) == 2 && token.length() == 4) {
			hh = token.substring(0, token.indexOf(AM));
			// 12am is 00:00
			if (hh.equals("12")) {
				hh = "00";
			}
			mm = "00";
		} else if (token.indexOf(PM) == 1 && token.length() == 3) {
			hh = Integer.toString(Integer.parseInt(token.substring(0, token.indexOf(PM))) + 12);
			// 12pm is 12:00
			if (hh.equals("24")) {
				hh = "12";
			}
			mm = "00";
		} else if (token.indexOf(PM) == 2 && token.length() == 4) {
			hh = Integer.toString(Integer.parseInt(token.substring(0, token.indexOf(PM))) + 12);
			// 12pm is 12:00
			if (hh.equals("24")) {
				hh = "12";
			}
			mm = "00";
		} else if (token.indexOf(COLON) == 1 && token.length() == 4) {
			hh = "0" + token.substring(0, token.indexOf(COLON));
			mm = token.substring(token.indexOf(COLON) + 1);
		} else if (token.indexOf(COLON) == 2 && token.length() == 5) {
			hh = token.substring(0, token.indexOf(COLON));
			mm = token.substring(token.indexOf(COLON) + 1);
		} else if (token.indexOf(DOT) == 1 && token.length() == 4) {
			hh = "0" + token.substring(0, token.indexOf(DOT));
			mm = token.substring(token.indexOf(DOT) + 1);
		} else if (token.indexOf(DOT) == 2 && token.length() == 5) {
			hh = token.substring(0, token.indexOf(DOT));
			mm = token.substring(token.indexOf(DOT) + 1);
		} else {
			assert false: "Found a foreign case for time validator.";
		}
		
		return hh + COLON + mm;
	}
	
}
