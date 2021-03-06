//@@author A0126536E
package com.taskboard.main.command;

import java.util.ArrayList;
import java.io.IOException;

import com.taskboard.main.logger.GlobalLogger;
import com.taskboard.main.tempstoragemanipulator.TempStorageManipulator;
import com.taskboard.main.userinterface.UserInterface;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.Response;

/**
 * This class changes the value of the reminder hour that affects how many hours 
 * before UI displays the "SOON" label on an entry.
 * @author Alvian Prasetya
 */
public class ReminderCommand extends Command {
	
	private static final String MESSAGE_SET_REMINDER_SUCCESS = "Reminder succesfully set to %1$s hour(s).";
	private static final String MESSAGE_SET_REMINDER_FAILURE = "Reminder could not be set to %1$s hour(s).";
	
	public ReminderCommand(ArrayList<Parameter> parameters) {
		assert parameters != null;
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
		
		_logger = GlobalLogger.getInstance().getLogger();
	}
	
	public Response executeCommand() {
		Response responseForReminder = new Response();
		
		assert (_parameters.size() == 1) : "Critical error: Reminder command parameter not found.";
		String reminderHourString = _parameters.get(0).getParameterValue();
		int reminderHour = Integer.parseInt(reminderHourString);
		
		if (reminderHour < 0) {
			responseForReminder.setIsSuccess(false);
			responseForReminder.setFeedback(String.format(MESSAGE_SET_REMINDER_FAILURE, reminderHourString));
		} else {
			UserInterface.getInstance().setReminderHour(reminderHour);
			try {
				_tempStorageManipulator.setReminderHour(reminderHour);
				responseForReminder.setIsSuccess(true);
				responseForReminder.setEntries(_tempStorageManipulator.getTempStorage());
				responseForReminder.setFeedback(String.format(MESSAGE_SET_REMINDER_SUCCESS, reminderHourString));
			} catch (IOException e) {
				responseForReminder.setIsSuccess(false);
				responseForReminder.setFeedback(String.format(MESSAGE_SET_REMINDER_FAILURE, reminderHourString));
			}
		}
		
		return responseForReminder;
	}
	
}
