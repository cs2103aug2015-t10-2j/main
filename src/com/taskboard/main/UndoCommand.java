package com.taskboard.main;

import java.util.ArrayList;

public class UndoCommand extends Command {
	
	private static final String MESSAGE_UNDO_SUCCESS = "Succesfully undo last operation.";
	private static final String MESSAGE_UNDO_FAILED = "Undo failed: no previous checkpoint found.";
	
	public UndoCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		ArrayList<Entry> initialTempStorage = _tempStorageManipulator.getTempStorage();
		
		Response responseForUndo = new Response();
		
		ArrayList<Entry> lastTempStorage = _tempStorageManipulator.getLastTempStorage();
		if (lastTempStorage != null) {
			_tempStorageManipulator.setTempStorage(lastTempStorage);
			_tempStorageManipulator.setLastTempStorage(initialTempStorage);
			responseForUndo.setIsSuccess(true);
			responseForUndo.setFeedback(MESSAGE_UNDO_SUCCESS);
			responseForUndo.setEntries(lastTempStorage);
			// Only updates the lastTempStorage upon successful execution.
			_tempStorageManipulator.setLastTempStorage(initialTempStorage);
		} else {
			responseForUndo.setIsSuccess(false);
			responseForUndo.setException(new IllegalThreadStateException(MESSAGE_UNDO_FAILED));
		}
		
		return responseForUndo;
	}
	
}
