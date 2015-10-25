package com.taskboard.main;

import java.util.Date;

public class DateTimeProcessor {
	
	private static final String MESSAGE_ERROR_FOR_NO_DATE = "No date provided.";
	private static final String MESSAGE_ERROR_FOR_NO_START_DATE = "No start date provided.";
	private static final String MESSAGE_ERROR_FOR_NO_END_DATE_TIME = "No end date time provided.";
	
	// constructor
	
	public DateTimeProcessor() {
		
	}
	
	public Response processDeadlineDateTimeDetails(String date, String time) {
		Response responseForDateTime = new Response();
		
		if (date.isEmpty()) {
			setFailureResponseForNoDate(responseForDateTime);
		} else {
			responseForDateTime.setIsSuccess(true);
		}
		
		return responseForDateTime;
	}
	
	private void setFailureResponseForNoDate(Response response) {
		response.setIsSuccess(false);
		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_NO_DATE);
		response.setException(exObj);
	}
	
	public Response validateDeadlineDateTimeDetails(String date, String time) {
		Response responseForDateTime = new Response();
		
		DateTimeValidator dateTimeValidator = new DateTimeValidator();
		Date currentDate = new Date();
		responseForDateTime = dateTimeValidator.validateDateTimeDetails(date, time, currentDate);
		
		return responseForDateTime;
	}
	
	public Response processEventDateTimeDetails(String startDate, String startTime, String endDate, 
			                                    String endTime) {
		Response responseForDateTime = new Response();
		
		if (startDate.isEmpty()) {
			setFailureResponseForNoStartDate(responseForDateTime);
		} else if (endDate.isEmpty() && endTime.isEmpty()) {
			setFailureResponseForNoEndDateTime(responseForDateTime);
		} else if (endDate.isEmpty()) {
			responseForDateTime.setIsSuccess(true);
		}
		
		return responseForDateTime;
	}
	
	private void setFailureResponseForNoStartDate(Response response) {
		response.setIsSuccess(false);
		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_NO_START_DATE);
		response.setException(exObj);
	}
	
	private void setFailureResponseForNoEndDateTime(Response response) {
		response.setIsSuccess(false);
		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_NO_END_DATE_TIME);
		response.setException(exObj);
	}
	
	public Response validateEventDateTimeDetails(String startDate, String startTime, String endDate, 
			                                      String endTime) {
		Response responseForDateTime = new Response();
		
		DateTimeValidator startDateTimeValidator = new DateTimeValidator();
		Date currentDate = new Date();
		responseForDateTime = startDateTimeValidator.validateDateTimeDetails(startDate, startTime, currentDate);
		
		if (responseForDateTime.isSuccess() == true) {
			DateTimeValidator endDateTimeValidator = new DateTimeValidator();
			Date inputStartDate = startDateTimeValidator.getDate();
			responseForDateTime = endDateTimeValidator.validateDateTimeDetails(endDate, endTime, inputStartDate);
		}
		
		return responseForDateTime;
	}
}