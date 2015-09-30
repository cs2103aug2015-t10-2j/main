package com.taskboard.main;

import java.io.File;
import java.io.IOException;

public class Storage {
	
	private File original;
	
	public Storage(String fileName) {
		setUpStorage(fileName);
	}
	
	private void setUpStorage(String fileName) {
		original = new File(fileName);
		testFileValidity(original);
	}
	
	private void testFileValidity(File fileToCheck) {
		if (!fileToCheck.exists()) {
			try {
				fileToCheck.createNewFile();
			} catch (IOException e) {
				System.exit(0);
			}
		}
	}
}
