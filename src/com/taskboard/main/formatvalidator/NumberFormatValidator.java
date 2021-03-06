//@@author A0126536E
package com.taskboard.main.formatvalidator;

/**
 * This class validates a String if it is a valid number (integer).
 * @author ASUS
 */
public class NumberFormatValidator implements FormatValidator {
	
	public NumberFormatValidator() {
		
	}
	
	public boolean isValidFormat(String token) {
		token = token.toLowerCase();
		if (isNumeric(token)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isNumeric(String token) {
		try {
			Integer.parseInt(token);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	public String toDefaultFormat(String token) {
		return token;
	}
	
}
