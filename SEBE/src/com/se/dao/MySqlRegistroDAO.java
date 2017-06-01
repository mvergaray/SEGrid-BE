package com.se.dao;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.se.errorhandling.AppException;
import com.se.interfaces.RecordDAO;
import com.se.responses.RecordsResponse;
import com.se.structures.Record;
import com.se.util.MySqlConexion;

public class MySqlRegistroDAO implements RecordDAO {

	@Override
	public int saveRecords(List<Record> records) throws AppException {
		int rows = 0, result ;
		Connection con = MySqlConexion.getConnection();
		
		try{
			con.setAutoCommit(false);
			System.out.println("vao a guardar");
			PreparedStatement ps = con.prepareStatement("INSERT INTO RECORDS(" +
				"CODE, DOCUMENT, DATE, ADDRESS, DISTRICT, PROVINCE," +
				"SENDER, DESTINATION, REFERENCE, CREATIONCODE" +
				") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

			try{
    			for(Record reg : records){
    				rows++;
    				ps.setString(1, reg.getCode());
                    ps.setString(2, reg.getDocument());
                    if (reg.getTime() != 0L) {
                    	ps.setDate(3, new Date(reg.getTime()));
                    } else {
                    	ps.setDate(3, null);
                    }
                    ps.setString(4, reg.getAddress());
                    ps.setString(5, reg.getDistrict());
                    ps.setString(6, reg.getProvince());
                    ps.setString(7, reg.getSender());
                    ps.setString(8, reg.getDestination());
                    ps.setString(9, reg.getReference());
                    ps.setString(10, reg.getCreationCode());
                    ps.addBatch();
    			}

    			result = ps.executeBatch().length;
    			con.commit();
    		} catch (BatchUpdateException e) {
    			e.printStackTrace();
    			throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
    		} catch(NullPointerException e) {
    			e.printStackTrace();
    			throw new AppException(500, 500, "Internal Server Error", "NullPointerException on row: " + rows, "");
    		}catch (Exception e) {
    			throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			} finally {
    			ps.close();
        	}
    	} catch (SQLException e) {
    		throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
    	} catch (AppException e) {
    		throw new AppException(500, 500, "Internal Server Error", e.getDeveloperMessage(), "");
    	} catch (Exception e) {
    		throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
    	} finally {
    		try {
				con.close();
			} catch (SQLException e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			} catch (Exception e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			}
    	}
		
		return result;
	}

	@Override
	public RecordsResponse listRecords(int pageStart, int pageCount, String orderBy, 
			long startDate, long endDate, Record filter) throws AppException {
		Connection con = MySqlConexion.getConnection();
		RecordsResponse result = new RecordsResponse();
		List<Record> list = new ArrayList<Record>();
		int parameters = 0;
		
		// Armar sentencia
		String dataQuery = "SELECT * FROM RECORDS WHERE 1 ";
		String countQuery = "SELECT COUNT(IDRECORD) FROM RECORDS WHERE 1 ";
		
		// Filter only available records (status = 1)
		dataQuery += " AND STATUS = 1 ";
		countQuery += " AND STATUS = 1 ";
		
		if (filter.getIdRecord() != 0) {
			dataQuery += " AND IDRECORD = ? ";
			countQuery += " AND IDRECORD = ? ";
		}
		
		if (filter.getCode() != null) {
			dataQuery += " AND CODE LIKE ? ";
			countQuery += " AND CODE LIKE ? ";
		}
		
		if (filter.getDocument() != null) {
			dataQuery += " AND DOCUMENT LIKE ? ";
			countQuery += " AND DOCUMENT LIKE ? ";
		}
		
		if (filter.getDestination() != null) {
			dataQuery += " AND DESTINATION LIKE ? ";
			countQuery += " AND DESTINATION LIKE ? ";
		}
		
		if (filter.getAddress() != null) {
			dataQuery += " AND ADDRESS LIKE ? ";
			countQuery += " AND ADDRESS LIKE ? ";
		}
		
		if (filter.getDistrict() != null) {
			dataQuery += " AND DISTRICT LIKE ? ";
			countQuery += " AND DISTRICT LIKE ? ";
		}
		if (filter.getSender() != null) {
			dataQuery += " AND SENDER LIKE ? ";
			countQuery += " AND SENDER LIKE ? ";
		}
		
		if (startDate != 0L) {
			dataQuery += " AND DATE >= ? ";
			countQuery += " AND DATE >= ? ";
		}
		
		if (endDate != 0L) {
			dataQuery += " AND DATE <= ? ";
			countQuery += " AND DATE <= ? ";
		}
		
		// Add an ORDER BY sentence
		dataQuery += " ORDER BY ";
		if (orderBy != null && !orderBy.isEmpty()) {
			dataQuery += orderBy;
		} else {
			dataQuery += "DATE DESC";
		}
		
	
		// Agregar siempre el inicio del listado solo para los datos
		dataQuery += " LIMIT ?";
		
		if (pageCount != 0) {
			dataQuery += ", ?";
		} else {
			// Request 500 records at most if limit is not specified
			dataQuery += ", 500";
		}
		
		dataQuery += ";";
		countQuery += ";";
		
		try {
			// Execute query for data
			PreparedStatement ps = con.prepareStatement(dataQuery);
			
			if (filter.getIdRecord() != 0) {
				ps.setInt(++parameters, filter.getIdRecord());
			}
			
			if (filter.getCode() != null) {
				ps.setString(++parameters, "%" + filter.getCode() + "%");
			}
			
			if (filter.getDocument() != null) {
				ps.setString(++parameters, "%" + filter.getDocument() + "%");
			}
			
			if (filter.getDestination() != null) {
				ps.setString(++parameters, "%" + filter.getDestination() + "%");
			}
			
			if (filter.getAddress() != null) {
				ps.setString(++parameters, "%" + filter.getAddress() + "%");
			}
			
			if (filter.getDistrict() != null) {
				ps.setString(++parameters, "%" + filter.getDistrict() + "%");
			}
			
			if (filter.getSender() != null) {
				ps.setString(++parameters, "%" + filter.getSender() + "%");
			}
			
			if (startDate != 0L) {
				ps.setDate(++parameters, new Date(startDate));
			}
			
			if (endDate != 0L) {
				ps.setDate(++parameters, new Date(endDate));
			}
			
			ps.setInt(++parameters, pageStart);
			
			if (pageCount != 0) {	
				ps.setInt(++parameters, pageCount);
			}
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Record objRegistro = new Record();
				objRegistro.setIdRecord(rs.getInt("IDRECORD"));
				objRegistro.setCode(rs.getString("CODE"));
                objRegistro.setDate(rs.getDate("DATE"));
                if (objRegistro.getDate() != null && objRegistro.getDate().getTime() != 0L) {
                	objRegistro.setTime(objRegistro.getDate().getTime());
                }
				objRegistro.setDocument(rs.getString("DOCUMENT"));
				objRegistro.setAddress(rs.getString("ADDRESS"));
				objRegistro.setDistrict(rs.getString("DISTRICT"));
				objRegistro.setProvince(rs.getString("PROVINCE"));
				objRegistro.setSender(rs.getString("SENDER"));
				objRegistro.setDestination(rs.getString("DESTINATION"));
				objRegistro.setReference(rs.getString("REFERENCE"));
				objRegistro.setCreationCode(rs.getString("CREATIONCODE"));
				
				// Get creation date time
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
				objRegistro.setCreationDate(formatter.parse(objRegistro.getCreationCode()).getTime());
				
				list.add(objRegistro);
			}
			
			result.setData(list);
			
			// Execute query for counter
			ps = con.prepareStatement(countQuery);
			parameters = 0;
			
			if (filter.getIdRecord() != 0) {
				ps.setInt(++parameters, filter.getIdRecord());
			}
			
			if (filter.getCode() != null) {
				ps.setString(++parameters, "%" + filter.getCode() + "%");
			}
			
			if (filter.getDocument() != null) {
				ps.setString(++parameters, "%" + filter.getDocument() + "%");
			}
			
			if (filter.getDestination() != null) {
				ps.setString(++parameters, "%" + filter.getDestination() + "%");
			}
			
			if (filter.getAddress() != null) {
				ps.setString(++parameters, "%" + filter.getAddress() + "%");
			}
			
			if (filter.getDistrict() != null) {
				ps.setString(++parameters, "%" + filter.getDistrict() + "%");
			}
			
			if (filter.getSender() != null) {
				ps.setString(++parameters, "%" + filter.getSender() + "%");
			}
			
			if (startDate != 0L) {
				ps.setDate(++parameters, new Date(startDate));
			}
			
			if (endDate != 0L) {
				ps.setDate(++parameters, new Date(endDate));
			}
			
			rs = ps.executeQuery();
			
			if (rs.next()) {
				result.setTotalCount(rs.getInt(1));
			}
			
		} catch (SQLException e) {
			throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
		} catch (Exception e) {
    		throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
    	} finally {
    		try {
				con.close();
			} catch (SQLException e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			} catch (Exception e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			}
    	}
		return result;
	}

