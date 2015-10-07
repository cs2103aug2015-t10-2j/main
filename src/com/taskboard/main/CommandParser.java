package com.taskboard.main;

public class CommandParser {
	
	private CommandTypeParser _commandTypeParser;
	private ParameterParser _parameterParser;
	
	public CommandParser() {
		_commandTypeParser = new CommandTypeParser();
		_parameterParser = new ParameterParser();
	}
	
	public Command parseCommand(String commandString) {
		Command newCommand = new Command();
		newCommand.setCommandType(_commandTypeParser.parseCommandType(commandString));
		newCommand.setParameters(_parameterParser.parseParameters(commandString));
		
		return newCommand;
	}
	
}
