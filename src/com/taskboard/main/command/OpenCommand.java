//@@author A0123935E
package com.taskboard.main.command;

import java.io.IOException;

import java.util.ArrayList;
import java.util.logging.Level;

import com.taskboard.main.GlobalLogger;
import com.taskboard.main.TempStorageManipulator;
import com.taskboard.main.userinterface.UserInterface;

import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.Response;

/**
 * This class inherits from the Command class and executes the Open command.
 * @author Amarparkash Singh Mavi
 *
 */
public class OpenCommand extends Command{
	
	// These are the feedback messages to be displayed to the user
	private static final String MESSAGE_WELCOME = "Welcome to TASKBOARD!";
	private static final String MESSAGE_FOR_FILENAME = "Scheduler \"%1$s\" is ready for use.";
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
		// _parameters must have the name of the file to be opened for Open command to be valid
		assert _parameters.size() > 0;
		_logger.log(Level.INFO, "Commence execution of OpenCommand");
		
		String fileName = getDetailFromParameter(getNameParameter());
		assert fileName != null;
		_logger.log(Level.INFO, "Successfully retrieved filename: " + fileName);
		Response responseForOpen =  getResponseForLaunch(fileName);
		
		// facilitates the Undo command 
		if (responseForOpen.isSuccess()) {
			_tempStorageManipulator.setLastTempStorage(new ArrayList<Entry>());
			_tempStorageManipulator.setLastTempArchive(new ArrayList<Entry>());
		}
		
		return responseForOpen;
	}
	
	private Response getResponseForLaunch(String fileName) {
		Response responseForOpen = new Response();	
		try {
			_tempStorageManipulator.repopulate(fileName);
			updateUIPreferences();
			updateUITitle(fileName);
			setSuccessResponseForLaunchOpen(responseForOpen, fileName);
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
	
	private void setSuccessResponseForLaunchOpen(Response response, String fileName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForSuccessfulLaunchOpen(fileName);
		response.setFeedback(userFeedback);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private String getFeedbackForSuccessfulLaunchOpen(String fileName) {
		String feedback = MESSAGE_WELCOME.concat("<br>");
		feedback = feedback.concat(String.format(MESSAGE_FOR_FILENAME, fileName));
		
		return feedback;
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
		UserInterface.getInstance().setBackgroundPath(backgroundPath);
		
		int reminderHour = _tempStorageManipulator.getReminderHour();
		UserInterface.getInstance().setReminderHour(reminderHour);
	}

	private void updateUITitle(String title) {
		UserInterface.getInstance().setTitle(String.format(TITLE_AFTER_LAUNCH, title));
	}
}