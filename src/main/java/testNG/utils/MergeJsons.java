package testNG.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * @summary This class is responsible for merging multiple json files into a single json file
 * @author Manoj.Jain
 */
public class MergeJsons {
	final static Logger logger = Logger.getLogger(MergeJsons.class);
	private List<File> listFilesForFolder(String folder) throws IOException {
		List<File> jsonFile = new ArrayList<File>();

		File directoryPath = new File(folder);
		File[] list = directoryPath.listFiles();
		logger.info(list);

		for (File file : list) {
			if (file.getName().endsWith("json")) {
				jsonFile.add(file);
			}
		}
		return jsonFile;
	}
	
	private List<Map<String, String>> getJsonObject(List<File> jsonFile) throws JsonSyntaxException, JsonIOException, IOException {
		List<Map<String, String>> jsonObjectList = new ArrayList<Map<String, String>>();
		for (File file : jsonFile) {
			String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			Map<String, String> outcomeReport = new Gson().fromJson(content, Map.class);
			jsonObjectList.add(outcomeReport);
		}
		
		return jsonObjectList;
	}
	
	/**
	 * 
	 * @param folder
	 * @return
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<Map<String, String>> getJsonObject(String folder) throws JsonSyntaxException, JsonIOException, FileNotFoundException, IOException {
		return getJsonObject(listFilesForFolder(folder));
	}
}