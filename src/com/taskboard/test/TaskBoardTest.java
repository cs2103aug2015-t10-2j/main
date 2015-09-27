package com.taskboard.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;

import com.taskboard.main.Command;
import com.taskboard.main.CommandType;
import com.taskboard.main.Parameter;
import com.taskboard.main.ParameterType;

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
		expected2.add(new Parameter(ParameterType.NAME, "Hello Again"));
		expected2.add(new Parameter(ParameterType.DESCRIPTION, "This is the second Hello"));
		assertParameters(expected2, "add Hello Again; This is the second Hello");
		
		ArrayList<Parameter> expected3 = new ArrayList<Parameter>();
		expected3.add(new Parameter(ParameterType.NAME, "Meeting with Chris"));
		expected3.add(new Parameter(ParameterType.START_DATE, "27/09/2015"));
		expected3.add(new Parameter(ParameterType.START_TIME, "19:00"));
		expected3.add(new Parameter(ParameterType.END_DATE, "27/09/2015"));
		expected3.add(new Parameter(ParameterType.END_TIME, "21:00"));
		assertParameters(expected3, "add Meeting with Chris; from 7pm To 9pm");
	}

	private void assertCommandType(CommandType expected, String command) {
		assertEquals(expected, new Command(command).getCommandType()); 
	}
	
	private void assertParameters(ArrayList<Parameter> expected, String command) {
		assertEquals(toParameterString(expected), toParameterString(new Command(command).getParameters()));
	}
	
	private static String toParameterString(ArrayList<Parameter> parameters) {
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