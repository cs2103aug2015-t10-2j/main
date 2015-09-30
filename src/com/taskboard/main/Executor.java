 package com.taskboard.main;

import java.util.ArrayList;

public class Executor {
		
	private static final int PARAM_POSITION_TASKNAME_FOR_ADD_FLOATING = 0;
	
	private Storage targetStorage;

	public Executor(String fileName) {
		targetStorage = new Storage(fileName);
	}
		
	public Response processCommand(String userInput) {
		Command commandInput = new Command(userInput);
		CommandType commandType = commandInput.getCommandType();
		
		Response responseForOperations = new Response();
		
		switch (commandType) {
			case ADD:
				responseForOperations = executeAddCommand(commandInput);
				break;
			case EDIT:
				responseForOperations = executeEditCommand(commandInput);
				break;
			case DELETE:
				responseForOperations = executeDeleteCommand(commandInput);
				break;
			case UNKNOWN:
				responseForOperations = failToRecogniseCommand();
				break;
		}
		
		return responseForOperations; 
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
		String userFeedback = getFeedbackForAdd(parameters);
		
		Response responseForAddFloating = new Response();
		responseForAddFloating.setIsSuccess(true);
		responseForAddFloating.setFeedback(userFeedback);
		
		return responseForAddFloating;
	}
	
	private String getFeedbackForAdd(ArrayList<Parameter> parameters) {
		String taskName = parameters.get(PARAM_POSITION_TASKNAME_FOR_ADD_FLOATING).getParameterValue();
		String userFeedback = taskName.concat(" added!");
		
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
	
	private Response failToRecogniseCommand() {
		Response responseForInvalidCommand = new Response();
		responseForInvalidCommand.setIsSuccess(false);
		IllegalArgumentException exobj = new IllegalArgumentException("Invalid command type provided.");
		responseForInvalidCommand.setIllegalArgumentException(exobj);
		
		return responseForInvalidCommand;
	}
}
