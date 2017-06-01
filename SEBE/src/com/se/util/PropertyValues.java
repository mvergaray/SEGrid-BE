package com.se.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.se.errorhandling.AppException;

public class PropertyValues {
	Properties properties = null;
	InputStream inputStream = null;
	
	public PropertyValues() throws AppException {
		try {
			properties = new Properties();
			String propFileName = "config.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				properties.load(inputStream);
			} else {
				throw new AppException(500, 500, "Internal Server Error",
						"property file '" + propFileName + "' not found in the classpath", "");
			}
		} catch (Exception e) {
			throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			}
		}
	}
	
	public String getPropertyValues(String property) {
		return properties.getProperty(property);
	}
}
