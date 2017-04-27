package com.dieson.driver;

import com.dieson.utils.ReportUtil;
import com.dieson.utils.Util;

import macaca.client.MacacaClient;
import macaca.client.commands.Element;
import macaca.client.common.ElementSelector;
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

	/**
	 * find element
	 * @param locator
	 * @return
	 * @throws Exception
	 */
	public Element findElement(String locator) {
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

	/**
	 * find elements
	 * @param locator
	 * @return
	 */
	public ElementSelector findElements(String locator) {
		GetElementWay locatorType = Util.getLocatorType(locator);
		String locatorStr = Util.getLocatorStr(locator);
		ElementSelector elements = null;

		try {
			switch (locatorType) {
			case XPATH:
				elements = driver.elementsByXPath(locatorStr);
				break;
			case ID:
				elements = driver.elementsById(locatorStr);
				break;
			case CLASS_NAME:
				elements = driver.elementsByClassName(locatorStr);
				break;
			case TAG_NAME:
				elements = driver.elementsByTagName(locatorStr);
				break;
			case LINK_TEXT:
				elements = driver.elementsByLinkText(locatorStr);
				break;
			case NAME:
				elements = driver.elementsByName(locatorStr);
				break;
			default:
				elements = null;
				break;
			}
			ReportUtil.log("[Successful] Find the element");
		} catch (Exception e) {
			ReportUtil.log("[Fail] Unable find element");
			ReportUtil.log(e.toString());
		}
		return elements;
	}
}
