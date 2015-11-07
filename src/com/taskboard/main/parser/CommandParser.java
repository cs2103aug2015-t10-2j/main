//@@author A0126536E
package com.taskboard.main.parser;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.taskboard.main.GlobalLogger;
import com.taskboard.main.command.AddCommand;
import com.taskboard.main.command.ArchiveCommand;
import com.taskboard.main.command.BackgroundCommand;
import com.taskboard.main.command.Command;
import com.taskboard.main.command.CompleteCommand;
import com.taskboard.main.command.DeleteCommand;
import com.taskboard.main.command.EditCommand;
import com.taskboard.main.command.HelpCommand;
import com.taskboard.main.command.InvalidCommand;
import com.taskboard.main.command.NewCommand;
import com.taskboard.main.command.OpenCommand;
import com.taskboard.main.command.ReminderCommand;
import com.taskboard.main.command.RestoreCommand;
import com.taskboard.main.command.UndoCommand;
import com.taskboard.main.command.ViewCommand;
import com.taskboard.main.util.CommandType;
import com.taskboard.main.util.Parameter;

/**
 * This class is a facade class that is used to parse a command string into 
 * a Command object containing CommandType and ArrayList of Parameters.
 * @author Alvian Prasetya
 */
public class CommandParser {
	
	private CommandTypeParser _commandTypeParser;
	private ParameterParser _parameterParser;
	private Logger _logger;
	
	public CommandParser() {
		_commandTypeParser = new CommandTypeParser();
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	public Command parseCommand(String commandString) throws IllegalArgumentException {
		_logger.log(Level.INFO, "Started parsing command string: " + commandString);
		CommandType newCommandType = _commandTypeParser.parseCommandType(commandString);
		switch (newCommandType) {
			case NEW:
				_parameterParser = new OpenParameterParser();
				ArrayList<Parameter> newCommandParameters = _parameterParser.parseParameters(commandString);
				_logger.log(Level.INFO, "Finished parsing NEW command string");
				return new NewCommand(newCommandParameters);
			case OPEN:
				_parameterParser = new OpenParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				_logger.log(Level.INFO, "Finished parsing OPEN command string");
				return new OpenCommand(newCommandParameters);
			case ADD:
				_parameterParser = new AddParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				_logger.log(Level.INFO, "Finished parsing ADD command string");
				return new AddCommand(newCommandParameters);
			case EDIT:
				_parameterParser = new EditParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				_logger.log(Level.INFO, "Finished parsing EDIT command string");
				return new EditCommand(newCommandParameters);
			case DELETE:
				_parameterParser = new DeleteParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				_logger.log(Level.INFO, "Finished parsing DELETE command string");
				return new DeleteCommand(newCommandParameters);
			case VIEW:
				_parameterParser = new ViewParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				_logger.log(Level.INFO, "Finished parsing VIEW command string");
				return new ViewCommand(newCommandParameters);
			case COMPLETE:
				_parameterParser = new CompleteParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				_logger.log(Level.INFO, "Finished parsing COMPLETE command string");
				return new CompleteCommand(newCommandParameters);
			case RESTORE:
				_parameterParser = new RestoreParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				_logger.log(Level.INFO, "Finished parsing RESTORE command string");
				return new RestoreCommand(newCommandParameters);
			case ARCHIVE:
				_logger.log(Level.INFO, "Finished parsing ARCHIVE command string");
				return new ArchiveCommand(null);
			case UNDO:
				_logger.log(Level.INFO, "Finished parsing UNDO command string");
				return new UndoCommand(null);
			case BACKGROUND:
				_parameterParser = new BackgroundParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				_logger.log(Level.INFO, "Finished parsing BACKGROUND command string");
				return new BackgroundCommand(newCommandParameters);
			case REMINDER:
				_parameterParser = new ReminderParameterParser();
				newCommandParameters = _parameterParser.parseParameters(commandString);
				_logger.log(Level.INFO, "Finished parsing REMINDER command string");
				return new ReminderCommand(newCommandParameters);
			case HELP:
				_logger.log(Level.INFO, "Finished parsing HELP command string");
				return new HelpCommand(null);
			case UNKNOWN:
				_logger.log(Level.INFO, "Finished parsing UNKNOWN command string");
				return new InvalidCommand(null);
			default:
				_logger.log(Level.SEVERE, "Unexpected execution of unreachable code in CommandParser");
				assert false : "Unexpected execution of unreachable code";
				return null;
		}
	}
	
}
