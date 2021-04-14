package com.exportzephyr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

public class CreateExcel {
	final static Logger logger = Logger.getLogger(CreateExcel.class);
//	public static String filePath = "C:\\Users\\Public\\p20-legalentitymanagement-20210326.xlsx";
	public static String filePath = "p20-legalentitymanagement-20210326.xlsx";
	public static String sheetName = "EntityMaster";
	public static XSSFWorkbook workbook = null;

	@Test
	public static void createWorkbook() throws IOException, InterruptedException {
		workbook = new XSSFWorkbook();
		createSheet();
	}

	// Method to create sheet in "TestResultFormat.xlsx" workbook with name =
	// 'Basic'
	public static void createSheet() throws FileNotFoundException, IOException {
		XSSFSheet sheet = workbook.createSheet(sheetName);

		CellStyle style = workbook.createCellStyle(); // Create style
		Font font = workbook.createFont(); // Create font
		font.setBold(true); // Make font bold
		style.setFont(font); // set it to bold

		Object[][] bookData = { { "SOLUTION", "ENVIRONMENT", "RELEASE DATE", "TYPE", "CATEGORY", "SUBCATEGORY", "MODE",
			"BATTERY", "EXECUTION", "TEST CASE", "#DEFECTS", "RESULT", "TOTAL BUGS", "OPEN[ALL]", "OPEN[BLOCKERS]", "TEST CYCLE" } };

		int rowCount = 0;

		for (Object[] aBook : bookData) {
			Row row = sheet.createRow(rowCount);

			int columnCount = -1;

			for (Object field : aBook) {
				Cell cell = row.createCell(++columnCount);
				if (field instanceof String) {
					cell.setCellValue((String) field);
					cell.setCellStyle(style);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
					cell.setCellStyle(style);
				} else if (field instanceof Date) {
					cell.setCellValue((Date) field);
					cell.setCellStyle(style);
				}
			}
		}
		//For windows
//		try (FileOutputStream outputStream = new FileOutputStream(FilePath)) {
//			workbook.write(outputStream);
//		}
		//For linux
		File fileName = new File(filePath);
		try (FileOutputStream outputStream = new FileOutputStream(fileName.getAbsolutePath())) {
			workbook.write(outputStream);
		}
	}

	// Method to create Log File
	public static void createNewResultFile() throws IOException, InterruptedException {
		File excelFile = new File(filePath);
		// if file doesn't exists, then create it
		if (!excelFile.exists()) {
			excelFile.createNewFile();
		} else if (excelFile.exists()) {
//				excelFile.delete();
			excelFile.createNewFile();
		} else {
			logger.info("System Unable to Create Log File");
		}
	}
}