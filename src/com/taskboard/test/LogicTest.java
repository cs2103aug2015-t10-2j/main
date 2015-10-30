package com.taskboard.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;

import java.util.ArrayList;

import com.taskboard.main.Logic;
import com.taskboard.main.Command;
import com.taskboard.main.NewCommand;
import com.taskboard.main.OpenCommand;
import com.taskboard.main.AddCommand;
import com.taskboard.main.EditCommand;
import com.taskboard.main.Response;
import com.taskboard.main.Entry;
import com.taskboard.main.Parameter;
import com.taskboard.main.ParameterType;

public class LogicTest {
	
	private static final String MESSAGE_WELCOME = "Welcome to TASKBOARD!";
	private static final String MESSAGE_ERROR_FOR_CREATING_EXISTNG_FILE = "The file already exists.";
	private static final String MESSAGE_ERROR_FOR_OPENING_NON_EXISTING_FILE = "The file does not exists.";
	private static final String MESSAGE_AFTER_ADD = "\"%1$s\" added!";
	
	@Test
	public void testValidNewAndAdd() {
		ArrayList<Parameter> testParameters = new ArrayList<Parameter>();
		testParameters.add(new Parameter(ParameterType.NAME, "myFile"));
		Command command = new NewCommand(testParameters);
		Response actualResponse = command.executeCommand();
		
		ArrayList<Entry> expectedEntries = new ArrayList<Entry>();
		Response expectedSuccessResponse = createSuccessResponse(true, MESSAGE_WELCOME, expectedEntries);
		testResponseEquality("Success response for creating new file", expectedSuccessResponse, actualResponse);
		
		testParameters.clear();
		Parameter nameParameter = new Parameter(ParameterType.NAME, "MA3264 Revision");
		testParameters.add(nameParameter);
		command = new AddCommand(testParameters);
		actualResponse = command.executeCommand();
		
		Entry entry = new Entry();
		Parameter indexParameter = new Parameter(ParameterType.INDEX, "1");
		entry.addToParameters(indexParameter);
		entry.addToParameters(nameParameter);
		expectedEntries.add(entry);
		String completedMessage = String.format(MESSAGE_AFTER_ADD, "MA3264 Revision");
		expectedSuccessResponse = createSuccessResponse(true, completedMessage, expectedEntries);
		testResponseEquality("Success response for adding floating task", expectedSuccessResponse, actualResponse);		
	}
	
	public void 
	
	private Response createSuccessResponse(boolean isSuccess, String feedback, ArrayList<Entry> entries) {
		Response response = new Response();
		response.setIsSuccess(isSuccess);
		response.setFeedback(feedback);
		response.setEntries(entries);
		
		return response;
	}
	
	private Response createFailureResponse(boolean isSuccess, Exception exObj) {
		Response response = new Response();
		response.setIsSuccess(isSuccess);
		response.setException(exObj);
		
		return response;
	}
	
	private void testResponseEquality(String description, Response expectedResponse, Response actualResponse) {
		assertTrue(description, expectedResponse.equals(actualResponse));
	}
}