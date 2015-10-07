package com.taskboard.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ParameterParser {

	// constants
	
	public static final int MILLISECONDS_PER_DAY = 86400000;
	public static final int DAY_INDEX_MONDAY = 1;
	public static final int DAY_INDEX_TUESDAY = 2;
	public static final int DAY_INDEX_WEDNESDAY = 3;
	public static final int DAY_INDEX_THURSDAY = 4;
	public static final int DAY_INDEX_FRIDAY = 5;
	public static final int DAY_INDEX_SATURDAY = 6;
	public static final int DAY_INDEX_SUNDAY = 7;
	
	public ParameterParser() {
		
	}
	
	public ArrayList<Parameter> parseParameters(String commandString) {
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		ArrayList<String> parameterStrings = extractParameterStrings(commandString);
		
		boolean isNameFound = false;
		boolean isNewNameFound = false;
		for (String currentParameterString : parameterStrings) {
			ArrayList<DelimiterType> delimiterTypes = extractDelimiterTypes(currentParameterString);
			
			if (delimiterTypes.size() == 0) {
				if (!isNameFound) {
					parameters.add(new Parameter(ParameterType.NAME, currentParameterString));
					isNameFound = true;
				} else if (!isNewNameFound) {
					parameters.add(new Parameter(ParameterType.NEW_NAME, currentParameterString));
					isNewNameFound = true;
				} else {
					// Throw exception here (unexpected parameter)
				}
			} else {
				int expectedDelimiterId = 0;
				DelimiterType currentDelimiterType = null;
				DelimiterType expectedDelimiterType = delimiterTypes.get(expectedDelimiterId);
				String expectedDelimiterName = expectedDelimiterType.name().toLowerCase();
				
				String temporaryString = new String();
				for (String currentParameterToken : currentParameterString.split(" ")) {
					if (currentParameterToken.toLowerCase().equals(expectedDelimiterName)) {
						if (!temporaryString.isEmpty()) {
							parameters.addAll(convertToParameters(temporaryString, currentDelimiterType));
							temporaryString = new String();
						}
						
						expectedDelimiterId++;
						currentDelimiterType = expectedDelimiterType;
						if (expectedDelimiterId < delimiterTypes.size()) {
							expectedDelimiterType = delimiterTypes.get(expectedDelimiterId);
							expectedDelimiterName = expectedDelimiterType.name().toLowerCase();
						} else {
							expectedDelimiterType = null;
							expectedDelimiterName = new String();
						}
					} else {
						temporaryString += currentParameterToken + ' ';
					}
				}
				parameters.addAll(convertToParameters(temporaryString, currentDelimiterType));
			}
		}
		
		return parameters;
	}
	
	public static ArrayList<String> extractParameterStrings(String commandString) {
		// remove the commandType token (add, edit, delete, etc.)
		commandString = commandString.substring(commandString.indexOf(' ') + 1, commandString.length());
		// split into tokens with ';' as delimiters
		ArrayList<String> parameterStrings = new ArrayList<String>(Arrays.asList(commandString.split("\\s*;\\s*")));
		
		return parameterStrings;
	}
	
	public static ArrayList<DelimiterType> extractDelimiterTypes(String parameterString) {
		ArrayList<DelimiterType> delimiterTypes = new ArrayList<DelimiterType>();
		
		parameterString = parameterString.toLowerCase();
		for (String parameterToken : parameterString.split(" ")) {
			if (parameterToken.equals("from")) {
				delimiterTypes.add(DelimiterType.FROM);
			} else if (parameterToken.equals("to")) {
				delimiterTypes.add(DelimiterType.TO);
			} else if (parameterToken.equals("by")) {
				delimiterTypes.add(DelimiterType.BY);
			} else if (parameterToken.equals("every")) {
				delimiterTypes.add(DelimiterType.EVERY);
			}
		}
		
		return delimiterTypes;
	}
	
	public static ArrayList<Parameter> convertToParameters(String parameterString, DelimiterType delimiterType) {
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		
		switch (delimiterType) {
			case FROM:
				for (String parameterToken : parameterString.split(" ")) {
					if (isValidDateFormat(parameterToken)) {
						String startDate = toDefaultDateFormat(parameterToken);
						parameters.add(new Parameter(ParameterType.START_DATE, startDate));
					} else if (isValidTimeFormat(parameterToken)) {
						String startTime = toDefaultTimeFormat(parameterToken);
						parameters.add(new Parameter(ParameterType.START_TIME, startTime));
					}
				}
				break;
			case TO:
				for (String parameterToken : parameterString.split(" ")) {
					if (isValidDateFormat(parameterToken)) {
						String endDate = toDefaultDateFormat(parameterToken);
						parameters.add(new Parameter(ParameterType.END_DATE, endDate));
					} else if (isValidTimeFormat(parameterToken)) {
						String endTime = toDefaultTimeFormat(parameterToken);
						parameters.add(new Parameter(ParameterType.END_TIME, endTime));
					}
				}
				break;
			case BY:
				for (String parameterToken : parameterString.split(" ")) {
					if (isValidDateFormat(parameterToken)) {
						String date = toDefaultDateFormat(parameterToken);
						parameters.add(new Parameter(ParameterType.DATE, date));
					} else if (isValidTimeFormat(parameterToken)) {
						String time = toDefaultTimeFormat(parameterToken);
						parameters.add(new Parameter(ParameterType.TIME, time));
					}
				}
				break;
			case EVERY:
				// TBD: recurring task
				break;
			default:
				break;
		}
		
		return parameters;
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
	
	public static String toDefaultDateFormat(String token) {
		token = token.toLowerCase();
		SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();
		switch (token) {
			case "today":
				return defaultDateFormat.format(today);
			case "tomorrow":
				Date tomorrow = new Date(today.getTime() + MILLISECONDS_PER_DAY);
				return defaultDateFormat.format(tomorrow);
			case "monday":
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_MONDAY));
			case "tuesday":
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_TUESDAY));
			case "wednesday":
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_WEDNESDAY));
			case "thursday":
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_THURSDAY));
			case "friday":
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_FRIDAY));
			case "saturday":
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_SATURDAY));
			case "sunday":
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_SUNDAY));
			default:
				return null;
		}
	}
	
	public static boolean isValidTimeFormat(String token) {
		token = token.toLowerCase();
		if (token.indexOf("am") != -1 || token.indexOf("pm") != -1 || token.indexOf(':') != -1) {
			return true;
		} else {
			return false;
		}
	}
	
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
	
	public static Date getNextOccurenceDate(int dayIndex) {
		SimpleDateFormat dayIndexFormat = new SimpleDateFormat("u");
		Date today = new Date();
		int todayDayIndex = Integer.parseInt(dayIndexFormat.format(today));
		
		if (todayDayIndex < dayIndex) {
			Date monday = new Date(today.getTime() + MILLISECONDS_PER_DAY * 
						  (dayIndex - todayDayIndex));
			return monday;
		} else {
			Date monday = new Date(today.getTime() + MILLISECONDS_PER_DAY * 
					  	  (dayIndex - todayDayIndex + 7));
			return monday;
		}
	}
	
	public static void main(String[] args) {
		new ParameterParser().parseParameters("add Meeting with Chris; from tomorrow 7pm to tomorrow 9pm");
	}
	
}
