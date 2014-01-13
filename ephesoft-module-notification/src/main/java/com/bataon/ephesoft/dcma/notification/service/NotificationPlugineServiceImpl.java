package com.bataon.ephesoft.dcma.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.bataon.ephesoft.dcma.notification.NotificationProperties;
import com.bataon.ephesoft.dcma.notification.helper.PushBulletHelper;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.annotation.PostProcess;
import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.util.BackUpFileService;

public class NotificationPlugineServiceImpl implements NotificationPlugineService, ICommonConstants {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationPlugineServiceImpl.class);
	private PushBulletHelper pushBulletHelper;

	private BatchClassPluginConfigService batchClassPluginConfigService;

	public void init() throws Exception {
		log("Initialization of the Notification Plugin Service.");
	}

	@PreProcess
	public void preProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		BackUpFileService.backUpBatch(batchInstanceID.getID());
	}

	@PostProcess
	public void postProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		BackUpFileService.backUpBatch(batchInstanceID.getID(), pluginWorkflow);
	}

	public void pushNotification(BatchInstanceID batchInstanceID, String pluginWorkflow) throws DCMAException {
		log("Pushing notification...");

		boolean active = Boolean.valueOf(batchClassPluginConfigService.getPluginPropertiesForBatch(batchInstanceID.getID(), "ANDROID_NOTIFICATION_PLUGIN").get(
				NotificationProperties.NOTIFICATION_ACTIVE.getPropertyKey()));
		log("Notification Plugin, is Active ?" + active);
		
		if (active) {
			try {
				PushBulletHelper helper = getPushBulletHelper();
				String apiKey = batchClassPluginConfigService.getPluginPropertiesForBatch(batchInstanceID.getID(), "ANDROID_NOTIFICATION_PLUGIN").get(
						NotificationProperties.NOTIFICATION_API_KEY.getPropertyKey());
				String deviceIds = batchClassPluginConfigService.getPluginPropertiesForBatch(batchInstanceID.getID(), "ANDROID_NOTIFICATION_PLUGIN").get(
						NotificationProperties.NOTIFICATION_DEVICE_IDS.getPropertyKey());

				helper.setApiKey(apiKey);
				helper.setDeviceIds(deviceIds);

				String title = batchClassPluginConfigService.getPluginPropertiesForBatch(batchInstanceID.getID(), "ANDROID_NOTIFICATION_PLUGIN").get(
						NotificationProperties.NOTIFICATION_TITLE.getPropertyKey());
				String body = batchClassPluginConfigService.getPluginPropertiesForBatch(batchInstanceID.getID(), "ANDROID_NOTIFICATION_PLUGIN").get(
						NotificationProperties.NOTIFICATION_BODY.getPropertyKey());
				String url = batchClassPluginConfigService.getPluginPropertiesForBatch(batchInstanceID.getID(), "ANDROID_NOTIFICATION_PLUGIN").get(
						NotificationProperties.NOTIFICATION_URL.getPropertyKey());
				
				
				boolean addLink = Boolean.valueOf(batchClassPluginConfigService.getPluginPropertiesForBatch(batchInstanceID.getID(), "ANDROID_NOTIFICATION_PLUGIN").get(
						NotificationProperties.NOTIFICATION_ADD_LINK.getPropertyKey()));
				
				if (addLink) {
					url = url.replaceAll("$EphesoftBatchID", batchInstanceID.getID());
					helper.pushLink(title, url);
				} else
					helper.pushNote(title, body);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void log(String msg) {
		LOGGER.debug(msg);
		System.out.println(msg);
	}

	public PushBulletHelper getPushBulletHelper() {
		return pushBulletHelper;
	}

	public void setPushBulletHelper(PushBulletHelper pushBulletHelper) {
		this.pushBulletHelper = pushBulletHelper;
	}

	public BatchClassPluginConfigService getBatchClassPluginConfigService() {
		return batchClassPluginConfigService;
	}

	public void setBatchClassPluginConfigService(BatchClassPluginConfigService batchClassPluginConfigService) {
		this.batchClassPluginConfigService = batchClassPluginConfigService;
	}

}
