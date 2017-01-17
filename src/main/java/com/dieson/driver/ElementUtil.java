package com.dieson.driver;

import org.testng.Assert;

import com.dieson.utils.ReportUtil;
import com.dieson.utils.Util;

import macaca.client.MacacaClient;
import macaca.client.commands.Element;
import macaca.client.common.GetElementWay;

/**
 * @author Dieson Zuo
 * @date 创建时间：16 Jan 2017 1:13:39 pm
 */
public class ElementUtil {
	private MacacaClient driver = new MacacaClient();

	private Init init = new Init(driver);
	private DriverUtil du = new DriverUtil(driver);
	private FindElementUtil feu = new FindElementUtil(driver);

	public void start(String platform) throws Exception {
		if (platform.equals("ios")) {
			init.startiOS();
		} else if (platform.equals("android")) {
			init.startAndroid();
		} else {
			ReportUtil.log("Unable start Macaca");
			Assert.fail();
		}
	}

	/**
	 * clear value
	 * 
	 * @param locator
	 * @param elementName
	 */
	public void clear(String locator, String elementName) {
		try {
			feu.findElement(locator).clearText();
			ReportUtil.log("[Successful] Clear the " + elementName);
		} catch (Exception e) {
			du.screenshot(elementName);
			ReportUtil.log("[Fail] Unable to Clear the " + elementName);
			Assert.fail();
		}
	}

	/**
	 * input value
	 * 
	 * @param locator
	 * @param value
	 * @param elementName
	 */
	public void input(String locator, Object value, String elementName) {

		try {
			Element element = feu.findElement(locator);
			element.sendKeys(value.toString());
			ReportUtil.log("[Successful] " + elementName + " input:" + value);
		} catch (Exception e) {
			du.screenshot(elementName);
			ReportUtil.log("[Fail] Unable to input");
			Assert.fail();
		}
	}

	/**
	 * Click on an element.
	 * 
	 * @param locator
	 * @param elementName
	 */
	public void click(String locator, String elementName) {
		try {
			feu.findElement(locator).click();
			ReportUtil.log("[Successful] Click the " + elementName);
		} catch (Exception e) {
			du.screenshot(elementName);
			ReportUtil.log("[Fail] Unable to click");
			Assert.fail();
		}
	}

	/**
	 * get text
	 * 
	 * @param locator
	 * @param elementName
	 * @return
	 */
	public String getText(String locator, String elementName) {
		String msg = "";
		try {
			msg = feu.findElement(locator).getText();
			ReportUtil.log("[Successful] Get the " + elementName);
		} catch (Exception e) {
			du.screenshot(elementName);
			ReportUtil.log("[Fail] Get attribute failure");
			Assert.fail();
		}
		return msg;
	}

	/**
	 * Get the result of a property of a element. 'isVisible', 'label', 'value',
	 * Android: 'selected', 'description', 'text'
	 * 
	 * @param locator
	 * @param attriubte
	 * @param elementName
	 * @return
	 */
	public String isSelect(String locator, String attriubte, String elementName) {
		String isSelected = "";
		try {
			Element element = feu.findElement(locator);
			isSelected = element.getProperty(attriubte).toString();
			ReportUtil.log("[Successful] " + elementName + attriubte + isSelected);
		} catch (Exception e) {
			du.screenshot(elementName.toString());
			ReportUtil.log("[Fail] Not to selected");
			ReportUtil.log(e.toString());
			Assert.fail();
		}
		return isSelected;
	}

	/**
	 * check if target element exist
	 * @param locator
	 * @return
	 */
	public boolean isExistElement(String locator, String elementName) {
		GetElementWay locatorType = Util.getLocatorType(locator);
		String locatorStr = Util.getLocatorStr(locator);
		boolean exist = true;

		try {
			exist = driver.isElementExist(locatorType, locatorStr);
			ReportUtil.log("[Successful] Check the element exist");
		} catch (Exception e) {
			exist = false;
			du.screenshot(elementName.toString());
			ReportUtil.log("[Fail] Cannot check the element exist");
			Assert.fail();
		}
		return exist;
	}

}
