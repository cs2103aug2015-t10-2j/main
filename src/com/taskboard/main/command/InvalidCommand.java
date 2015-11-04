package com.taskboard.main.command;

import java.util.ArrayList;
import java.util.logging.Level;

import com.taskboard.main.GlobalLogger;
import com.taskboard.main.Parameter;
import com.taskboard.main.Response;
import com.taskboard.main.TempStorageManipulator;

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
		responseForInvalidCommand.setFeedback(MESSAGE_ERROR_INVALID_COMMAND);
		_logger.log(Level.INFO, "Generated failure response for invalid command");
		
		return responseForInvalidCommand;
	}
}