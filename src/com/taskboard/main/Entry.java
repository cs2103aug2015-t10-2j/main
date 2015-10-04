package com.taskboard.main;

import java.util.ArrayList;

public class Entry {
	
	// attribute
	
	private ArrayList<String> _details;
	
	// constructor
	
	public Entry() {
		_details = new ArrayList<String>();
	}
	
	// accessor
	
	public ArrayList<String> getDetails() {
		return _details;
	}
	
	// mutator
	
	public void setDetails(ArrayList<String> details) {
		_details = details;
	}
	 
	public void addToDetails(String detail) {
		_details.add(detail);
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
