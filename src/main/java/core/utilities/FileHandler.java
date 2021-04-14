package core.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Properties;

/**
 * @summary For reading/writing any file
 * @author Surendra.Shekhawat
 */
public class FileHandler {
	/**
	 * @summary Read a property from a file at certain path 
	 * @param filePath
	 * @param propertyName
	 * @return
	 */
	public static String getProperty(String filePath, String propertyName) {
		Properties properties = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(new File(filePath));
			properties.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties.getProperty(propertyName);
	}

	/**
	 * @summary Modify a property from a file at certain path
	 * @param filePath
	 * @param oldString
	 * @param newString
	 */
	public static void modifyFile(String filePath, String oldString, String newString) {
		File fileToBeModified = new File(filePath);

		String oldContent = "";

		BufferedReader reader = null;

		FileWriter writer = null;

		try {
			reader = new BufferedReader(new FileReader(fileToBeModified));

			// Reading all the lines of input text file into oldContent

			String line = reader.readLine();

			while (line != null) {
				oldContent = oldContent + line + System.lineSeparator();

				line = reader.readLine();
			}

			// Replacing oldString with newString in the oldContent

			String newContent = oldContent.replaceAll(oldString, newString);

			// Rewriting the input text file with newContent

			writer = new FileWriter(fileToBeModified);

			writer.write(newContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// Closing the resources

				reader.close();

				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @summary Modify multiple properties from a file at certain path
	 * @param filePath
	 * @param modifiedTexts
	 */
	public static void modifyFileByHashMap(String filePath, LinkedHashMap<String, String> modifiedTexts) {
		File fileToBeModified = new File(filePath);

		String oldContent = "";

		BufferedReader reader = null;

		FileWriter writer = null;

		try {
			reader = new BufferedReader(new FileReader(fileToBeModified));

			// Reading all the lines of input text file into oldContent

			String line = reader.readLine();

			while (line != null) {
				oldContent = oldContent + line + System.lineSeparator();

				line = reader.readLine();
			}

			// Replacing oldString with newString in the oldContent
			String newContent = oldContent;
			for (String key : modifiedTexts.keySet()) {
				newContent = newContent.replace(key, modifiedTexts.get(key));
			}
			// Rewriting the input text file with newContent

			writer = new FileWriter(fileToBeModified);

			writer.write(newContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// Closing the resources

				reader.close();

				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

//    public static String appendTextToRow(String filePath, String )
//    {
//
//    }
}
