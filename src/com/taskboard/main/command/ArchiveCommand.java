//@@author A0123935E
package com.taskboard.main.command;

import java.util.ArrayList;
import java.util.logging.Level;

import com.taskboard.main.GlobalLogger;
import com.taskboard.main.TempStorageManipulator;
import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.Response;

public class ArchiveCommand extends Command {
	
	private static final String MESSAGE_EMPTY_ARCHIVE = "There are no completed entries.";
	private static final String MESSAGE_RETRIEVE_ARCHIVE_SUCCESS = "Successfully retrieved all archived entries.";  

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
		ArrayList<Entry> entries = _tempStorageManipulator.getTempArchive();
		responseForArchive.setEntries(entries);
		if (entries.isEmpty()) {
			responseForArchive.setFeedback(MESSAGE_EMPTY_ARCHIVE);
		} else {
			responseForArchive.setFeedback(MESSAGE_RETRIEVE_ARCHIVE_SUCCESS);
		}
		_logger.log(Level.INFO, "Generated success response for accessing archive");
		
		return responseForArchive;
	}	
}