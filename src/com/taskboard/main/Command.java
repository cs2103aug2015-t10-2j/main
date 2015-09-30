package com.taskboard.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import java.text.SimpleDateFormat;

public class Command {
	
	// constants
	
	public static final int SECONDS_PER_DAY = 1000 * 60 * 60 * 24;
	public static final int DAY_INDEX_MONDAY = 1;
	public static final int DAY_INDEX_TUESDAY = 2;
	public static final int DAY_INDEX_WEDNESDAY = 3;
	public static final int DAY_INDEX_THURSDAY = 4;
	public static final int DAY_INDEX_FRIDAY = 5;
	public static final int DAY_INDEX_SATURDAY = 6;
	public static final int DAY_INDEX_SUNDAY = 7;
	
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
		
		ArrayList<String> parameterStrings = new ArrayList<String>(Arrays.asList(commandString.split("\\s*;\\s*")));
		
		return parameterStrings;
	}
	
	public static String removeSemicolon(String str) {
		if (str.lastIndexOf(';') != str.length() - 1) {
			return str;
		} else {
			return str.substring(0, str.lastIndexOf(';')) + str.substring(str.lastIndexOf(';') + 1);
		}
	}
	
	// TBD: extract methods from here
	public static ArrayList<Parameter> extractParameters(ArrayList<String> parameterStrings) {
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		boolean isNameFound = false;
		boolean isDescFound = false;
		
		for (int i = 0; i < parameterStrings.size(); i++) {
			DelimiterType delimiter = extractDelimiterType(parameterStrings.get(i));
			String unparsedParameterString = parameterStrings.get(i);
			
			switch (delimiter) {
				case FROM_TO:
					String startDate = extractNextDate(unparsedParameterString);
					parameters.add(new Parameter(ParameterType.START_DATE, startDate));
					String startTime = extractNextTime(unparsedParameterString);
					parameters.add(new Parameter(ParameterType.START_TIME, startTime));
					
					// TBD: case insensitive search to be implemented in indexOf
					unparsedParameterString = unparsedParameterString.substring(unparsedParameterString.indexOf("To"), unparsedParameterString.length());
					String endDate = extractNextDate(unparsedParameterString);
					parameters.add(new Parameter(ParameterType.END_DATE, endDate));
					String endTime = extractNextTime(unparsedParameterString);
					parameters.add(new Parameter(ParameterType.END_TIME, endTime));
					
					break;
				case FROM:
					startDate = extractNextDate(unparsedParameterString);
					parameters.add(new Parameter(ParameterType.START_DATE, startDate));
					startTime = extractNextTime(unparsedParameterString);
					parameters.add(new Parameter(ParameterType.START_TIME, startTime));
					break;
				case TO:
					endDate = extractNextDate(unparsedParameterString);
					parameters.add(new Parameter(ParameterType.END_DATE, endDate));
					endTime = extractNextTime(unparsedParameterString);
					parameters.add(new Parameter(ParameterType.END_TIME, endTime));
					break;
				case BY:
					String date = extractNextDate(unparsedParameterString);
					parameters.add(new Parameter(ParameterType.DATE, date));
					String time = extractNextTime(unparsedParameterString);
					parameters.add(new Parameter(ParameterType.TIME, time));
					break;
				case NONE:
					if (!isNameFound) {
						parameters.add(new Parameter(ParameterType.NAME, unparsedParameterString));
						isNameFound = true;
					} else if (!isDescFound) {
						parameters.add(new Parameter(ParameterType.DESCRIPTION, unparsedParameterString));
						isDescFound = true;
					} else {
						// TBD: throw unrecognized parameter exception here
					}
					break;
				default:
					// TBD: throw delimiter unrecognized exception here
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
	
	public static String extractNextDate(String stringToParse) {
		String[] tokens = stringToParse.split(" ");
		for (int i = 0; i < tokens.length; i++) {
			if (isValidDateFormat(tokens[i])) {
				return toDefaultDateFormat(tokens[i]);
			}
		}
		// TBD: throw no valid date format exception here
		return null;
	}
	
	public static boolean isValidDateFormat(String token) {
		token = token.toLowerCase();
		switch (token) {
			case "today":
			case "tomorrow":
			case "monday":
			case "tuesday":
			case "wednesday":
			case "thursday":
			case "friday":
			case "saturday":
			case "sunday":
				return true;
			default:
				return false;
		}
	}
	
	// TBD: cover more cases
	public static String toDefaultDateFormat(String token) {
		token = token.toLowerCase();
		SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dayIndexFormat = new SimpleDateFormat("u");
		Date today = new Date();
		int todayDayIndex = Integer.parseInt(dayIndexFormat.format(today));
		switch (token) {
			case "today":
				return defaultDateFormat.format(today);
			case "tomorrow":
				Date tomorrow = new Date(today.getTime() + SECONDS_PER_DAY);
				return defaultDateFormat.format(tomorrow);
			case "monday":
				if (todayDayIndex < DAY_INDEX_MONDAY) {
					Date monday = new Date(today.getTime() + SECONDS_PER_DAY * 
								  (DAY_INDEX_MONDAY - todayDayIndex));
					return defaultDateFormat.format(monday);
				} else {
					Date monday = new Date(today.getTime() + SECONDS_PER_DAY * 
							  	  (DAY_INDEX_MONDAY - todayDayIndex + 7));
					return defaultDateFormat.format(monday);
				}
			case "tuesday":
				if (todayDayIndex < DAY_INDEX_TUESDAY) {
					Date monday = new Date(today.getTime() + SECONDS_PER_DAY * 
								  (DAY_INDEX_TUESDAY - todayDayIndex));
					return defaultDateFormat.format(monday);
				} else {
					Date monday = new Date(today.getTime() + SECONDS_PER_DAY * 
							  	  (DAY_INDEX_TUESDAY - todayDayIndex + 7));
					return defaultDateFormat.format(monday);
				}
			case "wednesday":
				if (todayDayIndex < DAY_INDEX_WEDNESDAY) {
					Date monday = new Date(today.getTime() + SECONDS_PER_DAY * 
								  (DAY_INDEX_WEDNESDAY - todayDayIndex));
					return defaultDateFormat.format(monday);
				} else {
					Date monday = new Date(today.getTime() + SECONDS_PER_DAY * 
							  	  (DAY_INDEX_WEDNESDAY - todayDayIndex + 7));
					return defaultDateFormat.format(monday);
				}
			case "thursday":
				if (todayDayIndex < DAY_INDEX_THURSDAY) {
					Date monday = new Date(today.getTime() + SECONDS_PER_DAY * 
								  (DAY_INDEX_THURSDAY - todayDayIndex));
					return defaultDateFormat.format(monday);
				} else {
					Date monday = new Date(today.getTime() + SECONDS_PER_DAY * 
							  	  (DAY_INDEX_THURSDAY - todayDayIndex + 7));
					return defaultDateFormat.format(monday);
				}
			case "friday":
				if (todayDayIndex < DAY_INDEX_FRIDAY) {
					Date monday = new Date(today.getTime() + SECONDS_PER_DAY * 
								  (DAY_INDEX_FRIDAY - todayDayIndex));
					return defaultDateFormat.format(monday);
				} else {
					Date monday = new Date(today.getTime() + SECONDS_PER_DAY * 
							  	  (DAY_INDEX_FRIDAY - todayDayIndex + 7));
					return defaultDateFormat.format(monday);
				}
			case "saturday":
				if (todayDayIndex < DAY_INDEX_SATURDAY) {
					Date monday = new Date(today.getTime() + SECONDS_PER_DAY * 
								  (DAY_INDEX_SATURDAY - todayDayIndex));
					return defaultDateFormat.format(monday);
				} else {
					Date monday = new Date(today.getTime() + SECONDS_PER_DAY * 
							  	  (DAY_INDEX_SATURDAY - todayDayIndex + 7));
					return defaultDateFormat.format(monday);
				}
			case "sunday":
				if (todayDayIndex < DAY_INDEX_SUNDAY) {
					Date monday = new Date(today.getTime() + SECONDS_PER_DAY * 
								  (DAY_INDEX_SUNDAY - todayDayIndex));
					return defaultDateFormat.format(monday);
				} else {
					Date monday = new Date(today.getTime() + SECONDS_PER_DAY * 
							  	  (DAY_INDEX_SUNDAY - todayDayIndex + 7));
					return defaultDateFormat.format(monday);
				}
			default:
				return null;
		}
	}
	
	public static String extractNextTime(String stringToParse) {
		String[] tokens = stringToParse.split(" ");
		for (int i = 0; i < tokens.length; i++) {
			if (isValidTimeFormat(tokens[i])) {
				return toDefaultTimeFormat(tokens[i]);
			}
		}
		// TBD: throw no valid time format exception here
		return null;
	}
	
	public static boolean isValidTimeFormat(String token) {
		token = token.toLowerCase();
		if (token.indexOf("am") != -1 || token.indexOf("pm") != -1 || token.indexOf(':') != -1) {
			return true;
		} else {
			return false;
		}
	}
	
	// TBD: cover more cases
	public static String toDefaultTimeFormat(String token) {
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
