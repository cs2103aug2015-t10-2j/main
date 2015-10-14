package com.taskboard.main;

import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

public class Add extends Command {
	
	private static final String MESSAGE_AFTER_ADD = "\"%1$s\" added!";
	private static final String MESSAGE_ERROR_FOR_ADD = "The entry could not be added to the file.";
	private static final String MESSAGE_ERROR_FOR_INVALID_DATE_TIME = "Invalid date time provided.";
	private static final String MESSAGE_ERROR_FOR_PAST_DATE_TIME = "Past date time provided.";
	
	private static final String DATE_FORMAT = "dd/MM/yyyy'T'HH:mm";
	private static final String DEFAULT_TIME_FORMAT = "00:00";
	
	private static final int SIZE_OF_PARAMETERS_STORAGE_FOR_ADD_FLOATING = 1;
	
	private static final int INDEX_OF_TASKNAME_FOR_ADD_FLOATING = 0;
	
	public Add(CommandType commandType, ArrayList<Parameter> parameters) {
		_commandType = commandType;
		_parameters = parameters;
	}
	
	public Response executeCommand() {
		Response responseForAdd = new Response();
		
		if (isAddFloatingTask()) {
			responseForAdd = addFloatingTask();
		} else if (isAddDeadlineTask()) {
			responseForAdd = addDeadlineTask();
		} else if (isAddEvent()) {
			responseForAdd = addEvent();
		}
		
		return responseForAdd;
	}
	
	private boolean isAddFloatingTask() {
		if (_parameters.size() == SIZE_OF_PARAMETERS_STORAGE_FOR_ADD_FLOATING) {
			return true;
		}
		
		return false;
	}
	
	private Response addFloatingTask() {		
		Parameter taskNameParameter = _parameters.get(INDEX_OF_TASKNAME_FOR_ADD_FLOATING);
		String taskName = taskNameParameter.getParameterValue();
		
		Response responseForAddFloating = new Response();
		responseForAddFloating = processFloatingTaskForStorage(taskName);
		
		return responseForAddFloating;
	}
	
	private Response processFloatingTaskForStorage(String taskName) {
		Response responseForAddFloating = new Response();
		
		Entry floatingTask = formatFloatingTaskForStorage(taskName);
		
		StorageHandler storageHandler = StorageHandler.getInstance();
		
		if (storageHandler.isAddToFileSuccessful(floatingTask)) {
			setSuccessResponseForAdd(responseForAddFloating, taskName);
		} else {
			setFailureResponseForAdd(responseForAddFloating);
		}
		
		return responseForAddFloating;
	}
	
	private Entry formatFloatingTaskForStorage(String taskName) {
		String formattedTaskName = "Name: " + taskName;
		
		Entry floatingTask = new Entry();
		floatingTask.addToDetails(formattedTaskName);
		
		return floatingTask;
	}
	
