package com.taskboard.main;

import java.util.ArrayList;

public abstract class Command {
	
	// attributes
	
	protected static TempStorageManipulator _tempStorageManipulator;
	protected ArrayList<Parameter> _parameters;
	
	// accessors
		
	public ArrayList<Parameter> getParameters() {
		return _parameters;
	}
	
	public TempStorageManipulator getTempStorageManipulator() {
		return _tempStorageManipulator;
	}
	
	// mutators
	
	public void setParameters(ArrayList<Parameter> newParameters) {
		_parameters = newParameters;
	}
	
	protected String getFeedbackForUser(String feedbackMessage, String detail) {
		return String.format(feedbackMessage, detail);
	}
	
	public abstract Response executeCommand(); 
}