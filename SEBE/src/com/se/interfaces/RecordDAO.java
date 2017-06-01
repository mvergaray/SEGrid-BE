package com.se.interfaces;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.se.errorhandling.AppException;
import com.se.responses.RecordsResponse;
import com.se.structures.Record;

@XmlRootElement
public interface RecordDAO {
	public int saveRecords(List<Record> records) throws AppException;
	
	public RecordsResponse listRecords(int pageStart, int pageCount, String orderBy,
			long startDate, long endDate, Record filter) throws AppException;
	
	public RecordsResponse updateRecord(Record record) throws AppException;
	
	public List<String> getCreationCodeList() throws AppException;
	
	public int deleteRecordsByCreationCode(String creationCode) throws AppException;
}
