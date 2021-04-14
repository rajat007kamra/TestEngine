package selenium.driver;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/** This class is responsible for making connection with Safari through LAMBDA TEST TOOL
 *
 * @author Surbhi.Singh
 *
 */
public class DriverSafari {
	final static Logger logger = Logger.getLogger(DriverSafari.class);
	public static WebDriver Safari() {
		String username = "surbhi.singh";
	    String accesskey = "qqYpPfPrM5SflGhvK3UehI6btulxrGSHwMLKSEMPfudbjvVA7V";
	    RemoteWebDriver driver = null;
	    String gridURL = "@hub.lambdatest.com/wd/hub";
	    boolean status = false;	
	    
	        DesiredCapabilities capabilities = new DesiredCapabilities();
	        capabilities.setCapability("browserName", "Chrome");
	        capabilities.setCapability("version", "86.0");
	        capabilities.setCapability("platform", "Windows 10"); // If this cap isn't specified, it will just get any available one.
	        capabilities.setCapability("build", "LambdaTestSampleApp");
	        capabilities.setCapability("name", "LambdaTestJavaSample");
	        capabilities.setCapability("network", true); // To enable network logs
	        capabilities.setCapability("visual", true); // To enable step by step screenshot
	        capabilities.setCapability("video", true); // To enable video recording
	        capabilities.setCapability("console", true); // To capture console logs
	        capabilities.setCapability("javascript.enabled", true);
	        capabilities.setCapability("resolution", "1920x1080");
	        try {
	            driver = new RemoteWebDriver(new URL("https://" + username + ":" + accesskey + gridURL), capabilities);
	            status = true;
	        } catch (MalformedURLException e) {
	            logger.info("Invalid grid URL");
	            status = false;
	        } catch (Exception e) {
	            logger.info(e.getMessage());
	            status = false;
	        }
	        
	        tearDownSafari(driver, status);
	        return driver;
	    }
		
 
    public static void tearDownSafari(RemoteWebDriver driver, Boolean status) {
        if (driver != null) {
            ((JavascriptExecutor) driver).executeScript("lambda-status=" + status);
            //driver.quit(); //really important statement for preventing your test execution from a timeout.
        }
    }
}
