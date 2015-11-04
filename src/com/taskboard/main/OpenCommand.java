package com.taskboard.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class OpenCommand extends Command{
	
	private static final String MESSAGE_WELCOME = "Welcome to TASKBOARD!";
	private static final String MESSAGE_ERROR_FOR_LAUNCH_OPEN = "Failed to open file.";
	private static final String TITLE_AFTER_LAUNCH = "TaskBoard: Your Revolutionary Task Manager (%1$s)";
	
	public OpenCommand(ArrayList<Parameter> parameters) {
		assert parameters != null;
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
		
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	public Response executeCommand() {
		assert _parameters.size() > 0;
		_logger.log(Level.INFO, "Commenced execution of OpenCommand");
		
		String fileName = getDetailFromParameter(getNameParameter());
		assert fileName != null;
		_logger.log(Level.INFO, "Successfully retrieved filename: " + fileName);
	
		return getResponseForLaunch(fileName);
	}
	
	private Response getResponseForLaunch(String fileName) {
		Response responseForOpen = new Response();
			
		try {
			_tempStorageManipulator.repopulate(fileName);
			updateUIPreferences();
			updateUITitle(fileName);
			setSuccessResponseForLaunchOpen(responseForOpen);
			_logger.log(Level.INFO, "Generated success response for opening existing file");
		} catch (IllegalArgumentException ex) {
			setFailureResponseForInvalidOpen(responseForOpen, ex);
			_logger.log(Level.INFO, "Generated failure response for opening non-existent file");
		} catch (IOException ex) {
			setFailureResponseForLaunchOpen(responseForOpen);
			_logger.log(Level.INFO, "Generated failure response for opening existing file");
		}
	
		return responseForOpen;
	}
	
	private void setSuccessResponseForLaunchOpen(Response response) {
		response.setIsSuccess(true);
		response.setFeedback(MESSAGE_WELCOME);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private void setFailureResponseForInvalidOpen(Response response, IllegalArgumentException ex) {
		response.setIsSuccess(false);
		response.setFeedback(ex.getMessage());
	}
	
	private void setFailureResponseForLaunchOpen(Response response) {
		response.setIsSuccess(false);
		response.setFeedback(MESSAGE_ERROR_FOR_LAUNCH_OPEN);
	}
	
	private void updateUIPreferences() throws IOException {
		String backgroundPath = _tempStorageManipulator.getBackgroundPath();
		int reminderHour = _tempStorageManipulator.getReminderHour();
		UserInterface.getInstance().setBackgroundPath(backgroundPath);
		UserInterface.getInstance().setReminderHour(reminderHour);
	}

	private void updateUITitle(String title) {
		UserInterface.getInstance().setTitle(String.format(TITLE_AFTER_LAUNCH, title));
	}
	
}
