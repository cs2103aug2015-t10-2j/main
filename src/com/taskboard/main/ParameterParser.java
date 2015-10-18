package com.taskboard.main;

import java.util.ArrayList;

public interface ParameterParser {

	public ArrayList<Parameter> parseParameters(String commandString);
	
	static Parameter getIndex(String parameterString, FormatValidator indexFormatValidator) {
		String trimmedParameterString = parameterString.trim();
		if (trimmedParameterString.split(" ").length == 1) {
			if (indexFormatValidator.isValidFormat(parameterString)) {
				String index = indexFormatValidator.toDefaultFormat(parameterString);
				return new Parameter(ParameterType.INDEX, index);
			} else {
				// TBA: throw exception here
				return null;
			}
		} else {
			// TBA: throw exception here
			return null;
		}
	}
	
	static Parameter getNewName(String parameterString) {
		return new Parameter(ParameterType.NEW_NAME, parameterString);
	}
	
	static ArrayList<Parameter> getStartDateTime(String parameterString, FormatValidator dateFormatValidator, 
														 FormatValidator timeFormatValidator) {
		ArrayList<Parameter> startDateTime = new ArrayList<Parameter>();

		for (String parameterToken : parameterString.split(" ")) {
			if (dateFormatValidator.isValidFormat(parameterToken)) {
				String startDate = dateFormatValidator.toDefaultFormat(parameterToken);
				startDateTime.add(new Parameter(ParameterType.START_DATE, startDate));
			} else if (timeFormatValidator.isValidFormat(parameterToken)) {
				String startTime = timeFormatValidator.toDefaultFormat(parameterToken);
				startDateTime.add(new Parameter(ParameterType.START_TIME, startTime));
			}
		}

		return startDateTime;
	}

	static ArrayList<Parameter> getEndDateTime(String parameterString, FormatValidator dateFormatValidator, 
													   FormatValidator timeFormatValidator) {
		ArrayList<Parameter> endDateTime = new ArrayList<Parameter>();

		for (String parameterToken : parameterString.split(" ")) {
			if (dateFormatValidator.isValidFormat(parameterToken)) {
				String endDate = dateFormatValidator.toDefaultFormat(parameterToken);
				endDateTime.add(new Parameter(ParameterType.END_DATE, endDate));
			} else if (timeFormatValidator.isValidFormat(parameterToken)) {
				String endTime = timeFormatValidator.toDefaultFormat(parameterToken);
				endDateTime.add(new Parameter(ParameterType.END_TIME, endTime));
			}
		}

		return endDateTime;
	}

	static ArrayList<Parameter> getDueDateTime(String parameterString, FormatValidator dateFormatValidator, 
													   FormatValidator timeFormatValidator) {
		ArrayList<Parameter> dueDateTime = new ArrayList<Parameter>();

		for (String parameterToken : parameterString.split(" ")) {
			if (dateFormatValidator.isValidFormat(parameterToken)) {
				String date = dateFormatValidator.toDefaultFormat(parameterToken);
				dueDateTime.add(new Parameter(ParameterType.DATE, date));
			} else if (timeFormatValidator.isValidFormat(parameterToken)) {
				String time = timeFormatValidator.toDefaultFormat(parameterToken);
				dueDateTime.add(new Parameter(ParameterType.TIME, time));
			}
		}

		return dueDateTime;
	}

	static Parameter getCategory(String parameterString) {
		String trimmedParameterString = parameterString.trim();
		return new Parameter(ParameterType.CATEGORY, trimmedParameterString);
	}

	static Parameter getPriority(String parameterString, FormatValidator priorityFormatValidator) {
		String trimmedParameterString = parameterString.trim();
		if (trimmedParameterString.split(" ").length == 1) {
			if (priorityFormatValidator.isValidFormat(trimmedParameterString)) {
				String priority = priorityFormatValidator.toDefaultFormat(trimmedParameterString);
				return new Parameter(ParameterType.PRIORITY, priority);
			} else {
				// TBA: throw exception here
				return null;
			}
		} else {
			// TBA: throw exception here
			return null;
		}
	}
	
}

