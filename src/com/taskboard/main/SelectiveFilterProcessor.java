//@@author A0123935E
package com.taskboard.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.ParameterType;
import com.taskboard.main.util.Response;

public class SelectiveFilterProcessor {
	
	private static final String MESSAGE_FILTER_RESULTS = "%1$s entries found based on search results!";
	
	private static final String FORMAT_DEFAULT_TIME_FOR_FILTER_BY_DATE = "00:00";
	private static final String FORMAT_DEFAULT_START_TIME_FOR_FILTER_BY_DATE_TIME_RANGE = "00:00";
	
	// attribute
	
	private ArrayList<Entry> _filteredEntries;
	private Logger _logger;
	
	// constructor
	
	public SelectiveFilterProcessor() {
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	// accessor
	
	public ArrayList<Entry> getFilteredEntries() {
		return _filteredEntries;
	}
	
	public Response processFiltering(ArrayList<Entry> entries, ArrayList<Parameter> parameters) {
		_filteredEntries = entries;
		if (isFilterByName(parameters)) {
			_logger.log(Level.INFO, "Start processing filter by name");
			_filteredEntries = processFilterByName(parameters);
		}
		
		if (isFilterByPriority(parameters)) {
			_logger.log(Level.INFO, "Start processing filter by priority");
			_filteredEntries = processFilterByPriority(parameters);
		}
		
		if (isFilterByCategory(parameters)) {
			_logger.log(Level.INFO, "Start processing filter by category");
			_filteredEntries = processFilterByCategory(parameters);
		}
		
		Response responseForFiltering = new Response();
		if (isFilterByDate(parameters)) {
			_logger.log(Level.INFO, "Start processing filter by date");
			DateTimeValidator dateValidator = new DateTimeValidator();
			responseForFiltering = checkForDateValidity(dateValidator, parameters);
			if (responseForFiltering.getFeedback() != null) {
				return responseForFiltering;
			}
			
			_filteredEntries = processFilterByDate(dateValidator);
		}
		
		if (isFilterByDateTime(parameters)) {
			_logger.log(Level.INFO, "Start processing filter by date time");
			DateTimeValidator dateTimeValidator = new DateTimeValidator();
			responseForFiltering = checkForDateTimeValidity(dateTimeValidator, parameters);
			if (responseForFiltering.getFeedback() != null) {
				return responseForFiltering;
			}
			
			_filteredEntries = processFilterByDateTime(dateTimeValidator); 
		}
		
		if (isFilterByDateTimeRange(parameters)) {
			_logger.log(Level.INFO, "Start processing filter by date time range");
			DateTimeValidator startDateTimeValidator = new DateTimeValidator();
			DateTimeValidator endDateTimeValidator = new DateTimeValidator();
			responseForFiltering = checkForDateTimeValidity(startDateTimeValidator, endDateTimeValidator,
					                                        parameters);
			if (responseForFiltering.getFeedback() != null) {
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
		String searchKey = getDetailFromParameter(getNameParameter(parameters));
		SelectiveFilter selectiveFilterByName = new SelectiveFilter();
		ArrayList<Entry> filteredEntries = selectiveFilterByName.filterByName(_filteredEntries, searchKey);
		_logger.log(Level.INFO, "Successfully filtered " + String.valueOf(filteredEntries.size())
		            + " entries by name");
		
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
		String searchPriority = getDetailFromParameter(getPriorityParameter(parameters));
		SelectiveFilter selectiveFilterByPriority = new SelectiveFilter();
		ArrayList<Entry> filteredEntries = selectiveFilterByPriority.filterByPriority(_filteredEntries, 
				                                                                      searchPriority);
		_logger.log(Level.INFO, "Successfully filtered " + String.valueOf(filteredEntries.size())
                    + " entries by priority");
		
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
		String searchCategory = getDetailFromParameter(getCategoryParameter(parameters));
		SelectiveFilter selectiveFilterByCategory = new SelectiveFilter();
		ArrayList<Entry> filteredEntries = selectiveFilterByCategory.filterByCategory(_filteredEntries, 
				                                                                      searchCategory); 
		_logger.log(Level.INFO, "Successfully filtered " + String.valueOf(filteredEntries.size())
        			+ " entries by category");
		
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
		String date = getDetailFromParameter(getDateParameter(parameters));
		String time = FORMAT_DEFAULT_TIME_FOR_FILTER_BY_DATE; 
		_logger.log(Level.INFO, "Start validating date");
		Response responseForDate = dateValidator.validateDateTimeDetails(date, time, null);
		
		return responseForDate;
	}
	
	private ArrayList<Entry> processFilterByDate(DateTimeValidator dateValidator) {
		Date inputDate = dateValidator.getDate();
		SelectiveFilter selectiveFilterByDate = new SelectiveFilter();
		ArrayList<Entry> filteredEntries = selectiveFilterByDate.filterByDate(_filteredEntries, 
				                                                              inputDate);
		_logger.log(Level.INFO, "Successfully filtered " + String.valueOf(filteredEntries.size())
                    + " entries by date");

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
		String date = getDetailFromParameter(getDateParameter(parameters));
		String time = getDetailFromParameter(getTimeParameter(parameters));	
		DateTimeProcessor deadlineDateTimeProcessor = new DateTimeProcessor();
		_logger.log(Level.INFO, "Start processing date time details");
		Response responseForDateTime = deadlineDateTimeProcessor.processDeadlineDateTimeDetails(date, time);
		if (responseForDateTime.isSuccess()) {
			_logger.log(Level.INFO, "Start validating date time details");
			responseForDateTime = dateTimeValidator.validateDateTimeDetails(date, time, null);
		}
		
		return responseForDateTime;
	}
	
	private ArrayList<Entry> processFilterByDateTime(DateTimeValidator dateTimeValidator) {
		Date inputDate = dateTimeValidator.getDate();
		SelectiveFilter selectiveFilterByDateTime = new SelectiveFilter();
		ArrayList<Entry> filteredEntries = selectiveFilterByDateTime.filterByDateTime(_filteredEntries, 
				                                                                      inputDate);
		_logger.log(Level.INFO, "Successfully filtered " + String.valueOf(filteredEntries.size())
                    + " entries by date time");
		
		return filteredEntries;
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
		String startDate = getDetailFromParameter(getStartDateParameter(parameters));
		String startTime = getDetailFromParameter(getStartTimeParameter(parameters));
		String endDate = getDetailFromParameter(getEndDateParameter(parameters));
		String endTime = getDetailFromParameter(getEndTimeParameter(parameters));
		DateTimeProcessor eventDateTimeProcessor = new DateTimeProcessor();
		if (startTime.isEmpty()) {
			startTime = FORMAT_DEFAULT_START_TIME_FOR_FILTER_BY_DATE_TIME_RANGE; 
		}
		
		_logger.log(Level.INFO, "Start processing range of date time details");
		Response responseForDateTime = eventDateTimeProcessor.processEventDateTimeDetails(startDate, startTime, 
				                                                                          endDate, endTime);
		if (responseForDateTime.isSuccess()) {
			_logger.log(Level.INFO, "Assign start date to end date");
			endDate = startDate;
		}
		if (responseForDateTime.getFeedback() == null) {
			_logger.log(Level.INFO, "Start validating start date time details");
			responseForDateTime = startDateTimeValidator.validateDateTimeDetails(startDate, startTime, null);	
			if (responseForDateTime.getFeedback() == null) {
				Date inputStartDate = startDateTimeValidator.getDate();
				_logger.log(Level.INFO, "Start validating end date time details");
				responseForDateTime = endDateTimeValidator.validateDateTimeDetails(endDate, endTime, 
						                                                           inputStartDate);
			}
		}
		
		return responseForDateTime;
	}
		
	private ArrayList<Entry> processFilterByDateTimeRange(DateTimeValidator startDateTimeValidator,
			                                              DateTimeValidator endDateTimeValidator) {
		Date inputStartDate = startDateTimeValidator.getDate();
		Date inputEndDate = endDateTimeValidator.getDate();
		SelectiveFilter selectiveFilterByDateTimeRange = new SelectiveFilter();
		ArrayList<Entry> filteredEntries = selectiveFilterByDateTimeRange.filterByDateTimeRange(_filteredEntries, 
				                                                                                inputStartDate,
				                                                                                inputEndDate);
		_logger.log(Level.INFO, "Successfully filtered " + String.valueOf(filteredEntries.size())
                    + " entries by date time range");
		
		return filteredEntries;
	}
	
	private void setSuccessResponseForViewWithFilter(Response response) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForSuccessfulViewWithFilter();
		response.setFeedback(userFeedback);
		response.setEntries(_filteredEntries);
	}
	
	private String getFeedbackForSuccessfulViewWithFilter() {
		int numOfFilteredEntries = _filteredEntries.size();
		String feedback = String.format(MESSAGE_FILTER_RESULTS, numOfFilteredEntries);
		if (numOfFilteredEntries == 1) {
			feedback = feedback.replace("entries", "entry");
		}
		
		return feedback;
		
	}
}