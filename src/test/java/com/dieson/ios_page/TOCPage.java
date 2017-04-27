package com.dieson.ios_page;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import macaca.client.commands.Element;

import org.testng.Assert;

import com.dieson.driver.RedIOS;
import com.dieson.utils.PropertyUtil;

/** 
 * @author  Dieson Zuo 
 * @date 创建时间：1 Apr 2017 4:45:51 pm 
 */
public class TOCPage {
	
	private RedIOS screen;
	
	private static final Properties TOC_PAGE = new PropertyUtil().loadProperties("/ios_properties/TOCPage.properties");
	
	private final String title = TOC_PAGE.getProperty("IOS_TITLE");
	private final String hideButton = TOC_PAGE.getProperty("IOS_HIDEBUTTON");
	private final String backButton = TOC_PAGE.getProperty("IOS_BACKBUTTON");
	private final String toc = TOC_PAGE.getProperty("IOS_TOC");
	private final String tocTitle = TOC_PAGE.getProperty("IOS_TOCTITLE");
	private final String backArrow = TOC_PAGE.getProperty("IOS_BACKARROW");
	private final String forwardArrow = TOC_PAGE.getProperty("IOS_FORWARDARROW");
	private final String tocList = TOC_PAGE.getProperty("IOS_TOCLIST");
	
	public TOCPage(RedIOS screen) {
		this.screen = screen;
	}
	
	public void hideTOC() {
		
		//Open a book.
		screen.click(title, "Publication");
		screen.waitProgress();
		
		//Get the TOC status
		boolean appear = (boolean)screen.getProperty(toc, "isVisible", "TOC List");
		// Full screen
		screen.click(hideButton, "Hide Button");
		boolean hide = (boolean)screen.getProperty(toc, "isVisible", "TOC List");
		screen.click(backButton, "Back to publication");
		screen.waitProgress();
		
		Assert.assertEquals(appear, true);
		Assert.assertEquals(hide, false);
	}
	
	public void previousNext() {
		
		// Open a book.
		screen.click(title, "Publication");
		screen.waitProgress();
		
		// Generate two TOC titles.
		String firstTOC = screen.getText(tocTitle, "TOC title");
		
		String tocNameI = "";
		String tocNameV = "1";
		for (int i = 0; !tocNameI.equals(tocNameV); i++) {
			
			List<Element> tocListI = new ArrayList<>();
			for (int j = 1; screen.isExistElement("XPATH://XCUIElementTypeOther[2]/XCUIElementTypeTable[1]/XCUIElementTypeCell[" + j + "]/XCUIElementTypeStaticText[1]", "TOC"); j++) {
				tocListI.add(screen.findElement("XPATH://XCUIElementTypeOther[2]/XCUIElementTypeTable[1]/XCUIElementTypeCell[" + j + "]/XCUIElementTypeStaticText[1]"));
			}
			
			int tocI = tocListI.size();
			int x = (int) (i + Math.random() * (tocI - i));
			tocNameI = screen.getText(tocListI.get(x), "TOC name");
			screen.click(tocListI.get(x), "TOC");
			
			List<Element> tocListV = new ArrayList<>();
			for (int j = 1; screen.isExistElement("XPATH://XCUIElementTypeOther[2]/XCUIElementTypeTable[1]/XCUIElementTypeCell[" + j + "]/XCUIElementTypeStaticText[1]", "TOC"); j++) {
				tocListV.add(screen.findElement("XPATH://XCUIElementTypeOther[2]/XCUIElementTypeTable[1]/XCUIElementTypeCell[" + j + "]/XCUIElementTypeStaticText[1]"));
			}
			int tocV = tocListV.size();
			if (tocV == tocI) {
				tocNameV = screen.getText(tocListI.get(x), "TOC name");
			}
		}
		String secoundTOC = screen.getText(tocTitle, "TOC title");
		
		// Forward and backward.
		screen.click(backArrow, "Back Arrow");
		screen.waitProgress();
		String checkFirstTOC = screen.getText(tocTitle, "TOC title");
		screen.click(forwardArrow, "Forward Arrow");
		screen.waitProgress();
		String checkSecoundTOC = screen.getText(tocTitle, "TOC title");
		
		// Back to publication
		screen.click(backButton, "Back to publication");
		
		Assert.assertEquals(checkFirstTOC, firstTOC);
		Assert.assertEquals(checkSecoundTOC, secoundTOC);
	}
	
}
