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
//	private static final int INDEX_OF_FORMATTED_ENTRY = 0;
//	private static final int INDEX_OF_ENTRY_INDEX = 0;
//	private static final int INDEX_OF_DETAIL_TYPE = 0;
//	private static final int INDEX_OF_DETAIL = 1;
//	
	// attributes
	private File _archive;
	private ArrayList<Entry> _completedEntries;
	
	// constructor
	public ArchiveHandler() {
	}
	
	// accessor
	public File getArchive() {
		return _archive;
	}
	
	public boolean isCreatingNewFileSuccessful(String fileName) throws IOException {
		_archive = new File(fileName);
		
		boolean doesFileExist = doesFileExist(_archive);
		
//		assert doesFileExist: false;
		if (!doesFileExist) {
			_archive.createNewFile();
			_completedEntries = new ArrayList<Entry>();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isOpeningExistingFileSuccessful(String fileName) throws IOException{
		_archive = new File(fileName);
		
		boolean doesFileExist = doesFileExist(_archive);
		if (doesFileExist) {
			copyExistingEntriesFromFile();
			return true;
		} else {
			return false;
		}
	}
	
	private void copyExistingEntriesFromFile() {
		try {
			Scanner scanFileToCopy = new Scanner(_archive);
			
			Entry entry = new Entry();
			
			while (scanFileToCopy.hasNext()) {
				String detail = scanFileToCopy.nextLine();
				
				if (detail.contains(MARKER_FOR_NEXT_ENTRY_IN_FILE)) {
					_completedEntries.add(entry);
					entry = new Entry();	
				}
				
				if (!detail.isEmpty()) {
					Parameter parameter = new Parameter();
					parameter.setParameterType(ParameterType.valueOf(detail));
					parameter.setParameterValue(detail);
					entry.addToParameters(parameter);
				}
			}
			
			if (!_completedEntries.isEmpty()) {
				_completedEntries.add(entry);
				_completedEntries.remove(INDEX_OF_EMPTY_ENTRY);
			}
			
			scanFileToCopy.close();	
		} catch (FileNotFoundException e) {
			return;
		}
	}
	
	private boolean doesFileExist(File fileToCheck) {
		if (!fileToCheck.exists()) {
			return false;
		}		
		return true;
	}
	
	public void setStorage(ArrayList<Entry> entries) {
		_completedEntries = entries;
	}
	
}
