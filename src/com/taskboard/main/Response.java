package com.taskboard.main;

public class Response {
	
	// attributes
	
	private boolean _isSuccess;
	private String _feedback;
	private IllegalArgumentException _exobj;
	
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
	
	public IllegalArgumentException getIllegalArgumentException() {
		return _exobj;
	}
	
	// mutators
	
	public void setIsSuccess(boolean isSuccess) {
		_isSuccess = isSuccess;	
	}
	
	public void setFeedback(String feedback) {
		_feedback = feedback;
	}
	
	public void setIllegalArgumentException(IllegalArgumentException exobj) {
		_exobj = exobj;
	}
}
