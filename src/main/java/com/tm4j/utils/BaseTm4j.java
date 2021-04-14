package com.tm4j.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * 
 * @author Surendra.Shekhawat
 *
 */
public class BaseTm4j {
	final static Logger logger = Logger.getLogger(BaseTm4j.class);
	protected static HttpURLConnection setUp(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestProperty("authorization", Tm4jConstatnts.apiToken);
		con.setRequestProperty("accept", "application/json");
		con.setRequestProperty("content-type", "application/json");

		return con;
	}

	protected static String readResponse(HttpURLConnection con) throws IOException {
		int responseCode = con.getResponseCode();
		logger.info("Response Code :: " + responseCode);
		logger.info("Response Status Message :: "+ con.getResponseMessage());
		if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		} else {
			return null;
		}
	}
}
