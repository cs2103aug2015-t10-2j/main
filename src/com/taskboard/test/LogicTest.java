package com.taskboard.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;

import com.taskboard.main.DateComparator;
import com.taskboard.main.Entry;
import com.taskboard.main.Logic;
import com.taskboard.main.Parameter;
import com.taskboard.main.ParameterType;
import com.taskboard.main.Response;

public class LogicTest {
	
	private static final String MESSAGE_WELCOME = "Welcome to TASKBOARD!";
	private static final String MESSAGE_AFTER_ADD = "Entry successfully added:";
	private static final String MESSAGE_ERROR_FOR_CREATING_EXISTNG_FILE = "The file already exists.";
	private static final String MESSAGE_ERROR_FOR_OPENING_NON_EXISTING_FILE = "The file does not exists.";
	private static final String MESSAGE_ERROR_FOR_NO_PARAMETERS_AFTER_COMMAND = "No parameters provided.";
	private static final String MESSAGE_ERROR_FOR_EMPTY_PRI_PARAMETER = "Empty pri parameter provided.";
	private static final String MESSAGE_ERROR_FOR_EMPTY_BY_PARAMETER = "Empty by parameter provided.";
	private static final String MESSAGE_ERROR_FOR_EMPTY_CAT_PARAMETER = "Empty cat parameter provided.";
	private static final String MESSAGE_ERROR_FOR_EMPTY_FROM_PARAMETER = "Empty from parameter provided.";
	private static final String MESSAGE_ERROR_FOR_EMPTY_TO_PARAMETER = "Empty to parameter provided.";
	private static final String MESSAGE_ERROR_FOR_NO_DATE = "No date provided.";
	private static final String MESSAGE_ERROR_FOR_NO_START_DATE = "No start date provided.";
	private static final String MESSAGE_ERROR_FOR_NO_END_DATE_TIME = "No end date time provided.";
	private static final String MESSAGE_ERROR_FOR_INVALID_DATE_TIME = "Invalid date time provided.";
	private static final String MESSAGE_ERROR_FOR_PAST_DATE_TIME = "Past date time provided.";
	
	private File testStorageFileForNew;
	private File testArchiveFileForNew;
	private File testPreferenceFileForNew;
	private File testStorageFileForOpen;
	private File testArchiveFileForOpen;
	private File testPreferenceFileForOpen;
	private ArrayList<Entry> expectedEntries; 
	private Logic logic;
	
	@Before 
	public void setUp() throws IOException {
		logic = new Logic();
		
		testStorageFileForNew = new File("testNew" + ".str");
		testStorageFileForNew.createNewFile();
		testArchiveFileForNew = new File("testNew" + ".arc");
		testArchiveFileForNew.createNewFile();
		testPreferenceFileForNew = new File("testNew" + ".pref");
		testPreferenceFileForNew.createNewFile();
		
		testStorageFileForOpen = new File("testOpen" + ".str");
		testStorageFileForOpen.createNewFile();
		testArchiveFileForOpen = new File("testOpen" + ".arc");
		testArchiveFileForOpen.createNewFile();
		testPreferenceFileForOpen = new File("testOpen" + ".pref");
		testPreferenceFileForOpen.createNewFile();
		
		Entry event = new Entry();
		event.addToParameters(new Parameter(ParameterType.INDEX, "1"));
		event.addToParameters(new Parameter(ParameterType.NAME, "EE2020 Final Quiz"));
		event.addToParameters(new Parameter(ParameterType.START_DATE, "14/11/2015"));
		event.addToParameters(new Parameter(ParameterType.START_TIME, "10:00"));
		event.addToParameters(new Parameter(ParameterType.END_DATE, "14/11/2015"));
		event.addToParameters(new Parameter(ParameterType.END_TIME, "11:30"));
		event.addToParameters(new Parameter(ParameterType.PRIORITY, "high"));
		expectedEntries = new ArrayList<Entry>();
		expectedEntries.add(event);
		
		Entry floatingTask = new Entry();
		floatingTask.addToParameters(new Parameter(ParameterType.INDEX, "2"));
		floatingTask.addToParameters(new Parameter(ParameterType.NAME, "EE2021 Revision"));
		floatingTask.addToParameters(new Parameter(ParameterType.CATEGORY, "Exam Prep"));
		expectedEntries.add(floatingTask);
		
		FileWriter addToFile = new FileWriter(testStorageFileForOpen);
		for (int i = 0; i < expectedEntries.size(); i++) {
			String entrydetails = expectedEntries.get(i).toString();
			addToFile.write(entrydetails);
			addToFile.write(System.lineSeparator());
			addToFile.flush();
		}
		addToFile.close();
	}
		
	@Test
	public void testResponsesForNew() {
		Response actualResponse = logic.processCommand("new testNew");
		Response expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_CREATING_EXISTNG_FILE);
		testResponseEquality("test failure Response for creating file that already exists", expectedResponse,
				             actualResponse);
			
