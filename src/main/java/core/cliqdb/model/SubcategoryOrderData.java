package core.cliqdb.model;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class SubcategoryOrderData {

	public static JsonElement getOrderData(String templateName)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		switch (templateName) {
		case "FILES":
			return new Gson().fromJson(new FileReader("conf/file-section-order.json"), JsonElement.class);
		case "API":		
			return new Gson().fromJson(new FileReader("conf/api-section-order.json"), JsonElement.class);


		default:
			return new Gson().fromJson(new FileReader("conf/file-section-order.json"), JsonElement.class);
		}
	}
}
