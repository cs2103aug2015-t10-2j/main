package com.taskboard.main;

import java.util.ArrayList;
import java.util.Date;

public class SelectiveFilter {

	public SelectiveFilter() {
		
	}
	
	public ArrayList<Entry> filterByName(ArrayList<Entry> entries, String searchKey) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			String entryName = entry.getNameParameter().getParameterValue();
			
			if (entryName.contains(searchKey)) {
				filteredEntries.add(entry);
			}
		}
		
		return filteredEntries;
	}
	
	public ArrayList<Entry> filterByPriority(ArrayList<Entry> entries, String searchPriority) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			Parameter priorityParameter = entry.getPriorityParameter();
			
			if (priorityParameter != null) {
				String priority = priorityParameter.getParameterValue();
				
				if (priority.equals(searchPriority)) {
					filteredEntries.add(entry);
				}
			}
		} 
		
		return filteredEntries;
	}
	
	public ArrayList<Entry> filterByCategory(ArrayList<Entry> entries, String searchCategory) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			Parameter categoryParameter = entry.getCategoryParameter();
			
			if (categoryParameter != null) {
				String category = categoryParameter.getParameterValue();
				
				if (category.equals(searchCategory)) {
					filteredEntries.add(entry);
				}
			}
		} 
		
		return filteredEntries;
	}
	
