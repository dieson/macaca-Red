package com.dieson.ios_page;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import macaca.client.commands.Element;
import macaca.client.common.ElementSelector;

import org.testng.Assert;

import com.dieson.driver.RedIOS;
import com.dieson.utils.PropertyUtil;

/**
 * @author Dieson Zuo
 * @date 创建时间：20 Mar 2017 3:36:35 pm
 */
public class HistoryPage {

	private RedIOS screen;

	private static final Properties HISTORY_PAGE = new PropertyUtil()
			.loadProperties("/ios_properties/HistoryPage.properties");

	private final String title = HISTORY_PAGE.getProperty("IOS_TITLE");
	private final String bookName = HISTORY_PAGE.getProperty("IOS_TITLENAME");
	private final String tocTitle = HISTORY_PAGE.getProperty("IOS_TOCTITLE");
	private final String tocList = HISTORY_PAGE.getProperty("IOS_TOCLIST");
	private final String backButton = HISTORY_PAGE.getProperty("IOS_BACKBUTTON");
	private final String historyTOCName = HISTORY_PAGE
			.getProperty("IOS_HISTORYTOCNAME");
	private final String historyTitleName = HISTORY_PAGE
			.getProperty("IOS_HISTORYTITLENAME");
	private final String history = HISTORY_PAGE.getProperty("IOS_HISTORY");
	private final String historyIcon = HISTORY_PAGE
			.getProperty("IOS_HISTORYICON");
	private final String popTitleName = HISTORY_PAGE
			.getProperty("IOS_POPTITLENAME");
	private final String popTOCName = HISTORY_PAGE
			.getProperty("IOS_POPTOCNAME");
	private final String historyNumberContent = HISTORY_PAGE.getProperty("IOS_HISTORYNUMBERCONTENT");
	private final String historyNumberPublication = HISTORY_PAGE.getProperty("IOS_HISTORYNUMBERPUBLICATION");
	private final String tableOfContents = HISTORY_PAGE.getProperty("IOS_TABLEOFCONTENTS");

	public HistoryPage(RedIOS screen) {
		this.screen = screen;
	}

	public void recentHistory() {
		screen.offGroup();
		// Open a book.
		screen.click(title, "Publication");
		screen.waitProgress();
		String titleName = screen.getText(bookName, "Title Name");

		// Create a history.
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
		screen.click(backButton, "Back to publication");
		screen.waitProgress();

		// Get the history info of the publication page.
		String historyTOC = screen.getText(historyTOCName, "History TOC name");
		String historyTitle = screen.getText(historyTitleName,
				"History title name");
		// Open this history
		screen.click(history, "History");
		screen.waitProgress();
		// Get the history info of the content page.
		screen.click(historyIcon, "History Icon");
		String titleNamePop = screen.getText(popTitleName, "Pop title name");
		String TOCNamePop = screen.getText(popTOCName, "Pop TOC name");
		screen.tap(611, 26);
		// Back to publication page.
		screen.click(backButton, "Back to publication");

		Assert.assertEquals(historyTitle, titleName);
		Assert.assertEquals(historyTOC, tocNameV);
		Assert.assertEquals(titleNamePop, titleName);
		Assert.assertEquals(TOCNamePop, tocNameV);
	}

	public void historyMaxNo() {
		screen.offGroup();
		// Open a book.
		screen.click(title, "Publication");
		screen.waitProgress();
		// Create 10 histories.
		for (int m = 0; m < 11; m++) {
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
			screen.click(tableOfContents, "Table of contents");
		}
		// Get the history number of the content page.
		screen.click(historyIcon, "History Icon");
		int historyNOContent = screen.findElements(historyNumberContent).size();
		// Back to publication page.
		screen.tap(611, 26);
		screen.click(backButton, "Back to publication");
		// Get the history number of the publication page.
		int historyNOPublication = screen.findElements(historyNumberPublication).size();
		
		Assert.assertEquals((historyNOContent < 10 || historyNOContent == 10), true);
		Assert.assertEquals((historyNOPublication < 10 || historyNOPublication == 10), true);
	}

}
