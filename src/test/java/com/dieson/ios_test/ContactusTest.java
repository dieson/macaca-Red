package com.dieson.ios_test;

import org.testng.annotations.Test;

import com.dieson.driver.RedIOS;
import com.dieson.ios_page.ContactusPage;
import com.dieson.ios_page.LoginPage;
import com.dieson.ios_page.LogoutPage;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.AfterTest;

public class ContactusTest {
	private RedIOS screen;
	
	@Test
	public void contactus() {
		ContactusPage contact = new ContactusPage(screen);
		contact.contactusVerify();
	}

	@Parameters({"userName", "passWord", "country"})
	@BeforeTest
	public void beforeTest(String userName, String passWord, String country) throws Exception {
		screen = new RedIOS();
		screen.start("ios");

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
