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
	
	@Test
	public void testLogicComponent() {
		ArrayList<Parameter> expectedParameters = new ArrayList<Parameter>();
		expectedParameters.add(new Parameter(ParameterType.NAME, "myFile"));
		Command command = new NewCommand(expectedParameters);
		Response actualResponse = command.executeCommand();
		
		ArrayList<Entry> expectedEntries = new ArrayList<Entry>();
		Response expectedResponse = createSuccessResponse(true, MESSAGE_WELCOME, expectedEntries);
		testResponseEquality("Success response for creating new file", expectedResponse, actualResponse);
	}
	
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