package com.taskboard.main;

import java.util.ArrayList;
import java.util.Date;

public class SelectiveFilterProcessor {
	
	private static final String MESSAGE_FILTER_RESULTS = "entries found based on search results!";
	
	// attribute
	
	private ArrayList<Entry> _filteredEntries;
	
	// constructor
	
	public SelectiveFilterProcessor() {
		
	}
	
	// accessor
	
	public ArrayList<Entry> getFilteredEntries() {
		return _filteredEntries;
	}
	
	public Response processFiltering(ArrayList<Entry> entries, ArrayList<Parameter> parameters) {
		Response responseForFiltering = new Response();
		
		_filteredEntries = entries;
		
		if (isFilterByName(parameters)) {
			_filteredEntries = processFilterByName(parameters);
		}
		
		if (isFilterByPriority(parameters)) {
			_filteredEntries = processFilterByPriority(parameters);
		}
		
		if (isFilterByCategory(parameters)) {
			_filteredEntries = processFilterByCategory(parameters);
		}
		
		if (isFilterByDate(parameters)) {
			DateTimeValidator dateValidator = new DateTimeValidator();
			responseForFiltering = checkForDateValidity(dateValidator, parameters);
			
			if (responseForFiltering.getException() != null) {
				return responseForFiltering;
			}
			
			_filteredEntries = processFilterByDateTime(dateValidator);
		}
		
		if (isFilterByDateTime(parameters)) {
			DateTimeValidator dateTimeValidator = new DateTimeValidator();
			responseForFiltering = checkForDateTimeValidity(dateTimeValidator, parameters);
			
			if (responseForFiltering.getException() != null) {
				return responseForFiltering;
			}
			
			_filteredEntries = processFilterByDateTime(dateTimeValidator); 
		}
		
		if (isFilterByDateTimeRange(parameters)) {
			DateTimeValidator startDateTimeValidator = new DateTimeValidator();
			DateTimeValidator endDateTimeValidator = new DateTimeValidator();
			responseForFiltering = checkForDateTimeValidity(startDateTimeValidator, endDateTimeValidator,
					                                        parameters);
			
			if (responseForFiltering.getException() != null) {
				return responseForFiltering;
			}
			
			_filteredEntries = processFilterByDateTimeRange(startDateTimeValidator, 
					                                       endDateTimeValidator);
		}
		
		setSuccessResponseForViewWithFilter(responseForFiltering);
		
		return responseForFiltering;
	}
	
	private boolean isFilterByName(ArrayList<Parameter> parameters) {
		Parameter nameParameter = getNameParameter(parameters);
		
		if (nameParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private Parameter getNameParameter(ArrayList<Parameter> parameters) {
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getParameterType() == ParameterType.NAME) {
				return parameters.get(i);
			}
		}
		
		return null;
	}
	
	private ArrayList<Entry> processFilterByName(ArrayList<Parameter> parameters) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		String searchKey = getDetailFromParameter(getNameParameter(parameters));
		SelectiveFilter selectiveFilterByName = new SelectiveFilter();
		filteredEntries = selectiveFilterByName.filterByName(_filteredEntries, searchKey);
		
