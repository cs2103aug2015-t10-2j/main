package com.taskboard.main;

import java.util.ArrayList;
import java.util.logging.Level;

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