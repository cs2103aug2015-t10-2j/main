package com.taskboard.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class RestoreCommand extends Command {
	
	private static final String MESSAGE_AFTER_RESTORE = "\"%1$s\" restored to entry list!";
	private static final String MESSAGE_ERROR_FOR_RESTORE = "The entry could not be restored.";
	
	public RestoreCommand(ArrayList<Parameter> parameters) {
		assert parameters != null;
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
		
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	public Response executeCommand() {
		assert _parameters.size() > 0;
		_logger.log(Level.INFO, "Commenced execution of RestoreCommand");
		
		ArrayList<Entry> initialTempStorage = new ArrayList<Entry>();
		for (Entry entry: _tempStorageManipulator.getTempStorage()) {
			initialTempStorage.add(new Entry(entry));
		}
		
		ArrayList<Entry> initialTempArchive = new ArrayList<Entry>();
		for (Entry entry: _tempStorageManipulator.getTempArchive()) {
			initialTempArchive.add(new Entry(entry));
		}
		
		IndexProcessor indexProcessorForRestore = new IndexProcessor();
		ArrayList<Entry> entries = _tempStorageManipulator.getTempArchive();
		Response responseForRestore = indexProcessorForRestore.processInputIndex(_parameters, entries);
		if (responseForRestore.isSuccess()) {
			_logger.log(Level.INFO, "Start process of restoring entry");
			responseForRestore = processRestoringOfEntry();
		}
		
		if (responseForRestore.isSuccess()) {
			_tempStorageManipulator.setLastTempStorage(initialTempStorage);
			_tempStorageManipulator.setLastTempArchive(initialTempArchive);
		}
		
		return responseForRestore;
	}
	
	private Response processRestoringOfEntry() {
		String index = getDetailFromParameter(getIndexParameter());
		int indexValue = Integer.parseInt(index);
		int tempArchiveIndex = indexValue - 1;
		Response responseForRestore = new Response();
		try {
			String entryName = getEntryName(indexValue);
			_tempStorageManipulator.restoreToTempStorage(tempArchiveIndex);
			setSuccessResponseForRestore(responseForRestore, entryName);
			_logger.log(Level.INFO, "Generated success response for restoring entry");
		} catch (IOException ex) {
			setFailureResponseForRestore(responseForRestore);
			_logger.log(Level.INFO, "Generated failure response for restoring entry");
		}
		
		return responseForRestore;
	}
	
	private String getEntryName(int index) {
		ArrayList<Entry> entries = _tempStorageManipulator.getTempArchive(); 
		Entry entry = entries.get(index - 1);
		Parameter entryNameParameter = entry.getNameParameter();
		String entryName = entryNameParameter.getParameterValue();
		
		return entryName;
	}
	
	private void setSuccessResponseForRestore(Response response, String entryName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_RESTORE, entryName);
		response.setFeedback(userFeedback);
		response.setEntries(_tempStorageManipulator.getTempArchive());
	}
	
	private void setFailureResponseForRestore(Response response) {
		response.setIsSuccess(false);
		response.setFeedback(MESSAGE_ERROR_FOR_RESTORE);
	}
}