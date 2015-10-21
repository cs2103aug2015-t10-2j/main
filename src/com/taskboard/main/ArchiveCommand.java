package com.taskboard.main;

import java.util.ArrayList;

public class ArchiveCommand extends Command {

	public ArchiveCommand(ArrayList<Parameter> parameters) {
			_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		Response responseForArchive = new Response();
		
		responseForArchive.setIsSuccess(true);
		responseForArchive.setFeedback("Successfully retrieved all archived entries.");
		responseForArchive.setEntries(_tempStorageManipulator.getTempArchive());
		
		return responseForArchive;
	}
	
}
