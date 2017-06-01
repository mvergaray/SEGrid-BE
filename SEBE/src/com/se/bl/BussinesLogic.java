package com.se.bl;

import java.util.List;

import com.se.dao.DAOFactory;
import com.se.errorhandling.AppException;
import com.se.interfaces.RecordDAO;
import com.se.responses.RecordsResponse;
import com.se.structures.Record;

public class BussinesLogic {
	static DAOFactory factory = DAOFactory.getDAOFactory(1);
	
	static RecordDAO objRecordDAO = factory.getRecordDAO();
	
	public static int saveRecords(List<Record> registros) throws AppException {
		return objRecordDAO.saveRecords(registros);
	}
	
	public static RecordsResponse listRecords(int pageStart, int pageCount, String orderBy,
			long startDate, long endDate, Record filter) throws AppException {
		
		return objRecordDAO.listRecords(pageStart, pageCount, orderBy, startDate, endDate, filter);
	}
	
	public static RecordsResponse updateRecord(Record record) throws AppException {
		return objRecordDAO.updateRecord(record);
	}
	
	public static List<String> getCreationCodeList() throws AppException {
		return objRecordDAO.getCreationCodeList();
	}
	
	public static int deleteRecordsByCreationCode(String creationCode) throws AppException {
		return objRecordDAO.deleteRecordsByCreationCode(creationCode);
	}
}
