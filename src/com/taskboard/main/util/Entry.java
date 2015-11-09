//@@author A0126536E
package com.taskboard.main.util;

import java.util.ArrayList;

/**
 * This class is used as a container class comprising of parameters in an entry.
 * @author Alvian Prasetya
 */
public class Entry {
	
	private static final String STRING_NAME = "NAME";
	private static final String STRING_DATE = "DATE";
	private static final String STRING_TIME = "TIME";
	private static final String STRING_START_DATE = "START_DATE";
	private static final String STRING_START_TIME = "START_TIME";
	private static final String STRING_END_DATE = "END_DATE";
	private static final String STRING_END_TIME = "END_TIME";
	
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
	
	public void removeIndexParameter() {
		for (int i = 0; i < _parameters.size(); i++) {
			if (_parameters.get(i).getParameterType() == ParameterType.INDEX) {
				_parameters.remove(i);
				break;
			}
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Entry) {
			Entry entry = (Entry) obj;
			Entry currentEntry = new Entry(this);
			Entry comparedEntry = new Entry(entry);
			
			currentEntry.removeIndexParameter();
			comparedEntry.removeIndexParameter();
			if (currentEntry.toString().equals(comparedEntry.toString())) {
				return true;
			} else {
				return false;
			}
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
	
	public String toHTMLString() {
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
			entryDetails = entryDetails.concat("<b>" + detailType + "</b>").concat(": ").concat(detail).concat("<br>");
		}
		
		return entryDetails;
	}
	
	public String toUIString() {
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
			if (!detailType.equals("INDEX") && !detailType.equals("CATEGORY") && !detailType.equals("PRIORITY")) {
				switch (detailType) {
					case STRING_NAME :
						entryDetails += detail + "\n";
						break;
					case STRING_DATE :
						if (getTimeParameter() != null) {
							entryDetails += "On " + detail + " ";
						} else {
							entryDetails += "On " + detail + "\n";
						}
						break;
					case STRING_TIME :
						entryDetails += detail + "\n";
						break;
					case STRING_START_DATE :
						if (getStartTimeParameter() != null) {
							entryDetails += "From " + detail + " ";
						} else {
							entryDetails += "From " + detail + " To ";
						}
						break;
					case STRING_START_TIME :
						entryDetails += detail + " To ";
						break;
					case STRING_END_DATE:
						if (getEndTimeParameter() != null) {
							entryDetails += detail + " ";
						} else {
							entryDetails += detail + "\n";
						}
						break;
					case STRING_END_TIME :
						entryDetails += detail + "\n";
						break;
					default:
						entryDetails += detail + "\n";
						break;
				}
			}
		}
		
		return entryDetails;
	}
	
}
