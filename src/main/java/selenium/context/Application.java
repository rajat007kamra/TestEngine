package selenium.context;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @summary To initiate sprint boot. This class is the entry point when tests start getting executed
 * @author Manoj.Jain
 */
@SpringBootApplication
public class Application {

	public static void main(final String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
