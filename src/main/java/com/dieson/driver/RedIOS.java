package com.dieson.driver;

import com.dieson.utils.ReportUtil;

/**
 * @author Dieson Zuo
 * @date 创建时间：18 Jan 2017 12:44:32 pm
 */
public class RedIOS extends ElementUtil {

	public void waitProgress() {
		try {
			for (int i = 0; super.isExistElement("NAME:Loading", "Loading"); i++) {
				super.wait(1);
				if (i > 5) {
					break;
				}
			}
		} catch (Exception e) {
			ReportUtil.log(e.toString());
		}
	}

	public void skipTour() {
		if (super.isExistElement("NAME:Skip intro", "Skip Tour")) {
			super.click("NAME:Skip intro", "Skip Tour");
		}
	}

	/**
	 * Cancel messages push
	 */
	public void allowUpdate() {
		if (super.isExistElement("NAME:Don’t Allow", "Don’t Allow")) {
			super.click("NAME:Don’t Allow", "Don’t Allow");
		}
	}

	public void onGroup() {
		ReportUtil.log("On Group mode.");
		super.click("NAME:Edit", "Edit");
		
		Object status = super.getProperty("XPATH://XCUIElementTypeApplication[1]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[2]/XCUIElementTypeOther[3]/XCUIElementTypeOther[2]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeSwitch[1]", "value", "Switch");
		if (status.equals(false)) {
			super.click("CLASS:Switch", "Switch");
		}
		this.closeExpand();
	}

	public void offGroup() {
		ReportUtil.log("Off Group mode.");
		super.click("NAME:Edit", "Edit");
		
		Object status = super.getProperty("XPATH://XCUIElementTypeApplication[1]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[2]/XCUIElementTypeOther[3]/XCUIElementTypeOther[2]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeSwitch[1]", "value", "Switch");
		if (status.equals(true)) {
			super.click("XPATH://XCUIElementTypeApplication[1]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[2]/XCUIElementTypeOther[3]/XCUIElementTypeOther[2]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeSwitch[1]", "Switch");
		}
		this.closeExpand();
	}

	public void closeExpand() {
		ReportUtil.log("Close the expand window.");
		super.tap(15, 534);
	}
}
