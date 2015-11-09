//@@author A0123935E
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

import com.taskboard.main.Logic;

import com.taskboard.main.comparator.DateComparator;
import com.taskboard.main.comparator.ParameterComparator;

import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.ParameterType;
import com.taskboard.main.util.Response;

/** This is an integration test class to test the integration of Logic with CommandParser
 * and the storage handler classes. It tests for the different types of responses returned
 * to UserInterface. Therefore, the test cases have been designed using Equivalence Partitioning
 * where each partition corresponds to a response for a specific scenario. The intent is to achieve
 * maximum decision/branch coverage.
 * @author Amarparkash Singh Mavi
 *
 */
public class LogicTest {
	
	private static final String MESSAGE_WELCOME = "Welcome to TASKBOARD!";
	private static final String MESSAGE_FOR_FILENAME = "Scheduler \"%1$s\" is ready for use.";
	private static final String MESSAGE_AFTER_ADD = "Entry successfully added:";
	private static final String MESSAGE_AFTER_EDIT = "Entry successfully updated:";
	private static final String MESSAGE_FOR_UPDATED_ENTRY = "Entry after update =>";
	private static final String MESSAGE_FOR_OLD_ENTRY = "Entry before update =>";
	private static final String MESSAGE_AFTER_COMPLETE = "Entry successfully indicated as completed:";
	private static final String MESSAGE_EMPTY_ARCHIVE = "There are no completed entries.";
	private static final String MESSAGE_RETRIEVE_ARCHIVE_SUCCESS = "Successfully retrieved all archived entries.";
	private static final String MESSAGE_AFTER_RESTORE = "Entry successfully restored:";
	private static final String MESSAGE_FILTER_RESULTS = "%1$s entries found based on search results!";
	private static final String MESSAGE_AFTER_DELETE = "Entry successfully deleted:";
	private static final String MESSAGE_ERROR_FOR_CREATING_EXISTNG_FILE = "The file already exists.";
	private static final String MESSAGE_ERROR_FOR_OPENING_NON_EXISTING_FILE = "The file does not exists.";
	private static final String MESSAGE_ERROR_FOR_NO_PARAMETERS_AFTER_COMMAND = "No parameters provided.";
	private static final String MESSAGE_ERROR_FOR_EMPTY_PRI_PARAMETER = "Empty pri parameter provided.";
	private static final String MESSAGE_ERROR_FOR_EMPTY_BY_PARAMETER = "Empty by parameter provided.";
	private static final String MESSAGE_ERROR_FOR_EMPTY_CAT_PARAMETER = "Empty cat parameter provided.";
	private static final String MESSAGE_ERROR_FOR_EMPTY_FROM_PARAMETER = "Empty from parameter provided.";
	private static final String MESSAGE_ERROR_FOR_EMPTY_TO_PARAMETER = "Empty to parameter provided.";
	private static final String MESSAGE_ERROR_FOR_NO_VALID_DATE = "No valid date provided.";
	private static final String MESSAGE_ERROR_FOR_NO_VALID_START_DATE = "No valid start date provided.";
	private static final String MESSAGE_ERROR_FOR_NO_VALID_END_DATE_TIME = "No valid end date time provided.";
	private static final String MESSAGE_ERROR_FOR_PAST_DATE_TIME = "Past date time provided.";
	private static final String MESSAGE_ERROR_FOR_NO_EDITED_DETAILS = "No edited details provided.";
	private static final String MESSAGE_ERROR_FOR_INVALID_INDEX_FORMAT = "Invalid numeric format provided for index.";
	private static final String MESSAGE_ERROR_FOR_INVALID_INDEX = "Invalid index provided.";
	
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
		
