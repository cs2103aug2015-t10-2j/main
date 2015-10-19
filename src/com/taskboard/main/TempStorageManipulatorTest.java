package com.taskboard.main;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class TempStorageManipulatorTest {
	
	TempStorageManipulator tempStorageManipulator = new TempStorageManipulator();
	String fileNameNew = "testFileNew.txt";
	String fileNameOpen = "testFileOpen.txt";
	StorageHandler storageHandler = new StorageHandler();
	
	public void createTestFile() throws IOException {
		File testFileOpen = new File("testFileOpen.txt");
		testFileOpen.createNewFile();
		FileWriter writer = new FileWriter(testFileOpen);
		writer.write("NAME: test1");
		writer.write(System.lineSeparator());
		writer.write("NAME: test2");
		writer.write(System.lineSeparator());
		writer.write("NAME: test3");
		writer.write(System.lineSeparator());
		writer.flush();
		writer.close();		
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
		ArrayList<Entry> entries = new ArrayList<Entry>();
		assertEquals(entries, tempStorageManipulator.getTempStorage());
		newFile.delete();
		assert newFile.delete(): true;
	}
	
	public void addEntryToArrayListOfEntries(ArrayList<Entry> entries, String name) {
		Parameter parameter = new Parameter(ParameterType.valueOf("NAME"), name);
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(parameter);
		Entry entry = new Entry();
		entry.setParameters(parameters);
		entries.add(entry);
	}
	@Test
	public void testRepopulate() throws IllegalArgumentException, IOException {
		createTestFile();
		tempStorageManipulator.repopulate(fileNameOpen);
		ArrayList<Entry> entries = new ArrayList<Entry>();
		addEntryToArrayListOfEntries(entries, "test1");
		addEntryToArrayListOfEntries(entries, "test2");
		addEntryToArrayListOfEntries(entries, "test3");
		assertEquals(entries, tempStorageManipulator.getTempStorage());
	}

	@Test
	public void testAddToTempStorage() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditTempStorage() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteFromTempStorage() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetCompletedInTempStorage() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTempStorageToFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTempArchiveToFile() {
		fail("Not yet implemented");
	}

}
