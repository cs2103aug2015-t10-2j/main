package com.taskboard.main;

//import java.io.File;
//import java.io.FileWriter;
//import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
//import java.util.Scanner;

public class TempStorageManipulator {
	
	// attributes
//	private static TempStorageManipulator instance = null;
	private ArrayList<Entry> _tempStorage;
	private ArrayList<Entry> _tempArchive;
	StorageHandler _storageHandler = new StorageHandler();
	ArchiveHandler _archiveHandler = new ArchiveHandler();
	
	// constructor
	private TempStorageManipulator() {
		
	}
	
	// accessor
	public ArrayList<Entry> getEntryListFromStorage() {
		return _tempStorage;
	}
	
	
//	public static TempStorageManipulator getInstance() {
//		if (instance == null) {
//			instance = new TempStorageManipulator();
//		}
//		return instance;
//	}
	
	public ArrayList<Entry> fileStatus(String fileName) throws IOException {
//		StorageHandler storageHandler = new StorageHandler();
		boolean isItANewFile = _storageHandler.isCreatingNewFileSuccessful(fileName);
		boolean isItAnExistingFile = _storageHandler.isOpeningExistingFileSuccessful(fileName);
		
		if (isItANewFile) {
			assert isItAnExistingFile: false;
			_tempStorage = new ArrayList<Entry>();
		}
		
		if (isItAnExistingFile) {
			assert isItANewFile = false;
			_tempStorage = _storageHandler.getEntryListFromStorage();
		}
		
		return _tempStorage;		
	}
	
	public void addToTempStorage(Entry entry) {
		_tempStorage.add(entry);
		setTempStorageToFile(_tempStorage);
		
	}
	
	public void editTempStorage(Integer i, ArrayList<String> newContent) {
		Entry editedEntry = _tempStorage.get(i);
		ArrayList<Parameter> entryDetails = editedEntry.getParameters();
		replaceWithNewContent(entryDetails, newContent);
		editedEntry.setParameters(entryDetails);
		_tempStorage.set(i, editedEntry);
		setTempStorageToFile(_tempStorage);
	}
	
	private void replaceWithNewContent(ArrayList<Parameter> entryDetails, ArrayList<String> newContent) {
		boolean isDetailPresent = false;
		
		for (int i = 1; i < newContent.size(); i++) {
			String newFormattedDetail = newContent.get(i);
//			String[] newFormattedDetailSegments = newFormattedDetail.split(":");
			
			for (int j = 0; j < entryDetails.size(); j++) {
//				String existingFormattedDetail = entryDetails.get(j).getParameterValue();
				
				if (entryDetails.get(j).getParameterType() == ParameterType.valueOf(newFormattedDetail)) {					
					entryDetails.get(j).setParameterValue(newFormattedDetail);
					isDetailPresent = true;
				} 
			}
			
			if (!isDetailPresent) {
				Parameter newParameter = new Parameter();
				newParameter.setParameterType(ParameterType.valueOf(newFormattedDetail));
				newParameter.setParameterValue(newFormattedDetail);
				entryDetails.add(newParameter);
			}
			
			isDetailPresent = false;
		}
	}
	
	public void deleteFromTempStorage(Integer i) {
		_tempStorage.remove(i);
		setTempStorageToFile(_tempStorage);

	}		

	public void setCompletedInTempStorage(Integer i) throws IOException {
		Entry entry = _tempStorage.get(i);
		entry.setCompleted(true);
		_tempStorage.remove(i);
		_tempArchive.add(entry);
		setTempStorageToFile(_tempStorage);
		setTempArchiveToFile(_tempArchive);
	}

	public void setTempStorageToFile(ArrayList<Entry> entries) {
		_storageHandler.setStorage(entries);
	}
	
	public void setTempArchiveToFile(ArrayList<Entry> entries) {
		_archiveHandler.setStorage(entries);
	}
}
