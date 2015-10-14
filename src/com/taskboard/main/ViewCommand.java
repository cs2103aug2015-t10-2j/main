package com.taskboard.main;

import java.util.ArrayList;

public class ViewCommand extends Command {
	
	private static final String MESSAGE_EMPTY_FILE = "There are no registered entries.";

	public ViewCommand(CommandType commandType, ArrayList<Parameter> parameters) {
		_commandType = commandType;
		_parameters = parameters;
	}
	
	public Response executeCommand() {
		Response responseForView = new Response();
		
		responseForView.setIsSuccess(true);
		String userFeedback = retrieveEntries();
		
		if (userFeedback.isEmpty()) {
			userFeedback = MESSAGE_EMPTY_FILE;
		}
		
		responseForView.setFeedback(userFeedback);
		
		return responseForView;
	}
	
	private String retrieveEntries() {
		StorageHandler storageHandler = StorageHandler.getInstance();
		String entriesList = storageHandler.retrieveEntriesInFile();
		
		return entriesList;
	}
}
