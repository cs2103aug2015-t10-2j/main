package com.taskboard.main;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ShortcutListener implements KeyListener {
	private static final String EXIT_COMMAND_STRING = "exit";
	private static final String HELP_COMMAND_STRING = "help";
	private static final String UNDO_COMMAND_STRING = "undo";
	
	final ArrayList<Integer> pressed = new ArrayList<Integer>();
	
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub  
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_CONTROL) {
			pressed.remove(new Integer(KeyEvent.VK_CONTROL));
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!UserInterface.getInstance().getCommandField().getText().isEmpty()) {
				UserInterface.getInstance().executeInputCommand();
			}
		} else if (arg0.getKeyCode() == KeyEvent.VK_CONTROL) {
			pressed.add(new Integer(KeyEvent.VK_CONTROL));
		} else if (pressed.contains(new Integer(KeyEvent.VK_CONTROL))) {
			if (arg0.getKeyCode() == KeyEvent.VK_Z) {
				UserInterface.getInstance().getCommandField().setText(UNDO_COMMAND_STRING);
				UserInterface.getInstance().executeInputCommand();
			} else if (arg0.getKeyCode() == KeyEvent.VK_Q) {
				UserInterface.getInstance().getCommandField().setText(EXIT_COMMAND_STRING);
				UserInterface.getInstance().executeInputCommand();
			} else if (arg0.getKeyCode() == KeyEvent.VK_H) {
				UserInterface.getInstance().getCommandField().setText(HELP_COMMAND_STRING);
				UserInterface.getInstance().executeInputCommand();
			}
		}
	}
}
