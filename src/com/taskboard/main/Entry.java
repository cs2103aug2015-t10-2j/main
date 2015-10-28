package com.taskboard.main;

import java.util.ArrayList;

public class Entry {
	
	// attribute
	
	private ArrayList<Parameter> _parameters;
	private boolean _isCompleted;
	
	// constructor
	
	public Entry() {
		_parameters = new ArrayList<Parameter>();
		_isCompleted = false;
	}
	
	// accessors
	
	public ArrayList<Parameter> getParameters() {
		return _parameters;
	}
	
	public Parameter getIndexParameter() {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.INDEX) {
				return _parameters.get(i);
			}
		}
		
		return null;
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
	
	public Parameter getPriorityParameter() {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.PRIORITY) {
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
	
	public boolean isCompleted() {
		return _isCompleted;
	}
	
	// mutators
	
	public void setParameters(ArrayList<Parameter> newParameters) {
		_parameters = newParameters;
	}
	
	public void addToParameters(Parameter newParameter) {
		_parameters.add(newParameter);
	}
	
	public void setIndexParameter(Parameter newIndexParameter) {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.INDEX) {
				_parameters.set(i, newIndexParameter);
			}
		}
	}
	
	public void setNameParameter(Parameter newNameParameter) {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.NAME) {
				_parameters.set(i, newNameParameter);
			}
		}
	}

	public void setDateParameter(Parameter newDateParameter) {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.DATE) {
				_parameters.set(i, newDateParameter);
			}
		}
	}
	
	public void setTimeParameter(Parameter newTimeParameter) {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.TIME) {
				_parameters.set(i, newTimeParameter);
			}
		}
	}
	
	public void setStartDateParameter(Parameter newStartDateParameter) {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.START_DATE) {
				_parameters.set(i, newStartDateParameter);
			}
		}
	}
	
	public void setStartTimeParameter(Parameter newStartTimeParameter) {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.START_TIME) {
				_parameters.set(i, newStartTimeParameter);
			}
		}
	}
	
	public void setEndDateParameter(Parameter newEndDateParameter) {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.END_DATE) {
				_parameters.set(i, newEndDateParameter);
			}
		}
	}
	
	public void setEndTimeParameter(Parameter newEndTimeParameter) {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.END_TIME) {
				_parameters.set(i, newEndTimeParameter);
			}
		}
	}
	
	public void setCategoryParameter(Parameter newCategoryParameter) {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.CATEGORY) {
				_parameters.set(i, newCategoryParameter);
			}
		}
	}
	
	public void setPriorityParameter(Parameter newPriorityParameter) {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.PRIORITY) {
				_parameters.set(i, newPriorityParameter);
			}
		}
	}
	
	public void setCompleted(boolean newIsCompleted) {
		_isCompleted = newIsCompleted;
	}
	
	@Override
	public String toString() {
		String entryDetails = "";
		
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter detailParameter = _parameters.get(i);
			String detailType;
			if (detailParameter.getParameterType() != null) {
				detailType = detailParameter.getParameterType().name();
			} else {
				detailType = "";
			}
			String detail = detailParameter.getParameterValue();
			entryDetails = entryDetails.concat(detailType).concat(": ").concat(detail).concat("\n");
		}
		
		return entryDetails;
	}
	
	public String toUIString() {
		String entryDetails = "<html>";
		
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter detailParameter = _parameters.get(i);
			String detailType;
			if (detailParameter.getParameterType() != null) {
				detailType = detailParameter.getParameterType().name();
			} else {
				detailType = "";
			}
			String detail = detailParameter.getParameterValue();
			entryDetails += detailType + ": " + detail + "<br>";
		}
		
		entryDetails += "</html>";
		
		return entryDetails;
	}
	
}
