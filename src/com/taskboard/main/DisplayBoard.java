package com.taskboard.main;

import java.util.ArrayList;

public class DisplayBoard {

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
	
}
