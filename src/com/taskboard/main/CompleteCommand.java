package com.taskboard.main;

import java.io.IOException;
import java.util.ArrayList;

public class CompleteCommand extends Command {
	
	public CompleteCommand(ArrayList<Parameter> parameters) {
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
		
		Response responseForComplete = new Response();
		
		int indexToComplete = Integer.parseInt(_parameters.get(0).getParameterValue());
		if (indexToComplete >= 1 && indexToComplete <= _tempStorageManipulator.getTempStorage().size()) {
			try {
				_tempStorageManipulator.setCompletedInTempStorage(indexToComplete - 1);
				responseForComplete.setIsSuccess(true);
				responseForComplete.setFeedback("Successfully marked index " + indexToComplete + " as completed.");
				responseForComplete.setEntries(_tempStorageManipulator.getTempStorage());
			} catch (IOException ex) {
				// TBA: Handle IO exception
			}
		} else {
			responseForComplete.setIsSuccess(false);
			responseForComplete.setException(new IllegalArgumentException("The specified index cannot be found."));
		}
		
		if (responseForComplete.isSuccess()) {
			_tempStorageManipulator.setLastTempStorage(initialTempStorage);
			_tempStorageManipulator.setLastTempArchive(initialTempArchive);
		}
		
		return responseForComplete;
	}
	
}
