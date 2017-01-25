package com.dieson.ios_page;

import java.util.Map;
import java.util.Properties;

import org.testng.Assert;

import com.dieson.driver.RedIOS;
import com.dieson.utils.PropertyUtil;

/** 
 * @author  Dieson Zuo 
 * @date 创建时间：18 Jan 2017 10:52:12 am 
 */
public class LoginPage {
	
	private RedIOS screen;
	private Map<String, Object> data;
	
	private static final Properties LOGIN_PAGE_PROPERTIES = new PropertyUtil().loadProperties("/ios_properties/LoginPage.properties");
	
	private final String userName = LOGIN_PAGE_PROPERTIES.getProperty("IOS_USERNAME");
	private final String passWord = LOGIN_PAGE_PROPERTIES.getProperty("IOS_PASSWORD");
	private final String country = LOGIN_PAGE_PROPERTIES.getProperty("IOS_COUNTRY");
	private final String loginButton = LOGIN_PAGE_PROPERTIES.getProperty("IOS_LOGINBUTTON");
	private final String loginMessage = LOGIN_PAGE_PROPERTIES.getProperty("IOS_LOGINMESSAGE");
	private final String alertOK = LOGIN_PAGE_PROPERTIES.getProperty("IOS_ALERTOK");
	
	public LoginPage(RedIOS screen) {
		this.screen = screen;
	}
	
	public LoginPage(RedIOS screen, Map<String, Object> data) {
		this.screen = screen;
		this.data = data;
	}
	
	public void login(String userNameData, String passWordData, String countryData) {
		screen.skipTour();
		screen.click(country, "country");
		screen.click("NAME:" + countryData, countryData);
		screen.clear(userName, "username");
		screen.input(userName, userNameData, "username");
		screen.clear(passWord, "password");
		screen.input(passWord, passWordData, "password");
		screen.click(loginButton, "Login Button");
		screen.waitProgress();
		screen.waitProgress();
	}
	
	public void loginTest() {
		screen.skipTour();
		screen.click(country, "country");
		screen.click("NAME:" + data.get("country").toString(), data.get("country").toString());
		screen.clear(userName, "username");
		screen.input(userName, data.get("username").toString(), "username");
		screen.clear(passWord, "password");
		screen.input(passWord, data.get("password").toString(), "password");
		screen.click(loginButton, "Login Button");
		screen.wait(2);
		
		String message = screen.getText(loginMessage, "Login Message");
		screen.click(alertOK, "Error OK");
		
		Assert.assertEquals(message, data.get("msgExpect"));
	}
	
}
