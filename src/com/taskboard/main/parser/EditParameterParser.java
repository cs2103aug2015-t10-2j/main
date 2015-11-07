//@@author A0126536E
package com.taskboard.main.parser;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.taskboard.main.GlobalLogger;
import com.taskboard.main.util.DelimiterType;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.ParameterType;

/**
 * This class is an implementation of ParameterParser and is used to parse an 
 * EDIT command string into its parameters.
 * @author Alvian Prasetya
 */
public class EditParameterParser implements ParameterParser {
	
	private static final String STRING_EMPTY = "";
	private static final String STRING_SPACE = " ";
	private static final String MESSAGE_NO_PARAMETERS = "No parameters provided.";
	private static final String MESSAGE_NO_EDITED_DETAILS = "No edited details provided.";
	private static final String MESSAGE_EMPTY_PARAMETER = "Empty %1$s parameter provided.";
	private static final String MESSAGE_NO_INDEX = "No entry index provided.";
	
	private Logger _logger;
	
	public EditParameterParser() {
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	/**
	 * Returns a list of parameters from a command string.
	 * 
	 * @param commandString		The command string to be parsed into parameters.
	 * @return					ArrayList of parsed parameters.
	 * @throws IllegalArgumentException		If no entry index, no parameters, or empty parameter.
	 */
	public ArrayList<Parameter> parseParameters(String commandString) throws IllegalArgumentException {
		_logger.log(Level.INFO, "Started parsing parameters of EDIT command");
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		// remove the commandType token (add, edit, delete, etc.) and remove trailing whitespace
		String parameterString = STRING_EMPTY;
		if (commandString.trim().indexOf(STRING_SPACE) != -1) {
			parameterString = commandString.substring(commandString.indexOf(STRING_SPACE)).trim();
		} else {
			throw new IllegalArgumentException(MESSAGE_NO_PARAMETERS);
		}
		
		if (parameterString.trim().indexOf(STRING_SPACE) != -1) {
			String indexString = parameterString.substring(0, parameterString.indexOf(STRING_SPACE)).trim();
			
			Parameter indexParameter = ParameterParser.getIndex(indexString);
			parameters.add(indexParameter);
			
			parameterString = parameterString.substring(parameterString.indexOf(STRING_SPACE)).trim();
		} else {
			throw new IllegalArgumentException(MESSAGE_NO_EDITED_DETAILS);
		}
		
		
		ArrayList<DelimiterType> delimiterTypes = extractDelimiterTypes(parameterString);
		if (delimiterTypes.isEmpty()) {
			parameters.addAll(convertToParameters(parameterString, DelimiterType.NONE));
		} else {
			parameters.addAll(extractParameters(delimiterTypes, parameterString));
		}
		
		_logger.log(Level.INFO, "Finished parsing parameters of EDIT command");
		_logger.log(Level.INFO, "Recognized parameters:");
		for (int i = 0; i < parameters.size(); i++) {
			_logger.log(Level.INFO, parameters.get(i).getParameterType().name() + ": " + 
						parameters.get(i).getParameterValue());
		}
		
		if (!isParameterExists(parameters, ParameterType.INDEX)) {
			throw new IllegalArgumentException(MESSAGE_NO_INDEX);
		}
		
		return parameters;
	}

	private static ArrayList<DelimiterType> extractDelimiterTypes(String parameterString) {
		ArrayList<DelimiterType> delimiterTypes = new ArrayList<DelimiterType>();

		parameterString = parameterString.toLowerCase();
		String[] tokens = parameterString.split(STRING_SPACE);
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
	
	private static ArrayList<Parameter> extractParameters(ArrayList<DelimiterType> delimiterTypes, 
														  String parameterString) throws IllegalArgumentException {
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		
		int expectedDelimiterId = 0;
		DelimiterType expectedDelimiterType = delimiterTypes.get(expectedDelimiterId);
		String expectedDelimiterName = expectedDelimiterType.name().toLowerCase();

		String temporaryString = STRING_EMPTY;
		String[] tokens = parameterString.split(STRING_SPACE);
		for (int i = tokens.length - 1; i >= 0; i--) {
			if (tokens[i].toLowerCase().equals(expectedDelimiterName)) {
				if (temporaryString.isEmpty()) {
					throw new IllegalArgumentException(String.format(MESSAGE_EMPTY_PARAMETER, expectedDelimiterName));
				} else {
					ArrayList<Parameter> parametersToAdd = convertToParameters(reverseTokens(temporaryString), expectedDelimiterType);
					if (parametersToAdd.isEmpty()) {
						temporaryString += STRING_SPACE + tokens[i];
					} else {
						parameters.addAll(parametersToAdd);
						temporaryString = STRING_EMPTY;
					}
				}

				expectedDelimiterId++;
				if (expectedDelimiterId < delimiterTypes.size()) {
					expectedDelimiterType = delimiterTypes.get(expectedDelimiterId);
					expectedDelimiterName = expectedDelimiterType.name().toLowerCase();
				} else {
					expectedDelimiterType = null;
					expectedDelimiterName = STRING_EMPTY; // name does not have delimiter
				}
			} else {
				temporaryString += STRING_SPACE + tokens[i];
			}
		}
		if (!temporaryString.isEmpty()) {
			parameters.addAll(convertToParameters(reverseTokens(temporaryString), DelimiterType.NONE));
		}
		
		return parameters;
	}
		
	private static String reverseTokens(String parameterString) {
		String resultString = STRING_EMPTY;

		String[] tokens = parameterString.split(STRING_SPACE);
		for (int i = tokens.length - 1; i >= 0; i--) {
			resultString += tokens[i] + STRING_SPACE;
		}

		return resultString.trim();
	}

	private static ArrayList<Parameter> convertToParameters(String parameterString, DelimiterType delimiterType) {
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		
		switch (delimiterType) {
			case NONE:
				parameters.add(ParameterParser.getNewName(parameterString));
				break;
			case FROM:
				parameters.addAll(ParameterParser.getStartDateTime(parameterString));
				break;
			case TO:
				parameters.addAll(ParameterParser.getEndDateTime(parameterString));
				break;
			case BY:
			case AT:
			case ON:
				parameters.addAll(ParameterParser.getDueDateTime(parameterString));
				break;
			case CAT:
				parameters.add(ParameterParser.getCategory(parameterString));
				break;
			case PRI:
				parameters.add(ParameterParser.getPriority(parameterString));
				break;
			default:
				break;
		}

		return parameters;
	}
	
	private boolean isParameterExists(ArrayList<Parameter> parameters, ParameterType parameterType) {
		for (Parameter currentParameter: parameters) {
			if (currentParameter.getParameterType() == parameterType) {
				return true;
			}
		}
		return false;
	}

}
