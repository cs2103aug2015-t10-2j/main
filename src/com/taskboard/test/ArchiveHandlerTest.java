package com.taskboard.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import com.taskboard.main.filehandler.ArchiveHandler;
import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.ParameterType;

public class ArchiveHandlerTest {
	
	//@@author A0129889A 
	
	ArchiveHandler archiveHandler = new ArchiveHandler();

	private boolean doesFileExist(File fileToCheck) {
		if (fileToCheck.exists()) {
			return true;
		}
		return false;
	}

	private void createTestFile() throws IOException {
		String fileName = "tempTestFile.arc";
		File testFile = new File(fileName);
		if (!doesFileExist(testFile)) {
			testFile.createNewFile();
		}
	}
	

	@Test
	public void createNewFileTest() throws IllegalArgumentException, IOException {
		String fileName = "testFile";
		archiveHandler.createNewFile(fileName);
		File fileToCheck = new File("testFile.arc");
		boolean result = doesFileExist(fileToCheck);
		assertEquals(true, result);
		fileToCheck.delete();
		assert fileToCheck.delete() : true;
	}

	@Test
	public void openExistingFileTest() throws IOException {
		createTestFile();
		String fileName = "tempTestFile";
		archiveHandler.openExistingFile(fileName);
		File fileToCheck = new File("tempTestFile.arc");
		boolean result = doesFileExist(fileToCheck);
		assertEquals(true, result);
		fileToCheck.delete();
		assert fileToCheck.delete() : true;
	}

	private String convertArrayListToString(ArrayList<Entry> entries) {
		String result = "";
		for (int i = 0; i < entries.size(); i++) {
			result += entries.toString();
		}
		return result;
	}

	private Entry createNewEntry(int i, String parameterValue) {
		Entry entry = new Entry();
		Parameter parameter1 = new Parameter(ParameterType.INDEX, String.valueOf(i));
		Parameter parameter2 = new Parameter(ParameterType.NAME, parameterValue);
		entry.addToParameters(parameter1);
		entry.addToParameters(parameter2);
		return entry;
	}

	@Test
	public void updateTempStorageToFileTest() throws IOException {
		String fileName = "tempTestFile";
		archiveHandler.createNewFile(fileName);
		ArrayList<Entry> entries = new ArrayList<Entry>();
		Entry entry1 = createNewEntry(1, "test1");
		Entry entry2 = createNewEntry(2, "test2");
		entries.add(entry1);
		entries.add(entry2);
		archiveHandler.updateTempStorageToFile(entries);
		String expected = convertArrayListToString(entries);
		String actual = convertArrayListToString(archiveHandler.openExistingFile("tempTestFile"));
		assertEquals(expected, actual);
	}

}
