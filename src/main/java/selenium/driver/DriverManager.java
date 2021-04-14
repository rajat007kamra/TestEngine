package selenium.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import selenium.driver.*;

/**
 * @summary Class for instantiate desired driver based on inputs provided
 * @author Surendra.Shekhawat
 */
public class DriverManager {

	private static final String HEADLESS = "headless";

	/**
	 * @summary Method for initiating web driver
	 * @param browserName
	 * @param appUrl
	 * @param methodName
	 * @return
	 * @throws MalformedURLException
	 */
	public static WebDriver createInstance(String browserName, String appUrl, String methodName)
			throws MalformedURLException {
		final String browserMode = System.getProperty("mode");
		WebDriver driver = null;
		String rootDir = null;
		
		if (OsCheck.getOperatingSystemType() == OsCheck.OSType.Linux) {
			rootDir = "driver/linux/";
		} else if (OsCheck.getOperatingSystemType() == OsCheck.OSType.MacOS) {
			rootDir = "driver/osx/";
		} else if (OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows) {
			rootDir = "driver/windows/";
		}

		if (browserName.toLowerCase().contains("firefox")) {
			if (OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows) {
				System.setProperty("webdriver.gecko.driver", rootDir + "geckodriver.exe");
			} else {
				System.setProperty("webdriver.gecko.driver", rootDir + "geckodriver");
			}

			if (browserMode != null && browserMode.equals(HEADLESS)) {
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				firefoxOptions.addArguments("--headless");
				firefoxOptions.addArguments("--no-sandbox");
				firefoxOptions.addArguments("--disable-dev-shm-usage");
				driver = new FirefoxDriver(firefoxOptions);
			} else {
				driver = new FirefoxDriver();
			}

		}
		
		if (browserName.toLowerCase().contains("edge")) {
			if (OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows) {
				System.setProperty("webdriver.edge.driver", rootDir + "msedgedriver.exe");
			} else {
				System.setProperty("webdriver.edge.driver", rootDir + "msedgedriver");
			}

			if (browserMode != null && browserMode.equals(HEADLESS)) {
				EdgeOptions edgeoptions = new EdgeOptions();
				edgeoptions.setCapability(HEADLESS, true);
				//edgeoptions.addArgument("headless");
				driver = new EdgeDriver(edgeoptions);
			} else {
				driver = new EdgeDriver();
			}

		}
		
		if (browserName.toLowerCase().contains("safari")) {
		    driver = DriverSafari.Safari();
		   
		}	
		
		if (browserName.toLowerCase().contains("chrome")) {
			if (OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows) {
				System.setProperty("webdriver.chrome.driver", rootDir + "chromedriver.exe");
			} else {
				System.setProperty("webdriver.chrome.driver", rootDir + "chromedriver");
			}

			if (browserMode != null && browserMode.equals(HEADLESS)) {
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--headless");
				chromeOptions.addArguments("--no-sandbox");
				chromeOptions.addArguments("--disable-dev-shm-usage");
				chromeOptions.addArguments("--window-size=1920,1080");
				chromeOptions.addArguments("--disable-gpu");
				chromeOptions.addArguments("--disable-extensions");
				chromeOptions.setExperimentalOption("useAutomationExtension", false);
				chromeOptions.addArguments("--proxy-server='direct://'");
				chromeOptions.addArguments("--proxy-bypass-list=*");
				chromeOptions.addArguments("--start-maximized");

				driver = new ChromeDriver(chromeOptions);
			} else {
				driver = new ChromeDriver();
			}

		}
		
		//Not currently used anywhere and to be used for SeleniumHub
		if (browserName.toLowerCase().contains("zalenium")) {
			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability("name", methodName);
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
		}
		
		driver.navigate().to(appUrl);
		return driver;
	}
}
