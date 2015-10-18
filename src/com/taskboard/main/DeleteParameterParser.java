package com.taskboard.main;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import java.io.IOException;

public class DeleteParameterParser implements ParameterParser {
	
	private static final String logDirectory = "logs/";
	private static final String logFileFormat = ".log";
	
	private Logger _logger;
	private FileHandler _loggerFileHandler;
	private SimpleFormatter _loggerFormatter;
	
	public DeleteParameterParser() {
		_logger = Logger.getLogger(DeleteParameterParser.class.getName());
		try {
			_loggerFileHandler = new FileHandler(logDirectory + DeleteParameterParser.class.getSimpleName() 
												 + logFileFormat);
		} catch (IOException exception) {
			// TBA: process exception or throw exception
		}
		_logger.addHandler(_loggerFileHandler);
		_loggerFormatter = new SimpleFormatter();
		_loggerFileHandler.setFormatter(_loggerFormatter);
	}
	
	public ArrayList<Parameter> parseParameters(String commandString) {
		_logger.log(Level.INFO, "Started parsing parameters of DELETE command");
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		// remove the commandType token (add, edit, delete, etc.) and remove trailing whitespaces
		String parameterString = new String();
		if (commandString.trim().indexOf(" ") != -1) {
			parameterString = commandString.substring(commandString.indexOf(" ")).trim();
		} else {
			throw new IllegalArgumentException();
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
						// throw exception here (empty parameter exception)
					} else {
						ArrayList<Parameter> parametersToAdd = convertToParameters(temporaryString, expectedDelimiterType);
						if (parametersToAdd.isEmpty()) {
							temporaryString += tokens[i] + ' ';
						} else {
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
					temporaryString += tokens[i] + ' ';
				}
			}
			if (!temporaryString.isEmpty()) {
				temporaryString = reverseTokens(temporaryString);
				parameters.addAll(convertToParameters(temporaryString, DelimiterType.NONE));
			}
		}
		
		_logger.log(Level.INFO, "Finished parsing parameters of DELETE command");
		_logger.log(Level.INFO, "  Recognized parameters:");
		for (int i = 0; i < parameters.size(); i++) {
			_logger.log(Level.INFO, "    " + parameters.get(i).getParameterType().name() + ": " + 
						parameters.get(i).getParameterValue());
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
		FormatValidator indexFormatValidator = new IndexFormatValidator();
		FormatValidator dateFormatValidator = new DateFormatValidator();
		FormatValidator timeFormatValidator = new TimeFormatValidator();
		FormatValidator priorityFormatValidator = new PriorityFormatValidator();
		
		switch (delimiterType) {
			case NONE:
				parameters.add(ParameterParser.getIndex(parameterString, indexFormatValidator));
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

}
