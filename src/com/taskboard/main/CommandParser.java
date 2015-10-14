package com.taskboard.main;

public class CommandParser {
	
	private CommandTypeParser _commandTypeParser;
	private ParameterParser _parameterParser;
	
	public CommandParser() {
		_commandTypeParser = new CommandTypeParser();
	}
	
	public Command parseCommand(String commandString) {
		Command newCommand = new Command();
		newCommand.setCommandType(_commandTypeParser.parseCommandType(commandString));
		switch (newCommand.getCommandType()) {
			case NEW:
			case OPEN:
				_parameterParser = new OpenParameterParser();
				newCommand.setParameters(_parameterParser.parseParameters(commandString));
				break;
			case ADD:
				_parameterParser = new AddParameterParser();
				newCommand.setParameters(_parameterParser.parseParameters(commandString));
				break;
			case EDIT:
				_parameterParser = new EditParameterParser();
				newCommand.setParameters(_parameterParser.parseParameters(commandString));
				break;
			case DELETE:
				_parameterParser = new DeleteParameterParser();
				newCommand.setParameters(_parameterParser.parseParameters(commandString));
				break;
			case UNKNOWN:
				throw new UnsupportedOperationException();
			default:
				assert false : "Unexpected execution of unreachable code";
				break;
		}
		
		return newCommand;
	}
	
}
