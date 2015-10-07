package com.taskboard.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.Date;

import java.io.IOException;

public class Logic {
	
	private static final String MESSAGE_WELCOME = "Welcome to TASKBOARD!"; 
	private static final String MESSAGE_AFTER_ADD = "\"%1$s\" added!";
	private static final String MESSAGE_AFTER_DELETE = "\"%1$s\" deleted!";
	private static final String MESSAGE_ERROR_FOR_ADD = "The entry could not be added to the file.";
	private static final String MESSAGE_ERROR_FOR_LAUNCH = "Fail to create new scheduler.";
	private static final String MESSAGE_ERROR_INVALID_DATE_TIME = "Invalid date time provided.";
	private static final String MESSAGE_ERROR_PAST_DATE_TIME = "Past date time provided.";
	private static final String MESSAGE_ERROR_FOR_DELETE = "The entry could not be deleted from the file.";
	private static final String MESSAGE_ERROR_INVALID_COMMAND = "Invalid command type provided.";
	private static final String DATE_FORMAT = "dd/MM/yyyy'T'HH:mm";
	private static final String DEFAULT_TIME_FORMAT = "00:00";
	
	private static final int INDEX_OF_TASKNAME_FOR_ADD_FLOATING = 0;
	private static final int INDEX_OF_FILENAME_FOR_LAUNCH = 0;
	private static final int INDEX_OF_FIRST_CHAR_IN_TIME = 0;
	
	// attributes
	
	private CommandParser _commandParser;
	private StorageHandler _storageHandler;
	
	// constructor
	
	public Logic() {
		_commandParser = new CommandParser();
	}
		
	public Response processCommand(String userInput) {
		Command commandInput = _commandParser.parseCommand(userInput);
		CommandType commandType = commandInput.getCommandType();
		
		Response responseForOperations = new Response();
		
		switch (commandType) {
			case NEW: case OPEN:
				responseForOperations = executeLaunchCommand(commandInput);
				break;
			case ADD:
				responseForOperations = executeAddCommand(commandInput);
				break;
			case EDIT:
				responseForOperations = executeEditCommand(commandInput);
				break;
			case DELETE:
				responseForOperations = executeDeleteCommand(commandInput);
				break;
			case EXIT:
				System.exit(0);
			case UNKNOWN:
				responseForOperations = failToRecogniseCommand();
				break;
		}
		
		return responseForOperations; 
	}
	
	private Response executeLaunchCommand(Command commandInput) {
		ArrayList<Parameter> parameters = commandInput.getParameters();
		Parameter fileNameParameter = parameters.get(INDEX_OF_FILENAME_FOR_LAUNCH); 
		String fileName = fileNameParameter.getParameterValue();
		
		return getResponseForLaunch(fileName);
	}
	
	private Response getResponseForLaunch(String fileName) {
		_storageHandler = new StorageHandler();
		
		Response responseForLaunch = new Response();
		
		if (_storageHandler.isSetUpSuccessful(fileName)) {
			setSuccessResponseForLaunch(responseForLaunch, fileName);
		} else {
			setFailureResponseForLaunch(responseForLaunch);
		}	
		
		return responseForLaunch;
	}
	