	public RecordsResponse updateRecord(Record record) throws AppException {
		RecordsResponse result = new RecordsResponse();
		Connection con = MySqlConexion.getConnection();
		
		try{
			con.setAutoCommit(false);
			
			PreparedStatement ps = con.prepareStatement("UPDATE RECORDS SET " +
				" DOCUMENT = ?, ADDRESS = ?, " +
				" DISTRICT = ?, PROVINCE = ?, SENDER = ?, DESTINATION = ?, " +
				" REFERENCE = ? WHERE IDRECORD = ?; ");

			try{
				ps.setString(1, record.getDocument());
				ps.setString(2, record.getAddress());
				ps.setString(3, record.getDistrict());
				ps.setString(4, record.getProvince());
				ps.setString(5, record.getSender());
				ps.setString(6, record.getDestination());
				ps.setString(7, record.getReference());
				ps.setInt(8, record.getIdRecord());
				
				ps.executeUpdate();
    			con.commit();
    			
    			result = listRecords(0, 1, null, 0, 0, record);
    		} catch (BatchUpdateException e) {
    			e.printStackTrace();
    			throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
    		} catch(NullPointerException e) {
    			e.printStackTrace();
    			throw new AppException(500, 500, "Internal Server Error", "NullPointerException on update.", "");
    		} catch (Exception e) {
    			throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			} finally {
    			ps.close();
        	}
    	} catch (SQLException e) {
    		throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
    	} catch (AppException e) {
    		throw new AppException(500, 500, "Internal Server Error", e.getDeveloperMessage(), "");
    	} catch (Exception e) {
    		throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
    	} finally {
    		try {
				con.close();
			} catch (SQLException e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			} catch (Exception e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			}
    	}
		
		return result;
	}

