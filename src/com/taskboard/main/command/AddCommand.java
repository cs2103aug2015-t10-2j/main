//@@author A0123935E
package com.taskboard.main.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import com.taskboard.main.DateTimeProcessor;
import com.taskboard.main.GlobalLogger;
import com.taskboard.main.TempStorageManipulator;
import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.ParameterType;
import com.taskboard.main.util.Response;

public class AddCommand extends Command {
	
	private static final String MESSAGE_AFTER_ADD = "Entry successfully added:";
	private static final String MESSAGE_ERROR_FOR_ADD = "The entry could not be added to the file.";

	public AddCommand(ArrayList<Parameter> parameters) {
		assert parameters != null;
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
		
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	public Response executeCommand() {
		assert _parameters.size() > 0;
		_logger.log(Level.INFO, "Commenced execution of AddCommand");
		
		ArrayList<Entry> initialTempStorage = new ArrayList<Entry>();
		for (Entry entry: _tempStorageManipulator.getTempStorage()) {
			initialTempStorage.add(new Entry(entry));
		}
		
		ArrayList<Entry> initialTempArchive = new ArrayList<Entry>();
		for (Entry entry: _tempStorageManipulator.getTempArchive()) {
			initialTempArchive.add(new Entry(entry));
		}
		
		Response responseForAdd = new Response();
		if (isAddFloatingTask()) {
			_logger.log(Level.INFO, "Start process of adding floating task");
			responseForAdd = addFloatingTask();
		} else if (isAddDeadlineTask()) {
			_logger.log(Level.INFO, "Start process of adding deadline task");
			responseForAdd = addDeadlineTask();
		} else if (isAddEvent()) {
			_logger.log(Level.INFO, "Start process of adding event");
			responseForAdd = addEvent();
		}
		
		if (responseForAdd.isSuccess()) {
			_tempStorageManipulator.setLastTempStorage(initialTempStorage);
			_tempStorageManipulator.setLastTempArchive(initialTempArchive);
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
		String taskName = getDetailFromParameter(getNameParameter());
		assert !taskName.isEmpty();
		_logger.log(Level.INFO, "Successfully retrieved name of floating task: " + taskName);
		
		String priority = getDetailFromParameter(getPriorityParameter());
		String category = getDetailFromParameter(getCategoryParameter());
		Response responseForAddFloating = processFloatingTaskForStorage(taskName, priority, category);
		
		return responseForAddFloating;
	}
	
	private Response processFloatingTaskForStorage(String taskName, String priority, String category) {
		Entry floatingTask = constructFloatingTaskParameters(taskName, priority, category);
		_logger.log(Level.INFO, "Successfully created floating task");
		Response responseForAddFloating = updateNewEntryToStorage(floatingTask);
			
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
	
	private Response updateNewEntryToStorage(Entry entry) {
		Response responseForAdd = new Response();
		try {
			_tempStorageManipulator.addToTempStorage(entry);
			setSuccessResponseForAdd(responseForAdd, entry);
			_logger.log(Level.INFO, "Generated success response for adding entry");
		} catch (IOException ex) {
			setFailureResponseForAdd(responseForAdd);
			_logger.log(Level.INFO, "Generated failure response for adding entry");
		}
		
		return responseForAdd;
	}
	
	private void setSuccessResponseForAdd(Response response, Entry entry) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackforSuccessfulAdd(entry);
		response.setFeedback(userFeedback);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private String getFeedbackforSuccessfulAdd(Entry entry) {
		String feedback = MESSAGE_AFTER_ADD.concat("<br>").concat("<br>").concat(entry.toHTMLString());
		
		return feedback;
	}
	
	private void setFailureResponseForAdd(Response response) {
		response.setIsSuccess(false);
		response.setFeedback(MESSAGE_ERROR_FOR_ADD);
	}
			
	private Response addDeadlineTask() {
		String taskName = getDetailFromParameter(getNameParameter());
		assert !taskName.isEmpty();
		_logger.log(Level.INFO, "Successfully retrieved name of deadline task: " + taskName);
		
		String date = getDetailFromParameter(getDateParameter());
		String time = getDetailFromParameter(getTimeParameter());
		String priority = getDetailFromParameter(getPriorityParameter());
		String category = getDetailFromParameter(getCategoryParameter());
		
		DateTimeProcessor deadlineDateTimeProcessor = new DateTimeProcessor();
		_logger.log(Level.INFO, "Start processing date time details for deadline task");
		Response responseForAddDeadline = deadlineDateTimeProcessor.processDeadlineDateTimeDetails(date, time);
		if (responseForAddDeadline.isSuccess()) {
			_logger.log(Level.INFO, "Start validating date time details for deadline task");
			responseForAddDeadline = deadlineDateTimeProcessor.validateDeadlineDateTimeDetails(date, time);
			if (responseForAddDeadline.isSuccess()) {
				_logger.log(Level.INFO, "Start processing deadline task for storage");
				responseForAddDeadline = processDeadlineTaskForStorage(taskName, date, time, priority, category);  	                                               
			}
		}

		return responseForAddDeadline;
	}
	
	private Response processDeadlineTaskForStorage(String taskName, String date, String time,  
			                                       String priority, String category) {
		Entry deadlineTask = constructDeadlineTaskParameters(taskName, date, time, priority, category);
		_logger.log(Level.INFO, "Successfully created deadline task");
		Response responseForAddDeadline = updateNewEntryToStorage(deadlineTask);
		
		return responseForAddDeadline;
	}
	
	private Entry constructDeadlineTaskParameters(String taskName, String date, String time, 
			                                      String priority, String category) {
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
		String eventName = getDetailFromParameter(getNameParameter());
		assert !eventName.isEmpty();
		_logger.log(Level.INFO, "Successfully retrieved name of event: " + eventName);
		
		String startDate = getDetailFromParameter(getStartDateParameter());
		String startTime = getDetailFromParameter(getStartTimeParameter());
		String endDate = getDetailFromParameter(getEndDateParameter());
		String endTime = getDetailFromParameter(getEndTimeParameter());
		String priority = getDetailFromParameter(getPriorityParameter());
		String category = getDetailFromParameter(getCategoryParameter());
		
		DateTimeProcessor eventDateTimeProcessor = new DateTimeProcessor();
		_logger.log(Level.INFO, "Start processing date time details for event");
		Response responseForAddEvent = eventDateTimeProcessor.processEventDateTimeDetails(startDate, startTime, 
				                                                                          endDate, endTime);
		if (responseForAddEvent.isSuccess()) {
			_logger.log(Level.INFO, "Assign start date to end date");
			endDate = startDate;
		}
		if (responseForAddEvent.getFeedback() == null) {
			_logger.log(Level.INFO, "Start validating date time details for event");
			responseForAddEvent = eventDateTimeProcessor.validateEventDateTimeDetails(startDate, startTime, 
					                                                                  endDate, endTime);	
			if (responseForAddEvent.isSuccess()) {
				_logger.log(Level.INFO, "Start processing event for storage");
				responseForAddEvent = processEventForStorage(eventName, startDate, startTime, endDate, 
						                                     endTime, priority, category);
			}
		}

		return responseForAddEvent;
	}
		
	private Response processEventForStorage(String eventName, String startDate, String startTime,  
			                                String endDate, String endTime, String priority, String category) {
		Entry event = constructEventParameters(eventName, startDate, startTime, endDate, endTime,  
				                               priority, category);
		_logger.log(Level.INFO, "Successfully created event");
		Response responseForEvent = updateNewEntryToStorage(event);
		
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