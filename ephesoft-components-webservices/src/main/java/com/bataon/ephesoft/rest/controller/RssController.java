package com.bataon.ephesoft.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bataon.ephesoft.rest.bean.RssBatchStatus;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchInstanceService;

@Controller
public class RssController {
	
	/**
	 * Initializing batchInstanceService {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;
 
	@RequestMapping(value="/rssfeed", method = RequestMethod.GET)
	public ModelAndView getFeedInRss() {
		List<RssBatchStatus> items = new ArrayList<RssBatchStatus>();
 
		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);

		List<BatchInstance> batchInstances = batchInstanceService.getBatchInstanceByStatusList(statusList);
		for (BatchInstance batchInstance : batchInstances) {
			RssBatchStatus status  = new RssBatchStatus(batchInstance);
			items.add(status);
		}
 
		ModelAndView mav = new ModelAndView();
		mav.setViewName("rssViewer");
		mav.addObject("feedContent", items);
 
		return mav;
 
	}
 
}