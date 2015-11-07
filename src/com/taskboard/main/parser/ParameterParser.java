//@@author A0126536E
package com.taskboard.main.parser;

import java.util.ArrayList;

import com.taskboard.main.formatvalidator.FormatValidator;
import com.taskboard.main.formatvalidator.NumberFormatValidator;
import com.taskboard.main.formatvalidator.DateFormatValidator;
import com.taskboard.main.formatvalidator.TimeFormatValidator;
import com.taskboard.main.formatvalidator.PriorityFormatValidator;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.ParameterType;

/**
 * This is an interface used to parse Parameters from a command string.
 * Will be implemented through various classes depending on the CommandType.
 * @author Alvian Prasetya
 */
public interface ParameterParser {
	
	static final String STRING_EMPTY = "";
	static final String STRING_SPACE = " ";
	static final String MESSAGE_INVALID_NUMERIC_FORMAT = "Invalid numeric format provided for index.";
	static final String MESSAGE_INVALID_PRIORITY_FORMAT = "Invalid priority format provided.";
	
	public ArrayList<Parameter> parseParameters(String commandString);
	
	static Parameter getIndex(String parameterString) throws IllegalArgumentException {
		FormatValidator numberFormatValidator = new NumberFormatValidator();
		String trimmedParameterString = parameterString.trim();
		if (trimmedParameterString.split(STRING_SPACE).length == 1) {
			if (numberFormatValidator.isValidFormat(parameterString)) {
				String index = numberFormatValidator.toDefaultFormat(parameterString);
				return new Parameter(ParameterType.INDEX, index);
			} else {
				throw new IllegalArgumentException(MESSAGE_INVALID_NUMERIC_FORMAT);
			}
		} else {
			throw new IllegalArgumentException(MESSAGE_INVALID_NUMERIC_FORMAT);
		}
	}
	
	static Parameter getNewName(String parameterString) {
		return new Parameter(ParameterType.NEW_NAME, parameterString);
	}
	
	static ArrayList<Parameter> getStartDateTime(String parameterString) {
		FormatValidator dateFormatValidator = new DateFormatValidator();
		FormatValidator timeFormatValidator = new TimeFormatValidator();
		ArrayList<Parameter> startDateTime = new ArrayList<Parameter>();
		String parameterTokens[] = parameterString.split(STRING_SPACE);
		String compoundToken = STRING_EMPTY;
		
		for (int i = parameterTokens.length - 1; i >= 0; i--) {
			compoundToken = (parameterTokens[i] + STRING_SPACE + compoundToken).trim();
			if (dateFormatValidator.isValidFormat(parameterTokens[i])) {
				String startDate = dateFormatValidator.toDefaultFormat(parameterTokens[i]);
				startDateTime.add(new Parameter(ParameterType.START_DATE, startDate));
				compoundToken = STRING_EMPTY;
			} else if (dateFormatValidator.isValidFormat(compoundToken)) {
				String startDate = dateFormatValidator.toDefaultFormat(compoundToken);
				startDateTime.add(new Parameter(ParameterType.START_DATE, startDate));
				compoundToken = STRING_EMPTY;
			} else if (timeFormatValidator.isValidFormat(parameterTokens[i])) {
				String startTime = timeFormatValidator.toDefaultFormat(parameterTokens[i]);
				startDateTime.add(new Parameter(ParameterType.START_TIME, startTime));
				compoundToken = STRING_EMPTY;
			} else if (timeFormatValidator.isValidFormat(compoundToken)) {
				String startTime = timeFormatValidator.toDefaultFormat(compoundToken);
				startDateTime.add(new Parameter(ParameterType.START_TIME, startTime));
				compoundToken = STRING_EMPTY;
			}
		}

		return startDateTime;
	}

