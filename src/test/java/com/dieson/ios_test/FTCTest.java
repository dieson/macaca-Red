package com.dieson.ios_test;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import com.dieson.driver.RedIOS;
import com.dieson.ios_page.FTCPage;
import com.dieson.ios_page.LoginPage;
import com.dieson.ios_page.LogoutPage;

public class FTCTest {
	private RedIOS screen;
	private String ftcName;
	
	@Test
	public void ftc() {
		FTCPage ftc = new FTCPage(screen);
		ftc.ftc(ftcName);
	}

	@Parameters({"userName", "passWord", "country", "ftcName"})
	@BeforeTest
	public void beforeTest(String userName, String passWord, String country, String ftcName) {
		screen = new RedIOS();
		screen.start("ios");
		this.ftcName = ftcName;

		LoginPage login = new LoginPage(screen);
		login.login(userName, passWord, country);
	}

	@AfterTest
	public void afterTest() {
		LogoutPage logout = new LogoutPage(screen);
		logout.logout();
		screen.quit();
	}

}
