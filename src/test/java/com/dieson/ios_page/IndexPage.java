package com.dieson.ios_page;

import java.util.Properties;

import macaca.client.commands.Element;

import com.dieson.driver.RedIOS;
import com.dieson.utils.PropertyUtil;
import com.dieson.utils.ReportUtil;

/** 
 * @author  Dieson Zuo 
 * @date 创建时间：28 Mar 2017 3:29:21 pm 
 */
public class IndexPage {
	private RedIOS screen;
	
	private static final Properties INDEX_PAGE = new PropertyUtil().loadProperties("/ios_properties/IndexPage.properties");
	
	private final String indexButton = INDEX_PAGE.getProperty("IOS_INDEXBUTTON");
	private final String backButton = INDEX_PAGE.getProperty("IOS_BACKBUTTON");
	
	public IndexPage(RedIOS screen) {
		this.screen = screen;
	}
	
	public void index(String indexName) {
		
		screen.offGroup();
		//open the index title
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
				if(titleName.equals(indexName)) {
					break lable;
				} else {
					i++;
				}
			}
		}
		
		screen.click("XPATH://XCUIElementTypeScrollView[1]/XCUIElementTypeOther[" + i + "]/XCUIElementTypeImage[1]/XCUIElementTypeButton[1]", "Index Title");
		screen.waitProgress();
		
		//Open the index
		screen.click(indexButton, "Index Button");
		screen.click(backButton, "Back to publication");
	}
	
	
}
