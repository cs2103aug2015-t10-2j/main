package com.taskboard.main;

import java.util.ArrayList;
import java.io.IOException;

public class UndoCommand extends Command {
	
	private static final String MESSAGE_UNDO_SUCCESS = "Succesfully undo last operation.";
	private static final String MESSAGE_UNDO_FAILURE = "Undo failed: no previous checkpoint found.";
	
	public UndoCommand(ArrayList<Parameter> parameters) {
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
		
		ArrayList<Entry> lastTempStorage = new ArrayList<Entry>();
		ArrayList<Entry> lastTempArchive = new ArrayList<Entry>();
		for (Entry entry: _tempStorageManipulator.getLastTempStorage()) {
			lastTempStorage.add(new Entry(entry));
		}
		for (Entry entry: _tempStorageManipulator.getLastTempArchive()) {
			lastTempArchive.add(new Entry(entry));
		}
		
		Response responseForUndo = new Response();
		
		if (lastTempStorage != null && lastTempArchive != null) {
			System.out.println(_tempStorageManipulator.getTempStorage().toString() + _tempStorageManipulator.getLastTempStorage().toString());
			responseForUndo.setIsSuccess(true);
			responseForUndo.setFeedback(MESSAGE_UNDO_SUCCESS);
			responseForUndo.setEntries(lastTempStorage);
			// Only updates the lastTempStorage & lastTempArchive upon successful execution.
			try {
				_tempStorageManipulator.setTempStorage(lastTempStorage);
				_tempStorageManipulator.setLastTempStorage(initialTempStorage);
				_tempStorageManipulator.setTempArchive(lastTempArchive);
				_tempStorageManipulator.setLastTempArchive(initialTempArchive);
			} catch (IOException e) {
				// Handle exceptions here
			}
		} else {
			responseForUndo.setIsSuccess(false);
			responseForUndo.setFeedback(MESSAGE_UNDO_FAILURE);
		}
		
		return responseForUndo;
	}
	
}
