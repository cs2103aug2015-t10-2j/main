package com.taskboard.main;

import java.util.ArrayList;

public class Row {
	
	private static final String LINES = "______________________";

	// attributes
	
	private ArrayList<Cell> _cells;
	
	// constructors
	
	public Row() {
		_cells = new ArrayList<Cell>();
	}
	
	public Row(ArrayList<Cell> cells) {
		_cells = cells;
	}
	
	// accessors
	
	public int getNumCells() {
		return _cells.size();
	}
	
	public ArrayList<Cell> getCells() {
		return _cells;
	}
	
	public Cell getCell(int id) {
		return _cells.get(id);
	}
	
	// mutators
	
	public void addCells(ArrayList<Cell> newCells) {
		_cells.addAll(newCells);
	}
	
	public void addCell(Cell newCell) {
		_cells.add(newCell);
	}
	
	public void setCells(ArrayList<Cell> newCells) {
		_cells = newCells;
	}
	
	public void setCell(int id, Cell newCell) {
		_cells.set(id, newCell);
	}
	
	// functionalities
	
	public void printRow() {
		for (int i = 0; i < _cells.size(); i++) {
			_cells.get(i).printCell();
		}
		System.out.println();
		System.out.println(LINES);
	}
	
}
