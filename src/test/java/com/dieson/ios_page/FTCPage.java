package com.dieson.ios_page;

import java.util.Properties;

import macaca.client.commands.Element;

import org.testng.Assert;

import com.dieson.driver.RedIOS;
import com.dieson.utils.PropertyUtil;
import com.dieson.utils.ReportUtil;

/**
 * @author Dieson Zuo
 * @date 创建时间：13 Mar 2017 10:04:35 am
 */
public class FTCPage {

	private RedIOS screen;

	private static final Properties FTC_PAGE = new PropertyUtil()
			.loadProperties("/ios_properties/FTCPage.properties");

	private final String publication = FTC_PAGE.getProperty("IOS_PUBLICATION");
	private final String infoTitle = FTC_PAGE.getProperty("IOS_INFOTITLE");
	private final String plusCases = FTC_PAGE.getProperty("IOS_PLUSCASES");
	private final String plusCasesExp = FTC_PAGE
			.getProperty("IOS_PLUSCASESEXP");

	public FTCPage(RedIOS screen) {
		this.screen = screen;
	}

	public void ftc(String ftcName) {

		// Open the FTC title.
		int i = 1;
		lable: while (screen.isExistElement(
				"XPATH://XCUIElementTypeScrollView[1]/XCUIElementTypeOther["
						+ i + "]", "Title")) {
			String titleName = "";
			try {
				Element title = screen
						.findElement("XPATH://XCUIElementTypeScrollView[1]/XCUIElementTypeOther["
								+ i
								+ "]/XCUIElementTypeImage[1]/XCUIElementTypeOther[1]/XCUIElementTypeStaticText[1]");
				titleName = title.getText();
			} catch (Exception e) {
				ReportUtil.log(e.toString());
			} finally {
				if (titleName.equals(ftcName)) {
					break lable;
				} else {
					i++;
				}
			}
		}
		boolean isImage = screen.isExistElement(
				"XPATH://XCUIElementTypeScrollView[1]/XCUIElementTypeOther["
						+ i
						+ "]/XCUIElementTypeImage[1]/XCUIElementTypeImage[1]",
				"+cases Icon");
		screen.click(
				"XPATH://XCUIElementTypeScrollView[1]/XCUIElementTypeOther["
						+ i
						+ "]/XCUIElementTypeImage[1]/XCUIElementTypeButton[1]",
				"FTC Title");
		screen.waitProgress();

		boolean isCaseTOC = false;
		for (int j = 0; j < 3; j++) {
			if (!isCaseTOC) {
				screen.drag(10, 607, 10, 153, 1, 1);
				isCaseTOC = screen
						.isExistElement("NAME:+ Cases", "+cases Icon");
			} else {
				break;
			}
		}
		screen.click(publication, "Publication");

		Assert.assertEquals(isImage, true);
		Assert.assertEquals(isCaseTOC, true);
	}

	public void information(String ftcName) {

		// Select the FTC title.
		int i = 1;
		lable: while (screen.isExistElement(
				"XPATH://XCUIElementTypeScrollView[1]/XCUIElementTypeOther["
						+ i + "]", "Title")) {
			String titleName = "";
			try {
				Element title = screen
						.findElement("XPATH://XCUIElementTypeScrollView[1]/XCUIElementTypeOther["
								+ i
								+ "]/XCUIElementTypeImage[1]/XCUIElementTypeOther[1]/XCUIElementTypeStaticText[1]");
				titleName = title.getText();
			} catch (Exception e) {
				ReportUtil.log(e.toString());
			} finally {
				if (titleName.equals(ftcName)) {
					break lable;
				} else {
					i++;
				}
			}
		}
		screen.click(
				"XPATH://XCUIElementTypeApplication[1]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeScrollView[1]/XCUIElementTypeScrollView[1]/XCUIElementTypeOther["
						+ i + "]/XCUIElementTypeButton[1]", "More Info");
		String infoTitleValue = screen.getText(infoTitle, "Info Title");
		String plusCasesValue = screen.getText(plusCases, "Plus Cases");

		screen.tap(60, 190);

		Assert.assertEquals(infoTitleValue + " + Cases", ftcName);
		//Assert.assertEquals(plusCasesValue, plusCasesExp);
	}

}
