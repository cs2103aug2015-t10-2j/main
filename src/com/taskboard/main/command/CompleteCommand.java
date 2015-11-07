//@@author A0123935E
package com.taskboard.main.command;

import java.io.IOException;

import java.util.ArrayList;
import java.util.logging.Level;

import com.taskboard.main.GlobalLogger;
import com.taskboard.main.IndexProcessor;
import com.taskboard.main.TempStorageManipulator;

import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.Response;

/**
 * This class inherits from the Command class and executes the Complete command.
 * @author Amarparkash Singh Mavi
 *
 */
public class CompleteCommand extends Command {
	
	// These are the feedback messages to be displayed to the user
	private static final String MESSAGE_AFTER_COMPLETE = "Entry successfully indicated as completed:";
	private static final String MESSAGE_ERROR_FOR_COMPLETE = "The entry could not be indicated as completed.";
	
	public CompleteCommand(ArrayList<Parameter> parameters) {
		assert parameters != null;
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
		
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	public Response executeCommand() {
		// _parameters must have the index of entry to be completed for Complete command to be valid
		assert _parameters.size() > 0;
		_logger.log(Level.INFO, "Commence execution of CompleteCommand");
		
		// facilitates the Undo command
		ArrayList<Entry> initialTempStorage = new ArrayList<Entry>();
		for (Entry entry: _tempStorageManipulator.getTempStorage()) {
			initialTempStorage.add(new Entry(entry));
		}
		
		// facilitates the Undo command
		ArrayList<Entry> initialTempArchive = new ArrayList<Entry>();
		for (Entry entry: _tempStorageManipulator.getTempArchive()) {
			initialTempArchive.add(new Entry(entry));
		}
		
		IndexProcessor indexProcessorForComplete = new IndexProcessor();
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage();
		Response responseForComplete = indexProcessorForComplete.processInputIndex(_parameters, entries);
		
		if (responseForComplete.isSuccess()) {
			_logger.log(Level.INFO, "Start process of indicating entry completion");
			responseForComplete = processEntryCompletion();
		}
		
		// facilitates the Undo command
		if (responseForComplete.isSuccess()) {
			_tempStorageManipulator.setLastTempStorage(initialTempStorage);
			_tempStorageManipulator.setLastTempArchive(initialTempArchive);
		}
		
		return responseForComplete;
	}
		
	private Response processEntryCompletion() {
		String index = getDetailFromParameter(getIndexParameter());
		int indexValue = Integer.parseInt(index);
		int tempStorageIndex = indexValue - 1;
		Response responseForComplete = new Response();
		try {
			Entry entryToComplete = _tempStorageManipulator.getTempStorage().get(tempStorageIndex);
			Entry entryBeforeComplete = new Entry(entryToComplete);
			_tempStorageManipulator.setCompletedInTempStorage(tempStorageIndex);
			setSuccessResponseForComplete(responseForComplete, entryBeforeComplete);
			_logger.log(Level.INFO, "Generated success response for indicating entry completion");
		} catch (IOException ex) {
			setFailureResponseForComplete(responseForComplete);
			_logger.log(Level.INFO, "Generated failure response for indicating entry completion");
		}
		
		return responseForComplete;
	}
	
	private void setSuccessResponseForComplete(Response response, Entry entry) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForSuccessfulComplete(entry);
		response.setFeedback(userFeedback);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private String getFeedbackForSuccessfulComplete(Entry entry) {
		String feedback = MESSAGE_AFTER_COMPLETE.concat("<br>").concat("<br>").concat(entry.toHTMLString());
		
		return feedback;
	}
	
	private void setFailureResponseForComplete(Response response) {
		response.setIsSuccess(false);
		response.setFeedback(MESSAGE_ERROR_FOR_COMPLETE);
	}
}