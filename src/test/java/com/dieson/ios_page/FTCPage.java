package com.dieson.ios_page;

import macaca.client.commands.Element;

import org.testng.Assert;

import com.dieson.driver.RedIOS;

/** 
 * @author  Dieson Zuo 
 * @date 创建时间：13 Mar 2017 10:04:35 am 
 */ 
public class FTCPage {
	
	private RedIOS screen;
	
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
				isCaseTOC = screen.isExistElement("NAME:+ Cases", "+cases Icon");
				screen.drag(10, 607, 10, 153, 1, 1);
			} else {
				break;
			}
		}
		screen.click("XPATH://XCUIElementTypeApplication[1]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeNavigationBar[1]/XCUIElementTypeOther[1]/XCUIElementTypeButton[1]", "Publication");
	
		Assert.assertEquals(isImage, true);
		Assert.assertEquals(isCaseTOC, true);
	}
} 
