package com.taskboard.main;

import java.util.ArrayList;

import java.io.IOException;

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
