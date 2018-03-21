package com.excilys.xdurbec.formation.computerdatabase.dao;

public enum ComputerAttributes {
	NAME("computer.name"), ID("computer.id"), COMPANY_NAME("company.name"), INTRODUCED("computer.introduced"), DISCONTINUED("computer.discontinued");
	
	public final String sqlName;
	
	ComputerAttributes(String sqlName) {
		this.sqlName = sqlName;
	}
	
}
