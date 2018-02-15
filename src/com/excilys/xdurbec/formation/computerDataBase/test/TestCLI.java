package com.excilys.xdurbec.formation.computerDataBase.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import com.excilys.xdurbec.formation.computerDataBase.userInterface.commandeLineInterface.CommandLineInterface;

public class TestCLI {
	public static void main(String arg[]) throws IOException, SQLException  {
		CommandLineInterface.launchCLI();
	}
}
