package com.taskboard.main;

import java.util.ArrayList;

public class UndoCommand extends Command {
	
	public UndoCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		Response responseForUndo = new Response();
		
		return responseForUndo;
	}
	
}
