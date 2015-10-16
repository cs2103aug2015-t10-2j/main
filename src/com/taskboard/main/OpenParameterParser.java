package com.taskboard.main;

import java.util.ArrayList;

public class OpenParameterParser implements ParameterParser {

	public OpenParameterParser() {
		
	}
	
	public ArrayList<Parameter> parseParameters(String commandString) {
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		// remove the commandType token (add, edit, delete, etc.) and remove trailing whitespaces
		String parameterString = new String();
		if (commandString.trim().indexOf(" ") != -1) {
			parameterString = commandString.substring(commandString.indexOf(" ")).trim();
		} else {
			throw new IllegalArgumentException();
		}
		parameters.add(new Parameter(ParameterType.NAME, parameterString));
		
		return parameters;
	}
	
}
