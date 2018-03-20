package com.excilys.xdurbec.formation.computerDataBase.servlet;

import java.util.Map;

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
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.CompanyMapperDTO;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerDTO;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerMapperDTO;

@Controller
public class EditeComputerController {

	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private ComputerService computerService;
	
	@Autowired
	private CompanyService companyService;
	
	
	@GetMapping("editComputer")
	public String getAddComputerPage(ModelMap model, @RequestParam Map<String, String> params) {
		int computerId = Integer.valueOf(params.get("id"));

		try {
			model.addAttribute("computer", ComputerMapperDTO.toComputerDTO(computerService.getById(computerId)));
			model.addAttribute("companyList", CompanyMapperDTO.toCompanyDTOList(companyService.getAll()));
		} catch (ExceptionService e) {
			log.error(e);
		}
		return "editComputer";
	}
	
	@PostMapping("editComputer")
	public String editComputer(ModelMap model, @RequestParam Map<String, String> params) {
		ComputerDTO computerDTO = new ComputerDTO();
		System.out.println("Id: " + params.get("id"));
		computerDTO.setId(Integer.parseInt(params.get("id")));
		computerDTO.setName(params.get("computerName"));
		computerDTO.setIntroduced(params.get("introduced"));
		computerDTO.setDiscontinued(params.get("discontinued"));
		String computerCompanyIdString = params.get("companyId");
		try {
			if (!"0".equals(computerCompanyIdString)) {
				computerDTO.setCompany(CompanyMapperDTO.toCompanyDTO(companyService.getCompanyById(Integer.valueOf(computerCompanyIdString))));
			}
			computerService.update(ComputerMapperDTO.toComputer(computerDTO));
		} catch (NumberFormatException | ExceptionService e) {
			log.error(e);
			return "editComputer";
		}

		return "redirect:dashboard";
	}
}
