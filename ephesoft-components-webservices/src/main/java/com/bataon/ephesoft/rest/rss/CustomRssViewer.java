package com.bataon.ephesoft.rest.rss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.bataon.ephesoft.rest.bean.RssBatchStatus;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Item;

public class CustomRssViewer extends AbstractRssFeedView {

	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Channel feed, HttpServletRequest request) {

		feed.setTitle("Ephesoft RSS feed");
		feed.setDescription("Ephesoft RSS feed");
		feed.setLink("http://");

		super.buildFeedMetadata(model, feed, request);
	}

	@Override
	protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		@SuppressWarnings("unchecked")
		List<RssBatchStatus> listContent = (List<RssBatchStatus>) model.get("feedContent");
		List<Item> items = new ArrayList<Item>(listContent.size());

		for (RssBatchStatus tempContent : listContent) {

			Item item = new Item();

			Description description = new Description();
			description.setValue(tempContent.getSummary());

			item.setTitle(tempContent.getTitle());
			item.setDescription(description);
			item.setPubDate(tempContent.getCreatedDate());

			items.add(item);
		}

		return items;
	}
}