	private void setSuccessResponseForAdd(Response response, String taskName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_ADD, taskName);
		response.setFeedback(userFeedback);
	}
	
	private String getFeedbackForUser(String feedbackMessage, String details) {
		return String.format(feedbackMessage, details);
	}
	
	private void setFailureResponseForAdd(Response response) {
		response.setIsSuccess(false);
		IOException exObj = new IOException(MESSAGE_ERROR_FOR_ADD);
		response.setException(exObj);
	}
	
	private boolean isAddDeadlineTask() {
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			if (parameterType == ParameterType.DATE) {
				return true;
			}
		}
		
		return false;
	}
	
	private Response addDeadlineTask() {
		Response responseForAddDeadline = new Response();
		
		String taskName = "";
		String date = "";
		String time = "";
		
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			switch (parameterType) {
				case NAME:
					taskName = parameter.getParameterValue();
					break;
				case DATE:
					date = parameter.getParameterValue();
					break;
				case TIME:
					time = parameter.getParameterValue();
					break;
			}
		}
		
		responseForAddDeadline = validateDateTimeDetailsForDeadlineTask(date, time);
		
		if (responseForAddDeadline.getIsSuccess() == true) {
			responseForAddDeadline = processDeadlineTaskForStorage(taskName, date, time);
		}
	
		return responseForAddDeadline;
	}
	
	private Response validateDateTimeDetailsForDeadlineTask(String date, String time) {
		Response responseForDateTime = new Response();
		
		String dateTime = getDateTimeFormat(date, time);
		Date inputDate = getInputDate(dateTime);
		Date todayDate = new Date();
		responseForDateTime = checkValidityOfInputDate(inputDate, todayDate);
		
		return responseForDateTime;
	}
	
	private String getDateTimeFormat(String date, String time) {
		if (time.isEmpty()) {
			time = DEFAULT_TIME_FORMAT;
		}
		
		String dateTime = date.concat("T").concat(time);
		
		return dateTime;
	}
	
	private Date getInputDate(String dateTime) {
		 try {
			 DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	         dateFormat.setLenient(false);
	         Date inputDate = dateFormat.parse(dateTime);
	         
	         return inputDate;
	     } catch (ParseException e) {
	         return null;
	     }
	}
	
	private Response checkValidityOfInputDate(Date inputDate, Date referenceDate) {
		Response responseForInputDate = new Response();
		
		if (inputDate == null) {
			setFailureResponseForInvalidDateTime(responseForInputDate);
		} else {
			
			if (inputDate.after(referenceDate)) {
				responseForInputDate.setIsSuccess(true);
			} else {
				setFailureResponseForPastDateTime(responseForInputDate);
			}
		}	
		
		return responseForInputDate;
	}
	
	private void setFailureResponseForInvalidDateTime(Response response) {
		response.setIsSuccess(false);
		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_INVALID_DATE_TIME);
		response.setException(exObj);
	}
	
	private void setFailureResponseForPastDateTime(Response response) {
		response.setIsSuccess(false);
		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_PAST_DATE_TIME);
		response.setException(exObj);
	}
	
	private Response processDeadlineTaskForStorage(String taskName, String date, String time) {
		Response responseForAddDeadline = new Response();
		
		Entry deadlineTask = formatDeadlineTaskForStorage(taskName, date, time);
		
		StorageHandler storageHandler = StorageHandler.getInstance();
		
		if (storageHandler.isAddToFileSuccessful(deadlineTask)) {
			setSuccessResponseForAdd(responseForAddDeadline, taskName);
		} else {
			setFailureResponseForAdd(responseForAddDeadline);
		}
		
		return responseForAddDeadline;
	}
	
	private Entry formatDeadlineTaskForStorage(String taskName, String date, String time) {
		String formattedTaskName = "Name: " + taskName;
		String formattedDate = "Due date: " + date;
		
		Entry deadlineTask = new Entry();
		deadlineTask.addToDetails(formattedTaskName);
		deadlineTask.addToDetails(formattedDate);
		
		if (!time.isEmpty()) {
			String formattedTime = "Due time: " + time;
			deadlineTask.addToDetails(formattedTime);
		}
		
		return deadlineTask;
	}
	
	private boolean isAddEvent() {
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			if (parameterType == ParameterType.START_DATE) {
				return true;
			}
		}
		
		return false;
	}
	
	private Response addEvent() {
		Response responseForAddEvent = new Response();
		
		String eventName = "";
		String startDate = "";
		String startTime = "";
		String endDate = "";
		String endTime = "";
		
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			switch (parameterType) {
				case NAME:
					eventName = parameter.getParameterValue();
					break;
				case START_DATE:
					startDate = parameter.getParameterValue();
					break;
				case START_TIME:
					startTime = parameter.getParameterValue();
					break;
				case END_DATE:
					endDate = parameter.getParameterValue();
					break;
				case END_TIME:
					endTime = parameter.getParameterValue();
					break;
			}
		}
		
		if (endDate.isEmpty()) {
			endDate = startDate;
		}
		
		responseForAddEvent = validateDateTimeDetailsForEvent(startDate, startTime, endDate, endTime);
		
		if (responseForAddEvent.getIsSuccess() == true) {
			responseForAddEvent = processEventForStorage(eventName, startDate, startTime, endDate, endTime);
		}
		
		return responseForAddEvent;
	}
	
	private Response validateDateTimeDetailsForEvent(String startDate, String startTime, String endDate, 
                                                     String endTime) {
		Response responseForDateTime = new Response();
		
		String startDateTime = getDateTimeFormat(startDate, startTime);
		Date inputStartDate = getInputDate(startDateTime);
		Date todayDate = new Date();
		responseForDateTime = checkValidityOfInputDate(inputStartDate, todayDate);
		
		if (responseForDateTime.getIsSuccess() == true) {			
			String endDateTime = getDateTimeFormat(endDate, endTime);
			Date inputEndDate = getInputDate(endDateTime);
			responseForDateTime = checkValidityOfInputDate(inputEndDate, inputStartDate);
		}
		
		return responseForDateTime;
	}
	
	private Response processEventForStorage(String eventName, String startDate, String startTime, 
                                            String endDate, String endTime) {
		Response responseForEvent = new Response();
		
		Entry event = formatEventForStorage(eventName, startDate, startTime, endDate, endTime);
		
		StorageHandler storageHandler = StorageHandler.getInstance();
		
		if (storageHandler.isAddToFileSuccessful(event)) {
			setSuccessResponseForAdd(responseForEvent, eventName);
		} else {
			setFailureResponseForAdd(responseForEvent);
		}
		
		return responseForEvent;
	}
	
	private Entry formatEventForStorage(String eventName, String startDate, String startTime, 
                                        String endDate, String endTime) {
		String formattedEventName = "Name: " + eventName;
		String formattedStartDate = "Start date: " + startDate;
		
		Entry event = new Entry();
		event.addToDetails(formattedEventName);
		event.addToDetails(formattedStartDate);
		
		if (!startTime.isEmpty()) {
			String formattedStartTime = "Start time: " + startTime;
			event.addToDetails(formattedStartTime);
		}
		
		String formattedEndDate = "End date: " + endDate;
		event.addToDetails(formattedEndDate);
		
		if (!endTime.isEmpty()) {
			String formattedEndTime = "End time: " + endTime;
			event.addToDetails(formattedEndTime);
		}
		
		return event;
	}
}
