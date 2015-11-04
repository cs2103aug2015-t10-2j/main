package com.taskboard.main.parser;

import java.util.ArrayList;

import com.taskboard.main.Parameter;
import com.taskboard.main.ParameterType;
import com.taskboard.main.formatvalidator.FormatValidator;

public interface ParameterParser {
	
	public ArrayList<Parameter> parseParameters(String commandString);
	
	static Parameter getIndex(String parameterString, FormatValidator indexFormatValidator) throws IllegalArgumentException {
		String trimmedParameterString = parameterString.trim();
		if (trimmedParameterString.split(" ").length == 1) {
			if (indexFormatValidator.isValidFormat(parameterString)) {
				String index = indexFormatValidator.toDefaultFormat(parameterString);
				return new Parameter(ParameterType.INDEX, index);
			} else {
				throw new IllegalArgumentException("Invalid numeric format provided.");
			}
		} else {
			throw new IllegalArgumentException("Invalid numeric format provided.");
		}
	}
	
	static Parameter getNewName(String parameterString) {
		return new Parameter(ParameterType.NEW_NAME, parameterString);
	}
	
	static ArrayList<Parameter> getStartDateTime(String parameterString, FormatValidator dateFormatValidator, 
														 FormatValidator timeFormatValidator) {
		ArrayList<Parameter> startDateTime = new ArrayList<Parameter>();
		String parameterTokens[] = parameterString.split(" ");
		String compoundToken = "";
		
		for (int i = parameterTokens.length - 1; i >= 0; i--) {
			compoundToken = (parameterTokens[i] + " " + compoundToken).trim();
			if (dateFormatValidator.isValidFormat(parameterTokens[i])) {
				String startDate = dateFormatValidator.toDefaultFormat(parameterTokens[i]);
				startDateTime.add(new Parameter(ParameterType.START_DATE, startDate));
				compoundToken = "";
			} else if (dateFormatValidator.isValidFormat(compoundToken)) {
				String startDate = dateFormatValidator.toDefaultFormat(compoundToken);
				startDateTime.add(new Parameter(ParameterType.START_DATE, startDate));
				compoundToken = "";
			} else if (timeFormatValidator.isValidFormat(parameterTokens[i])) {
				String startTime = timeFormatValidator.toDefaultFormat(parameterTokens[i]);
				startDateTime.add(new Parameter(ParameterType.START_TIME, startTime));
				compoundToken = "";
			} else if (timeFormatValidator.isValidFormat(compoundToken)) {
				String startTime = timeFormatValidator.toDefaultFormat(compoundToken);
				startDateTime.add(new Parameter(ParameterType.START_TIME, startTime));
				compoundToken = "";
			}
		}

		return startDateTime;
	}

	static ArrayList<Parameter> getEndDateTime(String parameterString, FormatValidator dateFormatValidator, 
													   FormatValidator timeFormatValidator) {
		ArrayList<Parameter> endDateTime = new ArrayList<Parameter>();
		String parameterTokens[] = parameterString.split(" ");
		String compoundToken = "";

		for (int i = parameterTokens.length - 1; i >= 0; i--) {
			compoundToken = (parameterTokens[i] + " " + compoundToken).trim();
			if (dateFormatValidator.isValidFormat(parameterTokens[i])) {
				String endDate = dateFormatValidator.toDefaultFormat(parameterTokens[i]);
				endDateTime.add(new Parameter(ParameterType.END_DATE, endDate));
				compoundToken = "";
			} else if (dateFormatValidator.isValidFormat(compoundToken)) {
				String endDate = dateFormatValidator.toDefaultFormat(compoundToken);
				endDateTime.add(new Parameter(ParameterType.END_DATE, endDate));
				compoundToken = "";
			} else if (timeFormatValidator.isValidFormat(parameterTokens[i])) {
				String endTime = timeFormatValidator.toDefaultFormat(parameterTokens[i]);
				endDateTime.add(new Parameter(ParameterType.END_TIME, endTime));
				compoundToken = "";
			} else if (timeFormatValidator.isValidFormat(compoundToken)) {
				String endTime = timeFormatValidator.toDefaultFormat(compoundToken);
				endDateTime.add(new Parameter(ParameterType.END_TIME, endTime));
				compoundToken = "";
			}
		}

		return endDateTime;
	}

	static ArrayList<Parameter> getDueDateTime(String parameterString, FormatValidator dateFormatValidator, 
													   FormatValidator timeFormatValidator) {
		ArrayList<Parameter> dueDateTime = new ArrayList<Parameter>();
		String parameterTokens[] = parameterString.split(" ");
		String compoundToken = "";

		for (int i = parameterTokens.length - 1; i >= 0; i--) {
			compoundToken = (parameterTokens[i] + " " + compoundToken).trim();
			if (dateFormatValidator.isValidFormat(parameterTokens[i])) {
				String date = dateFormatValidator.toDefaultFormat(parameterTokens[i]);
				dueDateTime.add(new Parameter(ParameterType.DATE, date));
				compoundToken = "";
			} else if (dateFormatValidator.isValidFormat(compoundToken)) {
				String date = dateFormatValidator.toDefaultFormat(compoundToken);
				dueDateTime.add(new Parameter(ParameterType.DATE, date));
				compoundToken = "";
			} else if (timeFormatValidator.isValidFormat(parameterTokens[i])) {
				String time = timeFormatValidator.toDefaultFormat(parameterTokens[i]);
				dueDateTime.add(new Parameter(ParameterType.TIME, time));
				compoundToken = "";
			} else if (timeFormatValidator.isValidFormat(compoundToken)) {
				String time = timeFormatValidator.toDefaultFormat(compoundToken);
				dueDateTime.add(new Parameter(ParameterType.TIME, time));
				compoundToken = "";
			}
		}
		
		return dueDateTime;
	}

	static Parameter getCategory(String parameterString) {
		String trimmedParameterString = parameterString.trim();
		return new Parameter(ParameterType.CATEGORY, trimmedParameterString);
	}

	static Parameter getPriority(String parameterString, FormatValidator priorityFormatValidator) throws IllegalArgumentException {
		String trimmedParameterString = parameterString.trim();
		if (trimmedParameterString.split(" ").length == 1) {
			if (priorityFormatValidator.isValidFormat(trimmedParameterString)) {
				String priority = priorityFormatValidator.toDefaultFormat(trimmedParameterString);
				return new Parameter(ParameterType.PRIORITY, priority);
			} else {
				throw new IllegalArgumentException("Invalid priority format provided.");
			}
		} else {
			throw new IllegalArgumentException("Invalid priority format provided.");
		}
	}
	
}

