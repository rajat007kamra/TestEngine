package testNG.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.xml.XmlSuite;

/**
 * @summary This class is responsible for creating mastertestNG.xml out of multiple subcategories
 * @author Manoj.Jain
 */
public class MasterTestNg {
	private static String filename = "masterTestNg.xml";

	private XmlSuite createXMLSuite(String suiteName, List<String> files) {
		List<String> listners = new ArrayList<String>();
		listners.add(TestNGEmailableReport.class.getName());
		XmlSuite suite = new XmlSuite();
		suite.setName(suiteName);
		suite.setSuiteFiles(files);
		return suite;
	}

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

	public void createMasterFile(List<String> files) {
		XmlSuite suite = createXMLSuite("Suite Of Suites", files);
		createXmlFile(suite, filename);
	}
}
