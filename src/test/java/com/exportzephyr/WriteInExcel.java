package com.exportzephyr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteInExcel 
{		
	//Method to update 'Basic' Sheet in "TestResultFormat.xlsx"
	public static void addRowInExcelSheet(String solution, String environment, String testCaseLastExecutionDate, String testCaseType, String testCaseCategory, String testCaseSubCategory, String testCaseMode, String testCaseBattery, String testCaseExecution, String testCaseName, int testCaseDefectCount, String testCaseResult, String totalRelatedBugs, String openAllBugs, String openBlockedBugs, String testCycleName) throws IOException
	{
		//Create an array with the data in the same order in which you expect to be filled in excel file
		String[] valueToWrite = {solution, environment, testCaseLastExecutionDate, testCaseType, testCaseCategory, testCaseSubCategory, testCaseMode, testCaseBattery, testCaseExecution, testCaseName, String.valueOf(testCaseDefectCount), testCaseResult, totalRelatedBugs, openAllBugs, openBlockedBugs, testCycleName};

        //Create an object of current class
        WriteInExcel objExcelFile = new WriteInExcel();

        //Write the file using file name, sheet name and the data to be filled
//        objExcelFile.writeExcel("C:\\Users\\Public\\", "CreateExcel.filePath", CreateExcel.sheetName, valueToWrite);
        objExcelFile.writeExcel(CreateExcel.filePath, CreateExcel.sheetName, valueToWrite);
	}
	
	//Method which creating rows and columns in Excel sheets for writing new values in them
	public void writeExcel(String fileName, String sheetName, String[] dataToWrite) throws IOException
	{
        //Create an object of File class to open xlsx file
//        File file = new File(filePath + fileName);				//For windows
        File file = new File(fileName);				//For linux

        //Create an object of FileInputStream class to read excel file
        FileInputStream inputStream = new FileInputStream(file);

        Workbook workbook = null;

        //Find the file extension by splitting  file name in substring and getting only extension name
        String fileExtensionName = fileName.substring(fileName.indexOf("."));

        //Check condition if the file is xlsx file
        if(fileExtensionName.equals(".xlsx"))
        {
        	//If it is xlsx file then create object of XSSFWorkbook class
        	workbook = new XSSFWorkbook(inputStream);
        }
        //Check condition if the file is xls file
        else if(fileExtensionName.equals(".xls"))
        {
            //If it is xls file then create object of XSSFWorkbook class
        	workbook = new HSSFWorkbook(inputStream);
        }
        
        //Read excel sheet by sheet name
        Sheet sheet = workbook.getSheet(sheetName);
        
        //Get the current count of rows in excel file
        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
        
        //Get the first row from the sheet
        Row row = sheet.getRow(0);

        //Create a new row and append it at last of sheet
        Row newRow = sheet.createRow(rowCount+1);

        //Create a loop over the cell of newly created Row
        for(int j = 0; j < row.getLastCellNum(); j++)
        {
        	//Fill data in row
        	Cell cell = newRow.createCell(j);
        	cell.setCellValue(dataToWrite[j]);
        }

        //Close input stream
        inputStream.close();

        //Create an object of FileOutputStream class to create write data in excel file
        FileOutputStream outputStream = new FileOutputStream(file);

        //write data in the excel file
        workbook.write(outputStream);

        //close output stream
        outputStream.close();
	}
}