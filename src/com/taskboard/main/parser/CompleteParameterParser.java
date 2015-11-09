//@@author A0126536E
package com.taskboard.main.parser;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.taskboard.main.logger.GlobalLogger;
import com.taskboard.main.util.Parameter;

/**
 * This class is an implementation of ParameterParser and is used to parse a 
 * COMPLETE command string into its parameters.
 * @author Alvian Prasetya
 */
public class CompleteParameterParser implements ParameterParser {
	
	private static final String STRING_EMPTY = "";
	private static final String STRING_SPACE = " ";
	private static final String MESSAGE_NO_PARAMETERS = "No parameters provided.";
	
	private Logger _logger;
	
	public CompleteParameterParser() {
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	/**
	 * Returns a list of parameters from a command string.
	 * 
	 * @param commandString		The command string to be parsed into parameters.
	 * @return					ArrayList of parsed parameters.
	 * @throws IllegalArgumentException		If no index provided.
	 */
	public ArrayList<Parameter> parseParameters(String commandString) throws IllegalArgumentException {
		_logger.log(Level.INFO, "Started parsing parameters of COMPLETE command");
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		// remove the commandType token (add, edit, delete, etc.) and remove trailing whitespace
		String parameterString = STRING_EMPTY;
		if (commandString.trim().indexOf(STRING_SPACE) != -1) {
			parameterString = commandString.substring(commandString.indexOf(STRING_SPACE)).trim();
		} else {
			throw new IllegalArgumentException(MESSAGE_NO_PARAMETERS);
		}
		
		parameters.add(ParameterParser.getIndex(parameterString));
		
		_logger.log(Level.INFO, "Finished parsing parameters of COMPLETE command");
		_logger.log(Level.INFO, "Recognized parameters:");
		for (int i = 0; i < parameters.size(); i++) {
			_logger.log(Level.INFO, parameters.get(i).getParameterType().name() + ": " + 
						parameters.get(i).getParameterValue());
		}
		
		return parameters;
	}
	
}
