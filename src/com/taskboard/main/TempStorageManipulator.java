	//@@author A0129889A 
package com.taskboard.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.*;

import com.taskboard.main.comparator.DateComparator;
import com.taskboard.main.comparator.ParameterComparator;
import com.taskboard.main.filehandler.ArchiveHandler;
import com.taskboard.main.filehandler.PreferenceHandler;
import com.taskboard.main.filehandler.StorageHandler;
import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.ParameterType;

public class TempStorageManipulator {

	// attributes

	private ArrayList<Entry> _tempStorage;
	private ArrayList<Entry> _tempArchive;
	private ArrayList<Entry> _lastTempStorage;
	private ArrayList<Entry> _lastTempArchive;
	private ArrayList<String> _tempPreference;
	StorageHandler _storageHandler;
	ArchiveHandler _archiveHandler;
	PreferenceHandler _preferenceHandler;

	private static Logger _logger = GlobalLogger.getInstance().getLogger();
	// constructor

	public TempStorageManipulator() {
		_storageHandler = new StorageHandler();
		_archiveHandler = new ArchiveHandler();
		_preferenceHandler = new PreferenceHandler();
	}

	// accessors

	public ArrayList<Entry> getTempStorage() {
		return _tempStorage;
	}

	public ArrayList<Entry> getTempArchive() {
		return _tempArchive;
	}
	
	public ArrayList<Entry> getLastTempStorage() {
		return _lastTempStorage;
	}
	
	public ArrayList<Entry> getLastTempArchive() {
		return _lastTempArchive;
	}
	
	public String getBackgroundPath() {
		return _tempPreference.get(0);
	}
	
	public int getReminderHour() {
		return Integer.valueOf(_tempPreference.get(1));
	}
	// mutators

	public void setTempStorage(ArrayList<Entry> newTempStorage) throws IOException {
		_tempStorage = newTempStorage;
		Collections.sort(_tempStorage, new DateComparator());
		setIndexForAllEntries();
		setTempStorageToFile(_tempStorage);
	}

	public void setTempArchive(ArrayList<Entry> newTempArchive) throws IOException {
		_tempArchive = newTempArchive;
		Collections.sort(_tempArchive, new DateComparator());
		setIndexForAllEntries();
		setTempArchiveToFile(_tempArchive);
	}
	
	public void setLastTempStorage(ArrayList<Entry> newLastTempStorage) {
		_lastTempStorage = newLastTempStorage;
	}
	
	public void setLastTempArchive(ArrayList<Entry> newLastTempArchive) {
		_lastTempArchive = newLastTempArchive;
	}
	
	public void setBackgroundPath(String bgPath) throws IOException {
		_tempPreference.set(0, bgPath);
		setTempPreferenceToFile(_tempPreference);
	}
	
	public void setReminderHour(int numOfHours) throws IOException {
		_tempPreference.set(1, String.valueOf(numOfHours));
		setTempPreferenceToFile(_tempPreference);
	}

	public void initialise(String fileName) throws IllegalArgumentException, IOException {
		_tempStorage = _storageHandler.createNewFile(fileName);
		_tempArchive = _archiveHandler.createNewFile(fileName);
		_tempPreference = _preferenceHandler.createNewFile(fileName);
		_logger.log(Level.INFO, "Initialise a new temporary storage.");
	}

	public void repopulate(String fileName) throws IllegalArgumentException, FileNotFoundException {
		_tempStorage = _storageHandler.openExistingFile(fileName);
		_tempArchive = _archiveHandler.openExistingFile(fileName);
		_tempPreference = _preferenceHandler.openExistingFile(fileName);
		_logger.log(Level.INFO, "Repopulate a temporary storage.");
	}

	public void addToTempStorage(Entry entry) throws IOException {
		_tempStorage.add(entry);
		Collections.sort(_tempStorage, new DateComparator());
		setIndexForAllEntries();
		setTempStorageToFile(_tempStorage);
		_logger.log(Level.INFO, "Add entry into temp storage.");
	}

	public Entry editTempStorage(int i, ArrayList<Parameter> newContent, boolean isEntryTypeChanged) throws IOException {
		Entry editedEntry = _tempStorage.get(i);
		ArrayList<Parameter> entryDetails = editedEntry.getParameters();
		replaceWithNewContent(entryDetails, newContent, isEntryTypeChanged);
		_logger.log(Level.INFO, "Replace old entries with new ones.");
		editedEntry.setParameters(entryDetails);
		_tempStorage.set(i, editedEntry);
		Collections.sort(_tempStorage, new DateComparator());
		setIndexForAllEntries();
		setTempStorageToFile(_tempStorage);
		
		for (Entry currentEntry: _tempStorage) {
			if (currentEntry.equals(editedEntry)) {
				return currentEntry;
			}
		}
		assert false: "Critical error: Edited entry not found.";
		return null;
	}

	private void replaceParameters(ArrayList<Parameter> oldParameters, ArrayList<Parameter> newParameters) {
		for (int i = 0; i < oldParameters.size(); i++) {
			for (int j = 0; j < newParameters.size(); j++) {
				if (oldParameters.get(i).getParameterType() == newParameters.get(j).getParameterType()) {
					oldParameters.get(i).setParameterValue(newParameters.get(j).getParameterValue());
					_logger.log(Level.INFO, "Parameter to be edited found and replaced.");
					newParameters.remove(j);
					_logger.log(Level.INFO, "Parameter removed from newParameters.");
					break;
				}
			}
		}
	}

