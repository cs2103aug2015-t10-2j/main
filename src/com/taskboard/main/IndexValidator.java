package com.taskboard.main;

import java.util.ArrayList;

public class IndexValidator {
	
	private static final String MESSAGE_ERROR_FOR_INVALID_INDEX = "Invalid index provided.";
	private static final int MIN_ENTRY_INDEX = 1;
	
	// constructor
	
	public IndexValidator() {
		
	}
	
	public Response checkValidityOfInputIndex(String index, ArrayList<Entry> entries) {
		Response responseForIndex = new Response();
		
//		for (int i = 0; i < parameters.size(); i++) {
//			Parameter parameter = parameters.get(i);
//			ParameterType parameterType = parameter.getParameterType();
//			
//			if (parameterType == ParameterType.INDEX) {
//				index = parameter.getParameterValue();
//			}
//		}
		
		int indexValue = Integer.valueOf(index);
			
		int maxEntryIndex = entries.size();
		
		if (indexValue > maxEntryIndex || indexValue < MIN_ENTRY_INDEX) {
			setFailureResponseForInvalidIndex(responseForIndex);
		} else {
			responseForIndex.setIsSuccess(true);
		}
		
		return responseForIndex;
	}
	
	private void setFailureResponseForInvalidIndex(Response response) {
		response.setIsSuccess(false);
		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_INVALID_INDEX);
		response.setException(exObj);
	}
}