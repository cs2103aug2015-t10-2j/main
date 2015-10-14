package com.taskboard.main;

import java.text.SimpleDateFormat;
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
				if (token.length() == 10 && token.indexOf('/') == 2 && token.lastIndexOf('/') == 5) {
					return true;
				} else {
					return false;
				}
		}
	}
	
	public String toDefaultFormat(String token) {
		SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
				if (token.length() == 10 && token.indexOf('/') == 2 && token.lastIndexOf('/') == 5) {
					return token;
				} else {
					return null;
				}
		}
	}
	
	private static Date getNextOccurenceDate(int dayIndex) {
		SimpleDateFormat dayIndexFormat = new SimpleDateFormat("u");
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
	
}
