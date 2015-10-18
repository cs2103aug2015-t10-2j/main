package com.taskboard.main;

import java.util.ArrayList;

public class ViewCommand extends Command {
	
	private static final String MESSAGE_EMPTY_FILE = "There are no registered entries.";
	private static final String MESSAGE_RETRIEVE_SUCCESS = "Successfully retrieved all entries.";

	public ViewCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		Response responseForView = new Response();
		
		responseForView.setIsSuccess(true);
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage();
		responseForView.setEntries(entries);
		
		if (entries.isEmpty()) {
			responseForView.setFeedback(MESSAGE_EMPTY_FILE);
		} else {
			responseForView.setFeedback(MESSAGE_RETRIEVE_SUCCESS);
		}
	
		return responseForView;
	}
}