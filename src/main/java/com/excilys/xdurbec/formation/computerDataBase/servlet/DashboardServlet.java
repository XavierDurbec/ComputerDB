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

	private  Logger log = LogManager.getLogger(this.getClass());
	

	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = config.getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	    AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();
	    autowireCapableBeanFactory.autowireBean(this);
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		 int nbComputerByPage = 20;
		 int pageNb = 1; 
		 String filter = ""; 
		 ComputerAttributes orderBy =  ComputerAttributes.ID;

		 boolean ascendingOrder = true;
		try {
			String ascendingOrderString = request.getParameter("orderDirection");		
			String nbComputerByPageString = request.getParameter(ServletString.NB_COMPUTER_BY_PAGE);
			if (nbComputerByPageString != null && !nbComputerByPageString.equals("")) {
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
			int nbComputerPage = getNbComputerPage(filter, nbComputerByPage);
			if (pageNb > nbComputerPage) {
				pageNb = nbComputerPage;
			}			
			String orderByString = request.getParameter(ServletString.ORDER_TYPE);
			if (orderByString != null) {
				orderBy = this.orderBySet(orderByString);
			}
			String oldOrderByString = request.getParameter(ServletString.OLD_ORDER_TYPE);
			if (oldOrderByString != null) {
				ascendingOrder = this.orderDirectionSet(orderBy, this.orderBySet(oldOrderByString), ascendingOrderString);
			}
			ComputerPage computerPage = computerService.getComputerPage(pageNb, nbComputerByPage, filter, orderBy, ascendingOrder);

			ascendingOrderString = ascendingOrder ? "ASC" : "DESC";
			request.setAttribute("orderDirection", ascendingOrderString);
			request.setAttribute(ServletString.JSP_ORDER_VALUE, orderBy.sqlName);
			request.setAttribute(ServletString.JSP_COMPUTER_LIST, ComputerMapperDTO
					.toComputerDTOList(computerService.refresh(computerPage).getComputerList()));
			request.setAttribute(ServletString.JSP_SEARCH_VALUE, filter);
			request.setAttribute(ServletString.JSP_COMPUTER_COUNT, computerService.getComputerNumber(filter));
			request.setAttribute(ServletString.JSP_MAX_PAGE, nbComputerPage);
			request.setAttribute(ServletString.JSP_PAGE_NB, pageNb);
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

	private int getNbComputerPage(String filter, int nbComputerByPage) {
		int nbPage;
		try {
			nbPage = computerService.getComputerNumber(filter) / nbComputerByPage;
			if (computerService.getComputerNumber(filter) % nbComputerByPage != 0) {
				nbPage++;
			}
			return nbPage;  
		} catch (ExceptionService e) {
			log.error(e.getMessage());
			return 0;
		}
	}

	private ComputerAttributes orderBySet(String orderByString) {
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
		return orderByTmp;
	}
	
	private boolean orderDirectionSet(ComputerAttributes orderBy, ComputerAttributes oldOrderBy, String ascendingString) {
		boolean orderDirection = ascendingString != null ? ascendingString.equals("DESC") ? false : true : true;
		orderDirection = orderBy.equals(oldOrderBy) ? !orderDirection : orderDirection;
		return orderDirection;
	}

}

