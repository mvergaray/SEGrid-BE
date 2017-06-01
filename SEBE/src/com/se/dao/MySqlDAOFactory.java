package com.se.dao;

import com.se.interfaces.RecordDAO;

public class MySqlDAOFactory extends DAOFactory {

	@Override
	public RecordDAO getRecordDAO() {
		// TODO Auto-generated method stub
		return new MySqlRegistroDAO();
	}

}
