package com.nspl.app.service;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExcelReadAndValidationService {

	private final Logger log = LoggerFactory.getLogger(ExcelReadAndValidationService.class);

	public String ExcelCellValidator(XSSFCell cell, XSSFWorkbook workBook)
	{
		FormulaEvaluator formulaEval = workBook.getCreationHelper().createFormulaEvaluator();
		String value = "";
		
		if(cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK)
		{
			value = "0";
		}
		else if(cell.getCellType() == Cell.CELL_TYPE_STRING)
		{
			value = formulaEval.evaluate(cell).formatAsString();
			value = value.replaceAll("\"", "");
		}
		else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
		{
			if(DateUtil.isCellDateFormatted(cell))
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				value = dateFormat.format(cell.getDateCellValue());
			}
			else
			{
				value = Double.toString(cell.getNumericCellValue());
				if(value.contains("."))
				{
					String[] num = value.split("\\.");
					if("0".equals(num[1]))
					{
						value = num[0];
					}
				}
			}
		}

		else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
		{
			value = Boolean.toString(cell.getBooleanCellValue());
		}
		else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA)
		{
			value = formulaEval.evaluate(cell).formatAsString();
			if(value.contains("."))
			{
				String[] num = value.split("\\.");
				if("0".equals(num[1]))
				{
					value = num[0];
				}
			}
		}


		return value;

	}
	
	
	
	@Transactional
	public  boolean isRowEmptyOrWhiteSpaces(Row row) {
		log.info("entered isRowEmptyOrWhiteSpaces");
		boolean flag = false;
		int rowStartIndex=row.getFirstCellNum();
		int rowEndIndex=row.getLastCellNum();
		log.info("rowStartIndex: "+rowStartIndex+"and rowEndIndex: "+rowEndIndex);
		
		for (int i = rowStartIndex; i < rowEndIndex; i++) 
		{
			if (StringUtils.isEmpty(String.valueOf(row.getCell(i))) == true || 
					StringUtils.isWhitespace(String.valueOf(row.getCell(i))) == true || 
					StringUtils.isBlank(String.valueOf(row.getCell(i))) == true || 
					String.valueOf(row.getCell(i)).length() == 0 || 
					row.getCell(i) == null) {} 
			else {
				flag = true;
			}
		}
		log.info("method returns:- "+flag);
		return flag;

	}


	/**
	 * Retrieve whether row is contains blank cells or not
	 */
	@Transactional
	public  boolean isRowEmpty(Row row, int rowNumber) {
		log.info("entered isRowEmpty"+rowNumber);

		log.info("Last Cell Number: "+ row.getLastCellNum());
		for (int c = 0; c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c); 
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && !("".equals(cell.toString())))
				return false;
		}
		return true;
	}

}
