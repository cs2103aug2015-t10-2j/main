//@@author A0123935E
package com.taskboard.main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.taskboard.main.util.Response;

/**
 * This class validates the provided date time details and creates a corresponding Date object
 * if the details are valid. If a reference Date is provided, it goes on to verify the currency
 * of the created Date against the reference Date. It returns a corresponding Response upon
 * verification.
 * @author Amarparkash Singh Mavi
 *
 */
public class DateTimeValidator {
	
	// These are the feedback messages to be displayed to the user
	private static final String MESSAGE_ERROR_FOR_INVALID_DATE_TIME = "Invalid date time provided.";
	private static final String MESSAGE_ERROR_FOR_PAST_DATE_TIME = "Past date time provided.";
	
	// This is the default time format assigned during validation, if no time detail is provided
	private static final String FORMAT_DEFAULT_TIME = "23:59";
	
	// This is the date time format used when validating the date time details
	private static final String FORMAT_DATE_TIME = "dd/MM/yyyy'T'HH:mm";
	
	// attributes
	
	/** This attribute is the Date object created if the provided date time details are valid.*/
	private Date _inputDate;
	
	private Logger _logger;
	
	// constructor
	
	public DateTimeValidator() {
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	// accessor
	
	public Date getDate() {
		return _inputDate;
	}
	
	// other functionalities
	
	/** 
	 * This method validates the date time details provided. If the details are valid, 
	 * it creates a Date object that initialises _inputDate. If referenceDate is not null,
	 * it goes on to verify the currency of the created Date against referenceDate.
	 * It returns the corresponding Response upon verification.
	 * 
	 * @param date			Date in string format.
	 * @param time          Time in string format.
	 * @param referenceDate Date to be compared against.
	 * @return				Response.
	 */
	public Response validateDateTimeDetails(String date, String time, Date referenceDate) {
		String dateTime = getDateTimeFormat(date, time);
		_inputDate = getInputDate(dateTime);
		Response responseForDateTime = new Response();
		
		if (_inputDate == null) {
			setFailureResponseForInvalidDateTime(responseForDateTime);
			_logger.log(Level.INFO, "Generated failure response for invalid date time");
		} else if (referenceDate != null){
			responseForDateTime = checkCurrencyOfInputDate(referenceDate);
		}
		
		return responseForDateTime;
	}
	
	private String getDateTimeFormat(String date, String time) {
		if (time.isEmpty()) {
			time = FORMAT_DEFAULT_TIME;
		}
		
		String dateTime = date.concat("T").concat(time);
		
		return dateTime;
	}
	
	private Date getInputDate(String dateTime) {
		 try {
			 DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE_TIME);
	         dateFormat.setLenient(false);
	         Date inputDate = dateFormat.parse(dateTime);
 	         
	         return inputDate;
	     } catch (ParseException e) {
	         return null;
	     }
	}
	
	private void setFailureResponseForInvalidDateTime(Response response) {
		response.setIsSuccess(false);
		response.setFeedback(MESSAGE_ERROR_FOR_INVALID_DATE_TIME);
	}
	
	private Response checkCurrencyOfInputDate(Date referenceDate) {
		Response responseForInputDate = new Response();
		if (_inputDate.after(referenceDate)) {
			responseForInputDate.setIsSuccess(true);
		} else {
			setFailureResponseForPastDateTime(responseForInputDate);
			_logger.log(Level.INFO, "Generated failure response for past date time");
		}
			
		return responseForInputDate;
	}
	
	private void setFailureResponseForPastDateTime(Response response) {
		response.setIsSuccess(false);
		response.setFeedback(MESSAGE_ERROR_FOR_PAST_DATE_TIME);
	}
}