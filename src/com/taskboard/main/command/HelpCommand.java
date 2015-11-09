//@@author A0126536E
package com.taskboard.main.command;

import java.util.ArrayList;

import com.taskboard.main.TempStorageManipulator;
import com.taskboard.main.util.Entry;
import com.taskboard.main.util.Parameter;
import com.taskboard.main.util.Response;

/**
 * This class is responsible for forming the help message to be displayed to the user.
 * @author Alvian Prasetya
 */
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
		
		for (int i = 0; i < 46; i++) {
			helpList.add(new Entry());
		}
		helpList.get(0).addToParameters(new Parameter(null, "<center><b>COMMAND</b></center>"));
		helpList.get(1).addToParameters(new Parameter(null, "<center><b>DESCRIPTION</b></center>"));
		
		helpList.get(2).addToParameters(new Parameter(null, "<b>add</b> NAME <b>by</b> DATE TIME"));
		helpList.get(3).addToParameters(new Parameter(null, "Add a deadline task"));
		
		helpList.get(4).addToParameters(new Parameter(null, "<b>add</b> NAME <b>from</b> DATE TIME <b>to</b> DATE TIME"));
		helpList.get(5).addToParameters(new Parameter(null, "Add an event"));
		
		helpList.get(6).addToParameters(new Parameter(null, "<b>add</b> NAME"));
		helpList.get(7).addToParameters(new Parameter(null, "Add a side task"));
		
		helpList.get(8).addToParameters(new Parameter(null, "<b>edit</b> INDEX SPECIFICATIONS <font color='red'>*</font>"));
		helpList.get(9).addToParameters(new Parameter(null, "Edit entry with specified index"));
		
		helpList.get(10).addToParameters(new Parameter(null, "<b>delete</b> INDEX"));
		helpList.get(11).addToParameters(new Parameter(null, "Delete entry with specified index"));
		
		helpList.get(12).addToParameters(new Parameter(null, "<b>delete</b> SPECIFICATIONS <font color='red'>*</font>"));
		helpList.get(13).addToParameters(new Parameter(null, "Delete entries that meet the specifications"));
		
		helpList.get(14).addToParameters(new Parameter(null, "<b>view</b> SPECIFICATIONS <font color='red'>*</font>"));
		helpList.get(15).addToParameters(new Parameter(null, "View entries that meet the specifications"));
		
		helpList.get(16).addToParameters(new Parameter(null, "<b>complete</b> INDEX"));
		helpList.get(17).addToParameters(new Parameter(null, "Mark entry with specified index as completed"));
		
		helpList.get(18).addToParameters(new Parameter(null, "<b>restore</b> INDEX"));
		helpList.get(19).addToParameters(new Parameter(null, "Restore entry with specified index from archive"));
		
		helpList.get(20).addToParameters(new Parameter(null, "<b>archive</b>"));
		helpList.get(21).addToParameters(new Parameter(null, "Display archived entries"));
		
		helpList.get(22).addToParameters(new Parameter(null, "<b>remind</b> NUM_OF_HOUR(S)"));
		helpList.get(23).addToParameters(new Parameter(null, "Set reminder to the specified number of hours"));
		
		helpList.get(24).addToParameters(new Parameter(null, "<b>background</b> IMAGE_FILE_PATH"));
		helpList.get(25).addToParameters(new Parameter(null, "Set the specified image file as background"));
		
		helpList.get(26).addToParameters(new Parameter(null, "<b>background</b> IMAGE_URL"));
		helpList.get(27).addToParameters(new Parameter(null, "Set the specified image URL as background"));
		
		helpList.get(28).addToParameters(new Parameter(null, "<b>command</b>"));
		helpList.get(29).addToParameters(new Parameter(null, "View TaskBoard's command list"));
		
		helpList.get(30).addToParameters(new Parameter(null, "<b>exit</b>"));
		helpList.get(31).addToParameters(new Parameter(null, "Quit the program"));
		
		helpList.get(32).addToParameters(new Parameter(null, ""));
		helpList.get(33).addToParameters(new Parameter(null, ""));
		
		helpList.get(34).addToParameters(new Parameter(null, "<center><b>SPECIFICATIONS</b></center>"));
		helpList.get(35).addToParameters(new Parameter(null, "<center><b>Delete, View, or Edit entries...</b></center>"));
		
		helpList.get(36).addToParameters(new Parameter(null, "<b>name</b>"));
		helpList.get(37).addToParameters(new Parameter(null, "which include the name"));
		
		helpList.get(38).addToParameters(new Parameter(null, "<b>by</b> DATE"));
		helpList.get(39).addToParameters(new Parameter(null, "of the specified date"));
		
		helpList.get(40).addToParameters(new Parameter(null, "<b>from</b> DATE <b>to</b> DATE"));
		helpList.get(41).addToParameters(new Parameter(null, "from and up to a specified date"));
		
		helpList.get(42).addToParameters(new Parameter(null, "<b>pri</b> PRIORITY_LEVEL"));
		helpList.get(43).addToParameters(new Parameter(null, "with the specified priority level"));
		
		helpList.get(44).addToParameters(new Parameter(null, "<b>cat</b> CATEGORY"));
		helpList.get(45).addToParameters(new Parameter(null, "with the specified category name"));
		
		responseForHelp.setIsSuccess(true);
		responseForHelp.setFeedback("Successfully displayed the list of commands.");
		responseForHelp.setEntries(helpList);
		
		return responseForHelp;
	}
	
}
