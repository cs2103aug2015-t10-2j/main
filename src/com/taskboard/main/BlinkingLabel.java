package com.taskboard.main;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.Timer;

public class BlinkingLabel extends JLabel {
	
	private static final long serialVersionUID = 5;
	private static final int DEFAULT_BLINK_RATE = 500;

	private int _blinkRate;
	private boolean _isBlinking;

	public BlinkingLabel() {
		super();
		_blinkRate = DEFAULT_BLINK_RATE;
		_isBlinking = true;
		Timer timer = new Timer(_blinkRate, new TimerListener(this));
		timer.setInitialDelay(0);
		timer.start();
	}

	public void setBlinking(boolean flag) {
		_isBlinking = flag;
	}
	public boolean isBlinking() {
		return _isBlinking;
	}

	private class TimerListener implements ActionListener {
		private BlinkingLabel bl;
		private boolean isVisible = true;

		public TimerListener(BlinkingLabel bl) {
			this.bl = bl;
		}

		public void actionPerformed(ActionEvent e) {
			if (bl.isBlinking()) {
				if (isVisible) {
					bl.setVisible(true);
				}
				else {
					bl.setVisible(false);
				}
				isVisible = !isVisible;
			}
			else {
				// Make sure that the label is visible if the blinking is off.
				if (isVisible) {
					bl.setVisible(true);
					isVisible = false;
				}
			}
		}

	}
	
}