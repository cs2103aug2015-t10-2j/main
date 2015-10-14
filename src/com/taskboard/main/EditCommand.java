package com.taskboard.main;

import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

public class EditCommand extends Command {
	
	private static final String MESSAGE_ERROR_FOR_INVALID_DATE_TIME = "Invalid date time provided.";
	private static final String MESSAGE_ERROR_FOR_PAST_DATE_TIME = "Past date time provided.";
	private static final String MESSAGE_AFTER_EDIT = "\"%1$s\" updated!";
	private static final String MESSAGE_ERROR_FOR_EDIT = "The entry could not be edited.";
	
	private static final String DATE_FORMAT = "dd/MM/yyyy'T'HH:mm";
	private static final String DEFAULT_TIME_FORMAT = "00:00";
	
	public EditCommand(CommandType commandType, ArrayList<Parameter> parameters) {
		_commandType = commandType;
		_parameters = parameters;
	}
	
	public Response executeCommand() {
		Response responseForEdit = new Response();
		responseForEdit = processEditedDetailsForStorage();
		
		return responseForEdit;
	}
	
	private Response processEditedDetailsForStorage() {
		ArrayList<String> editedDetails = new ArrayList<String>();
		Response responseForEdit = new Response();
		
		String taskName = "";
		String editedTaskName = "";
		String editedDueDate = "";
		String editedDueTime = "";
		
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			switch (parameterType) {
				case NAME:
					taskName = parameter.getParameterValue();
					editedDetails.add(taskName);
					break;
				case NEW_NAME:
					editedTaskName = parameter.getParameterValue();
					String formattedName = "Name: " + editedTaskName;
					editedDetails.add(formattedName);
					break;
				case DATE:
					editedDueDate = parameter.getParameterValue();
					break;
				case TIME:
					editedDueTime = parameter.getParameterValue();
					break;
			}			
		}
		
		String detailType = "";
		boolean isEditDueDateTime = true;
		
		if (editedDueDate.isEmpty() && editedDueTime.isEmpty()) {
			isEditDueDateTime = false;
		}
		
		StorageHandler storageHandler = StorageHandler.getInstance();
		
		if (isEditDueDateTime) {
			if (editedDueDate.isEmpty()) {
				detailType = "Due date:";
				editedDueDate = storageHandler.retrieveDetail(taskName, detailType);	
			} else if (editedDueTime.isEmpty()) {
				detailType = "Due time:";
				editedDueTime = storageHandler.retrieveDetail(taskName, detailType);
			}
			
			responseForEdit = formatEditedDateTimeDetails(editedDetails, editedDueDate, editedDueTime);
			
			if (responseForEdit.getIsSuccess() == false) {
				return responseForEdit;
			}
		}
		
		if (storageHandler.isEditInFileSuccessful(editedDetails)) {
			setSuccessResponseForEdit(responseForEdit, taskName);
		} else {
			setFailureResponseForEdit(responseForEdit);
		}
		
		return responseForEdit;
	}
	
	private Response formatEditedDateTimeDetails(ArrayList<String> editedDetails, String date, String time) {
		Response responseForDateTime = new Response();
		
		String dateTime = getDateTimeFormat(date, time);
		Date inputDate = getInputDate(dateTime);
		
		if (inputDate == null) {
			setFailureResponseForInvalidDateTime(responseForDateTime);
		}
		else {
			Date todayDate = new Date();
			
			if (inputDate.after(todayDate)) {
				responseForDateTime.setIsSuccess(true);
				
				String formattedDate = "Due date: " + date;
				editedDetails.add(formattedDate);
				
				String formattedTime = "Due time: " + time;
				editedDetails.add(formattedTime);
			} else {
				setFailureResponseForPastDateTime(responseForDateTime);
			}
		}
		
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
	
	private void setSuccessResponseForEdit(Response response, String taskName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_EDIT, taskName);
		response.setFeedback(userFeedback);
	}
	
	private String getFeedbackForUser(String feedbackMessage, String details) {
		return String.format(feedbackMessage, details);
	}
	
	private void setFailureResponseForEdit(Response response) {
		response.setIsSuccess(false);
		IOException exobj = new IOException(MESSAGE_ERROR_FOR_EDIT);
		response.setException(exobj);
	}
}
