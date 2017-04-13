package com.dieson.ios_page;

import java.util.Properties;

import macaca.client.commands.Element;

import org.testng.Assert;

import com.dieson.driver.RedIOS;
import com.dieson.utils.PropertyUtil;

/** 
 * @author  Dieson Zuo 
 * @date 创建时间：13 Mar 2017 10:04:35 am 
 */ 
public class FTCPage {
	
	private RedIOS screen;
	
	private static final Properties FTC_PAGE = new PropertyUtil().loadProperties("/ios_properties/FTCPage.properties");
	
	private final String publication = FTC_PAGE.getProperty("IOS_PUBLICATION");
	private final String infoTitle = FTC_PAGE.getProperty("IOS_INFOTITLE");
	private final String plusCases = FTC_PAGE.getProperty("IOS_PLUSCASES");
	private final String plusCasesExp = FTC_PAGE.getProperty("IOS_PLUSCASESEXP");
	
	public FTCPage(RedIOS screen) {
		this.screen = screen;
	}
	
	public void ftc(String ftcName) {
		
		Element ftcTitle = null;
		int i = 1;
		while (screen.isExistElement("XPATH://XCUIElementTypeApplication[1]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeScrollView[1]/XCUIElementTypeScrollView[1]/XCUIElementTypeOther[" + i + "]", "Title")) {
			Element title = screen.findElement("XPATH://XCUIElementTypeScrollView[1]/XCUIElementTypeOther[" + i + "]/XCUIElementTypeImage[1]/XCUIElementTypeOther[1]/XCUIElementTypeStaticText[1]");
			if(screen.getText(title, "Title Name").equals(ftcName)) {
				ftcTitle = title;
				break;
			} else {
				i++;
			}
		}
		
		boolean isImage = screen.isExistElement("XPATH://XCUIElementTypeScrollView[1]/XCUIElementTypeOther[" + i + "]/XCUIElementTypeImage[1]/XCUIElementTypeImage[1]", "+cases Icon");
		screen.click(ftcTitle, "FTC Title");
		screen.waitProgress();
		
		boolean isCaseTOC = false;
		for (int j = 0; j < 3; j++) {
			if (!isCaseTOC) {
				screen.drag(10, 607, 10, 153, 1, 1);
				isCaseTOC = screen.isExistElement("NAME:+ Cases", "+cases Icon");
			} else {
				break;
			}
		}
		screen.click(publication, "Publication");
	
		Assert.assertEquals(isImage, true);
		Assert.assertEquals(isCaseTOC, true);
	}
	
	public void information(String ftcName) {
		
		int i = 1;
		while (screen.isExistElement("XPATH://XCUIElementTypeApplication[1]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeScrollView[1]/XCUIElementTypeScrollView[1]/XCUIElementTypeOther[" + i + "]", "Title")) {
			Element title = screen.findElement("XPATH://XCUIElementTypeScrollView[1]/XCUIElementTypeOther[" + i + "]/XCUIElementTypeImage[1]/XCUIElementTypeOther[1]/XCUIElementTypeStaticText[1]");
			if(screen.getText(title, "Title Name").equals(ftcName)) {
				break;
			} else {
				i++;
			}
		}
		
		screen.click("XPATH://XCUIElementTypeApplication[1]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeScrollView[1]/XCUIElementTypeScrollView[1]/XCUIElementTypeOther[" + i + "]/XCUIElementTypeButton[1]", "More Info");
		String infoTitleValue = screen.getText(infoTitle, "Info Title");
		String plusCasesValue = screen.getText(plusCases, "Plus Cases");
		
		screen.tap(60, 190);
		
		Assert.assertEquals(infoTitleValue + " + Cases", ftcName);
		Assert.assertEquals(plusCasesValue, plusCasesExp);
	}
	
	
} 
