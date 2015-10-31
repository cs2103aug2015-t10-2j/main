package com.taskboard.main;

import java.util.ArrayList;
import java.util.logging.Level;

public class InvalidCommand extends Command {
	
	private static final String MESSAGE_ERROR_INVALID_COMMAND = "Invalid command type provided.";
	
	public InvalidCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
		
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	public Response executeCommand() {
		Response responseForInvalidCommand = new Response();
		responseForInvalidCommand.setIsSuccess(false);
		IllegalArgumentException exobj = new IllegalArgumentException(MESSAGE_ERROR_INVALID_COMMAND);
		responseForInvalidCommand.setException(exobj);
		_logger.log(Level.INFO, "Generated failure response for invalid command");
		
		return responseForInvalidCommand;
	}
}