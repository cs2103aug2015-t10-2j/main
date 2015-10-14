package com.taskboard.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.Date;

import java.io.IOException;

public class Logic {
	
//	private static final String MESSAGE_WELCOME = "Welcome to TASKBOARD!"; 
//	private static final String MESSAGE_AFTER_ADD = "\"%1$s\" added!";
//	private static final String MESSAGE_AFTER_DELETE = "\"%1$s\" deleted!";
//	private static final String MESSAGE_ERROR_FOR_ADD = "The entry could not be added to the file.";
//	private static final String MESSAGE_ERROR_FOR_LAUNCH = "Failed to create new scheduler.";
//	private static final String MESSAGE_EMPTY_FILE = "There are no registered entries.";
//	private static final String MESSAGE_ERROR_INVALID_DATE_TIME = "Invalid date time provided.";
//	private static final String MESSAGE_ERROR_PAST_DATE_TIME = "Past date time provided.";
//	private static final String MESSAGE_AFTER_EDIT = "\"%1$s\" updated!";
//	private static final String MESSAGE_ERROR_FOR_EDIT = "The entry could not be edited.";
//	private static final String MESSAGE_ERROR_FOR_DELETE = "The entry could not be deleted from the file.";
//	private static final String MESSAGE_ERROR_INVALID_COMMAND = "Invalid command type provided.";
	
//	private static final String DATE_FORMAT = "dd/MM/yyyy'T'HH:mm";
//	
//	private static final String DEFAULT_TIME_FORMAT = "00:00";
	
//	private static final int INDEX_OF_FILENAME_FOR_LAUNCH = 0;
//	private static final int INDEX_OF_TASKNAME_FOR_ADD_FLOATING = 0;
	
//	private static final int SIZE_OF_PARAMETERS_STORAGE_FOR_ADD_FLOATING = 1;	
	
//	private static final int INDEX_OF_FIRST_CHAR_IN_TIME = 0;
//	private static final int SIZE_OF_PARAMETERS_STORAGE_FOR_EDIT_FLOATING = 2;
	
	// attributes
	
	private CommandParser _commandParser;
//	private StorageHandler _storageHandler;
	
	// constructor
	
	public Logic() {
		_commandParser = new CommandParser();
	}
		