	private void setSuccessResponseForLaunch(Response response, String fileName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_WELCOME, fileName);
		response.setFeedback(userFeedback);
	}
	
	private void setFailureResponseForLaunch(Response response) {
		response.setIsSuccess(false);
		IOException exobj = new IOException(MESSAGE_ERROR_FOR_LAUNCH);
		response.setException(exobj);
	}
	
	private String getFeedbackForUser(String feedbackMessage, String details) {
		return String.format(feedbackMessage, details);
	}
	
	private Response executeAddCommand(Command commandInput) {
		ArrayList<Parameter> parameters = commandInput.getParameters();
		
		Response responseForAdd = new Response();
		
		if (isAddFloatingTask(parameters)) {
			responseForAdd = addFloatingTask(parameters);
		} else if (isAddDeadlineTask(parameters)) {
			responseForAdd = addDeadlineTask(parameters);
		}
		
		return responseForAdd;
	}
	
	private boolean isAddFloatingTask (ArrayList<Parameter> parameters) {
		if (parameters.size() == 1) {
			return true;
		}
		
		return false;
	}
		
	private Response addFloatingTask(ArrayList<Parameter> parameters) {		
		Parameter taskNameParameter = parameters.get(INDEX_OF_TASKNAME_FOR_ADD_FLOATING);
		String taskName = taskNameParameter.getParameterValue();
		Entry floatingTask = formatFloatingTaskForStorage(taskName);
		
		Response responseForAddFloating = new Response();
		
		if (_storageHandler.isAddToFileSuccessful(floatingTask)) {
			setSuccessResponseForAdd(responseForAddFloating, taskName);
		} else {
			setFailureResponseForAdd(responseForAddFloating);
		}
		
		return responseForAddFloating;
	}
	
	private void setSuccessResponseForAdd(Response response, String taskName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_ADD, taskName);
		response.setFeedback(userFeedback);
	}
	
	private void setFailureResponseForAdd(Response response) {
		response.setIsSuccess(false);
		IOException exobj = new IOException(MESSAGE_ERROR_FOR_ADD);
		response.setException(exobj);
	}
	
	private Entry formatFloatingTaskForStorage(String taskName) {
		String formattedTaskName = "Name: " + taskName;
		
		Entry floatingTask = new Entry();
		floatingTask.addToDetails(formattedTaskName);
		//floatingTask.addToDetails("\n");
		
		return floatingTask;
	}
	
	private boolean isAddDeadlineTask(ArrayList<Parameter> parameters) {
		for (int i = 0; i < parameters.size(); i++) {
			Parameter parameter = parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			if (parameterType == ParameterType.DATE || parameterType == ParameterType.TIME) {
				return true;
			}
		}
		
		return false;
	}
	
	private Response addDeadlineTask(ArrayList<Parameter> parameters) {
		Response responseForAddDeadline = new Response();
		
		String time = "";
		String date = "";
		String taskName = "";
		
		for (int i = 0; i < parameters.size(); i++) {
			Parameter parameter = parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			if (parameterType == ParameterType.TIME) {
				time = parameter.getParameterValue();
			} else if (parameterType == ParameterType.DATE) {
				date = parameter.getParameterValue();
			} else {
				taskName = parameter.getParameterValue();
			}
		}
		
		String dateTime = getDateTimeFormat(date, time);
		Date inputDate = getInputDate(dateTime);
		
		if (inputDate == null) {
			setFailureResponseForInvalidDateTime(responseForAddDeadline);
		}
		else {
			Date todayDate = new Date();
			
			if (inputDate.after(todayDate)) {
				Entry deadlineTask = formatDeadlineTaskForStorage(taskName, date, time);
				
				if (_storageHandler.isAddToFileSuccessful(deadlineTask)) {
					setSuccessResponseForAdd(responseForAddDeadline, taskName);
				} else {
					setFailureResponseForAdd(responseForAddDeadline);
				}
				
			} else {
				setFailureResponseForPastDateTime(responseForAddDeadline);
			}
		}	
		
		return responseForAddDeadline;
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
	
	private void setFailureResponseForInvalidDateTime(Response response) {
		response.setIsSuccess(false);
		IllegalArgumentException exobj = new IllegalArgumentException(MESSAGE_ERROR_INVALID_DATE_TIME);
		response.setException(exobj);
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
	
	private void setFailureResponseForPastDateTime(Response response) {
		response.setIsSuccess(false);
		IllegalArgumentException exobj = new IllegalArgumentException(MESSAGE_ERROR_PAST_DATE_TIME);
		response.setException(exobj);
	}
	
	private Response executeEditCommand(Command commandInput) {
		// STUB
		Response responseForEdit = new Response();
		
		return responseForEdit;
	}

	private Response executeDeleteCommand(Command commandInput) {
		ArrayList<Parameter> parameters = commandInput.getParameters();
		
		Response responseForDelete = new Response();
		
		for (int i = 0; i < parameters.size(); i++) {
			Parameter parameter = parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			if (parameterType == ParameterType.NAME) {
				responseForDelete = deleteByName(parameter);
			}
		}
		
		return responseForDelete;
	}
	
	private Response deleteByName(Parameter parameter) {
		Response responseForDeleteByName = new Response();
		
		String taskName = parameter.getParameterValue();
		
		if (_storageHandler.isDeletingSuccessful(taskName)) {
			setSuccessResponseForDeleteByName(responseForDeleteByName, taskName);
		} else {
			setFailureResponseForDeleteByName(responseForDeleteByName);
		}
		
		return responseForDeleteByName;
	}
	
	private void setSuccessResponseForDeleteByName(Response response, String taskName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_DELETE, taskName);
		response.setFeedback(userFeedback);
	}
	
	private void setFailureResponseForDeleteByName(Response response) {
		response.setIsSuccess(false);
		IOException exobj = new IOException(MESSAGE_ERROR_FOR_DELETE);
		response.setException(exobj);
	}
	
	public String retrieveEntries() {
		String entriesList = _storageHandler.retrieveEntriesInFile();
		
		return entriesList;
	}
	
	private Response failToRecogniseCommand() {
		Response responseForInvalidCommand = new Response();
		responseForInvalidCommand.setIsSuccess(false);
		IllegalArgumentException exobj = new IllegalArgumentException(MESSAGE_ERROR_INVALID_COMMAND);
		responseForInvalidCommand.setException(exobj);
		
		return responseForInvalidCommand;
	}
}
