package testNG.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * @summary Class responsible for writing json data in csv
 * @author Surendra.Shekhawat
 */
public class JsonToCsv {
	public void csvWriter(List<Map<String, String>> listOfMap, String filename) throws IOException {
		File file = new File(filename + ".csv");

		Writer writer = new FileWriter(file, true);
		CsvSchema schema = null;
		CsvSchema.Builder schemaBuilder = CsvSchema.builder();
		if (listOfMap != null && !listOfMap.isEmpty()) {
			for (String col : listOfMap.get(0).keySet()) {
				schemaBuilder.addColumn(col);
			}
			schema = schemaBuilder.build().withLineSeparator(System.lineSeparator()).withHeader();
		}
		CsvMapper mapper = new CsvMapper();
		mapper.writer(schema).writeValues(writer).writeAll(listOfMap);
		writer.flush();
	}

	/*
	 * public static void main(String[] args) throws JsonSyntaxException,
	 * JsonIOException, FileNotFoundException, IOException { MergeJsons mergeJsons =
	 * new MergeJsons(); List<Map<String, String>> jsonObject =
	 * mergeJsons.getJsonObject("target"); JsonToCsv jsonToCsv = new JsonToCsv();
	 * jsonToCsv.csvWriter(jsonObject, "feedback"); logger.info("Done"); }
	 */
}