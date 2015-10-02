package com.taskboard.main;

public class TestDriver {
	public static void main(String[] args) {
		UserInterface _userInterface = new UserInterface();
		Executor _executor = new Executor();
		
		String currentCommand = _userInterface.getCommandReader().getNextCommand();
		while (!currentCommand.toLowerCase().equals("exit")) {
			Response currentResponse = _executor.processCommand(currentCommand);
			if (currentResponse.getIsSuccess()) {
				System.out.println(currentResponse.getFeedback());
			} else {
				System.out.println(currentResponse.getException().getMessage());
			}
			currentCommand = _userInterface.getCommandReader().getNextCommand();
		}
	}
}
