package testNG.utils;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @summary This class is responsible for scrapping basic information from Cliq URL
 * @author Manoj.Jain
 */
public class BasicInfoUtility {
	private final String tenant = "mezocliq";
	private final String version = "version.html";
	private final String separators = "/";
	private final String protocol = "https://";
	private String appUrl;

	public BasicInfoUtility(String appUrl) {
		this.appUrl = appUrl;
	}

	private String getBuildInfo(String URL) throws IOException {
		Document document = Jsoup.connect(URL).get();
		return document.select("body").text();
	}

	private String getEnv() {
		return appUrl.split("/")[2];
	}

	public void getSubTenantName() {
		// TODO
	}

	public String getEnvName() {
		return getEnv();
	}

	public String getBuildInfo() throws IOException {
		return getBuildInfo(protocol + getEnv()+separators +tenant+separators+ version);
	}
}
