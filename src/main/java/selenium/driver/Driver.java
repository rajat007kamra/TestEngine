package selenium.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @summary This is class for instantiate web driver
 * @author Surendra.Shekhawat
 */
public class Driver {

	WebDriver driver;

	public Driver(WebDriver driver) {

		PageFactory.initElements(driver, this);
		this.driver = driver;
	}

	private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();

	public static WebDriver getDriver() {

		return webDriver.get();
	}

	public static void setWebDriver(WebDriver driver) {

		webDriver.set(driver);
	}

}
