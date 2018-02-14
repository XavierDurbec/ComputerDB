package com.excilys.xdurbec.formation.computerDataBase.userInterface.commandeLineInterface;

import java.sql.SQLException;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import com.excilys.xdurbec.formation.computerDataBase.dao.CompanyDAO;
import com.excilys.xdurbec.formation.computerDataBase.dao.ComputerDAO;
import com.sun.tracing.dtrace.ArgsAttributes;

public class CommandLineInterfaceControler {

	private static CommandLineInterfaceControler cliControler;
	
	private CompanyDAO companyDAO;
	private ComputerDAO computerDAO;
	
	private CommandLineInterfaceControler() {	
		this.companyDAO = CompanyDAO.getCompanyDAO();
		this.computerDAO = ComputerDAO.getComputerDAO();
	}
	
	public static CommandLineInterfaceControler getCommandLineInterfaceControler() {
		if(cliControler == null) {
			cliControler = new CommandLineInterfaceControler();
		}
			return cliControler;

	}
	
	
	public String pars(String command) {
		String msg = "";
		if(command == null) {
			return msg; 
		}
		else {
			String[] commandSplit = command.split(" ");
			int nbArguments = commandSplit.length - 2 ;
			switch (commandSplit[0]){
				case "get":
					msg = getEntity(commandSplit, nbArguments);
					break;
				case "getAll":
					break;
				case "create":
					break;
				case "update":
					break;
				case "delete":
					break;
				default :
					return "commande non reconue";
			}
		
		return msg;
		}
	}
		
		private String getEntity (String[] args, int nbArguments ) throws SQLException {
			if(nbArguments < 1) {
				return "too few argument";
			}
			try {
				int id = Integer.parseInt(args[2]);
	
			switch(args[1]) {
				case "computer":
					return computerDAO.getById(id).toString();

				case "company":
					return companyDAO.getById(id).toString();

				default:
					return "Entity not know";
			}
			
			}
			catch(NumberFormatException e){
				return "Arg not valide. Need an integer.";
			}
			
		}
	
		
		
		
		private String getAllEntity(String[] command) throws SQLException {
			switch(command[1]) {
			case "company":
				return companyDAO.getAll().toString();
			case "computer":
				return computerDAO.getAll().toString();
			default:
				return "Entity inconnue.";
			}
		}
		private String createComputer (String[] args) {
			int nbVariable = args.length - 2 ;
			if(nbVariable < 2) {
				return "ERROR";
			}
			else if(nbVariable == 2) {
				
			}
			else if (nbVariable == 3) {
				
			}
			else if (nbVariable >= 4) {
				
			}
			
			return "";
		}
	
}
 