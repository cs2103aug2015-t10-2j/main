package com.taskboard.main.comparator;

import java.util.Comparator;

import com.taskboard.main.util.Parameter;
//@@author A0129889A 
public class ParameterComparator implements Comparator<Parameter> {
	public int compare(Parameter p1, Parameter p2) {
		return p1.getParameterType().compareTo(p2.getParameterType());
	}
}
