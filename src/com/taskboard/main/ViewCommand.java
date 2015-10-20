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
		
		if (isViewWithoutFilter()) {
			setSuccessResponseForView(responseForView);
		} else {
			responseForView = processFilteredView();
		}
		
		return responseForView;
	}
	
	public boolean isViewWithoutFilter() {
		if (_parameters.isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	public void setSuccessResponseForView(Response response) {
		response.setIsSuccess(true);
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage();
		response.setEntries(entries);
		
		if (entries.isEmpty()) {
			response.setFeedback(MESSAGE_EMPTY_FILE);
		} else {
			response.setFeedback(MESSAGE_RETRIEVE_SUCCESS);
		}
	}
	
	public Response processFilteredView() {
		Response responseForFilteredView = new Response();
		
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		return responseForFilteredView;
	}
}