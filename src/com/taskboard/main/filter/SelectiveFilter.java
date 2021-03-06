//@@author A0123935E
package com.taskboard.main.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.taskboard.main.logger.GlobalLogger;
import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.validator.DateTimeValidator;

/**
 * This class contains the different filters and executes each of them when invoked. 
 * It returns the filtered entries as a result.
 * @author Amarparkash Singh Mavi
 *
 */
public class SelectiveFilter {
	
	// These are the time formats assigned for the respective entries to facilitate filtering by date only
	private static final String FORMAT_DEADLINE_TASK_DEFAULT_TIME_FOR_FILTER_BY_DATE = "00:00";
	private static final String FORMAT_EVENT_DEFAULT_START_TIME_FOR_FILTER_BY_DATE = "00:00";
	private static final String FORMAT_EVENT_DEFAULT_END_TIME_FOR_FILTER_BY_DATE = "23:59";
	
	// attribute
	
	private Logger _logger;

	// constructor
	
	public SelectiveFilter() {
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	/** 
	 * This method executes filtering of entries by entry name and returns the filtered entries.
	 * 
	 * @param entries   Entries used for the filtering
	 * @param searchKey Substring used to filter entries by entry name
	 * @return			Filtered entries 
	 */
	public ArrayList<Entry> filterByName(ArrayList<Entry> entries, String searchKey) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			String entryName = entry.getNameParameter().getParameterValue();
			
			// performing case-insensitive search
			if (entryName.toLowerCase().contains(searchKey.toLowerCase())) {
				filteredEntries.add(entry);
				_logger.log(Level.INFO, "Successfully filtered entry by name: " + entryName);
			}
		}
		
