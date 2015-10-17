package com.taskboard.main;

import java.util.ArrayList;

public class IndexValidator {
	
	private static final int MIN_ENTRY_INDEX = 1;
	
	// constructor
	
	public IndexValidator() {
		
	}
	
	public boolean isInputIndexValid(ArrayList<Parameter> parameters, ArrayList<Entry> entries) {
		String index = "";
		
		for (int i = 0; i < parameters.size(); i++) {
			Parameter parameter = parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			if (parameterType == ParameterType.INDEX) {
				index = parameter.getParameterValue();
			}
		}
		
		int indexValue = Integer.valueOf(index);
			
		int maxEntryIndex = entries.size();
		
		if (indexValue > maxEntryIndex || indexValue < MIN_ENTRY_INDEX) {
			return false;
		}
		
		return true;
	}

}
