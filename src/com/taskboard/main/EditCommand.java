package com.taskboard.main;

import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

public class EditCommand extends Command {
	
	private static final String MESSAGE_AFTER_EDIT = "\"%1$s\" updated!";
	private static final String MESSAGE_ERROR_FOR_EDIT = "The entry could not be edited.";
	private static final String MESSAGE_ERROR_FOR_NO_DATE = "No date provided.";
	private static final String MESSAGE_ERROR_FOR_NO_START_DATE = "No start date provided.";
	private static final String MESSAGE_ERROR_FOR_NO_END_DATE_TIME = "No end date time provided.";
	
	public EditCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		Response responseForEdit = new Response();
		
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage();
		IndexValidator indexValidator = new IndexValidator();
		
		responseForEdit = indexValidator.checkValidityOfInputIndex(_parameters, entries);
		
		if (responseForEdit.isSuccess() == true) {
			responseForEdit = processEditedDetailsForStorage();
		}
		 		
		return responseForEdit;
	}
		
	private Response processEditedDetailsForStorage() {
		ArrayList<Parameter> editedParameters = new ArrayList<Parameter>();
		Response responseForEdit = new Response();
		
		String index = "";
		String newEntryName = "";
		
		int indexValue = 0;
		
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			switch (parameterType) {
				case INDEX:
					index = parameter.getParameterValue();
					indexValue = Integer.valueOf(index);
					break;
				case NEW_NAME:
					newEntryName = parameter.getParameterValue();
					Parameter newEntryNameParameter = new Parameter(ParameterType.NAME, newEntryName);
					editedParameters.add(newEntryNameParameter);
					break;
				case PRIORITY:
					editedParameters.add(parameter);
					break;
				case CATEGORY:
					editedParameters.add(parameter);
					break;
			}			
		}
		
		if (isEditDateTimeForDeadlineTask()) {
			responseForEdit = processEditedDateTimeDetailsForDeadlineTask(editedParameters, indexValue);
		} else if (isEditDateTimeForEvent()) {
			responseForEdit = processEditedDateTimeDetailsForEvent(editedParameters, indexValue);
		} else {
			responseForEdit = updateEditedDetailsToStorage(editedParameters, indexValue, false);
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
		
		String newDate = "";
		String newTime = "";
		
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			switch (parameterType) {
				case DATE:
					newDate = parameter.getParameterValue();
					break;
				case TIME:
					newTime = parameter.getParameterValue();
					break;
			}
		}
		
		if (isEditedEntryFloatingTask(index) || isEditedEntryEvent(index)) {
			responseForDateTime = processConversionToDeadlineTask(editedParameters, index, newDate, newTime);							                                                              
		} else if (isEditedEntryDeadlineTask(index)) {
			responseForDateTime = processEditingDeadlineTask(editedParameters, index, newDate, newTime);
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
	
	private Response processConversionToDeadlineTask(ArrayList<Parameter> editedParameters, int index, String newDate,
			                                         String newTime) {
		Response responseForDateTime = new Response();
		
		if (newDate.isEmpty()) {
			setFailureResponseForNoDate(responseForDateTime);
			return responseForDateTime;
		}
		
		responseForDateTime = validateDateTimeDetailsForDeadlineTask(newDate, newTime);	
		
		if (responseForDateTime.isSuccess() == true) {
			boolean isEntryTypeChanged = true;
			responseForDateTime = formatEditedDateTimeParametersForDeadlineTask(editedParameters, index, newDate,
					                                                            newTime, isEntryTypeChanged);
		}
		
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
	
	private Response formatEditedDateTimeParametersForDeadlineTask(ArrayList<Parameter> editedParameters, int index,
			                                                       String newDate, String newTime, 
			                                                       boolean isEntryTypeChanged) {
		Response responseForDateTime = new Response();
		
		Parameter newDateParameter = new Parameter(ParameterType.DATE, newDate);
		editedParameters.add(newDateParameter);
		
		if (!newTime.isEmpty()) {	
			Parameter newTimeParameter = new Parameter(ParameterType.TIME, newTime);
			editedParameters.add(newTimeParameter);
		}
		
		responseForDateTime = updateEditedDetailsToStorage(editedParameters, index, isEntryTypeChanged);
		
		return responseForDateTime;
	}
	
	
	private Response processEditingDeadlineTask(ArrayList<Parameter> editedParameters, int index, String newDate,
			                                    String newTime) {
		Response responseForDateTime = new Response();
		
		if (newDate.isEmpty()) {
			newDate = getDateFromEntry(index);
		}
		
		if (newTime.isEmpty()) {
			newTime = getTimeFromEntry(index);
		}
		
		responseForDateTime = validateDateTimeDetailsForDeadlineTask(newDate, newTime);
			
		if (responseForDateTime.isSuccess() == true) {
			boolean isEntryTypeChanged = false;
			responseForDateTime = formatEditedDateTimeParametersForDeadlineTask(editedParameters, index, newDate,
					                                                            newTime, isEntryTypeChanged);
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
		
		String newStartDate = "";
		String newStartTime = "";
		String newEndDate = "";
		String newEndTime = "";
		
		for (int i = 0; i < _parameters.size(); i++) {
			Parameter parameter = _parameters.get(i);
			ParameterType parameterType = parameter.getParameterType();
			
			switch (parameterType) {
				case START_DATE:
					newStartDate = parameter.getParameterValue();
					break;
				case START_TIME:
					newStartTime = parameter.getParameterValue();
					break;
				case END_DATE:
					newEndDate = parameter.getParameterValue();
					break;
				case END_TIME:
					newEndTime = parameter.getParameterValue();
					break; 
			}
		}
		
		if (isEditedEntryFloatingTask(index) || isEditedEntryDeadlineTask(index)) {
			responseForDateTime = processConversionToEvent(editedParameters, index, newStartDate, newStartTime,
					                                       newEndDate, newEndTime);							                                                              
		} else if (isEditedEntryEvent(index)) {
			responseForDateTime = processEditingEvent(editedParameters, index, newStartDate, newStartTime,
					                                  newEndDate, newEndTime);		 
		} 
			
		return responseForDateTime;
	}
	
	private Response processConversionToEvent(ArrayList<Parameter> editedParameters, int index, String newStartDate,
			                                  String newStartTime, String newEndDate, String newEndTime) {
		Response responseForDateTime = new Response();
		
		if (newStartDate.isEmpty()) {
			setFailureResponseForNoStartDate(responseForDateTime);
			return responseForDateTime;
		}
		
		if (newEndDate.isEmpty() && newEndTime.isEmpty()) {
			setFailureResponseForNoEndDateTime(responseForDateTime);
			return responseForDateTime;
		}
		
		if (newEndDate.isEmpty()) {
			newEndDate = newStartDate;
		}
		
		responseForDateTime = validateDateTimeDetailsForEvent(newStartDate, newStartTime, newEndDate, newEndTime);
		
		if (responseForDateTime.isSuccess() == true) {
			boolean isEntryTypeChanged = true;
			responseForDateTime = formatEditedDateTimeParametersForEvent(editedParameters, index, newStartDate,  
					                                                     newStartTime, newEndDate, newEndTime,
					                                                     isEntryTypeChanged);
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
	
	private Response validateDateTimeDetailsForEvent(String startDate, String startTime, String endDate, 
			                                         String endTime) {
		Response responseForDateTime = new Response();
		
		DateTimeValidator startDateTimeValidator = new DateTimeValidator();
		Date currentDate = new Date();
		responseForDateTime = startDateTimeValidator.validateDateTimeDetails(startDate, startTime, currentDate);
		
		if (responseForDateTime.isSuccess() == true) {
			DateTimeValidator endDateTimeValidator = new DateTimeValidator();
			Date inputStartDate = startDateTimeValidator.getInputDate();
			responseForDateTime = endDateTimeValidator.validateDateTimeDetails(endDate, endTime, inputStartDate);
		}
		
		return responseForDateTime;
	}
	
	private Response formatEditedDateTimeParametersForEvent(ArrayList<Parameter> editedParameters, int index,
			                                                String newStartDate, String newStartTime, String newEndDate, 
			                                                String newEndTime, boolean isEntryTypeChanged) {                      
		Response responseForDateTime = new Response();
		
		Parameter newStartDateParameter = new Parameter(ParameterType.START_DATE, newStartDate);
		editedParameters.add(newStartDateParameter);
		
		if (!newStartTime.isEmpty()) {	
			Parameter newStartTimeParameter = new Parameter(ParameterType.START_TIME, newStartTime);
			editedParameters.add(newStartTimeParameter);
		}
		
		Parameter newEndDateParameter = new Parameter(ParameterType.END_DATE, newEndDate);
		editedParameters.add(newEndDateParameter);
		
		if (!newEndTime.isEmpty()) {	
			Parameter newEndTimeParameter = new Parameter(ParameterType.END_TIME, newEndTime);
			editedParameters.add(newEndTimeParameter);
		}
		
		responseForDateTime = updateEditedDetailsToStorage(editedParameters, index, isEntryTypeChanged);
		
		return responseForDateTime;
	}
	
	private Response processEditingEvent(ArrayList<Parameter> editedParameters, int index, String newStartDate,
			                             String newStartTime, String newEndDate, String newEndTime) {
		Response responseForDateTime = new Response();
		
		if (newStartDate.isEmpty()) {
			newStartDate = getStartDateFromEntry(index);
		}
		
		if (newStartTime.isEmpty()) {
			newStartTime = getStartTimeFromEntry(index);
		}
		
		if (newEndDate.isEmpty()) {
			newEndDate = getEndDateFromEntry(index);
		}
		
		if (newEndTime.isEmpty()) {
			newEndTime = getEndTimeFromEntry(index);
		}
		
		responseForDateTime = validateDateTimeDetailsForEvent(newStartDate, newStartTime, newEndDate, newEndTime);
		
		if (responseForDateTime.isSuccess() == true) {
			boolean isEntryTypeChanged = false;
			responseForDateTime = formatEditedDateTimeParametersForEvent(editedParameters, index, newStartDate,  
					                                                     newStartTime, newEndDate, newEndTime,
					                                                     isEntryTypeChanged);
		}
		
		return responseForDateTime;
	}
	
	private String getStartDateFromEntry(int index) {
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage(); 
		Entry entry = entries.get(index - 1);
		Parameter startDateParameter = entry.getStartDateParameter();
		String startDate = startDateParameter.getParameterValue();
	
		return startDate;
	}
	
	private String getStartTimeFromEntry(int index) {
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage(); 
		Entry entry = entries.get(index - 1);
		Parameter startTimeParameter = entry.getStartTimeParameter();
		
		String startTime = "";
		
		if (startTimeParameter != null) {
			startTime = startTimeParameter.getParameterValue();
		}
		 
		return startTime;
	}
	
	private String getEndDateFromEntry(int index) {
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage(); 
		Entry entry = entries.get(index - 1);
		Parameter endDateParameter = entry.getEndDateParameter();
		String endDate = endDateParameter.getParameterValue();
	
		return endDate;
	}
	
	private String getEndTimeFromEntry(int index) {
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage(); 
		Entry entry = entries.get(index - 1);
		Parameter endTimeParameter = entry.getEndTimeParameter();
		
		String endTime = "";
		
		if (endTimeParameter != null) {
			endTime = endTimeParameter.getParameterValue();
		}
		 
		return endTime;
	}
	
	private Response updateEditedDetailsToStorage(ArrayList<Parameter> editedParameters, int index,
			                                      boolean isEntryTypeChanged) {
		Response responseForEdit = new Response();
		
		try {
			_tempStorageManipulator.editTempStorage(index - 1, editedParameters, isEntryTypeChanged);
			String entryName = getEntryName(index);
			setSuccessResponseForEdit(responseForEdit, entryName);
		} catch (IOException ex) {
			setFailureResponseForEdit(responseForEdit);
		}
		
		return responseForEdit;
	}
	
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
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private void setFailureResponseForEdit(Response response) {
		response.setIsSuccess(false);
		IOException exobj = new IOException(MESSAGE_ERROR_FOR_EDIT);
		response.setException(exobj);
	}
}
