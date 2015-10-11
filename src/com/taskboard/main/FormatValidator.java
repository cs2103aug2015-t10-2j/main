package com.taskboard.main;

public interface FormatValidator {

	public boolean isValidFormat(String token);
	public String toDefaultFormat(String token);
	
}
