package com.taskboard.main.command;

import java.util.ArrayList;

import com.taskboard.main.TempStorageManipulator;
import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.Response;

public class HelpCommand extends Command {

	public HelpCommand(ArrayList<Parameter> parameters) {
		_parameters = parameters;
		
		if (getTempStorageManipulator() == null) {
			_tempStorageManipulator = new TempStorageManipulator();
		}
	}
	
	public Response executeCommand() {
		Response responseForHelp = new Response();
		ArrayList<Entry> helpList = new ArrayList<Entry>();
		
		helpList.add(new Entry());
		helpList.get(0).addToParameters(new Parameter(null, "TaskBoard Command List:"));
		helpList.get(0).addToParameters(new Parameter(null, ""));
		helpList.get(0).addToParameters(new Parameter(null, "- add .... : Add a new entry"));
		helpList.get(0).addToParameters(new Parameter(null, "- edit .... : Edit an entry"));
		helpList.get(0).addToParameters(new Parameter(null, "- delete .... : Delete entries"));
		helpList.get(0).addToParameters(new Parameter(null, "- view .... : Selectively view entries"));
		helpList.get(0).addToParameters(new Parameter(null, "- complete .... : Indicate entry as completed"));
		helpList.get(0).addToParameters(new Parameter(null, "- restore .... : Restore entry from archive"));
		helpList.get(0).addToParameters(new Parameter(null, "- archive .... : Display archived entries"));
		helpList.get(0).addToParameters(new Parameter(null, "- command : View command list"));
		helpList.get(0).addToParameters(new Parameter(null, "- exit : Terminates program"));
		
		responseForHelp.setIsSuccess(true);
		responseForHelp.setFeedback("Successfully displayed all entries in command list.");
		responseForHelp.setEntries(helpList);
		
		return responseForHelp;
	}
	
}
