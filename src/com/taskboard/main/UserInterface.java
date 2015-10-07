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
		String userInput;
		String feedback;
		
		UserInterface _userInterface = new UserInterface();
		userInput = _userInterface.getCommandReader().getNextCommand();	
		
		_userInterface.getLogic().processCommand(userInput);
		
		feedback = _userInterface.getResponse().getFeedback();
		_userInterface.getCommandReader().showToUser(feedback);
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
