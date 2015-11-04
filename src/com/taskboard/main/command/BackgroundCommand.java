package com.taskboard.main.command;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.awt.Image;

import javax.imageio.ImageIO;

import com.taskboard.main.Parameter;
import com.taskboard.main.Response;
import com.taskboard.main.TempStorageManipulator;
import com.taskboard.main.UserInterface;

public class BackgroundCommand extends Command {

	private static final String MESSAGE_SET_BACKGROUND_SUCCESS = "Succesfully changed background image.";
	private static final String MESSAGE_SET_BACKGROUND_FAILURE = "The image format is not supported.";
	private static final String MESSAGE_FILE_NOT_FOUND = "The specified image does not exist.";
	
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
				UserInterface.getInstance().setBackgroundPath(imageFilePathString);
				_tempStorageManipulator.setBackgroundPath(imageFilePathString);
				responseForBackground.setIsSuccess(true);
				responseForBackground.setFeedback(MESSAGE_SET_BACKGROUND_SUCCESS);
			} catch (IOException e) {
				responseForBackground.setIsSuccess(false);
				responseForBackground.setFeedback(MESSAGE_SET_BACKGROUND_FAILURE);
			}
		} else {
			try {
				URL imageURLPathString = new URL(imageFilePathString);
				final HttpURLConnection connection = (HttpURLConnection) imageURLPathString.openConnection();
				connection.setRequestProperty(
				    "User-Agent",
				    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
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
