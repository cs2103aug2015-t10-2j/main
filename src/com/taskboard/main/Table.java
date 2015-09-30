package com.taskboard.main;

import java.util.ArrayList;

public class Table {

	// attributes
	
	String _name;
	ArrayList<Row> _rows;
	
	// constructors
	
	public Table() {
		_name = new String();
		_rows = new ArrayList<Row>();
	}
	
	public Table(String name) {
		_name = name;
		_rows = new ArrayList<Row>();
	}
	
	public Table(String name, ArrayList<Row> rows) {
		_name = name;
		_rows = rows;
	}
	
	// accessors
	
	public String getName() {
		return _name;
	}
	
	public ArrayList<Row> getRows() {
		return _rows;
	}
	
	public Row getRow(int id) {
		return _rows.get(id);
	}
	
	// mutators
	
	public void setName(String newName) {
		_name = newName;
	}
	
	public void addRows(ArrayList<Row> newRows) {
		_rows.addAll(newRows);
	}
	
	public void addRow(Row newRow) {
		_rows.add(newRow);
	}
	
	public void setRows(ArrayList<Row> newRows) {
		_rows = newRows;
	}
	
	public void setRow(int id, Row newRow) {
		_rows.set(id, newRow);
	}
	
	public void printTable() {
		for (int i = 0; i < _rows.size(); i++) {
			_rows.get(i).printRow();
		}
		System.out.println();
	}
	
}
