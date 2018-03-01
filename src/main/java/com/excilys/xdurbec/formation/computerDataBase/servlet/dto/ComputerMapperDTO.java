package com.excilys.xdurbec.formation.computerDataBase.servlet.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.excilys.xdurbec.formation.computerDataBase.model.Computer;

public class ComputerMapperDTO {
	public static Computer toComputer(ComputerDTO computerDTO) {
		try {
		Computer computer = new Computer(computerDTO.getName(), 
				Date.valueOf(computerDTO.getIntroduced()), 
				Date.valueOf(computerDTO.getDiscontinued()), 
				CompanyMapperDTO.toCompany((computerDTO.getCompany())));
		computer.setId(computerDTO.getId());
		return computer;
		} catch () {
			
		}
	}

	public static ComputerDTO toComputerDTO(Computer computer) {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setId(computer.getId());
		computerDTO.setName(computer.getName());

		if (computer.getIntroduced() != null) {
			computerDTO.setIntroduced(computer.getIntroduced().toString());
		}
		if (computer.getDiscontinued() != null) {
			computerDTO.setDiscontinued(computer.getDiscontinued().toString());
		}
		computerDTO.setCompany(CompanyMapperDTO.toCompanyDTO(computer.getCompany()));
		return computerDTO;
	}

	public static List<Computer> toComputerList(List<ComputerDTO> computerDTOList) {
		List<Computer> computerList = new ArrayList<Computer>();
		for (ComputerDTO computerDTO:computerDTOList) {
			computerList.add(toComputer(computerDTO));
		}
		return computerList;
	}

	public static List<ComputerDTO> toComputerDTOList(List<Computer> computerList) {
		List<ComputerDTO> computerDTOList = new ArrayList<ComputerDTO>();

		for (Computer computer : computerList) {
			computerDTOList.add(toComputerDTO(computer));
		}
		return computerDTOList;
	}
	
	private static boolean computerParseValidator(ComputerDTO computerDTO) {
		return false;
	}
}
