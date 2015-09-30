package com.taskboard.main;

import java.util.Scanner;

public class CommandReader {

	// constants
	
	public static final String MESSAGE_ENTER_COMMAND = "Please enter command: ";
	
	// attributes
	
	private Scanner _scanner;
	
	// constructors
	
	public CommandReader() {
		_scanner = new Scanner(System.in);
	}
	
	public CommandReader(Scanner scanner) {
		_scanner = scanner;
	}
	
	// accessors
	
	public Scanner getScanner() {
		return _scanner;
	}
	
	// mutators
	
	public void setScanner(Scanner newScanner) {
		_scanner = newScanner;
	}
	
	// functionalities
	
	public String getNextCommand() {
		showToUser(MESSAGE_ENTER_COMMAND);
		return _scanner.nextLine();
	}
	
	public void showToUser(String stringToShow) {
		System.out.print(stringToShow);
	}
	
}
