//@@author A0123935E
package com.taskboard.main.validator;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.taskboard.main.logger.GlobalLogger;
import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.ParameterType;
import com.taskboard.main.util.Response;

/**
 * This class retrieves the entry index to be processed and validates it. 
 * It returns a corresponding Response upon verification.
 * @author Amarparkash Singh Mavi
 *
 */
public class IndexProcessor {
	
	// This is the feedback message to be displayed to the user
	private static final String MESSAGE_ERROR_FOR_INVALID_INDEX = "Invalid index provided.";
	
	// This is the minimum index value a Entry can have
	private static final int MIN_ENTRY_INDEX = 1;
	
	// attribute
	
	private Logger _logger;
	
	// constructor
	
	public IndexProcessor() {
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	// other functionalities
	
	/**
	 * This method retrieves the entry index to be processed from parameters and 
	 * validates it. It returns a corresponding Response upon verification.
	 * 
	 * @param parameters ArrayList of parameter objects containing the index parameter.
	 * @param entries    ArrayList of entry objects that represents the current entries registered.
	 * @return			 Response.
	 */
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