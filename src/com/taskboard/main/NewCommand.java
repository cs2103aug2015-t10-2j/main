package com.taskboard.main;

import java.io.IOException;

import java.util.ArrayList;
import java.util.logging.Level;

public class NewCommand extends Command {
	
	private static final String MESSAGE_WELCOME = "Welcome to TASKBOARD!";
	private static final String MESSAGE_ERROR_FOR_LAUNCH_NEW = "Failed to create new file.";
	
	public NewCommand(ArrayList<Parameter> parameters) {
		assert parameters != null;
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
		
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	public Response executeCommand() {
		assert _parameters.size() > 0;
		_logger.log(Level.INFO, "Commenced execution of NewCommand");
		
		String fileName = getDetailFromParameter(getNameParameter());
		assert fileName != null;
		_logger.log(Level.INFO, "Successfully retrieved filename: " + fileName);
		
		return getResponseForLaunch(fileName);
	}
	
	private Response getResponseForLaunch(String fileName) {
		Response responseForNew = new Response();
		
		try {
			_tempStorageManipulator.initialise(fileName);
			setSuccessResponseForLaunchNew(responseForNew);
			_logger.log(Level.INFO, "Generated success response for creating new file");
		} catch (IllegalArgumentException ex) {
			setFailureResponseForInvalidNew(responseForNew, ex);
			_logger.log(Level.INFO, "Generated failure response for creating new file with "
					    + "existing filename");
		} catch (IOException ex) {
			setFailureResponseForLaunchNew(responseForNew);
			_logger.log(Level.INFO, "Generated failure response for creating new file");
		}
		
		return responseForNew;
	}
	
	private void setSuccessResponseForLaunchNew(Response response) {
		response.setIsSuccess(true);
		response.setFeedback(MESSAGE_WELCOME);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private void setFailureResponseForInvalidNew(Response response, IllegalArgumentException ex) {
		response.setIsSuccess(false);
		response.setFeedback(ex.getMessage());
	}
	
	private void setFailureResponseForLaunchNew(Response response) {
		response.setIsSuccess(false);
		response.setFeedback(MESSAGE_ERROR_FOR_LAUNCH_NEW);
	}
}