//@@author A0123935E
package com.taskboard.main.util;

import java.util.ArrayList;

/**
 * This class is used as a container class comprising of a boolean denoting 
 * whether the operation is successful, a feedback String of the operation, and 
 * a collection of entries denoting the entries after the operation is done.
 * @author Alvian Prasetya
 */
public class Response {
	
	// attributes
	
	private boolean _isSuccess;
	private String _feedback;
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
						  this.retrieveEntryDetails(this._entries).equals(response.retrieveEntryDetails(response._entries));
			} else {
				isEqual = this._isSuccess == response._isSuccess &&
						  this._feedback.equals(response._feedback);
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