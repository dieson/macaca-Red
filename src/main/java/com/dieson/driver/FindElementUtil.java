package com.dieson.driver;

import com.dieson.utils.ReportUtil;
import com.dieson.utils.Util;

import macaca.client.MacacaClient;
import macaca.client.commands.Element;
import macaca.client.common.GetElementWay;

/**
 * @author Dieson Zuo
 * @date 创建时间：16 Jan 2017 2:30:22 pm
 */
public class FindElementUtil {
	private MacacaClient driver;

	public FindElementUtil(MacacaClient driver) {
		this.driver = driver;
	}

	public Element findElement(String locator) throws Exception {
		GetElementWay locatorType = Util.getLocatorType(locator);
		String locatorStr = Util.getLocatorStr(locator);
		Element element = null;

		try {
			element = driver.getElement(locatorType, locatorStr);
			ReportUtil.log("[Successful] Find the element");
		} catch (Exception e) {
			ReportUtil.log("[Fail] Unable find element");
			ReportUtil.log(e.toString());
		}
		return element;
	}
}