		return filteredEntries;
	}
	
	/**
	 * This method executes filtering of entries by priority and returns the filtered entries.
	 * 
	 * @param entries		 Entries used for the filtering
	 * @param searchPriority Priority level used to filter entries by priority
	 * @return               Filtered Entries
	 */
	public ArrayList<Entry> filterByPriority(ArrayList<Entry> entries, String searchPriority) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			Parameter priorityParameter = entry.getPriorityParameter();
			
			if (priorityParameter != null) {
				String priority = priorityParameter.getParameterValue();
				if (priority.equals(searchPriority)) {
					filteredEntries.add(entry);
					_logger.log(Level.INFO, "Successfully filtered entry by priority: " 
					            + entry.getNameParameter().getParameterValue());
				}
			}
		} 
		
		return filteredEntries;
	}
	
	/**
	 * This method executes filtering of entries by category and returns the filtered entries.
	 * 
	 * @param entries        Entries used for the filtering
	 * @param searchCategory Category name used to filter entries by category
	 * @return				 Filtered Entries
	 */
	public ArrayList<Entry> filterByCategory(ArrayList<Entry> entries, String searchCategory) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			Parameter categoryParameter = entry.getCategoryParameter();
			
			if (categoryParameter != null) {
				String category = categoryParameter.getParameterValue();
				// performing case-insensitive search
				if (category.equalsIgnoreCase(searchCategory)) {
					filteredEntries.add(entry);
					_logger.log(Level.INFO, "Successfully filtered entry by category: " 
				                + entry.getNameParameter().getParameterValue());
				}
			}
		} 
		
		return filteredEntries;
	}
	
	/**
	 * This method executes filtering of entries by date only and returns the filtered entries.
	 *  
	 * @param entries   Entries used for the filtering
	 * @param inputDate Date used to filter entries by date
	 * @return			Filtered entries
	 */
	public ArrayList<Entry> filterByDate(ArrayList<Entry> entries, Date inputDate) {
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			Parameter dateParameter = entry.getDateParameter();
			Parameter startDateParameter = entry.getStartDateParameter();
			Parameter endDateParameter = entry.getEndDateParameter();
			
			boolean hasDeadlineDateMatched = false;
			boolean hasEventDateMatched = false;
			if (dateParameter != null) {
				_logger.log(Level.INFO, "Start checking whether to filter deadline task: "
					        + entry.getNameParameter().getParameterValue());
				hasDeadlineDateMatched = hasDeadlineDateMatched(dateParameter, inputDate);
			} else if (startDateParameter != null) {
				_logger.log(Level.INFO, "Start checking whether to filter event: "
					        + entry.getNameParameter().getParameterValue());
			    hasEventDateMatched = hasEventDateMatched(startDateParameter, endDateParameter,  
					                                      inputDate); 		
			}
			
			if (hasDeadlineDateMatched || hasEventDateMatched) {
				filteredEntries.add(entry);
				_logger.log(Level.INFO, "Successfully filtered entry by date: " 
		                    + entry.getNameParameter().getParameterValue());
			}
		}
			
		return filteredEntries;
	}
		
	private boolean hasDeadlineDateMatched(Parameter dateParameter, Date referenceDate) {
		String date = dateParameter.getParameterValue();
		String time = FORMAT_DEADLINE_TASK_DEFAULT_TIME_FOR_FILTER_BY_DATE;
		Date deadlineDate = retrieveDateFromDateTimeDetails(date, time);
		if (deadlineDate.equals(referenceDate)) {
			_logger.log(Level.INFO, "Deadline task successfully satisfied filter by date");
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
	
	private boolean hasEventDateMatched(Parameter startDateParameter, Parameter endDateParameter,
			                            Date referenceDate) {
		String startDate = startDateParameter.getParameterValue();
		String startTime = FORMAT_EVENT_DEFAULT_START_TIME_FOR_FILTER_BY_DATE;
		String endDate = endDateParameter.getParameterValue();
		String endTime = FORMAT_EVENT_DEFAULT_END_TIME_FOR_FILTER_BY_DATE;
		
		Date eventStartDate = retrieveDateFromDateTimeDetails(startDate, startTime);
		int referenceDateIndicatorForEventStartDate = eventStartDate.compareTo(referenceDate);
		Date eventEndDate = retrieveDateFromDateTimeDetails(endDate, endTime);
		int referenceDateIndicatorForEventEndDate = eventEndDate.compareTo(referenceDate);
		if (referenceDateIndicatorForEventStartDate <= 0 && referenceDateIndicatorForEventEndDate >= 0) {
			_logger.log(Level.INFO, "Event successfully satisfied filter by date");
			return true;
		}
		
		return false;
		
	}
	
	/**
	 * This method executes filtering of entries by date time and returns the filtered entries.
	 * 
	 * @param entries   Entries used for the filtering
	 * @param inputDate Date used to filter entries by date time
	 * @return			Filtered entries
	 */
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
				_logger.log(Level.INFO, "Start checking whether to filter deadline task: "
						    + entry.getNameParameter().getParameterValue());
				hasDeadlineDateTimeMatched = hasDeadlineDateTimeMatched(dateParameter, timeParameter, 
						                                                inputDate); 
			} else if (startDateParameter != null) {
				_logger.log(Level.INFO, "Start checking whether to filter event: "
						    + entry.getNameParameter().getParameterValue());
				hasEventDateTimeMatched = hasEventDateTimeMatched(startDateParameter, startTimeParameter, 
						                                          endDateParameter, endTimeParameter, 
						                                          inputDate); 				                                         
			}
			
			if (hasDeadlineDateTimeMatched || hasEventDateTimeMatched) {
				filteredEntries.add(entry);
				_logger.log(Level.INFO, "Successfully filtered entry by date time: " 
		                    + entry.getNameParameter().getParameterValue());
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
			_logger.log(Level.INFO, "Deadline task successfully satisfied filter by date time");
			return true;
		}
	
		return false;
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
		int referenceDateIndicatorForEventStartDate = eventStartDate.compareTo(referenceDate);
		Date eventEndDate = retrieveDateFromDateTimeDetails(endDate, endTime);
		int referenceDateIndicatorForEventEndDate = eventEndDate.compareTo(referenceDate);
		if (referenceDateIndicatorForEventStartDate <= 0 && referenceDateIndicatorForEventEndDate >= 0) {
			_logger.log(Level.INFO, "Event successfully satisfied filter by date time");
			return true;
		}
		
		return false;
	}
	
	/**
	 * This method executes filtering of entries by date time range and returns the filtered entries.
	 * 
	 * @param entries        Entries used for the filtering
	 * @param inputStartDate Start date of date time range
	 * @param inputEndDate   End date of date time range
	 * @return				 Filtered entries
	 */
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
				_logger.log(Level.INFO, "Start checking whether to filter deadline task: "
					        + entry.getNameParameter().getParameterValue());
				isDeadlineDateTimeInRange = isDeadlineDateTimeInRange(dateParameter, timeParameter,  
						                                              inputStartDate, inputEndDate);
			} else if (startDateParameter != null) {
				_logger.log(Level.INFO, "Start checking whether to filter event: "
					        + entry.getNameParameter().getParameterValue());
				isEventDateTimeInRange = isEventDateTimeInRange(startDateParameter, startTimeParameter,  
				                                                endDateParameter, endTimeParameter,
				                                                inputStartDate,  inputEndDate);
			}
			
			if (isDeadlineDateTimeInRange || isEventDateTimeInRange) {
				filteredEntries.add(entry);
				_logger.log(Level.INFO, "Successfully filtered entry by date time range: " 
	                        + entry.getNameParameter().getParameterValue());
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
			_logger.log(Level.INFO, "Deadline task successfully satisfied filter by date time range");
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
			_logger.log(Level.INFO, "Event successfully satisfied filter by date time range");
			return true;
		}
		
		return false;
	}
}