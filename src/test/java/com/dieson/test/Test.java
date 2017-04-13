package com.dieson.test;

import java.util.Properties;

import com.dieson.utils.PropertyUtil;



/**
 * @author Dieson Zuo
 * @date 创建时间：9 Jan 2017 2:30:44 pm
 */
public class Test {
	public static void main(String[] args) throws Exception {
		Properties test = new PropertyUtil().loadProperties("/ios_properties/PBOPage.properties");
		String i = "abc";
		String j = test.getProperty("TEST");
		System.out.println(j);
	}

}
