package com.dieson.test;

import com.alibaba.fastjson.JSONObject;


/**
 * @author Dieson Zuo
 * @date 创建时间：9 Jan 2017 2:30:44 pm
 */
public class Test {
	public static void main(String[] args) throws Exception {
		Object c;
		
		boolean a = true;
		c = a;
		JSONObject b = (JSONObject) c;
		System.out.println(b.toString());
	}

}
