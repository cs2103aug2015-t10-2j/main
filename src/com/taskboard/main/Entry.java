package com.taskboard.main;

import java.util.ArrayList;

public class Entry {
	
	// attribute
	
	private ArrayList<String> _details;
	private boolean _completionStatus = false;
	
	// constructor
	
	public Entry() {
		_details = new ArrayList<String>();
		_completionStatus = false;
	}
	
	// accessor
	
	public ArrayList<String> getDetails() {
		return _details;
	}
	
	public boolean getCompletionStatus() {
		return _completionStatus;
	}
	
	// mutator
	
	public void setDetails(ArrayList<String> details) {
		_details = details;
	}
	 
	public void addToDetails(String detail) {
		_details.add(detail);
	}
	
	public void setCompletionStatus(boolean trueOrFalse) {
		_completionStatus = trueOrFalse;
	}
	// overriding method
	
	public String toString() {
		String entryDetails = "";
		
		for (int i = 0; i < _details.size(); i++) {
			String detail = _details.get(i);
			entryDetails = entryDetails.concat(detail).concat("\n");
		}
		
		return entryDetails;
	}
}

