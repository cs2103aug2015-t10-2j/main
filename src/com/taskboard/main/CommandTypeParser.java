package com.taskboard.main;

public class CommandTypeParser {

	public CommandTypeParser() {
		
	}
	
	public CommandType parseCommandType(String commandString) {
		String commandTypeString = extractCommandTypeString(commandString).toLowerCase();
		
		switch (commandTypeString) {
			case "a":
			case "add":
				return CommandType.ADD;
			case "e":
			case "edit":
				return CommandType.EDIT;
			case "d":
			case "delete":
				return CommandType.DELETE;
			case "v":
			case "view":
				return CommandType.VIEW;
			case "o":
			case "open":
				return CommandType.OPEN;
			case "n":
			case "new":
				return CommandType.NEW;
			case "h":
			case "help":
			case "cmd":
			case "command":
			case "commands":
				return CommandType.HELP;
			case "esc":
			case "exit":
				return CommandType.EXIT;
			default:
				return CommandType.UNKNOWN;
		}
	}
	
	public static String extractCommandTypeString(String commandString) {
		if (commandString.indexOf(' ') == -1) {
			return commandString;
		} else {
			return commandString.substring(0, commandString.indexOf(' ')).toLowerCase();
		}
	}
	
}
