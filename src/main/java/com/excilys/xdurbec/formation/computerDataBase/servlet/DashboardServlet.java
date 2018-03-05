package com.excilys.xdurbec.formation.computerDataBase.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.excilys.xdurbec.formation.computerDataBase.service.ComputerService;
import com.excilys.xdurbec.formation.computerDataBase.service.ExceptionService;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerMapperDTO;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet{
	private static ComputerService computerService = ComputerService.getComputerService();

	private int nbComputerByPage = 20;
	private int pageNb = 1;
	protected Logger log = LogManager.getLogger(this.getClass());

	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		try {
			String pageString = request.getParameter("page");
			if (pageString != null) {
				pageNb = Integer.valueOf(request.getParameter("page"));
			}
			request.setAttribute("computerCount", computerService.getAll().size());
			request.setAttribute("computerList", ComputerMapperDTO
					.toComputerDTOList(computerService.getComputerPage(pageNb, nbComputerByPage).getComputerList()));
			request.setAttribute("maxPage", getNbComputerPage());
			request.setAttribute("pageNb", this.pageNb);
		} catch (ExceptionService e) {
			log.error(e.getMessage());
		}

		this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
	}	

	private int getNbComputerPage() {
		int nbPage;
		try {
			nbPage = computerService.getComputerNumber() / this.nbComputerByPage;

			if (computerService.getComputerNumber() % this.nbComputerByPage != 0) {
				nbPage++;
			}
			return nbPage;  
		} catch (ExceptionService e) {
			log.error(e.getMessage());
			return 0;
		}
	}



}
