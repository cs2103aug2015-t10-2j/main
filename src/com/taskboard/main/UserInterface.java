package com.taskboard.main;

public class UserInterface {

	private CommandReader _commandReader;
	private DisplayBoard _displayBoard;
	
	public UserInterface() {
		_commandReader = new CommandReader();
		_displayBoard = new DisplayBoard();
	}
	
	// accessors
	
	public CommandReader getCommandReader() {
		return _commandReader;
	}
	
	public DisplayBoard getDisplayBoard() {
		return _displayBoard;
	}
	
}
