package com.taskboard.main;

import java.util.ArrayList;

import java.io.IOException;

public class Logic {
		
	private static final int INDEX_OF_TASKNAME_FOR_ADD_FLOATING = 0;
	private static final int INDEX_OF_FILENAME = 0;
	
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
		String fileName = parameters.get(INDEX_OF_FILENAME).getParameterValue();
		
		return getResponseForLaunch(fileName);
	}
	
	private Response getResponseForLaunch(String fileName) {
		_storageHandler = new StorageHandler();
		
		Response responseForLaunch = new Response();
		
		if (_storageHandler.isSetUpSuccessful(fileName)) {
			responseForLaunch.setIsSuccess(true);
			responseForLaunch.setFeedback("Welcome to TASKBOARD!");
		} else {
			responseForLaunch.setIsSuccess(false);
			IOException exobj = new IOException("Failed to create new scheduler.");
			responseForLaunch.setException(exobj);
		}	
		
		return responseForLaunch;
	}
	
	private Response executeAddCommand(Command commandInput) {
		ArrayList<Parameter> parameters = commandInput.getParameters();
		
		Response responseForAdd = new Response();
		
		if (parameters.size() == 1) {
			responseForAdd = addFloatingTask(parameters);
		}
		
		return responseForAdd;
	}
		
	private Response addFloatingTask(ArrayList<Parameter> parameters) {		
		Entry floatingTask = formatFloatingTaskForStorage(parameters);
		
		Response responseForAddFloating = new Response();
		
		if (_storageHandler.isAddToFileSuccessful(floatingTask)) {
			responseForAddFloating.setIsSuccess(true);
			String userFeedback = getFeedbackForAdd(parameters);
			responseForAddFloating.setFeedback(userFeedback);
		} else {
			responseForAddFloating.setIsSuccess(false);
			IOException exobj = new IOException("The entry could not be added to the file.");
			responseForAddFloating.setException(exobj);
		}
		
		return responseForAddFloating;
	}
	
	private Entry formatFloatingTaskForStorage(ArrayList<Parameter> parameters) {
		String taskName = parameters.get(INDEX_OF_TASKNAME_FOR_ADD_FLOATING).getParameterValue();
		String formattedTaskName = "Name: " + taskName;
		
		Entry floatingTask = new Entry();
		floatingTask.addToDetails(formattedTaskName);
		//floatingTask.addToDetails("\n");
		
		return floatingTask;
	}
	
	private String getFeedbackForAdd(ArrayList<Parameter> parameters) {
		String taskName = parameters.get(INDEX_OF_TASKNAME_FOR_ADD_FLOATING).getParameterValue();
		String userFeedback = "\"".concat(taskName).concat("\"").concat(" added!");
		
		return userFeedback;
	}
	
	private Response executeEditCommand(Command commandInput) {
		// STUB
		Response responseForEdit = new Response();
		
		return responseForEdit;
	}

	private Response executeDeleteCommand(Command commandInput) {
		// STUB
		Response responseForDelete = new Response();
		
		return responseForDelete;
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
