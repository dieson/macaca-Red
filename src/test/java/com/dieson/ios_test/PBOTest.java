package com.dieson.ios_test;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.dieson.driver.RedIOS;
import com.dieson.ios_page.LoginPage;
import com.dieson.ios_page.LogoutPage;
import com.dieson.ios_page.PBOPage;

public class PBOTest {
	private RedIOS screen;
	private String pboName;
	
	@Test
	public void pbo() {
		PBOPage pbo = new PBOPage(screen);
		pbo.goToPage(pboName);
	}

	@Parameters({"userName", "passWord", "country", "pboName"})
	@BeforeTest
	public void beforeTest(String userName, String passWord, String country, String pboName) {
		screen = new RedIOS();
		screen.start("ios");
		this.pboName = pboName;

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
