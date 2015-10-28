package com.taskboard.main;

import java.io.IOException;

import java.util.ArrayList;

public class DeleteCommand extends Command {
	
	private static final String MESSAGE_AFTER_DELETE = "\"%1$s\" deleted!";
	private static final String MESSAGE_ERROR_FOR_DELETE = "The deletion was unsuccessful.";
	
	public DeleteCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		Response responseForDelete = new Response();
		
		if (isDeleteByIndex()) {
			responseForDelete = processInputIndex();
			
			if (responseForDelete.isSuccess() == false) {
				return responseForDelete;
			}
			
			responseForDelete = processDeleteByIndex();
		} else {
			responseForDelete = processDeleteByFiltering();
		}
		
		return responseForDelete;
	}
	
	private boolean isDeleteByIndex() {
		Parameter indexParameter = getIndexParameter();
		
		if (indexParameter != null) {
			return true;
		}
		
		return false; 
	}
	
	private Response processInputIndex() {
		Response responseForInputIndex = new Response();
		
		String index = getDetailFromParameter(getIndexParameter());
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage();
		IndexValidator indexValidator = new IndexValidator();
		responseForInputIndex = indexValidator.checkValidityOfInputIndex(index, entries);
		
		return responseForInputIndex;
	}
	
	private Response processDeleteByIndex() {
		Response ResponseForDeleteByIndex = new Response();
		
		String index = getDetailFromParameter(getIndexParameter());
		int indexValue = Integer.parseInt(index);
		int tempStorageIndex = indexValue - 1;
		
		try {
			String entryName = getEntryName(indexValue);
			_tempStorageManipulator.deleteFromTempStorage(tempStorageIndex);
			setSuccessResponseForDeleteByIndex(ResponseForDeleteByIndex, entryName);
		} catch (IOException ex) {
			setFailureResponseForDelete(ResponseForDeleteByIndex);
		}
		
		return ResponseForDeleteByIndex;
	}
	
	private String getEntryName(int index) {
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage(); 
		Entry entry = entries.get(index - 1);
		Parameter entryNameParameter = entry.getNameParameter();
		String entryName = entryNameParameter.getParameterValue();
		
		return entryName;
	}
	
	private void setSuccessResponseForDeleteByIndex(Response response, String entryName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForUser(MESSAGE_AFTER_DELETE, entryName);
		response.setFeedback(userFeedback);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private void setFailureResponseForDelete(Response response) {
		response.setIsSuccess(false);
		IOException exobj = new IOException(MESSAGE_ERROR_FOR_DELETE);
		response.setException(exobj);
	}
	
	private Response processDeleteByFiltering() {
		SelectiveFilterProcessor selectiveFilterProcessor = new SelectiveFilterProcessor();
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage();
		Response responseForFiltering = selectiveFilterProcessor.processFiltering(entries, _parameters);
		ArrayList<Entry> filteredEntries = selectiveFilterProcessor.getFilteredEntries();
		
		try {
			if (!filteredEntries.isEmpty()) {
				_tempStorageManipulator.deleteFromTempStorage(filteredEntries);
			}
			
			setSuccessResponseForDeleteByFiltering(responseForFiltering);
			
			return responseForFiltering;
		} catch (IOException ex) {
			Response failedResponseForDelete = new Response();
			setFailureResponseForDelete(failedResponseForDelete);
			
			return failedResponseForDelete;
		}
	}
	
	private void setSuccessResponseForDeleteByFiltering(Response response) {
		String userFeedback = response.getFeedback().replace("found", "deleted");
		response.setFeedback(userFeedback);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
//	private boolean isFilterByName() {
//		Parameter nameParameter = getNameParameter();
//		
//		if (nameParameter != null) {
//			return true;
//		}
//		
//		return false;
//	}
//	
//	private ArrayList<Entry> processFilterByName(ArrayList<Entry> entries) {
//		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
//		
//		String searchKey = getDetailFromParameter(getNameParameter());
//		SelectiveFilter selectiveFilterByName = new SelectiveFilter();
//		filteredEntries = selectiveFilterByName.filterByName(entries, searchKey);
//		
//		return filteredEntries;
//	}
//	
//	private boolean isFilterByPriority() {
//		Parameter priorityParameter = getPriorityParameter();
//		
//		if (priorityParameter != null) {
//			return true;
//		}
//		
//		return false;
//	}
//	
//	private ArrayList<Entry> processFilterByPriority(ArrayList<Entry> entries) {
//		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
//		
//		String searchPriority = getDetailFromParameter(getPriorityParameter());
//		SelectiveFilter selectiveFilterByPriority = new SelectiveFilter();
//		filteredEntries = selectiveFilterByPriority.filterByPriority(entries, searchPriority);
//		
//		return filteredEntries;
//	}
//	
//	private boolean isFilterByCategory() {
//		Parameter categoryParameter = getCategoryParameter();
//		
//		if (categoryParameter != null) {
//			return true;
//		}
//		
//		return false;
//	}
//	
//	private ArrayList<Entry> processFilterByCategory(ArrayList<Entry> entries) {
//		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
//		
//		String searchCategory = getDetailFromParameter(getCategoryParameter());
//		SelectiveFilter selectiveFilterByCateogory = new SelectiveFilter();
//		filteredEntries = selectiveFilterByCateogory.filterByCategory(entries, searchCategory); 
//		
//		return filteredEntries;
//	}
//	
//	private boolean isFilterByDate() {
//		Parameter dateParameter = getDateParameter();
//		Parameter timeParameter = getTimeParameter();
//		
//		if (dateParameter != null && timeParameter == null) {
//			return true;
//		}
//		
//		return false;
//	}
//	
//	private Response checkForDateValidity() {
//		Response responseForDate = new Response();
//		
//		String date = getDetailFromParameter(getDateParameter());
//		DateTimeValidator dateValidator = new DateTimeValidator();
//		responseForDate = dateValidator.validateDateTimeDetails(date, "", null);
//		
//		return responseForDate;
//	}
//	
//	private ArrayList<Entry> processFilterByDate(ArrayList<Entry> entries) {
//		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
//		
//		String searchDate = getDetailFromParameter(getDateParameter());
//		SelectiveFilter selectiveFilterByDate = new SelectiveFilter();
//		filteredEntries = selectiveFilterByDate.filterByDate(entries, searchDate);
//		
//		return filteredEntries;
//	}
//	
//	private boolean isFilterByDateTime() {
//		Parameter timeParameter = getTimeParameter();
//		
//		if (timeParameter != null) {
//			return true;
//		}
//		
//		return false;
//	}
//	
//	private Response checkForDateTimeValidity(DateTimeValidator dateTimeValidator) {
//		Response responseForDateTime = new Response();
//		
//		String date = getDetailFromParameter(getDateParameter());
//		String time = getDetailFromParameter(getTimeParameter());
//		
//		DateTimeProcessor deadlineDateTimeProcessor = new DateTimeProcessor();
//		responseForDateTime = deadlineDateTimeProcessor.processDeadlineDateTimeDetails(date, time);
//		
//		if (responseForDateTime.isSuccess() == true) {
//			responseForDateTime = dateTimeValidator.validateDateTimeDetails(date, time, null);
//		}
//		
//		return responseForDateTime;
//	}
//	
//	private ArrayList<Entry> processFilterByDateTime(ArrayList<Entry> entries, 
//			                                         DateTimeValidator dateTimeValidator) {
//		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
//		
//		Date inputDate = dateTimeValidator.getDate();
//		SelectiveFilter selectiveFilterByDateTime = new SelectiveFilter();
//		filteredEntries = selectiveFilterByDateTime.filterByDateTime(entries, inputDate);
//		
//		return filteredEntries;
//	}
//	
//	private boolean isFilterByDateTimeRange() {
//		Parameter startDateParameter = getStartDateParameter();
//		Parameter startTimeParameter = getStartTimeParameter();
//		Parameter endDateParameter = getEndDateParameter();
//	    Parameter endTimeParameter = getEndTimeParameter();
//		
//		if (startDateParameter != null || startTimeParameter != null || endDateParameter != null || 
//	        endTimeParameter != null) {
//			return true;
//		}
//		
//		return false;
//	}
//	
//	private Response checkForDateTimeValidity(DateTimeValidator startDateTimeValidator, 
//			                                  DateTimeValidator endDateTimeValidator) {
//		Response responseForDateTime = new Response();
//		
//		String startDate = getDetailFromParameter(getStartDateParameter());
//		String startTime = getDetailFromParameter(getStartTimeParameter());
//		String endDate = getDetailFromParameter(getEndDateParameter());
//		String endTime = getDetailFromParameter(getEndTimeParameter());
//		
//		DateTimeProcessor eventDateTimeProcessor = new DateTimeProcessor();
//		responseForDateTime = eventDateTimeProcessor.processEventDateTimeDetails(startDate, startTime, 
//				                                                                 endDate, endTime);
//		
//		if (responseForDateTime.isSuccess() == true) {
//			endDate = startDate;
//		}
//		
//		if (responseForDateTime.getException() == null) {
//			responseForDateTime = startDateTimeValidator.validateDateTimeDetails(startDate, startTime, null);
//			
//			if (responseForDateTime.getException() == null) {
//				Date inputStartDate = startDateTimeValidator.getDate();
//				responseForDateTime = endDateTimeValidator.validateDateTimeDetails(endDate, endTime, 
//						                                                           inputStartDate);
//			}
//		}
//		
//		return responseForDateTime;
//	}
//		
//	private ArrayList<Entry> processFilterByDateTimeRange(ArrayList<Entry> entries, 
//			                                              DateTimeValidator startDateTimeValidator,
//			                                              DateTimeValidator endDateTimeValidator) {
//		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
//		
//		Date inputStartDate = startDateTimeValidator.getDate();
//		Date inputEndDate = endDateTimeValidator.getDate();
//		SelectiveFilter selectiveFilterByDateTimeRange = new SelectiveFilter();
//		filteredEntries = selectiveFilterByDateTimeRange.filterByDateTimeRange(entries, inputStartDate, 
//				                                                               inputEndDate);
//		
//		return filteredEntries;
//	}
}