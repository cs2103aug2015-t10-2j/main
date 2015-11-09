//@@author A0129889A 
package com.taskboard.main.comparator;

import java.util.Comparator;
import java.util.Date;

import com.taskboard.main.DateTimeValidator;
import com.taskboard.main.util.Entry;

// This class will be used to compare two entry based on date.
public class DateComparatorForArchive implements Comparator<Entry>{
	private DateTimeValidator dateTimeValidator = new DateTimeValidator();

	public int compare(Entry entry1, Entry entry2) {
		Date date1 = getDateObject(entry1);
		Date date2 = getDateObject(entry2);
		return date2.compareTo(date1);
	}

	private Date getDateObject(Entry entry) {
		if (entry.getDateParameter() != null) {
			String date = entry.getDateParameter().getParameterValue();
			String time = new String();
			if (entry.getTimeParameter() != null) {
				time = entry.getTimeParameter().getParameterValue();
			} else {
				time = "00:00";
			}
			dateTimeValidator.validateDateTimeDetails(date, time, null);
			return dateTimeValidator.getDate();
		} else if (entry.getStartDateParameter() != null){
			String date = entry.getStartDateParameter().getParameterValue();
			String time = new String();
			if (entry.getStartTimeParameter() != null) {
				time = entry.getStartTimeParameter().getParameterValue();
			} else {
				time = "00:00";
			}		
			dateTimeValidator.validateDateTimeDetails(date, time, null);
			return dateTimeValidator.getDate();
		} else {
			String date = "31/12/1990";
			String time = "23:59";
			dateTimeValidator.validateDateTimeDetails(date, time, null);
			return dateTimeValidator.getDate();
		}
	}
}
