package selenium.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @summary Auto wiring sprint boot with browser and url as bean so that it can be referred anywhere
 * @author Surendra.Shekhawat
 */
@Configuration
public class DomainConfiguration {

	@Autowired
	private Environment env;

	@Bean(name = "appUrl")
	public String appUrl() {
		return env.getProperty("appUrl");
	}

	@Bean(name = "browser")
	public String browser() {
		return env.getProperty("browser");
	}
}
