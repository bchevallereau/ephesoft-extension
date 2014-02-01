package com.bataon.ephesoft.dcma.dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bataon.ephesoft.dcma.dropbox.helper.DropboxHelper;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.util.EphesoftStringUtil;

public class DropboxImporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(DropboxImporter.class);

	private String appKey;
	private String appSecret;
	private String appCode;
	private String batchClassConfig;

	private BatchClassService batchClassService;

	public void dropboxImport() throws DCMAException {
		DropboxHelper helper = new DropboxHelper(getProperties());
		helper.setPluginName("dcma-dropboximport-plugin");
		DbxClient client = helper.authenticateApp();

		String[] bcs = getBatchClassConfig().split(";");
		for (String batchClassIdentifier : bcs) {

			LOGGER.debug("Importing the batch class " + batchClassIdentifier);

			try {
				String dropboxFolder = getProperty(batchClassIdentifier, "folder");
				String filePattern = getProperty(batchClassIdentifier, "pattern");
				String action = getProperty(batchClassIdentifier, "action");
				String actionName = action.split("\\|")[0];

				LOGGER.debug(" - Dropbox folder: " + dropboxFolder);
				LOGGER.debug(" - File pattern: " + filePattern);

				DbxEntry.WithChildren listing = client.getMetadataWithChildren(dropboxFolder);
				LOGGER.debug(" - Files in the folder:");
				if (listing != null) {
					for (DbxEntry child : listing.children) {
						System.out.println(" -- " + child.name);

						if (child.name.matches(filePattern)) {
							System.out.println(" -- Import file");
							importFilesFromDropbox(child, batchClassIdentifier, client);

							if (actionName.equalsIgnoreCase("moveTo")) {
								String targetFolder = action.split("\\|")[1];
								client.move(child.path, targetFolder + "/" + child.name);
							} else if (actionName.equalsIgnoreCase("delete"))
								client.delete(child.path);

						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void importFilesFromDropbox(DbxEntry entry, String bcIdentifier, DbxClient client) throws Exception {
		String fileName = entry.name;
		if (null != fileName) {
			int indexOfDot = fileName.indexOf('.');
			if (-1 != indexOfDot) {
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(bcIdentifier);

				if (batchClass != null) {
					String uncFolder = batchClassService.getBatchClassByIdentifier(bcIdentifier).getUncFolder();
					String destinationFolder = EphesoftStringUtil.concatenate(new Object[] { uncFolder, File.separator, fileName.substring(0, indexOfDot), Character.valueOf('_'),
							Long.valueOf(System.currentTimeMillis()) });
					String destinationFile = EphesoftStringUtil.concatenate(new String[] { destinationFolder, File.separator, fileName });

					File folder = new File(destinationFolder);
					if (!folder.exists())
						folder.mkdir();

					FileOutputStream outputStream = new FileOutputStream(destinationFile);
					client.getFile(entry.path, null, outputStream);
				} else
					LOGGER.debug(bcIdentifier + " doesn't exist.");
			}
		}
	}

	private Map<String, String> getProperties() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(DropboxProperties.DROPBOX_APP_KEY.getPropertyKey(), getAppKey());
		map.put(DropboxProperties.DROPBOX_APP_CODE.getPropertyKey(), getAppCode());
		map.put(DropboxProperties.DROPBOX_APP_SECRET.getPropertyKey(), getAppSecret());
		return map;
	}

	private File getPropertyFile() throws IOException {
		File f = null;

		// Try to open the file
		String home = System.getenv("DCMA_HOME");
		String folderPath = home + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "META-INF" + File.separator + "dcma-dropboximport-plugin";
		File folder = new File(folderPath);

		if (folder.exists()) {
			String filePath = folderPath + File.separator + "dcma-dropboximport-plugin.properties";
			f = new File(filePath);
			if (!f.exists())
				f.createNewFile();
		} else
			folder.mkdir();

		return f;
	}

	private String getProperty(String bcIdentifier, String propName) throws IOException {
		String value = null;

		File f = getPropertyFile();
		if (f != null && f.exists()) {
			Properties prop = new Properties();
			prop.load(new FileInputStream(f));
			value = prop.getProperty(bcIdentifier + "." + propName);
		}

		return value;
	}

	public String getAppKey() {
		return appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getBatchClassConfig() {
		return batchClassConfig;
	}

	public void setBatchClassConfig(String batchClassConfig) {
		this.batchClassConfig = batchClassConfig;
	}

	public BatchClassService getBatchClassService() {
		return batchClassService;
	}

	public void setBatchClassService(BatchClassService batchClassService) {
		this.batchClassService = batchClassService;
	}

}