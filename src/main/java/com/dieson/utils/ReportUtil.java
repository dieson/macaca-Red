package com.dieson.utils;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.testng.Reporter;

/** 
 * @author  Dieson Zuo 
 * @date 创建时间：9 Jan 2017 2:36:26 pm 
 */
public class ReportUtil {
	
	public static void log(String comm){
		
		String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		Reporter.log("[" + time + "]" + comm );
		System.out.println("[" + time + "]" + comm );
	}
	
	public static void screenLog(String comm) {
		String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		Reporter.log(comm);
		System.out.println("[" + time + "]Screenshot: " + comm);
	}
}
