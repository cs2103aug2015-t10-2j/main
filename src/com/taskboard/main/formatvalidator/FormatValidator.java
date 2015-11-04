//@@author A0126536E
package com.taskboard.main.formatvalidator;

public interface FormatValidator {

	public boolean isValidFormat(String token);
	public String toDefaultFormat(String token);
	
}
