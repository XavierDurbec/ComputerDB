package com.excilys.xdurbec.formation.computerDataBase.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/addcomputer")
public class ComputerAddServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(request, response);
	}	
}
