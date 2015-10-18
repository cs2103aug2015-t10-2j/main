package com.taskboard.main;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

public class ArchiveHandler {
	
	private static final String MARKER_FOR_NEXT_ENTRY_IN_FILE = "Name:";
	private static final int INDEX_OF_EMPTY_ENTRY = 0;
	private static final String MESSAGE_ERROR_FOR_CREATING_EXISTNG_FILE = "The file already exists.";
	private static final String MESSAGE_ERROR_FOR_OPENING_NON_EXISTING_FILE = "The file does not exists.";

	// attributes
	private File _archive;
//	private ArrayList<Entry> _completedEntries;
	
	// constructor
	public ArchiveHandler() {
	}
	
	// accessor
	public File getArchive() {
		return _archive;
	}
	
	public ArrayList<Entry> createNewFile(String fileName) throws IllegalArgumentException,IOException {
		_archive = new File(fileName);
		
		boolean doesFileExist = doesFileExist(_archive);
		ArrayList<Entry> completedEntries;
//		assert doesFileExist: false;
		if (!doesFileExist) {
			_archive.createNewFile();
			completedEntries = new ArrayList<Entry>();
		} else {
			throw new IllegalArgumentException(MESSAGE_ERROR_FOR_CREATING_EXISTNG_FILE);
		}
		return completedEntries;
	}
	
	public ArrayList<Entry> openExistingFile(String fileName) throws IllegalArgumentException, FileNotFoundException {
		_archive = new File(fileName);
		boolean doesFileExist = doesFileExist(_archive);
		
		ArrayList<Entry> completedEntries;
		
		if (doesFileExist) {
			Scanner scanFileToCopy = new Scanner(_archive);
			completedEntries = copyExistingEntriesFromFile(scanFileToCopy);
			scanFileToCopy.close();	
		} else {
			throw new IllegalArgumentException(MESSAGE_ERROR_FOR_OPENING_NON_EXISTING_FILE);
		}
		
		return completedEntries;
	}
	
	private ArrayList<Entry> copyExistingEntriesFromFile(Scanner scanFileToCopy) {
//			Scanner scanFileToCopy = new Scanner(_archive);
		ArrayList<Entry> completedEntries = new ArrayList<Entry>();
		Entry entry = new Entry();
			
		while (scanFileToCopy.hasNext()) {
			String detail = scanFileToCopy.nextLine();
				
			if (detail.contains(MARKER_FOR_NEXT_ENTRY_IN_FILE)) {
				completedEntries.add(entry);
				entry = new Entry();	
			}
				
			if (!detail.isEmpty()) {
				Parameter parameter = new Parameter();
				parameter.setParameterType(ParameterType.valueOf(detail));
				parameter.setParameterValue(detail);
				entry.addToParameters(parameter);
			}
		}
			
		if (!completedEntries.isEmpty()) {
			completedEntries.add(entry);
			completedEntries.remove(INDEX_OF_EMPTY_ENTRY);
		}
		
		scanFileToCopy.close();	
		return completedEntries;
	}
	
	private boolean doesFileExist(File fileToCheck) {
		if (!fileToCheck.exists()) {
			return false;
		}		
		return true;
	}
	
	public void updateTempStorageToFile(ArrayList<Entry> entries) throws IOException {
		FileWriter fileToAdd = new FileWriter(_archive);
		copyAllEntriesToFile(fileToAdd, entries);
	}
	
	public void copyAllEntriesToFile(FileWriter fileToAdd, ArrayList<Entry> entries) throws IOException {
		for (int i = 0; i < entries.size(); i++) {
			addSingleEntryToFile(fileToAdd, entries.get(i));
		}
	}
	
	public void addSingleEntryToFile(FileWriter fileToAdd, Entry entry) throws IOException {
		ArrayList<Parameter> parameters = entry.getParameters();
		
		for (int i = 0; i < parameters.size(); i++) {
			String entrydetails = parameters.get(i).toString();
			fileToAdd.write(entrydetails);
			fileToAdd.write("\n");
			fileToAdd.flush();
		}
//		fileToAdd.write("\n");
//		fileToAdd.flush();
	}
}
