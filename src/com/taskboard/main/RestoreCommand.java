package com.taskboard.main;

import java.io.IOException;
import java.util.ArrayList;

public class RestoreCommand extends Command {
	
	public RestoreCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		ArrayList<Entry> initialTempStorage = new ArrayList<Entry>();
		ArrayList<Entry> initialTempArchive = new ArrayList<Entry>();
		for (Entry entry: _tempStorageManipulator.getTempStorage()) {
			initialTempStorage.add(new Entry(entry));
		}
		for (Entry entry: _tempStorageManipulator.getTempArchive()) {
			initialTempArchive.add(new Entry(entry));
		}
		
		Response responseForRestore = new Response();
		
		int indexToRestore = Integer.parseInt(_parameters.get(0).getParameterValue());
		if (indexToRestore >= 1 && indexToRestore <= _tempStorageManipulator.getTempArchive().size()) {
			try {
				_tempStorageManipulator.restoreToTempStorage(indexToRestore - 1);
				responseForRestore.setIsSuccess(true);
				responseForRestore.setFeedback("Successfully restored index " + indexToRestore + " to task list.");
				responseForRestore.setEntries(_tempStorageManipulator.getTempArchive());
			} catch (IOException ex) {
				// TBA: Handle IO exception
			}
		} else {
			responseForRestore.setIsSuccess(false);
			responseForRestore.setException(new IllegalArgumentException("The specified index cannot be found."));
		}
		
		if (responseForRestore.isSuccess()) {
			_tempStorageManipulator.setLastTempStorage(initialTempStorage);
			_tempStorageManipulator.setLastTempArchive(initialTempArchive);
		}
		
		return responseForRestore;
	}
	
}
