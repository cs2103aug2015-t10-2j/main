package com.taskboard.main;

import java.util.ArrayList;

public class RedoCommand extends Command {
	
	public RedoCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		Response responseForRedo = new Response();
		
		return responseForRedo;
	}
	
}
