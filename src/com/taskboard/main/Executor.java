package com.taskboard.main;

import java.util.ArrayList;

public class Executor {
		
	private Storage targetStorage;

	public Executor(String fileName) {
		targetStorage = new Storage(fileName);
	}
		
	public void processCommand(String userInput) {
		Command commandInput = new Command(userInput);
		CommandType commandType = commandInput.getCommandType();
			
		switch (commandType) {
			case ADD:
				executeAddCommand(commandInput);
				break;
			case EDIT:
				executeEditCommand();
				break;
			case DELETE:
				executeDeleteCommand();
				break;
			case UNKNOWN:
				return;
		}
			
	}
	
	public void executeAddCommand(Command commandInput) {
		ArrayList<Parameter> parameters = commandInput.getParameters();
		if (parameters.size() == 1) {
			addFloatingTask();
		}
	}
		
	public void addFloatingTask() {
	
	}
	
	private void executeEditCommand() {
		
	}

	private void executeDeleteCommand() {
		
	}
}
