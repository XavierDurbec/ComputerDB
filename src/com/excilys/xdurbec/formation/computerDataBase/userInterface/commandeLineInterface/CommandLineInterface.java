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
				break;
			case("update"):
				break;
			case("delete"):
				this.deleteComputer();
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

	private void createComputer()  {
		Computer computer = new Computer();

		this.setLocalComputerName(computer);

		this.setLocalComuterIntroducedDate(computer);

		this.setLocalComputerDiscontinuedDate(computer);

		this.setLocalComputerCompany(computer);

		try {
			computerService.create(computer);
		} catch (ExceptionService e) {
			System.out.println(e.getMessage());
			return;
		}


	}

	private void setLocalComputerName(Computer computer) {
		System.out.println("Give a name for your company:");
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
				stayIn = false;
			break;
			default:
				try {
					computer.setDiscontinued(Date.valueOf(line));
				}catch(IllegalArgumentException e){
					System.out.println("Your date is not good. (AAAA-MM-DD) (/cancel for return and /skip for  go to company set)");
				}
				System.out.println("Discontinued Date skiped.");
			}
			try { 
				computer.setIntroduced(Date.valueOf(line));
				break;
			}catch(IllegalArgumentException e) {
				System.out.println("Your date is not good. (AAAA-MM-DD) (/cancel for return and /skip for  go to company set)");
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
				stayIn = false;
			default:
				Company company = new Company();
				try {
					int company_id = Integer.valueOf(line);
					if(companyService.companyExistenceVerification(company_id)) {
						company.setId(company_id);
						stayIn = false;
						computer.setCompany(company);

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


	private void setLocalComputerId(Computer computer) {
		System.out.println("Write the id od computer to update:");
		Boolean stayIn = true;
		while(stayIn) {
			this.readLine();
			if(line.equals("/cancel")) {
				return;
			}
			else {
				try {
					int id = Integer.valueOf(line);
					computer.setId(id);
					stayIn = false;
				}
				catch(IllegalArgumentException e) {
					System.out.println("Id have to be a number. try more or /cancel");
				}

			}
		}
	}
	private void updateComputer() {
		Computer computer= new Computer();

		this.setLocalComputerId(computer);
		this.setLocalComputerName(computer);
		this.setLocalComuterIntroducedDate(computer);
		this.setLocalComputerDiscontinuedDate(computer);
		this.setLocalComputerCompany(computer);
		try {
			computerService.update(computer);
			System.out.println("Computer has been update");
		}catch(ExceptionService e) {
			System.out.println(e.getMessage());
			return;
		}
	}
	private void deleteComputer() {
		System.out.println("Give the computer id:");
		this.readLine();
		try {
			int id = Integer.parseInt(line);
			computerService.deleteById(id);
			System.out.println("Computer "+id+" as been removed.");
		} catch (ExceptionService e) {
			System.out.println(e.getMessage());
		} catch (NumberFormatException e) {
			log.error(e.getMessage());
			System.out.println("You have to write an number.");
		}
	}
}
