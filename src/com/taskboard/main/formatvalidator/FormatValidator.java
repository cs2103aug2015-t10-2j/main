//@@author A0126536E
package com.taskboard.main.formatvalidator;

/**
 * This is an interface in which all format validating classes are implementing.
 * @author Alvian Prasetya
 */
public interface FormatValidator {

	public boolean isValidFormat(String token);
	public String toDefaultFormat(String token);
	
}
