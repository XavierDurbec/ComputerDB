package com.excilys.xdurbec.formation.computerdatabase.servlet;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class ComputerValidator {
	
	private ComputerValidator() { }
	
	private static boolean dateLogicValidation(String introduced, String discontinued) {
			return introduced == null  || discontinued == null 
					|| introduced.equals("")  || discontinued.equals("") 
					|| Date.valueOf(discontinued).after(Date.valueOf(introduced));
	}
	
	

	private static boolean validName(String name)  {
		return name.length() < 30;
}
	
	public static Map<String, String> validator(Map<String, String> params) {
		Map<String, String> errors = new HashMap<>();
		
		if (!dateLogicValidation(params.get(ServletString.COMPUTER_INTRODUCED), params.get(ServletString.COMPUTER_DISCONTINUED))) {
			errors.put(ServletString.COMPUTER_INTRODUCED, ServletString.DATE_POSITION_ERROR);
		}
		if (!validName(params.get(ServletString.COMPUTER_NAME))) {
			errors.put(ServletString.COMPUTER_NAME, ServletString.NAME_SIZE_ERROR);
		}
		return errors;
	}
}
