package com.dieson.driver;

import java.io.File;

import org.testng.Assert;

import com.dieson.utils.ReportUtil;

import macaca.client.MacacaClient;

/**
 * @author Dieson Zuo
 * @date 创建时间：16 Jan 2017 10:30:22 am
 */
public class DriverUtil {
	private MacacaClient driver;
	
	public DriverUtil(MacacaClient driver) {
		this.driver = driver;
	}
	
	/**
	 * wait -- session
	 * 
	 * @param i
	 *            second
	 */
	public void sleep(int i) {
		try {
			driver.sleep(i * 1000);
		} catch (Exception e) {
			ReportUtil.log(e.toString());
		}
	}

	/**
	 * Sreenshot
	 */
	public void screenshot(String imageName) {

		try {
			File imageFile = new File("");
			String courseFile = imageFile.getCanonicalPath() + "/screenshot/" + imageName;
			driver.saveScreenshot(courseFile);

			ReportUtil.screenLog(courseFile);
		} catch (Exception e) {
			e.printStackTrace();
			ReportUtil.log("[Fail] Screenshot");
		}
	}

	/**
	 * Get alert text
	 * 
	 * @return
	 */
	public String alertGetText() {
		String text = "";

		try {
			text = driver.alertText();
			ReportUtil.log("[successful] Get alert: " + text);
		} catch (Exception e) {
			screenshot("Alert");
			ReportUtil.log("[Fail] Unable get text");
			ReportUtil.log(e.toString());
		}

		return text;
	}

	/**
	 * Alert accept
	 */
	public void alertAccept() {

		try {
			driver.acceptAlert();
			ReportUtil.log("[Successful] Accept");
		} catch (Exception e) {
			screenshot("Alert");
			ReportUtil.log("[Fail] Unable accept");
			ReportUtil.log(e.toString());
		}
	}

	/**
	 * Alert dismiss
	 */
	public void alertDismiss() {

		try {
			driver.dismissAlert();
			ReportUtil.log("[Successful] Dismiss");
		} catch (Exception e) {
			screenshot("Alert");
			ReportUtil.log("[Fail] Unable dismiss");
			ReportUtil.log(e.toString());
		}
	}

	/**
	 * tap by coordinate
	 * 
	 * @param x
	 * @param y
	 */
	public void tap(int x, int y) {

		try {
			driver.tap(x, y);
			ReportUtil.log("[Successful] Click the coordinate X:" + x + " Y:" + y);
		} catch (Exception e) {
			screenshot("tap");
			ReportUtil.log("[Fail] Unable to click the coordinate");
			Assert.fail();
		}
	}

	/**
	 * long press
	 * 
	 * @param x
	 *            coordinate - x
	 * @param y
	 *            coordinate - y
	 * @param duration
	 *            (for - iOS,time-unit:s) time to press(valid for
	 *            iOS,time-unit:s)
	 * @param steps
	 *            (for - android,time-unit:step) time to press（valid for
	 *            Android,1 step is about 5ms）
	 */
	public void press(double x, double y, double duration, int steps) {

		try {
			driver.press(x, y, duration, steps);
			ReportUtil.log("[Successful] Click the coordinate X:" + x + " Y:" + y);
		} catch (Exception e) {
			screenshot("Press");
			ReportUtil.log("[Fail] Unable to click the coordinate");
			Assert.fail();
		}
	}

	/**
	 * drag Support
	 * 
	 * @param xStart
	 *            drag start x-coordinate
	 * @param yStart
	 *            drag start y-coordinate
	 * @param xEnd
	 *            drag end x-coordinate
	 * @param yEnd
	 *            drag end y-coordinate
	 * @param duration
	 *            drag duration (valid for iOS,time-unit:s)
	 * @param steps
	 *            drag duration (valid for Android,time-unit:steps)
	 */
	public void drag(double xStart, double yStart, double xEnd, double yEnd, double duration, int steps) {
		
		try {
			driver.drag(xStart, yStart, xEnd, yEnd, duration, steps);
			ReportUtil.log("[Successful] Slide: (" + xStart + "," + yStart + ") => (" + xEnd + "," + yEnd + ")");
		} catch (Exception e) {
			screenshot("drag");
			ReportUtil.log("[Fail] Unable slide");
			ReportUtil.log(e.toString());
		}
	}

}
