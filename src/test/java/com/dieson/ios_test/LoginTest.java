package com.dieson.ios_test;

import java.util.Iterator;
import java.util.Map;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.dieson.datautils.ExcelUtils;
import com.dieson.driver.RedIOS;
import com.dieson.ios_page.LoginPage;

public class LoginTest {
	private RedIOS screen;
	private ExcelUtils excel;
	
	@Test(dataProvider = "dp")
	public void login(Map<String, Object> data) {
		LoginPage login = new LoginPage(screen, data);
		login.loginTest();
	}

	@DataProvider(name = "dp")
	public Iterator<Object []> testData() {
		excel = new ExcelUtils("ios.xlsx", "Login");
		return excel;
	}

	@BeforeTest
	public void beforeTest() {
		screen = new RedIOS();
		screen.start("ios");
	}

	@AfterTest
	public void afterTest() {
		screen.quit();
	}

}
