package com.taskboard.main;

import java.util.ArrayList;

public class Executor {
		
	private Command _commandInput;

	public Executor(String userInput) {
		_commandInput = new Command(userInput);
		processCommand();
	}
		
	public void processCommand() {
		CommandType commandType = _commandInput.getCommandType();
			
		switch (commandType) {
			case ADD:
				executeAddCommand();
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
		
	public void executeAddCommand() {
		ArrayList<Parameter> parameters = _commandInput.getParameters();
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
