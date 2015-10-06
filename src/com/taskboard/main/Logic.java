package com.taskboard.main;

import java.util.ArrayList;

import java.io.IOException;

public class Logic {
	
	private static final String MESSAGE_WELCOME = "Welcome to TASKBOARD!"; 
	private static final String MESSAGE_AFTER_ADD = "\"%1$s\" added!";
	private static final String MESSAGE_AFTER_DELETE = "\"%1$s\" deleted!";
	private static final String MESSAGE_ERROR_FOR_ADD = "The entry could not be added to the file.";
	private static final String MESSAGE_ERROR_FOR_LAUNCH = "Fail to create new scheduler.";
	private static final String MESSAGE_ERROR_INVALID_TIME = "Invalid time provided.";
	private static final String MESSAGE_ERROR_FOR_DELETE = "The entry could not be deleted from the file.";
	private static final String MESSAGE_ERROR_INVALID_COMMAND = "Invalid command type provided.";
	
	private static final int INDEX_OF_TASKNAME_FOR_ADD_FLOATING = 0;
	private static final int INDEX_OF_FILENAME_FOR_LAUNCH = 0;
	private static final int INDEX_OF_FIRST_CHAR_IN_TIME = 0;
	
	// attribute
	
	private StorageHandler _storageHandler;
	
	// constructor
	
	public Logic() {
		
	}
		
	public Response processCommand(String userInput) {
		Command commandInput = new Command(userInput);
		CommandType commandType = commandInput.getCommandType();
		
		Response responseForOperations = new Response();
		
		switch (commandType) {
			case NEW: case OPEN:
				responseForOperations = executeLaunchCommand(commandInput);
				break;
			case ADD:
				responseForOperations = executeAddCommand(commandInput);
				break;
			case EDIT:
				responseForOperations = executeEditCommand(commandInput);
				break;
			case DELETE:
				responseForOperations = executeDeleteCommand(commandInput);
				break;
			case EXIT:
				System.exit(0);
			case UNKNOWN:
				responseForOperations = failToRecogniseCommand();
				break;
		}
		
		return responseForOperations; 
	}
	
	private Response executeLaunchCommand(Command commandInput) {
		ArrayList<Parameter> parameters = commandInput.getParameters();
		Parameter fileNameParameter = parameters.get(INDEX_OF_FILENAME_FOR_LAUNCH); 
		String fileName = fileNameParameter.getParameterValue();
		
		return getResponseForLaunch(fileName);
	}
	
