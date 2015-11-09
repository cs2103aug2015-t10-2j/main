//@@author A0126536E
package com.taskboard.main.command;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.awt.Image;

import javax.imageio.ImageIO;

import com.taskboard.main.TempStorageManipulator;
import com.taskboard.main.userinterface.UserInterface;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.Response;

/**
 * This class is responsible for executing the change background command. It recognizes 
 * both image file from a specified directory or an image URL from the internet.
 * @author Alvian Prasetya
 */
public class BackgroundCommand extends Command {

	private static final String MESSAGE_SET_BACKGROUND_SUCCESS = "Succesfully changed background image.";
	private static final String MESSAGE_SET_BACKGROUND_FAILURE = "The image format is not supported.";
	private static final String MESSAGE_FILE_NOT_FOUND = "The specified image does not exist.";
	
	private static final String STRING_USER_AGENT_ID = "User-Agent";
	private static final String STRING_USER_AGENT_CONTENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31";
	
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
			// If the image is a file.
			try {
				UserInterface.getInstance().setBackgroundPath(imageFilePathString);
				_tempStorageManipulator.setBackgroundPath(imageFilePathString);
				responseForBackground.setIsSuccess(true);
				responseForBackground.setFeedback(MESSAGE_SET_BACKGROUND_SUCCESS);
			} catch (IOException e) {
				responseForBackground.setIsSuccess(false);
				responseForBackground.setFeedback(MESSAGE_SET_BACKGROUND_FAILURE);
			}
		} else {
			// If the image is not a file.
			try {
				// Checks if image is from a URL.
				URL imageURLPathString = new URL(imageFilePathString);
				final HttpURLConnection connection = (HttpURLConnection) imageURLPathString.openConnection();
				connection.setRequestProperty(
				    STRING_USER_AGENT_ID,
				    STRING_USER_AGENT_CONTENT);
				Image imageFileURL = ImageIO.read(connection.getInputStream());
				if (imageFileURL != null) {
					UserInterface.getInstance().setBackgroundPath(imageFilePathString);
					_tempStorageManipulator.setBackgroundPath(imageFilePathString);
					responseForBackground.setIsSuccess(true);
					responseForBackground.setFeedback(MESSAGE_SET_BACKGROUND_SUCCESS);
				} else {
					responseForBackground.setIsSuccess(false);
					responseForBackground.setFeedback(MESSAGE_FILE_NOT_FOUND);
				}
			} catch (MalformedURLException e) {
				responseForBackground.setIsSuccess(false);
				responseForBackground.setFeedback(MESSAGE_FILE_NOT_FOUND);
			} catch (IOException e) {
					responseForBackground.setIsSuccess(false);
					responseForBackground.setFeedback(MESSAGE_SET_BACKGROUND_FAILURE);
			}
		}
		
		return responseForBackground;
	}
	
}
