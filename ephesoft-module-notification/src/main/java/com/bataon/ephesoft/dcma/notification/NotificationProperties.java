package com.bataon.ephesoft.dcma.notification;

import com.ephesoft.dcma.core.common.PluginProperty;

public final class NotificationProperties implements PluginProperty {
	
	private NotificationProperties(String fieldKey) {
		this.key = fieldKey;
	}

	public String getPropertyKey() {
		return key;
	}

	public static final NotificationProperties NOTIFICATION_ACTIVE;
	public static final NotificationProperties NOTIFICATION_API_KEY;
	public static final NotificationProperties NOTIFICATION_DEVICE_IDS;
	public static final NotificationProperties NOTIFICATION_TITLE;
	public static final NotificationProperties NOTIFICATION_BODY;
	public static final NotificationProperties NOTIFICATION_ADD_LINK;
	public static final NotificationProperties NOTIFICATION_URL;

	String key;

	static {
		NOTIFICATION_ACTIVE = new NotificationProperties("notification.active");
		NOTIFICATION_API_KEY = new NotificationProperties("notification.APIKey");
		NOTIFICATION_DEVICE_IDS = new NotificationProperties("notification.deviceIds");
		NOTIFICATION_TITLE = new NotificationProperties("notification.title");
		NOTIFICATION_BODY = new NotificationProperties("notification.body");
		NOTIFICATION_ADD_LINK = new NotificationProperties("notification.addLink");
		NOTIFICATION_URL = new NotificationProperties("notification.url");
	}
}