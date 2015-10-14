package com.taskboard.main;

import java.util.ArrayList;

public class InvalidCommand extends Command {
	
	private static final String MESSAGE_ERROR_INVALID_COMMAND = "Invalid command type provided.";
	
	public InvalidCommand(CommandType commandType, ArrayList<Parameter> parameters) {
		_commandType = commandType;
		_parameters = parameters;
	}
	
	public Response executeCommand() {
		Response responseForInvalidCommand = new Response();
		responseForInvalidCommand.setIsSuccess(false);
		IllegalArgumentException exobj = new IllegalArgumentException(MESSAGE_ERROR_INVALID_COMMAND);
		responseForInvalidCommand.setException(exobj);
		
		return responseForInvalidCommand;
	}
}
