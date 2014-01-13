package com.bataon.ephesoft.dcma.notification;

import static org.junit.Assert.fail;

import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import com.bataon.ephesoft.dcma.notification.helper.PushBulletHelper;

public class HelperTest {
	
	private static Properties props = new Properties();
	
	private static PushBulletHelper helper;

	@BeforeClass
	public static void testSetup() {
		try {
			props.load(ClassLoader.getSystemResourceAsStream("unittest.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		helper = new PushBulletHelper();
		helper.setApiKey(props.getProperty("notification.apiKey"));
		helper.setTestUrl(props.getProperty("notification.url.test"));
		helper.setDeviceIds(props.getProperty("notification.deviceIds"));
		helper.setPushUrl(props.getProperty("notification.url.push"));
	}

	@Test
	public void testTestConnection() {
		if (!helper.testConnection())
			fail("Connection Test Impossible");
	}
	
	@Test
	public void testPushNotification() {
		String result = null;
		try {
			result = helper.pushNote("Test message", "Body of my test message.");
		} catch (Exception e) {
			fail("Error during Push Notification Test");
		}
		if (result == null || result.length() == 0)
			fail("Push Notification Test Failed");
	}

}
