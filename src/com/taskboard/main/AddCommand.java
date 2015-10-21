package com.taskboard.main;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;

public class AddCommand extends Command {
	
	private static final String MESSAGE_AFTER_ADD = "\"%1$s\" added!";
	private static final String MESSAGE_ERROR_FOR_ADD = "The entry could not be added to the file.";
	private static final String MESSAGE_ERROR_FOR_NO_DATE = "No date provided.";
	private static final String MESSAGE_ERROR_FOR_NO_START_DATE = "No start date provided.";
	private static final String MESSAGE_ERROR_FOR_NO_END_DATE_TIME = "No end date time provided.";
	
	public AddCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
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
		if(!isAddDeadlineTask() && !isAddEvent()) {
			return true;
		}
		
		return false;
	}
	
	private boolean isAddDeadlineTask() {
		Parameter dateParameter = getDateParameter();
		Parameter timeParameter = getTimeParameter();
		
		if (dateParameter != null || timeParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private boolean isAddEvent() {
		Parameter startDateParameter = getStartDateParameter();
		Parameter startTimeParameter = getStartTimeParameter();
		
		if (startDateParameter != null || startTimeParameter != null) {
			return true;
		}
		
		return false;
	}
		
	private Response addFloatingTask() {
		Response responseForAddFloating = new Response();
		
		String taskName = getDetailFromParameter(getNameParameter());
		String priority = getDetailFromParameter(getPriorityParameter());
		String category = getDetailFromParameter(getCategoryParameter());
		
		responseForAddFloating = processFloatingTaskForStorage(taskName, priority, category);
		
		return responseForAddFloating;
	}
	
	private Response processFloatingTaskForStorage(String taskName, String priority, String category) {
		Response responseForAddFloating = new Response();
		
		Entry floatingTask = constructFloatingTaskParameters(taskName, priority, category);
		
		try {
			_tempStorageManipulator.addToTempStorage(floatingTask);
			setSuccessResponseForAdd(responseForAddFloating, taskName);
		} catch (IOException ex) {
			setFailureResponseForAdd(responseForAddFloating);
		}
			
		return responseForAddFloating;
	}
	
	private Entry constructFloatingTaskParameters(String taskName, String priority, String category) {
		Entry floatingTask = new Entry();
		
		Parameter indexParameter = new Parameter(ParameterType.INDEX, "");
		floatingTask.addToParameters(indexParameter);
		
		addParameterToEntry(floatingTask, ParameterType.NAME, taskName);
		addParameterToEntry(floatingTask, ParameterType.PRIORITY, priority);
		addParameterToEntry(floatingTask, ParameterType.CATEGORY, category);
		
		return floatingTask;
	}
	
	private void addParameterToEntry(Entry entry, ParameterType parameterType, String detail) {
		if (!detail.isEmpty()) {
			Parameter parameter = new Parameter(parameterType, detail);
			entry.addToParameters(parameter);
		}
	}
	
	private void setSuccessResponseForAdd(Response response, String entryName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_ADD, entryName);
		response.setFeedback(userFeedback);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private void setFailureResponseForAdd(Response response) {
		response.setIsSuccess(false);
		IOException exObj = new IOException(MESSAGE_ERROR_FOR_ADD);
		response.setException(exObj);
	}
			
	private Response addDeadlineTask() {
		Response responseForAddDeadline = new Response();
		
		String taskName = getDetailFromParameter(getNameParameter());
		String date = getDetailFromParameter(getDateParameter());
		String time = getDetailFromParameter(getTimeParameter());
		String priority = getDetailFromParameter(getPriorityParameter());
		String category = getDetailFromParameter(getCategoryParameter());
		
		responseForAddDeadline = processDateTimeDetailsForDeadlineTask(date, time);
		
		if (responseForAddDeadline.isSuccess() == true) {
			responseForAddDeadline = processDeadlineTaskForStorage(taskName, date, time, priority, category);
		}
	
		return responseForAddDeadline;
	}
	
	private Response processDateTimeDetailsForDeadlineTask(String date, String time) {
		Response responseForDateTime = new Response();
		
		if (date.isEmpty()) {
			setFailureResponseForNoDate(responseForDateTime);
			return responseForDateTime;
		}
		
		responseForDateTime = validateDateTimeDetailsForDeadlineTask(date, time);
		
		return responseForDateTime;
	}
	
	private void setFailureResponseForNoDate(Response response) {
		response.setIsSuccess(false);
		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_NO_DATE);
		response.setException(exObj);
	}
	
	private Response validateDateTimeDetailsForDeadlineTask(String date, String time) {
		Response responseForDateTime = new Response();
		
		DateTimeValidator dateTimeValidator = new DateTimeValidator();
		Date currentDate = new Date();
		responseForDateTime = dateTimeValidator.validateDateTimeDetails(date, time, currentDate);
		
		return responseForDateTime;
	}
		
	private Response processDeadlineTaskForStorage(String taskName, String date, String time, String priority, 
			                                       String category) {
		Response responseForAddDeadline = new Response();
		
		Entry deadlineTask = constructDeadlineTaskParameters(taskName, date, time, priority, category);
		
		try {
			_tempStorageManipulator.addToTempStorage(deadlineTask);
			setSuccessResponseForAdd(responseForAddDeadline, taskName);
		} catch (IOException ex) {
			setFailureResponseForAdd(responseForAddDeadline);
		}
		
		return responseForAddDeadline;
	}
	
	private Entry constructDeadlineTaskParameters(String taskName, String date, String time, String priority,
			                                      String category) {
		Entry deadlineTask = new Entry();
		
		Parameter indexParameter = new Parameter(ParameterType.INDEX, "");
		deadlineTask.addToParameters(indexParameter);
		
		addParameterToEntry(deadlineTask, ParameterType.NAME, taskName);
		addParameterToEntry(deadlineTask, ParameterType.DATE, date);
		addParameterToEntry(deadlineTask, ParameterType.TIME, time);
		addParameterToEntry(deadlineTask, ParameterType.PRIORITY, priority);
		addParameterToEntry(deadlineTask, ParameterType.CATEGORY, category);
	
		return deadlineTask;
	}
		
	private Response addEvent() {
		Response responseForAddEvent = new Response();
		
		String eventName = getDetailFromParameter(getNameParameter());
		String startDate = getDetailFromParameter(getStartDateParameter());
		String startTime = getDetailFromParameter(getStartTimeParameter());
		String endDate = getDetailFromParameter(getEndDateParameter());
		String endTime = getDetailFromParameter(getEndTimeParameter());
		String priority = getDetailFromParameter(getPriorityParameter());
		String category = getDetailFromParameter(getCategoryParameter());
		
		if (startDate.isEmpty()) {
			setFailureResponseForNoStartDate(responseForAddEvent);
			return responseForAddEvent;
		}
		
		if (endDate.isEmpty() && endTime.isEmpty()) {
			setFailureResponseForNoEndDateTime(responseForAddEvent);
			return responseForAddEvent;
		}
		
		if (endDate.isEmpty()) {
			endDate = startDate;
		}

		responseForAddEvent = validateDateTimeDetailsForEvent(startDate, startTime, endDate, endTime);
		
		if (responseForAddEvent.isSuccess() == true) {
			responseForAddEvent = processEventForStorage(eventName, startDate, startTime, endDate, endTime, 
					                                     priority, category);
		}
		
		return responseForAddEvent;
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
	
	private Response validateDateTimeDetailsForEvent(String startDate, String startTime, String endDate, 
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
	
	private Response processEventForStorage(String eventName, String startDate, String startTime, String endDate, 
                                            String endTime, String priority, String category) {
		Response responseForEvent = new Response();
		
		Entry event = constructEventParameters(eventName, startDate, startTime, endDate, endTime,
				                               priority, category);
		
		try {
			_tempStorageManipulator.addToTempStorage(event);
			setSuccessResponseForAdd(responseForEvent, eventName);
		} catch (IOException ex) {
			setFailureResponseForAdd(responseForEvent);
		}
		
		return responseForEvent;
	}
	
	private Entry constructEventParameters(String eventName, String startDate, String startTime, String endDate,
                                           String endTime, String priority, String category) {
		Entry event = new Entry();
		
		Parameter indexParameter = new Parameter(ParameterType.INDEX, "");
		event.addToParameters(indexParameter);
		
		addParameterToEntry(event, ParameterType.NAME, eventName);
		addParameterToEntry(event, ParameterType.START_DATE, startDate);
		addParameterToEntry(event, ParameterType.START_TIME, startTime);
		addParameterToEntry(event, ParameterType.END_DATE, endDate);
		addParameterToEntry(event, ParameterType.END_TIME, endTime);
		addParameterToEntry(event, ParameterType.PRIORITY, priority);
		addParameterToEntry(event, ParameterType.CATEGORY, category);
		
		return event;
	}
}