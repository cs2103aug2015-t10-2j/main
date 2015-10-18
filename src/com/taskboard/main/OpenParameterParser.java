package com.taskboard.main;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import java.io.IOException;

public class OpenParameterParser implements ParameterParser {
	
	private static final String logDirectory = "logs/";
	private static final String logFileFormat = ".log";
	
	private Logger _logger;
	private FileHandler _loggerFileHandler;
	private SimpleFormatter _loggerFormatter;
	
	public OpenParameterParser() {
		_logger = Logger.getLogger(OpenParameterParser.class.getName());
		try {
			_loggerFileHandler = new FileHandler(logDirectory + OpenParameterParser.class.getSimpleName() 
												 + logFileFormat);
		} catch (IOException exception) {
			// TBA: process exception or throw exception
		}
		_logger.addHandler(_loggerFileHandler);
		_loggerFormatter = new SimpleFormatter();
		_loggerFileHandler.setFormatter(_loggerFormatter);
	}
	
	public ArrayList<Parameter> parseParameters(String commandString) {
		_logger.log(Level.INFO, "Started parsing parameters of OPEN command");
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		// remove the commandType token (add, edit, delete, etc.) and remove trailing whitespaces
		String parameterString = new String();
		if (commandString.trim().indexOf(" ") != -1) {
			parameterString = commandString.substring(commandString.indexOf(" ")).trim();
		} else {
			throw new IllegalArgumentException();
		}
		parameters.add(new Parameter(ParameterType.NAME, parameterString));
		
		_logger.log(Level.INFO, "Finished parsing parameters of OPEN command");
		_logger.log(Level.INFO, "  Recognized parameters:");
		for (int i = 0; i < parameters.size(); i++) {
			_logger.log(Level.INFO, "    " + parameters.get(i).getParameterType().name() + ": " + 
						parameters.get(i).getParameterValue());
		}
		
		return parameters;
	}
	
}
