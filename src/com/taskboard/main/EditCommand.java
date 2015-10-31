package com.taskboard.main;

import java.io.IOException;

import java.util.ArrayList;
import java.util.logging.Level;

public class EditCommand extends Command {
	
	private static final String MESSAGE_AFTER_EDIT = "Entry successfully updated:";
	private static final String MESSAGE_FOR_UPDATED_ENTRY = "Entry after update =>";
	private static final String MESSAGE_FOR_OLD_ENTRY = "Entry before update =>";
	private static final String MESSAGE_ERROR_FOR_EDIT = "The entry could not be edited.";

	public EditCommand(ArrayList<Parameter> parameters) {
		assert parameters != null;
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
		
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	public Response executeCommand() {
		assert _parameters.size() > 1;
		_logger.log(Level.INFO, "Commenced execution of EditCommand");
		
		ArrayList<Entry> initialTempStorage = new ArrayList<Entry>();
		for (Entry entry: _tempStorageManipulator.getTempStorage()) {
			initialTempStorage.add(new Entry(entry));
		}
		
		ArrayList<Entry> initialTempArchive = new ArrayList<Entry>();
		for (Entry entry: _tempStorageManipulator.getTempArchive()) {
			initialTempArchive.add(new Entry(entry));
		}
				
		IndexProcessor indexProcessorForEdit = new IndexProcessor();
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage();
		Response responseForEdit = indexProcessorForEdit.processInputIndex(_parameters, entries);
		if (responseForEdit.isSuccess()) {
			_logger.log(Level.INFO, "Start process of editing entry");
			responseForEdit = processEditedDetailsForStorage();
		}
		
		if (responseForEdit.isSuccess()) {
			_tempStorageManipulator.setLastTempStorage(initialTempStorage);
			_tempStorageManipulator.setLastTempArchive(initialTempArchive);
		}
		 
		return responseForEdit;
	}
	
	private Response processEditedDetailsForStorage() {
		ArrayList<Parameter> editedParameters = new ArrayList<Parameter>();
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
		
		Response responseForEdit = new Response();
		String index = getDetailFromParameter(getIndexParameter());
		int indexValue = Integer.valueOf(index);
		if (isEditDateTimeForDeadlineTask()) {
			_logger.log(Level.INFO, "Start processing edited date time details for deadline task");
			responseForEdit = processEditedDateTimeDetailsForDeadlineTask(editedParameters, indexValue);
		} else if (isEditDateTimeForEvent()) {
			_logger.log(Level.INFO, "Start processing edited date time details for event");
			responseForEdit = processEditedDateTimeDetailsForEvent(editedParameters, indexValue);
		} else {
			boolean isEntryTypeChanged = false;
			_logger.log(Level.INFO, "Start processing edited details for storage");
			responseForEdit = updateEditedDetailsToStorage(editedParameters, indexValue, isEntryTypeChanged);
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
	
	private Response processEditedDateTimeDetailsForDeadlineTask(ArrayList<Parameter> editedParameters, 
			                                                     int index) {
		String newDate = getDetailFromParameter(getDateParameter());
		String newTime = getDetailFromParameter(getTimeParameter());
		Response responseForDateTime = new Response();
		if (isEditedEntryFloatingTask(index) || isEditedEntryEvent(index)) {
			_logger.log(Level.INFO, "Start process of converting existing entry to deadline task");
			responseForDateTime = processConversionToDeadlineTask(editedParameters, index, newDate, newTime);							                                                              
		} else if (isEditedEntryDeadlineTask(index)) {
			_logger.log(Level.INFO, "Start process of editing existing deadline task");
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
	
	private Response processConversionToDeadlineTask(ArrayList<Parameter> editedParameters, int index, 
			                                         String newDate, String newTime) {
		DateTimeProcessor deadlineDateTimeProcessor = new DateTimeProcessor();
		Response responseForDateTime = deadlineDateTimeProcessor.processDeadlineDateTimeDetails(newDate, newTime);
		if (responseForDateTime.isSuccess()) {
			_logger.log(Level.INFO, "Start validating edited date time details for deadline task");
			responseForDateTime = deadlineDateTimeProcessor.validateDeadlineDateTimeDetails(newDate, newTime);
			if (responseForDateTime.isSuccess()) {
				boolean isEntryTypeChanged = true;
				responseForDateTime = constructEditedDateTimeParametersForDeadlineTask(editedParameters, index, 
						                                                               newDate, newTime, 
						                                                               isEntryTypeChanged);
			}
		}
		
		return responseForDateTime;
	}
	
	private Response constructEditedDateTimeParametersForDeadlineTask(ArrayList<Parameter> editedParameters, 
			                                                          int index, String newDate, String newTime, 
			                                                          boolean isEntryTypeChanged) {
		addParameterToEditedParameters(editedParameters, ParameterType.DATE, newDate);
		addParameterToEditedParameters(editedParameters, ParameterType.TIME, newTime);
		_logger.log(Level.INFO, "Start processing edited details for storage");
		Response responseForDateTime = updateEditedDetailsToStorage(editedParameters, index, isEntryTypeChanged);
		
		return responseForDateTime;
	}
	
	private void addParameterToEditedParameters(ArrayList<Parameter> editedParameters,  
			                                    ParameterType parameterType, String detail) {
		if (!detail.isEmpty()) {
			Parameter parameter = new Parameter(parameterType, detail);
			editedParameters.add(parameter);
		}
	}
	
	private Response processEditingDeadlineTask(ArrayList<Parameter> editedParameters, int index, 
			                                    String newDate, String newTime) {
		if (newDate.isEmpty()) {
			newDate = getDateFromEntry(index);
		}
		if (newTime.isEmpty()) {
			newTime = getTimeFromEntry(index);
		}
		
		DateTimeProcessor deadlineDateTimeProcessor = new DateTimeProcessor();
		_logger.log(Level.INFO, "Start validating edited date time details for deadline task");
		Response responseForDateTime = deadlineDateTimeProcessor.validateDeadlineDateTimeDetails(newDate, newTime);
		if (responseForDateTime.isSuccess()) {
			boolean isEntryTypeChanged = false;
			responseForDateTime = constructEditedDateTimeParametersForDeadlineTask(editedParameters, index, 
					                                                               newDate, newTime, 
					                                                               isEntryTypeChanged);
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
		String newStartDate = getDetailFromParameter(getStartDateParameter());
		String newStartTime = getDetailFromParameter(getStartTimeParameter());
		String newEndDate = getDetailFromParameter(getEndDateParameter());
		String newEndTime = getDetailFromParameter(getEndTimeParameter());
		Response responseForDateTime = new Response();
		if (isEditedEntryFloatingTask(index) || isEditedEntryDeadlineTask(index)) {
			_logger.log(Level.INFO, "Start process of converting existing entry to event");
			responseForDateTime = processConversionToEvent(editedParameters, index, newStartDate, newStartTime,
					                                       newEndDate, newEndTime);							                                                              
		} else if (isEditedEntryEvent(index)) {
			_logger.log(Level.INFO, "Start process of editing existing event");
			responseForDateTime = processEditingEvent(editedParameters, index, newStartDate, newStartTime,
					                                  newEndDate, newEndTime);		 
		} 
			
		return responseForDateTime;
	}
	
	private Response processConversionToEvent(ArrayList<Parameter> editedParameters, int index, 
			                                  String newStartDate, String newStartTime, String newEndDate, 
			                                  String newEndTime) {
		DateTimeProcessor eventDateTimeProcessor = new DateTimeProcessor();
		Response responseForDateTime = eventDateTimeProcessor.processEventDateTimeDetails(newStartDate, newStartTime, 
				                                                                          newEndDate, newEndTime);
		if (responseForDateTime.isSuccess()) {
			_logger.log(Level.INFO, "Assign start date to end date");
			newEndDate = newStartDate;
		}
		if (responseForDateTime.getFeedback() == null) {
			_logger.log(Level.INFO, "Start validating edited date time details for event");
			responseForDateTime = eventDateTimeProcessor.validateEventDateTimeDetails(newStartDate, newStartTime, 
					                                                                  newEndDate, newEndTime);
			if (responseForDateTime.isSuccess()) {
				boolean isEntryTypeChanged = true;
				responseForDateTime = constructEditedDateTimeParametersForEvent(editedParameters, index, 
						                                                        newStartDate, newStartTime, 
						                                                        newEndDate, newEndTime,
						                                                        isEntryTypeChanged);
			}
		}
		
		return responseForDateTime;
	}
	
	private Response constructEditedDateTimeParametersForEvent(ArrayList<Parameter> editedParameters, int index,
			                                                   String newStartDate, String newStartTime, 
			                                                   String newEndDate, String newEndTime, 
			                                                   boolean isEntryTypeChanged) {                      
		addParameterToEditedParameters(editedParameters, ParameterType.START_DATE, newStartDate);
		addParameterToEditedParameters(editedParameters, ParameterType.START_TIME, newStartTime);
		addParameterToEditedParameters(editedParameters, ParameterType.END_DATE, newEndDate);
		addParameterToEditedParameters(editedParameters, ParameterType.END_TIME, newEndTime);
		_logger.log(Level.INFO, "Start processing edited details for storage");
		Response responseForDateTime = updateEditedDetailsToStorage(editedParameters, index, isEntryTypeChanged);
		
		return responseForDateTime;
	}
	
	private Response processEditingEvent(ArrayList<Parameter> editedParameters, int index, String newStartDate,
			                             String newStartTime, String newEndDate, String newEndTime) {
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
		
		DateTimeProcessor eventDateTimeProcessor = new DateTimeProcessor();
		_logger.log(Level.INFO, "Start validating edited date time details for event");
		Response responseForDateTime = eventDateTimeProcessor.validateEventDateTimeDetails(newStartDate, newStartTime, 
				                                                                           newEndDate, newEndTime);
		if (responseForDateTime.isSuccess()) {
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
			Entry entryBeforeUpdate = _tempStorageManipulator.getTempStorage().get(tempStorageIndex);
			_tempStorageManipulator.editTempStorage(tempStorageIndex, editedParameters, isEntryTypeChanged);
			Entry entryAfterUpdate = _tempStorageManipulator.getTempStorage().get(tempStorageIndex);
			setSuccessResponseForEdit(responseForEdit, entryBeforeUpdate, entryAfterUpdate);
			_logger.log(Level.INFO, "Generated success response for editing entry");
		} catch (IOException ex) {
			setFailureResponseForEdit(responseForEdit);
			_logger.log(Level.INFO, "Generated failure response for editing entry");
		}
		
		return responseForEdit;
	}

	private void setSuccessResponseForEdit(Response response, Entry entryBeforeUpdate, Entry entryAfterUpdate) {
		response.setIsSuccess(true);
		String userFeedback = MESSAGE_AFTER_EDIT.concat("\n").concat("\n").concat(MESSAGE_FOR_UPDATED_ENTRY);
		userFeedback = userFeedback.concat("\n").concat(entryAfterUpdate.toString());
		userFeedback = userFeedback.concat("\n").concat(MESSAGE_FOR_OLD_ENTRY);
		userFeedback = userFeedback.concat("\n").concat(entryBeforeUpdate.toString());
		response.setFeedback(userFeedback);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private void setFailureResponseForEdit(Response response) {
		response.setIsSuccess(false);
		response.setFeedback(MESSAGE_ERROR_FOR_EDIT);
	}
}