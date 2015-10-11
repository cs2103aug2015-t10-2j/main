package com.taskboard.main;

import java.util.ArrayList;

public class OpenParameterParser implements ParameterParser {

	public OpenParameterParser() {
		
	}
	
	public ArrayList<Parameter> parseParameters(String commandString) {
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		// remove the commandType token (add, edit, delete, etc.) and remove trailing whitespaces
		String parameterString = commandString.substring(commandString.indexOf(" ")).trim();
		parameters.add(new Parameter(ParameterType.NAME, parameterString));
		
		return parameters;
	}
	
}
