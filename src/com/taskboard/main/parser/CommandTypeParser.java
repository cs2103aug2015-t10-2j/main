//@@author A0126536E
package com.taskboard.main.parser;

import com.taskboard.main.util.CommandType;

/**
 * This class is used to parse a command string into its respective CommandType.
 * @author Alvian Prasetya
 */
public class CommandTypeParser {
	
	private static final String STRING_SPACE = " ";
	
	public CommandTypeParser() {
		
	}
	
	public CommandType parseCommandType(String commandString) {
		String commandTypeString = extractCommandTypeString(commandString).toLowerCase();
		
		switch (commandTypeString) {
			case "a" :
				// Fallthrough.
			case "add" :
				return CommandType.ADD;
			case "e" :
				// Fallthrough.
			case "edit" :
				return CommandType.EDIT;
			case "d" :
				// Fallthrough.
			case "delete" :
				return CommandType.DELETE;
			case "v" :
				// Fallthrough.
			case "view" :
				return CommandType.VIEW;
			case "o" :
				// Fallthrough.
			case "open" :
				return CommandType.OPEN;
			case "n" :
				// Fallthrough.
			case "new" :
				return CommandType.NEW;
			case "done" :
				// Fallthrough.
			case "c" :
				// Fallthrough.
			case "check" :
				// Fallthrough.
			case "complete" :
				return CommandType.COMPLETE;
			case "r" :
				// Fallthrough.
			case "restore" :
				// Fallthrough.
			case "revert" :
				return CommandType.RESTORE;
			case "arc" :
				// Fallthrough.
			case "arch" :
				// Fallthrough.
			case "archive" :
				return CommandType.ARCHIVE;
			case "undo" :
				// Fallthrough.
			case "un" :
				// Fallthrough.
			case "u" :
				return CommandType.UNDO;
			case "background" :
				// Fallthrough.
			case "bg" :
				return CommandType.BACKGROUND;
			case "reminder" :
				// Fallthrough.
			case "remind" :
				return CommandType.REMINDER;
			case "h" :
				// Fallthrough.
			case "help" :
				// Fallthrough.
			case "cmd" :
				// Fallthrough.
			case "command" :
				// Fallthrough.
			case "commands":
				return CommandType.HELP;
			case "esc" :
				// Fallthrough.
			case "exit":
				return CommandType.EXIT;
			default:
				return CommandType.UNKNOWN;
		}
	}
	
	public static String extractCommandTypeString(String commandString) {
		if (commandString.indexOf(STRING_SPACE) == -1) {
			return commandString;
		} else {
			return commandString.substring(0, commandString.indexOf(STRING_SPACE)).toLowerCase();
		}
	}
	
}
