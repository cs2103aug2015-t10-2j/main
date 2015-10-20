package com.taskboard.main;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.*;

public class TempStorageManipulator {

	// attributes

	private ArrayList<Entry> _tempStorage;
	private ArrayList<Entry> _tempArchive;
	StorageHandler _storageHandler;
	ArchiveHandler _archiveHandler;

	private static Logger logger = Logger.getLogger("TempStorageManipulator");
	// constructor

	public TempStorageManipulator() {
		_storageHandler = new StorageHandler();
		_archiveHandler = new ArchiveHandler();
	}

	// accessor

	public ArrayList<Entry> getTempStorage() {
		return _tempStorage;
	}

	public void initialise(String fileName) throws IllegalArgumentException, IOException {
		_tempStorage = _storageHandler.createNewFile(fileName);
		_tempArchive = _archiveHandler.createNewFile(fileName);
		logger.log(Level.INFO, "Initialise a new temporary storage.");
	}

	public void repopulate(String fileName) throws IllegalArgumentException, FileNotFoundException {
		_tempStorage = _storageHandler.openExistingFile(fileName);
		_tempArchive = _archiveHandler.openExistingFile(fileName);
		logger.log(Level.INFO, "Repopulate a temporary storage.");
	}

	public void addToTempStorage(Entry entry) throws IOException {
		_tempStorage.add(entry);
		Collections.sort(_tempStorage, new DateComparator());
		_storageHandler.updateTempStorageToFile(_tempStorage);
		logger.log(Level.INFO, "Add entry into temp storage.");
	}

	public void editTempStorage(int i, ArrayList<Parameter> newContent, boolean isEntryTypeChanged) throws IOException {
		Entry editedEntry = _tempStorage.get(i);
		ArrayList<Parameter> entryDetails = editedEntry.getParameters();
		replaceWithNewContent(entryDetails, newContent, isEntryTypeChanged);
		logger.log(Level.INFO, "Replace old entries with new ones.");
		editedEntry.setParameters(entryDetails);
		_tempStorage.set(i, editedEntry);
		setTempStorageToFile(_tempStorage);
	}

	private void replaceWithNewContent(ArrayList<Parameter> entryDetails, ArrayList<Parameter> newContent,
			boolean isEntryTypeChanged) {
		boolean isDetailPresent = false;

		for (int i = 0; i < newContent.size(); i++) {
			Parameter newFormattedDetail = newContent.get(i);

			if (!isEntryTypeChanged) {
				for (int j = 0; j < entryDetails.size(); j++) {
					if (entryDetails.get(j).getParameterType() == newFormattedDetail.getParameterType()) {
						entryDetails.get(j).setParameterValue(newFormattedDetail.getParameterValue());
						logger.log(Level.INFO, "Entry to be edited found and replaced.");
						isDetailPresent = true;
					}
				}

				if (!isDetailPresent) {
//					assert isDetailPresent: false;
					Parameter newParameter = new Parameter();
					newParameter.setParameterType(newFormattedDetail.getParameterType());
					newParameter.setParameterValue(newFormattedDetail.getParameterValue());
					logger.log(Level.INFO, "New parameters added into an existing entry.");
					entryDetails.add(newParameter);
				}

				isDetailPresent = false;
			} else {
				assert isEntryTypeChanged: true;
				logger.log(Level.INFO, "Type of Entry is changed.");
				Entry tempEntry = new Entry();
				ArrayList<Parameter> tempParameters = tempEntry.getParameters();
				for (int k = 0; k < entryDetails.size(); k++) {
					if ((entryDetails.get(k).getParameterType() == ParameterType.valueOf("NAME")) || 
					(entryDetails.get(k).getParameterType() == ParameterType.valueOf("INDEX")) ||
					(entryDetails.get(k).getParameterType() == ParameterType.valueOf("CATEGORY")) ||
					(entryDetails.get(k).getParameterType() == ParameterType.valueOf("PRIORITY"))) {
						tempParameters.add(entryDetails.get(k));
					}
				Parameter newParameter = new Parameter();
				newParameter.setParameterType(newFormattedDetail.getParameterType());
				newParameter.setParameterValue(newFormattedDetail.getParameterValue());
				logger.log(Level.INFO, "New parameters added into an existing entry.");
				tempParameters.add(newParameter);
				}
			}
		}
	}

	public void deleteFromTempStorage(int i) throws IOException {
		_tempStorage.remove(i);
		setTempStorageToFile(_tempStorage);
		logger.log(Level.INFO, "Delete an entry from temp storage.");

	}

	public void setCompletedInTempStorage(int i) throws IOException {
		Entry entry = _tempStorage.get(i);
		entry.setCompleted(true);
		_tempStorage.remove(i);
		_tempArchive.add(entry);
		logger.log(Level.INFO, "Completed entry removed from storage and placed in archive.");
		setTempStorageToFile(_tempStorage);
		setTempArchiveToFile(_tempArchive);
	}

	public void setTempStorageToFile(ArrayList<Entry> entries) throws IOException {
		_storageHandler.updateTempStorageToFile(entries);
		logger.log(Level.INFO, "Transfer the temp storage into file.");
	}

	public void setTempArchiveToFile(ArrayList<Entry> entries) throws IOException {
		_archiveHandler.updateTempStorageToFile(entries);
		logger.log(Level.INFO, "Transfer the temp storage into archive.");
	}

}
