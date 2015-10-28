package com.taskboard.main;

public class Parameter {
	
	// attributes
	
	private ParameterType _parameterType;
	private String _parameterValue;
	
	// constructors
	
	public Parameter() {
		
	}
	
	public Parameter(ParameterType parameterType, String parameterValue) {
		_parameterType = parameterType;
		_parameterValue = parameterValue;
	}
	
	public Parameter(Parameter oldParameter) {
		_parameterType = oldParameter.getParameterType();
		_parameterValue = oldParameter.getParameterValue();
	}
	
	// accessors
	
	public ParameterType getParameterType() {
		return _parameterType;
	}
	
	public String getParameterValue() {
		return _parameterValue;
	}
	
	// mutators
	
	public void setParameterType(ParameterType newParameterType) {
		_parameterType = newParameterType;
	}
	
	public void setParameterValue(String newParameterValue) {
		_parameterValue = newParameterValue;
	}
	
}
