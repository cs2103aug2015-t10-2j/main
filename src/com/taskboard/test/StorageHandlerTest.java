package com.taskboard.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.taskboard.main.*;
import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.ParameterType;

import org.junit.Test;

public class StorageHandlerTest {
	
	StorageHandler storageHandler = new StorageHandler();
	
	private boolean doesFileExist(File fileToCheck) {
		if (fileToCheck.exists()) {
			return true;
		}
		return false;
	}
	
	private void createTestFile() throws IOException {
		String fileName = "tempTestFile.str";
		File testFile = new File(fileName);
		if (!doesFileExist(testFile)) {
			testFile.createNewFile();
		}
	}

	@Test
	public void createNewFileTest() throws IllegalArgumentException, IOException {
		String fileName = "testFile";
		storageHandler.createNewFile(fileName);
		File fileToCheck = new File("testFile.str");
		boolean result = doesFileExist(fileToCheck);
		assertEquals(true, result);
		fileToCheck.delete();
		assert fileToCheck.delete(): true;
	}
	
	@Test
	public void openExistingFileTest() throws IOException {
		createTestFile();
		String fileName = "tempTestFile";
		storageHandler.openExistingFile(fileName);
		File fileToCheck = new File("tempTestFile.str");
		boolean result = doesFileExist(fileToCheck);
		assertEquals(true, result);
		fileToCheck.delete();
		assert fileToCheck.delete(): true;
	}
	
	private String convertArrayListToString(ArrayList<Entry> entries) {
		String result = "";
		for (int i = 0; i< entries.size(); i++) {
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
		storageHandler.createNewFile(fileName);
		ArrayList<Entry> entries = new ArrayList<Entry>();
		Entry entry1 = createNewEntry(1, "test1");
		Entry entry2 = createNewEntry(2, "test2");
		entries.add(entry1);
		entries.add(entry2);
		storageHandler.updateTempStorageToFile(entries);
		String expected = convertArrayListToString(entries);
		String actual = convertArrayListToString(storageHandler.openExistingFile("tempTestFile"));
		assertEquals(expected, actual);
	}
}
