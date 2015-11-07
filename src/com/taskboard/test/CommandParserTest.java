//@@author A0126536E
package com.taskboard.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;

import java.text.SimpleDateFormat;

import com.taskboard.main.comparator.ParameterComparator;
import com.taskboard.main.parser.AddParameterParser;
import com.taskboard.main.parser.CommandTypeParser;
import com.taskboard.main.parser.CompleteParameterParser;
import com.taskboard.main.parser.DeleteParameterParser;
import com.taskboard.main.parser.EditParameterParser;
import com.taskboard.main.parser.ViewParameterParser;
import com.taskboard.main.util.CommandType;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.ParameterType;

/**
 * This is a unit test class for CommandParser class. It constitutes of tests 
 * for CommandTypeParser and tests for ParameterParser. All the tests are distributed 
 * using equivalence partitioning for maximum case coverage.
 * @author Alvian Prasetya
 */
public class CommandParserTest {
	
	private static final String STRING_SPACE = " ";
	private static final String STRING_NEW_LINE = "\n";
	
	@Test
	public void testCommandTypeParserAdd1() {
		// lower case partition
		assertCommandType(CommandType.ADD, "add");
	}
	
	@Test
	public void testCommandTypeParserAdd2() {
		// mixed case partition
		assertCommandType(CommandType.ADD, "aDd");
	}
	
	@Test
	public void testCommandTypeParserAdd3() {
		// upper case partition
		assertCommandType(CommandType.ADD, "ADD");
	}

	@Test
	public void testCommandTypeParserAdd4() {
		// alternative format partition
		assertCommandType(CommandType.ADD, "a");
	}
	
	@Test
	public void testCommandTypeParserEdit1() {
		// upper case partition
		assertCommandType(CommandType.EDIT, "edit");
	}
	
	@Test
	public void testCommandTypeParserEdit2() {
		// mixed case partition
		assertCommandType(CommandType.EDIT, "eDiT");
	}
	
	@Test
	public void testCommandTypeParserEdit3() {
		// lower case partition
		assertCommandType(CommandType.EDIT, "EDIT");
	}
	
	@Test
	public void testCommandTypeParserEdit4() {
		// alternative format partition
		assertCommandType(CommandType.EDIT, "e");
	}
	
	@Test
	public void testCommandTypeParserDelete1() {
		// lower case partition
		assertCommandType(CommandType.DELETE, "delete");
	}
	
	@Test
	public void testCommandTypeParserDelete2() {
		// mixed case partition
		assertCommandType(CommandType.DELETE, "deLeTE");
	}
	
	@Test
	public void testCommandTypeParserDelete3() {
		// upper case partition
		assertCommandType(CommandType.DELETE, "DELETE");
	}
	
	@Test
	public void testCommandTypeParserDelete4() {
		// alternative format partition
		assertCommandType(CommandType.DELETE, "d");
	}
	
	@Test
	public void testCommandTypeParserView1() {
		// lower case partition
		assertCommandType(CommandType.VIEW, "view");
	}
	
	@Test
	public void testCommandTypeParserView2() {
		// mixed case partition
		assertCommandType(CommandType.VIEW, "ViEW");
	}
	
	@Test
	public void testCommandTypeParserView3() {
		// upper case partition
		assertCommandType(CommandType.VIEW, "VIEW");
	}
	
	@Test
	public void testCommandTypeParserView4() {
		// alternative format partition
		assertCommandType(CommandType.VIEW, "v");
	}
	
	@Test
	public void testCommandTypeParserUnknown1() {
		// this is a mixed case unknown command partition
		assertCommandType(CommandType.UNKNOWN, "Addt");
	}
	
	@Test
	public void testCommandTypeParserUnknown2() {
		// this is a boundary case for empty string
		assertCommandType(CommandType.UNKNOWN, "");
	}
	
	@Test
	public void testCommandTypeParserUnknown3() {
		// this is a boundary case for spaces-only string
		assertCommandType(CommandType.UNKNOWN, "   ");
	}
	
	@Test
	public void testParameterParserAdd1() {
		// floating task adding testParameterParser, 'multiple tokens' partition
		ArrayList<Parameter> expected = new ArrayList<Parameter>();
		expected.add(new Parameter(ParameterType.NAME, "Hello again World!"));
		
		assertAddParameters(expected, "add Hello again World!");
	}
	
