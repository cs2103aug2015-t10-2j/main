//@@author A0123935E
package com.taskboard.main.command;

import java.util.ArrayList;
import java.util.logging.Level;

import com.taskboard.main.GlobalLogger;
import com.taskboard.main.TempStorageManipulator;

import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.Response;

/**
 * This class inherits from the Command class and returns a corresponding Response
 * for invalid command types input by the user.
 * @author Amarparkash Singh Mavi
 *
 */
public class InvalidCommand extends Command {
	
	// This is the feedback message to be displayed to the user
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