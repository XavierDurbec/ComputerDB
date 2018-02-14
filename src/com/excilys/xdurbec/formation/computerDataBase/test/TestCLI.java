package com.excilys.xdurbec.formation.computerDataBase.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestCLI {
	public static void main(String arg[]) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		while(!line.equals("exit")) {
			line = reader.readLine();
			System.out.println(line);
		}
	}
}
