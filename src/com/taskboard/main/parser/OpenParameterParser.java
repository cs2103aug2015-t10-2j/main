//@@author A0126536E
package com.taskboard.main.parser;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.taskboard.main.GlobalLogger;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.ParameterType;

/**
 * This class is an implementation of ParameterParser and is used to parse an 
 * OPEN command string into its parameters.
 * @author Alvian Prasetya
 */
public class OpenParameterParser implements ParameterParser {
	
	private static final String STRING_EMPTY = "";
	private static final String STRING_SPACE = " ";
	private static final String MESSAGE_NO_PARAMETERS = "No parameters provided.";
	
	private Logger _logger;
	
	public OpenParameterParser() {
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	/**
	 * Returns a list of parameters from a command string.
	 * 
	 * @param commandString		The command string to be parsed into parameters.
	 * @return					ArrayList of parsed parameters.
	 * @throws IllegalArgumentException		If no parameters provided.
	 */
	public ArrayList<Parameter> parseParameters(String commandString) throws IllegalArgumentException {
		_logger.log(Level.INFO, "Started parsing parameters of OPEN command");
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		// remove the commandType token (add, edit, delete, etc.) and remove trailing whitespace
		String parameterString = STRING_EMPTY;
		if (commandString.trim().indexOf(STRING_SPACE) != -1) {
			parameterString = commandString.substring(commandString.indexOf(STRING_SPACE)).trim();
		} else {
			throw new IllegalArgumentException(MESSAGE_NO_PARAMETERS);
		}
		parameters.add(new Parameter(ParameterType.NAME, parameterString));
		
		_logger.log(Level.INFO, "Finished parsing parameters of OPEN command");
		_logger.log(Level.INFO, "Recognized parameters:");
		for (int i = 0; i < parameters.size(); i++) {
			_logger.log(Level.INFO, parameters.get(i).getParameterType().name() + ": " + 
						parameters.get(i).getParameterValue());
		}
		
		return parameters;
	}
	
}
