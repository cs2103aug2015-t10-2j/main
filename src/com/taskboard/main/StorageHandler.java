package com.taskboard.main;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

public class StorageHandler {
	
	private static final String MARKER_FOR_NEXT_ENTRY_IN_FILE = "Name:";
	private static final int INDEX_OF_EMPTY_ENTRY = 0;
	
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
				
				if (!detail.isEmpty()) {
					entry.addToDetails(detail);
				}
			}
			
			if (!_entries.isEmpty()) {
				_entries.add(entry);
				_entries.remove(INDEX_OF_EMPTY_ENTRY);
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
			
			/*ArrayList<String> details = entry.getDetails();
		
			for (int i = 0; i < details.size(); i++) {
				String detail = details.get(i);
				fileToAdd.write(detail);
				fileToAdd.write("\n");
				fileToAdd.flush();
			}*/
			addSingleEntryToFile(fileToAdd, entry);
			
			fileToAdd.write("\n");
			fileToAdd.flush();
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
			
			if (i == _entries.size() - 1) {
				entriesList = entriesList.concat(entryDetails);
			} else {
				entriesList = entriesList.concat(entryDetails).concat("\n");
			}
		}
		
		return entriesList;
	}
	
	public boolean isEditingEntriesSuccessful(ArrayList<String> newContent) {
		try {
			File tempStorage = new File("_temp");
			FileWriter fileToAdd = new FileWriter(tempStorage, true);
			ArrayList<String> detailsToBeEdited = new ArrayList<String>();
			
			for (int i = 0; i < _entries.size(); i++) {
				Entry tempEntry = _entries.get(i);
				ArrayList<String> tempEntryDetails = tempEntry.getDetails();
				replaceOldDetailsWithNewDetails(tempEntryDetails, detailsToBeEdited);
				addSingleEntryToFile(fileToAdd, tempEntry);
			}
			
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public void addSingleEntryToFile(FileWriter fileToAdd, Entry entry) {
		try {
			ArrayList<String> details = entry.getDetails();
		
			for (int i = 0; i < details.size(); i++) {
				String detail = details.get(i);
				fileToAdd.write(detail);
				fileToAdd.write("\n");
				fileToAdd.flush();
			}
		} catch (IOException e) {
			return;
		}
	}
	
	public void replaceOldDetailsWithNewDetails(ArrayList<String> oldDetails, ArrayList<String> newDetails) {
		if ((oldDetails.get(0)).equals((newDetails).get(0))) {
			for (int j = 1; j < newDetails.size(); j += 2) {
				for (int k = 1; k < oldDetails.size(); k++) {
					if ((newDetails.get(j)).equals(oldDetails.get(k))) {
						oldDetails.set(k+1, newDetails.get(j));
						break;
					}

				}
			}
		}
	}
	
	public boolean isDeletingSuccessful(String nameOfEntryToBeDeleted) {
		return true;
	}
}
