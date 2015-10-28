package com.taskboard.main;

import java.io.IOException;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.Date;

import java.text.SimpleDateFormat;

public class GlobalLogger {
	
	private static final String logDirectory = "logs/";
	private static final String logFileName = new SimpleDateFormat("dd-MM-yyyy_hh.mm.ss").format(new Date());
	private static final String logFileFormat = ".log";
	
	private static GlobalLogger _instance = null;
	private static Logger _logger;
	private static FileHandler _loggerFileHandler;
	private static SimpleFormatter _loggerFormatter;
	
	private GlobalLogger() {
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
	
	public static GlobalLogger getInstance() {
		if (_instance == null) {
			_instance = new GlobalLogger();
		}
		return _instance;
	}
	
	public Logger getLogger() {
		return _logger;
	}
	
}
