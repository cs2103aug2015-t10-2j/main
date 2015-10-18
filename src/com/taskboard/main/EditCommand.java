package com.taskboard.main;

import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

public class EditCommand extends Command {
	
	private static final String MESSAGE_ERROR_FOR_INVALID_INDEX = "Invalid index provided.";
//	private static final String MESSAGE_ERROR_FOR_INVALID_DATE_TIME = "Invalid date time provided.";
//	private static final String MESSAGE_ERROR_FOR_PAST_DATE_TIME = "Past date time provided.";
	private static final String MESSAGE_ERROR_FOR_NO_DATE = "No date provided.";
	private static final String MESSAGE_AFTER_EDIT = "\"%1$s\" updated!";
	private static final String MESSAGE_ERROR_FOR_EDIT = "The entry could not be edited.";
	
//	private static final String FORMAT_DEFAULT_TIME = "00:00";
//	private static final String FORMAT_DATE_TIME = "dd/MM/yyyy'T'HH:mm";

	private static final int MIN_ENTRY_INDEX = 1;
	
	public EditCommand(CommandType commandType, ArrayList<Parameter> parameters) {
		_commandType = commandType;
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		Response responseForEdit = new Response();
		
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage();
		IndexValidator indexValidator = new IndexValidator();
		
		if (indexValidator.isInputIndexValid(_parameters, entries)) {
			responseForEdit = processEditedDetailsForStorage();
		} else {
			setFailureResponseForInvalidIndex(responseForEdit);
		}
 		
		return responseForEdit;
	}
	
