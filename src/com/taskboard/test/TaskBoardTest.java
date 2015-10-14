package com.taskboard.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;

import java.text.SimpleDateFormat;

import com.taskboard.main.Command;
import com.taskboard.main.CommandType;
import com.taskboard.main.Parameter;
import com.taskboard.main.ParameterType;
import com.taskboard.main.CommandTypeParser;
import com.taskboard.main.AddParameterParser;
import com.taskboard.main.EditParameterParser;
import com.taskboard.main.DeleteParameterParser;
import com.taskboard.main.DateFormatValidator;

public class TaskBoardTest {
	
	@Test
	public void testCommandType() {
		assertCommandType(CommandType.ADD, "Add Hello; by tomorrow 10pm");
		assertCommandType(CommandType.EDIT, "edit Hello; by today");
		assertCommandType(CommandType.DELETE, "deLeTe Hello");
		assertCommandType(CommandType.UNKNOWN, "tHrow");
	}
	
	@Test
	public void TestParameters() {
		ArrayList<Parameter> expected1 = new ArrayList<Parameter>();
		expected1.add(new Parameter(ParameterType.NAME, "Hello World!"));
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
		expected4.add(new Parameter(ParameterType.NAME, "Submit paperwork"));
		SimpleDateFormat dayIndexFormat = new SimpleDateFormat("u");
		int todayDayIndex = Integer.parseInt(dayIndexFormat.format(today));
		Date monday = new Date(today.getTime() + DateFormatValidator.MILLISECONDS_PER_DAY * 
					  (DateFormatValidator.DAY_INDEX_MONDAY - todayDayIndex + 
					  ((todayDayIndex < DateFormatValidator.DAY_INDEX_MONDAY) ? 0 : 7)));
		expected4.add(new Parameter(ParameterType.DATE, defaultDateFormat.format(monday)));
		expected4.add(new Parameter(ParameterType.TIME, "23:59"));
		Collections.reverse(expected4);
		assertAddParameters(expected4, "edit Submit paperwork by monday 23:59");
		
		ArrayList<Parameter> expected5 = new ArrayList<Parameter>();
		expected5.add(new Parameter(ParameterType.NAME, "talk to friend"));
		expected5.add(new Parameter(ParameterType.DATE, "13/10/2020"));
		expected5.add(new Parameter(ParameterType.TIME, "14:00"));
		expected5.add(new Parameter(ParameterType.PRIORITY, "medium"));
		Collections.reverse(expected5);
		assertEditParameters(expected5, "edit talk to friend by 13/10/2020 14:00 pri med");
		
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
	}

	private void assertCommandType(CommandType expected, String command) {
		assertEquals(expected, new CommandTypeParser().parseCommandType(command)); 
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