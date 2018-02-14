package com.excilys.xdurbec.formation.computerDataBase.userInterface.commandeLineInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineInterface {
		public static void launchCLI() throws IOException {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String line = "";
			while(!line.equals("exit")) {
				line = reader.readLine();
				System.out.println(line);
			}
		}
		
		
		
		
}
