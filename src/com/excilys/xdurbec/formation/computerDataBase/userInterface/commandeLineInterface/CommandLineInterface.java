package com.excilys.xdurbec.formation.computerDataBase.userInterface.commandeLineInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.excilys.xdurbec.formation.computerDataBase.model.Company;
import com.excilys.xdurbec.formation.computerDataBase.model.Computer;
import com.excilys.xdurbec.formation.computerDataBase.service.CompanyService;
import com.excilys.xdurbec.formation.computerDataBase.service.ComputerService;
import com.excilys.xdurbec.formation.computerDataBase.service.ExceptionService;

public class CommandLineInterface {
	private static CommandLineInterface commandLineInterface;

	private Logger log = Logger.getLogger(CompanyService.class);
	private BufferedReader reader; 
	private CompanyService companyService;
	private ComputerService computerService;
	private String line;

	private CommandLineInterface() {
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.companyService = CompanyService.getCompanyService();
		this.computerService = ComputerService.getComputerService();
	}

	public static CommandLineInterface getCommandLineInterface() {
		if( commandLineInterface == null) {
			return new CommandLineInterface();
		}
		else {
			return commandLineInterface;
		}
	}

	private void readLine() {

		try {
			line = reader.readLine();
		} catch (IOException e) {
			log.error("Reader Error:"+e.getMessage());
		}
		if(line.equals("/exit")) {
			this.exit();
		}
	}

	private void exit() {
		System.out.println("Good bye.");
		System.exit(0);
	}

	public void launch() {

		System.out.println("Hello! write 'help' for show all commands.");
		readLine();
		while(true) {
			switch(line) {
			case("get"):
				this.getComputer();
			break;
			case("getAll"):
				this.getAll();
				break;
			case("create"):
				break;
			case("update"):
				break;
			case("delete"):
				break;
			case("help"):
				break;
			default:
				System.out.println("Command not reconize.");
			}
			readLine();
		}

	}


	private void getComputer()  {
		System.out.println("Give the computer id:");
		this.readLine();
		try {
			int id = Integer.parseInt(line);
			System.out.println(computerService.getById(id));
		} catch (ExceptionService e) {
			System.out.println(e.getMessage());
		} catch (NumberFormatException e) {
			log.error(e.getMessage());
			System.out.println("You have to write an number.");
		}
	}


	private void getAll() {
		System.out.println("Do you want a Company or a Computer?");
		this.readLine();
		try {
			switch(line) {
			case("company"):
				for(Company company : companyService.getAll()) {
					System.out.println(company);	
				}
			break;
			case("computer"):
				for(Computer computer : computerService.getAll()) {
					System.out.println(computer);
				}
			break;
			default:
				System.out.println("command not reconize.");
			}
		}catch(ExceptionService e) {
			System.out.println(e.getMessage());
		}
	}
}
