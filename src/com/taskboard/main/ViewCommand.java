package com.taskboard.main;

import java.util.ArrayList;
import java.util.Date;

public class ViewCommand extends Command {
	
	private static final String MESSAGE_EMPTY_FILE = "There are no registered entries.";
	private static final String MESSAGE_RETRIEVE_SUCCESS = "Successfully retrieved all entries.";
	private static final String MESSAGE_FILTER_RESULTS = "\"%1$s\" entries found based on search results!";

	public ViewCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		Response responseForView = new Response();
		
		if (isViewWithoutFilter()) {
			setSuccessResponseForViewWithoutFilter(responseForView);
		} else {
			responseForView = processFiltering();
		}
		
		return responseForView;
	}
	
	private boolean isViewWithoutFilter() {
		if (_parameters.isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	private void setSuccessResponseForViewWithoutFilter(Response response) {
		response.setIsSuccess(true);
		ArrayList<Entry> entries = _tempStorageManipulator.getTempStorage();
		response.setEntries(entries);
		
		if (entries.isEmpty()) {
			response.setFeedback(MESSAGE_EMPTY_FILE);
		} else {
			response.setFeedback(MESSAGE_RETRIEVE_SUCCESS);
		}
	}
	
	private Response processFiltering() {
		Response responseForFiltering = new Response();
		
		ArrayList<Entry> filteredEntries = _tempStorageManipulator.getTempStorage();
		
		if (isFilterByName()) {
			filteredEntries = processFilterByName(filteredEntries);
		}
		
		if (isFilterByPriority()) {
			filteredEntries = processFilterByPriority(filteredEntries);
		}
		
		if (isFilterByCategory()) {
			filteredEntries = processFilterByCategory(filteredEntries);
		}
		
		if (isFilterByDate()) {
			responseForFiltering = checkForDateValidity();
			
			if (responseForFiltering.getException() != null) {
				return responseForFiltering;
			}
			
			filteredEntries = processFilterByDate(filteredEntries);
		}
		
		if (isFilterByDateTime()) {
			DateTimeValidator dateTimeValidator = new DateTimeValidator();
			responseForFiltering = checkForDateTimeValidity(dateTimeValidator);
			
			if (responseForFiltering.getException() != null) {
				return responseForFiltering;
			}
			
			filteredEntries = processFilterByDateTime(filteredEntries, dateTimeValidator); 
		}
		
		if (isFilterByDateTimeRange()) {
			DateTimeValidator startDateTimeValidator = new DateTimeValidator();
			DateTimeValidator endDateTimeValidator = new DateTimeValidator();
			responseForFiltering = checkForDateTimeValidity(startDateTimeValidator, endDateTimeValidator);
			
			if (responseForFiltering.getException() != null) {
				return responseForFiltering;
			}
			
			filteredEntries = processFilterByDateTimeRange(filteredEntries, startDateTimeValidator, 
					                                       endDateTimeValidator);
		}
		
		setSuccessResponseForViewWithFilter(responseForFiltering, filteredEntries);
		
		return responseForFiltering;
	}
	
	private boolean isFilterByName() {
		Parameter nameParameter = getNameParameter();
		
		if (nameParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private ArrayList<Entry> processFilterByName(ArrayList<Entry> entries) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		String searchKey = getDetailFromParameter(getNameParameter());
		SelectiveFilter selectiveFilterByName = new SelectiveFilter();
		filteredEntries = selectiveFilterByName.filterByName(entries, searchKey);
		
		return filteredEntries;
	}
	
	private boolean isFilterByPriority() {
		Parameter priorityParameter = getPriorityParameter();
		
		if (priorityParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private ArrayList<Entry> processFilterByPriority(ArrayList<Entry> entries) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		String searchPriority = getDetailFromParameter(getPriorityParameter());
		SelectiveFilter selectiveFilterByPriority = new SelectiveFilter();
		filteredEntries = selectiveFilterByPriority.filterByPriority(entries, searchPriority);
		
		return filteredEntries;
	}
	
	private boolean isFilterByCategory() {
		Parameter categoryParameter = getCategoryParameter();
		
		if (categoryParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private ArrayList<Entry> processFilterByCategory(ArrayList<Entry> entries) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		String searchCategory = getDetailFromParameter(getCategoryParameter());
		SelectiveFilter selectiveFilterByCateogory = new SelectiveFilter();
		filteredEntries = selectiveFilterByCateogory.filterByCategory(entries, searchCategory); 
		
		return filteredEntries;
	}
	
	private boolean isFilterByDate() {
		Parameter dateParameter = getDateParameter();
		Parameter timeParameter = getTimeParameter();
		
		if (dateParameter != null && timeParameter == null) {
			return true;
		}
		
		return false;
	}
	
	private Response checkForDateValidity() {
		Response responseForDate = new Response();
		
		String date = getDetailFromParameter(getDateParameter());
		DateTimeValidator dateValidator = new DateTimeValidator();
		responseForDate = dateValidator.validateDateTimeDetails(date, "", null);
		
		return responseForDate;
	}
	
	private ArrayList<Entry> processFilterByDate(ArrayList<Entry> entries) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		String searchDate = getDetailFromParameter(getDateParameter());
		SelectiveFilter selectiveFilterByDate = new SelectiveFilter();
		filteredEntries = selectiveFilterByDate.filterByDate(entries, searchDate);
		
		return filteredEntries;
	}
	
	private boolean isFilterByDateTime() {
		Parameter timeParameter = getTimeParameter();
		
		if (timeParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private Response checkForDateTimeValidity(DateTimeValidator dateTimeValidator) {
		Response responseForDateTime = new Response();
		
		String date = getDetailFromParameter(getDateParameter());
		String time = getDetailFromParameter(getTimeParameter());
		
		DateTimeProcessor deadlineDateTimeProcessor = new DateTimeProcessor();
		responseForDateTime = deadlineDateTimeProcessor.processDeadlineDateTimeDetails(date, time);
		
		if (responseForDateTime.isSuccess() == true) {
			responseForDateTime = dateTimeValidator.validateDateTimeDetails(date, time, null);
		}
		
		return responseForDateTime;
	}
	
	private ArrayList<Entry> processFilterByDateTime(ArrayList<Entry> entries, 
			                                         DateTimeValidator dateTimeValidator) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		Date inputDate = dateTimeValidator.getDate();
		SelectiveFilter selectiveFilterByDateTime = new SelectiveFilter();
		filteredEntries = selectiveFilterByDateTime.filterByDateTime(entries, inputDate);
		
		return filteredEntries;
	}
	
	private boolean isFilterByDateTimeRange() {
		Parameter startDateParameter = getStartDateParameter();
		Parameter startTimeParameter = getStartTimeParameter();
		Parameter endDateParameter = getEndDateParameter();
	    Parameter endTimeParameter = getEndTimeParameter();
		
		if (startDateParameter != null || startTimeParameter != null || endDateParameter != null || 
	        endTimeParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private Response checkForDateTimeValidity(DateTimeValidator startDateTimeValidator, 
			                                  DateTimeValidator endDateTimeValidator) {
		Response responseForDateTime = new Response();
		
		String startDate = getDetailFromParameter(getStartDateParameter());
		String startTime = getDetailFromParameter(getStartTimeParameter());
		String endDate = getDetailFromParameter(getEndDateParameter());
		String endTime = getDetailFromParameter(getEndTimeParameter());
		
		DateTimeProcessor eventDateTimeProcessor = new DateTimeProcessor();
		responseForDateTime = eventDateTimeProcessor.processEventDateTimeDetails(startDate, startTime, 
				                                                                 endDate, endTime);
		
		if (responseForDateTime.isSuccess() == true) {
			endDate = startDate;
		}
		
		if (responseForDateTime.getException() == null) {
			responseForDateTime = startDateTimeValidator.validateDateTimeDetails(startDate, startTime, null);
			
			if (responseForDateTime.getException() == null) {
				Date inputStartDate = startDateTimeValidator.getDate();
				responseForDateTime = endDateTimeValidator.validateDateTimeDetails(endDate, endTime, 
						                                                           inputStartDate);
			}
		}
		
		return responseForDateTime;
	}
		
	private ArrayList<Entry> processFilterByDateTimeRange(ArrayList<Entry> entries, 
			                                              DateTimeValidator startDateTimeValidator,
			                                              DateTimeValidator endDateTimeValidator) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		Date inputStartDate = startDateTimeValidator.getDate();
		Date inputEndDate = endDateTimeValidator.getDate();
		SelectiveFilter selectiveFilterByDateTimeRange = new SelectiveFilter();
		filteredEntries = selectiveFilterByDateTimeRange.filterByDateTimeRange(entries, inputStartDate, 
				                                                               inputEndDate);
		
		return filteredEntries;
	}
	
	private void setSuccessResponseForViewWithFilter(Response response, ArrayList<Entry> filteredEntries) {
		response.setIsSuccess(true);
		int numOfFilteredEntries = filteredEntries.size();
		String userFeedback = getFeedbackForUser(MESSAGE_FILTER_RESULTS, String.valueOf(numOfFilteredEntries));
		response.setFeedback(userFeedback);
		response.setEntries(filteredEntries);
	}
}