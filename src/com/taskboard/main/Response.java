package com.taskboard.main;

import java.util.ArrayList;

public class Response {
	
	// attributes
	
	private boolean _isSuccess;
	private String _feedback;
	private Exception _exobj;
	private ArrayList<Entry> _entries;
	
	// constructor
	
	public Response() {		
	
	}
	
	// accessors 
	
	public boolean getIsSuccess() {
		return _isSuccess;
	}
	
	public String getFeedback() {
		return _feedback;
	}
	
	public Exception getException() {
		return _exobj;
	}
	
	public ArrayList<Entry> getEntries() {
		return _entries;
	}
	
	// mutators
	
	public void setIsSuccess(boolean isSuccess) {
		_isSuccess = isSuccess;	
	}
	
	public void setFeedback(String feedback) {
		_feedback = feedback;
	}
	
	public void setException(Exception exobj) {
		_exobj = exobj;
	}
	
	public void setEntries(ArrayList<Entry> newEntries) {
		_entries = newEntries;
	}
}
