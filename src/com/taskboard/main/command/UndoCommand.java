//@@author A0126536E
package com.taskboard.main.command;

import java.util.ArrayList;
import java.io.IOException;

import com.taskboard.main.TempStorageManipulator;
import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.Response;

/**
 * This class is responsible for executing the undo command. It swaps 
 * last checkpoint's TempStorage with the current TempStorage to simulate the undo 
 * effect. p.s.: Undo twice is equivalent to Redo.
 * @author Alvian Prasetya
 */
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
		Response responseForUndo = new Response();
		
		ArrayList<Entry> initialTempStorage = new ArrayList<Entry>();
		ArrayList<Entry> initialTempArchive = new ArrayList<Entry>();
		for (Entry entry: _tempStorageManipulator.getTempStorage()) {
			initialTempStorage.add(new Entry(entry));
		}
		for (Entry entry: _tempStorageManipulator.getTempArchive()) {
			initialTempArchive.add(new Entry(entry));
		}
		
		ArrayList<Entry> lastTempStorage;
		if (_tempStorageManipulator.getLastTempStorage() != null) {
			System.out.println("INNNNN");
			lastTempStorage = new ArrayList<Entry>();
			for (Entry entry: _tempStorageManipulator.getLastTempStorage()) {
				lastTempStorage.add(new Entry(entry));
			}
		} else {
			lastTempStorage = null;
		}
		
		ArrayList<Entry> lastTempArchive;
		if (_tempStorageManipulator.getLastTempArchive() != null) {
			System.out.println("INNNNN");
			lastTempArchive = new ArrayList<Entry>();
			for (Entry entry: _tempStorageManipulator.getLastTempArchive()) {
				lastTempArchive.add(new Entry(entry));
			}
		} else {
			lastTempArchive = null;
		}
		
		if (lastTempStorage != null && lastTempArchive != null) {
			// Only updates the lastTempStorage & lastTempArchive upon successful execution.
			try {
				_tempStorageManipulator.setTempStorage(lastTempStorage);
				_tempStorageManipulator.setLastTempStorage(initialTempStorage);
				_tempStorageManipulator.setTempArchive(lastTempArchive);
				_tempStorageManipulator.setLastTempArchive(initialTempArchive);
				responseForUndo.setIsSuccess(true);
				responseForUndo.setFeedback(MESSAGE_UNDO_SUCCESS);
				responseForUndo.setEntries(lastTempStorage);
			} catch (IOException e) {
				responseForUndo.setIsSuccess(false);
				responseForUndo.setFeedback(MESSAGE_UNDO_FAILURE);
			}
		} else {
			responseForUndo.setIsSuccess(false);
			responseForUndo.setFeedback(MESSAGE_UNDO_FAILURE);
		}
		
		return responseForUndo;
	}
	
}