	public Response processCommand(String userInput) {
		Command commandInput = _commandParser.parseCommand(userInput);
//		CommandType commandType = commandInput.getCommandType();
		
		Response responseForOperations = new Response();
		responseForOperations = commandInput.executeCommand();
		
//		switch (commandType) {
//			case NEW: case OPEN:
//				responseForOperations = executeLaunchCommand(commandInput);
//				break;
//			case ADD:
//				responseForOperations = executeAddCommand(commandInput);
//				break;
//			case VIEW:
//				responseForOperations = executeViewCommand();
//				break;
//			case EDIT:
//				responseForOperations = executeEditCommand(commandInput);
//				break;
//			case DELETE:
//				responseForOperations = executeDeleteCommand(commandInput);
//				break;
//			case EXIT:
//				System.exit(0);
//			case UNKNOWN:
//				responseForOperations = failToRecogniseCommand();
//				break;
//		}
		
		return responseForOperations; 
	}
	
//	private Response executeLaunchCommand(Command commandInput) {
//		ArrayList<Parameter> parameters = commandInput.getParameters();
//		Parameter fileNameParameter = parameters.get(INDEX_OF_FILENAME_FOR_LAUNCH); 
//		String fileName = fileNameParameter.getParameterValue();
//		
//		return getResponseForLaunch(fileName);
//	}
	
//	private Response getResponseForLaunch(String fileName) {
//		_storageHandler = new StorageHandler();
//		
//		Response responseForLaunch = new Response();
//		
//		if (_storageHandler.isSetUpSuccessful(fileName)) {
//			setSuccessResponseForLaunch(responseForLaunch, fileName);
//		} else {
//			setFailureResponseForLaunch(responseForLaunch);
//		}	
//		
//		return responseForLaunch;
//	}
	
//	private void setSuccessResponseForLaunch(Response response, String fileName) {
//		response.setIsSuccess(true);
//		String userFeedback = getFeedbackForUser(MESSAGE_WELCOME, fileName);
//		response.setFeedback(userFeedback);
//	}
//	
//	private String getFeedbackForUser(String feedbackMessage, String details) {
//		return String.format(feedbackMessage, details);
//	}
//	
//	private void setFailureResponseForLaunch(Response response) {
//		response.setIsSuccess(false);
//		IOException exObj = new IOException(MESSAGE_ERROR_FOR_LAUNCH);
//		response.setException(exObj);
//	}
	
//	private Response executeAddCommand(Command commandInput) {
//		ArrayList<Parameter> parameters = commandInput.getParameters();
//		
//		Response responseForAdd = new Response();
//		
//		if (isAddFloatingTask(parameters)) {
//			responseForAdd = addFloatingTask(parameters);
//		} else if (isAddDeadlineTask(parameters)) {
//			responseForAdd = addDeadlineTask(parameters);
//		} else if (isAddEvent(parameters)) {
//			responseForAdd = addEvent(parameters);
//		}
//		
//		return responseForAdd;
//	}

//	private boolean isAddFloatingTask (ArrayList<Parameter> parameters) {
//		if (parameters.size() == SIZE_OF_PARAMETERS_STORAGE_FOR_ADD_FLOATING) {
//			return true;
//		}
//		
//		return false;
//	}
		
//	private Response addFloatingTask(ArrayList<Parameter> parameters) {		
//		Parameter taskNameParameter = parameters.get(INDEX_OF_TASKNAME_FOR_ADD_FLOATING);
//		String taskName = taskNameParameter.getParameterValue();
//		
//		Response responseForAddFloating = new Response();
//		responseForAddFloating = processFloatingTaskForStorage(taskName);
//		
//		return responseForAddFloating;
//	}
	
//	private Response processFloatingTaskForStorage(String taskName) {
//		Response responseForAddFloating = new Response();
//		
//		Entry floatingTask = formatFloatingTaskForStorage(taskName);
//		
//		if (_storageHandler.isAddToFileSuccessful(floatingTask)) {
//			setSuccessResponseForAdd(responseForAddFloating, taskName);
//		} else {
//			setFailureResponseForAdd(responseForAddFloating);
//		}
//		
//		return responseForAddFloating;
//	}
	
//	private Entry formatFloatingTaskForStorage(String taskName) {
//		String formattedTaskName = "Name: " + taskName;
//		
//		Entry floatingTask = new Entry();
//		floatingTask.addToDetails(formattedTaskName);
//		//floatingTask.addToDetails("\n");
//		
//		return floatingTask;
//	}
	
//	private void setSuccessResponseForAdd(Response response, String taskName) {
//		response.setIsSuccess(true);
//		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_ADD, taskName);
//		response.setFeedback(userFeedback);
//	}
//	
//	private void setFailureResponseForAdd(Response response) {
//		response.setIsSuccess(false);
//		IOException exObj = new IOException(MESSAGE_ERROR_FOR_ADD);
//		response.setException(exObj);
//	}
		
//	private boolean isAddDeadlineTask(ArrayList<Parameter> parameters) {
//		for (int i = 0; i < parameters.size(); i++) {
//			Parameter parameter = parameters.get(i);
//			ParameterType parameterType = parameter.getParameterType();
//			
//			if (parameterType == ParameterType.DATE) {
//				return true;
//			}
//		}
//		
//		return false;
//	}
	
//	private Response addDeadlineTask(ArrayList<Parameter> parameters) {
//		Response responseForAddDeadline = new Response();
//		
//		String taskName = "";
//		String date = "";
//		String time = "";
//		
//		for (int i = 0; i < parameters.size(); i++) {
//			Parameter parameter = parameters.get(i);
//			ParameterType parameterType = parameter.getParameterType();
//			
//			switch (parameterType) {
//				case NAME:
//					taskName = parameter.getParameterValue();
//					break;
//				case DATE:
//					date = parameter.getParameterValue();
//					break;
//				case TIME:
//					time = parameter.getParameterValue();
//					break;
//			}
//		}
//		
//		responseForAddDeadline = validateDateTimeDetailsForDeadlineTask(date, time);
//		
//		if (responseForAddDeadline.getIsSuccess() == true) {
//			responseForAddDeadline = processDeadlineTaskForStorage(taskName, date, time);
//		}
//	
//		return responseForAddDeadline;
//	}
	
//	private Response validateDateTimeDetailsForDeadlineTask(String date, String time) {
//		Response responseForDateTime = new Response();
//		
//		String dateTime = getDateTimeFormat(date, time);
//		Date inputDate = getInputDate(dateTime);
//		Date todayDate = new Date();
//		responseForDateTime = checkValidityOfInputDate(inputDate, todayDate);
//		
//		return responseForDateTime;
//	}

//	private String getDateTimeFormat(String date, String time) {
//		if (time.isEmpty()) {
//			time = DEFAULT_TIME_FORMAT;
//		}
//		
//		String dateTime = date.concat("T").concat(time);
//		
//		return dateTime;
//	}
	
//	private Date getInputDate(String dateTime) {
//		 try {
//			 DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
//	         dateFormat.setLenient(false);
//	         Date inputDate = dateFormat.parse(dateTime);
//	         
//	         return inputDate;
//	     } catch (ParseException e) {
//	         return null;
//	     }
//	}
	
//	private boolean isTimeValid(String time) {
//		int indexOfColon = time.indexOf(':');
//		String hourPart = time.substring(INDEX_OF_FIRST_CHAR_IN_TIME, indexOfColon);
//		int hour = Integer.parseInt(hourPart);
//		String minutePart = time.substring(indexOfColon + 1);
//		int minute = Integer.parseInt(minutePart);
//				
//		boolean isHourValid = true;
//		boolean isMinuteValid = true;
//				
//		if (hour < 0 || hour > 23) {
//			isHourValid = false;
//		}
//				
//		if (minute < 0 || minute > 59) {
//			isMinuteValid = false;
//		}
//				
//		if (!isHourValid || !isMinuteValid) {
//			return false;
//		}
//		
//		return true;
//	}
	
//	private Response checkValidityOfInputDate(Date inputDate, Date referenceDate) {
//		Response responseForInputDate = new Response();
//		
//		if (inputDate == null) {
//			setFailureResponseForInvalidDateTime(responseForInputDate);
//		} else {
//			
//			if (inputDate.after(referenceDate)) {
//				responseForInputDate.setIsSuccess(true);
//			} else {
//				setFailureResponseForPastDateTime(responseForInputDate);
//			}
//		}	
//		
//		return responseForInputDate;
//	}
	
//	private void setFailureResponseForInvalidDateTime(Response response) {
//		response.setIsSuccess(false);
//		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_INVALID_DATE_TIME);
//		response.setException(exObj);
//	}
//	
//	private void setFailureResponseForPastDateTime(Response response) {
//		response.setIsSuccess(false);
//		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_PAST_DATE_TIME);
//		response.setException(exObj);
//	}
	
//	private Response processDeadlineTaskForStorage(String taskName, String date, String time) {
//		Response responseForAddDeadline = new Response();
//		
//		Entry deadlineTask = formatDeadlineTaskForStorage(taskName, date, time);
//		
//		if (_storageHandler.isAddToFileSuccessful(deadlineTask)) {
//			setSuccessResponseForAdd(responseForAddDeadline, taskName);
//		} else {
//			setFailureResponseForAdd(responseForAddDeadline);
//		}
//		
//		return responseForAddDeadline;
//	}
	
//	private Entry formatDeadlineTaskForStorage(String taskName, String date, String time) {
//		String formattedTaskName = "Name: " + taskName;
//		String formattedDate = "Due date: " + date;
//		
//		Entry deadlineTask = new Entry();
//		deadlineTask.addToDetails(formattedTaskName);
//		deadlineTask.addToDetails(formattedDate);
//		
//		if (!time.isEmpty()) {
//			String formattedTime = "Due time: " + time;
//			deadlineTask.addToDetails(formattedTime);
//		}
//		
//		return deadlineTask;
//	}
	
//	private boolean isAddEvent(ArrayList<Parameter> parameters) {
//		for (int i = 0; i < parameters.size(); i++) {
//			Parameter parameter = parameters.get(i);
//			ParameterType parameterType = parameter.getParameterType();
//			
//			if (parameterType == ParameterType.START_DATE) {
//				return true;
//			}
//		}
//		
//		return false;
//	}
	
//	private Response addEvent(ArrayList<Parameter> parameters) {
//		Response responseForAddEvent = new Response();
//		
//		String eventName = "";
//		String startDate = "";
//		String startTime = "";
//		String endDate = "";
//		String endTime = "";
//		
//		for (int i = 0; i < parameters.size(); i++) {
//			Parameter parameter = parameters.get(i);
//			ParameterType parameterType = parameter.getParameterType();
//			
//			switch (parameterType) {
//				case NAME:
//					eventName = parameter.getParameterValue();
//					break;
//				case START_DATE:
//					startDate = parameter.getParameterValue();
//					break;
//				case START_TIME:
//					startTime = parameter.getParameterValue();
//					break;
//				case END_DATE:
//					endDate = parameter.getParameterValue();
//					break;
//				case END_TIME:
//					endTime = parameter.getParameterValue();
//					break;
//			}
//		}
//		
//		if (endDate.isEmpty()) {
//			endDate = startDate;
//		}
//		
//		responseForAddEvent = validateDateTimeDetailsForEvent(startDate, startTime, endDate, endTime);
//		
//		if (responseForAddEvent.getIsSuccess() == true) {
//			responseForAddEvent = processEventForStorage(eventName, startDate, startTime, endDate, endTime);
//		}
//		
//		return responseForAddEvent;
//	}
	
	
//	private Response validateDateTimeDetailsForEvent(String startDate, String startTime, String endDate, 
//			                                         String endTime) {
//		Response responseForDateTime = new Response();
//		
//		String startDateTime = getDateTimeFormat(startDate, startTime);
//		Date inputStartDate = getInputDate(startDateTime);
//		Date todayDate = new Date();
//		responseForDateTime = checkValidityOfInputDate(inputStartDate, todayDate);
//		
//		if (responseForDateTime.getIsSuccess() == true) {			
//			String endDateTime = getDateTimeFormat(endDate, endTime);
//			Date inputEndDate = getInputDate(endDateTime);
//			responseForDateTime = checkValidityOfInputDate(inputEndDate, inputStartDate);
//		}
//		
//		return responseForDateTime;
//	}

//	private Response processEventForStorage(String eventName, String startDate, String startTime, 
//			                                String endDate, String endTime) {
//		Response responseForEvent = new Response();
//		
//		Entry event = formatEventForStorage(eventName, startDate, startTime, endDate, endTime);
//		
//		if (_storageHandler.isAddToFileSuccessful(event)) {
//			setSuccessResponseForAdd(responseForEvent, eventName);
//		} else {
//			setFailureResponseForAdd(responseForEvent);
//		}
//		
//		return responseForEvent;
//	}

//	private Entry formatEventForStorage(String eventName, String startDate, String startTime, 
//			                            String endDate, String endTime) {
//		String formattedEventName = "Name: " + eventName;
//		String formattedStartDate = "Start date: " + startDate;
//		
//		Entry event = new Entry();
//		event.addToDetails(formattedEventName);
//		event.addToDetails(formattedStartDate);
//		
//		if (!startTime.isEmpty()) {
//			String formattedStartTime = "Start time: " + startTime;
//			event.addToDetails(formattedStartTime);
//		}
//		
//		String formattedEndDate = "End date: " + endDate;
//		event.addToDetails(formattedEndDate);
//		
//		if (!endTime.isEmpty()) {
//			String formattedEndTime = "End time: " + endTime;
//			event.addToDetails(formattedEndTime);
//		}
//		
//		return event;
//	}

//	private Response executeViewCommand() {
//		Response responseForView = new Response();
//		
//		responseForView.setIsSuccess(true);
//		String userFeedback = retrieveEntries();
//		
//		if (userFeedback.isEmpty()) {
//			userFeedback = MESSAGE_EMPTY_FILE;
//		}
//		
//		responseForView.setFeedback(userFeedback);
//		
//		return responseForView;
//	}
	
//	private String retrieveEntries() {
//		String entriesList = _storageHandler.retrieveEntriesInFile();
//		
//		return entriesList;
//	}
	
//	private Response executeEditCommand(Command commandInput) {
//		ArrayList<Parameter> parameters = commandInput.getParameters();
//		
//		Response responseForEdit = new Response();
//		responseForEdit = processEditedDetailsForStorage(parameters);
//		
//		return responseForEdit;
//	}

//	private Response processEditedDetailsForStorage(ArrayList<Parameter> parameters) {
//		ArrayList<String> editedDetails = new ArrayList<String>();
//		Response responseForEdit = new Response();
//		
//		String taskName = "";
//		String editedTaskName = "";
//		String editedDueDate = "";
//		String editedDueTime = "";
//		
//		for (int i = 0; i < parameters.size(); i++) {
//			Parameter parameter = parameters.get(i);
//			ParameterType parameterType = parameter.getParameterType();
//			
//			switch (parameterType) {
//				case NAME:
//					taskName = parameter.getParameterValue();
//					editedDetails.add(taskName);
//					break;
//				case NEW_NAME:
//					editedTaskName = parameter.getParameterValue();
//					String formattedName = "Name: " + editedTaskName;
//					editedDetails.add(formattedName);
//					break;
//				case DATE:
//					editedDueDate = parameter.getParameterValue();
//					break;
//				case TIME:
//					editedDueTime = parameter.getParameterValue();
//					break;
//			}			
//		}
//		
//		String detailType = "";
//		boolean isEditDueDateTime = true;
//		
//		if (editedDueDate.isEmpty() && editedDueTime.isEmpty()) {
//			isEditDueDateTime = false;
//		}
//		
//		if (isEditDueDateTime) {
//			if (editedDueDate.isEmpty()) {
//				detailType = "Due date:";
//				editedDueDate = _storageHandler.retrieveDetail(taskName, detailType);	
//			} else if (editedDueTime.isEmpty()) {
//				detailType = "Due time:";
//				editedDueTime = _storageHandler.retrieveDetail(taskName, detailType);
//			}
//			
//			responseForEdit = formatEditedDateTimeDetails(editedDetails, editedDueDate, editedDueTime);
//			
//			if (responseForEdit.getIsSuccess() == false) {
//				return responseForEdit;
//			}
//		}
//		
//		if (_storageHandler.isEditInFileSuccessful(editedDetails)) {
//			setSuccessResponseForEdit(responseForEdit, taskName);
//		} else {
//			setFailureResponseForEdit(responseForEdit);
//		}
//		
//		return responseForEdit;
//	}
		
//	private Response formatEditedDateTimeDetails(ArrayList<String> editedDetails, String date, String time) {
//		Response responseForDateTime = new Response();
//		
//		String dateTime = getDateTimeFormat(date, time);
//		Date inputDate = getInputDate(dateTime);
//		
//		if (inputDate == null) {
//			setFailureResponseForInvalidDateTime(responseForDateTime);
//		}
//		else {
//			Date todayDate = new Date();
//			
//			if (inputDate.after(todayDate)) {
//				responseForDateTime.setIsSuccess(true);
//				
//				String formattedDate = "Due date: " + date;
//				editedDetails.add(formattedDate);
//				
//				String formattedTime = "Due time: " + time;
//				editedDetails.add(formattedTime);
//			} else {
//				setFailureResponseForPastDateTime(responseForDateTime);
//			}
//		}
//		
//		return responseForDateTime;
//	}
		
//	private void setSuccessResponseForEdit(Response response, String taskName) {
//		response.setIsSuccess(true);
//		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_EDIT, taskName);
//		response.setFeedback(userFeedback);
//	}
//	
//	private void setFailureResponseForEdit(Response response) {
//		response.setIsSuccess(false);
//		IOException exobj = new IOException(MESSAGE_ERROR_FOR_EDIT);
//		response.setException(exobj);
//	}
//	
//	private boolean isEditFloatingTask(ArrayList<Parameter> parameters) {
//		if (parameters.size() == SIZE_OF_PARAMETERS_STORAGE_FOR_EDIT_FLOATING) {
//			return true;
//		}
//		
//		return false;
//	}
	
//	private Response EditFloatingTask(ArrayList<Parameter> parameters) {
//		Parameter taskNameParameter = parameters.get(INDEX_OF_TASKNAME_FOR_ADD_FLOATING);
//		String taskName = taskNameParameter.getParameterValue();
//		Entry floatingTask = formatEditedFloatingTaskForStorage(taskName);
//		
//		Response responseForAddFloating = new Response();
//		
//		if (_storageHandler.isAddToFileSuccessful(floatingTask)) {
//			setSuccessResponseForAdd(responseForAddFloating, taskName);
//		} else {
//			setFailureResponseForAdd(responseForAddFloating);
//		}
//		
//		return responseForAddFloating;
//		return null;
//	}

//	private Response executeDeleteCommand(Command commandInput) {
//		ArrayList<Parameter> parameters = commandInput.getParameters();
//		
//		Response responseForDelete = new Response();
//		
//		for (int i = 0; i < parameters.size(); i++) {
//			Parameter parameter = parameters.get(i);
//			ParameterType parameterType = parameter.getParameterType();
//			
//			if (parameterType == ParameterType.NAME) {
//				responseForDelete = deleteByName(parameter);
//			}
//		}
//		
//		return responseForDelete;
//	}
	
//	private Response deleteByName(Parameter parameter) {
//		Response responseForDeleteByName = new Response();
//		
//		String taskName = parameter.getParameterValue();
//		
//		if (_storageHandler.isDeleteFromFileSuccessful(taskName)) {
//			setSuccessResponseForDeleteByName(responseForDeleteByName, taskName);
//		} else {
//			setFailureResponseForDeleteByName(responseForDeleteByName);
//		}
//		
//		return responseForDeleteByName;
//	}
	
//	private void setSuccessResponseForDeleteByName(Response response, String taskName) {
//		response.setIsSuccess(true);
//		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_DELETE, taskName);
//		response.setFeedback(userFeedback);
//	}
//	
//	private void setFailureResponseForDeleteByName(Response response) {
//		response.setIsSuccess(false);
//		IOException exobj = new IOException(MESSAGE_ERROR_FOR_DELETE);
//		response.setException(exobj);
//	}
	
//	private Response failToRecogniseCommand() {
//		Response responseForInvalidCommand = new Response();
//		responseForInvalidCommand.setIsSuccess(false);
//		IllegalArgumentException exobj = new IllegalArgumentException(MESSAGE_ERROR_INVALID_COMMAND);
//		responseForInvalidCommand.setException(exobj);
//		
//		return responseForInvalidCommand;
//	}
}
