package com.taskboard.main;

import java.util.Comparator;
import java.util.Date;

public class DateComparator implements Comparator<Entry>{
	private DateTimeValidator dateTimeValidator = new DateTimeValidator();
	
	public int compare(Entry entry1, Entry entry2) {
		Date date1 = getDateObject(entry1);
		Date date2 = getDateObject(entry2);
		return date1.compareTo(date2);
	}

	public Date getDateObject(Entry entry) {
		if (entry.getDateParameter() != null) {
			String date = entry.getDateParameter().getParameterValue();
			String time = new String();
			if (entry.getTimeParameter().getParameterValue() != null) {
				time = entry.getTimeParameter().getParameterValue();
			}
			String dateTime = dateTimeValidator.getDateTimeFormat(date, time);
			return dateTimeValidator.getInputDate(dateTime);
		} else {
			String date = entry.getStartDateParameter().getParameterValue();
			String time = new String();
			if (entry.getTimeParameter().getParameterValue() != null) {
				time = entry.getTimeParameter().getParameterValue();
			}			
			String dateTime = dateTimeValidator.getDateTimeFormat(date, time);
			return dateTimeValidator.getInputDate(dateTime);
		}	
	}
}
