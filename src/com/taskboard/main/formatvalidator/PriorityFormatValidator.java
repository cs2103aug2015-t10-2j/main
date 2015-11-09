//@@author A0126536E
package com.taskboard.main.formatvalidator;

/**
 * This class validates a String if it is of a supported priority format and 
 * converts it to the default priority format.
 * @author Alvian Prasetya
 */
public class PriorityFormatValidator implements FormatValidator {

	// constructors
	
	public PriorityFormatValidator() {
		
	}
	
	// functionalies
	
	public boolean isValidFormat(String token) {
		token = token.toLowerCase();
		switch (token) {
			case "l" :
				// Fallthrough.
			case "lo" :
				// Fallthrough.
			case "low" :
				// Fallthrough.
			case "m" :
				// Fallthrough.
			case "med" :
				// Fallthrough.
			case "medium" :
				// Fallthrough.
			case "h" :
				// Fallthrough.
			case "hi" :
				// Fallthrough.
			case "high" :
				return true;
			default :
				return false;
		}
	}
	
	public String toDefaultFormat(String token) {
		token = token.toLowerCase();
		switch (token) {
			case "l" :
				// Fallthrough.
			case "lo" :
				// Fallthrough.
			case "low" :
				return new String("low");
			case "m" :
				// Fallthrough.
			case "med" :
				// Fallthrough.
			case "medium" :
				return new String("medium");
			case "h" :
				// Fallthrough.
			case "hi" :
				// Fallthrough.
			case "high" :
				return new String("high");
			default :
				return null;
		}
	}
	
}
