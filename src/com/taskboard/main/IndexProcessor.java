package com.taskboard.main;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.ParameterType;
import com.taskboard.main.util.Response;

public class IndexProcessor {
	
	private static final String MESSAGE_ERROR_FOR_INVALID_INDEX = "Invalid index provided.";
	
	private static final int MIN_ENTRY_INDEX = 1;
	
	// attribute
	
	private Logger _logger;
	
	// constructor
	
	public IndexProcessor() {
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	public Response processInputIndex(ArrayList<Parameter> parameters, ArrayList<Entry> entries) {
		String inputIndex = "";
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getParameterType() == ParameterType.INDEX) {
				inputIndex = parameters.get(i).getParameterValue(); 
			}
		}
		
		assert !inputIndex.isEmpty();
		_logger.log(Level.INFO, "Successfully retrieved index of entry: " + inputIndex);
		Response responseForInputIndex = checkValidityOfInputIndex(inputIndex, entries);
		
		return responseForInputIndex;
	}
	
	private Response checkValidityOfInputIndex(String index, ArrayList<Entry> entries) {
		int indexValue = Integer.valueOf(index);
		int maxEntryIndex = entries.size();
		Response responseForInputIndex = new Response();
		if (indexValue > maxEntryIndex || indexValue < MIN_ENTRY_INDEX) {
			setFailureResponseForInvalidIndex(responseForInputIndex);
			_logger.log(Level.INFO, "Generated failure response for invalid index");
		} else {
			responseForInputIndex.setIsSuccess(true);
		}
		
		return responseForInputIndex;
	}
	
	private void setFailureResponseForInvalidIndex(Response response) {
		response.setIsSuccess(false);
		response.setFeedback(MESSAGE_ERROR_FOR_INVALID_INDEX);
	}
}