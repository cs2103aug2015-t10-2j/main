package com.taskboard.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class CompleteCommand extends Command {
	
	private static final String MESSAGE_AFTER_COMPLETE = "\"%1$s\" completed!";
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
			String entryName = getEntryName(indexValue);
			_tempStorageManipulator.setCompletedInTempStorage(tempStorageIndex);
			setSuccessResponseForComplete(responseForComplete, entryName);
			_logger.log(Level.INFO, "Generated success response for indicating entry completion");
		} catch (IOException ex) {
			setFailureResponseForComplete(responseForComplete);
			_logger.log(Level.INFO, "Generated failure response for indicating entry completion");
		}
		
		return responseForComplete;
	}
	
	private String getEntryName(int index) {
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage(); 
		Entry entry = entries.get(index - 1);
		Parameter entryNameParameter = entry.getNameParameter();
		String entryName = entryNameParameter.getParameterValue();
		
		return entryName;
	}
	
	private void setSuccessResponseForComplete(Response response, String entryName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_COMPLETE, entryName);
		response.setFeedback(userFeedback);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private void setFailureResponseForComplete(Response response) {
		response.setIsSuccess(false);
		IOException exobj = new IOException(MESSAGE_ERROR_FOR_COMPLETE);
		response.setException(exobj);
	}
}