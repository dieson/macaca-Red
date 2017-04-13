package com.dieson.ios_test;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.dieson.driver.RedIOS;
import com.dieson.ios_page.IndexPage;
import com.dieson.ios_page.LoginPage;
import com.dieson.ios_page.LogoutPage;

public class IndexTest {
	private RedIOS screen;
	private String indexName;
	
	@Test
	public void index() {
		IndexPage index = new IndexPage(screen);
		index.index(indexName);
	}
	
	@Parameters({"userName", "passWord", "country", "indexName"})
	@BeforeTest
	public void beforeTest(String userName, String passWord, String country, String indexName) {
		screen = new RedIOS();
		screen.start("ios");
		this.indexName = indexName;

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
