package com.dieson.ios_page;

import java.util.Properties;

import org.testng.Assert;

import com.dieson.driver.RedIOS;
import com.dieson.utils.PropertyUtil;

/** 
 * @author  Dieson Zuo 
 * @date 创建时间：19 Jan 2017 10:34:45 am 
 */
public class ContactusPage {
	
	private RedIOS screen;
	
	private static final Properties CONTACTUS_PAGE_PROPERTIES = new PropertyUtil().loadProperties("/ios_properties/ContactusPage.properties");
	
	private final String setting = CONTACTUS_PAGE_PROPERTIES.getProperty("IOS_SETTING");
	private final String help = CONTACTUS_PAGE_PROPERTIES.getProperty("IOS_HELP");
	private final String contactus = CONTACTUS_PAGE_PROPERTIES.getProperty("IOS_CONTACTUS");
	private final String phone = CONTACTUS_PAGE_PROPERTIES.getProperty("IOS_PHONE");
	private final String call = CONTACTUS_PAGE_PROPERTIES.getProperty("IOS_CALL");
	private final String fax = CONTACTUS_PAGE_PROPERTIES.getProperty("IOS_FAX");
	private final String email = CONTACTUS_PAGE_PROPERTIES.getProperty("IOS_EMAIL");
	private final String post = CONTACTUS_PAGE_PROPERTIES.getProperty("IOS_POST");
	private final String send = CONTACTUS_PAGE_PROPERTIES.getProperty("IOS_SEND");
	
	public ContactusPage(RedIOS screen) {
		this.screen = screen;
	}
	
	public void contactusVerify() {
		screen.click(setting, "Setting");
		screen.click(help, "Help");
		screen.click(contactus, "Contact Us");
		screen.wait(1);
		
		String phoneStr = screen.getText(phone, "Phone");
		String callStr = screen.getText(call, "Call");
		String faxStr = screen.getText(fax, "Fax");
		String emailStr = screen.getText(email, "Email");
		//String postStr = screen.getText(post, "Post");
		//String sendStr = screen.getText(send, "Send");
		
		screen.tap(424, 15);
		
		Assert.assertEquals(phoneStr, "1800 999 906");
		Assert.assertEquals(callStr, "+61 2 9422 2174");
		Assert.assertEquals(faxStr, "02 9422 2405");
		Assert.assertEquals(emailStr, "techsupport@lexisnexis.com.au");
		//Assert.assertEquals(postStr, "Level 9 Locked Bag 2222 Chatswood Delivery Centre Chatswood NSW 2067");
		//Assert.assertEquals(sendStr, "Street Address: LexisNexis DX 29590 Chatswood Tower 2, 475-495 Victoria Avenue Chatswood NSW 2067");
		
	}
}
