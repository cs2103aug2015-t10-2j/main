//@@author A0123935E
package com.taskboard.main.command;

import java.util.ArrayList;
import java.util.logging.Level;

import com.taskboard.main.GlobalLogger;
import com.taskboard.main.SelectiveFilterProcessor;
import com.taskboard.main.TempStorageManipulator;

import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.Response;

/**
 * This class inherits from the Command class and executes the View command.
 * It returns a corresponding Response that denotes the success of the operation.
 * @author Amarparkash Singh Mavi
 *
 */
public class ViewCommand extends Command {
	
	// These are the feedback messages to be displayed to the user
	private static final String MESSAGE_EMPTY_ENTRY_LIST = "There are no registered entries.";
	private static final String MESSAGE_RETRIEVE_ENTRIES_SUCCESS = "Successfully retrieved all entries.";

	public ViewCommand(ArrayList<Parameter> parameters) {
		assert parameters != null;
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
		
		_logger = GlobalLogger.getInstance().getLogger();
	}
	 
	public Response executeCommand() {
		_logger.log(Level.INFO, "Commence execution of ViewCommand");
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
			response.setFeedback(MESSAGE_EMPTY_ENTRY_LIST);
		} else {
			response.setFeedback(MESSAGE_RETRIEVE_ENTRIES_SUCCESS);
		}
	}
}