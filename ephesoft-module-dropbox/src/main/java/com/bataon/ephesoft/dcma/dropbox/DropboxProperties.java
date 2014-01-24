package com.bataon.ephesoft.dcma.dropbox;

import com.ephesoft.dcma.core.common.PluginProperty;

public final class DropboxProperties implements PluginProperty {
	
	private DropboxProperties(String fieldKey) {
		this.key = fieldKey;
	}

	public String getPropertyKey() {
		return key;
	}

	public static final DropboxProperties DROPBOX_SWITCH;
	public static final DropboxProperties DROPBOX_APP_KEY;
	public static final DropboxProperties DROPBOX_APP_SECRET;
	public static final DropboxProperties DROPBOX_APP_CODE;
	public static final DropboxProperties DROPBOX_FOLDER;
	public static final DropboxProperties DROPBOX_UPLOAD_FILEEXT;
	public static final DropboxProperties DROPBOX_UPLOAD_FILENAME;

	String key;

	static {
		DROPBOX_SWITCH = new DropboxProperties("dropbox.switch");
		DROPBOX_APP_KEY = new DropboxProperties("dropbox.appKey");
		DROPBOX_APP_SECRET = new DropboxProperties("dropbox.appSecret");
		DROPBOX_APP_CODE = new DropboxProperties("dropbox.appCode");
		DROPBOX_FOLDER = new DropboxProperties("dropbox.folder");
		DROPBOX_UPLOAD_FILEEXT = new DropboxProperties("dropbox.extension");
		DROPBOX_UPLOAD_FILENAME = new DropboxProperties("dropbox.filename");
	}
	
}