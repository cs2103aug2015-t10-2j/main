package com.taskboard.main;

import java.util.ArrayList;

public class Command {
	
	// constants
	
	public static final int SECONDS_PER_DAY = 1000 * 60 * 60 * 24;
	public static final int DAY_INDEX_MONDAY = 1;
	public static final int DAY_INDEX_TUESDAY = 2;
	public static final int DAY_INDEX_WEDNESDAY = 3;
	public static final int DAY_INDEX_THURSDAY = 4;
	public static final int DAY_INDEX_FRIDAY = 5;
	public static final int DAY_INDEX_SATURDAY = 6;
	public static final int DAY_INDEX_SUNDAY = 7;
	
	// attributes
	
	private CommandType _commandType;
	private ArrayList<Parameter> _parameters;
	
	// constructors
	
	public Command() {
		
	}
	
	public Command(CommandType commandType, ArrayList<Parameter> parameters) {
		_commandType = commandType;
		_parameters = parameters;
	}
	
	// accessors
	
	public CommandType getCommandType() {
		return _commandType;
	}
		
	public ArrayList<Parameter> getParameters() {
		return _parameters;
	}
	
	// mutators
	
	public void setCommandType(CommandType newCommandType) {
		_commandType = newCommandType;
	}
	
	public void setParameters(ArrayList<Parameter> newParameters) {
		_parameters = newParameters;
	}
	
}
