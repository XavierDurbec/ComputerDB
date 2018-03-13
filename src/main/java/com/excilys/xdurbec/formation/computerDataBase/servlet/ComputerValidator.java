package com.excilys.xdurbec.formation.computerDataBase.servlet;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class ComputerValidator {
	
	private static boolean dateLogicValidation(String introduced, String discontinued) {
		System.out.println("intro : " + introduced);
		System.out.println("dis : " + discontinued);
			return introduced == null  || discontinued == null 
					|| introduced.equals("")  || discontinued.equals("") 
					|| Date.valueOf(discontinued).after(Date.valueOf(introduced));
	}
	
	

	private static boolean validName(String name)  {
		return name.length() < 30;
}
	
	public static Map<String, String> validator(HttpServletRequest request) {
		Map<String, String> errors = new HashMap<String, String>();
		
		if (!dateLogicValidation(request.getParameter(ServletString.COMPUTER_INTRODUCED), request.getParameter(ServletString.COMPUTER_DISCONTINUED))) {
			System.out.println("Bien dans l'erreur");
			errors.put(ServletString.COMPUTER_INTRODUCED, ServletString.DATE_POSITION_ERROR);
		}
		if (!validName(request.getParameter(ServletString.COMPUTER_NAME))) {
			errors.put(ServletString.COMPUTER_NAME, ServletString.NAME_SIZE_ERROR);
		}
		return errors;
	}
}