	private Response getResponseForLaunch(String fileName) {
		_storageHandler = new StorageHandler();
		
		Response responseForLaunch = new Response();
		
		if (_storageHandler.isSetUpSuccessful(fileName)) {
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
	
	private void setFailureResponseForLaunch(Response response) {
		response.setIsSuccess(false);
		IOException exobj = new IOException(MESSAGE_ERROR_FOR_LAUNCH);
		response.setException(exobj);
	}
	
	private String getFeedbackForUser(String feedbackMessage, String details) {
		return String.format(feedbackMessage, details);
	}
	
	private Response executeAddCommand(Command commandInput) {
		ArrayList<Parameter> parameters = commandInput.getParameters();
		
		Response responseForAdd = new Response();
		
		if (isAddFloatingTask(parameters)) {
			responseForAdd = addFloatingTask(parameters);
		} else if (isAddDeadlineTask(parameters)) {
			responseForAdd = addDeadlineTask(parameters);
		}
		
		return responseForAdd;
	}
	
	private boolean isAddFloatingTask (ArrayList<Parameter> parameters) {
		if (parameters.size() == 1) {
			return true;
		}
		
		return false;
	}
		
	private Response addFloatingTask(ArrayList<Parameter> parameters) {		
		Parameter taskNameParameter = parameters.get(INDEX_OF_TASKNAME_FOR_ADD_FLOATING);
		String taskName = taskNameParameter.getParameterValue();
		Entry floatingTask = formatFloatingTaskForStorage(taskName);
		
		Response responseForAddFloating = new Response();
		
		if (_storageHandler.isAddToFileSuccessful(floatingTask)) {
			setSuccessResponseForAddFloating(responseForAddFloating, taskName);
		} else {
			setFailureResponseForAddFloating(responseForAddFloating);
		}
		
		return responseForAddFloating;
	}
	
	private void setSuccessResponseForAddFloating(Response response, String taskName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_ADD, taskName);
		response.setFeedback(userFeedback);
	}
	
	private void setFailureResponseForAddFloating(Response response) {
		response.setIsSuccess(false);
		IOException exobj = new IOException(MESSAGE_ERROR_FOR_ADD);
		response.setException(exobj);
	}
	
	private Entry formatFloatingTaskForStorage(String taskName) {
		String formattedTaskName = "Name: " + taskName;
		
		Entry floatingTask = new Entry();
		floatingTask.addToDetails(formattedTaskName);
		//floatingTask.addToDetails("\n");
		
		return floatingTask;
	}
	
	private boolean isAddDeadlineTask(ArrayList<Parameter> parameters) {
		for (int i = 0; i < parameters.size(); i++) {
			Parameter parameter = parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			if (parameterType == ParameterType.DATE || parameterType == ParameterType.TIME) {
				return true;
			}
		}
		
		return false;
	}
	
	private Response addDeadlineTask(ArrayList<Parameter> parameters) {
		Response responseForAddDeadline = new Response();
		
		for (int i = 0; i < parameters.size(); i++) {
			Parameter parameter = parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			if (parameterType == ParameterType.TIME) {
				String time = parameter.getParameterValue();
			} else if (parameterType == ParameterType.DATE) {
				String date = parameter.getParameterValue();
			} else {
				String taskName = parameter.getParameterValue();
			}
		}
		
//		if (!isDateValid(date)) {
//			
//		} else if (!isTimeValid(time)) {
//			setFailureResponseForInvalidTime(responseForAddDeadline);
//		} else {
//			
//		}
		
		return responseForAddDeadline;
	}
	
	private boolean isDateValid(ArrayList<Parameter> parameters) {
		// Stub
		return false;
	}
	
	private boolean isTimeValid(String time) {
		int indexOfColon = time.indexOf(':');
		String hourPart = time.substring(INDEX_OF_FIRST_CHAR_IN_TIME, indexOfColon);
		int hour = Integer.parseInt(hourPart);
		String minutePart = time.substring(indexOfColon + 1);
		int minute = Integer.parseInt(minutePart);
				
		boolean isHourValid = true;
		boolean isMinuteValid = true;
				
		if (hour < 0 || hour > 23) {
			isHourValid = false;
		}
				
		if (minute < 0 || minute > 59) {
			isMinuteValid = false;
		}
				
		if (!isHourValid || !isMinuteValid) {
			return false;
		}
		
		return true;
	}
	
	private void setFailureResponseForInvalidTime(Response response) {
		response.setIsSuccess(false);
		IllegalArgumentException exobj = new IllegalArgumentException(MESSAGE_ERROR_INVALID_TIME);
		response.setException(exobj);
	}
	
	private Response executeEditCommand(Command commandInput) {
		// STUB
		Response responseForEdit = new Response();
		
		return responseForEdit;
	}

	private Response executeDeleteCommand(Command commandInput) {
		ArrayList<Parameter> parameters = commandInput.getParameters();
		
		Response responseForDelete = new Response();
		
		for (int i = 0; i < parameters.size(); i++) {
			Parameter parameter = parameters.get(i);
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
		
		if (_storageHandler.isDeletingSuccessful(taskName)) {
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
	
	private void setFailureResponseForDeleteByName(Response response) {
		response.setIsSuccess(false);
		IOException exobj = new IOException(MESSAGE_ERROR_FOR_DELETE);
		response.setException(exobj);
	}
	
	public String retrieveEntries() {
		String entriesList = _storageHandler.retrieveEntriesInFile();
		
		return entriesList;
	}
	
	private Response failToRecogniseCommand() {
		Response responseForInvalidCommand = new Response();
		responseForInvalidCommand.setIsSuccess(false);
		IllegalArgumentException exobj = new IllegalArgumentException(MESSAGE_ERROR_INVALID_COMMAND);
		responseForInvalidCommand.setException(exobj);
		
		return responseForInvalidCommand;
	}
}
