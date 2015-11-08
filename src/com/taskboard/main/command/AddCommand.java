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

/**
 * This class inherits from the Command class and executes the Add command.
 * It returns a corresponding Response that denotes the success of the operation.
 * @author Amarparkash Singh Mavi
 *
 */
public class AddCommand extends Command {
	
	// These are the feedback messages to be displayed to the user
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
		// _parameters should minimally have the name of entry for Add command to be valid
		assert _parameters.size() > 0;
		_logger.log(Level.INFO, "Commence execution of AddCommand");
		
		// facilitates the Undo command 
		ArrayList<Entry> initialTempStorage = new ArrayList<Entry>();
		for (Entry entry: _tempStorageManipulator.getTempStorage()) {
			initialTempStorage.add(new Entry(entry));
		}
		
		// facilitates the Undo command 
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
		
		// facilitates the Undo command 
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
		
		_logger.log(Level.INFO, "Start processing floating task for storage");
		Response responseForAddFloating = processFloatingTaskForStorage();
		
		return responseForAddFloating;
	}
	
	private Response processFloatingTaskForStorage() {
		Entry floatingTask = createFloatingTask();
		_logger.log(Level.INFO, "Successfully created floating task");
		Response responseForAddFloating = updateNewEntryToStorage(floatingTask);
			
		return responseForAddFloating;
	}
		
	private Entry createFloatingTask() {
		Entry floatingTask = new Entry();
		addParameterToEntry(floatingTask, new Parameter(ParameterType.INDEX, ""));
		addParameterToEntry(floatingTask, getNameParameter());
		addParameterToEntry(floatingTask, getPriorityParameter());
		addParameterToEntry(floatingTask, getCategoryParameter());
			
		return floatingTask;
	}
	
	private void addParameterToEntry(Entry entry, Parameter parameter) {
		if (parameter != null) {
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
		
		DateTimeProcessor deadlineDateTimeProcessor = new DateTimeProcessor();
		_logger.log(Level.INFO, "Start processing date time details for deadline task");
		Response responseForAddDeadline = deadlineDateTimeProcessor.processDeadlineDateTimeDetails(date, time);
		
		if (responseForAddDeadline.isSuccess()) {
			_logger.log(Level.INFO, "Start validating date time details for deadline task");
			responseForAddDeadline = deadlineDateTimeProcessor.validateDeadlineDateTimeDetails(date, time);
			if (responseForAddDeadline.isSuccess()) {
				_logger.log(Level.INFO, "Start processing deadline task for storage");
				responseForAddDeadline = processDeadlineTaskForStorage();  	                                               
			}
		}

		return responseForAddDeadline;
	}
	
	private Response processDeadlineTaskForStorage() {
		Entry deadlineTask = createDeadlineTask();
		_logger.log(Level.INFO, "Successfully created deadline task");
		Response responseForAddDeadline = updateNewEntryToStorage(deadlineTask);
		
		return responseForAddDeadline;
	}
	
	private Entry createDeadlineTask() {
		Entry deadlineTask = new Entry();		
		addParameterToEntry(deadlineTask, new Parameter(ParameterType.INDEX, ""));
		addParameterToEntry(deadlineTask, getNameParameter());
		addParameterToEntry(deadlineTask, getDateParameter());
		addParameterToEntry(deadlineTask, getTimeParameter());
		addParameterToEntry(deadlineTask, getPriorityParameter());
		addParameterToEntry(deadlineTask, getCategoryParameter());

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

		DateTimeProcessor eventDateTimeProcessor = new DateTimeProcessor();
		_logger.log(Level.INFO, "Start processing date time details for event");
		Response responseForAddEvent = eventDateTimeProcessor.processEventDateTimeDetails(startDate, startTime, 
				                                                                          endDate, endTime);
		if (responseForAddEvent.isSuccess()) {
			_logger.log(Level.INFO, "Assign start date to end date");
			endDate = startDate;
			_parameters.add(new Parameter(ParameterType.END_DATE, startDate));
		}
		
		// This implies date time details are in accepted event format and can proceed for validation
		if (responseForAddEvent.getFeedback() == null) {
			_logger.log(Level.INFO, "Start validating date time details for event");
			responseForAddEvent = eventDateTimeProcessor.validateEventDateTimeDetails(startDate, startTime, 
					                                                                  endDate, endTime);
			if (responseForAddEvent.isSuccess()) {
				_logger.log(Level.INFO, "Start processing event for storage");
				responseForAddEvent = processEventForStorage();
			}
		}

		return responseForAddEvent;
	}
		
	private Response processEventForStorage() {
		Entry event = createEvent();
		_logger.log(Level.INFO, "Successfully created event");
		Response responseForEvent = updateNewEntryToStorage(event);
		
		return responseForEvent;
	}
	
	private Entry createEvent() {
		Entry event = new Entry();
		addParameterToEntry(event, new Parameter(ParameterType.INDEX, ""));
		addParameterToEntry(event, getNameParameter());
		addParameterToEntry(event, getStartDateParameter());
		addParameterToEntry(event, getStartTimeParameter());
		addParameterToEntry(event, getEndDateParameter());
		addParameterToEntry(event, getEndTimeParameter());
		addParameterToEntry(event, getPriorityParameter());
		addParameterToEntry(event, getCategoryParameter());
		
		return event;
	}
}