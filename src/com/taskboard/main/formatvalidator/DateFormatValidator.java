//@@author A0126536E
package com.taskboard.main.formatvalidator;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.Calendar;
import java.util.Date;

/**
 * This class validates a String if it's a valid date format 
 * and converts it to the default date format dd/MM/yyyy.
 * @author Alvian Prasetya
 */
public class DateFormatValidator implements FormatValidator {
	
	// constants
	
	private static final int MILLISECONDS_PER_DAY = 86400000;
	private static final int DAY_INDEX_MONDAY = 1;
	private static final int DAY_INDEX_TUESDAY = 2;
	private static final int DAY_INDEX_WEDNESDAY = 3;
	private static final int DAY_INDEX_THURSDAY = 4;
	private static final int DAY_INDEX_FRIDAY = 5;
	private static final int DAY_INDEX_SATURDAY = 6;
	private static final int DAY_INDEX_SUNDAY = 7;
	private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	// The list of supported date formats.
	private static final String[] DATE_FORMATS = {"d/MM/yyyy", 
												 "d/M/yyyy", 
												 "dd/MM/yyyy", 
												 "dd/M/yyyy", 
												 "d-MM-yyyy", 
												 "d-M-yyyy", 
												 "dd-MM-yyyy", 
												 "dd-M-yyyy", 
												 "d/MM", 
												 "d/M", 
												 "dd/MM", 
												 "dd/M", 
												 "d-MM", 
												 "d-M", 
												 "dd-MM", 
												 "dd-M", 
												 "d MMM", 
												 "dd MMM", 
												 "d MMMMM", 
												 "dd MMMMM", 
												 "d MMM yyyy", 
												 "dd MMM yyyy", 
												 "d MMMMM yyyy", 
												 "dd MMMMM yyyy"};
	
	// constructors
	
	public DateFormatValidator() {
		
	}
	
	// functionalities
	
	public boolean isValidFormat(String token) {
		token = token.toLowerCase();
		switch (token) {
			case "tda" :
				// Fallthrough.
			case "today" :
				// Fallthrough.
			case "tomo" :
				// Fallthrough.
			case "tomorrow" :
				// Fallthrough.
			case "mon" :
				// Fallthrough.
			case "monday" :
				// Fallthrough.
			case "tue" :
				// Fallthrough.
			case "tuesday" :
				// Fallthrough.
			case "wed" :
				// Fallthrough.
			case "wednesday" :
				// Fallthrough.
			case "thu" :
				// Fallthrough.
			case "thursday" :
				// Fallthrough.
			case "fri" :
				// Fallthrough.
			case "friday" :
				// Fallthrough.
			case "sat" :
				// Fallthrough.
			case "saturday" :
				// Fallthrough.
			case "sun" :
				// Fallthrough.
			case "sunday" :
				return true;
			default :
				for (String currentDateFormat: DATE_FORMATS) {
					if (isMatchingDateFormat(new SimpleDateFormat(currentDateFormat), token)) {
						return true;
					}
				}
				return false;
		}
	}
	
	public String toDefaultFormat(String token) {
		SimpleDateFormat defaultDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		defaultDateFormat.setLenient(false);
		Date today = new Date();
		
		token = token.toLowerCase();
		switch (token) {
			case "tda" :
				// Fallthrough.
			case "today" :
				return defaultDateFormat.format(today);
			case "tomo" :
				// Fallthrough.
			case "tomorrow" :
				Date tomorrow = new Date(today.getTime() + MILLISECONDS_PER_DAY);
				return defaultDateFormat.format(tomorrow);
			case "mon" :
				// Fallthrough.
			case "monday" :
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_MONDAY));
			case "tue" :
				// Fallthrough.
			case "tuesday" :
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_TUESDAY));
			case "wed" :
				// Fallthrough.
			case "wednesday" :
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_WEDNESDAY));
			case "thu" :
				// Fallthrough.
			case "thursday" :
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_THURSDAY));
			case "fri" :
				// Fallthrough.
			case "friday" :
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_FRIDAY));
			case "sat" :
				// Fallthrough.
			case "saturday" :
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_SATURDAY));
			case "sun" :
				// Fallthrough.
			case "sunday" :
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_SUNDAY));
			default :
				for (String currentDateFormat: DATE_FORMATS) {
					if (isMatchingDateFormat(new SimpleDateFormat(currentDateFormat), token)) {
						return toStandardDateFormat(new SimpleDateFormat(currentDateFormat), token);
					}
				}
				return null;
		}
	}
	
	private static Date getNextOccurenceDate(int dayIndex) {
		SimpleDateFormat dayIndexFormat = new SimpleDateFormat("u");
		dayIndexFormat.setLenient(false);
		Date today = new Date();
		int todayDayIndex = Integer.parseInt(dayIndexFormat.format(today));
		
		if (todayDayIndex < dayIndex) {
			Date target = new Date(today.getTime() + MILLISECONDS_PER_DAY * 
						  (dayIndex - todayDayIndex));
			assert (!today.after(target)); // today must be either before or the same day as target
			return target;
		} else {
			Date target = new Date(today.getTime() + MILLISECONDS_PER_DAY * 
					  	  (dayIndex - todayDayIndex + 7));
			assert (!today.after(target)); // today must be either before or the same day as target
			return target;
		}
	}
	
	private static boolean isMatchingDateFormat(SimpleDateFormat dateFormat, String input) {
		dateFormat.setLenient(false);
		try {
			String output = dateFormat.format(dateFormat.parse(input));
			if (output.equalsIgnoreCase(input)) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			return false;
		}
	}
	
	private static String toStandardDateFormat(SimpleDateFormat dateFormat, String input) {
		SimpleDateFormat defaultDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		defaultDateFormat.setLenient(false);
		try {
			if (!dateFormat.toPattern().contains("y")) {
				Calendar currentDate = Calendar.getInstance();
				Calendar parsedDate = Calendar.getInstance();
				
				parsedDate.setTime(dateFormat.parse(input));
				parsedDate.set(Calendar.HOUR, 23);
				parsedDate.set(Calendar.MINUTE, 59);
				parsedDate.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
				
				if (parsedDate.before(currentDate)) {
					parsedDate.set(Calendar.YEAR, currentDate.get(Calendar.YEAR) + 1);
				}
				
				String output = defaultDateFormat.format(parsedDate.getTime());
				return output;
			} else {
				String output = defaultDateFormat.format(dateFormat.parse(input));
				return output;
			}
		} catch (ParseException e) {
			assert false: "Code should not be reached.";
			return null;
		}
	}
	
}
