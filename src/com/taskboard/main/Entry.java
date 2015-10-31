package com.taskboard.main;

import java.util.ArrayList;

public class Entry {
	
	// attribute
	
	private ArrayList<Parameter> _parameters;
	
	// constructor
	
	public Entry() {
		_parameters = new ArrayList<Parameter>();
	}
	
	public Entry(Entry oldEntry) {
		_parameters = new ArrayList<Parameter>();
		for (Parameter currentParameter: oldEntry.getParameters()) {
			_parameters.add(new Parameter(currentParameter));
		}
	}
	
	public Entry(ArrayList<Parameter> parameters) {
		_parameters = parameters;
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
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Entry) {
			Entry entry = (Entry) obj;
			
			boolean isEqual = true;
			
			for (Parameter comparedParameter: entry.getParameters()) {
				ParameterType comparedParameterType = comparedParameter.getParameterType();
				if (comparedParameterType != ParameterType.INDEX) {
					String comparedParameterValue = comparedParameter.getParameterValue();
					boolean parameterFound = false;
					for (Parameter currentParameter: _parameters) {
						ParameterType currentParameterType = currentParameter.getParameterType();
						String currentParameterValue = currentParameter.getParameterValue();
						if (currentParameterType == comparedParameterType) {
							parameterFound = true;
							if (!currentParameterValue.equals(comparedParameterValue)) {
								isEqual = false;
							}
							break;
						}
					}
					if (!parameterFound) {
						isEqual = false;
						break;
					}
				}
				if (!isEqual) {
					break;
				}
			}
			
			return isEqual;
		} else {
			return false;
		}
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
			if (!detailType.equals("INDEX") && !detailType.equals("PRIORITY")) {
				switch (detailType) {
					case "NAME":
						entryDetails += detail + "<br>";
						break;
					case "DATE":
						if (getTimeParameter() != null) {
							entryDetails += "On " + detail + " ";
						} else {
							entryDetails += "On " + detail + "<br>";
						}
						break;
					case "TIME":
						entryDetails += detail + "<br>";
						break;
					case "START_DATE":
						if (getStartTimeParameter() != null) {
							entryDetails += "From " + detail + " ";
						} else {
							entryDetails += "From " + detail + " To ";
						}
						break;
					case "START_TIME":
						entryDetails += detail + " To ";
						break;
					case "END_DATE":
						if (getEndTimeParameter() != null) {
							entryDetails += detail + " ";
						} else {
							entryDetails += detail + "<br>";
						}
						break;
					case "END_TIME":
						entryDetails += detail + "<br>";
						break;
					case "CATEGORY":
						entryDetails += "Category: " + detail + "<br>";
						break;
					default:
						entryDetails += detail + "<br>";
						break;
				}
			}
		}
		
		entryDetails += "</html>";
		
		return entryDetails;
	}
	
}
