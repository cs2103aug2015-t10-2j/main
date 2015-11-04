package com.taskboard.main.formatvalidator;

public class TimeFormatValidator implements FormatValidator {

	public TimeFormatValidator() {
		
	}
	
	public boolean isValidFormat(String token) {
		token = token.toLowerCase();
		if (token.indexOf("am") == 1 && token.length() == 3) {
			return true;
		} else if (token.indexOf("am") == 2 && token.length() == 4) {
			return true;
		} else if (token.indexOf("pm") == 1 && token.length() == 3) {
			return true;
		} else if (token.indexOf("pm") == 2 && token.length() == 4) {
			return true;
		} else if (token.indexOf(":") == 1 && token.length() == 4) {
			return true;
		} else if (token.indexOf(":") == 2 && token.length() == 5) {
			return true;
		} else if (token.indexOf(".") == 1 && token.length() == 4) {
			return true;
		} else if (token.indexOf(".") == 2 && token.length() == 5) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toDefaultFormat(String token) {
		String hh = "";
		String mm = "";
		
		token = token.toLowerCase();
		if (token.indexOf("am") == 1 && token.length() == 3) {
			hh = "0" + token.substring(0, token.indexOf("am"));
			// 12am is 00:00
			if (hh.equals("12")) {
				hh = "00";
			}
			mm = "00";
		} else if (token.indexOf("am") == 2 && token.length() == 4) {
			hh = token.substring(0, token.indexOf("am"));
			// 12am is 00:00
			if (hh.equals("12")) {
				hh = "00";
			}
			mm = "00";
		} else if (token.indexOf("pm") == 1 && token.length() == 3) {
			hh = Integer.toString(Integer.parseInt(token.substring(0, token.indexOf("pm"))) + 12);
			// 12pm is 12:00
			if (hh.equals("24")) {
				hh = "12";
			}
			mm = "00";
		} else if (token.indexOf("pm") == 2 && token.length() == 4) {
			hh = Integer.toString(Integer.parseInt(token.substring(0, token.indexOf("pm"))) + 12);
			// 12pm is 12:00
			if (hh.equals("24")) {
				hh = "12";
			}
			mm = "00";
		} else if (token.indexOf(":") == 1 && token.length() == 4) {
			hh = "0" + token.substring(0, token.indexOf(":"));
			mm = token.substring(token.indexOf(":") + 1);
		} else if (token.indexOf(":") == 2 && token.length() == 5) {
			hh = token.substring(0, token.indexOf(":"));
			mm = token.substring(token.indexOf(":") + 1);
		} else if (token.indexOf(".") == 1 && token.length() == 4) {
			hh = "0" + token.substring(0, token.indexOf("."));
			mm = token.substring(token.indexOf(".") + 1);
		} else if (token.indexOf(".") == 2 && token.length() == 5) {
			hh = token.substring(0, token.indexOf("."));
			mm = token.substring(token.indexOf(".") + 1);
		} else {
			assert false: "Found a foreign case for time validator.";
		}
		
		return hh + ":" + mm;
	}
	
}
