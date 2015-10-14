package com.taskboard.main;

public class PriorityFormatValidator implements FormatValidator {

	// constructors
	
	public PriorityFormatValidator() {
		
	}
	
	// functionalies
	
	public boolean isValidFormat(String token) {
		token = token.toLowerCase();
		switch (token) {
			case "l":
			case "lo":
			case "low":
			case "m":
			case "med":
			case "medium":
			case "h":
			case "hi":
			case "high":
				return true;
			default:
				return false;
		}
	}
	
	public String toDefaultFormat(String token) {
		token = token.toLowerCase();
		switch (token) {
			case "l":
			case "lo":
			case "low":
				return new String("low");
			case "m":
			case "med":
			case "medium":
				return new String("medium");
			case "h":
			case "hi":
			case "high":
				return new String("high");
			default:
				return null;
		}
	}
	
}
