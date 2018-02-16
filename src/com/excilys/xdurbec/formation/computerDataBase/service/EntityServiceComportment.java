package com.excilys.xdurbec.formation.computerDataBase.service;

import java.sql.SQLException;
import java.util.List;

public interface EntityServiceComportment<Entity> {
	public List<Entity> getAll() throws  ExceptionService;
	
}
