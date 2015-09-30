
package com.taskboard.main;

import java.io.File;
import java.io.IOException;

public class Storage {
	
	// attribute
	
	private File original;
	
	// constructor
	
	public Storage() {
		
	}
	
	public boolean isSetUpSuccessful(String fileName) {
		original = new File(fileName);
		
		return isFileValid(original);
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
}
