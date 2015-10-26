package com.taskboard.main;

public class Logic {
	
	// attribute
	
	private CommandParser _commandParser;
	
	// constructor
	
	public Logic() {
		_commandParser = new CommandParser();
	}
		
	public Response processCommand(String userInput) {
		Command commandInput = _commandParser.parseCommand(userInput);
		
		Response responseForOperations = commandInput.executeCommand();
		
		return responseForOperations; 
	}
}