package com.dieson.driver;

import com.alibaba.fastjson.JSONObject;
import com.dieson.utils.ReportUtil;
import com.dieson.utils.Util;

import macaca.client.MacacaClient;

/**
 * @author Dieson Zuo
 * @date 创建时间：9 Jan 2017 2:32:57 pm
 */
public class Init {
	MacacaClient driver;
	
	public Init(MacacaClient driver) {
		this.driver = driver;
	}
	
	public void startiOS() throws Exception {

		JSONObject porps = new JSONObject();
		porps.put("platformName", "ios");
		porps.put("app", Util.getSystemProperties("PATH_IOS"));
		porps.put("reuse", 1);

		porps.put("deviceName", Util.getSystemProperties("DEVICENAME_IOS"));

		JSONObject desiredCapabilities = new JSONObject();
		desiredCapabilities.put("desiredCapabilities", porps);
		driver.initDriver(desiredCapabilities);
	}

	public void startAndroid() throws Exception {

		JSONObject porps = new JSONObject();
		porps.put("platformName", "android");
		porps.put("app", Util.getSystemProperties("PATH_ANDROID"));
		porps.put("reuse", 1);

		porps.put("deviceName", Util.getSystemProperties("DEVICENAME_ANDROID"));

		JSONObject desiredCapabilities = new JSONObject();
		desiredCapabilities.put("desiredCapabilities", porps);
		driver.initDriver(desiredCapabilities);
	}
	
	/**
	 * Quit app.
	 */
	public void quit() {
		
		try {
			driver.quit();
			ReportUtil.log("Quit the app.");
		} catch (Exception e) {
			ReportUtil.log("[Faile] Quit the app.");
			ReportUtil.log(e.toString());
		}
	}

	/**
	 * Get the driver.
	 * 
	 * @return
	 */
	public MacacaClient getDriver() {
		
		return driver;
	}
	
}
