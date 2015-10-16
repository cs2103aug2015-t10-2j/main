package com.taskboard.main;

public class TimeFormatValidator implements FormatValidator {

	public TimeFormatValidator() {
		
	}
	
	public boolean isValidFormat(String token) {
		token = token.toLowerCase();
		if (token.indexOf("am") != -1 || token.indexOf("pm") != -1 || token.indexOf(':') != -1) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toDefaultFormat(String token) {
		String hh = new String();
		String mm = new String();
		if (token.indexOf("am") != -1) {
			hh = token.substring(0, token.indexOf("am"));
			// 12am is 00:00
			if (hh.equals("12")) {
				hh = "00";
			}
			mm = "00";
		} else if (token.indexOf("pm") != -1) {
			hh = Integer.toString(Integer.parseInt(token.substring(0, token.indexOf("pm"))) + 12);
			// 12pm is 12:00
			if (hh.equals("24")) {
				hh = "12";
			}
			mm = "00";
		} else if (token.indexOf(":") == 2 && token.length() == 5) {
			return token;
		}
		return hh + ":" + mm;
	}
	
}
