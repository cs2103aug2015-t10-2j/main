package com.taskboard.main;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class StorageHandler {

	private static final String MESSAGE_ERROR_FOR_CREATING_EXISTNG_FILE = "The file already exists.";
	private static final String MESSAGE_ERROR_FOR_OPENING_NON_EXISTING_FILE = "The file does not exists.";
	private static final String MARKER_FOR_NEXT_ENTRY_IN_FILE = "INDEX:";

	private static final int INDEX_OF_EMPTY_ENTRY = 0;
	private static final int INDEX_OF_DETAIL_TYPE = 0;
	private static final int INDEX_OF_DETAIL = 1;
	// private static final int INDEX_OF_FORMATTED_ENTRY = 0;
	// private static final int INDEX_OF_ENTRY_INDEX = 0;
	// private static final int INDEX_OF_DETAIL_TYPE = 0;
	// private static final int INDEX_OF_DETAIL = 1;

	// attributes

	private File _original;

	private static Logger _logger = GlobalLogger.getInstance().getLogger();

	// constructor

	public StorageHandler() {

	}

	// accessor

	// public ArrayList<Entry> getEntryListFromStorage() {
	// return _entries;
	// }
	//

	// public static StorageHandler getInstance() {
	// if (instance == null) {
	// instance = new StorageHandler();
	// }
	// return instance;
	// }

	public ArrayList<Entry> createNewFile(String fileName) throws IllegalArgumentException, IOException {
		String newFileName = fileName + ".str";
		_original = new File(newFileName);
		boolean doesFileExist = doesFileExist(_original);

		ArrayList<Entry> entries;

		// assert doesFileExist: false;
		if (!doesFileExist) {
			_original.createNewFile();
			entries = new ArrayList<Entry>();
		} else {
			throw new IllegalArgumentException(MESSAGE_ERROR_FOR_CREATING_EXISTNG_FILE);
		}
		_logger.log(Level.INFO, "Create a new file.");
		return entries;
	}

	public ArrayList<Entry> openExistingFile(String fileName) throws IllegalArgumentException, FileNotFoundException {
		String newFileName = fileName + ".str";
		_original = new File(newFileName);
		boolean doesFileExist = doesFileExist(_original);

		ArrayList<Entry> entries;

		if (doesFileExist) {
			Scanner scanFileToCopy = new Scanner(_original);
			entries = copyExistingEntriesFromFile(scanFileToCopy);
			scanFileToCopy.close();
		} else {
			throw new IllegalArgumentException(MESSAGE_ERROR_FOR_OPENING_NON_EXISTING_FILE);
		}
		_logger.log(Level.INFO, "Open an existing file.");
		return entries;
	}

	private boolean doesFileExist(File fileToCheck) {
		if (fileToCheck.exists()) {
			return true;
		}
		_logger.log(Level.INFO, "Check whether if a file already exists.");
		return false;
	}

	private ArrayList<Entry> copyExistingEntriesFromFile(Scanner scanFileToCopy) {
		ArrayList<Entry> entries = new ArrayList<Entry>();

		Entry entry = new Entry();

		while (scanFileToCopy.hasNext()) {
			String formattedDetail = scanFileToCopy.nextLine();

			if (formattedDetail.contains(MARKER_FOR_NEXT_ENTRY_IN_FILE)) {
				entries.add(entry);
				entry = new Entry();
			}

			if (!formattedDetail.isEmpty()) {
				String[] splitFormattedDetail = formattedDetail.split(": ");
				String detailType = splitFormattedDetail[INDEX_OF_DETAIL_TYPE].trim();
				String detail = splitFormattedDetail[INDEX_OF_DETAIL].trim();

				Parameter parameter = new Parameter();
				parameter.setParameterType(ParameterType.valueOf(detailType));
				parameter.setParameterValue(detail);
				entry.addToParameters(parameter);
			}
		}

		if (!entries.isEmpty()) {
			// to add the final entry once end of file is reached
			entries.add(entry);
			entries.remove(INDEX_OF_EMPTY_ENTRY);
		}
		_logger.log(Level.INFO, "Copy data from file to temp storage.");
		return entries;
	}

	public void updateTempStorageToFile(ArrayList<Entry> entries) throws IOException {
		FileWriter fileToAdd = new FileWriter(_original);
		copyAllEntriesToFile(fileToAdd, entries);
		fileToAdd.close();
		_logger.log(Level.INFO, "Copy data from temp storage to file.");
	}
	//
	// public boolean isAddToFileSuccessful(Entry entry) {
	// try {
	// FileWriter fileToAdd = new FileWriter(_original, true);
	// _entries.add(entry);
	//
	// addSingleEntryToFile(fileToAdd, entry);
	// fileToAdd.close();
	//
	// return true;
	// } catch (IOException e) {
	// return false;
	// }
	// }

	// public String retrieveEntriesInFile() {
	// String entriesList = "";
	//
	// for (int i = 0; i < _entries.size(); i++) {
	// Entry entry = _entries.get(i);
	// String entryDetails = entry.toString();
	//
	// if (i == _entries.size() - 1) {
	// entriesList = entriesList.concat(entryDetails);
	// } else {
	// assert i < _entries.size() -1;
	// entriesList = entriesList.concat(entryDetails).concat("\n");
	// }
	// }
	//
	// return entriesList;
	// }

	// public boolean isEditInFileSuccessful(ArrayList<String> newContent) {
	// try {
	// FileWriter fileToEdit = new FileWriter(_original);
	//
	// Integer indexOfEditedEntry =
	// Integer.valueOf(newContent.get(INDEX_OF_ENTRY_INDEX));
	//
	//// for (int i = 0; i < _entries.size(); i++) {
	//// Entry entry = _entries.get(i);
	//// ArrayList<String> entryDetails = entry.getDetails();
	//// Integer formattedTaskIndex =
	// Integer.valueOf(entryDetails.get(INDEX_OF_FORMATTED_ENTRY));
	////
	//// if (formattedTaskIndex == indexOfEditedTask) {
	//// replaceWithNewContent(entryDetails, newContent);
	//// entry.setDetails(entryDetails);
	//// _entries.set(i, entry);
	//// }
	//// }
	//
	// Entry editedEntry = _entries.get(indexOfEditedEntry);
	// ArrayList<Parameter> entryDetails = editedEntry.getDetails();
	// replaceWithNewContent(entryDetails, newContent);
	// editedEntry.setDetails(entryDetails);
	// _entries.set(indexOfEditedEntry, editedEntry);
	//
	// copyAllEntriesToFile(fileToEdit, _entries);
	// fileToEdit.close();
	//
	// return true;
	// } catch (IOException e) {
	// return false;
	// }
	// }

	// private void replaceWithNewContent(ArrayList<Parameter> entryDetails,
	// ArrayList<String> newContent) {
	// boolean isDetailPresent = false;
	//
	// for (int i = 1; i < newContent.size(); i++) {
	// String newFormattedDetail = newContent.get(i);
	// String[] newFormattedDetailSegments = newFormattedDetail.split(":");
	//
	// for (int j = 0; j < entryDetails.size(); j++) {
	// String existingFormattedDetail = entryDetails.get(j).getParameterValue();
	//
	// if
	// (existingFormattedDetail.contains(newFormattedDetailSegments[INDEX_OF_DETAIL_TYPE]))
	// {
	// entryDetails.set(j, newFormattedDetail);
	// isDetailPresent = true;
	// }
	// }
	//
	// if (!isDetailPresent) {
	// entryDetails.add(newFormattedDetail);
	// }
	//
	// isDetailPresent = false;
	// }
	// }
	//
	// public String retrieveDetail(String searchTaskName, String detailType) {
	// String detail = "";
	//
	// for (int i = 0; i < _entries.size(); i++) {
	// Entry entry = _entries.get(i);
	// ArrayList<String> entryDetails = entry.getDetails();
	// String formattedTaskName = entryDetails.get(INDEX_OF_FORMATTED_ENTRY);
	// String[] formattedTaskNameSegments = formattedTaskName.split(": ");
	// String taskName = formattedTaskNameSegments[INDEX_OF_DETAIL];
	//
	// if (taskName.equals(searchTaskName)) {
	// detail = getDetailFromEntry(entryDetails, detailType);
	// }
	// }
	// return detail;
	// }
	//
	// private String getDetailFromEntry(ArrayList<String> entryDetails, String
	// detailType) {
	// String detail = "";
	//
	// for (int i = 0; i < entryDetails.size(); i++) {
	// String formattedDetail = entryDetails.get(i);
	//
	// if (formattedDetail.contains(detailType)) {
	// String[] formattedDetailSegments = formattedDetail.split(": ");
	// detail = formattedDetailSegments[INDEX_OF_DETAIL];
	// }
	// }
	//
	// return detail;
	// }

	private void copyAllEntriesToFile(FileWriter fileToAdd, ArrayList<Entry> entries) throws IOException {
		for (int i = 0; i < entries.size(); i++) {
			addSingleEntryToFile(fileToAdd, entries.get(i));
		}
	}

	private void addSingleEntryToFile(FileWriter fileToAdd, Entry entry) throws IOException {
		String entrydetails = entry.toString();
		fileToAdd.write(entrydetails);
		fileToAdd.write(System.lineSeparator());
		fileToAdd.flush();
	}

	// public boolean isDeleteFromFileSuccessful(Integer i) {
	// try {
	// FileWriter fileToAdd = new FileWriter(_original);
	// _entries.remove(i);
	// copyAllEntriesToFile(fileToAdd, _entries);
	// fileToAdd.close();
	//
	// return true;
	// } catch (IOException e) {
	// return false;
	// }
	// }

	// public boolean isEntryCompletedSuccessful(Integer i) throws IOException {
	// if (_entries.get(i).getCompletionStatus()) {
	// return true;
	// } else {
	// assert _entries.get(i).getCompletionStatus() : false;
	// return false;
	// }
	// }
}
