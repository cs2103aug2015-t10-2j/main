package com.taskboard.main;

public class UserInterface {

	private CommandReader _commandReader;
	private DisplayBoard _displayBoard;
	private Logic _logic;
	private Response _response;
	
	public UserInterface() {
		_commandReader = new CommandReader();
		_displayBoard = new DisplayBoard();
		_logic = new Logic();
		_response = new Response();
	}
	
	public static void main(String[] args){
		UserInterface _userInterface = new UserInterface();
		
		String userInput = _userInterface.getCommandReader().getNextCommand();	
		while (!userInput.toLowerCase().equals("exit")) {
			Response currentResponse = _userInterface.getLogic().processCommand(userInput);
			if (currentResponse.getIsSuccess()) {
				System.out.println(currentResponse.getFeedback());
			} else {
				System.out.println(currentResponse.getException().getMessage());
			}
			userInput = _userInterface.getCommandReader().getNextCommand();
		}
	}
	
	// accessors
	
	public CommandReader getCommandReader() {
		return _commandReader;
	}
	
	public Logic getLogic() {
		return _logic;
	}
	
	public Response getResponse() {
		return _response;
	}
	
	public DisplayBoard getDisplayBoard() {
		return _displayBoard;
	}
	
}
