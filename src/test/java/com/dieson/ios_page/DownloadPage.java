package com.dieson.ios_page;

import java.util.Properties;

import com.dieson.driver.RedIOS;
import com.dieson.utils.PropertyUtil;

import macaca.client.common.ElementSelector;

/**
 * @author Dieson Zuo
 * @date 创建时间：15 Fer 2017 10:34:45 am
 */
public class DownloadPage {

	private RedIOS screen;
	
	private static final Properties DOWNLOAD_PAGE_PROPETIES = new PropertyUtil().loadProperties("/ios_properties/DownloadPage.properties");

	private final String download = DOWNLOAD_PAGE_PROPETIES.getProperty("IOS_DOWNLOAD");
	private final String downloading = DOWNLOAD_PAGE_PROPETIES.getProperty("IOS_DOWNLOADING");
	
	public DownloadPage(RedIOS screen) {
		this.screen = screen;
	}

	/**
	 * Download all the title
	 * @throws Exception
	 */
	public void downloadAll() {

		screen.offGroup();
		ElementSelector elements = screen.findElements(download);
		
		for (int i = 0; i < elements.size(); i++) {
			screen.click(elements.getIndex(i), "Download Button");
		}
		
		while (screen.isExistElement(downloading, "Downloading status")) {
			screen.wait(1);
		}
	}

}
