package com.excilys.xdurbec.formation.computerDataBase.dao;

import java.sql.SQLException;
import java.util.List;

public interface EntityDAO<T>  {
	public T get(int id) throws SQLException;
	public List<T> getAll() throws SQLException;
	public void create(T entity)throws SQLException;
	public void set(T entity)throws SQLException;
	public void delete(T entity)throws SQLException;
	
}
