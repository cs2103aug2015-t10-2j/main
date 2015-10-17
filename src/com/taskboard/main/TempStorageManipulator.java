package com.taskboard.main;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;

public class TempStorageManipulator {
	
	// attributes

	private ArrayList<Entry> _tempStorage;
	private ArrayList<Entry> _tempArchive;
	StorageHandler _storageHandler;
	ArchiveHandler _archiveHandler;
	
	// constructor
	
	public TempStorageManipulator() {
		_storageHandler = new StorageHandler();
		_archiveHandler = new ArchiveHandler();
	}
	
	// accessor
	
	public ArrayList<Entry> getTempStorage() {
		return _tempStorage;
	}
	
	public void initialise(String fileName) throws IllegalArgumentException, IOException  {
		_tempStorage = _storageHandler.createNewFile(fileName);	
	}
	
	public void repopulate(String fileName) throws IllegalArgumentException, FileNotFoundException {
		_tempStorage = _storageHandler.openExistingFile(fileName);
	}
	
	public void addToTempStorage(Entry entry) throws IOException {
		_tempStorage.add(entry);
		_storageHandler.updateTempStorageToFile(_tempStorage);	
	}
	
	public void editTempStorage(Integer i, ArrayList<String> newContent) throws IOException {
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
	
	public void deleteFromTempStorage(Integer i) throws IOException {
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

//	public void setTempStorageToFile(ArrayList<Entry> entries) throws IOException {
//		_storageHandler.setStorage(entries);
//	}
	
//	public void setTempArchiveToFile(ArrayList<Entry> entries) {
//		_archiveHandler.setStorage(entries);
//	}
}
