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
		_userInterface.initializeDisplayBoard();
		
		String userInput = _userInterface.getCommandReader().getNextCommand();	
		while (!userInput.toLowerCase().equals("exit")) {
			Response currentResponse = _userInterface.getLogic().processCommand(userInput);
			if (currentResponse.getIsSuccess()) {
				String feedback = currentResponse.getFeedback().trim();
				if (userInput.toLowerCase().equals("view")) {
					_userInterface.getDisplayBoard().getTable(0).getRow(0).getCell(0).setContent(feedback);
					_userInterface.getDisplayBoard().getTable(1).getRow(0).getCell(0).setContent("Successfully displayed all entries.");
				} else {
					_userInterface.getDisplayBoard().getTable(0).getRow(0).getCell(0).setContent("No entry to display.");
					_userInterface.getDisplayBoard().getTable(1).getRow(0).getCell(0).setContent(feedback);
				}
			} else {
				String exception = currentResponse.getException().getMessage();
				_userInterface.getDisplayBoard().getTable(1).getRow(0).getCell(0).setContent(exception);
			}
			_userInterface.getDisplayBoard().printDisplayBoard();
			
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
	
	// functionalities
	
	public void initializeDisplayBoard() {
		Cell displayCell = new Cell();
		Row displayRow = new Row();
		displayRow.addCell(displayCell);
		Table displayTable = new Table();
		displayTable.addRow(displayRow);
		
		Cell responseCell = new Cell();
		Row responseRow = new Row();
		responseRow.addCell(responseCell);
		Table responseTable = new Table();
		responseTable.addRow(responseRow);
		
		_displayBoard.addTable(displayTable);
		_displayBoard.addTable(responseTable);
	}
	
}