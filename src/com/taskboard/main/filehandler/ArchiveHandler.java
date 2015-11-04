package com.taskboard.main.filehandler;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.taskboard.main.GlobalLogger;
import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.ParameterType;

public class ArchiveHandler {
	
	private static final String MARKER_FOR_NEXT_ENTRY_IN_FILE = "INDEX:";
	private static final int INDEX_OF_EMPTY_ENTRY = 0;
	private static final String MESSAGE_ERROR_FOR_CREATING_EXISTNG_FILE = "The file already exists.";
	private static final String MESSAGE_ERROR_FOR_OPENING_NON_EXISTING_FILE = "The file does not exists.";
	private static final int INDEX_OF_DETAIL_TYPE = 0;
	private static final int INDEX_OF_DETAIL = 1;
	
	// attributes
	private File _archive;
//	private ArrayList<Entry> _completedEntries;
	private static Logger _logger = GlobalLogger.getInstance().getLogger();
	
	// constructor
	public ArchiveHandler() {
	}
	
	// accessor
	public File getArchive() {
		return _archive;
	}
	
	public ArrayList<Entry> createNewFile(String fileName) throws IllegalArgumentException,IOException {
		String archiveFileName = fileName + ".arc";
		_archive = new File(archiveFileName);
		
		boolean doesFileExist = doesFileExist(_archive);
		ArrayList<Entry> completedEntries;
//		assert doesFileExist: false;
		if (!doesFileExist) {
			_archive.createNewFile();
			completedEntries = new ArrayList<Entry>();
		} else {
			throw new IllegalArgumentException(MESSAGE_ERROR_FOR_CREATING_EXISTNG_FILE);
		}
		_logger.log(Level.INFO, "Create a new archive.");
		return completedEntries;
	}
	
	public ArrayList<Entry> openExistingFile(String fileName) throws IllegalArgumentException, FileNotFoundException {
		String archiveFileName = fileName + ".arc";
		_archive = new File(archiveFileName);
		boolean doesFileExist = doesFileExist(_archive);
		
		ArrayList<Entry> completedEntries;
		
		if (doesFileExist) {
			Scanner scanFileToCopy = new Scanner(_archive);
			completedEntries = copyExistingEntriesFromFile(scanFileToCopy);
			scanFileToCopy.close();	
		} else {
			throw new IllegalArgumentException(MESSAGE_ERROR_FOR_OPENING_NON_EXISTING_FILE);
		}
		_logger.log(Level.INFO, "Open an existing archive.");
		return completedEntries;
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
				String[] splitFormattedDetail = formattedDetail.split(":");
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
	
	private boolean doesFileExist(File fileToCheck) {
		if (!fileToCheck.exists()) {
			return false;
		}		
		_logger.log(Level.INFO, "Check whether a file already exists.");
		return true;
	}
	
	public void updateTempStorageToFile(ArrayList<Entry> entries) throws IOException {
		FileWriter fileToAdd = new FileWriter(_archive);
		copyAllEntriesToFile(fileToAdd, entries);
		fileToAdd.close();
		_logger.log(Level.INFO, "Copy temp storage to archive.");
	}
	
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
}