	private void addParameters(ArrayList<Parameter> oldParameters, ArrayList<Parameter> newParameters) {
		if (!newParameters.isEmpty()) {
			for (int i = 0; i < newParameters.size(); i++) {
				oldParameters.add(newParameters.get(i));
				_logger.log(Level.INFO, "New parameter added to the oldParameters.");
			}
		}
	}

	private void replaceWithNewContent(ArrayList<Parameter> entryDetails, ArrayList<Parameter> newContent,
			boolean isEntryTypeChanged) {
		if (!isEntryTypeChanged) {
			replaceParameters(entryDetails, newContent);
			addParameters(entryDetails, newContent);
			Collections.sort(entryDetails, new ParameterComparator());
		} else {
			replaceParameters(entryDetails, newContent);
			while (!checkParameter(entryDetails)) {
				removingSomeContent(entryDetails);
			}
			addParameters(entryDetails, newContent);
			Collections.sort(entryDetails, new ParameterComparator());
		}
	}

	// Removing non Name/Index/Category/Priority
	private void removingSomeContent(ArrayList<Parameter> entryDetails) {
		for (int i = 0; i < entryDetails.size(); i++) {
			if ((entryDetails.get(i).getParameterType() != ParameterType.NAME)
					&& (entryDetails.get(i).getParameterType() != ParameterType.INDEX)
					&& (entryDetails.get(i).getParameterType() != ParameterType.CATEGORY)
					&& (entryDetails.get(i).getParameterType() != ParameterType.PRIORITY)) {
				entryDetails.remove(i);
				_logger.log(Level.INFO, "Removing non Name/Index/Category/Priorty.");
				break;
			}
		}
	}

	// Check if there is any parameter which is not NAME, INDEX, CAT or PRIORITY
	private boolean checkParameter(ArrayList<Parameter> entryDetails) {
		boolean result = true;
		for (int i = 0; i < entryDetails.size(); i++) {
			if ((entryDetails.get(i).getParameterType() != ParameterType.NAME)
					&& (entryDetails.get(i).getParameterType() != ParameterType.INDEX)
					&& (entryDetails.get(i).getParameterType() != ParameterType.CATEGORY)
					&& (entryDetails.get(i).getParameterType() != ParameterType.PRIORITY)) {
				result = false;
				break;
			}
		}
		return result;
	}

	public void deleteFromTempStorage(int i) throws IOException {
		_tempStorage.remove(i);
		setIndexForAllEntries();
		setTempStorageToFile(_tempStorage);
		_logger.log(Level.INFO, "Deleted an entry from temp storage.");
	}

	public void deleteFromTempStorage(ArrayList<Entry> deletedEntries) throws IOException {
		ArrayList<Entry> tempEntries = new ArrayList<Entry>();

		for (int i = 0; i < _tempStorage.size(); i++) {
			Entry currentEntry = _tempStorage.get(i);
			boolean isDeleted = false;
			for (int j = 0; j < deletedEntries.size(); j++) {
				if (currentEntry.toString().equals(deletedEntries.get(j).toString())) {
					isDeleted = true;
					break;
				}
			}
			if (!isDeleted) {
				tempEntries.add(currentEntry);
			}
		}
		_tempStorage = tempEntries;
		Collections.sort(_tempStorage, new DateComparator());
		setIndexForAllEntries();
		setTempStorageToFile(_tempStorage);
		_logger.log(Level.INFO, "Deleted entries from temp storage.");
	}

	public void setCompletedInTempStorage(int i) throws IOException {
		Entry entry = _tempStorage.get(i);
		_tempStorage.remove(i);
		_tempArchive.add(entry);
		_logger.log(Level.INFO, "Completed entry removed from storage and placed in archive.");
		Collections.sort(_tempStorage, new DateComparator());
		setIndexForAllEntries();
		setTempStorageToFile(_tempStorage);
		setTempArchiveToFile(_tempArchive);
	}

	public void restoreToTempStorage(int i) throws IOException {
		Entry entry = _tempArchive.get(i);
		_tempArchive.remove(i);
		_tempStorage.add(entry);
		_logger.log(Level.INFO, "Restored entry to storage.");
		Collections.sort(_tempStorage, new DateComparator());
		setIndexForAllEntries();
		setTempStorageToFile(_tempStorage);
		setTempArchiveToFile(_tempArchive);
	}

	public void setTempStorageToFile(ArrayList<Entry> entries) throws IOException {
		_storageHandler.updateTempStorageToFile(entries);
		_logger.log(Level.INFO, "Transfer the temp storage into file.");
	}

	public void setTempArchiveToFile(ArrayList<Entry> entries) throws IOException {
		_archiveHandler.updateTempStorageToFile(entries);
		_logger.log(Level.INFO, "Transfer the temp storage into archive.");
	}

	public void setTempPreferenceToFile(ArrayList<String> contents) throws IOException {
		_preferenceHandler.updateTempStorageToFile(contents);
		_logger.log(Level.INFO, "Transfer the temp storage into preference.");
	}

	private void setIndexForAllEntries() {
		for (int i = 0; i < _tempStorage.size(); i++) {
			Parameter indexParameter = new Parameter(ParameterType.INDEX, String.valueOf(i + 1));
			_tempStorage.get(i).setIndexParameter(indexParameter);
		}

		for (int i = 0; i < _tempArchive.size(); i++) {
			Parameter indexParameter = new Parameter(ParameterType.INDEX, String.valueOf(i + 1));
			_tempArchive.get(i).setIndexParameter(indexParameter);
		}
	}
}
