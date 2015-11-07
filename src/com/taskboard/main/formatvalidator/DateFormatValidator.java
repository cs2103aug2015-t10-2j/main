//@@author A0126536E
package com.taskboard.main.formatvalidator;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.Calendar;
import java.util.Date;

public class DateFormatValidator implements FormatValidator {
	
	// constants
	
	public static final int MILLISECONDS_PER_DAY = 86400000;
	public static final int DAY_INDEX_MONDAY = 1;
	public static final int DAY_INDEX_TUESDAY = 2;
	public static final int DAY_INDEX_WEDNESDAY = 3;
	public static final int DAY_INDEX_THURSDAY = 4;
	public static final int DAY_INDEX_FRIDAY = 5;
	public static final int DAY_INDEX_SATURDAY = 6;
	public static final int DAY_INDEX_SUNDAY = 7;
	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	
	// constructors
	
	public DateFormatValidator() {
		
	}
	
	// functionalities
	
	public boolean isValidFormat(String token) {
		token = token.toLowerCase();
		switch (token) {
			case "tda":
			case "today":
			case "tomo":
			case "tomorrow":
			case "mon":
			case "monday":
			case "tue":
			case "tuesday":
			case "wed":
			case "wednesday":
			case "thu":
			case "thursday":
			case "fri":
			case "friday":
			case "sat":
			case "saturday":
			case "sun":
			case "sunday":
				return true;
			default:
				if (isMatchingDateFormat(new SimpleDateFormat("d/MM/yyyy"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd/MM/yyyy"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("d-MM-yyyy"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd-MM-yyyy"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("d/MM"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd/MM"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("d-MM"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd-MM"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("d MMM"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd MMM"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("d MMMMM"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd MMMMM"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("d MMM yyyy"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd MMM yyyy"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("d MMMMM yyyy"), token)) {
					return true;
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd MMMMM yyyy"), token)) {
					return true;
				} else {
					return false;
				}
		}
	}
	
	public String toDefaultFormat(String token) {
		SimpleDateFormat defaultDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		defaultDateFormat.setLenient(false);
		Date today = new Date();
		
		token = token.toLowerCase();
		switch (token) {
			case "tda":
			case "today":
				return defaultDateFormat.format(today);
			case "tomo":
			case "tomorrow":
				Date tomorrow = new Date(today.getTime() + MILLISECONDS_PER_DAY);
				return defaultDateFormat.format(tomorrow);
			case "mon":
			case "monday":
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_MONDAY));
			case "tue":
			case "tuesday":
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_TUESDAY));
			case "wed":
			case "wednesday":
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_WEDNESDAY));
			case "thu":
			case "thursday":
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_THURSDAY));
			case "fri":
			case "friday":
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_FRIDAY));
			case "sat":
			case "saturday":
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_SATURDAY));
			case "sun":
			case "sunday":
				return defaultDateFormat.format(getNextOccurenceDate(DAY_INDEX_SUNDAY));
			default:
				if (isMatchingDateFormat(new SimpleDateFormat("d/MM/yyyy"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("d/MM/yyyy"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd/MM/yyyy"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("dd/MM/yyyy"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("d-MM-yyyy"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("d-MM-yyyy"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd-MM-yyyy"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("dd-MM-yyyy"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("d/MM"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("d/MM"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd/MM"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("dd/MM"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("d-MM"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("d-MM"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd-MM"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("dd-MM"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("d MMM"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("d MMM"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd MMM"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("dd MMM"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("d MMMMM"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("d MMMMM"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd MMMMM"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("dd MMMMM"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("d MMM yyyy"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("d MMM yyyy"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd MMM yyyy"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("dd MMM yyyy"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("d MMMMM yyyy"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("d MMMMM yyyy"), token);
				} else if (isMatchingDateFormat(new SimpleDateFormat("dd MMMMM yyyy"), token)) {
					return toStandardDateFormat(new SimpleDateFormat("dd MMMMM yyyy"), token);
				} else {
					return null;
				}
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
