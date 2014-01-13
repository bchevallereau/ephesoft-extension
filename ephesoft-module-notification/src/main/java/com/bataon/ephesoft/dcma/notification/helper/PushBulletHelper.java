package com.bataon.ephesoft.dcma.notification.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PushBulletHelper {

	private String apiKey;
	private String testUrl;
	private String pushUrl;
	private String deviceIds;

	public boolean testConnection() {
		boolean verified = false;

		int responseCode;
		try {
			// Trying to get the list of devices
			URL obj = new URL(getTestUrl());
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			// Set authentication
			String userPassword = getApiKey() + ":";
			String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
			con.setRequestProperty("Authorization", "Basic " + encoding);

			responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			verified = responseCode == 200;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return verified;
	}

	public String pushNote(String title, String body) throws Exception {
		String result = "";
		for (String deviceId : deviceIds.split(";")) {
			URL obj = new URL(getPushUrl());
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add request header
			con.setRequestMethod("POST");

			// Set authentication
			String userPassword = getApiKey() + ":";
			String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
			con.setRequestProperty("Authorization", "Basic " + encoding);

			String urlParameters = "device_id=" + deviceId;
			urlParameters += "&type=note";
			urlParameters += "&title=" + title;
			urlParameters += "&body=" + body;

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			result += response.toString();
		}

		return result;

	}
	
	public String pushLink(String title, String url) throws Exception {
		String result = "";
		for (String deviceId : deviceIds.split(";")) {
			URL obj = new URL(getPushUrl());
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add request header
			con.setRequestMethod("POST");

			// Set authentication
			String userPassword = getApiKey() + ":";
			String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
			con.setRequestProperty("Authorization", "Basic " + encoding);

			String urlParameters = "device_id=" + deviceId;
			urlParameters += "&type=link";
			urlParameters += "&title=" + title;
			urlParameters += "&url=" + url;

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			result += response.toString();
		}

		return result;

	}

	public String getApiKey() {
		return apiKey;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String getPushUrl() {
		return pushUrl;
	}

	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

	public String getTestUrl() {
		return testUrl;
	}

	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}

}
