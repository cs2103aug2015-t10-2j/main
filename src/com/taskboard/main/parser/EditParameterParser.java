package com.taskboard.main.parser;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.taskboard.main.DateFormatValidator;
import com.taskboard.main.DelimiterType;
import com.taskboard.main.FormatValidator;
import com.taskboard.main.GlobalLogger;
import com.taskboard.main.IndexFormatValidator;
import com.taskboard.main.Parameter;
import com.taskboard.main.ParameterType;
import com.taskboard.main.PriorityFormatValidator;
import com.taskboard.main.TimeFormatValidator;

public class EditParameterParser implements ParameterParser {
	
	private Logger _logger;
	
	public EditParameterParser() {
		_logger = GlobalLogger.getInstance().getLogger();
	}

	public ArrayList<Parameter> parseParameters(String commandString) throws IllegalArgumentException {
		_logger.log(Level.INFO, "Started parsing parameters of EDIT command");
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		// remove the commandType token (add, edit, delete, etc.) and remove trailing whitespace
		String parameterString = "";
		if (commandString.trim().indexOf(" ") != -1) {
			parameterString = commandString.substring(commandString.indexOf(" ")).trim();
		} else {
			throw new IllegalArgumentException("No parameters provided.");
		}
		
		if (parameterString.trim().indexOf(" ") != -1) {
			FormatValidator indexFormatValidator =  new IndexFormatValidator();
			
			String indexString = parameterString.substring(0, parameterString.indexOf(" ")).trim();
			if (indexFormatValidator.isValidFormat(indexString)) {
				indexString = indexFormatValidator.toDefaultFormat(indexString);
				parameters.add(new Parameter(ParameterType.INDEX, indexString));
			} else {
				throw new IllegalArgumentException("No index provided.");
			}
			
			parameterString = parameterString.substring(parameterString.indexOf(" ")).trim();
		} else {
			throw new IllegalArgumentException("No edited details provided.");
		}
		
		
		ArrayList<DelimiterType> delimiterTypes = extractDelimiterTypes(parameterString);
		if (delimiterTypes.isEmpty()) {
			parameters.addAll(convertToParameters(parameterString, DelimiterType.NONE));
		} else {
			int expectedDelimiterId = 0;
			DelimiterType expectedDelimiterType = delimiterTypes.get(expectedDelimiterId);
			String expectedDelimiterName = expectedDelimiterType.name().toLowerCase();

			String temporaryString = new String();
			String[] tokens = parameterString.split(" ");
			for (int i = tokens.length - 1; i >= 0; i--) {
				if (tokens[i].toLowerCase().equals(expectedDelimiterName)) {
					if (temporaryString.isEmpty()) {
						throw new IllegalArgumentException("Empty " + expectedDelimiterName + " parameter provided.");
					} else {
						ArrayList<Parameter> parametersToAdd = convertToParameters(temporaryString, expectedDelimiterType);
						if (parametersToAdd.isEmpty()) {
							temporaryString += ' ' + tokens[i];
						} else {
							temporaryString = reverseTokens(temporaryString);
							parameters.addAll(convertToParameters(temporaryString, expectedDelimiterType));
							temporaryString = new String();
						}
					}

					expectedDelimiterId++;
					if (expectedDelimiterId < delimiterTypes.size()) {
						expectedDelimiterType = delimiterTypes.get(expectedDelimiterId);
						expectedDelimiterName = expectedDelimiterType.name().toLowerCase();
					} else {
						expectedDelimiterType = null;
						expectedDelimiterName = new String(); // name does not have delimiter
					}
				} else {
					temporaryString += ' ' + tokens[i];
				}
			}
			if (!temporaryString.isEmpty()) {
				temporaryString = reverseTokens(temporaryString);
				parameters.addAll(convertToParameters(temporaryString, DelimiterType.NONE));
			}
		}
		
		_logger.log(Level.INFO, "Finished parsing parameters of EDIT command");
		_logger.log(Level.INFO, "  Recognized parameters:");
		for (int i = 0; i < parameters.size(); i++) {
			_logger.log(Level.INFO, "    " + parameters.get(i).getParameterType().name() + ": " + 
						parameters.get(i).getParameterValue());
		}
		
		if (!isParameterExists(parameters, ParameterType.INDEX)) {
			throw new IllegalArgumentException("No entry index provided.");
		}
		
		return parameters;
	}

