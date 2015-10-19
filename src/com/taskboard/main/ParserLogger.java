package com.taskboard.main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ParserLogger {
	
	private static final String logDirectory = "logs/";
	private static final String logFileName = "CommandParserLog";
	private static final String logFileFormat = ".log";
	
	private static ParserLogger _instance = null;
	private static Logger _logger;
	private static FileHandler _loggerFileHandler;
	private static SimpleFormatter _loggerFormatter;
	
	private ParserLogger() {
		try {
			_loggerFileHandler = new FileHandler(logDirectory + logFileName + logFileFormat, true);
		} catch (IOException exception) {
			// TBA: process exception or throw exception
		}
		_loggerFormatter = new SimpleFormatter();
		_loggerFileHandler.setFormatter(_loggerFormatter);
		_logger = Logger.getLogger(logFileName);
		_logger.addHandler(_loggerFileHandler);
	}
	
	public static ParserLogger getInstance() {
		if (_instance == null) {
			_instance = new ParserLogger();
		}
		return _instance;
	}
	
	public Logger getLogger() {
		return _logger;
	}
	
}
