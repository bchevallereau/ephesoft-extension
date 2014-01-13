package com.bataon.ephesoft.dcma.notification.service;

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.da.id.BatchInstanceID;

/**
 * This is a Dummy Plugin Service.
 * 
 * @author Ephesoft
 * 
 * @version 1.0
 * @see com.ephesoft.dcma.cmis.service.CMISExportServiceImpl
 */
public interface NotificationPlugineService {

	/**
	 * This is a dummy method which is to be called by workflow.
	 * 
	 * @param batchInstanceID
	 * @throws DCMAException
	 */
	void pushNotification(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;

}
