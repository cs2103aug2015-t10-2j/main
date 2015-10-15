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
	private static final int INDEX_OF_FORMATTED_ENTRY = 0;
	private static final int INDEX_OF_ENTRY_INDEX = 0;
	private static final int INDEX_OF_DETAIL_TYPE = 0;
	private static final int INDEX_OF_DETAIL = 1;
	
	// attributes
	
//	private static StorageHandler instance = null;
	private File _original;
	private ArrayList<Entry> _entries;
	
	// constructor
	
	public StorageHandler() {
		
	}
	
	// accessor
	
	public ArrayList<Entry> getEntryListFromStorage() {
		return _entries;
	}
	
	
//	public static StorageHandler getInstance() {
//		if (instance == null) {
//			instance = new StorageHandler();
//		}
//		return instance;
//	}
	
	public boolean isCreatingNewFileSuccessful(String fileName) throws IOException {
		_original = new File(fileName);
		
		boolean doesFileExist = doesFileExist(_original);
		
//		assert doesFileExist: false;
		if (!doesFileExist) {
			_original.createNewFile();
			_entries = new ArrayList<Entry>();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isOpeningExistingFileSuccessful(String fileName) throws IOException{
		_original = new File(fileName);
		
		boolean doesFileExist = doesFileExist(_original);
		if (doesFileExist) {
			copyExistingEntriesFromFile();
			return true;
		} else {
			return false;
		}
	}
	
	private boolean doesFileExist(File fileToCheck) {
		if (!fileToCheck.exists()) {
			return false;
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
					Parameter parameter = new Parameter();
					parameter.setParameterType(ParameterType.valueOf(detail));
					parameter.setParameterValue(detail);
					entry.addToParameters(parameter);
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
	
	public void setStorage(ArrayList<Entry> entries) {
		_entries = entries;
	}
	
//	public boolean isAddToFileSuccessful(Entry entry) {
//		try {
//			FileWriter fileToAdd = new FileWriter(_original, true);
//			_entries.add(entry);
//			
//			addSingleEntryToFile(fileToAdd, entry);
//			fileToAdd.close();
//			
//			return true;
//		} catch (IOException e) {
//			return false;
//		}
//	}
	
//	public String retrieveEntriesInFile() {
//		String entriesList = "";
//		
//		for (int i = 0; i < _entries.size(); i++) {
//			Entry entry = _entries.get(i);
//			String entryDetails = entry.toString();
//			
//			if (i == _entries.size() - 1) {
//				entriesList = entriesList.concat(entryDetails);
//			} else {
//				assert i < _entries.size() -1;
//				entriesList = entriesList.concat(entryDetails).concat("\n");
//			}
//		}
//		
//		return entriesList;
//	}
	
//	public boolean isEditInFileSuccessful(ArrayList<String> newContent) {
//		try {
//			FileWriter fileToEdit = new FileWriter(_original);
//			
//			Integer indexOfEditedEntry = Integer.valueOf(newContent.get(INDEX_OF_ENTRY_INDEX));
//			
////			for (int i = 0; i < _entries.size(); i++) {
////				Entry entry = _entries.get(i);
////				ArrayList<String> entryDetails = entry.getDetails();
////				Integer formattedTaskIndex = Integer.valueOf(entryDetails.get(INDEX_OF_FORMATTED_ENTRY));
////				
////				if (formattedTaskIndex == indexOfEditedTask) {
////					replaceWithNewContent(entryDetails, newContent);
////					entry.setDetails(entryDetails);
////					_entries.set(i, entry);
////				}				
////			}
//			
//			Entry editedEntry = _entries.get(indexOfEditedEntry); 
//			ArrayList<Parameter> entryDetails = editedEntry.getDetails();
//			replaceWithNewContent(entryDetails, newContent);
//			editedEntry.setDetails(entryDetails);
//			_entries.set(indexOfEditedEntry, editedEntry);
//			
//			copyAllEntriesToFile(fileToEdit, _entries);
//			fileToEdit.close();
//			
//			return true;
//		} catch (IOException e) {
//			return false;
//		}
//	}

//	private void replaceWithNewContent(ArrayList<Parameter> entryDetails, ArrayList<String> newContent) {
//		boolean isDetailPresent = false;
//		
//		for (int i = 1; i < newContent.size(); i++) {
//			String newFormattedDetail = newContent.get(i);
//			String[] newFormattedDetailSegments = newFormattedDetail.split(":");
//			
//			for (int j = 0; j < entryDetails.size(); j++) {
//				String existingFormattedDetail = entryDetails.get(j).getParameterValue();
//				
//				if (existingFormattedDetail.contains(newFormattedDetailSegments[INDEX_OF_DETAIL_TYPE])) {
//					entryDetails.set(j, newFormattedDetail);
//					isDetailPresent = true;
//				} 
//			}
//			
//			if (!isDetailPresent) {
//				entryDetails.add(newFormattedDetail);
//			}
//			
//			isDetailPresent = false;
//		}
//	}
//	
//	public String retrieveDetail(String searchTaskName, String detailType) {
//		String detail = "";
//		
//		for (int i = 0; i < _entries.size(); i++) {
//			Entry entry = _entries.get(i);
//			ArrayList<String> entryDetails = entry.getDetails();
//			String formattedTaskName = entryDetails.get(INDEX_OF_FORMATTED_ENTRY);
//			String[] formattedTaskNameSegments = formattedTaskName.split(": ");
//			String taskName = formattedTaskNameSegments[INDEX_OF_DETAIL];
//			
//			if (taskName.equals(searchTaskName)) {
//				detail = getDetailFromEntry(entryDetails, detailType);
//			}	
//		}
//		return detail;
//	}
//	
//	private String getDetailFromEntry(ArrayList<String> entryDetails, String detailType) {
//		String detail = "";
//		
//		for (int i = 0; i < entryDetails.size(); i++) {
//			String formattedDetail = entryDetails.get(i);
//			
//			if (formattedDetail.contains(detailType)) {
//				String[] formattedDetailSegments = formattedDetail.split(": ");
//				detail = formattedDetailSegments[INDEX_OF_DETAIL];
//			}
//		}
//		
//		return detail;
//	}
	
	public void copyAllEntriesToFile(FileWriter fileToAdd, ArrayList<Entry> _entries) throws IOException {
		for (int i = 0; i < _entries.size(); i++) {
			addSingleEntryToFile(fileToAdd, _entries.get(i));
		}
	}
	
	public void addSingleEntryToFile(FileWriter fileToAdd, Entry entry) throws IOException {
		ArrayList<Parameter> parameters = entry.getParameters();
		
		for (int i = 0; i < parameters.size(); i++) {
			String detail = parameters.get(i).getParameterValue();
			fileToAdd.write(detail);
			fileToAdd.write("\n");
			fileToAdd.flush();
		}
		fileToAdd.write("\n");
		fileToAdd.flush();
	}
	
//	public boolean isDeleteFromFileSuccessful(Integer i) {
//		try {
//			FileWriter fileToAdd = new FileWriter(_original);
//			_entries.remove(i);
//			copyAllEntriesToFile(fileToAdd, _entries);
//			fileToAdd.close();	
//			
//			return true;
//		} catch (IOException e) {
//			return false;
//		}		
//	}

//	public boolean isEntryCompletedSuccessful(Integer i) throws IOException {
//		if (_entries.get(i).getCompletionStatus()) {
//			return true;
//		} else {
//			assert _entries.get(i).getCompletionStatus() : false;
//			return false;
//		}
//	}
}
