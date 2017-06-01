package com.se.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.se.errorhandling.AppException;
import com.se.structures.Record;

public class ReadWriteExcel {
	
	private String FILES_PATH = "";
	private String RECORDS_FILE_PATH = "";
	private String RECEIPT_FILES_PATH = ""; 

	public ReadWriteExcel() throws AppException {
		PropertyValues prop = new PropertyValues();
		FILES_PATH = prop.getPropertyValues("records");
		RECORDS_FILE_PATH = FILES_PATH + "Records.xlsx";
		RECEIPT_FILES_PATH = prop.getPropertyValues("receipts"); 
	}
	
	public void castCell(int columnIndex, Record record, String value) throws UnsupportedEncodingException {
		switch (columnIndex) {
		case 0: // Month
			// Do not push month
			break;
		case 1: // Province
			record.setProvince(value);
			break;
		case 2: // Document
			record.setDocument(value);
			break;
		case 3: // Date
			// Don't do anything
			/*
			 * formatDate = undefined;
            // Get correct date
            // Format wether it's a time number or a String in format dd/mm/yyyy
            if (typeof cell == 'number') {
              formatDate = new Date(1900, 0, cell-1);
            } else if (typeof cell == 'string') {
              // If '/' is found in the cell use it as separator, otherwise use '-'
              pattern = cell.indexOf('/') > -1 ?
                /(\d{2})\/(\d{2})\/(\d{4})/ :
                /(\d{2})\-(\d{2})\-(\d{4})/;

              formatDate = new Date(cell.replace(pattern, '$2-$1-$3'));
            }

            record.push(formatDate);
			 */
			break;
		case 4: // Destination
			record.setDestination(value);
			break;
		case 5: // Address
			record.setAddress(value);
			break;
		case 6: // District
			record.setDistrict(value);
			break;
		case 7: // Sender
			record.setSender(value);
			break;
		case 8: // Code
			record.setCode(value);
			break;
		case 9: // Reference
			record.setReference(value);
		}
	}
	
	public void writeExcel(InputStream inputStream) throws AppException {
        OutputStream outputStream = null;
        String qualifiedUploadFilePath = RECORDS_FILE_PATH;

        try {
        	// Ensure there's no files with the same name
        	deleteFile();
        	
            outputStream = new FileOutputStream(new File(qualifiedUploadFilePath));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
        } catch (IOException ioe) {
        	throw new AppException(500, 500, "Internal Server Error", ioe.getMessage(), "");
        } catch (Exception e) {
        	throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
        } finally{
            try {
				outputStream.close();
			} catch (IOException e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			}
        }
	}
	
	public ArrayList<Record> readExcel() throws AppException {
		ArrayList<Record> records = new ArrayList<Record>();
		Record record = new Record();
		int columnIndex = 0;
		
		// Creation Code
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String creationCode = format.format(date);
		XSSFWorkbook myWorkBook = null;
		
		try {
			File myFile = new File(RECORDS_FILE_PATH); 
			FileInputStream fis = new FileInputStream(myFile); 
			
			myWorkBook = new XSSFWorkbook(fis);
			XSSFSheet mySheet = myWorkBook.getSheetAt(0);
			Iterator<Row> rowIterator = mySheet.iterator();
			
			// Ignore first row since it has the headers
			rowIterator.next();
			
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				columnIndex = 0;
				
				record = new Record();
				record.setCreationCode(creationCode);
				
				while (cellIterator.hasNext()) { 
					if (columnIndex > 9) {
						break;
					}
					Cell cell = cellIterator.next(); 
					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)) {
						record.setDate(cell.getDateCellValue());
						record.setTime(record.getDate().getTime());
					} else {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						castCell(columnIndex, record, cell.getStringCellValue());
					}
		            
					columnIndex++;
				}
				
				if ( record.getCode() != null && !record.getCode().isEmpty()) {
					records.add(record);
				}
			}
			// Delete temp file at the end
			myFile.delete();
		} catch (IOException ioe) {
        	throw new AppException(500, 500, "Internal Server Error", ioe.getMessage(), "");
        } catch (Exception e) {
        	throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
        } finally{
            try {
            	myWorkBook.close();
			} catch (IOException e) {
				throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
			}
        }
		
		return records;
	}
	
	private void deleteFile() throws AppException {
		try {
			File myFile = new File(RECORDS_FILE_PATH); 
			myFile.delete();
        } catch (Exception e) {
        	throw new AppException(500, 500, "Internal Server Error", e.getMessage(), "");
        }
	}
	
	public File getFile(String code) throws AppException {
		File result = new File(RECEIPT_FILES_PATH + code);
		
		return result;
	}
	
	public List<String> getFilesName(String code) throws AppException {
		List<String> result = new ArrayList<String>(); 
		File file = new File(RECEIPT_FILES_PATH);
	    if (file.isDirectory()) {
	        for (String child : file.list()) {
	            if (child.contains(code)) {
	            	result.add(child);
	            }
	        }
	    }
		
		return result;
	}
	
}
