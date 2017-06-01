package com.se.dao;

import com.se.interfaces.RecordDAO;

public abstract class DAOFactory {
    
    public static final int MYSQL = 1;
    
    public abstract RecordDAO getRecordDAO();
    public static DAOFactory getDAOFactory(int whichFactory){
       switch(whichFactory){
      	case MYSQL:
       	    return new MySqlDAOFactory();
       	default:
       	    return null;
       }
    }
    
    
}
