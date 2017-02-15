package com.dieson.ios_test;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.dieson.driver.RedIOS;
import com.dieson.ios_page.LoginPage;
import com.dieson.ios_page.LogoutPage;

public class LogoutTest {
	private RedIOS screen;
	
	@Test
	public void logout() {
		LogoutPage logout = new LogoutPage(screen);
		logout.logoutTest();
	}

	@Parameters({"userName", "passWord", "country"})
	@BeforeTest
	public void beforeTest(String userName, String passWord, String country) {
		screen = new RedIOS();
		screen.start("ios");

		LoginPage login = new LoginPage(screen);
		login.login(userName, passWord, country);
	}

	@AfterTest
	public void afterTest() {
		screen.quit();
	}

}
