package com.taskboard.main;

import java.io.IOException;

import java.util.ArrayList;

public class NewCommand extends Command {
	
	private static final String MESSAGE_WELCOME = "Welcome to TASKBOARD!";
	private static final String MESSAGE_ERROR_FOR_LAUNCH_NEW = "Failed to create new file.";
	
//	private static final int INDEX_OF_FILENAME = 0;
	
	public NewCommand(ArrayList<Parameter> parameters) {
		assert parameters != null;
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		assert _parameters.size() > 0;
//		Parameter fileNameParameter = _parameters.get(INDEX_OF_FILENAME); 
//		assert fileNameParameter != null;
		String fileName = getDetailFromParameter(getNameParameter());
		assert fileName != null;
		return getResponseForLaunch(fileName);
	}
	
	private Response getResponseForLaunch(String fileName) {
		Response responseForNew = new Response();
		
		try {
			_tempStorageManipulator.initialise(fileName);
			setSuccessResponseForNew(responseForNew, fileName);
		} catch (IllegalArgumentException ex) {
			setFailureResponseForInvalidNew(responseForNew, ex);
		} catch (IOException ex) {
			setFailureResponseForLaunchNew(responseForNew);
		}
		
		return responseForNew;
	}
	
	private void setSuccessResponseForNew(Response response, String fileName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_WELCOME, fileName);
		response.setFeedback(userFeedback);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private void setFailureResponseForInvalidNew(Response response, IllegalArgumentException ex) {
		response.setIsSuccess(false);
		response.setException(ex);
	}
	
	private void setFailureResponseForLaunchNew(Response response) {
		response.setIsSuccess(false);
		IOException exObj = new IOException(MESSAGE_ERROR_FOR_LAUNCH_NEW);
		response.setException(exObj);
	}
}
