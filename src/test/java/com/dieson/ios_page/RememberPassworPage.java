package com.dieson.ios_page;

import java.util.Properties;

import com.dieson.driver.RedIOS;
import com.dieson.utils.PropertyUtil;

/** 
 * @author  Dieson Zuo 
 * @date 创建时间：24 Feb 2017 4:54:21 pm 
 */
public class RememberPassworPage {
	private RedIOS screen;
	
	private static final Properties REMEMBER_PASSWORD_PAGE = new PropertyUtil().loadProperties("/ios_properties/RememberPasswordPage.properties");
	
	public RememberPassworPage(RedIOS screen) {
		this.screen = screen;
	}
}
