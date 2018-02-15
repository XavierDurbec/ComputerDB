package com.excilys.xdurbec.formation.computerDataBase.dao;

import java.sql.SQLException;
import java.util.List;
public interface EntityDAO<T>  {
	public List<T> getAll() throws SQLException;
}