		actualResponse = logic.processCommand("new AcademicManager");		
		ArrayList<Entry> expectedEntriesForNew = new ArrayList<Entry>();
		expectedResponse = createSuccessResponse(MESSAGE_WELCOME, expectedEntriesForNew);
		testResponseEquality("test success response for creating new file", expectedResponse, actualResponse);
		File storageFile = new File("AcademicManager" + ".str");
		storageFile.delete();
		File archiveFile = new File("AcademicManager" + ".arc");
		archiveFile.delete();
		File preferenceFile = new File("AcademicManager" + ".pref");
		preferenceFile.delete();
	}
	
	@Test
	public void testResponsesForOpen() {
		Response actualResponse = logic.processCommand("open TaskManager");
		Response expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_OPENING_NON_EXISTING_FILE);
		testResponseEquality("test failure response for opening file that does not exists", expectedResponse,
				             actualResponse);
		
		actualResponse = logic.processCommand("open testOpen");
		expectedResponse = createSuccessResponse(MESSAGE_WELCOME, expectedEntries);
		testResponseEquality("test success response for opening existing file", expectedResponse, actualResponse);
	}
	
	@Test
	public void testResponsesForAdd() {
		Response actualResponse = logic.processCommand("add ");
		Response expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_NO_PARAMETERS_AFTER_COMMAND);
		testResponseEquality("test failure response for no parameters after add command", expectedResponse,
				             actualResponse);
		
		actualResponse = logic.processCommand("a Prepare for EE2020 Final Quiz pri ");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_EMPTY_PRI_PARAMETER);
		testResponseEquality("test failure response for empty pri parameter", expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("a Prepare for EE2020 Final Quiz pri H");
		Entry floatingTask = new Entry();
		floatingTask.addToParameters(new Parameter(ParameterType.INDEX, ""));
		floatingTask.addToParameters(new Parameter(ParameterType.NAME, "Prepare for EE2020 Final Quiz"));
		floatingTask.addToParameters(new Parameter(ParameterType.PRIORITY, "high"));
		expectedEntries.add(floatingTask);
		updateSortingOfEntries(expectedEntries);
		String feedback = MESSAGE_AFTER_ADD.concat("<br>").concat("<br>").concat(floatingTask.toHTMLString());
		expectedResponse = createSuccessResponse(feedback, expectedEntries);
		testResponseEquality("test success response for adding floating task with priority", expectedResponse, 
				             actualResponse);
		
		actualResponse = logic.processCommand("add Submit MA3264 by ");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_EMPTY_BY_PARAMETER);
		testResponseEquality("test failure response for empty by parameter", expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Submit MA3264 by 10am");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_NO_DATE);
		testResponseEquality("test failure response for no date", expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Submit MA3264 by 11/13/2015 10am");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_INVALID_DATE_TIME);
		testResponseEquality("test failure response for invalid date time", expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Submit MA3264 by 03/11/2015 10am");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_PAST_DATE_TIME);
		testResponseEquality("test failure response for past date time for deadline task", expectedResponse, 
				             actualResponse);
		
		actualResponse = logic.processCommand("add Submit MA3264 by fri 10am cat ");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_EMPTY_CAT_PARAMETER);
		testResponseEquality("test failure response for empty cat parameter", expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Submit MA3264 by fri 10am cat Mathematics");
		Entry deadlineTask = new Entry();
		deadlineTask.addToParameters(new Parameter(ParameterType.INDEX, ""));
		deadlineTask.addToParameters(new Parameter(ParameterType.NAME, "Submit MA3264"));
		deadlineTask.addToParameters(new Parameter(ParameterType.DATE, "06/11/2015"));
		deadlineTask.addToParameters(new Parameter(ParameterType.TIME, "10:00"));
		deadlineTask.addToParameters(new Parameter(ParameterType.CATEGORY, "Mathematics"));
		expectedEntries.add(deadlineTask);
		updateSortingOfEntries(expectedEntries);
		feedback = MESSAGE_AFTER_ADD.concat("<br>").concat("<br>").concat(deadlineTask.toHTMLString());
		expectedResponse = createSuccessResponse(feedback, expectedEntries);
		testResponseEquality("test success response for adding deadline task with category", expectedResponse, 
				             actualResponse);
		
		actualResponse = logic.processCommand("add Final Exam from");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_EMPTY_FROM_PARAMETER);
		testResponseEquality("test failure response for empty from parameter", expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Final Exam from 27/11/2015 1pm");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_NO_END_DATE_TIME);
		testResponseEquality("test failure response for no end date time", expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Final Exam from 27/11/2015 1pm to");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_EMPTY_TO_PARAMETER);
		testResponseEquality("test failure response for empty to parameter", expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Final Exam from 1pm to 3pm");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_NO_START_DATE);
		testResponseEquality("test failure response for no start date", expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Final Exam from 27/11/2015 1pm to 12pm");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_PAST_DATE_TIME);
		testResponseEquality("test failure response for past date time for event", expectedResponse, 
				             actualResponse);
		
//		actualResponse = logic.processCommand("add Final Exam from 27/11/2015 1pm to 3pm pri H cat EE2020");
//		Entry event = new Entry();
		
	}
	
	private void updateSortingOfEntries(ArrayList<Entry> entries) {
		Collections.sort(entries, new DateComparator());
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			Parameter indexParameter = new Parameter(ParameterType.INDEX, String.valueOf(i+1));
			entry.setIndexParameter(indexParameter);
		}
	}
	
	private Response createSuccessResponse(String feedback, ArrayList<Entry> entries) {
		Response response = new Response();
		response.setIsSuccess(true);
		response.setFeedback(feedback);
		response.setEntries(entries);
		
		return response;
	}
	
	private Response createFailureResponse(String exMsg) {
		Response response = new Response();
		response.setIsSuccess(false);
		response.setFeedback(exMsg);
		
		return response;
	}
	
	private void testResponseEquality(String description, Response expectedResponse, Response actualResponse) {
		assertEquals(description, expectedResponse, actualResponse);
	}
	
	@After
	public void terminate() {
		testStorageFileForNew.delete();
		testArchiveFileForNew.delete();
		testPreferenceFileForNew.delete();
		testStorageFileForOpen.delete();
		testArchiveFileForOpen.delete();
		testPreferenceFileForOpen.delete();
	}
	
//	@Test
//	public void testValidNewAndAdd() {
//		ArrayList<Parameter> testParameters = new ArrayList<Parameter>();
//		testParameters.add(new Parameter(ParameterType.NAME, "myFile"));
//		Command command = new NewCommand(testParameters);
//		Response actualResponse = command.executeCommand();
//		
//		ArrayList<Entry> expectedEntries = new ArrayList<Entry>();
//		Response expectedSuccessResponse = createSuccessResponse(true, MESSAGE_WELCOME, expectedEntries);
//		testResponseEquality("Success response for creating new file", expectedSuccessResponse, actualResponse);
//		
//		testParameters.clear();
//		Parameter nameParameter = new Parameter(ParameterType.NAME, "MA3264 Revision");
//		testParameters.add(nameParameter);
//		Parameter priorityParameter = new Parameter(ParameterType.PRIORITY, "low");
//		testParameters.add(priorityParameter);
//		command = new AddCommand(testParameters);
//		actualResponse = command.executeCommand();
//		
//		Entry floatingTask = new Entry();
//		Parameter indexParameter = new Parameter(ParameterType.INDEX, "1");
//		floatingTask.addToParameters(indexParameter);
//		floatingTask.addToParameters(nameParameter);
//		floatingTask.addToParameters(priorityParameter);
//		expectedEntries.add(floatingTask);
//		String completedMessage = String.format(MESSAGE_AFTER_ADD, "MA3264 Revision");
//		expectedSuccessResponse = createSuccessResponse(true, completedMessage, expectedEntries);
//		testResponseEquality("Success response for adding floating task with priority", expectedSuccessResponse,
//				             actualResponse);
//		
//		testParameters.clear();
//		nameParameter = new Parameter(ParameterType.NAME, "HW4 Assignment");
//		testParameters.add(nameParameter);
//		Parameter dateParameter = new Parameter(ParameterType.DATE, "04/11/2015");
//		testParameters.add(dateParameter);
//		Parameter timeParameter = new Parameter(ParameterType.TIME, "12:00");
//		testParameters.add(timeParameter);
//		Parameter categoryParameter = new Parameter(ParameterType.CATEGORY, "EE2021");
//		testParameters.add(categoryParameter);
//		command = new AddCommand(testParameters);
//		actualResponse = command.executeCommand();
//		
//		Entry deadlineTask = new Entry();
//		indexParameter = new Parameter(ParameterType.INDEX, "2");
//		deadlineTask.addToParameters(indexParameter);
//		deadlineTask.addToParameters(nameParameter);
//		deadlineTask.addToParameters(dateParameter);
//		deadlineTask.addToParameters(timeParameter);
//		deadlineTask.addToParameters(categoryParameter);
//		expectedEntries.add(deadlineTask);
//		Collections.sort(expectedEntries, new DateComparator());
//		completedMessage = String.format(MESSAGE_AFTER_ADD, "HW4 Assignment");
//		expectedSuccessResponse = createSuccessResponse(true, completedMessage, expectedEntries);
//		testResponseEquality("Success response for adding deadline task with category", expectedSuccessResponse,
//				             actualResponse);
//	}
}