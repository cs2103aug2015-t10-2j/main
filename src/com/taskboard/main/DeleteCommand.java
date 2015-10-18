package com.taskboard.main;

import java.io.IOException;

import java.util.ArrayList;

public class DeleteCommand extends Command {
	
	private static final String MESSAGE_AFTER_DELETE = "\"%1$s\" deleted!";
	private static final String MESSAGE_ERROR_FOR_DELETE = "The entry could not be deleted from the file.";
	
	public DeleteCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		Response responseForDelete = new Response();
		
		if (isDeleteByIndex()) {
			ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage();
			IndexValidator indexValidator = new IndexValidator();
			
			responseForDelete = indexValidator.checkValidityOfInputIndex(_parameters, entries);
		}
		
		return responseForDelete;
	}
	
	private boolean isDeleteByIndex() {
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			if (parameterType == ParameterType.INDEX) {
				return true;
			}
		}
		
		return false; 
	}
	
//	private Response deleteByName(Parameter parameter) {
//		Response responseForDeleteByName = new Response();
//		
//		String taskName = parameter.getParameterValue();
//		
//		StorageHandler storageHandler = StorageHandler.getInstance();
//		
//		if (storageHandler.isDeleteFromFileSuccessful(taskName)) {
//			setSuccessResponseForDeleteByName(responseForDeleteByName, taskName);
//		} else {
//			setFailureResponseForDeleteByName(responseForDeleteByName);
//		}
//		
//		return responseForDeleteByName;
//	}
	
	private void setSuccessResponseForDeleteByName(Response response, String taskName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_DELETE, taskName);
		response.setFeedback(userFeedback);
	}
	
	private void setFailureResponseForDeleteByName(Response response) {
		response.setIsSuccess(false);
		IOException exobj = new IOException(MESSAGE_ERROR_FOR_DELETE);
		response.setException(exobj);
	}
}