	private void setFailureResponseForInvalidIndex(Response response) {
		response.setIsSuccess(false);
		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_INVALID_INDEX);
		response.setException(exObj);
	}
	
	private Response processEditedDetailsForStorage() {
		ArrayList<Parameter> editedParameters = new ArrayList<Parameter>();
		Response responseForEdit = new Response();
		
		String index = "";
		String editedEntryName = "";
		String editedPriority = "";
		String editedCategory = "";
		
		int indexValue;
		
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			switch (parameterType) {
				case INDEX:
					index = parameter.getParameterValue();
					indexValue = Integer.valueOf(index);
					break;
				case NEW_NAME:
					editedEntryName = parameter.getParameterValue();
					Parameter editedEntryNameParameter = new Parameter(ParameterType.NAME, editedEntryName);
					editedParameters.add(editedEntryNameParameter);
					break;
				case PRIORITY:
					editedPriority = parameter.getParameterValue();
					Parameter editedPriorityParameter = new Parameter(ParameterType.PRIORITY, editedPriority);
					editedParameters.add(editedPriorityParameter);
					break;
				case CATEGORY:
					editedCategory = parameter.getParameterValue();
					Parameter editedCategoryParameter = new Parameter(ParameterType.CATEGORY, editedCategory);
					editedParameters.add(editedCategoryParameter);
					break;
			}			
		}
		
		if (isEditDateTimeForDeadlineTask()) {
			responseForEdit = processEditedDateTimeDetailsForDeadlineTask(editedParameters, indexValue);
		} else if (isEditDateTimeForEvent()) {
			responseForEdit = processEditedDateTimeDetailsForEvent(editedParameters, indexValue);
		} else {
			responseForEdit.setIsSuccess(true);
		}
		
		
		if (responseForEdit.isSuccess() == true) {
			try {
				_tempStorageManipulator.editTempStorage(indexValue, editedParameters);
				String entryName = getEntryName(indexValue);
				setSuccessResponseForEdit(responseForEdit, entryName);
			} catch (IOException ex) {
				setFailureResponseForEdit(responseForEdit);
			}
		}
		
		return responseForEdit;
	}
	
	private boolean isEditDateTimeForDeadlineTask() {
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			if (parameterType == ParameterType.DATE || parameterType == ParameterType.TIME) {
				return true;
			}
		}
		
		return false;
	}
	
	private Response processEditedDateTimeDetailsForDeadlineTask(ArrayList<Parameter> editedParameters, int index) {
		Response responseForDateTime = new Response();
		
		String editedDate = "";
		String editedTime = "";
		
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			switch (parameterType) {
				case DATE:
					editedDate = parameter.getParameterValue();
					break;
				case TIME:
					editedTime = parameter.getParameterValue();
					break;
			}
		}
		
		if (isEditedEntryFloatingTask(index) || isEditedEntryEvent(index)) {
			responseForDateTime = processEditingToDeadlineTask(editedParameters, index, editedDate,
					                                           editedTime);			                                                              
		} else if (isEditedEntryDeadlineTask(index)) {
			responseForDateTime = processEditingDeadlineTask(editedParameters, index, editedDate,
					                                         editedTime);
		} 
				
		return responseForDateTime;
	}
	
	private boolean isEditedEntryFloatingTask(int index) {
		if (!isEditedEntryDeadlineTask(index) && !isEditedEntryEvent(index)) {
			return true;
		}
		
		return false;
	}
	
	private boolean isEditedEntryDeadlineTask(int index) {
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage(); 
		Entry entry = entries.get(index - 1);
		Parameter dateParameter = entry.getDateParameter();
		
		if (dateParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private boolean isEditedEntryEvent(int index) {
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage(); 
		Entry entry = entries.get(index - 1);
		Parameter startDateParameter = entry.getStartDateParameter();
		
		if (startDateParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private Response processEditingToDeadlineTask(ArrayList<Parameter> editedParameters, int index,
			                                      String editedDate, String editedTime) {
		Response responseForDateTime = new Response();
		
		if (editedDate.isEmpty()) {
			setFailureResponseForNoDate(responseForDateTime);
			return responseForDateTime;
		}
		
		responseForDateTime = validateEditedDateTimeDetails(editedDate, editedTime);	
		
		if (responseForDateTime.isSuccess() == true) {
			Parameter editedDateParameter = new Parameter(ParameterType.DATE, editedDate);
			editedParameters.add(editedDateParameter);
			
			if (!editedTime.isEmpty()) {	
				Parameter editedTimeParameter = new Parameter(ParameterType.TIME, editedTime);
				editedParameters.add(editedTimeParameter);
			}
		}
		
		return responseForDateTime;
	}
	
	private void setFailureResponseForNoDate(Response response) {
		response.setIsSuccess(false);
		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_NO_DATE);
		response.setException(exObj);
	}
	
	private Response validateEditedDateTimeDetails(String editedDate, String editedTime) {
		Response responseForDateTime = new Response();
		
		DateTimeValidator dateTimeValidator = new DateTimeValidator();
		Date currentDate = new Date();
		responseForDateTime = dateTimeValidator.validateDateTimeDetails(editedDate, editedTime, currentDate);
		
		return responseForDateTime;
	}
	
	private Response processEditingDeadlineTask(ArrayList<Parameter> editedParameters, int index,
			                                    String editedDate, String editedTime) {
		Response responseForDateTime = new Response();
		
		if (editedDate.isEmpty()) {
			editedDate = getDateFromEntry(index);
			responseForDateTime = validateEditedDateTimeDetails(editedDate, editedTime);
			
			if (responseForDateTime.isSuccess() == true) {
				Parameter editedTimeParameter = new Parameter(ParameterType.TIME, editedTime);
				editedParameters.add(editedTimeParameter);
			}
			
		} else if (editedTime.isEmpty()) {
			editedTime = getTimeFromEntry(index);
			responseForDateTime = validateEditedDateTimeDetails(editedDate, editedTime);
			
			if (responseForDateTime.isSuccess() == true) {
				Parameter editedDateParameter = new Parameter(ParameterType.DATE, editedDate);
				editedParameters.add(editedDateParameter);
			}
			
		} else {
			responseForDateTime = validateEditedDateTimeDetails(editedDate, editedTime);
			
			if (responseForDateTime.isSuccess() == true) {
				Parameter editedDateParameter = new Parameter(ParameterType.DATE, editedDate);
				editedParameters.add(editedDateParameter);
				
				Parameter editedTimeParameter = new Parameter(ParameterType.TIME, editedTime);
				editedParameters.add(editedTimeParameter);
			}
		}
		
		return responseForDateTime;
	}
	
	private String getDateFromEntry(int index) {
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage(); 
		Entry entry = entries.get(index - 1);
		Parameter dateParameter = entry.getDateParameter();
		String date = dateParameter.getParameterValue();
		
		return date;
	}
	
	private String getTimeFromEntry(int index) {
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage(); 
		Entry entry = entries.get(index - 1);
		Parameter timeParameter = entry.getTimeParameter();
		
		String time = "";
		
		if (timeParameter != null) {
			time = timeParameter.getParameterValue();
		}
		 
		return time;
	}
	
	private boolean isEditDateTimeForEvent() {
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			if (parameterType == ParameterType.START_DATE || parameterType == ParameterType.START_TIME ||
				parameterType == ParameterType.END_DATE || parameterType == ParameterType.END_TIME) {
				return true;
			}
		}
		
		return false;
	}
	
	private Response processEditedDateTimeDetailsForEvent(ArrayList<Parameter> editedParameters, int index) {
		Response responseForDateTime = new Response();
		
		String editedStartDate = "";
		String editedStartTime = "";
		String editedEndDate = "";
		String editedEndTime = "";
		
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			switch (parameterType) {
				case START_DATE:
					editedStartDate = parameter.getParameterValue();
					break;
				case START_TIME:
					editedStartTime =  parameter.getParameterValue();
					break;
				case END_DATE:
					editedEndDate =  parameter.getParameterValue();
					break;
				case END_TIME:
					editedEndTime =  parameter.getParameterValue();
					break; 
			}
		}
		
		return responseForDateTime;
	}
	
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
	
//	private String getDateTimeFormat(String date, String time) {
//		if (time.isEmpty()) {
//			time = FORMAT_DEFAULT_TIME;
//		}
//		
//		String dateTime = date.concat("T").concat(time);
//		
//		return dateTime;
//	}
	
//	private Date getInputDate(String dateTime) {
//		 try {
//			 DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE_TIME);
//	         dateFormat.setLenient(false);
//	         Date inputDate = dateFormat.parse(dateTime);
//	         
//	         return inputDate;
//	     } catch (ParseException e) {
//	         return null;
//	     }
//	}
	
//	private void setFailureResponseForInvalidDateTime(Response response) {
//		response.setIsSuccess(false);
//		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_INVALID_DATE_TIME);
//		response.setException(exObj);
//	}
	
//	private void setFailureResponseForPastDateTime(Response response) {
//		response.setIsSuccess(false);
//		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_PAST_DATE_TIME);
//		response.setException(exObj);
//	}
	
	private String getEntryName(int index) {
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage(); 
		Entry entry = entries.get(index - 1);
		Parameter entryNameParameter = entry.getNameParameter();
		String entryName = entryNameParameter.getParameterValue();
		
		return entryName;
	}
	
	private void setSuccessResponseForEdit(Response response, String eventName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_EDIT, eventName);
		response.setFeedback(userFeedback);
	}
	
	private void setFailureResponseForEdit(Response response) {
		response.setIsSuccess(false);
		IOException exobj = new IOException(MESSAGE_ERROR_FOR_EDIT);
		response.setException(exobj);
	}
}
