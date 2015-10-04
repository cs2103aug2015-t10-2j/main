package com.taskboard.main;

import java.util.ArrayList;

import java.io.IOException;

public class Logic {
	
	private static final String MESSAGE_WELCOME = "Welcome to TASKBOARD!"; 
	private static final String MESSAGE_AFTER_ADD = "\"%1$s\" added!";
	private static final String MESSAGE_AFTER_DELETE = "\"%1$s\" deleted!";
	
	private static final int INDEX_OF_TASKNAME_FOR_ADD_FLOATING = 0;
	private static final int INDEX_OF_FILENAME_FOR_LAUNCH = 0;
	
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
			responseForLaunch.setIsSuccess(true);
			String userFeedback = getFeedbackForUser(MESSAGE_WELCOME, fileName);
			responseForLaunch.setFeedback(userFeedback);
		} else {
			responseForLaunch.setIsSuccess(false);
			IOException exobj = new IOException("Fail to create new scheduler.");
			responseForLaunch.setException(exobj);
		}	
		
		return responseForLaunch;
	}
	
	private String getFeedbackForUser(String feedbackMessage, String details) {
		return String.format(feedbackMessage, details);
	}
	
	private Response executeAddCommand(Command commandInput) {
		ArrayList<Parameter> parameters = commandInput.getParameters();
		
		Response responseForAdd = new Response();
		
		if (parameters.size() == 1) {
			Parameter taskNameParameter = parameters.get(INDEX_OF_TASKNAME_FOR_ADD_FLOATING);
			String taskName = taskNameParameter.getParameterValue();
			responseForAdd = addFloatingTask(taskName);
		}
		
		return responseForAdd;
	}
		
	private Response addFloatingTask(String taskName) {		
		Entry floatingTask = formatFloatingTaskForStorage(taskName);
		
		Response responseForAddFloating = new Response();
		
		if (_storageHandler.isAddToFileSuccessful(floatingTask)) {
			responseForAddFloating.setIsSuccess(true);
			String userFeedback = getFeedbackForUser(MESSAGE_AFTER_ADD, taskName);
			responseForAddFloating.setFeedback(userFeedback);
		} else {
			responseForAddFloating.setIsSuccess(false);
			IOException exobj = new IOException("The entry could not be added to the file.");
			responseForAddFloating.setException(exobj);
		}
		
		return responseForAddFloating;
	}
	
	private Entry formatFloatingTaskForStorage(String taskName) {
		String formattedTaskName = "Name: " + taskName;
		
		Entry floatingTask = new Entry();
		floatingTask.addToDetails(formattedTaskName);
		//floatingTask.addToDetails("\n");
		
		return floatingTask;
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
			responseForDeleteByName.setIsSuccess(true);
			String userFeedback = getFeedbackForUser(MESSAGE_AFTER_DELETE, taskName);
			responseForDeleteByName.setFeedback(userFeedback);
		} else {
			responseForDeleteByName.setIsSuccess(false);
			IOException exobj = new IOException("The entry could not be deleted from the file.");
			responseForDeleteByName.setException(exobj);
		}
		
		return responseForDeleteByName;
	}
	
	public String retrieveEntries() {
		String entriesList = _storageHandler.retrieveEntriesInFile();
		
		return entriesList;
	}
	
	private Response failToRecogniseCommand() {
		Response responseForInvalidCommand = new Response();
		responseForInvalidCommand.setIsSuccess(false);
		IllegalArgumentException exobj = new IllegalArgumentException("Invalid command type provided.");
		responseForInvalidCommand.setException(exobj);
		
		return responseForInvalidCommand;
	}
}
