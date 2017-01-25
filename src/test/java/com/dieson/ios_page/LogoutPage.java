package com.dieson.ios_page;

import java.util.Properties;

import org.testng.Assert;

import com.dieson.driver.RedIOS;
import com.dieson.utils.PropertyUtil;

/** 
 * @author  Dieson Zuo 
 * @date 创建时间：19 Jan 2017 5:46:26 pm 
 */
public class LogoutPage {
	private RedIOS screen;
	
	private static final Properties LOGOUT_PAGE_PROPERTIES = new PropertyUtil().loadProperties("/ios_properties/LogoutPage.properties");
	
	private final String setting = LOGOUT_PAGE_PROPERTIES.getProperty("IOS_SETTING");
	private final String logoutButton = LOGOUT_PAGE_PROPERTIES.getProperty("IOS_LOGOUTBUTTON");
	
	public LogoutPage(RedIOS screen) {
		this.screen = screen;
	}
	
	public void logout() {
		screen.click(setting, "Setting");
		screen.click(logoutButton, "Log Out");
		screen.alertAccept();
	}
	
	public void logoutTest() {
		screen.click(setting, "Setting");
		screen.click(logoutButton, "Log Out");
		
		String message = screen.alertGetText();
		Assert.assertEquals(message, "Thank you for using LexisNexis Red application. Are you sure you want to exit ?");
		screen.alertDismiss();
		
		screen.click(logoutButton, "Log Out");
		screen.alertAccept();
	}
	
}
