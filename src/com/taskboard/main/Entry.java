package com.taskboard.main;

import java.util.ArrayList;

public class Entry {
	
	// attribute
	
	private ArrayList<Parameter> _details;
	private boolean _completionStatus = false;
	
	// constructor
	
	public Entry() {
		_details = new ArrayList<Parameter>();
		_completionStatus = false;
	}
	
	// accessor
	
	public ArrayList<Parameter> getDetails() {
		return _details;
	}
	
	public boolean getCompletionStatus() {
		return _completionStatus;
	}
	
	// mutator
	
	public void setDetails(ArrayList<Parameter> details) {
		_details = details;
	}
	 
	public void addToDetails(Parameter detail) {
		_details.add(detail);
	}
	
	public void setCompletionStatus(boolean trueOrFalse) {
		_completionStatus = trueOrFalse;
	}
	// overriding method
	
	public String toString() {
		String entryDetails = "";
		
		for (int i = 0; i < _details.size(); i++) {
			Parameter detailParameter = _details.get(i);
			String detailType = detailParameter.getParameterType().name();
			String detail = detailParameter.getParameterValue();
			entryDetails = entryDetails.concat(detailType).concat(": ").concat(detail).concat("\n");
		}
		
		return entryDetails;
	}
}

