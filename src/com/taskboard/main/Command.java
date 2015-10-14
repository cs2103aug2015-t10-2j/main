package com.taskboard.main;

import java.util.ArrayList;

public abstract class Command {
	
	// attributes
	
	protected CommandType _commandType;
	protected ArrayList<Parameter> _parameters;
	
	// constructors
	
//	public Command() {
//		
//	}
//	
//	public Command(CommandType commandType, ArrayList<Parameter> parameters) {
//		_commandType = commandType;
//		_parameters = parameters;
//	}
	
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
	
	public abstract Response executeCommand(); 
}


