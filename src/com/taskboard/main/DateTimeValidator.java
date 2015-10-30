package com.taskboard.main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateTimeValidator {
	
	private static final String MESSAGE_ERROR_FOR_INVALID_DATE_TIME = "Invalid date time provided.";
	private static final String MESSAGE_ERROR_FOR_PAST_DATE_TIME = "Past date time provided.";
	
	private static final String FORMAT_DEFAULT_TIME = "23:59";
	private static final String FORMAT_DATE_TIME = "dd/MM/yyyy'T'HH:mm";
	
	// attribute
	
	private Date _inputDate;
	private Logger _logger;
	
	// constructor
	
	public DateTimeValidator() {
		
	}
	
	// accessor
	
	public Date getDate() {
		return _inputDate;
	}
	
	public Response validateDateTimeDetails(String date, String time, Date referenceDate) {
		Response responseForDateTime = new Response();
		
		String dateTime = getDateTimeFormat(date, time);
		_inputDate = getInputDate(dateTime);
		if (_inputDate == null) {
			setFailureResponseForInvalidDateTime(responseForDateTime);
			_logger.log(Level.INFO, "Generated failure response for invalid date time");
		} else if (referenceDate != null){
			responseForDateTime = checkValidityOfInputDate(referenceDate);
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
		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_INVALID_DATE_TIME);
		response.setException(exObj);
	}
	
	private Response checkValidityOfInputDate(Date referenceDate) {
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
		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_PAST_DATE_TIME);
		response.setException(exObj);
	}
}