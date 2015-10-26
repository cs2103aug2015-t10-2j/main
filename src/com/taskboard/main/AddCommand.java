package com.taskboard.main;

import java.io.IOException;

import java.util.ArrayList;

public class AddCommand extends Command {
	
	private static final String MESSAGE_AFTER_ADD = "\"%1$s\" added!";
	private static final String MESSAGE_ERROR_FOR_ADD = "The entry could not be added to the file.";

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
		responseForAddFloating = updateNewEntryToStorage(floatingTask, taskName);
			
		return responseForAddFloating;
	}
		
	private Entry constructFloatingTaskParameters(String taskName, String priority, String category) {
		Entry floatingTask = new Entry();

		addParameterToEntry(floatingTask, ParameterType.INDEX, "");
		addParameterToEntry(floatingTask, ParameterType.NAME, taskName);
		addParameterToEntry(floatingTask, ParameterType.PRIORITY, priority);
		addParameterToEntry(floatingTask, ParameterType.CATEGORY, category);
		
		return floatingTask;
	}
	
	private void addParameterToEntry(Entry entry, ParameterType parameterType, String detail) {
		if (parameterType == ParameterType.INDEX || !detail.isEmpty()) {
			Parameter parameter = new Parameter(parameterType, detail);
			entry.addToParameters(parameter);
		}
	}
	
	private Response updateNewEntryToStorage(Entry entry, String entryName) {
		Response responseForAdd = new Response();
		
		try {
			_tempStorageManipulator.addToTempStorage(entry);
			setSuccessResponseForAdd(responseForAdd, entryName);
		} catch (IOException ex) {
			setFailureResponseForAdd(responseForAdd);
		}
		
		return responseForAdd;
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
		
		DateTimeProcessor deadlineDateTimeProcessor = new DateTimeProcessor();
		responseForAddDeadline = deadlineDateTimeProcessor.processDeadlineDateTimeDetails(date, time);
		
		if (responseForAddDeadline.isSuccess() == true) {
			responseForAddDeadline = deadlineDateTimeProcessor.validateDeadlineDateTimeDetails(date, time);
			
			if (responseForAddDeadline.isSuccess() == true) {
				responseForAddDeadline = processDeadlineTaskForStorage(taskName, date, time, priority, 
						                                               category);
			}
		}

		return responseForAddDeadline;
	}
	
	private Response processDeadlineTaskForStorage(String taskName, String date, String time, String priority, 
			                                       String category) {
		Response responseForAddDeadline = new Response();
		
		Entry deadlineTask = constructDeadlineTaskParameters(taskName, date, time, priority, category);
		responseForAddDeadline = updateNewEntryToStorage(deadlineTask, taskName);
		
		return responseForAddDeadline;
	}
	
	private Entry constructDeadlineTaskParameters(String taskName, String date, String time, String priority,
			                                      String category) {
		Entry deadlineTask = new Entry();
		
		addParameterToEntry(deadlineTask, ParameterType.INDEX, "");
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
		
		DateTimeProcessor eventDateTimeProcessor = new DateTimeProcessor();
		responseForAddEvent = eventDateTimeProcessor.processEventDateTimeDetails(startDate, startTime, 
				                                                                 endDate, endTime);
		
		if (responseForAddEvent.isSuccess() == true) {
			endDate = startDate;
		}
		
		if (responseForAddEvent.getException() == null) {
			responseForAddEvent = eventDateTimeProcessor.validateEventDateTimeDetails(startDate, startTime, 
					                                                                  endDate, endTime);
			
			if (responseForAddEvent.isSuccess() == true) {
				responseForAddEvent = processEventForStorage(eventName, startDate, startTime, endDate, 
						                                     endTime, priority, category);
			}
		}

		return responseForAddEvent;
	}
		
	private Response processEventForStorage(String eventName, String startDate, String startTime,  
			                                String endDate, String endTime, String priority, String category) {
		Response responseForEvent = new Response();
		
		Entry event = constructEventParameters(eventName, startDate, startTime, endDate, endTime,
				                               priority, category);
		responseForEvent = updateNewEntryToStorage(event, eventName);
		
		return responseForEvent;
	}
	
	private Entry constructEventParameters(String eventName, String startDate, String startTime, String endDate,
                                           String endTime, String priority, String category) {
		Entry event = new Entry();
			
		addParameterToEntry(event, ParameterType.INDEX, "");
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