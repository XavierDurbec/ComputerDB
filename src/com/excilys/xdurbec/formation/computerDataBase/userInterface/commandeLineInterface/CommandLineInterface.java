package com.excilys.xdurbec.formation.computerDataBase.userInterface.commandeLineInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class CommandLineInterface {
		public static void launchCLI() throws IOException, SQLException {
			
			CommandLineInterfaceControler cliControler = CommandLineInterfaceControler.getCommandLineInterfaceControler();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String line = "";
			while(!line.equals("exit")) {
				line = reader.readLine();
				System.out.println(cliControler.pars(line));
			}
		}
		
		
		
		
}
