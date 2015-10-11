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
			mm = "00";
		} else if (token.indexOf("pm") != -1) {
			hh = Integer.toString(Integer.parseInt(token.substring(0, token.indexOf("pm"))) + 12);
			mm = "00";
		} else if (token.indexOf(":") == 2 && token.length() == 5) {
			return token;
		}
		return hh + ":" + mm;
	}
	
}
