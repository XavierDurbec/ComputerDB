package com.excilys.xdurbec.formation.computerDataBase.userInterface.commandeLineInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
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

	private void exit () {
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
				this.createComputer();
			break;
			case("update"):
				this.updateComputer();
			break;
			case("delete"):
				this.deleteComputer();
			break;
			case("help"):
				this.help();
				break;
			case("getAllPage"):
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
				System.out.println("command not recognized.");
			}
		}catch(ExceptionService e) {
			System.out.println(e.getMessage());
		}
	}

	private void createComputer()  {
		Computer computer = new Computer();

		this.setLocalComputerName(computer);
		if(this.line.equals("/cancel")) {return;}

		this.setLocalComuterIntroducedDate(computer);
		if(this.line.equals("/cancel")) {return;}

		this.setLocalComputerDiscontinuedDate(computer);
		if(this.line.equals("/cancel")) {return;}

		this.setLocalComputerCompany(computer);
		if(this.line.equals("/cancel")) {return;}

		try {
			computerService.create(computer);
			System.out.println("Computer "+computer.getName()+" has been created");
		} catch (ExceptionService e) {
			System.out.println(e.getMessage());
			return;
		}



	}

	private void setLocalComputerName(Computer computer) {
		System.out.println("Give a name for your computer:");
		this.readLine();
		computer.setName(line);
	}

	private void setLocalComuterIntroducedDate(Computer computer) {
		System.out.println("Give a date of introduction (AAAA-MM-DD) (/cancel for return):");
		Boolean stayIn = true;
		while(stayIn) {
			this.readLine();
			if(line.equals("/cancel")) {
				return;
			}
			try { 
				computer.setIntroduced(Date.valueOf(line));
				break;
			}catch(IllegalArgumentException e) {
				System.out.println("Your date is not good. (AAAA-MM-DD) (/cancel for return)");
			}
		}
	}

	private void setLocalComputerDiscontinuedDate(Computer computer) {
		System.out.println("Give a date for discontinued (AAAA-MM-DD) (/cancel for return and /skip for go to company set):");
		Boolean stayIn = true;
		while(stayIn) {
			this.readLine();
			switch(line) {
			case("/cancel"):
				return;
			case("/skip"):
				System.out.println("Discontinued date skiped");
			return;
			default:
				try {
					computer.setDiscontinued(Date.valueOf(line));
					return;
				}catch(IllegalArgumentException e){
					System.out.println("Your date is not good. (AAAA-MM-DD) (/cancel for return and /skip for  go to company set)");
				}
			}
		}

	}

	private void setLocalComputerCompany(Computer computer) {
		System.out.println("Give an id for set a company to your computer (/cancel for return and /skip for  go to company set):");
		Boolean stayIn = true;
		while(stayIn) {
			this.readLine();
			switch(line) {
			case("/cancel"):
				return;
			case("/skip"):
				System.out.println("Company skiped");
			return;
			default:
				Company company = new Company();
				try {
					int company_id = Integer.valueOf(line);
					if(companyService.companyExistenceVerification(company_id)) {
						company.setId(company_id);
						computer.setCompany(company);
						stayIn = false;
					}
					else{
						System.out.println("This company doesn't existe, give a valide id or write /skip");
					}
				}catch(IllegalArgumentException e) {
					System.out.println("id have to be a number.");
				}
			}
		}

	}


	private void updateComputer() {
		int id = verifyId();
		if(this.line.equals("/cancel")) {return;}
		Computer computer;

		try {
			computer = computerService.getById(id);
		} catch (ExceptionService e) {
			System.out.println("Update cancel because: " + e.getMessage());
			return;
		}

		this.updateLocalComputerName(computer);
		if(this.line.equals("/cancel")) {return;}

		this.updateLocalComuterIntroducedDate(computer);
		if(this.line.equals("/cancel")) {return;}

		this.setLocalComputerDiscontinuedDate(computer);
		if(this.line.equals("/cancel")) {return;}

		this.setLocalComputerCompany(computer);
		if(this.line.equals("/cancel")) {return;}

		try {
			computerService.update(computer);
			System.out.println("Computer has been update");
		}catch(ExceptionService e) {
			System.out.println(e.getMessage());
			return;
		}
	}
	private void updateLocalComputerName(Computer computer) {
		System.out.println("Give a name for your computer (/skip for ignore this):");
		this.readLine();
		switch(line){
		case ("/cancel"):
			return;
		case ("/skip"):
			System.out.println("Name skiped.");
			return;
		default:
			System.out.println("line: " +line);
			computer.setName(line);
		}
	}

	private void updateLocalComuterIntroducedDate(Computer computer) {
		System.out.println("Give a date of introduction (AAAA-MM-DD) (/skip for ignore this):");
		while(true) {
			this.readLine();
			switch(line) {
			case("/cancel"):
				return;
			case("/skip"):
				System.out.println("Introduced date skiped.");
				return;
			default:
				try { 
					computer.setIntroduced(Date.valueOf(line));
					return;
				}catch(IllegalArgumentException e) {
					System.out.println("Your date is not good. (AAAA-MM-DD) (/cancel for return)");
				}
			}
			
		}
	}

	private int verifyId() {
		while(true) {				
			System.out.println("Enter id of computer to update.");
			this.readLine();
			try {
				if(this.line.equals("/cancel")) {
					return 0;
				}
				else {
					return Integer.valueOf(line);
				}
			}catch(IllegalArgumentException e){
				System.out.println("id have to be a number. You write : "+line);
			}
		}
	}

	private void deleteComputer() {
		this.readLine();
		try {
			int id = Integer.parseInt(line);
			computerService.deleteById(id);
			if(this.line.equals("/cancel")) {return;}

			System.out.println("Computer "+id+" as been removed.");
		} catch (ExceptionService e) {
			System.out.println(e.getMessage());
		} catch (NumberFormatException e) {
			log.error(e.getMessage());
			System.out.println("You have to write an number.");
		}
	}
	

	private void help() {
		System.out.println("All command: \n   -create\n   -update\n   -get\n   -getAll\n   -getAllPage\n\nAnywhere you can quit with /exit or cancel a command with /cancel");
	}
	
}
