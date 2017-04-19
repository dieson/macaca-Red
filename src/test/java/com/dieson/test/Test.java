package com.dieson.test;




/**
 * @author Dieson Zuo
 * @date 创建时间：9 Jan 2017 2:30:44 pm
 */
public class Test {
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 5; i++) {
			int x = (int) (i + Math.random() * (30 - i));
			System.out.println(x);
		}
		
		
	}

}
