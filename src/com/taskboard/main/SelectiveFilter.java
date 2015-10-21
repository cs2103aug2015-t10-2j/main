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
	
	public ArrayList<Entry> filterByDate(ArrayList<Entry> entries, String searchDate) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			Parameter dateParameter = entry.getDateParameter();
			Parameter startDateParameter = entry.getStartDateParameter();
			
			boolean hasDeadlineDateMatched = hasEntryDateMatched(dateParameter, searchDate); 
		    boolean hasEventDateMatched = hasEntryDateMatched(startDateParameter, searchDate); 
		    
		    if (hasDeadlineDateMatched || hasEventDateMatched) {
		    	filteredEntries.add(entry);
		    }
		} 
		
		return filteredEntries;
	}
	
	private boolean hasEntryDateMatched(Parameter dateParameter, String referenceDate) {
		if (dateParameter != null) {
			String date = dateParameter.getParameterValue();
			
			if (date.equals(referenceDate)) {
				return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<Entry> filterByDateTime(ArrayList<Entry> entries, Date inputDate) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			Parameter dateParameter = entry.getDateParameter();
			Parameter timeParameter = entry.getTimeParameter();
			Parameter startDateParameter = entry.getStartDateParameter();
			Parameter startTimeParameter = entry.getStartTimeParameter();
			
			boolean hasDeadlineDateTimeMatched = hasEntryDateTimeMatched(dateParameter, timeParameter, inputDate);
			boolean hasEventDateTimeMatched = hasEntryDateTimeMatched(startDateParameter, startTimeParameter, 
					                                                  inputDate);
			
			if (hasDeadlineDateTimeMatched || hasEventDateTimeMatched) {
				filteredEntries.add(entry);
			}
		}
		
		return filteredEntries;
	}
	
	private boolean hasEntryDateTimeMatched(Parameter dateParameter, Parameter timeParameter, Date referenceDate) {
		if (dateParameter != null && timeParameter != null) {
			String date = dateParameter.getParameterValue();
			String time = timeParameter.getParameterValue();
		
			Date entryDate = retrieveDateFromDateTimeDetails(date, time);
		
			if (entryDate.equals(referenceDate)) {
				return true;
			}
		}
		
		return false;
	}
		
	private Date retrieveDateFromDateTimeDetails(String date, String time) {
		DateTimeValidator dateTimeValidator = new DateTimeValidator();
		dateTimeValidator.validateDateTimeDetails(date, time, null);
		Date convertedDate = dateTimeValidator.getDate();
		
		return convertedDate;
	}
	
	public ArrayList<Entry> filterByDateTimeRange(ArrayList<Entry> entries, Date inputStartDate, Date inputEndDate) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			Parameter dateParameter = entry.getDateParameter();
			Parameter timeParameter = entry.getTimeParameter();
			Parameter startDateParameter = entry.getStartDateParameter();
			Parameter startTimeParameter = entry.getStartTimeParameter();
			
			boolean isDeadlineDateTimeInRange = isEntryDateTimeInRange(dateParameter, timeParameter, inputStartDate, 
					                                                   inputEndDate);
			boolean isEventDateTimeInRange = isEntryDateTimeInRange(startDateParameter, startTimeParameter,  
					                                                inputStartDate,  inputEndDate);
			
			if (isDeadlineDateTimeInRange || isEventDateTimeInRange) {
				filteredEntries.add(entry);
			}
		}
		
		return filteredEntries;
	}
		
	private boolean isEntryDateTimeInRange(Parameter dateParameter, Parameter timeParameter, Date referenceStartDate,
			                               Date referenceEndDate) {
		if (dateParameter != null) {
			String date = dateParameter.getParameterValue();
			String time = "";
			
			if (timeParameter != null) {
				time = timeParameter.getParameterValue();
			}
			
			Date deadlineDate = retrieveDateFromDateTimeDetails(date, time);
			int startDateIndicator = deadlineDate.compareTo(referenceStartDate);
			int endDateIndicator = deadlineDate.compareTo(referenceEndDate);
			
			if (startDateIndicator >= 0 && endDateIndicator <= 0) {
				return true;
			}
		}
		
		return false;
	}
}