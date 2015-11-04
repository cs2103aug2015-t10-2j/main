//@@author A0129889A 
package com.taskboard.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import com.taskboard.main.*;
import com.taskboard.main.filehandler.ArchiveHandler;
import com.taskboard.main.filehandler.StorageHandler;
import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.ParameterType;

public class TempStorageManipulatorTest {	
	TempStorageManipulator tempStorageManipulator = new TempStorageManipulator();
	String fileNameNew = "testFileNew";
	String fileNameOpen = "testFileOpen";
	StorageHandler storageHandler = new StorageHandler();
	ArchiveHandler archiveHandler = new ArchiveHandler();
	ArrayList<Entry> _entries = new ArrayList<Entry>();
	ArrayList<Entry> _completedEntries = new ArrayList<Entry>();
	
	public void createTestFile() throws IOException {
		File testFileOpen = new File("testFileOpen.str");
		testFileOpen.createNewFile();
		File archiveFileOpen = new File("testFileOpen.arc");
		archiveFileOpen.createNewFile();
		FileWriter writer = new FileWriter(testFileOpen);
		writer.write("INDEX: 1");
		writer.write(System.lineSeparator());
		writer.write("NAME: test1");
		writer.write(System.lineSeparator());
		writer.write(System.lineSeparator());
		writer.write("INDEX: 2");
		writer.write(System.lineSeparator());
		writer.write("NAME: test2");
		writer.write(System.lineSeparator());
		writer.write(System.lineSeparator());
		writer.write("INDEX: 3");
		writer.write(System.lineSeparator());
		writer.write("NAME: test3");
		writer.write(System.lineSeparator());
		writer.write(System.lineSeparator());
		writer.flush();
		writer.close();		
		FileWriter writerArchive = new FileWriter(archiveFileOpen);
		writerArchive.write("NAME: test2");
		writerArchive.write(System.lineSeparator());
		writerArchive.write(System.lineSeparator());
		writerArchive.flush();
		writerArchive.close();
	}

	
	@Test
	public void testGetTempStorage() {
		ArrayList<Entry> entries = null;
		assertEquals(entries, tempStorageManipulator.getTempStorage());
	}

	@Test
	public void testInitialise() throws IllegalArgumentException, IOException {
		tempStorageManipulator.initialise(fileNameNew);
		File newFile = new File("testFileNew.str");
		File newArchive = new File("testFileNew.arc");
		ArrayList<Entry> entries = new ArrayList<Entry>();
		assertEquals(entries, tempStorageManipulator.getTempStorage());
		
		newFile.delete();
		newArchive.delete();
		assert newFile.delete(): true;
		assert newArchive.delete(): true;
	}
	
	public void addEntryToArrayListOfEntries(ArrayList<Entry> entries, String name, int i) {
		Parameter nameParameter = new Parameter(ParameterType.NAME, name);
		Parameter indexParameter = new Parameter(ParameterType.INDEX, String.valueOf(i));
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(indexParameter);
		parameters.add(nameParameter);
		Entry entry = new Entry();
		entry.setParameters(parameters);
		entries.add(entry);
	}
	
	public String convertArrayListToString(ArrayList<Entry> entries) {
		String result = "";
		for (int i = 0; i< entries.size(); i++) {
			result += entries.toString();
		}
		return result;
	}
	
	@Test
	public void testRepopulate() throws IllegalArgumentException, IOException {
		createTestFile();
		File newFile = new File("testFileOpen.str");
		File newArchive = new File("testFileOpen.arc");
		
		tempStorageManipulator.repopulate(fileNameOpen);
		
		addEntryToArrayListOfEntries(_entries, "test1", 1);
		addEntryToArrayListOfEntries(_entries, "test2", 2);
		addEntryToArrayListOfEntries(_entries, "test3", 3);
		String expected = convertArrayListToString(_entries);
		String actual = convertArrayListToString(tempStorageManipulator.getTempStorage());
		assertEquals(expected, actual);
		
		newFile.delete();
		newArchive.delete();
		assert newFile.delete(): true;
		assert newArchive.delete(): true;
	}

