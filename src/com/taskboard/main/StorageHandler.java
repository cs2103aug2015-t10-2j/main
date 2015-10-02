package com.taskboard.main;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

public class StorageHandler {
	
	private static final int PARAM_POSITION_OF_FIRST_DETAIL_IN_TEMP_STORAGE = 0; 
	
	// attributes
	
	private File original;
	private ArrayList<String> details;
	
	// constructor
	
	public StorageHandler() {
		
	}
	
	public boolean isSetUpSuccessful(String fileName) {
		original = new File(fileName);
		
		boolean isFileValid = isFileValid(original);
		
		if (isFileValid) {
			details = new ArrayList<String>();
			copyExistingTextFromFile();
		}
		
		return isFileValid;
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
	
	private void copyExistingTextFromFile() {
		try {
			Scanner scanFileToCopy = new Scanner(original);
			
			while (scanFileToCopy.hasNext()) {
				String line = scanFileToCopy.nextLine();
				details.add(line);
			}
			
			scanFileToCopy.close();
			
		} catch (FileNotFoundException e) {
			return;
		}
	}
	
	public boolean isAddToFileSuccessful(ArrayList<String> formattedDetailsForStorage) {
		try {
			FileWriter fileToAdd = new FileWriter(original, true);
		
			for (int i = 0; i < formattedDetailsForStorage.size(); i++) {
				String detail = formattedDetailsForStorage.get(i);
				details.add(detail);
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
		String detail;
		String entriesList = "";
		
		if (!details.isEmpty()) {
			detail = details.get(PARAM_POSITION_OF_FIRST_DETAIL_IN_TEMP_STORAGE);
			entriesList = detail;
		}
		
		for (int i = 1; i < details.size(); i++) {
			detail = details.get(i);
			entriesList = entriesList.concat("\n").concat(detail);
		}
		
		return entriesList;
	}
}
