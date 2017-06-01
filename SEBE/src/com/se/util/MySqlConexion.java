package com.se.util;

import java.sql.*;

import com.se.errorhandling.AppException;

public class MySqlConexion {

	static{
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws AppException {
		
		Connection cn = null;
		String strConnection = "",
			   user = "",
			   pwd = "";
		
		try {
			PropertyValues prop = new PropertyValues();
			strConnection = "jdbc:mysql://" + prop.getPropertyValues("host") + 
		        	":" + prop.getPropertyValues("port") + "/" + prop.getPropertyValues("schema") + 
		        	"?noAccessToProcedureBodies=true";
		    user = prop.getPropertyValues("mysql-user");
			pwd = prop.getPropertyValues("mysql-pwd");
			cn = 
				DriverManager.getConnection(strConnection, user, pwd);
			
		} catch (SQLException e) {
			throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
		} catch (AppException e) {
			throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
		}
		
		return cn;
	}
	
}
