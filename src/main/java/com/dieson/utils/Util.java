package com.dieson.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSONObject;

import macaca.client.common.GetElementWay;

/**
 * @author Dieson Zuo
 * @date 创建时间：9 Jan 2017 2:47:31 pm
 */
public class Util {
	/**
	 * Get the configuration
	 * 
	 * @param key
	 * @return
	 */
	public static String getSystemProperties(String key) {
		InputStream inputStream = null;
		String value = "";

		inputStream = FileUtils.class.getResourceAsStream("/macaca.properties");

		if (inputStream == null) {
			ReportUtil.log("macaca.properties does not exist!");
			return null;
		}
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
			value = properties.getProperty(key);
		} catch (IOException e) {
			ReportUtil.log(e.toString());
		}
		return value;
	}
	
	/**
	 * Get the locator type
	 * 
	 * @param locator
	 * @return
	 */
	public static GetElementWay getLocatorType(String locator) {
		if (locator.contains("XPATH:")) {
			return GetElementWay.XPATH;
		} else if (locator.contains("ID:")) {
			return GetElementWay.ID;
		} else if (locator.contains("CLASS:")) {
			return GetElementWay.CLASS_NAME;
		} else if (locator.contains("TAGNAME:")) {
			return GetElementWay.TAG_NAME;
		} else if (locator.contains("LINKTEXT:")) {
			return GetElementWay.LINK_TEXT;
		} else if (locator.contains("NAME:")) {
			return GetElementWay.NAME;
		} else {
			ReportUtil.log("[Fail] Unable get locator type");
			return null;
		}
	}

	/**
	 * Get locator string
	 * 
	 * @param locator
	 * @return
	 */
	public static String getLocatorStr(String locator) {
		if (locator.contains("XPATH:")) {
			return locator.replaceFirst("XPATH:", "");
		} else if (locator.contains("ID:")) {
			return locator.replaceFirst("ID:", "");
		} else if (locator.contains("CLASS:")) {
			return locator.replaceFirst("CLASS:", "");
		} else if (locator.contains("TAGNAME:")) {
			return locator.replaceFirst("TAGNAME:", "");
		} else if (locator.contains("LINKTEXT:")) {
			return locator.replaceFirst("LINKTEXT:", "");
		} else if (locator.contains("NAME:")) {
			return locator.replaceFirst("NAME:", "");
		} else {
			ReportUtil.log("[Fail] Unable get locator string");
			return locator;
		}
	}
	
	public static int getJSONObject(Object obj, String key) {
		JSONObject json = (JSONObject) obj;
		return json.getInteger(key).intValue();
	}
}
