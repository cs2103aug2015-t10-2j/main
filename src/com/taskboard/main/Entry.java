package com.taskboard.main;

import java.util.ArrayList;

public class Entry {
	
	// attribute
	
	private ArrayList<Parameter> _parameters;
	private boolean _isCompleted = false;
	
	// constructor
	
	public Entry() {
		_parameters = new ArrayList<Parameter>();
		_isCompleted = false;
	}
	
	// accessor
	
	public ArrayList<Parameter> getParameters() {
		return _parameters;
	}
	
	public boolean isCompleted() {
		return _isCompleted;
	}
	
	// mutator
	
	public void setDetails(ArrayList<Parameter> newParameters) {
		_parameters = newParameters;
	}
	 
	public void addToParameters(Parameter newParameter) {
		_parameters.add(newParameter);
	}
	
	public void setCompleted(boolean trueOrFalse) {
		_isCompleted = trueOrFalse;
	}
	
	public Parameter getNameParameter() {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.NAME) {
				return _parameters.get(i);
			}
		}
		return null;
	}
	
	public Parameter getDateParameter() {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.DATE) {
				return _parameters.get(i);
			}
		}
		return null;
	}
	
	public Parameter getTimeParameter() {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.TIME) {
				return _parameters.get(i);
			}
		}
		return null;
	}
	
	public Parameter getStartDateParameter() {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.START_DATE) {
				return _parameters.get(i);
			}
		}
		return null;
	}
	
	public Parameter getStartTimeParameter() {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.START_TIME) {
				return _parameters.get(i);
			}
		}
		return null;
	}
	
	public Parameter getEndDateParameter() {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.END_DATE) {
				return _parameters.get(i);
			}
		}
		return null;
	}
	
	public Parameter getEndTimeParameter() {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.END_TIME) {
				return _parameters.get(i);
			}
		}
		return null;
	}
	
	public Parameter getCategoryParameter() {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.CATEGORY) {
				return _parameters.get(i);
			}
		}
		return null;
	}
	
	public Parameter getPriorityParameter() {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.PRIORITY) {
				return _parameters.get(i);
			}
		}
		return null;
	}
	
	// overriding method
	@Override
	public String toString() {
		String entryDetails = "";
		
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter detailParameter = _parameters.get(i);
			String detailType = detailParameter.getParameterType().name();
			String detail = detailParameter.getParameterValue();
			entryDetails = entryDetails.concat(detailType).concat(": ").concat(detail).concat("\n");
		}
		
		return entryDetails;
	}
}

