package com.excilys.xdurbec.formation.computerDataBase.servlet.dto;

import java.util.ArrayList;
import java.util.List;

import com.excilys.xdurbec.formation.computerDataBase.model.Company;



public class CompanyMapperDTO {
	
	private CompanyMapperDTO() {};
	
	public static Company toCompany(CompanyDTO companyDTO) {
		Company company = new Company(companyDTO.getName());
		company.setId(companyDTO.getId());
		return company;
	}

	public static CompanyDTO toCompanyDTO(Company company) {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setId(company.getId());
		companyDTO.setName(company.getName());
		return companyDTO;
	}

	public static List<Company> toCompanyList(List<CompanyDTO> companyDTOList) {
		List<Company> companyList = new ArrayList<Company>();
		for (CompanyDTO companyDTO:companyDTOList) {
			companyList.add(toCompany(companyDTO));
		}
		return companyList;
	}

	public static List<CompanyDTO> toCompanyDTOList(List<Company> companyList) {
		List<CompanyDTO> companyDTOList = new ArrayList<CompanyDTO>();

		for (Company company : companyList) {
			companyDTOList.add(toCompanyDTO(company));
		}
		return companyDTOList;
	}

}
