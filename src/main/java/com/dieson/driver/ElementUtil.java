package com.dieson.driver;

import org.testng.Assert;

import com.dieson.utils.ReportUtil;
import com.dieson.utils.Util;

import macaca.client.MacacaClient;
import macaca.client.commands.Element;
import macaca.client.common.ElementSelector;
import macaca.client.common.GetElementWay;

/**
 * @author Dieson Zuo
 * @date 创建时间：16 Jan 2017 1:13:39 pm
 */
public class ElementUtil {
	protected MacacaClient driver = new MacacaClient();

	private Init init = new Init(driver);
	private DriverUtil du = new DriverUtil(driver);
	private FindElementUtil feu = new FindElementUtil(driver);

	/**
	 * Start app
	 * @param platform
	 * @throws Exception
	 */
	public void start(String platform) {
		if (platform.equals("ios")) {
			init.startiOS();
		} else if (platform.equals("android")) {
			init.startAndroid();
		} else {
			ReportUtil.log("Unable start Macaca ");
			Assert.fail();
		}
	}
	
	/**
	 * Quit app.
	 */
	public void quit() {
		init.quit();
	}
	
	/**
	 * find element
	 * @param locator
	 * @return
	 */
	public Element findElement(String locator) {
		return feu.findElement(locator);
	}
	
	/**
	 * find elements
	 * @param locator
	 * @return
	 */
	public ElementSelector findElements(String locator) {
		return feu.findElements(locator);
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
			ReportUtil.log(e.toString());
			Assert.fail();
		}
	}
	
	/**
	 * click an elelement from element 
	 * @param element	
	 * @param elementName
	 */
	public void click(Element element, String elementName) {
		try {
			element.click();
			ReportUtil.log("[Successful] Click the " + elementName);
		} catch (Exception e) {
			du.screenshot(elementName);
			ReportUtil.log("[Fail] Unable to click " + elementName);
			Assert.fail();
		}
	}

	/**
	 * Click on an element from locator
	 * 
	 * @param locator
	 * @param elementName
	 */
	public void click(String locator, String elementName) {
		Element element = feu.findElement(locator);
		this.click(element, elementName);
	} 
	
	public String getText(Element element, String elementName) {
		String msg = "";
		try {
			msg = element.getText();
			ReportUtil.log("[Successful] Get the " + elementName + ":" + msg);
		} catch (Exception e) {
			du.screenshot(elementName);
			ReportUtil.log("[Fail] Get attribute failure");
			Assert.fail();
		}
		return msg;
	}
		
	/**
	 * get text
	 * 
	 * @param locator
	 * @param elementName
	 * @return
	 */
	public String getText(String locator, String elementName) {
		Element element = feu.findElement(locator);
		return this.getText(element, elementName);
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
	public Object getProperty(String locator, String attriubte, String elementName) {
		Object isSelected = "";
		try {
			Element element = feu.findElement(locator);
			isSelected = element.getProperty(attriubte);
			ReportUtil.log("[Successful] " + elementName + attriubte);
		} catch (Exception e) {
			du.screenshot(elementName.toString());
			ReportUtil.log("[Fail] Unable to get attriubte.");
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
	
	/**
	 * wait
	 * @param secound
	 * @throws Exception
	 */
	public void wait(int secound) {
		try {
			driver.sleep(secound * 1000);
		} catch (Exception e) {
			du.screenshot("wait");
			ReportUtil.log("[Fail] Waiting for the failure");
			ReportUtil.log(e.toString());
			Assert.fail();
		}
	}
	
	/**
	 * Sreenshot
	 */
	public void screenshot(String imageName) {
		du.screenshot(imageName);
	}

	/**
	 * Get alert text
	 * 
	 * @return
	 */
	public String alertGetText() {
		return du.alertGetText();
	}
	
	/**
	 * Alert accept
	 */
	public void alertAccept() {
		du.alertAccept();
	}
	
	/**
	 * Alert dismiss
	 */
	public void alertDismiss() {
		du.alertDismiss();
	}
	
	/**
	 * Get the rect
	 * @param locator
	 * @param elementName
	 * @return
	 */
	public Object getRect(String locator, String elementName) {
		Object rect = new Object();
		try {            
			rect = feu.findElement(locator).getRect();
			ReportUtil.log("[Successful] Get the rect " + rect.toString());
		} catch (Exception e) {
			du.screenshot(elementName);
			ReportUtil.log("[Fail] Unable get the " + elementName + " rect");
			Assert.fail();
		}
		return rect;
	}
	
	/**
	 * tap by coordinate
	 * @param x
	 * @param y
	 */
	public void tap(int x, int y) {
		du.tap(x, y);
	}
	
	public void drag(double xStart, double yStart, double xEnd, double yEnd, double duration, int steps) {
		du.drag(xStart, yStart, xEnd, yEnd, duration, steps);
	}
}
