package com.taskboard.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;

import java.text.SimpleDateFormat;

import com.taskboard.main.CommandType;
import com.taskboard.main.Parameter;
import com.taskboard.main.ParameterType;
import com.taskboard.main.CommandTypeParser;
import com.taskboard.main.AddParameterParser;
import com.taskboard.main.EditParameterParser;
import com.taskboard.main.DeleteParameterParser;
import com.taskboard.main.ViewParameterParser;
import com.taskboard.main.DateFormatValidator;

public class TaskBoardTest {
	
	@Test
	public void testCommandTypeParser() {
		assertCommandType(CommandType.ADD, "Add Hello; by tomorrow 10pm");
		assertCommandType(CommandType.EDIT, "edit Hello; by today");
		assertCommandType(CommandType.DELETE, "deLeTe Hello");
		assertCommandType(CommandType.VIEW, "view from tomorrow 5pm to tomorrow 9pm");
		assertCommandType(CommandType.UNKNOWN, "tHrow");
	}
	
	private void assertCommandType(CommandType expected, String command) {
		assertEquals(expected, new CommandTypeParser().parseCommandType(command)); 
	}
	
	@Test
	public void TestParameterParser() {
		ArrayList<Parameter> expected1 = new ArrayList<Parameter>();
		expected1.add(new Parameter(ParameterType.NAME, "Hello World!"));
		Collections.reverse(expected1);
		assertAddParameters(expected1, "add Hello World!");
		
		ArrayList<Parameter> expected2 = new ArrayList<Parameter>();
		expected2.add(new Parameter(ParameterType.NAME, "Hello again!"));
		expected2.add(new Parameter(ParameterType.DATE, "12/10/2020"));
		Collections.reverse(expected2);
		assertAddParameters(expected2, "add Hello again! by 12/10/2020");
		
		ArrayList<Parameter> expected3 = new ArrayList<Parameter>();
		expected3.add(new Parameter(ParameterType.NAME, "Meeting with Chris"));
		SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
		expected3.add(new Parameter(ParameterType.START_DATE, defaultDateFormat.format(tomorrow)));
		expected3.add(new Parameter(ParameterType.START_TIME, "19:00"));
		expected3.add(new Parameter(ParameterType.END_DATE, defaultDateFormat.format(tomorrow)));
		expected3.add(new Parameter(ParameterType.END_TIME, "21:00"));
		Collections.reverse(expected3);
		assertAddParameters(expected3, "add Meeting with Chris from tomorrow 7pm to tomorrow 9pm");
		
		ArrayList<Parameter> expected4 = new ArrayList<Parameter>();
		expected4.add(new Parameter(ParameterType.INDEX, "4"));
		SimpleDateFormat dayIndexFormat = new SimpleDateFormat("u");
		int todayDayIndex = Integer.parseInt(dayIndexFormat.format(today));
		Date monday = new Date(today.getTime() + DateFormatValidator.MILLISECONDS_PER_DAY * 
					  (DateFormatValidator.DAY_INDEX_MONDAY - todayDayIndex + 
					  ((todayDayIndex < DateFormatValidator.DAY_INDEX_MONDAY) ? 0 : 7)));
		expected4.add(new Parameter(ParameterType.DATE, defaultDateFormat.format(monday)));
		expected4.add(new Parameter(ParameterType.TIME, "23:59"));
		Collections.reverse(expected4);
		assertEditParameters(expected4, "edit 4 by monday 23:59");
		
		ArrayList<Parameter> expected5 = new ArrayList<Parameter>();
		expected5.add(new Parameter(ParameterType.INDEX, "12"));
		expected5.add(new Parameter(ParameterType.DATE, "13/10/2020"));
		expected5.add(new Parameter(ParameterType.TIME, "14:00"));
		expected5.add(new Parameter(ParameterType.PRIORITY, "medium"));
		Collections.reverse(expected5);
		assertEditParameters(expected5, "edit 12 by 13/10/2020 14:00 pri med");
		
		ArrayList<Parameter> expected6 = new ArrayList<Parameter>();
		expected6.add(new Parameter(ParameterType.NAME, "V0.1 Progress Report"));
		expected6.add(new Parameter(ParameterType.CATEGORY, "NUS"));
		expected6.add(new Parameter(ParameterType.PRIORITY, "high"));
		Collections.reverse(expected6);
		assertAddParameters(expected6, "add V0.1 Progress Report cat NUS pri h");
		
		ArrayList<Parameter> expected7 = new ArrayList<Parameter>();
		expected7.add(new Parameter(ParameterType.CATEGORY, "other"));
		expected7.add(new Parameter(ParameterType.PRIORITY, "low"));
		Collections.reverse(expected7);
		assertDeleteParameters(expected7, "delete cat other pri low");
		
		ArrayList<Parameter> expected8 = new ArrayList<Parameter>();
		expected8.add(new Parameter(ParameterType.INDEX, "102"));
		Collections.reverse(expected8);
		assertDeleteParameters(expected8, "delete 102");
		
		ArrayList<Parameter> expected9 = new ArrayList<Parameter>();
		assertViewParameters(expected9, "view");
		
		ArrayList<Parameter> expected10 = new ArrayList<Parameter>();
		expected10.add(new Parameter(ParameterType.DATE, "25/10/2015"));
		expected10.add(new Parameter(ParameterType.CATEGORY, "Homework"));
		Collections.reverse(expected10);
		assertViewParameters(expected10, "view by 25/10/2015 cat Homework");
	}
	
	private void assertAddParameters(ArrayList<Parameter> expected, String command) {
		assertEquals(toString(expected), toString(new AddParameterParser().parseParameters(command)));
	}
	
	private void assertEditParameters(ArrayList<Parameter> expected, String command) {
		assertEquals(toString(expected), toString(new EditParameterParser().parseParameters(command)));
	}
	
	private void assertDeleteParameters(ArrayList<Parameter> expected, String command) {
		assertEquals(toString(expected), toString(new DeleteParameterParser().parseParameters(command)));
	}
	
	private void assertViewParameters(ArrayList<Parameter> expected, String command) {
		assertEquals(toString(expected), toString(new ViewParameterParser().parseParameters(command)));
	}
	
	private static String toString(ArrayList<Parameter> parameters) {
		String resultString = new String();
		
		for (int i = 0; i < parameters.size(); i++) {
			resultString += parameters.get(i).getParameterType().name();
			resultString += ' ';
			resultString += parameters.get(i).getParameterValue();
			resultString += '\n';
		}
		
		System.out.println(resultString);
		
		return resultString;
	}

}