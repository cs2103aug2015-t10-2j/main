package com.taskboard.main;

import java.util.ArrayList;

public class InvalidCommand extends Command {
	
	private static final String MESSAGE_ERROR_INVALID_COMMAND = "Invalid command type provided.";
	
	public InvalidCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		Response responseForInvalidCommand = new Response();
		responseForInvalidCommand.setIsSuccess(false);
		IllegalArgumentException exobj = new IllegalArgumentException(MESSAGE_ERROR_INVALID_COMMAND);
		responseForInvalidCommand.setException(exobj);
		
		return responseForInvalidCommand;
	}
}