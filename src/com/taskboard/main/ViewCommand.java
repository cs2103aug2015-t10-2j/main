package com.taskboard.main;

import java.util.ArrayList;
import java.util.logging.Level;

public class ViewCommand extends Command {
	
	private static final String MESSAGE_EMPTY_FILE = "There are no registered entries.";
	private static final String MESSAGE_RETRIEVE_SUCCESS = "Successfully retrieved all entries.";

	public ViewCommand(ArrayList<Parameter> parameters) {
		assert parameters != null;
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
		
		_logger = GlobalLogger.getInstance().getLogger();
	}
	 
	public Response executeCommand() {
		_logger.log(Level.INFO, "Commenced execution of ViewCommand");
		Response responseForView = new Response();
		if (isViewWithoutFilter()) {
			setSuccessResponseForViewWithoutFilter(responseForView);
			_logger.log(Level.INFO, "Generated success response for view without filter");
		} else {
			_logger.log(Level.INFO, "Start processing view with filter");
			SelectiveFilterProcessor selectiveFilterProcessor = new SelectiveFilterProcessor();
			ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage();
			responseForView = selectiveFilterProcessor.processFiltering(entries, _parameters);
		}
		
		return responseForView;
	}
	
	private boolean isViewWithoutFilter() {
		if (_parameters.isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	private void setSuccessResponseForViewWithoutFilter(Response response) {
		response.setIsSuccess(true);
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage();
		response.setEntries(entries);
		if (entries.isEmpty()) {
			response.setFeedback(MESSAGE_EMPTY_FILE);
		} else {
			response.setFeedback(MESSAGE_RETRIEVE_SUCCESS);
		}
	}
}