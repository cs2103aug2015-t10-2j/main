package com.taskboard.main;

import java.io.IOException;

import java.util.ArrayList;

public class Delete extends Command {
	
	private static final String MESSAGE_AFTER_DELETE = "\"%1$s\" deleted!";
	private static final String MESSAGE_ERROR_FOR_DELETE = "The entry could not be deleted from the file.";
	
	public Delete(CommandType commandType, ArrayList<Parameter> parameters) {
		_commandType = commandType;
		_parameters = parameters;
	}
	
	public Response executeCommand() {
		Response responseForDelete = new Response();
		
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			if (parameterType == ParameterType.NAME) {
				responseForDelete = deleteByName(parameter);
			}
		}
		
		return responseForDelete;
	}
	
	private Response deleteByName(Parameter parameter) {
		Response responseForDeleteByName = new Response();
		
		String taskName = parameter.getParameterValue();
		
		StorageHandler storageHandler = StorageHandler.getInstance();
		
		if (storageHandler.isDeleteFromFileSuccessful(taskName)) {
			setSuccessResponseForDeleteByName(responseForDeleteByName, taskName);
		} else {
			setFailureResponseForDeleteByName(responseForDeleteByName);
		}
		
		return responseForDeleteByName;
	}
	
	private void setSuccessResponseForDeleteByName(Response response, String taskName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_DELETE, taskName);
		response.setFeedback(userFeedback);
	}
	
	private String getFeedbackForUser(String feedbackMessage, String details) {
		return String.format(feedbackMessage, details);
	}
	
	private void setFailureResponseForDeleteByName(Response response) {
		response.setIsSuccess(false);
		IOException exobj = new IOException(MESSAGE_ERROR_FOR_DELETE);
		response.setException(exobj);
	}
}
