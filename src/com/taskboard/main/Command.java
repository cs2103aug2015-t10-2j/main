package com.taskboard.main;

import java.util.ArrayList;
import java.util.Arrays;

public class Command {
	
	// attributes
	
	private CommandType _commandType;
	private ArrayList<Parameter> _parameters;
	
	// constructors
	
	public Command() {
		
	}
	
	public Command(String commandString) {
		_commandType = extractCommandType(commandString);
		_parameters = new ArrayList<Parameter>();
		_parameters = extractParameters(extractParameterStrings(commandString));
	}
	
	// accessors
	
	public CommandType getCommandType() {
		return _commandType;
	}
		
	public ArrayList<Parameter> getParameters() {
		return _parameters;
	}
	
	// mutators
	
	public void setCommandType(CommandType newCommandType) {
		_commandType = newCommandType;
	}
	
	public void setParameters(ArrayList<Parameter> newParameters) {
		_parameters = newParameters;
	}
	
	// functionalities
	
	public static CommandType extractCommandType(String commandString) {
		String commandTypeString = extractCommandTypeString(commandString);
		
		switch (commandTypeString.toLowerCase()) {
			case "a":
			case "add":
				return CommandType.ADD;
			case "e":
			case "edit":
				return CommandType.EDIT;
			case "d":
			case "delete":
				return CommandType.DELETE;
			default:
				return CommandType.UNKNOWN;
		}
	}
	
	public static String extractCommandTypeString(String commandString) {
		if (commandString.indexOf(' ') == -1) {
			return commandString;
		} else {
			return commandString.substring(0, commandString.indexOf(' ')).toLowerCase();
		}
	}
	
	public static ArrayList<String> extractParameterStrings(String commandString) {
		// remove the commandType token (add, edit, delete, etc.)
		commandString = commandString.substring(commandString.indexOf(' ') + 1, commandString.length());
		
		ArrayList<String> parameterString = new ArrayList<String>(Arrays.asList(commandString.split("\\s*;\\s*")));
		
		return parameterString;
	}
	
	public static String removeSemicolon(String str) {
		if (str.lastIndexOf(';') != str.length() - 1) {
			return str;
		} else {
			return str.substring(0, str.lastIndexOf(';')) + str.substring(str.lastIndexOf(';') + 1);
		}
	}
	
	public static ArrayList<Parameter> extractParameters(ArrayList<String> parameterString) {
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		boolean isNameFound = false;
		boolean isDescFound = false;
		
		for (int i = 0; i < parameterString.size(); i++) {
			DelimiterType delimiter = extractDelimiterType(parameterString.get(i));
			switch (delimiter) {
				case FROM:
					parameters.add(new Parameter(ParameterType.START_DATE, parameterString.get(i)));
					parameters.add(new Parameter(ParameterType.START_TIME, parameterString.get(i)));
					// TBD: remove FROM from the parameter string, split date and time
					break;
				case TO:
					parameters.add(new Parameter(ParameterType.END_DATE, parameterString.get(i)));
					parameters.add(new Parameter(ParameterType.END_TIME, parameterString.get(i)));
					// TBD: remove TO from the parameter string, split date and time
					break;
				case FROM_TO:
					parameters.add(new Parameter(ParameterType.START_DATE, parameterString.get(i)));
					parameters.add(new Parameter(ParameterType.START_TIME, parameterString.get(i)));
					// TBD: remove FROM from the parameter string, split date and time
					parameters.add(new Parameter(ParameterType.END_DATE, parameterString.get(i)));
					parameters.add(new Parameter(ParameterType.END_TIME, parameterString.get(i)));
					// TBD: remove TO from the parameter string, split date and time
					break;
				case BY:
					parameters.add(new Parameter(ParameterType.DATE, parameterString.get(i)));
					parameters.add(new Parameter(ParameterType.TIME, parameterString.get(i)));
					// TBD: remove BY from the parameter string, split date and time
					break;
				case NONE:
					if (!isNameFound) {
						parameters.add(new Parameter(ParameterType.NAME, parameterString.get(i)));
						isNameFound = true;
					} else if (!isDescFound) {
						parameters.add(new Parameter(ParameterType.DESCRIPTION, parameterString.get(i)));
						isDescFound = true;
					} else {
						System.out.println("Unrecognized parameter: " + parameterString.get(i) + "\n");
					}
					break;
				default:
			}
		}
		
		return parameters;
	}
	
	public static DelimiterType extractDelimiterType(String parameterString) {
		parameterString = parameterString.toLowerCase();
		if (parameterString.indexOf("from") != -1 && parameterString.indexOf("to") != -1) {
			return DelimiterType.FROM_TO;
		} else if (parameterString.indexOf("from") != -1) {
			return DelimiterType.FROM;
		} else if (parameterString.indexOf("to") != -1) {
			return DelimiterType.TO;
		} else if (parameterString.indexOf("by") != -1) {
			return DelimiterType.BY;
		} else {
			return DelimiterType.NONE;
		}
	}
	
}
