package com.dieson.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.testng.internal.PropertyUtils;


/** 
 * @author  Dieson Zuo 
 * @date Jan 09, 2017 2:24:22 PM 
 * @parameter  
 */
public class PropertyUtil {

	private Properties properties = new Properties();

	/**
	 * Load properties
	 * @param path
	 * @return
	 */
	public Properties loadProperties(String path) {
		try {
			InputStream fileInputStream = PropertyUtils.class.getResourceAsStream(path);
			properties.load(fileInputStream);
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

}