	static ArrayList<Parameter> getEndDateTime(String parameterString) {
		FormatValidator dateFormatValidator = new DateFormatValidator();
		FormatValidator timeFormatValidator = new TimeFormatValidator();
		ArrayList<Parameter> endDateTime = new ArrayList<Parameter>();
		String parameterTokens[] = parameterString.split(STRING_SPACE);
		String compoundToken = STRING_EMPTY;

		for (int i = parameterTokens.length - 1; i >= 0; i--) {
			compoundToken = (parameterTokens[i] + STRING_SPACE + compoundToken).trim();
			if (dateFormatValidator.isValidFormat(parameterTokens[i])) {
				String endDate = dateFormatValidator.toDefaultFormat(parameterTokens[i]);
				endDateTime.add(new Parameter(ParameterType.END_DATE, endDate));
				compoundToken = STRING_EMPTY;
			} else if (dateFormatValidator.isValidFormat(compoundToken)) {
				String endDate = dateFormatValidator.toDefaultFormat(compoundToken);
				endDateTime.add(new Parameter(ParameterType.END_DATE, endDate));
				compoundToken = STRING_EMPTY;
			} else if (timeFormatValidator.isValidFormat(parameterTokens[i])) {
				String endTime = timeFormatValidator.toDefaultFormat(parameterTokens[i]);
				endDateTime.add(new Parameter(ParameterType.END_TIME, endTime));
				compoundToken = STRING_EMPTY;
			} else if (timeFormatValidator.isValidFormat(compoundToken)) {
				String endTime = timeFormatValidator.toDefaultFormat(compoundToken);
				endDateTime.add(new Parameter(ParameterType.END_TIME, endTime));
				compoundToken = STRING_EMPTY;
			}
		}

		return endDateTime;
	}

	static ArrayList<Parameter> getDueDateTime(String parameterString) {
		FormatValidator dateFormatValidator = new DateFormatValidator();
		FormatValidator timeFormatValidator = new TimeFormatValidator();
		ArrayList<Parameter> dueDateTime = new ArrayList<Parameter>();
		String parameterTokens[] = parameterString.split(STRING_SPACE);
		String compoundToken = STRING_EMPTY;

		for (int i = parameterTokens.length - 1; i >= 0; i--) {
			compoundToken = (parameterTokens[i] + STRING_SPACE + compoundToken).trim();
			if (dateFormatValidator.isValidFormat(parameterTokens[i])) {
				String date = dateFormatValidator.toDefaultFormat(parameterTokens[i]);
				dueDateTime.add(new Parameter(ParameterType.DATE, date));
				compoundToken = STRING_EMPTY;
			} else if (dateFormatValidator.isValidFormat(compoundToken)) {
				String date = dateFormatValidator.toDefaultFormat(compoundToken);
				dueDateTime.add(new Parameter(ParameterType.DATE, date));
				compoundToken = STRING_EMPTY;
			} else if (timeFormatValidator.isValidFormat(parameterTokens[i])) {
				String time = timeFormatValidator.toDefaultFormat(parameterTokens[i]);
				dueDateTime.add(new Parameter(ParameterType.TIME, time));
				compoundToken = STRING_EMPTY;
			} else if (timeFormatValidator.isValidFormat(compoundToken)) {
				String time = timeFormatValidator.toDefaultFormat(compoundToken);
				dueDateTime.add(new Parameter(ParameterType.TIME, time));
				compoundToken = STRING_EMPTY;
			}
		}
		
		return dueDateTime;
	}

	static Parameter getCategory(String parameterString) {
		String trimmedParameterString = parameterString.trim();
		return new Parameter(ParameterType.CATEGORY, trimmedParameterString);
	}

	static Parameter getPriority(String parameterString) throws IllegalArgumentException {
		FormatValidator priorityFormatValidator = new PriorityFormatValidator();
		String trimmedParameterString = parameterString.trim();
		if (trimmedParameterString.split(STRING_SPACE).length == 1) {
			if (priorityFormatValidator.isValidFormat(trimmedParameterString)) {
				String priority = priorityFormatValidator.toDefaultFormat(trimmedParameterString);
				return new Parameter(ParameterType.PRIORITY, priority);
			} else {
				throw new IllegalArgumentException(MESSAGE_INVALID_PRIORITY_FORMAT);
			}
		} else {
			throw new IllegalArgumentException(MESSAGE_INVALID_PRIORITY_FORMAT);
		}
	}
	
}

