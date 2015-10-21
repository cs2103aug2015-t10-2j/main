package com.taskboard.main;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

public class RestoreParameterParser implements ParameterParser {

	private Logger _logger;
	
	public RestoreParameterParser() {
		_logger = ParserLogger.getInstance().getLogger();
	}
	
	public ArrayList<Parameter> parseParameters(String commandString) {
		_logger.log(Level.INFO, "Started parsing parameters of RESTORE command");
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		// remove the commandType token (add, edit, delete, etc.) and remove trailing whitespaces
		String parameterString = new String();
		if (commandString.trim().indexOf(" ") != -1) {
			parameterString = commandString.substring(commandString.indexOf(" ")).trim();
		} else {
			throw new IllegalArgumentException();
		}
		
		FormatValidator indexFormatValidator =  new IndexFormatValidator();
		
		if (indexFormatValidator.isValidFormat(parameterString)) {
			parameterString = indexFormatValidator.toDefaultFormat(parameterString);
			parameters.add(new Parameter(ParameterType.INDEX, parameterString));
		} else {
			// TBA: index not found exception
		}
		
		_logger.log(Level.INFO, "Finished parsing parameters of RESTORE command");
		_logger.log(Level.INFO, "  Recognized parameters:");
		for (int i = 0; i < parameters.size(); i++) {
			_logger.log(Level.INFO, "    " + parameters.get(i).getParameterType().name() + ": " + 
						parameters.get(i).getParameterValue());
		}
		
		return parameters;
	}
	
}
