package selenium.listeners;

import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import selenium.context.Base;
import selenium.driver.Driver;
import selenium.driver.DriverManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @summary Web drive listener for invoking test cases
 * @author Surendra.Shekhawat
 */
public class WebDriverListener implements IInvokedMethodListener {
	final static Logger logger = Logger.getLogger(WebDriverListener.class);
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		PropertyConfigurator.configure("src/main/resources/log4j.properties");
		if (method.isTestMethod()) {
			String browserName = ((Base) method.getTestMethod().getInstance()).getBrowser();
			if (browserName != null && !browserName.equalsIgnoreCase("API")) {
				try {
					WebDriver driver = DriverManager.createInstance(browserName,
							((Base) method.getTestMethod().getInstance()).getAppUrl(),
							method.getTestMethod().getMethodName());
					System.out
							.println("Initializing webdriver session --> Thread ID: " + Thread.currentThread().getId());
					logger.info("Running test --> " + method.getTestMethod().getMethodName());
					driver.navigate().refresh();
					Driver.setWebDriver(driver);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @summary This method gets invoked when a test case is exited and actions such a killing browser or taking screen shots
	 */
	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {

		if (method.isTestMethod()) {
			String browserName = ((Base) method.getTestMethod().getInstance()).getBrowser();
			if (browserName != null && !browserName.equalsIgnoreCase("API")) {
				try {
					WebDriver driver = Driver.getDriver();
					if (driver != null) {
						takeScreenshotOnFailure(testResult);
						logger.info("Closing webdriver session: " + Thread.currentThread().getId());
						driver.quit();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @summary Capturing screen shots in case of failures 
	 * @param testResult
	 * @throws IOException
	 */
	public void takeScreenshotOnFailure(ITestResult testResult) throws IOException {

		if (testResult.getStatus() == ITestResult.FAILURE) {

			File screenShot = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.FILE);
			File destination = new File("target/failure-screenshots/" + testResult.getName() + "-"
					+ new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date()) + ".png");
			FileUtils.copyFile(screenShot, destination);

			InputStream screenShotStream = new FileInputStream(destination);
			byte[] screen = IOUtils.toByteArray(screenShotStream);

			saveScreenshot(screen);
		}
	}

	@Attachment(value = "Screenshot of the failure", type = "image/png")
	public byte[] saveScreenshot(byte[] screenShot) {

		return screenShot;
	}
}