	private static ArrayList<DelimiterType> extractDelimiterTypes(String parameterString) {
		ArrayList<DelimiterType> delimiterTypes = new ArrayList<DelimiterType>();

		parameterString = parameterString.toLowerCase();
		String[] tokens = parameterString.split(" ");
		for (int i = tokens.length - 1; i >= 0; i--) {
			if (tokens[i].equals("from") && !delimiterTypes.contains(DelimiterType.FROM)) {
				delimiterTypes.add(DelimiterType.FROM);
			} else if (tokens[i].equals("to") && !delimiterTypes.contains(DelimiterType.TO)) {
				delimiterTypes.add(DelimiterType.TO);
			} else if (tokens[i].equals("by") && !delimiterTypes.contains(DelimiterType.BY)) {
				delimiterTypes.add(DelimiterType.BY);
			} else if (tokens[i].equals("at") && !delimiterTypes.contains(DelimiterType.AT)) {
				delimiterTypes.add(DelimiterType.AT);
			} else if (tokens[i].equals("on") && !delimiterTypes.contains(DelimiterType.ON)) {
				delimiterTypes.add(DelimiterType.ON);
			} else if (tokens[i].equals("every") && !delimiterTypes.contains(DelimiterType.EVERY)) {
				delimiterTypes.add(DelimiterType.EVERY);
			} else if (tokens[i].equals("cat") && !delimiterTypes.contains(DelimiterType.CAT)) {
				delimiterTypes.add(DelimiterType.CAT);
			} else if (tokens[i].equals("pri") && !delimiterTypes.contains(DelimiterType.PRI)) {
				delimiterTypes.add(DelimiterType.PRI);
			}
		}

		return delimiterTypes;
	}

	private static String reverseTokens(String parameterString) {
		String resultString = new String();

		String[] tokens = parameterString.split(" ");
		for (int i = tokens.length - 1; i >= 0; i--) {
			resultString += tokens[i] + ' ';
		}

		return resultString.trim();
	}

	private static ArrayList<Parameter> convertToParameters(String parameterString, DelimiterType delimiterType) {
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		FormatValidator dateFormatValidator = new DateFormatValidator();
		FormatValidator timeFormatValidator = new TimeFormatValidator();
		FormatValidator priorityFormatValidator = new PriorityFormatValidator();
		
		switch (delimiterType) {
			case NONE:
				parameters.add(ParameterParser.getNewName(parameterString));
				break;
			case FROM:
				parameters.addAll(ParameterParser.getStartDateTime(parameterString, dateFormatValidator, timeFormatValidator));
				break;
			case TO:
				parameters.addAll(ParameterParser.getEndDateTime(parameterString, dateFormatValidator, timeFormatValidator));
				break;
			case BY:
			case AT:
			case ON:
				parameters.addAll(ParameterParser.getDueDateTime(parameterString, dateFormatValidator, timeFormatValidator));
				break;
			case EVERY:
				// TBD: recurring task
				break;
			case CAT:
				parameters.add(ParameterParser.getCategory(parameterString));
				break;
			case PRI:
				parameters.add(ParameterParser.getPriority(parameterString, priorityFormatValidator));
				break;
			default:
				break;
		}

		return parameters;
	}
	
	private boolean isParameterExists(ArrayList<Parameter> parameters, ParameterType parameterType) {
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getParameterType() == parameterType) {
				return true;
			}
		}
		return false;
	}

}
