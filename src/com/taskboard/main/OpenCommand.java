package com.taskboard.main;

import java.io.IOException;

import java.util.ArrayList;

public class OpenCommand extends Command{
	
	private static final String MESSAGE_WELCOME = "Welcome to TASKBOARD!";
	private static final String MESSAGE_ERROR_FOR_LAUNCH_OPEN = "Failed to open file.";
	
	private static final int INDEX_OF_FILENAME = 0;
	
	public OpenCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		Parameter fileNameParameter = _parameters.get(INDEX_OF_FILENAME); 
		String fileName = fileNameParameter.getParameterValue();
		
		return getResponseForLaunch(fileName);
	}
	
	private Response getResponseForLaunch(String fileName) {
		Response responseForOpen = new Response();
			
		try {
			_tempStorageManipulator.repopulate(fileName);
			setSuccessResponseForOpen(responseForOpen, fileName);
		} catch (IllegalArgumentException ex) {
			setFailureResponseForInvalidOpen(responseForOpen, ex);
		} catch (IOException ex) {
			setFailureResponseForLaunchOpen(responseForOpen);
		}
	
		return responseForOpen;
	}
	
	private void setSuccessResponseForOpen(Response response, String fileName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_WELCOME, fileName);
		response.setFeedback(userFeedback);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private void setFailureResponseForInvalidOpen(Response response, IllegalArgumentException ex) {
		response.setIsSuccess(false);
		response.setException(ex);
	}
	
	private void setFailureResponseForLaunchOpen(Response response) {
		response.setIsSuccess(false);
		IOException exObj = new IOException(MESSAGE_ERROR_FOR_LAUNCH_OPEN);
		response.setException(exObj);
	}
}
