package com.bataon.ephesoft.dcma.dropbox.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bataon.ephesoft.dcma.dropbox.DropboxExporter;
import com.bataon.ephesoft.dcma.dropbox.DropboxProperties;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.annotation.PostProcess;
import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;

public class DropboxPluginServiceImpl implements DropboxPluginService, ICommonConstants {

	private static final Logger LOGGER = LoggerFactory.getLogger(DropboxPluginServiceImpl.class);

	private BatchClassPluginConfigService batchClassPluginConfigService;
	private BatchSchemaService batchSchemaService;

	public void init() throws Exception {
		log("Initialization of the Dropbox Plugin Service.");
	}

	@PreProcess
	public void preProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		// Do Nothing
	}

	@PostProcess
	public void postProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		// Do Nothing
	}

	public void exportContent(BatchInstanceID batchInstanceID, String pluginWorkflow) throws DCMAException, DCMAApplicationException {
		log("Exporting documents to DropBox...");
		DropboxExporter exporter = new DropboxExporter(generateProperties(batchInstanceID));
		exporter.setBatchSchemaService(batchSchemaService);
		exporter.exportFiles(batchInstanceID.getID());
	}

	private Map<String, String> generateProperties(BatchInstanceID batchInstanceID) {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(DropboxProperties.DROPBOX_SWITCH.getPropertyKey(), batchClassPluginConfigService.getPluginPropertiesForBatch(batchInstanceID.getID(), "DROPBOX_PLUGIN").get(
				DropboxProperties.DROPBOX_SWITCH.getPropertyKey()));
		properties.put(DropboxProperties.DROPBOX_APP_KEY.getPropertyKey(), batchClassPluginConfigService.getPluginPropertiesForBatch(batchInstanceID.getID(), "DROPBOX_PLUGIN").get(
				DropboxProperties.DROPBOX_APP_KEY.getPropertyKey()));
		properties.put(DropboxProperties.DROPBOX_APP_SECRET.getPropertyKey(), batchClassPluginConfigService.getPluginPropertiesForBatch(batchInstanceID.getID(), "DROPBOX_PLUGIN").get(
				DropboxProperties.DROPBOX_APP_SECRET.getPropertyKey()));
		properties.put(DropboxProperties.DROPBOX_APP_CODE.getPropertyKey(), batchClassPluginConfigService.getPluginPropertiesForBatch(batchInstanceID.getID(), "DROPBOX_PLUGIN").get(
				DropboxProperties.DROPBOX_APP_CODE.getPropertyKey()));
		properties.put(DropboxProperties.DROPBOX_FOLDER.getPropertyKey(), batchClassPluginConfigService.getPluginPropertiesForBatch(batchInstanceID.getID(), "DROPBOX_PLUGIN").get(
				DropboxProperties.DROPBOX_FOLDER.getPropertyKey()));
		properties.put(DropboxProperties.DROPBOX_UPLOAD_FILEEXT.getPropertyKey(), batchClassPluginConfigService.getPluginPropertiesForBatch(batchInstanceID.getID(), "DROPBOX_PLUGIN").get(
				DropboxProperties.DROPBOX_UPLOAD_FILEEXT.getPropertyKey()));
		properties.put(DropboxProperties.DROPBOX_UPLOAD_FILENAME.getPropertyKey(), batchClassPluginConfigService.getPluginPropertiesForBatch(batchInstanceID.getID(), "DROPBOX_PLUGIN").get(
				DropboxProperties.DROPBOX_UPLOAD_FILENAME.getPropertyKey()));
		return properties;
	}

	private void log(String msg) {
		LOGGER.debug(msg);
		System.out.println(msg);
	}

	public BatchClassPluginConfigService getBatchClassPluginConfigService() {
		return batchClassPluginConfigService;
	}

	public void setBatchClassPluginConfigService(BatchClassPluginConfigService batchClassPluginConfigService) {
		this.batchClassPluginConfigService = batchClassPluginConfigService;
	}

	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

}
