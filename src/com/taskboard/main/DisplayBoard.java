package com.taskboard.main;

import java.util.ArrayList;

public class DisplayBoard {
	
	public static final String TASKBOARD_TITLE = 
			" _______  _______  _______  ___   _  _______  _______  _______  ______    ______  " + "\n" +
			"|       ||   _   ||       ||   | | ||  _    ||       ||   _   ||    _ |  |      | " + "\n" +
			"|_     _||  |_|  ||  _____||   |_| || |_|   ||   _   ||  |_|  ||   | ||  |  _    |" + "\n" +
			"  |   |  |       || |_____ |      _||       ||  | |  ||       ||   |_||_ | | |   |" + "\n" +
			"  |   |  |       ||_____  ||     |_ |  _   | |  |_|  ||       ||    __  || |_|   |" + "\n" +
			"  |   |  |   _   | _____| ||    _  || |_|   ||       ||   _   ||   |  | ||       |" + "\n" +
			"  |___|  |__| |__||_______||___| |_||_______||_______||__| |__||___|  |_||______| ";
	
	private static final String LINE = "__________________________________________________________________________________";
	
	private ArrayList<Table> _tables;
	
	public DisplayBoard() {
		_tables = new ArrayList<Table>();
	}
	
	// accessors
	
	public ArrayList<Table> getTables() {
		return _tables;
	}
	
	public Table getTable(int id) {
		return _tables.get(id);
	}
	
	// mutators
	
	public void addTables(ArrayList<Table> newTables) {
		_tables.addAll(newTables);
	}
	
	public void addTable(Table newTable) {
		_tables.add(newTable);
	}
	
	public void setTables(ArrayList<Table> newTables) {
		_tables = newTables;
	}
	
	public void setTable(int id, Table newTable) {
		_tables.set(id, newTable);
	}
	
	public void printDisplayBoard() {
		System.out.println(TASKBOARD_TITLE);
		System.out.println(LINE);
		for (int i = 0; i < _tables.size(); i++) {
			_tables.get(i).printTable();
		}
	}
	
}
