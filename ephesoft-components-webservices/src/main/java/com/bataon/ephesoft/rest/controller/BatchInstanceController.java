package com.bataon.ephesoft.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bataon.ephesoft.rest.bean.BatchInstance;
import com.bataon.ephesoft.rest.bean.BatchInstanceList;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.da.property.BatchInstanceProperty;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.license.aspect.LicenseAspect;

@Controller
public class BatchInstanceController {

	/**
	 * Initializing batchClassService {@link BatchClassService}.
	 */
	@Autowired
	private BatchClassService batchClassService;

	/**
	 * Initializing batchInstanceService {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	// //////////////////////// @ResponseBody ////////////////////////

	@RequestMapping(method = RequestMethod.GET, value = "/bataon/batchInstance/id/{id}")
	@ResponseBody
	public BatchInstance getBI(@PathVariable String id) throws DCMAException {
		LicenseAspect.aspectOf().secureWebService();
		BatchInstance bi = new BatchInstance(
				batchInstanceService.getBatchInstanceByIdentifier(id));
		return bi;
	}

	@RequestMapping(method=RequestMethod.GET, value="/bataon/batchInstance/status/{status}")
	@ResponseBody
	public BatchInstanceList getBIByStatus(@PathVariable String status) throws DCMAException {
		LicenseAspect.aspectOf().secureWebService();
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(BatchInstanceProperty.ID, true));
		
		List<com.ephesoft.dcma.da.domain.BatchInstance> batchInstances = batchInstanceService.getBatchInstByStatus(BatchInstanceStatus.valueOf(status));
		BatchInstanceList list = new BatchInstanceList();
		list.init(batchInstances);
		return list;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/bataon/batchInstance/status/active")
	@ResponseBody
	public BatchInstanceList getActiveBI() throws DCMAException {
		LicenseAspect.aspectOf().secureWebService();
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(BatchInstanceProperty.ID, true));
		
		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.add(BatchInstanceStatus.ERROR);
		statusList.add(BatchInstanceStatus.NEW);
		statusList.add(BatchInstanceStatus.READY);
		statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		statusList.add(BatchInstanceStatus.RUNNING);
		
		List<com.ephesoft.dcma.da.domain.BatchInstance> batchInstances = batchInstanceService.getBatchInstanceByStatusList(statusList);
		BatchInstanceList list = new BatchInstanceList();
		list.init(batchInstances);
		return list;
	}
}
