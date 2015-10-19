package com.taskboard.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import com.taskboard.main.*;

public class TempStorageManipulatorTest {
	
	TempStorageManipulator tempStorageManipulator = new TempStorageManipulator();
	String fileNameNew = "testFileNew.txt";
	String fileNameOpen = "testFileOpen.txt";
	StorageHandler storageHandler = new StorageHandler();
	ArchiveHandler archiveHandler = new ArchiveHandler();
	ArrayList<Entry> _entries = new ArrayList<Entry>();
	ArrayList<Entry> _completedEntries = new ArrayList<Entry>();
	
	public void createTestFile() throws IOException {
		File testFileOpen = new File("testFileOpen.txt");
		testFileOpen.createNewFile();
		File archiveFileOpen = new File("archiveOftestFileOpen.txt");
		archiveFileOpen.createNewFile();
		FileWriter writer = new FileWriter(testFileOpen);
		writer.write("NAME: test1");
		writer.write(System.lineSeparator());
		writer.write("NAME: test2");
		writer.write(System.lineSeparator());
		writer.write("NAME: test3");
		writer.write(System.lineSeparator());
		writer.flush();
		writer.close();		
		FileWriter writerArchive = new FileWriter(archiveFileOpen);
		writerArchive.write("NAME: test2");
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
		File newFile = new File("testFileNew.txt");
		File newArchive = new File("archiveOftestFileNew.txt");
		ArrayList<Entry> entries = new ArrayList<Entry>();
		assertEquals(entries, tempStorageManipulator.getTempStorage());
		
		newFile.delete();
		newArchive.delete();
		assert newFile.delete(): true;
		assert newArchive.delete(): true;
	}
	
	public void addEntryToArrayListOfEntries(ArrayList<Entry> entries, String name) {
		Parameter parameter = new Parameter(ParameterType.valueOf("NAME"), name);
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(parameter);
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
		File newFile = new File("testFileOpen.txt");
		File newArchive = new File("archiveOftestFileOpen.txt");
		
		tempStorageManipulator.repopulate(fileNameOpen);
		
		addEntryToArrayListOfEntries(_entries, "test1");
		addEntryToArrayListOfEntries(_entries, "test2");
		addEntryToArrayListOfEntries(_entries, "test3");
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
		File newFile = new File("testFileOpen.txt");
		File newArchive = new File("archiveOftestFileOpen.txt");
		
		Parameter parameter = new Parameter(ParameterType.valueOf("NAME"), "test4");
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(parameter);
		Entry entry = new Entry();
		entry.setParameters(parameters);
		tempStorageManipulator.repopulate(fileNameOpen);
		tempStorageManipulator.addToTempStorage(entry);
		
		addEntryToArrayListOfEntries(_entries, "test1");
		addEntryToArrayListOfEntries(_entries, "test2");
		addEntryToArrayListOfEntries(_entries, "test3");
		addEntryToArrayListOfEntries(_entries, "test4");
		
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
		File newFile = new File("testFileOpen.txt");
		File newArchive = new File("archiveOftestFileOpen.txt");
		
		tempStorageManipulator.repopulate(fileNameOpen);
		int i = 1;
		Parameter parameter = new Parameter(ParameterType.valueOf("NAME"), "test4");
		ArrayList<Parameter> newParameters = new ArrayList<Parameter>();
		newParameters.add(parameter);
		tempStorageManipulator.editTempStorage(i, newParameters, false);
		
		addEntryToArrayListOfEntries(_entries, "test1");
		addEntryToArrayListOfEntries(_entries, "test4");
		addEntryToArrayListOfEntries(_entries, "test3");
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
		File newFile = new File("testFileOpen.txt");
		File newArchive = new File("archiveOftestFileOpen.txt");
		
		tempStorageManipulator.repopulate(fileNameOpen);
		int i = 1;
		tempStorageManipulator.deleteFromTempStorage(i);
		
		addEntryToArrayListOfEntries(_entries, "test1");
		addEntryToArrayListOfEntries(_entries, "test3");
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
		File newFile = new File("testFileOpen.txt");
		File newArchive = new File("archiveOftestFileOpen.txt");
		
		tempStorageManipulator.repopulate(fileNameOpen);
		int i = 1;
		tempStorageManipulator.setCompletedInTempStorage(i);
		
		addEntryToArrayListOfEntries(_entries, "test1");
		addEntryToArrayListOfEntries(_entries, "test3");
		addEntryToArrayListOfEntries(_completedEntries, "test2");
		
		String expected = convertArrayListToString(_entries);
		String actual = convertArrayListToString(tempStorageManipulator.getTempStorage());
		assertEquals(expected, actual);
		
		newFile.delete();
		newArchive.delete();
		assert newFile.delete(): true;
		assert newArchive.delete(): true;
	}
}
