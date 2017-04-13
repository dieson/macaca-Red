package com.dieson.ios_page;

import java.util.Properties;

import macaca.client.commands.Element;

import org.testng.Assert;

import com.dieson.driver.RedIOS;
import com.dieson.utils.PropertyUtil;
import com.dieson.utils.ReportUtil;

/** 
 * @author  Dieson Zuo 
 * @date 创建时间：14 Mar 2017 3:06:57 pm 
 */
public class PBOPage {
	private RedIOS screen; 
	
	private static final Properties PBO_PAGE = new PropertyUtil().loadProperties("/ios_properties/PBOPage.properties");
	
	private final String gotoButton = PBO_PAGE.getProperty("IOS_GOTOBUTTON");
	private final String pageNotFound = PBO_PAGE.getProperty("IOS_PAGENOTFOUND");
	private final String notFoundMsg = PBO_PAGE.getProperty("IOS_NOTFOUNDMSG");
	private final String searchResult = PBO_PAGE.getProperty("IOS_SEARCHRESULT");
	private final String pageNo = PBO_PAGE.getProperty("IOS_PAGENO");
	private final String publication = PBO_PAGE.getProperty("IOS_PUBLICATION");
	
	public PBOPage(RedIOS screen) {
		this.screen = screen;
	}
	
	public void inputNumber(String number) {
		char[] strChar = number.toCharArray();
		
		for (char NO:strChar) {
			switch (NO) {
			case '1':
				screen.click("NAME:1", "1");
				break;
			case '2':
				screen.click("NAME:2", "2");
				break;
			case '3':
				screen.click("NAME:3", "3");
				break;
			case '4':
				screen.click("NAME:4", "4");
				break;
			case '5':
				screen.click("NAME:5", "5");
				break;
			case '6':
				screen.click("NAME:6", "6");
				break;
			case '7':
				screen.click("NAME:7", "7");
				break;
			case '8':
				screen.click("NAME:8", "8");
				break;
			case '9':
				screen.click("NAME:9", "9");
				break;
			case '0':
				screen.click("NAME:0", "0");
				break;
			}
		}
	}
	
	public void goToPage(String pboName) {
		screen.offGroup();
		// Open the PBO title.
		int i = 1;
		lable:
		while (screen.isExistElement("XPATH://XCUIElementTypeScrollView[1]/XCUIElementTypeOther[" + i + "]", "Title")) {
			String titleName = "";
			try {
				Element title = screen.findElement("XPATH://XCUIElementTypeScrollView[1]/XCUIElementTypeOther[" + i + "]/XCUIElementTypeImage[1]/XCUIElementTypeOther[1]/XCUIElementTypeStaticText[1]");
				titleName = title.getText();
			} catch (Exception e) {
				ReportUtil.log(e.toString());
			} finally {
				if(titleName.equals(pboName)) {
					break lable;
				} else {
					i++;
				}
			}
		}
		screen.click("XPATH://XCUIElementTypeScrollView[1]/XCUIElementTypeOther[" + i + "]/XCUIElementTypeImage[1]/XCUIElementTypeButton[1]", "PBO Title");
		screen.waitProgress();
		
		screen.click(gotoButton, "Go to button");
		int number = (int) Math.random()*100 + 1;
		ReportUtil.log(number + "");
		this.inputNumber(number + "");
		
		if (screen.isExistElement(pageNotFound, "Page Not Found Icon")) {
			//page not found
			boolean message = screen.isExistElement(notFoundMsg, "Message");
			Assert.assertEquals(message, true);
		} else {
			//find the page
			screen.click(searchResult, "Search Result");
			screen.waitProgress();
			String pageNO = screen.getText(pageNo, "Page Number");
			Assert.assertEquals(pageNO.contains(Integer.toString(number)), true);
		}
		screen.click(publication, "Publication");
	}

}
