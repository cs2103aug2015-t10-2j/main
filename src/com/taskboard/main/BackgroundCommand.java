package com.taskboard.main;

import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

public class BackgroundCommand extends Command {

	private static final String MESSAGE_SET_BACKGROUND_SUCCESS = "Succesfully changed background image.";
	
	public BackgroundCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		Response responseForBackground = new Response();
		
		assert (_parameters.size() == 1) : "Critical error: Background command parameter not found.";
		String imageFilePathString = _parameters.get(0).getParameterValue();
		File imageFilePath = new File(imageFilePathString);
		if (imageFilePath.exists() && !imageFilePath.isDirectory()) {
			try {
				UserInterface.getInstance().setBackground(imageFilePathString);
				responseForBackground.setIsSuccess(true);
				responseForBackground.setFeedback(MESSAGE_SET_BACKGROUND_SUCCESS);
			} catch (IOException e) {
				responseForBackground.setIsSuccess(false);
				responseForBackground.setException(new IllegalArgumentException("The file format is not supported."));
			}
		} else {
			responseForBackground.setIsSuccess(false);
			responseForBackground.setException(new IllegalArgumentException("The specified image file does not exist."));
		}
		
		return responseForBackground;
	}
	
}
