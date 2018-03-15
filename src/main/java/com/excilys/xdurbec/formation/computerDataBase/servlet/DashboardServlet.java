package com.excilys.xdurbec.formation.computerDataBase.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.excilys.xdurbec.formation.computerDataBase.dao.ComputerAttributes;
import com.excilys.xdurbec.formation.computerDataBase.model.ComputerPage;
import com.excilys.xdurbec.formation.computerDataBase.service.ComputerService;
import com.excilys.xdurbec.formation.computerDataBase.service.ExceptionService;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerMapperDTO;
import com.excilys.xdurbec.formation.computerDataBase.springConfig.ApplicationConfig;

@WebServlet("/dashboard")
@Scope("session")
public class DashboardServlet extends HttpServlet {

	@Autowired
	private ComputerService computerService;

	private int nbComputerByPage = 20;
	private int pageNb = 1; 
	private Logger log = LogManager.getLogger(this.getClass());
	private String filter = ""; 
	private ComputerAttributes orderBy =  ComputerAttributes.ID;
	private Boolean ascendingOrder = true;

	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = config.getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	    AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();
	    autowireCapableBeanFactory.autowireBean(this);
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		try {
			String nbComputerByPageString = request.getParameter(ServletString.NB_COMPUTER_BY_PAGE);
			if (nbComputerByPageString != null) {
				nbComputerByPage = Integer.valueOf(nbComputerByPageString);
			}
			String pageString = request.getParameter(ServletString.PAGE);
			if (pageString != null) {
				pageNb = Integer.valueOf(pageString);
			}
			String filterTmp = request.getParameter(ServletString.NAME_SEARCHED);
			if (filterTmp != null) {
				filter = filterTmp;
			}
			int nbComputerPage = getNbComputerPage(filter);
			if (pageNb > nbComputerPage) {
				pageNb = nbComputerPage;
			}			
			String orderByString = request.getParameter(ServletString.ORDER_TYPE);
			if (orderByString != null) {
				this.orderBySet(orderByString);
			}
			ComputerPage computerPage = computerService.getComputerPage(pageNb, nbComputerByPage, filter, orderBy, ascendingOrder);
			
			request.setAttribute(ServletString.JSP_COMPUTER_LIST, ComputerMapperDTO
					.toComputerDTOList(computerService.refresh(computerPage).getComputerList()));
			request.setAttribute(ServletString.JSP_SEARCH_VALUE, filter);
			request.setAttribute(ServletString.JSP_COMPUTER_COUNT, computerService.getComputerNumber(filter));
			request.setAttribute(ServletString.JSP_MAX_PAGE, nbComputerPage);
			request.setAttribute(ServletString.JSP_PAGE_NB, this.pageNb);
		} catch (ExceptionService e) {
			log.error(e.getMessage());
		}
		this.getServletContext().getRequestDispatcher(ServletString.CONTEXT_DASHBOARD).forward(request, response);
		
	}	


	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String computerListToDelete = request.getParameter(ServletString.COMPUTER_SELECTED);
		if (!computerListToDelete.equals(ServletString.VOID_STRING)) {
			for (String computerIdString : computerListToDelete.split(",")) {
				try {
					computerService.deleteById(Integer.valueOf(computerIdString));
				} catch (NumberFormatException | ExceptionService e) {
					e.printStackTrace();
				}
			}
		}
		try {
			response.sendRedirect(ServletString.DASHBOARD);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private int getNbComputerPage(String filter) {
		int nbPage;
		try {
			nbPage = computerService.getComputerNumber(filter) / this.nbComputerByPage;
			if (computerService.getComputerNumber(filter) % this.nbComputerByPage != 0) {
				nbPage++;
			}
			return nbPage;  
		} catch (ExceptionService e) {
			log.error(e.getMessage());
			return 0;
		}
	}

	private void orderBySet(String orderByString) {
		ComputerAttributes orderByTmp;
		switch (orderByString) {
		case "name":
			orderByTmp = ComputerAttributes.NAME;
			break;
		case "introduced":
			orderByTmp = ComputerAttributes.INTRODUCED;
			break;
		case "discontinued":
			orderByTmp = ComputerAttributes.DISCONTINUED;
			break;
		case "company":
			orderByTmp = ComputerAttributes.COMPANY_NAME;
			break;
		default :
			orderByTmp = ComputerAttributes.ID;
		}

		if (orderByTmp == this.orderBy) {
			this.ascendingOrder = !this.ascendingOrder;
		} else {
			this.orderBy = orderByTmp;
			this.ascendingOrder = true;
		}
	}

}