	@Test
	public void testAddToTempStorage() throws IOException {
		createTestFile();
		File newFile = new File("testFileOpen.str");
		File newArchive = new File("testFileOpen.arc");
		
		Parameter nameParameter = new Parameter(ParameterType.NAME, "test4");
		Parameter indexParameter = new Parameter(ParameterType.INDEX, String.valueOf(4));
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(indexParameter);
		parameters.add(nameParameter);
		Entry entry = new Entry();
		entry.setParameters(parameters);
		tempStorageManipulator.repopulate(fileNameOpen);
		tempStorageManipulator.addToTempStorage(entry);
		
		addEntryToArrayListOfEntries(_entries, "test1", 1);
		addEntryToArrayListOfEntries(_entries, "test2", 2);
		addEntryToArrayListOfEntries(_entries, "test3", 3);
		addEntryToArrayListOfEntries(_entries, "test4", 4);
		
		String expected = convertArrayListToString(_entries);
		String actual = convertArrayListToString(tempStorageManipulator.getTempStorage());
		assertEquals(expected, actual);

		newFile.delete();
		newArchive.delete();
		assert newFile.delete(): true;
		assert newArchive.delete(): true;
	}

	@Test
	public void testEditTempStorage() throws IllegalArgumentException, IOException {
		createTestFile();
		File newFile = new File("testFileOpen.str");
		File newArchive = new File("testFileOpen.arc");
		
		tempStorageManipulator.repopulate(fileNameOpen);
		int i = 1;
		Parameter nameParameter = new Parameter(ParameterType.NAME, "test4");
		Parameter indexParameter = new Parameter(ParameterType.INDEX, String.valueOf(2));
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(indexParameter);
		parameters.add(nameParameter);
		tempStorageManipulator.editTempStorage(i, parameters, false);
		
		addEntryToArrayListOfEntries(_entries, "test1", 1);
		addEntryToArrayListOfEntries(_entries, "test4", 2);
		addEntryToArrayListOfEntries(_entries, "test3", 3);
		String expected = convertArrayListToString(_entries);
		String actual = convertArrayListToString(tempStorageManipulator.getTempStorage());
		assertEquals(expected, actual);

		newFile.delete();
		newArchive.delete();
		assert newFile.delete(): true;
		assert newArchive.delete(): true;
	}

	@Test
	public void testDeleteFromTempStorage() throws IOException {
		createTestFile();
		File newFile = new File("testFileOpen.str");
		File newArchive = new File("testFileOpen.arc");
		
		tempStorageManipulator.repopulate(fileNameOpen);
		int i = 1;
		tempStorageManipulator.deleteFromTempStorage(i);
		
		addEntryToArrayListOfEntries(_entries, "test1", 1);
		addEntryToArrayListOfEntries(_entries, "test3", 2);
		String expected = convertArrayListToString(_entries);
		String actual = convertArrayListToString(tempStorageManipulator.getTempStorage());
		assertEquals(expected, actual);

		newFile.delete();
		newArchive.delete();
		assert newFile.delete(): true;
		assert newArchive.delete(): true;
	}

	@Test
	public void testSetCompletedInTempStorage() throws IOException {
		createTestFile();
		File newFile = new File("testFileOpen.str");
		File newArchive = new File("testFileOpen.arc");
		
		tempStorageManipulator.repopulate(fileNameOpen);
		int i = 1;
		tempStorageManipulator.setCompletedInTempStorage(i);
		
		addEntryToArrayListOfEntries(_entries, "test1", 1);
		addEntryToArrayListOfEntries(_entries, "test3", 2);
		addEntryToArrayListOfEntries(_completedEntries, "test2", 1);
		
		String expected = convertArrayListToString(_entries);
		String actual = convertArrayListToString(tempStorageManipulator.getTempStorage());
		assertEquals(expected, actual);
		
		newFile.delete();
		newArchive.delete();
		assert newFile.delete(): true;
		assert newArchive.delete(): true;
	}
}
