package com.taskboard.main;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IndexValidator {
	
	private static final String MESSAGE_ERROR_FOR_INVALID_INDEX = "Invalid index provided.";
	private static final int MIN_ENTRY_INDEX = 1;
	
	// attribute
	
	private Logger _logger;
	
	// constructor
	
	public IndexValidator() {
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	public Response checkValidityOfInputIndex(String index, ArrayList<Entry> entries) {
		int indexValue = Integer.valueOf(index);
		int maxEntryIndex = entries.size();
		Response responseForIndex = new Response();
		if (indexValue > maxEntryIndex || indexValue < MIN_ENTRY_INDEX) {
			setFailureResponseForInvalidIndex(responseForIndex);
			_logger.log(Level.INFO, "Generated failure response for invalid index");
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