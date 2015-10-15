package com.taskboard.main;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

public class ArchiveHandler {
	
	// attributes
	private File _archive;
	private ArrayList<Entry> _completedEntries;
	
	// constructor
	public ArchiveHandler() {
	}
	
	// accessor
	public File getArchive() {
		return _archive;
	}
	
	// TOOD
}
