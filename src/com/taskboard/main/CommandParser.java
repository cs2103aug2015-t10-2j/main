package com.taskboard.main;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

public class CommandParser {
	
	private CommandTypeParser _commandTypeParser;
	private ParameterParser _parameterParser;
	private Logger logger = Logger.getLogger(CommandParser.class.getName());
	
	public CommandParser() {
		_commandTypeParser = new CommandTypeParser();
	}
	
	public Command parseCommand(String commandString) {
		logger.log(Level.INFO, "Started parsing command string");
		CommandType newCommandType = _commandTypeParser.parseCommandType(commandString);
		switch (newCommandType) {
			case NEW:
				_parameterParser = new OpenParameterParser();
				ArrayList<Parameter> newCommandParameters = _parameterParser.parseParameters(commandString);
				logger.log(Level.INFO, "Finished parsing NEW command string");
				return new NewCommand(newCommandType, newCommandParameters);
			case OPEN:
				_parameterParser = new OpenParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				logger.log(Level.INFO, "Finished parsing OPEN command string");
				return new OpenCommand(newCommandType, newCommandParameters);
			case ADD:
				_parameterParser = new AddParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				logger.log(Level.INFO, "Finished parsing ADD command string");
				return new AddCommand(newCommandType, newCommandParameters);
			case EDIT:
				_parameterParser = new EditParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				logger.log(Level.INFO, "Finished parsing EDIT command string");
				return new EditCommand(newCommandType, newCommandParameters);
			case DELETE:
				_parameterParser = new DeleteParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				logger.log(Level.INFO, "Finished parsing DELETE command string");
				return new DeleteCommand(newCommandType, newCommandParameters);
			case VIEW:
				_parameterParser = new ViewParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				logger.log(Level.INFO, "Finished parsing VIEW command string");
				return new ViewCommand(newCommandType, null);
			case UNKNOWN:
				logger.log(Level.INFO, "Finished parsing UNKNOWN command string");
				return new InvalidCommand(null, null);
			default:
				logger.log(Level.SEVERE, "Unexpected execution of unreachable code in CommandParser");
				assert false : "Unexpected execution of unreachable code";
				return null;
		}
	}
	
}
