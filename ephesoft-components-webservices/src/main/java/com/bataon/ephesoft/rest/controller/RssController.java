package com.bataon.ephesoft.rest.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bataon.ephesoft.rest.bean.SampleContent;

@Controller
public class RssController {
 
	@RequestMapping(value="/rssfeed", method = RequestMethod.GET)
	public ModelAndView getFeedInRss() {
 
		List<SampleContent> items = new ArrayList<SampleContent>();
 
		SampleContent content  = new SampleContent();
		content.setTitle("Spring MVC Tutorial 1");
		content.setUrl("http://www.mkyong.com/spring-mvc/tutorial-1");
		content.setSummary("Tutorial 1 summary ...");
		content.setCreatedDate(new Date());
		items.add(content);
 
		SampleContent content2  = new SampleContent();
		content2.setTitle("Spring MVC Tutorial 2");
		content2.setUrl("http://www.mkyong.com/spring-mvc/tutorial-2");
		content2.setSummary("Tutorial 2 summary ...");
		content2.setCreatedDate(new Date());
		items.add(content2);
 
		ModelAndView mav = new ModelAndView();
		mav.setViewName("rssViewer");
		mav.addObject("feedContent", items);
 
		return mav;
 
	}
 
}