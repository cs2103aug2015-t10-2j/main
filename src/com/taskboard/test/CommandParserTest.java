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
import com.taskboard.main.CompleteParameterParser;
import com.taskboard.main.ParameterComparator;

public class CommandParserTest {
	
	@Test
	public void testCommandTypeParser() {
		assertCommandType(CommandType.ADD, "Add");
		// all capital case test
		assertCommandType(CommandType.EDIT, "EDIT");
		// mixed case test
		assertCommandType(CommandType.DELETE, "deLeTE");
		// lower case test
		assertCommandType(CommandType.VIEW, "view");
		// this is a boundary case for UNKNOWN command partition
		assertCommandType(CommandType.UNKNOWN, "Addt");
		// this is a boundary case for empty string
		assertCommandType(CommandType.UNKNOWN, "");
	}
	
	private void assertCommandType(CommandType expected, String command) {
		assertEquals(expected, new CommandTypeParser().parseCommandType(command)); 
	}
	
	@Test
	public void testParameterParser() {
		// floating task adding test, 'multiple tokens' partition
		ArrayList<Parameter> expected1 = new ArrayList<Parameter>();
		expected1.add(new Parameter(ParameterType.NAME, "Hello again World!"));
		
		assertAddParameters(expected1, "add Hello again World!");
		
		// adding of complex descriptive task, 'multiple constraints' partition
		ArrayList<Parameter> expected2 = new ArrayList<Parameter>();
		expected2.add(new Parameter(ParameterType.NAME, "Meet Chris"));
		SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
		expected2.add(new Parameter(ParameterType.START_DATE, defaultDateFormat.format(tomorrow)));
		expected2.add(new Parameter(ParameterType.START_TIME, "19:00"));
		expected2.add(new Parameter(ParameterType.END_DATE, defaultDateFormat.format(tomorrow)));
		expected2.add(new Parameter(ParameterType.END_TIME, "21:00"));
		expected2.add(new Parameter(ParameterType.CATEGORY, "meeting"));
		expected2.add(new Parameter(ParameterType.PRIORITY, "high"));
		
		assertAddParameters(expected2, "add Meet Chris from tomorrow 7pm to tomorrow 9pm cat meeting pri high");
		
		// adding of task with name containing a delimiter, 'ambiguous delimiter' partition
		ArrayList<Parameter> expected3 = new ArrayList<Parameter>();
		expected3.add(new Parameter(ParameterType.NAME, "Go to School from Home"));
		expected3.add(new Parameter(ParameterType.CATEGORY, "daily"));
		expected3.add(new Parameter(ParameterType.PRIORITY, "high"));
		
		assertAddParameters(expected3, "add Go to School from Home pri hi cat daily");
		
		// editing of complex descriptive task, 'multiple constraints' partition
		ArrayList<Parameter> expected4 = new ArrayList<Parameter>();
		expected4.add(new Parameter(ParameterType.INDEX, "12"));
		expected4.add(new Parameter(ParameterType.NEW_NAME, "test at school"));
		expected4.add(new Parameter(ParameterType.DATE, "13/10/2020"));
		expected4.add(new Parameter(ParameterType.TIME, "14:00"));
		expected4.add(new Parameter(ParameterType.PRIORITY, "medium"));
		expected4.add(new Parameter(ParameterType.CATEGORY, "test"));
		
		assertEditParameters(expected4, "edit 12 test at school at 13/10/2020 14:00 pri med cat test");
		
		// deleting of complex descriptive tasks, 'multiple constraints' partition
		ArrayList<Parameter> expected5 = new ArrayList<Parameter>();
		expected5.add(new Parameter(ParameterType.CATEGORY, "other"));
		expected5.add(new Parameter(ParameterType.PRIORITY, "low"));
		expected5.add(new Parameter(ParameterType.START_DATE, "23/10/2015"));
		expected5.add(new Parameter(ParameterType.END_DATE, "28/10/2015"));
		
		assertDeleteParameters(expected5, "delete cat other pri low from 23/10/2015 to 28/10/2015");
		
		// deleting task by index
		ArrayList<Parameter> expected6 = new ArrayList<Parameter>();
		expected6.add(new Parameter(ParameterType.INDEX, "102"));
		
		assertDeleteParameters(expected6, "delete 102");
		
		// viewing of all tasks, 'no constraint' partition
		ArrayList<Parameter> expected7 = new ArrayList<Parameter>();
		
		assertViewParameters(expected7, "view");
		
		// selective viewing of complex descriptive tasks, 'multiple constraints' partition
		ArrayList<Parameter> expected8 = new ArrayList<Parameter>();
		expected8.add(new Parameter(ParameterType.DATE, "25/10/2015"));
		expected8.add(new Parameter(ParameterType.CATEGORY, "Homework"));
		expected8.add(new Parameter(ParameterType.PRIORITY, "low"));
		
		assertViewParameters(expected8, "view by 25/10/2015 cat Homework pri low");
		
		// checking/marking of a task
		ArrayList<Parameter> expected9 = new ArrayList<Parameter>();
		expected9.add(new Parameter(ParameterType.INDEX, "5"));
		
		assertCheckParameters(expected9, "check 5");
	}
	
	private void assertAddParameters(ArrayList<Parameter> expected, String command) {
		Collections.sort(expected, new ParameterComparator());
		ArrayList<Parameter> actual = new AddParameterParser().parseParameters(command);
		Collections.sort(actual, new ParameterComparator());
		assertEquals(toString(expected), toString(actual));
	}
	
	private void assertEditParameters(ArrayList<Parameter> expected, String command) {
		Collections.sort(expected, new ParameterComparator());
		ArrayList<Parameter> actual = new EditParameterParser().parseParameters(command);
		Collections.sort(actual, new ParameterComparator());
		assertEquals(toString(expected), toString(actual));
	}
	
	private void assertDeleteParameters(ArrayList<Parameter> expected, String command) {
		Collections.sort(expected, new ParameterComparator());
		ArrayList<Parameter> actual = new DeleteParameterParser().parseParameters(command);
		Collections.sort(actual, new ParameterComparator());
		assertEquals(toString(expected), toString(actual));
	}
	
	private void assertViewParameters(ArrayList<Parameter> expected, String command) {
		Collections.sort(expected, new ParameterComparator());
		ArrayList<Parameter> actual = new ViewParameterParser().parseParameters(command);
		Collections.sort(actual, new ParameterComparator());
		assertEquals(toString(expected), toString(actual));
	}
	
	private void assertCheckParameters(ArrayList<Parameter> expected, String command) {
		Collections.sort(expected, new ParameterComparator());
		ArrayList<Parameter> actual = new CompleteParameterParser().parseParameters(command);
		Collections.sort(actual, new ParameterComparator());
		assertEquals(toString(expected), toString(actual));
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