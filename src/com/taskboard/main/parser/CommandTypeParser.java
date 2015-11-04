//@@author A0126536E
package com.taskboard.main.parser;

import com.taskboard.main.CommandType;

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
			case "done":
			case "c":
			case "check":
			case "complete":
				return CommandType.COMPLETE;
			case "r":
			case "restore":
			case "revert":
				return CommandType.RESTORE;
			case "arc":
			case "arch":
			case "archive":
				return CommandType.ARCHIVE;
			case "undo":
			case "un":
			case "u":
				return CommandType.UNDO;
			case "background":
			case "bg":
				return CommandType.BACKGROUND;
			case "reminder":
			case "remind":
				return CommandType.REMINDER;
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
