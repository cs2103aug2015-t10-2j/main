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

public class CompleteCommand extends Command {
	
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
		assert _parameters.size() > 0;
		_logger.log(Level.INFO, "Commenced execution of CompleteCommand");
		
		ArrayList<Entry> initialTempStorage = new ArrayList<Entry>();
		for (Entry entry: _tempStorageManipulator.getTempStorage()) {
			initialTempStorage.add(new Entry(entry));
		}
		
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
			_tempStorageManipulator.setCompletedInTempStorage(tempStorageIndex);
			setSuccessResponseForComplete(responseForComplete, entryToComplete);
			_logger.log(Level.INFO, "Generated success response for indicating entry completion");
		} catch (IOException ex) {
			setFailureResponseForComplete(responseForComplete);
			_logger.log(Level.INFO, "Generated failure response for indicating entry completion");
		}
		
		return responseForComplete;
	}
	
	private void setSuccessResponseForComplete(Response response, Entry entry) {
		response.setIsSuccess(true);
		String userFeedback = MESSAGE_AFTER_COMPLETE.concat("<br>").concat("<br>").concat(entry.toHTMLString());
		response.setFeedback(userFeedback);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private void setFailureResponseForComplete(Response response) {
		response.setIsSuccess(false);
		response.setFeedback(MESSAGE_ERROR_FOR_COMPLETE);
	}
}