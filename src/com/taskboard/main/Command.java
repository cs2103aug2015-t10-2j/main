package com.taskboard.main;

import java.util.ArrayList;

public abstract class Command {
	
	// attributes
	
	protected static TempStorageManipulator _tempStorageManipulator;
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
	
	public TempStorageManipulator getTempStorageManipulator() {
		return _tempStorageManipulator;
	}
	
	// mutators
	
	public void setCommandType(CommandType newCommandType) {
		_commandType = newCommandType;
	}
	
	public void setParameters(ArrayList<Parameter> newParameters) {
		_parameters = newParameters;
	}
	
	protected String getFeedbackForUser(String feedbackMessage, String detail) {
		return String.format(feedbackMessage, detail);
	}
	
	public abstract Response executeCommand(); 
}