		Entry event1 = new Entry();
		event1.addToParameters(new Parameter(ParameterType.INDEX, "1"));
		event1.addToParameters(new Parameter(ParameterType.NAME, "Quaterly Meeting"));
		event1.addToParameters(new Parameter(ParameterType.START_DATE, "03/03/2016"));
		event1.addToParameters(new Parameter(ParameterType.START_TIME, "10:00"));
		event1.addToParameters(new Parameter(ParameterType.END_DATE, "03/03/2016"));
		event1.addToParameters(new Parameter(ParameterType.END_TIME, "11:30"));
		event1.addToParameters(new Parameter(ParameterType.PRIORITY, "high"));
		event1.addToParameters(new Parameter(ParameterType.CATEGORY, "Finance"));
		expectedEntries = new ArrayList<Entry>();
		expectedEntries.add(event1);
		
		Entry event2 = new Entry();
		event2.addToParameters(new Parameter(ParameterType.INDEX, "2"));
		event2.addToParameters(new Parameter(ParameterType.NAME, "Annual Meeting"));
		event2.addToParameters(new Parameter(ParameterType.START_DATE, "14/04/2016"));
		event2.addToParameters(new Parameter(ParameterType.START_TIME, "09:00"));
		event2.addToParameters(new Parameter(ParameterType.END_DATE, "14/04/2016"));
		event2.addToParameters(new Parameter(ParameterType.END_TIME, "11:00"));
		event2.addToParameters(new Parameter(ParameterType.PRIORITY, "high"));
		event2.addToParameters(new Parameter(ParameterType.CATEGORY, "Marketing"));
		expectedEntries.add(event2);
		
		Entry deadlineTask1 = new Entry();
		deadlineTask1.addToParameters(new Parameter(ParameterType.INDEX, "3"));
		deadlineTask1.addToParameters(new Parameter(ParameterType.NAME, "Complete annual feedback survey"));
		deadlineTask1.addToParameters(new Parameter(ParameterType.DATE, "25/04/2016"));
		deadlineTask1.addToParameters(new Parameter(ParameterType.TIME, "14:00"));
		deadlineTask1.addToParameters(new Parameter(ParameterType.PRIORITY, "low"));
		expectedEntries.add(deadlineTask1);
		
		Entry deadlineTask2 = new Entry();
		deadlineTask2.addToParameters(new Parameter(ParameterType.INDEX, "4"));
		deadlineTask2.addToParameters(new Parameter(ParameterType.NAME, "Annual company dinner"));
		deadlineTask2.addToParameters(new Parameter(ParameterType.DATE, "02/05/2016"));
		deadlineTask2.addToParameters(new Parameter(ParameterType.TIME, "18:00"));
		deadlineTask2.addToParameters(new Parameter(ParameterType.CATEGORY, "Functions"));
		expectedEntries.add(deadlineTask2);
		
		FileWriter addToFile = new FileWriter(testStorageFileForOpen);
		for (int i = 0; i < expectedEntries.size(); i++) {
			String entrydetails = expectedEntries.get(i).toString();
			addToFile.write(entrydetails);
			addToFile.write(System.lineSeparator());
			addToFile.flush();
		}
		addToFile.close();
		
