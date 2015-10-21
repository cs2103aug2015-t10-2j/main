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
		Response responseForComplete = new Response();
		
		int indexToComplete = Integer.parseInt(_parameters.get(0).getParameterValue());
		try {
			_tempStorageManipulator.setCompletedInTempStorage(indexToComplete - 1);
			responseForComplete.setIsSuccess(true);
			responseForComplete.setFeedback("Successfully marked index " + indexToComplete + " as completed.");
			responseForComplete.setEntries(_tempStorageManipulator.getTempStorage());
		} catch (IOException ex) {
			// TBA: Handle IO exception
		}
		
		return responseForComplete;
	}
	
}