		return filteredEntries;
	}
	
	private String getDetailFromParameter(Parameter parameter) {
		String detail = "";
		
		if (parameter != null) {
			detail = parameter.getParameterValue();
		}
		
		return detail;
	}
	
	private boolean isFilterByPriority(ArrayList<Parameter> parameters) {
		Parameter priorityParameter = getPriorityParameter(parameters);
		
		if (priorityParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private Parameter getPriorityParameter(ArrayList<Parameter> parameters) {
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getParameterType() == ParameterType.PRIORITY) {
				return parameters.get(i);
			}
		}
		
		return null;
	}
	
	private ArrayList<Entry> processFilterByPriority(ArrayList<Parameter> parameters) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		String searchPriority = getDetailFromParameter(getPriorityParameter(parameters));
		SelectiveFilter selectiveFilterByPriority = new SelectiveFilter();
		filteredEntries = selectiveFilterByPriority.filterByPriority(_filteredEntries, searchPriority);
		
		return filteredEntries;
	}
	
	private boolean isFilterByCategory(ArrayList<Parameter> parameters) {
		Parameter categoryParameter = getCategoryParameter(parameters);
		
		if (categoryParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private Parameter getCategoryParameter(ArrayList<Parameter> parameters) {
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getParameterType() == ParameterType.CATEGORY) {
				return parameters.get(i);
			}
		}
		
		return null;
	}
	
	private ArrayList<Entry> processFilterByCategory(ArrayList<Parameter> parameters) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		String searchCategory = getDetailFromParameter(getCategoryParameter(parameters));
		SelectiveFilter selectiveFilterByCategory = new SelectiveFilter();
		filteredEntries = selectiveFilterByCategory.filterByCategory(_filteredEntries, searchCategory); 
		
		return filteredEntries;
	}
	
	private boolean isFilterByDate(ArrayList<Parameter> parameters) {
		Parameter dateParameter = getDateParameter(parameters);
		Parameter timeParameter = getTimeParameter(parameters);
		
		if (dateParameter != null && timeParameter == null) {
			return true;
		}
		
		return false;
	}
	
	private Parameter getDateParameter(ArrayList<Parameter> parameters) {
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getParameterType() == ParameterType.DATE) {
				return parameters.get(i);
			}
		}
		
		return null;
	}
	
	private Parameter getTimeParameter(ArrayList<Parameter> parameters) {
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getParameterType() == ParameterType.TIME) {
				return parameters.get(i);
			}
		}
		
		return null;
	}
	
	private Response checkForDateValidity(DateTimeValidator dateValidator, ArrayList<Parameter> parameters) {
		Response responseForDate = new Response();
		
		String date = getDetailFromParameter(getDateParameter(parameters));
		responseForDate = dateValidator.validateDateTimeDetails(date, "", null);
		
		return responseForDate;
	}
	
	private ArrayList<Entry> processFilterByDateTime(DateTimeValidator dateTimeValidator) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();

		Date inputDate = dateTimeValidator.getDate();
		SelectiveFilter selectiveFilterByDateTime = new SelectiveFilter();
		filteredEntries = selectiveFilterByDateTime.filterByDateTime(_filteredEntries, inputDate);

		return filteredEntries;
	}
	
	private boolean isFilterByDateTime(ArrayList<Parameter> parameters) {
		Parameter timeParameter = getTimeParameter(parameters);
		
		if (timeParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private Response checkForDateTimeValidity(DateTimeValidator dateTimeValidator, 
			                                  ArrayList<Parameter> parameters) {
		Response responseForDateTime = new Response();
		
		String date = getDetailFromParameter(getDateParameter(parameters));
		String time = getDetailFromParameter(getTimeParameter(parameters));
		
		DateTimeProcessor deadlineDateTimeProcessor = new DateTimeProcessor();
		responseForDateTime = deadlineDateTimeProcessor.processDeadlineDateTimeDetails(date, time);
		
		if (responseForDateTime.isSuccess() == true) {
			responseForDateTime = dateTimeValidator.validateDateTimeDetails(date, time, null);
		}
		
		return responseForDateTime;
	}
		
	private boolean isFilterByDateTimeRange(ArrayList<Parameter> parameters) {
		Parameter startDateParameter = getStartDateParameter(parameters);
		Parameter startTimeParameter = getStartTimeParameter(parameters);
		Parameter endDateParameter = getEndDateParameter(parameters);
	    Parameter endTimeParameter = getEndTimeParameter(parameters);
		
		if (startDateParameter != null || startTimeParameter != null || endDateParameter != null || 
	        endTimeParameter != null) {
			return true;
		}
		
		return false;
	}
	
	private Parameter getStartDateParameter(ArrayList<Parameter> parameters) {
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getParameterType() == ParameterType.START_DATE) {
				return parameters.get(i);
			}
		}
		
		return null;
	}
	
	private Parameter getStartTimeParameter(ArrayList<Parameter> parameters) {
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getParameterType() == ParameterType.START_TIME) {
				return parameters.get(i);
			}
		}
		
		return null;
	}
	
	private Parameter getEndDateParameter(ArrayList<Parameter> parameters) {
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getParameterType() == ParameterType.END_DATE) {
				return parameters.get(i);
			}
		}
		
		return null;
	}
	
	private Parameter getEndTimeParameter(ArrayList<Parameter> parameters) {
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getParameterType() == ParameterType.END_TIME) {
				return parameters.get(i);
			}
		}
		
		return null;
	}
	
	private Response checkForDateTimeValidity(DateTimeValidator startDateTimeValidator, 
			                                  DateTimeValidator endDateTimeValidator, 
			                                  ArrayList<Parameter> parameters) {
		Response responseForDateTime = new Response();
		
		String startDate = getDetailFromParameter(getStartDateParameter(parameters));
		String startTime = getDetailFromParameter(getStartTimeParameter(parameters));
		String endDate = getDetailFromParameter(getEndDateParameter(parameters));
		String endTime = getDetailFromParameter(getEndTimeParameter(parameters));
		
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
		
	private ArrayList<Entry> processFilterByDateTimeRange(DateTimeValidator startDateTimeValidator,
			                                              DateTimeValidator endDateTimeValidator) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		Date inputStartDate = startDateTimeValidator.getDate();
		Date inputEndDate = endDateTimeValidator.getDate();
		SelectiveFilter selectiveFilterByDateTimeRange = new SelectiveFilter();
		filteredEntries = selectiveFilterByDateTimeRange.filterByDateTimeRange(_filteredEntries, inputStartDate, 
				                                                               inputEndDate);
		
		return filteredEntries;
	}
	
	private void setSuccessResponseForViewWithFilter(Response response) {
		response.setIsSuccess(true);
		int numOfFilteredEntries = _filteredEntries.size();
		String userFeedback = String.valueOf(numOfFilteredEntries);
		userFeedback = userFeedback.concat(" ").concat(MESSAGE_FILTER_RESULTS);
		response.setFeedback(userFeedback);
		response.setEntries(_filteredEntries);
	}
	
	private String getFeedbackForUser(String feedbackMessage, String detail) {
		return String.format(feedbackMessage, detail);
	}
}