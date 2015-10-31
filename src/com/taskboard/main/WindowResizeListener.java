package com.taskboard.main;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

import javax.swing.JTextArea;

public class WindowResizeListener extends ComponentAdapter {
	// whenever the window is resized
	@Override
	public void componentResized(ComponentEvent frameResized) {
		try {
			UserInterface.getInstance().setBackground(UserInterface.getInstance().getBackgroundFilePath());
		} catch (IOException e) {
			JTextArea feedbackArea = UserInterface.getInstance().getFeedbackArea();
			if (feedbackArea == null) {
				UserInterface.getInstance().setFeedbackArea(new JTextArea());
			}
			feedbackArea.setText("Unexpected error during background resize.");
		}
	}
}
