//@@author A0123935E
package com.taskboard.main.logic;

import com.taskboard.main.command.Command;

import com.taskboard.main.parser.CommandParser;

import com.taskboard.main.util.Response;

/**
 * This class receives the user input and processes it accordingly.
 * It then executes the corresponding command and returns a Response based
 * on the success of the operation.
 * @author Amarparkash Singh Mavi
 *
 */
public class Logic {
	
	// attribute
	
	private CommandParser _commandParser;
	
	// constructor
	
	public Logic() {
		_commandParser = new CommandParser();
	}
	
	// other functionalities
	
	/**
	 * This method processes the input by the user and executes the corresponding
	 * command. It returns a Response based on the success of the operation.
	 * 
	 * @param userInput Input string by the user.
	 * @return          Response.
	 */
	public Response processCommand(String userInput) {
		Response responseForOperations = new Response();
		
		try {
			Command commandInput = _commandParser.parseCommand(userInput);
			responseForOperations = commandInput.executeCommand();
		} catch (IllegalArgumentException ex) {
			responseForOperations.setIsSuccess(false);
			responseForOperations.setFeedback(ex.getMessage());
		}
		
		return responseForOperations; 
	}
}