package testNG.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

/**
 * @summary Class for responsible for generating TestNG file for given subcategory (aka battery)
 * @author Manoj.Jain
 */
public class DynamicTestNG {
	final static Logger logger = Logger.getLogger(DynamicTestNG.class);
	public static XmlSuite suite = null;
	public static XmlTest test = null;
	public static XmlClass classes = null;
	public static List<XmlSuite> suiteList = null;

	/**
	 * @summary Method responsible for generating TestNG file with name as <subcategory>testNG.xml
	 * @param suiteIdentifiers
	 * @throws ClassNotFoundException
	 */
	public String createTestNGFile(SuiteIdentifiers suiteIdentifiers, String thread_count)
			throws ClassNotFoundException {
		String suiteName = suiteIdentifiers.getSuiteName();
		String suiteNameNoSpace = suiteName.replace(" ", "");
		String testNgFileName = suiteNameNoSpace + suiteIdentifiers.getTestNgFileName();

		createXMLSuite(suiteIdentifiers.getSuiteName(), suiteIdentifiers.isParallel(),
				suiteIdentifiers.getVerboseValue(), suiteIdentifiers.getThreadCount());
		if (thread_count.isBlank()) {
			String suite_name = suiteIdentifiers.getSuiteName();
			createTestSuite(suite_name, suiteIdentifiers.isPreserveOrder(), suiteIdentifiers.getCategory(),
					suiteIdentifiers.getBattery());
			createClassSuite(suiteIdentifiers.getTestCases());
		} else {
			//For Performance suite
			for (int i = 0; i < Integer.parseInt(thread_count); i++) {
				String suite_name = suiteIdentifiers.getSuiteName() + "_" + i;
				createTestSuite(suite_name, suiteIdentifiers.isPreserveOrder(), suiteIdentifiers.getCategory(),
						suiteIdentifiers.getBattery());
				createClassSuite(suiteIdentifiers.getTestCases());
			}
		}
		// Creating object of TestNG class
		TestNG testng = new TestNG();
		testng.setXmlSuites(suiteList);

		// Set the list of Suites to the testNG object you created earlier.

		suite.setFileName(testNgFileName);
		// testng.run();

		// Create physical XML file based on the virtual XML content
		createXmlFile(suite, testNgFileName);
		logger.info("testNgfile" + testng);
		logger.info("File Created Successfully.");
		return testNgFileName;
	}

	/**
	 * @summary This method is responsible for creating xml test suite out of json test suite
	 * @param suiteName
	 * @param isSuiteParallel
	 * @param verboseValue
	 * @param threadCount
	 */
	private void createXMLSuite(String suiteName, boolean isSuiteParallel, int verboseValue, int threadCount) {
		// Creating suite
		List<String> listners = new ArrayList<String>();
		listners.add(TestNGEmailableReport.class.getName());
		suite = new XmlSuite();
		suite.setName(suiteName);
		suite.onParameterElement("parallel", String.valueOf(isSuiteParallel));
		suite.setVerbose(verboseValue);
		suite.setThreadCount(threadCount);
		suite.setListeners(listners);
		if (threadCount > 1) {
			suite.setParallel(XmlSuite.ParallelMode.TESTS);
		}
	}

	/**
	 * @summary Private method for xml test suite attributes 
	 * @param testName
	 * @param preserveOrder
	 * @param category
	 * @param battery
	 */
	private void createTestSuite(String testName, boolean preserveOrder, String category, String battery) {
		test = new XmlTest(suite);
		test.setName(testName);
		test.addParameter("preserve-order", String.valueOf(preserveOrder));
		test.addParameter("category", category);
		test.addParameter("subcategory", battery);
	}

	/**
	 * @summary Method responsible for including test class in xml test suite
	 * @param testCases
	 * @throws ClassNotFoundException
	 */
	private void createClassSuite(LinkedList<String> testCases) throws ClassNotFoundException {
		try {
			List<XmlClass> myClasses = new ArrayList<XmlClass>();
			// creating classes
			for (String testClass : testCases) {
				TimeUnit.SECONDS.sleep(1);
				myClasses.add(new XmlClass(testClass));
			}
			// Create a list which can contain the classes that you want to run.
			// Adding class in List

			test.setXmlClasses(myClasses);
			suiteList = new ArrayList<XmlSuite>();
			suiteList.add(suite);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @summary This method will create a xml file based on the xml Suite data
	 * @param mSuite
	 */
	private void createXmlFile(XmlSuite mSuite, String fileName) {
		FileWriter writer;
		try {
			writer = new FileWriter(new File(fileName));
			writer.write(mSuite.toXml());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