	@Override
	public List<String> getCreationCodeList() throws AppException {
		Connection con = MySqlConexion.getConnection();
		List<String> result = new ArrayList<String>();
		String query = "SELECT DISTINCT CREATIONCODE FROM RECORDS WHERE STATUS = 1;";
		
		try {
			// Execute query for data
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			try {
				while (rs.next()) {
					result.add(rs.getString("CREATIONCODE"));
				}
			} catch (Exception e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			} finally {
				ps.close();
			}
		} catch (SQLException e) {
			throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
		} catch (Exception e) {
    		throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
    	} finally {
    		try {
				con.close();
			} catch (SQLException e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			} catch (Exception e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			}
    	}
		return result;
	}

	@Override
	public int deleteRecordsByCreationCode(String creationCode) throws AppException {
		RecordsResponse result = new RecordsResponse();
		Connection con = MySqlConexion.getConnection();
		
		try{
			con.setAutoCommit(false);
			
			PreparedStatement ps = con.prepareStatement("UPDATE RECORDS SET " +
				" STATUS = 2 WHERE CREATIONCODE = ?; ");
			try{
				ps.setString(1, creationCode);
				
				// Get the number of rows updated
				result.setTotalCount(ps.executeUpdate());
				con.commit();
			} catch (Exception e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			} finally {
				ps.close();
	    	}
		} catch (SQLException e) {
    		throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
    	} catch (AppException e) {
    		throw new AppException(500, 500, "Internal Server Error", e.getDeveloperMessage(), "");
    	} catch (Exception e) {
    		throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
    	} finally {
    		try {
				con.close();
			} catch (SQLException e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			} catch (Exception e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			}
    	}
		
		return result.getTotalCount();
	}
}
