package com.taskboard.main;

import java.util.ArrayList;

public class Response {
	
	// attributes
	
	private boolean _isSuccess;
	private String _feedback;
	private Exception _exObj;
	private ArrayList<Entry> _entries;
	
	// constructor
	
	public Response() {		
	
	}
	
	// accessors 
	
	public boolean isSuccess() {
		return _isSuccess;
	}
	
	public String getFeedback() {
		return _feedback;
	}
	
	public Exception getException() {
		return _exObj;
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
	
	public void setException(Exception exObj) {
		_exObj = exObj;
	}
	
	public void setEntries(ArrayList<Entry> entries) {
		_entries = entries;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Response) {
			Response response = (Response) obj;
			
			boolean isEqual;
			
			if (this.isSuccess() == true) {
				isEqual = this._isSuccess == response._isSuccess && 
						  this._feedback.equals(response._feedback) &&
						  this.retrieveEntryDetails(_entries).equals(response.retrieveEntryDetails(_entries));
			} else {
				isEqual = this._isSuccess == response._isSuccess &&
				          this._exObj.getMessage().equals(response._exObj.getMessage());
			}
			
			return isEqual;
		}
		
		return false;
	}
	
	private String retrieveEntryDetails(ArrayList<Entry> entries) {
		String entryDetails = "";
		
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			entryDetails = entryDetails.concat(entry.toString());
		}
		
		return entryDetails;
	}
}