package com.bataon.ephesoft.dcma.dropbox.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bataon.ephesoft.dcma.dropbox.DropboxProperties;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;
import com.ephesoft.dcma.core.DCMAException;

public class DropboxHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(DropboxHelper.class);
	private static String TOKEN = "token";

	private Map<String, String> properties;
	private String pluginName;

	public DropboxHelper(Map<String, String> properties) {
		this.properties = properties;
	}

	public DbxClient authenticateApp() throws DCMAException {
		DbxClient result = null;

		String APP_KEY = properties.get(DropboxProperties.DROPBOX_APP_KEY.getPropertyKey());
		String APP_SECRET = properties.get(DropboxProperties.DROPBOX_APP_SECRET.getPropertyKey());
		String APP_CODE = properties.get(DropboxProperties.DROPBOX_APP_CODE.getPropertyKey());

		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

		DbxRequestConfig config = new DbxRequestConfig("Ephesoft/1.0", Locale.getDefault().toString());
		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

		String ACCESS_TOKEN = null;
		try {
			ACCESS_TOKEN = getToken();
		} catch (IOException e) {
			log(e.getMessage());
		}

		String authorizeUrl = webAuth.start();
		try {
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				if (APP_CODE == null || APP_CODE.length() == 0) {
					log("1. Go to: " + authorizeUrl);
					log("2. Click \"Allow\" (you might have to log in first)");
					log("3. Save the authorization code in the plugin properties.");
					throw new DCMAException("There is no available code for Dropbox.");
				} else {
					DbxAuthFinish authFinish = webAuth.finish(APP_CODE);
					ACCESS_TOKEN = authFinish.accessToken;
					saveToken(ACCESS_TOKEN);
				}
			}

			if (ACCESS_TOKEN != null && ACCESS_TOKEN.length() > 0) {
				result = new DbxClient(config, ACCESS_TOKEN);
				log(" - Linked account: " + result.getAccountInfo().displayName);
			}
		} catch (DbxException e) {
			log("If your code is not valid:");
			log("1. Go to: " + authorizeUrl);
			log("2. Click \"Allow\" (you might have to log in first)");
			log("3. Save the authorization code in the plugin properties.");
			
			throw new DCMAException(e.getMessage());
		} catch (IOException e) {
			throw new DCMAException(e.getMessage());
		}

		return result;
	}

	public void uploadDocument(DbxClient client, File inputFile, String outputFile) throws Exception {
		FileInputStream inputStream = new FileInputStream(inputFile);
		try {
			DbxEntry.File uploadedFile = client.uploadFile(outputFile, DbxWriteMode.add(), inputFile.length(), inputStream);
			log(" - Uploaded: " + uploadedFile.toString());
		} finally {
			inputStream.close();
		}
	}

	private String getToken() throws IOException {
		String token = null;

		File f = getPropertyFile();
		if (f != null && f.exists())
			token = extractToken(f);
		
		return token;
	}

	private File getPropertyFile() throws IOException {
		File f = null;

		// Try to open the file
		String home = System.getenv("DCMA_HOME");
		String folderPath = home + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "META-INF" + File.separator + getPluginName();
		File folder = new File(folderPath);

		if (folder.exists()) {
			String filePath = folderPath + File.separator + getPluginName() + ".properties";
			f = new File(filePath);
			if (!f.exists())
				f.createNewFile();
		} else
			folder.mkdir();

		return f;
	}

	private String extractToken(File file) throws FileNotFoundException, IOException {
		String token = null;

		Properties prop = new Properties();
		prop.load(new FileInputStream(file));
		token = prop.getProperty(TOKEN);

		return token;
	}

	private void saveToken(String token) throws IOException {
		File f = getPropertyFile();
		if (f.exists()) {
			Properties prop = new Properties();
			prop.load(new FileInputStream(f));
			if (prop.containsKey(TOKEN))
				prop.setProperty(TOKEN, token);
			else
				prop.put(TOKEN, token);
			prop.store(new FileOutputStream(f), "Update Dropbox access token");
		}
	}

	private void log(String msg) {
		LOGGER.debug(msg);
		System.out.println(msg);
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
}