//	public ArrayList<Entry> filterByDate(ArrayList<Entry> entries, String searchDate) {
//		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
//		
//		for (int i = 0; i < entries.size(); i++) {
//			Entry entry = entries.get(i);
//			Parameter dateParameter = entry.getDateParameter();
//			Parameter startDateParameter = entry.getStartDateParameter();
//			
//			boolean hasDeadlineDateMatched = hasEntryDateMatched(dateParameter, searchDate); 
//		    boolean hasEventDateMatched = hasEntryDateMatched(startDateParameter, searchDate); 
//		    
//		    if (hasDeadlineDateMatched || hasEventDateMatched) {
//		    	filteredEntries.add(entry);
//		    }
//		} 
//		
//		return filteredEntries;
//	}
//	
//	private boolean hasEntryDateMatched(Parameter dateParameter, String referenceDate) {
//		if (dateParameter != null) {
//			String date = dateParameter.getParameterValue();
//			
//			if (date.equals(referenceDate)) {
//				return true;
//			}
//		}
//		
//		return false;
//	}
	
	public ArrayList<Entry> filterByDateTime(ArrayList<Entry> entries, Date inputDate) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			Parameter dateParameter = entry.getDateParameter();
			Parameter timeParameter = entry.getTimeParameter();
			Parameter startDateParameter = entry.getStartDateParameter();
			Parameter startTimeParameter = entry.getStartTimeParameter();
			Parameter endDateParameter = entry.getEndDateParameter();
			Parameter endTimeParameter = entry.getEndTimeParameter();
			
			boolean hasDeadlineDateTimeMatched = false;
			boolean hasEventDateTimeMatched = false;
			
			if (dateParameter != null) {
				hasDeadlineDateTimeMatched = hasDeadlineDateTimeMatched(dateParameter, timeParameter, 
						                                                inputDate); 
						
			} else if (startDateParameter != null) {
				hasEventDateTimeMatched = hasEventDateTimeMatched(startDateParameter, startTimeParameter, 
						                                          endDateParameter, endTimeParameter, 
						                                          inputDate); 
						                                         
			}
			
			if (hasDeadlineDateTimeMatched || hasEventDateTimeMatched) {
				filteredEntries.add(entry);
			}
		}
		
		return filteredEntries;
	}
	
	private boolean hasDeadlineDateTimeMatched(Parameter dateParameter, Parameter timeParameter, 
			                                   Date referenceDate) {
		String date = dateParameter.getParameterValue();
		String time = "";
			
		if (timeParameter != null) {
			time = timeParameter.getParameterValue();
		}
		
		Date deadlineDate = retrieveDateFromDateTimeDetails(date, time);
		
		if (deadlineDate.equals(referenceDate)) {
			return true;
		}
	
		return false;
	}
			
	private Date retrieveDateFromDateTimeDetails(String date, String time) {
		DateTimeValidator dateTimeValidator = new DateTimeValidator();
		dateTimeValidator.validateDateTimeDetails(date, time, null);
		Date convertedDate = dateTimeValidator.getDate();
		
		return convertedDate;
	}
	
	private boolean hasEventDateTimeMatched(Parameter startDateParameter, Parameter startTimeParameter,
			                                Parameter endDateParameter, Parameter endTimeParameter, 
			                                Date referenceDate) {
		String startDate = startDateParameter.getParameterValue();
		String startTime = "";
		
		if (startTimeParameter != null) {
			startTime = startTimeParameter.getParameterValue();
		}
		
		String endDate = endDateParameter.getParameterValue();
		String endTime = "";
		
		if (endTimeParameter != null) {
			endTime = endTimeParameter.getParameterValue();
		}
		
		Date eventStartDate = retrieveDateFromDateTimeDetails(startDate, startTime);
		Date eventEndDate = retrieveDateFromDateTimeDetails(endDate, endTime);
		int referenceDateIndicatorForEventStartDate = eventStartDate.compareTo(referenceDate);
		int referenceDateIndicatorForEventEndDate = eventEndDate.compareTo(referenceDate);
		
		if (referenceDateIndicatorForEventStartDate <= 0 && referenceDateIndicatorForEventEndDate >= 0) {
			return true;
		}
		
		return false;
	}
	
	public ArrayList<Entry> filterByDateTimeRange(ArrayList<Entry> entries, Date inputStartDate, 
			                                      Date inputEndDate) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			Parameter dateParameter = entry.getDateParameter();
			Parameter timeParameter = entry.getTimeParameter();
			Parameter startDateParameter = entry.getStartDateParameter();
			Parameter startTimeParameter = entry.getStartTimeParameter();
			Parameter endDateParameter = entry.getEndDateParameter();
			Parameter endTimeParameter = entry.getEndTimeParameter();
			
			boolean isDeadlineDateTimeInRange = false;
			boolean isEventDateTimeInRange = false;
			
			if (dateParameter != null) {
				isDeadlineDateTimeInRange = isDeadlineDateTimeInRange(dateParameter, timeParameter,  
						                                              inputStartDate, inputEndDate);
			} else if (startDateParameter != null) {
				isEventDateTimeInRange = isEventDateTimeInRange(startDateParameter, startTimeParameter,  
				                                                endDateParameter, endTimeParameter,
				                                                inputStartDate,  inputEndDate);
			}
			
			if (isDeadlineDateTimeInRange || isEventDateTimeInRange) {
				filteredEntries.add(entry);
			}
		}
		
		return filteredEntries;
	}
		
	private boolean isDeadlineDateTimeInRange(Parameter dateParameter, Parameter timeParameter, 
			                                  Date referenceStartDate, Date referenceEndDate) {
		String date = dateParameter.getParameterValue();
		String time = "";
			
		if (timeParameter != null) {
			time = timeParameter.getParameterValue();
		}
			
		Date deadlineDate = retrieveDateFromDateTimeDetails(date, time);
		int referenceStartDateIndicator = deadlineDate.compareTo(referenceStartDate);
		int referenceEndDateIndicator = deadlineDate.compareTo(referenceEndDate);
			
		if (referenceStartDateIndicator >= 0 && referenceEndDateIndicator <= 0) {
			return true;
		}
	
		return false;
	}
	
	private boolean isEventDateTimeInRange(Parameter startDateParameter, Parameter startTimeParameter,
			                               Parameter endDateParameter, Parameter endTimeParameter, 
			                               Date referenceStartDate, Date referenceEndDate) {
		String startDate = startDateParameter.getParameterValue();
		String startTime = "";
		
		if (startTimeParameter != null) {
			startTime = startTimeParameter.getParameterValue();
		}
		
		String endDate = endDateParameter.getParameterValue();
		String endTime = "";
		
		if (endTimeParameter != null) {
			endTime = endTimeParameter.getParameterValue();
		}
		
		Date eventStartDate = retrieveDateFromDateTimeDetails(startDate, startTime);
		int referenceStartDateIndicatorForEventStartDate = eventStartDate.compareTo(referenceStartDate);
		int referenceEndDateIndicatorForEventStartDate = eventStartDate.compareTo(referenceEndDate);
		boolean isEventStartDateInSearchRange = referenceStartDateIndicatorForEventStartDate >= 0 && 
				                                referenceEndDateIndicatorForEventStartDate <= 0;
		
		Date eventEndDate = retrieveDateFromDateTimeDetails(endDate, endTime);
		int referenceStartDateIndicatorForEventEndDate = eventEndDate.compareTo(referenceStartDate);
		int referenceEndDateIndicatorForEventEndDate = eventEndDate.compareTo(referenceEndDate);
		boolean isEventEndDateInSearchRange = referenceStartDateIndicatorForEventEndDate >= 0 && 
				                              referenceEndDateIndicatorForEventEndDate <= 0;
		
		boolean isSearchRangeInEventRange = referenceStartDateIndicatorForEventStartDate < 0 && 
			                            	referenceEndDateIndicatorForEventEndDate > 0;
		
		if (isEventStartDateInSearchRange || isEventEndDateInSearchRange || isSearchRangeInEventRange) {
			return true;
		}
		
		
		return false;
	}
}