package com.taskboard.main;

public class Cell {

	// attributes
	
	private String _content;
	
	// constructors
	
	public Cell() {
		_content = new String();
	}
	
	public Cell(String content) {
		_content = new String(content);
	}
	
	// accessors
	
	public String getContent() {
		return _content;
	}
	
	// mutators
	
	public void setContent(String newContent) {
		_content = newContent;
	}
	
	// functionalities
	
	public void printCell() {
		System.out.print(_content + " | ");
	}
	
}
