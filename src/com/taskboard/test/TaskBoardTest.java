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
		assertParameters(expected1, "add Hello World!");
		
		ArrayList<Parameter> expected2 = new ArrayList<Parameter>();
		expected2.add(new Parameter(ParameterType.NAME, "Hello again!"));
		expected2.add(new Parameter(ParameterType.DATE, "12/10/2020"));
		Collections.reverse(expected2);
		assertParameters(expected2, "add Hello again! by 12/10/2020");
		
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
		assertParameters(expected3, "add Meeting with Chris from tomorrow 7pm to tomorrow 9pm");
		
		ArrayList<Parameter> expected4 = new ArrayList<Parameter>();
		expected4.add(new Parameter(ParameterType.NAME, "Submit paperwork"));
		SimpleDateFormat dayIndexFormat = new SimpleDateFormat("u");
		int todayDayIndex = Integer.parseInt(dayIndexFormat.format(today));
		Date monday = new Date(today.getTime() + Command.SECONDS_PER_DAY * 
					  (Command.DAY_INDEX_MONDAY - todayDayIndex + 
					  ((todayDayIndex < Command.DAY_INDEX_MONDAY) ? 0 : 7)));
		expected4.add(new Parameter(ParameterType.DATE, defaultDateFormat.format(monday)));
		expected4.add(new Parameter(ParameterType.TIME, "23:59"));
		Collections.reverse(expected4);
		assertParameters(expected4, "add Submit paperwork by monday 23:59");
		
		ArrayList<Parameter> expected5 = new ArrayList<Parameter>();
		expected5.add(new Parameter(ParameterType.NAME, "talk to friend"));
		expected5.add(new Parameter(ParameterType.DATE, "13/10/2020"));
		expected5.add(new Parameter(ParameterType.TIME, "14:00"));
		Collections.reverse(expected5);
		assertParameters(expected5, "edit talk to friend by 13/10/2020 14:00");
	}

	private void assertCommandType(CommandType expected, String command) {
		assertEquals(expected, new CommandTypeParser().parseCommandType(command)); 
	}
	
	private void assertParameters(ArrayList<Parameter> expected, String command) {
		assertEquals(toString(expected), toString(new AddParameterParser().parseParameters(command)));
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