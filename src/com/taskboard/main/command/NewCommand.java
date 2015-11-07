//@@author A0123935E
package com.taskboard.main.command;

import java.io.IOException;

import java.util.ArrayList;
import java.util.logging.Level;

import com.taskboard.main.GlobalLogger;
import com.taskboard.main.TempStorageManipulator;
import com.taskboard.main.userinterface.UserInterface;

import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.Response;
import com.taskboard.main.util.Entry;

/**
 * This class inherits from the Command class and executes the New command.
 * @author Amarparkash Singh Mavi
 *
 */
public class NewCommand extends Command {
	
	// These are the feedback messages to be displayed to the user
	private static final String MESSAGE_WELCOME = "Welcome to TASKBOARD!";
	private static final String MESSAGE_FOR_FILENAME = "Scheduler \"%1$s\" is ready for use.";
	private static final String MESSAGE_ERROR_FOR_LAUNCH_NEW = "Failed to create new file.";
	
	private static final String TITLE_AFTER_LAUNCH = "TaskBoard: Your Revolutionary Task Manager (%1$s)";
	
	public NewCommand(ArrayList<Parameter> parameters) {
		assert parameters != null;
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
		
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	public Response executeCommand() {
		// _parameters must have the name of the file to be created for New command to be valid
		assert _parameters.size() > 0;
		_logger.log(Level.INFO, "Commence execution of NewCommand");
		
		String fileName = getDetailFromParameter(getNameParameter());
		assert fileName != null;
		_logger.log(Level.INFO, "Successfully retrieved filename: " + fileName);
		Response responseForNew = getResponseForLaunch(fileName);
		
		// facilitates the Undo command 
		if (responseForNew.isSuccess()) {
			_tempStorageManipulator.setLastTempStorage(new ArrayList<Entry>());
			_tempStorageManipulator.setLastTempArchive(new ArrayList<Entry>());
		}
		
		return responseForNew;
	}
	
	private Response getResponseForLaunch(String fileName) {
		Response responseForNew = new Response();
		try {
			_tempStorageManipulator.initialise(fileName);
			updateUIPreferences();
			updateUITitle(fileName);
			setSuccessResponseForLaunchNew(responseForNew, fileName);
			_logger.log(Level.INFO, "Generated success response for creating new file");
		} catch (IllegalArgumentException ex) {
			setFailureResponseForInvalidNew(responseForNew, ex);
			_logger.log(Level.INFO, "Generated failure response for creating new file with "
					    + "existing filename");
		} catch (IOException ex) {
			setFailureResponseForLaunchNew(responseForNew);
			_logger.log(Level.INFO, "Generated failure response for creating new file");
		}
		
		return responseForNew;
	}
	
	private void setSuccessResponseForLaunchNew(Response response, String fileName) {
		response.setIsSuccess(true);
		String userFeedback = getFeedbackForSuccessfulLaunchNew(fileName);
		response.setFeedback(userFeedback);
		response.setEntries(_tempStorageManipulator.getTempStorage());
	}
	
	private String getFeedbackForSuccessfulLaunchNew(String fileName) {
		String feedback = MESSAGE_WELCOME.concat("<br>");
		feedback = feedback.concat(String.format(MESSAGE_FOR_FILENAME, fileName));
		
		return feedback;
	}
	
	private void setFailureResponseForInvalidNew(Response response, IllegalArgumentException ex) {
		response.setIsSuccess(false);
		response.setFeedback(ex.getMessage());
	}
	
	private void setFailureResponseForLaunchNew(Response response) {
		response.setIsSuccess(false);
		response.setFeedback(MESSAGE_ERROR_FOR_LAUNCH_NEW);
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