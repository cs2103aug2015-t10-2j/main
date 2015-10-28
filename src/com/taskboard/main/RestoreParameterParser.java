package com.taskboard.main;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

public class RestoreParameterParser implements ParameterParser {

	private Logger _logger;
	
	public RestoreParameterParser() {
		_logger = ParserLogger.getInstance().getLogger();
	}
	
	public ArrayList<Parameter> parseParameters(String commandString) throws IllegalArgumentException {
		_logger.log(Level.INFO, "Started parsing parameters of RESTORE command");
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		// remove the commandType token (add, edit, delete, etc.) and remove trailing whitespace
		String parameterString = new String();
		if (commandString.trim().indexOf(" ") != -1) {
			parameterString = commandString.substring(commandString.indexOf(" ")).trim();
		} else {
			throw new IllegalArgumentException("No parameters provided.");
		}
		
		FormatValidator indexFormatValidator =  new IndexFormatValidator();
		parameters.add(ParameterParser.getIndex(parameterString, indexFormatValidator));
		
		_logger.log(Level.INFO, "Finished parsing parameters of RESTORE command");
		_logger.log(Level.INFO, "  Recognized parameters:");
		for (int i = 0; i < parameters.size(); i++) {
			_logger.log(Level.INFO, "    " + parameters.get(i).getParameterType().name() + ": " + 
						parameters.get(i).getParameterValue());
		}
		
		return parameters;
	}
	
}