		logic.processCommand("open testOpen");
	}
		
	@Test
	public void testResponsesForNew() {
		Response actualResponse = logic.processCommand("new testNew");
		Response expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_CREATING_EXISTNG_FILE);
		testResponseEquality("failure Response partition for creating file that already exists", 
				              expectedResponse, actualResponse);
			
		actualResponse = logic.processCommand("new AcademicManager");		
		ArrayList<Entry> expectedEntriesForNew = new ArrayList<Entry>();
		String feedback = getFeedbackForSuccessfulLaunch("AcademicManager");
		expectedResponse = createSuccessResponse(feedback, expectedEntriesForNew);
		testResponseEquality("success response partition for creating new file", expectedResponse, 
				              actualResponse);
		File storageFile = new File("AcademicManager" + ".str");
		storageFile.delete();
		File archiveFile = new File("AcademicManager" + ".arc");
		archiveFile.delete();
		File preferenceFile = new File("AcademicManager" + ".pref");
		preferenceFile.delete();
		
		logic.processCommand("open testOpen");
	}
	
	private String getFeedbackForSuccessfulLaunch(String fileName) {
		String feedback = MESSAGE_WELCOME.concat("<br>");
		feedback = feedback.concat(String.format(MESSAGE_FOR_FILENAME, fileName));
		
		return feedback;
	}
	
	@Test
	public void testResponsesForOpen() {
		Response actualResponse = logic.processCommand("open TaskManager");
		Response expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_OPENING_NON_EXISTING_FILE);
		testResponseEquality("failure response partition for opening file that does not exists", 
				              expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("open testOpen");
		String feedback = getFeedbackForSuccessfulLaunch("testOpen");
		expectedResponse = createSuccessResponse(feedback, expectedEntries);
		testResponseEquality("success response partition for opening existing file", expectedResponse, 
				              actualResponse);
	}
	
	@Test
	public void testResponsesForAdd() { 
		Response actualResponse = logic.processCommand("add ");
		Response expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_NO_PARAMETERS_AFTER_COMMAND);
		testResponseEquality("failure response partition for not providing parameters after add command", 
				             expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("a Prepare for EE2020 Final Quiz pri ");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_EMPTY_PRI_PARAMETER);
		testResponseEquality("failure response partition for providing empty pri parameter", 
				              expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("a Prepare for EE2020 Final Quiz pri H");
		Entry floatingTask = new Entry();
		floatingTask.addToParameters(new Parameter(ParameterType.INDEX, ""));
		floatingTask.addToParameters(new Parameter(ParameterType.NAME, "Prepare for EE2020 Final Quiz"));
		floatingTask.addToParameters(new Parameter(ParameterType.PRIORITY, "high"));
		expectedEntries.add(floatingTask);
		updateSortingOfEntries(expectedEntries);
		String feedback = getSuccessFeedbackForSingleEntryDetails(MESSAGE_AFTER_ADD, floatingTask);
		expectedResponse = createSuccessResponse(feedback, expectedEntries);
		// testing multiple inputs
		testResponseEquality("success response partition for adding floating task with priority", 
				              expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Submit MA3264 by ");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_EMPTY_BY_PARAMETER);
		testResponseEquality("failure response partition for providing empty by parameter", 
				              expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Submit MA3264 by 10am");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_NO_VALID_DATE);
		testResponseEquality("failure response partition for not providing date", expectedResponse, 
				              actualResponse);
		
		actualResponse = logic.processCommand("add Submit MA3264 by 03/11/2015 10am");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_PAST_DATE_TIME);
		testResponseEquality("failure response partition for providing past date time", 
				             expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Submit MA3264 by fri 10am cat ");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_EMPTY_CAT_PARAMETER);
		testResponseEquality("failure response partition for providing empty cat parameter", 
				              expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Submit HW2 by 3 Sep 2016 10am cat MA3264");
		Entry deadlineTask = new Entry();
		deadlineTask.addToParameters(new Parameter(ParameterType.INDEX, ""));
		deadlineTask.addToParameters(new Parameter(ParameterType.NAME, "Submit HW2"));
		deadlineTask.addToParameters(new Parameter(ParameterType.DATE, "03/09/2016"));
		deadlineTask.addToParameters(new Parameter(ParameterType.TIME, "10:00"));
		deadlineTask.addToParameters(new Parameter(ParameterType.CATEGORY, "MA3264"));
		expectedEntries.add(deadlineTask);
		updateSortingOfEntries(expectedEntries);
		feedback = getSuccessFeedbackForSingleEntryDetails(MESSAGE_AFTER_ADD, deadlineTask);
		expectedResponse = createSuccessResponse(feedback, expectedEntries);
		// testing multiple inputs
		testResponseEquality("success response partition for adding deadline task with category", 
				              expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Final Exam from");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_EMPTY_FROM_PARAMETER);
		testResponseEquality("failure response partition for providing empty from parameter", 
				              expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Final Exam from 27/11/2015 1pm");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_NO_VALID_END_DATE_TIME);
		testResponseEquality("failure response partition for not providing end date time", 
				              expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Final Exam from 27/11/2015 1pm to");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_EMPTY_TO_PARAMETER);
		testResponseEquality("failure response partition for providing empty to parameter", 
				              expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Final Exam from 1pm to 3pm");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_NO_VALID_START_DATE);
		testResponseEquality("failure response partition for not providing start date", expectedResponse, 
				             actualResponse);
		
		actualResponse = logic.processCommand("add Final Exam from 27/11/2015 1pm to 12pm");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_PAST_DATE_TIME);
		testResponseEquality("failure response partition for providing end date time earlier than "
				              + "start date time", expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("add Final Exam from 27 Nov 2016 1pm to 3pm pri H cat MA3264");
		Entry event = new Entry();
		event.addToParameters(new Parameter(ParameterType.INDEX, ""));
		event.addToParameters(new Parameter(ParameterType.NAME, "Final Exam"));
		event.addToParameters(new Parameter(ParameterType.START_DATE, "27/11/2016"));
		event.addToParameters(new Parameter(ParameterType.START_TIME, "13:00"));
		event.addToParameters(new Parameter(ParameterType.END_DATE, "27/11/2016"));
		event.addToParameters(new Parameter(ParameterType.END_TIME, "15:00"));
		event.addToParameters(new Parameter(ParameterType.PRIORITY, "high"));
		event.addToParameters(new Parameter(ParameterType.CATEGORY, "MA3264"));
		expectedEntries.add(event);
		updateSortingOfEntries(expectedEntries);
		feedback = getSuccessFeedbackForSingleEntryDetails(MESSAGE_AFTER_ADD, event);
		expectedResponse = createSuccessResponse(feedback, expectedEntries);
		// testing multiple inputs
		testResponseEquality("success response partition for adding event with priority and category", 
			                 expectedResponse, actualResponse);
	}
	
	private void updateSortingOfEntries(ArrayList<Entry> entries) {
		Collections.sort(entries, new DateComparator());
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			Parameter indexParameter = new Parameter(ParameterType.INDEX, String.valueOf(i+1));
			entry.setIndexParameter(indexParameter);
		}
	}
	
	private String getSuccessFeedbackForSingleEntryDetails(String message, Entry entry) {
		String feedback = message.concat("<br>").concat("<br>").concat(entry.toHTMLString());
		
		return feedback;
	}
	
	@Test
	public void testResponsesForEdit() {
		Response actualResponse = logic.processCommand("edit ");
		Response expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_NO_PARAMETERS_AFTER_COMMAND);
		testResponseEquality("failure response partition for not providing parameters after edit command", 
				             expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("edit Annual company dinner at Marina Mandarin");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_INVALID_INDEX_FORMAT);
		testResponseEquality("failure response partition for providing invalid index format with edit", 
	                         expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("edit 4");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_NO_EDITED_DETAILS);
		testResponseEquality("failure response partition for not providing edited details", 
	                         expectedResponse, actualResponse);
	
		actualResponse = logic.processCommand("edit 14 Annual company dinner at Marina Mandarin");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_INVALID_INDEX);
		testResponseEquality("failure response partition for providing invalid index with edit", 
				              expectedResponse, actualResponse);	
	
		actualResponse = logic.processCommand("edit 4 Annual company dinner at Marina Mandarin");
		Entry entryToBeEdited1 = expectedEntries.get(3);
		Entry entryBeforeUpdate1 = new Entry(entryToBeEdited1);
		ArrayList<Parameter> entryDetails1 = entryToBeEdited1.getParameters();
		for (int i = 0; i < entryDetails1.size(); i++) {
			if (entryDetails1.get(i).getParameterType() == ParameterType.NAME) {
				entryDetails1.get(i).setParameterValue("Annual company dinner at Marina Mandarin");
			}
		}
		entryToBeEdited1.setParameters(entryDetails1);
		Entry entryAfterUpdate1 = new Entry(entryToBeEdited1);
		expectedEntries.set(3, entryToBeEdited1);
		updateSortingOfEntries(expectedEntries);
		String feedback = getFeedbackForSuccessfulEdit(entryBeforeUpdate1, entryAfterUpdate1);
		expectedResponse = createSuccessResponse(feedback, expectedEntries);
		testResponseEquality("success response partition for editing entry name", expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("edit 3 cat Administrative pri m by 4pm");
		Entry entryToBeEdited2 = expectedEntries.get(2);
		Entry entryBeforeUpdate2 = new Entry(entryToBeEdited2);
		ArrayList<Parameter> entryDetails2 = entryToBeEdited2.getParameters();
		for (int i = 0; i < entryDetails2.size(); i++) {
			if (entryDetails2.get(i).getParameterType() == ParameterType.PRIORITY) {
				entryDetails2.get(i).setParameterValue("medium");
			}
			
			if (entryDetails2.get(i).getParameterType() == ParameterType.TIME) {
				entryDetails2.get(i).setParameterValue("16:00");
			}
		}
		entryDetails2.add(new Parameter(ParameterType.CATEGORY, "Administrative"));
		Collections.sort(entryDetails2, new ParameterComparator());
		entryToBeEdited2.setParameters(entryDetails2);
		Entry entryAfterUpdate2 = new Entry(entryToBeEdited2);
		expectedEntries.set(2, entryToBeEdited2);
		updateSortingOfEntries(expectedEntries);
		feedback = getFeedbackForSuccessfulEdit(entryBeforeUpdate2, entryAfterUpdate2);
		expectedResponse = createSuccessResponse(feedback, expectedEntries);
		// testing multiple inputs
		testResponseEquality("success response partition for editing multiple parameters of entry", 
			 	              expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("edit 4 from 2 May 2016 6pm to 9pm");
		Entry entryToBeEdited3 = expectedEntries.get(3);
		Entry entryBeforeUpdate3 = new Entry(entryToBeEdited3);
		ArrayList<Parameter> entryDetails3 = entryToBeEdited3.getParameters();
		ArrayList<Parameter> newEntryDetails = new ArrayList<Parameter>();
		for (int i = 0; i < entryDetails3.size(); i++) {
			if (entryDetails3.get(i).getParameterType() == ParameterType.INDEX) {
				newEntryDetails.add(entryDetails3.get(i));
			}
			
			if (entryDetails3.get(i).getParameterType() == ParameterType.NAME) {
				newEntryDetails.add(entryDetails3.get(i));
			}
				
			if (entryDetails3.get(i).getParameterType() == ParameterType.CATEGORY) {
				newEntryDetails.add(entryDetails3.get(i));
			}
		}
		newEntryDetails.add(new Parameter(ParameterType.START_DATE, "02/05/2016"));
		newEntryDetails.add(new Parameter(ParameterType.START_TIME, "18:00"));
		newEntryDetails.add(new Parameter(ParameterType.END_DATE, "02/05/2016"));
		newEntryDetails.add(new Parameter(ParameterType.END_TIME, "21:00"));
		Collections.sort(newEntryDetails, new ParameterComparator());
		entryToBeEdited3.setParameters(newEntryDetails);
		Entry entryAfterUpdate3 = new Entry(entryToBeEdited3);
		expectedEntries.set(3, entryToBeEdited3);
		updateSortingOfEntries(expectedEntries);
		feedback = getFeedbackForSuccessfulEdit(entryBeforeUpdate3, entryAfterUpdate3);
		expectedResponse = createSuccessResponse(feedback, expectedEntries);
		testResponseEquality("success response partition for changing entry type from deadline task to event", 
				             expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("edit 1 at 10:30 4 Mar 2016");
		Entry entryToBeEdited4 = expectedEntries.get(0);
		Entry entryBeforeUpdate4 = new Entry(entryToBeEdited4);
		ArrayList<Parameter> entryDetails4 = entryToBeEdited4.getParameters();
		ArrayList<Parameter> newEntryDetails2 = new ArrayList<Parameter>();
		for (int i = 0; i < entryDetails4.size(); i++) {
			if (entryDetails4.get(i).getParameterType() == ParameterType.INDEX) {
				newEntryDetails2.add(entryDetails4.get(i));
			}
			
			if (entryDetails4.get(i).getParameterType() == ParameterType.NAME) {
				newEntryDetails2.add(entryDetails4.get(i));
			}
			
			if (entryDetails4.get(i).getParameterType() == ParameterType.PRIORITY) {
				newEntryDetails2.add(entryDetails4.get(i));
			}
				
			if (entryDetails4.get(i).getParameterType() == ParameterType.CATEGORY) {
				newEntryDetails2.add(entryDetails4.get(i));
			}
		}
		newEntryDetails2.add(new Parameter(ParameterType.DATE, "04/03/2016"));
		newEntryDetails2.add(new Parameter(ParameterType.TIME, "10:30"));
		Collections.sort(newEntryDetails2, new ParameterComparator());
		entryToBeEdited4.setParameters(newEntryDetails2);
		Entry entryAfterUpdate4 = new Entry(entryToBeEdited4);
		expectedEntries.set(0, entryToBeEdited4);
		updateSortingOfEntries(expectedEntries);
		feedback = getFeedbackForSuccessfulEdit(entryBeforeUpdate4, entryAfterUpdate4);
		expectedResponse = createSuccessResponse(feedback, expectedEntries);
		testResponseEquality("success response partition for changing entry type from event to deadline task", 
				             expectedResponse, actualResponse);
	}
	
	private String getFeedbackForSuccessfulEdit(Entry entryBeforeUpdate, Entry entryAfterUpdate) {
		String feedback = MESSAGE_AFTER_EDIT.concat("<br>").concat("<br>").concat(MESSAGE_FOR_UPDATED_ENTRY);
		feedback = feedback.concat("<br>").concat(entryAfterUpdate.toHTMLString());
		feedback = feedback.concat("<br>").concat(MESSAGE_FOR_OLD_ENTRY);
		feedback = feedback.concat("<br>").concat(entryBeforeUpdate.toHTMLString());
		
		return feedback;
	}
	
	@Test
	public void testResponsesForCompletionAndArchive() {
		Response actualResponse = logic.processCommand("complete ");
		Response expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_NO_PARAMETERS_AFTER_COMMAND);
		testResponseEquality("failure response partition for not providing parameters after complete command", 
				             expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("archive");
		ArrayList<Entry> completedEntries = new ArrayList<Entry>();
		expectedResponse = createSuccessResponse(MESSAGE_EMPTY_ARCHIVE, completedEntries);
		testResponseEquality("success response partition for empty archive", expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("complete Annual Meeting");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_INVALID_INDEX_FORMAT);
		testResponseEquality("failure response partition for providing invalid index format with complete", 
	                         expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("complete 12");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_INVALID_INDEX);
		testResponseEquality("failure response partition for providing invalid index with complete", 
				              expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("complete 2");
		Entry entryCompleted = expectedEntries.remove(1);
		updateSortingOfEntries(expectedEntries);
		String feedback = getSuccessFeedbackForSingleEntryDetails(MESSAGE_AFTER_COMPLETE, entryCompleted);
		expectedResponse = createSuccessResponse(feedback, expectedEntries);
		testResponseEquality("success response partition for completing entry", expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("archive");		
		ArrayList<Parameter> entryDetails = entryCompleted.getParameters();
		for (int i = 0; i < entryDetails.size(); i++) {
			if (entryDetails.get(i).getParameterType() == ParameterType.INDEX) {
				entryDetails.get(i).setParameterValue("1");
			}
		}
		entryCompleted.setParameters(entryDetails);
		completedEntries.add(entryCompleted);
		expectedResponse = createSuccessResponse(MESSAGE_RETRIEVE_ARCHIVE_SUCCESS, completedEntries);
		testResponseEquality("success response partition for retrieving completed entries from archive", 
				              expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("restore Annual Meeting");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_INVALID_INDEX_FORMAT);
		testResponseEquality("failure response partition for providing invalid index format with restore", 
	                         expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("restore 2");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_INVALID_INDEX);
		testResponseEquality("failure response partition for providing invalid index with restore", 
				              expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("restore 1");
		Entry entryRestored = new Entry(entryCompleted);
		expectedEntries.add(entryCompleted);
		updateSortingOfEntries(expectedEntries);
		feedback = getSuccessFeedbackForSingleEntryDetails(MESSAGE_AFTER_RESTORE, entryRestored);
		completedEntries.clear();
		expectedResponse = createSuccessResponse(feedback, completedEntries);
		testResponseEquality("success response partition for restoring completed entry", expectedResponse, 
				              actualResponse);
	}
	
	@Test
	public void testResponsesForView() {
		Response actualResponse = logic.processCommand("view meeting");
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		filteredEntries.add(expectedEntries.get(0));
		filteredEntries.add(expectedEntries.get(1));
		String feedback = String.format(MESSAGE_FILTER_RESULTS, filteredEntries.size());
		Response expectedResponse = createSuccessResponse(feedback, filteredEntries);
		testResponseEquality("success response partition for filtering entries based on substring of entry names", 
				             expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("view pri low");
		filteredEntries.clear();
		filteredEntries.add(expectedEntries.get(2));
		feedback = String.format(MESSAGE_FILTER_RESULTS, filteredEntries.size());
		feedback = feedback.replace("entries", "entry");
		expectedResponse = createSuccessResponse(feedback, filteredEntries);
		testResponseEquality("success response partition for filter results with only 1 entry", expectedResponse, 
				             actualResponse);
		
		actualResponse = logic.processCommand("view from 3 Mar 2016 to 14 Apr 2016 pri H cat marketing");
		filteredEntries.clear();
		filteredEntries.add(expectedEntries.get(1));
		feedback = String.format(MESSAGE_FILTER_RESULTS, filteredEntries.size());
		feedback = feedback.replace("entries", "entry");
		expectedResponse = createSuccessResponse(feedback, filteredEntries);
		// testing multiple inputs
		testResponseEquality("success response partition for filtering entries using multiple filters", 
				             expectedResponse, actualResponse);
	}
	
	@Test
	public void testResponsesForDelete() {
		Response actualResponse = logic.processCommand("delete ");
		Response expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_NO_PARAMETERS_AFTER_COMMAND);
		testResponseEquality("failure response partition for not providing parameters after delete command", 
				             expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("delete Annual company dinner");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_INVALID_INDEX_FORMAT);
		testResponseEquality("failure response partition for providing invalid index format with delete", 
	                         expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("delete 10");
		expectedResponse = createFailureResponse(MESSAGE_ERROR_FOR_INVALID_INDEX);
		testResponseEquality("failure response partition for providing invalid index with delete", 
				              expectedResponse, actualResponse);
		
		actualResponse = logic.processCommand("delete 4");
		Entry entryDeleted = expectedEntries.remove(3);
		updateSortingOfEntries(expectedEntries);
		String feedback = getSuccessFeedbackForSingleEntryDetails(MESSAGE_AFTER_DELETE, entryDeleted);
		expectedResponse = createSuccessResponse(feedback, expectedEntries);
		testResponseEquality("success response partition for deleting entry by index", expectedResponse, 
				             actualResponse);
		
		
		actualResponse = logic.processCommand("delete pri H");
		ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
		filteredEntries.add(expectedEntries.get(0));
		filteredEntries.add(expectedEntries.get(1));
		expectedEntries.remove(1);
		expectedEntries.remove(0);
		updateSortingOfEntries(expectedEntries);
		feedback = getFeedbackForSuccessfulDeleteByFiltering(filteredEntries); 
		expectedResponse = createSuccessResponse(feedback, expectedEntries);
		testResponseEquality("success response partition for deleting multiple entries using filter", 
				             expectedResponse, actualResponse);
	}
	
	private String getFeedbackForSuccessfulDeleteByFiltering(ArrayList<Entry> filteredEntries) {
		String feedback = String.format(MESSAGE_FILTER_RESULTS, filteredEntries.size());
		feedback = feedback.replace("found", "deleted");
		feedback = feedback.replace(".", ":");
		feedback = feedback.concat("<br>");
		for (int i = 0; i < filteredEntries.size(); i++) {
			Entry entry = filteredEntries.get(i);
			feedback = feedback.concat("<br>").concat(entry.toHTMLString());
		}
		
		return feedback;
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
}