	@Test
	public void testParameterParserAdd2() {
		// adding of complex descriptive task, 'multiple constraints' partition
		ArrayList<Parameter> expected = new ArrayList<Parameter>();
		expected.add(new Parameter(ParameterType.NAME, "Meet Chris"));
		SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
		expected.add(new Parameter(ParameterType.START_DATE, defaultDateFormat.format(tomorrow)));
		expected.add(new Parameter(ParameterType.START_TIME, "19:00"));
		expected.add(new Parameter(ParameterType.END_DATE, defaultDateFormat.format(tomorrow)));
		expected.add(new Parameter(ParameterType.END_TIME, "21:00"));
		expected.add(new Parameter(ParameterType.CATEGORY, "meeting"));
		expected.add(new Parameter(ParameterType.PRIORITY, "high"));
		
		assertAddParameters(expected, "add Meet Chris from tomorrow 7pm to tomorrow 9pm cat meeting pri high");
	}
	
	@Test
	public void testParameterParserAdd3() {
		// adding of task with name containing a delimiter, 'ambiguous delimiter' partition
		ArrayList<Parameter> expected = new ArrayList<Parameter>();
		expected.add(new Parameter(ParameterType.NAME, "Go to School from Home"));
		expected.add(new Parameter(ParameterType.CATEGORY, "daily"));
		expected.add(new Parameter(ParameterType.PRIORITY, "high"));
		
		assertAddParameters(expected, "add Go to School from Home pri hi cat daily");
	}
	
	@Test
	public void testParameterParserEdit1() {
		// editing of complex descriptive task, 'multiple constraints' partition
		ArrayList<Parameter> expected = new ArrayList<Parameter>();
		expected.add(new Parameter(ParameterType.INDEX, "12"));
		expected.add(new Parameter(ParameterType.NEW_NAME, "testParameterParser at school"));
		expected.add(new Parameter(ParameterType.DATE, "13/10/2020"));
		expected.add(new Parameter(ParameterType.TIME, "14:00"));
		expected.add(new Parameter(ParameterType.PRIORITY, "medium"));
		expected.add(new Parameter(ParameterType.CATEGORY, "testParameterParser"));
		
		assertEditParameters(expected, "edit 12 testParameterParser at school at 13/10/2020 14:00 pri med cat testParameterParser");
	}
	
	@Test
	public void testParameterParserDelete1() {
		// deleting task by index
		ArrayList<Parameter> expected = new ArrayList<Parameter>();
		expected.add(new Parameter(ParameterType.INDEX, "102"));
		
		assertDeleteParameters(expected, "delete 102");
	}
	
	@Test
	public void testParameterParserDelete2() {
		// deleting of complex descriptive tasks, 'multiple constraints' partition
		ArrayList<Parameter> expected = new ArrayList<Parameter>();
		expected.add(new Parameter(ParameterType.CATEGORY, "other"));
		expected.add(new Parameter(ParameterType.PRIORITY, "low"));
		expected.add(new Parameter(ParameterType.START_DATE, "23/10/2015"));
		expected.add(new Parameter(ParameterType.END_DATE, "28/10/2015"));
		
		assertDeleteParameters(expected, "delete cat other pri low from 23/10/2015 to 28/10/2015");
	}
	
	@Test
	public void testParameterParserView1() {
		// viewing of all tasks, 'no constraint' partition
		ArrayList<Parameter> expected = new ArrayList<Parameter>();
		
		assertViewParameters(expected, "view");
	}
	
	@Test
	public void testParameterParserView2() {
		// selective viewing of complex descriptive tasks, 'multiple constraints' partition
		ArrayList<Parameter> expected = new ArrayList<Parameter>();
		expected.add(new Parameter(ParameterType.DATE, "25/10/2015"));
		expected.add(new Parameter(ParameterType.CATEGORY, "Homework"));
		expected.add(new Parameter(ParameterType.PRIORITY, "low"));
		
		assertViewParameters(expected, "view by 25/10/2015 cat Homework pri low");
	}
	
	@Test
	public void testParameterParserComplete1() {
		// checking/marking of a task
		ArrayList<Parameter> expected = new ArrayList<Parameter>();
		expected.add(new Parameter(ParameterType.INDEX, "5"));
		
		assertCheckParameters(expected, "complete 5");
	}
	
	private void assertCommandType(CommandType expected, String command) {
		assertEquals(expected, new CommandTypeParser().parseCommandType(command)); 
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
			resultString += STRING_SPACE;
			resultString += parameters.get(i).getParameterValue();
			resultString += STRING_NEW_LINE;
		}
		
		return resultString;
	}

}