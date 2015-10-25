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
	private static final String MESSAGE_ERROR_FOR_NO_INDEX = "No entry index provided";
	private static final String MESSAGE_ERROR_FOR_NO_EDIT_DETAILS = "No edit details provided.";
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
		
		responseForEdit = processInputIndex();
		
		if (responseForEdit.isSuccess() == true) {
			if (!isEditWithDetails()) {
				setFailureResponseForNoEditDetails(responseForEdit);
				return responseForEdit;
			}
			
			responseForEdit = processEditedDetailsForStorage();
		}
		 		
		return responseForEdit;
	}
	
	private Response processInputIndex() {
		Response responseForInputIndex = new Response();
		
		if (!isIndexProvided()) {
			setFailureResponseForNoIndex(responseForInputIndex);
			return responseForInputIndex;
		}
		
		String index = getDetailFromParameter(getIndexParameter());
		responseForInputIndex = validateInputIndex(index);
		
		return responseForInputIndex;
	}
	
	private boolean isIndexProvided() {
		Parameter indexParameter = getIndexParameter();
		
		if (indexParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private boolean isEditWithDetails() {
		if (isIndexProvided() && _parameters.size() > 1) {
			return true;
		}
		
		return false;
	}
	
	private void setFailureResponseForNoIndex(Response response) {
		response.setIsSuccess(false);
		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_NO_INDEX);
		response.setException(exObj);
	}
	
	private void setFailureResponseForNoEditDetails(Response response) {
		response.setIsSuccess(false);
		IllegalArgumentException exObj = new IllegalArgumentException(MESSAGE_ERROR_FOR_NO_EDIT_DETAILS);
		response.setException(exObj);
	}
	
	private Response validateInputIndex(String index) {
		Response responseForInputIndex = new Response();
		
		IndexValidator indexValidator = new IndexValidator();
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage();
		responseForInputIndex = indexValidator.checkValidityOfInputIndex(index, entries);
		
		return responseForInputIndex;
	}
	
	private Response processEditedDetailsForStorage() {
		ArrayList<Parameter> editedParameters = new ArrayList<Parameter>();
		Response responseForEdit = new Response();
		
		String index = getDetailFromParameter(getIndexParameter());
		int indexValue = Integer.valueOf(index);
		
		Parameter newNameParameter = getNewNameParameter();
		if (newNameParameter != null) {
			newNameParameter.setParameterType(ParameterType.NAME);
			editedParameters.add(newNameParameter);
		}
		
		Parameter priorityParameter = getPriorityParameter();
		if (priorityParameter != null) {
			editedParameters.add(priorityParameter);
		}
		
		Parameter categoryParameter = getCategoryParameter();
		if (categoryParameter != null) {
			editedParameters.add(categoryParameter);
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
		Parameter dateParameter = getDateParameter();
		Parameter timeParameter = getTimeParameter();
		
		if (dateParameter != null || timeParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private Response processEditedDateTimeDetailsForDeadlineTask(ArrayList<Parameter> editedParameters, int index) {
		Response responseForDateTime = new Response();
		
		String newDate = getDetailFromParameter(getDateParameter());
		String newTime = getDetailFromParameter(getTimeParameter());
		
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
			responseForDateTime = constructEditedDateTimeParametersForDeadlineTask(editedParameters, index, newDate,
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
	
	private Response constructEditedDateTimeParametersForDeadlineTask(ArrayList<Parameter> editedParameters, int index,
			                                                          String newDate, String newTime, 
			                                                          boolean isEntryTypeChanged) {
		Response responseForDateTime = new Response();
		
		addParameterToEditedParameters(editedParameters, ParameterType.DATE, newDate);
		addParameterToEditedParameters(editedParameters, ParameterType.TIME, newTime);
				
		responseForDateTime = updateEditedDetailsToStorage(editedParameters, index, isEntryTypeChanged);
		
		return responseForDateTime;
	}
	
	private void addParameterToEditedParameters(ArrayList<Parameter> editedParameters, ParameterType parameterType,
			                                    String detail) {
		if (!detail.isEmpty()) {
			Parameter parameter = new Parameter(parameterType, detail);
			editedParameters.add(parameter);
		}
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
			responseForDateTime = constructEditedDateTimeParametersForDeadlineTask(editedParameters, index, newDate,
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
		Parameter startDateParameter = getStartDateParameter();
		Parameter startTimeParameter = getStartTimeParameter();
		Parameter endDateParameter = getEndDateParameter();
		Parameter endTimeParameter = getEndTimeParameter();
		
		if (startDateParameter != null || startTimeParameter != null || 
		    endDateParameter != null || endTimeParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private Response processEditedDateTimeDetailsForEvent(ArrayList<Parameter> editedParameters, int index) {
		Response responseForDateTime = new Response();
		
		String newStartDate = getDetailFromParameter(getStartDateParameter());
		String newStartTime = getDetailFromParameter(getStartTimeParameter());
		String newEndDate = getDetailFromParameter(getEndDateParameter());
		String newEndTime = getDetailFromParameter(getEndTimeParameter());
		
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
			responseForDateTime = constructEditedDateTimeParametersForEvent(editedParameters, index, newStartDate,  
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
			Date inputStartDate = startDateTimeValidator.getDate();
			responseForDateTime = endDateTimeValidator.validateDateTimeDetails(endDate, endTime, inputStartDate);
		}
		
		return responseForDateTime;
	}
	
	private Response constructEditedDateTimeParametersForEvent(ArrayList<Parameter> editedParameters, int index,
			                                                   String newStartDate, String newStartTime, String newEndDate, 
			                                                   String newEndTime, boolean isEntryTypeChanged) {                      
		Response responseForDateTime = new Response();
		
		addParameterToEditedParameters(editedParameters, ParameterType.START_DATE, newStartDate);
		addParameterToEditedParameters(editedParameters, ParameterType.START_TIME, newStartTime);
		addParameterToEditedParameters(editedParameters, ParameterType.END_DATE, newEndDate);
		addParameterToEditedParameters(editedParameters, ParameterType.END_TIME, newEndTime);
			
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
			responseForDateTime = constructEditedDateTimeParametersForEvent(editedParameters, index, newStartDate,  
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
			int tempStorageIndex = index - 1;
			_tempStorageManipulator.editTempStorage(tempStorageIndex, editedParameters, isEntryTypeChanged);
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
	
	private void setSuccessResponseForEdit(Response response, String entryName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_EDIT, entryName);
		response.setFeedback(userFeedback);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private void setFailureResponseForEdit(Response response) {
		response.setIsSuccess(false);
		IOException exobj = new IOException(MESSAGE_ERROR_FOR_EDIT);
		response.setException(exobj);
	}
}