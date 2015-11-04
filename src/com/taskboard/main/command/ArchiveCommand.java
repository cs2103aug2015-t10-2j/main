package com.taskboard.main.command;

import java.util.ArrayList;
import java.util.logging.Level;

import com.taskboard.main.GlobalLogger;
import com.taskboard.main.Parameter;
import com.taskboard.main.Response;
import com.taskboard.main.TempStorageManipulator;

public class ArchiveCommand extends Command {
	
	private static final String MESSAGE_AFTER_ARCHIVE = "Successfully retrieved all archived entries.";  

	public ArchiveCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
		
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	public Response executeCommand() {
		Response responseForArchive = new Response();
		responseForArchive.setIsSuccess(true);
		responseForArchive.setFeedback(MESSAGE_AFTER_ARCHIVE);
		responseForArchive.setEntries(_tempStorageManipulator.getTempArchive());
		_logger.log(Level.INFO, "Generated success response for accessing archive");
		
		return responseForArchive;
	}	
}