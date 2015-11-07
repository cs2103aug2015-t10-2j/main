//@@author A0123935E
package com.taskboard.main;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.taskboard.main.util.Response;

/**
 * This class processes and validates the provided date time details in deadline or event format.
 * It returns the corresponding Response upon verification.
 * @author Amarparkash Singh Mavi
 *
 */
public class DateTimeProcessor {
	
	// These are the feedback messages to be displayed to the user
	private static final String MESSAGE_ERROR_FOR_NO_VALID_DATE = "No valid date provided.";
	private static final String MESSAGE_ERROR_FOR_NO_VALID_START_DATE = "No valid start date provided.";
	private static final String MESSAGE_ERROR_FOR_NO_VALID_END_DATE_TIME = "No valid end date time provided.";
	
	// attribute
	
	private Logger _logger;
	
	// constructor
	
	public DateTimeProcessor() {
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	// other functionalities
	
	/**
	 * This method verifies if the date time details are in the accepted deadline format
	 * and returns a corresponding Response upon verification. 
	 * 
	 * @param date Date in string format. 
	 * @param time Time in string format.
	 * @return     Response.   
	 */
	public Response processDeadlineDateTimeDetails(String date, String time) {
		Response responseForDateTime = new Response();
		if (date.isEmpty()) {
			setFailureResponseForNoDate(responseForDateTime);
			_logger.log(Level.INFO, "Generated failure response for no date");
		} else {
			responseForDateTime.setIsSuccess(true);
		}
		
		return responseForDateTime;
	}
	
	private void setFailureResponseForNoDate(Response response) {
		response.setIsSuccess(false);
		response.setFeedback(MESSAGE_ERROR_FOR_NO_VALID_DATE);
	}
	
	/** 
	 * This method validates the date time details in the deadline format and returns a
	 * corresponding Response upon verification.
	 * 
	 * @param date Date in string format.
	 * @param time Time in string format.
	 * @return     Response. 
	 */
	public Response validateDeadlineDateTimeDetails(String date, String time) {
		DateTimeValidator deadlineDateTimeValidator = new DateTimeValidator();
		Date currentDate = new Date();
		Response responseForDateTime = deadlineDateTimeValidator.validateDateTimeDetails(date, time, 
				                                                                         currentDate);
		
		return responseForDateTime;
	}
	
	/**
	 * This method verifies if the date time details are in the accepted event format and
	 * returns a corresponding Response upon verification.
	 *   
	 * @param startDate Start date in string format. 
	 * @param startTime Start time in string format.
	 * @param endDate   End date in string format.
	 * @param endTime   End time in string format.
	 * @return          Response.
	 */
	public Response processEventDateTimeDetails(String startDate, String startTime, String endDate, 
			                                    String endTime) {
		Response responseForDateTime = new Response();
		if (startDate.isEmpty()) {
			setFailureResponseForNoStartDate(responseForDateTime);
			_logger.log(Level.INFO, "Generated failure response for no start date");
		} else if (endDate.isEmpty() && endTime.isEmpty()) {
			setFailureResponseForNoEndDateTime(responseForDateTime);
			_logger.log(Level.INFO, "Generated failure response for no end date time");
		} else if (endDate.isEmpty()) {
			responseForDateTime.setIsSuccess(true);
		}
		
		return responseForDateTime;
	}
	
	private void setFailureResponseForNoStartDate(Response response) {
		response.setIsSuccess(false);
		response.setFeedback(MESSAGE_ERROR_FOR_NO_VALID_START_DATE);
	}
	
	private void setFailureResponseForNoEndDateTime(Response response) {
		response.setIsSuccess(false);
		response.setFeedback(MESSAGE_ERROR_FOR_NO_VALID_END_DATE_TIME);
	}
	
	/**
	 * This method validates the date time details in the event format and returns a
	 * corresponding Response upon verification.
	 * 
	 * @param startDate Start date in string format. 
	 * @param startTime Start time in string format.
	 * @param endDate   End date in string format.
	 * @param endTime   End time in string format.
	 * @return          Response.
	 */
	public Response validateEventDateTimeDetails(String startDate, String startTime, String endDate, 
			                                     String endTime) {
		DateTimeValidator startDateTimeValidator = new DateTimeValidator();
		Date currentDate = new Date();
		Response responseForDateTime = startDateTimeValidator.validateDateTimeDetails(startDate, startTime, 
				                                                                      currentDate);
		
		if (responseForDateTime.isSuccess() == true) {
			DateTimeValidator endDateTimeValidator = new DateTimeValidator();
			Date inputStartDate = startDateTimeValidator.getDate();
			responseForDateTime = endDateTimeValidator.validateDateTimeDetails(endDate, endTime, inputStartDate);
		}
		
		return responseForDateTime;
	}
}