package core.utilities;

import org.apache.log4j.Logger;

/**
 * @summary Responsible for creating table based on inputs provided 
 * @author Manoj.Jain
 */
public class HTMLTableBuilder {
	final static Logger logger = Logger.getLogger(HTMLTableBuilder.class);
	private int columns;
	private final StringBuilder table = new StringBuilder();
	public static String TABLE_START_BORDER = "<table border=\"1\">";
	public static String TABLE_START = "<table>";
	public static String TABLE_END = "</table>";
	public static String HEADER_START = "<th style='text-align:center;background-color: #d3d3d3'>";
	public static String HEADER_END = "</th>";
	public static String ROW_START = "<tr>";
	public static String ROW_END = "</tr>";
	public static String FIRST_COLUMN = "<td>";
	public static String COLUMN_START = "<td style=\"text-align:center\">";
	public static String COLUMN_END = "</td>";
	public static String RED_COLUMN = "<td style='text-align:center;background-color:#f4cccc'>";
	public static String YELLOW_COLUMN = "<td style='text-align:center;background-color:#ffd700'>";
	public static String GREEN_COLUMN = "<td style='text-align:center;background-color:#90ee90'>";

	/**
	 * @summary Constructor for class
	 * @param header
	 * @param border
	 * @param rows
	 * @param columns
	 */
	public HTMLTableBuilder(String header, boolean border, int rows, int columns) {
		this.columns = columns;
		if (header != null) {
			table.append("<b>");
			table.append(header);
			table.append("</b>");
		}
		table.append(border ? TABLE_START_BORDER : TABLE_START);
		table.append(TABLE_END);
	}

	/**
	 * @summary Method for adding header to table 
	 * @param values
	 */
	public void addTableHeader(String... values) {
		if (values.length != columns) {
			logger.info("Error column lenth");
		} else {
			int lastIndex = table.lastIndexOf(TABLE_END);
			if (lastIndex > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append(ROW_START);
				for (String value : values) {
					sb.append(HEADER_START);
					sb.append(value);
					sb.append(HEADER_END);
				}
				sb.append(ROW_END);
				table.insert(lastIndex, sb.toString());
			}
		}
	}

	/**
	 * @summary Method for adding row values
	 * @param values
	 */
	public void addRowValues(String... values) {
		int columnCount = 1;
		if (values.length != columns) {
			logger.info("Error column lenth");
		} else {
			int lastIndex = table.lastIndexOf(ROW_END);
			if (lastIndex > 0) {
				int index = lastIndex + ROW_END.length();
				StringBuilder sb = new StringBuilder();
				sb.append(ROW_START);
				for (String value : values) {
					if (columnCount == 2) {
						sb.append(FIRST_COLUMN);
					} else {
						if (value.contains("COLOR")) {
							String color = value.split("-")[1].trim();
							if (color.equalsIgnoreCase("RED")) {
								sb.append(RED_COLUMN);
							} else if (color.equalsIgnoreCase("YELLOW")) {
								sb.append(YELLOW_COLUMN);
							} else if (color.equalsIgnoreCase("GREEN")) {
								sb.append(GREEN_COLUMN);
							}
							value = value.split(":")[0].trim();
						} else {
							sb.append(COLUMN_START);						
						}
					}
					sb.append(value);
					sb.append(COLUMN_END);
					columnCount++;

				}
				sb.append(ROW_END);
				table.insert(index, sb.toString());
			}
		}
	}

	/**
	 * @summary Forms the table
	 * @return
	 */
	public String build() {
		return table.toString();
	}
}