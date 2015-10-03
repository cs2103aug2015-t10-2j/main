package com.taskboard.main;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

public class StorageHandler {
	
	private static final String MARKER_FOR_NEXT_ENTRY_IN_FILE = "Name:";
	
	// attributes
	
	private File _original;
	private ArrayList<Entry> _entries;
	
	// constructor
	
	public StorageHandler() {
		
	}
	
	public boolean isSetUpSuccessful(String fileName) {
		_original = new File(fileName);
		
		boolean isFileCreationSuccessful = isFileValid(_original);
		
		if (isFileCreationSuccessful) {
			_entries = new ArrayList<Entry>();
			copyExistingEntriesFromFile();
		}
		
		return isFileCreationSuccessful;
	}
	
	private boolean isFileValid(File fileToCheck) {
		if (!fileToCheck.exists()) {
			try {
				fileToCheck.createNewFile();
			} catch (IOException exobj) {
				return false;
			}
		}
		
		return true;
	}
	
	private void copyExistingEntriesFromFile() {
		try {
			Scanner scanFileToCopy = new Scanner(_original);
			
			Entry entry = new Entry();
			
			while (scanFileToCopy.hasNext()) {
				String detail = scanFileToCopy.nextLine();
				
				if (detail.contains(MARKER_FOR_NEXT_ENTRY_IN_FILE)) {
					_entries.add(entry);
					entry = new Entry();	
				}
				
				entry.addToDetails(detail);
			}
			
			if (!_entries.isEmpty()) {
				_entries.remove(0);
			}
			
			scanFileToCopy.close();
			
		} catch (FileNotFoundException e) {
			return;
		}
	}
	
	public boolean isAddToFileSuccessful(Entry entry) {
		try {
			FileWriter fileToAdd = new FileWriter(_original, true);
			_entries.add(entry);
			
			ArrayList<String> details = entry.getDetails();
		
			for (int i = 0; i < details.size(); i++) {
				String detail = details.get(i);
				fileToAdd.write(detail);
				fileToAdd.write("\n");
				fileToAdd.flush();
			}
			
			fileToAdd.close();
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
	
	public String retrieveEntriesInFile() {
		String entriesList = "";
		
		for (int i = 0; i < _entries.size(); i++) {
			Entry entry = _entries.get(i);
			String entryDetails = entry.toString();
			entriesList = entriesList.concat(entryDetails);
		}
		
		return entriesList;
	}
}
