package com.taskboard.main;

import java.util.ArrayList;

public class CommandParser {
	
	private CommandTypeParser _commandTypeParser;
	private ParameterParser _parameterParser;
	
	public CommandParser() {
		_commandTypeParser = new CommandTypeParser();
	}
	
	public Command parseCommand(String commandString) {
		CommandType newCommandType = _commandTypeParser.parseCommandType(commandString);
		switch (newCommandType) {
			case NEW:
				_parameterParser = new OpenParameterParser();
				ArrayList<Parameter> newCommandParameters = _parameterParser.parseParameters(commandString);
				return new NewCommand(newCommandType, newCommandParameters);
			case OPEN:
				_parameterParser = new OpenParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				return new OpenCommand(newCommandType, newCommandParameters);
			case ADD:
				_parameterParser = new AddParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				return new AddCommand(newCommandType, newCommandParameters);
			case EDIT:
				_parameterParser = new EditParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				return new EditCommand(newCommandType, newCommandParameters);
			case DELETE:
				_parameterParser = new DeleteParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				return new DeleteCommand(newCommandType, newCommandParameters);
			case VIEW:
				return new ViewCommand(newCommandType, null);
			case UNKNOWN:
				throw new UnsupportedOperationException();
			default:
				assert false : "Unexpected execution of unreachable code";
				return null;
		}
	}
	
}
