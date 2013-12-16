package com.bataon.alfresco.webscripts;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class GetActiveBatchInstances extends DeclarativeWebScript {

	private static Log logger = LogFactory.getLog(GetActiveBatchInstances.class);

	private int port;
	private String host;
	private String protocol;
	private String username;
	private String password;
	private String url;
	private String reviewValidateUrl;

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
		try {
			if (logger.isDebugEnabled())
				logger.debug("Querying Ephesoft...");

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("data", getData());
			return model;
		} catch (Exception e) {
			e.printStackTrace();

			status.setCode(HttpServletResponse.SC_BAD_REQUEST);
			status.setMessage(e.getMessage());
			status.setException(e);
			status.setRedirect(true);
		}
		return super.executeImpl(req, status, cache);
	}

	private List<Map<String, String>> getData() throws Exception {

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		GetMethod getMethod = new GetMethod(createUrl());

		Credentials defaultcreds = new UsernamePasswordCredentials(getUsername(), getPassword());
		HttpClient client = new HttpClient();
		client.getState().setCredentials(new AuthScope(getHost(), getPort()), defaultcreds);
		client.getParams().setAuthenticationPreemptive(true);
		getMethod.setDoAuthentication(true);

		int statusCode;
		try {
			statusCode = client.executeMethod(getMethod);
			if (statusCode == 200) {
				
				if (logger.isDebugEnabled())
					logger.debug("Web service executed successfully.");
				String responseBody = getMethod.getResponseBodyAsString();
				
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(responseBody.getBytes("utf-8"))));
				
				NodeList bis = doc.getElementsByTagName("batchInstance");
				for (int i = 0; i < bis.getLength(); i++) {
					Node nNode = bis.item(i);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						
						String id = null;
						String status = null;
						
						Map<String, String> properties = new HashMap<String, String>();

						NodeList children = eElement.getChildNodes();
						for (int j = 0; j < children.getLength(); j++) {
							Node child = children.item(j);
							if (child.getNodeType() == Node.ELEMENT_NODE) {
								Element attr = ((Element) child);
								properties.put(attr.getNodeName(), attr.getTextContent());
							
								if (attr.getNodeName().equalsIgnoreCase("id"))
									id = attr.getTextContent();
								else if (attr.getNodeName().equalsIgnoreCase("status"))
									status = attr.getTextContent();
							}
						}
						
						// Generate Ephesoft URLs
						if (id != null && status != null)
							if (status.equalsIgnoreCase("READY_FOR_REVIEW") || status.equalsIgnoreCase("READY_FOR_VALIDATION"))
								properties.put("reviewUrl", generateReviewValidateUrl(id));
						
						data.add(properties);
					}
				}
			} else if (statusCode == 403) {
				if (logger.isErrorEnabled())
					logger.error("Invalid username/password.");
				throw new Exception("Invalid username/password.");
			} else {
				if (logger.isErrorEnabled())
					logger.error(getMethod.getResponseBodyAsString());
				throw new Exception(getMethod.getResponseBodyAsString());
			}
		} catch (HttpException e) {
			throw new Exception(e.getMessage());
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (getMethod != null) {
				getMethod.releaseConnection();
			}
		}

		return data;
	}

	private String generateReviewValidateUrl(String id) {
		return getProtocol() + "://" + getHost() + ":" + getPort() + getReviewValidateUrl() + id;
	}

	private String createUrl() {
		return getProtocol() + "://" + getHost() + ":" + getPort() + "/" + getUrl();
	}

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getUrl() {
		return url;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReviewValidateUrl() {
		return reviewValidateUrl;
	}

	public void setReviewValidateUrl(String reviewValidateUrl) {
		this.reviewValidateUrl = reviewValidateUrl;
	}
}
