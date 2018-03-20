package com.excilys.xdurbec.formation.computerDataBase.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.xdurbec.formation.computerDataBase.service.CompanyService;
import com.excilys.xdurbec.formation.computerDataBase.service.ComputerService;
import com.excilys.xdurbec.formation.computerDataBase.service.ExceptionService;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.CompanyDTO;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.CompanyMapperDTO;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerDTO;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerMapperDTO;

@Controller
public class ComputerAddController {
	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private ComputerService computerService;
	@Autowired
	private CompanyService companyService;

	@GetMapping("addcomputer")
	public String getAddComputerPage(ModelMap model, @RequestParam Map<String, String> params) {

		try {
			model.addAttribute(ServletString.COMPANY_LIST, CompanyMapperDTO.toCompanyDTOList(companyService.getAll()));
		} catch (ExceptionService e) {
			log.error(e);
		}

		return "addComputer";
	}

	@PostMapping("addcomputer")
	public String addComputer(ModelMap model, @RequestParam Map<String, String> params) {
		try {
			Map<String, String> errors = ComputerValidator.validator(params);
			if (errors.isEmpty()) {
				ComputerDTO computerDTO = new ComputerDTO();
				computerDTO.setName(params.get(ServletString.COMPUTER_NAME));
				computerDTO.setIntroduced(params.get(ServletString.COMPUTER_INTRODUCED));
				computerDTO.setDiscontinued(params.get(ServletString.COMPUTER_DISCONTINUED));
				computerDTO.setCompany(getCompanyById(Integer.valueOf(params.getOrDefault(ServletString.COMPUTER_COMPANY_ID, "0"))));
				computerService.create(ComputerMapperDTO.toComputer(computerDTO));
				return "redirect:dashboard";

			} else {
				model.addAttribute(ServletString.ERRORS, errors);
				return "addComputer";
			}
		} catch (ExceptionService e) {
			log.error(e.getMessage());
			return "addComputer";
		}
	}

	private CompanyDTO getCompanyById(int id) {
		if (id != 0) {
			try {
				return CompanyMapperDTO.toCompanyDTO(companyService.getCompanyById(id));
			} catch (ExceptionService e) {
				log.error(e.getMessage());
				return null;
			}
		} else {
			return null;
		}
	}
}
