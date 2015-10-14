package com.taskboard.main;

import java.io.IOException;

import java.util.ArrayList;

public class Open extends Command{
	
	private static final String MESSAGE_WELCOME = "Welcome to TASKBOARD!";
	private static final String MESSAGE_ERROR_FOR_LAUNCH = "Failed to create new scheduler.";
	
	private static final int INDEX_OF_FILENAME_FOR_LAUNCH = 0;
	
	public Open(CommandType commandType, ArrayList<Parameter> parameters) {
		_commandType = commandType;
		_parameters = parameters;
	}
	
	public Response executeCommand() {
		Parameter fileNameParameter = _parameters.get(INDEX_OF_FILENAME_FOR_LAUNCH); 
		String fileName = fileNameParameter.getParameterValue();
		
		return getResponseForLaunch(fileName);
	}
	
	private Response getResponseForLaunch(String fileName) {
		Response responseForLaunch = new Response();
		
		StorageHandler storageHandler = StorageHandler.getInstance();
		
		if (storageHandler.isSetUpSuccessful(fileName)) {
			setSuccessResponseForLaunch(responseForLaunch, fileName);
		} else {
			setFailureResponseForLaunch(responseForLaunch);
		}	
		
		return responseForLaunch;
	}
	
	private void setSuccessResponseForLaunch(Response response, String fileName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_WELCOME, fileName);
		response.setFeedback(userFeedback);
	}
	
	private String getFeedbackForUser(String feedbackMessage, String details) {
		return String.format(feedbackMessage, details);
	}
	
	private void setFailureResponseForLaunch(Response response) {
		response.setIsSuccess(false);
		IOException exObj = new IOException(MESSAGE_ERROR_FOR_LAUNCH);
		response.setException(exObj);
	}
}
