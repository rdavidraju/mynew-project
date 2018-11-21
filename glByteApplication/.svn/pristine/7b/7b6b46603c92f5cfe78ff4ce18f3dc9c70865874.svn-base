package com.nspl.app.service;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nspl.app.web.rest.ReportsResource;


@Service
public class FileExportService {
	
	 private final Logger log = LoggerFactory.getLogger(ReportsResource.class);
	
	  public void jsonToCSV(List<LinkedHashMap> values,List<String> keysList,PrintWriter writer) throws IOException
	  { 	
		  log.info("keysList :"+keysList);
		/*  String commaSeparated = keysList.stream()
				  .collect(Collectors.joining(","));

		  log.info("commaSeparated :"+commaSeparated);*/

		  try (

				  CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
						  );
				  ) {
			  csvPrinter.printRecord(keysList);

			  for(int j=0;j<values.size();j++)
			  {
				  List<String> valuesList=new ArrayList<String>();
				  for(String hea:keysList)
				  {
					  //log.info("hea: "+hea);
					  if(values.get(j).containsKey(hea)){
						  //  log.info("values.get(j).get(hea): "+values.get(j).get(hea));
						  if(values.get(j).get(hea)!=null)
							  valuesList.add(values.get(j).get(hea).toString());
						  else
							  valuesList.add("");
					  }
					  else{
						  valuesList.add("");
					  }
				  }

				  /*  commaSeparated=valuesList.stream()
						  .collect(Collectors.joining(","));*/

				  csvPrinter.printRecord(valuesList);
			  }
			  csvPrinter.flush(); 
			  csvPrinter.close();
		  }


	  }
	  
	  public void jsonToCSVForReports(List<HashMap> values,List<String> keysList,PrintWriter writer) throws IOException
	  { 	
		  log.info("keysList :"+keysList);
		/*  String commaSeparated = keysList.stream()
				  .collect(Collectors.joining(","));

		  log.info("commaSeparated :"+commaSeparated);*/

		  try (

				  CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
						  );
				  ) {
			  csvPrinter.printRecord(keysList);
			  log.info("values.get(0): "+values.get(0));
			  for(int j=0;j<values.size();j++)
			  {
				  List<String> valuesList=new ArrayList<String>();
				  for(String hea:keysList)
				  {
					  //log.info("hea: "+hea);
					  if(values.get(j).containsKey(hea)){
						  //  log.info("values.get(j).get(hea): "+values.get(j).get(hea));
						  if(values.get(j).get(hea)!=null)
							  valuesList.add(values.get(j).get(hea).toString());
						  else
							  valuesList.add("");
					  }
					  else{
						  valuesList.add("");
					  }
				  }

				  /*  commaSeparated=valuesList.stream()
						  .collect(Collectors.joining(","));*/

				  csvPrinter.printRecord(valuesList);
			  }
			  csvPrinter.flush(); 
			  csvPrinter.close();
		  }


	  }
	  
	  /**
	    * Author: Rk
	    * @param response
	    * @param columnsList
	    * @param dataList
	    * @return
	    */
	   public HttpServletResponse exportToExcel2(HttpServletResponse response, List<String> columnsList, List<HashMap> dataList){
		   response.setHeader("Content-Disposition", "attachment; filename=MyExcel.xlsx");
			Workbook workbook=new XSSFWorkbook();
			try {
				if(dataList.size()>0) {
					workbook.createSheet();
					Sheet sheet=workbook.getSheetAt(0);
					org.apache.poi.ss.usermodel.Row colsRow=sheet.createRow(0);
					log.info("Co names: "+columnsList);
					for(int i=0;i<columnsList.size();i++) {
						Cell newCell=colsRow.createCell(i);
						newCell.setCellValue(columnsList.get(i));
					}
					for(int i=0;i<dataList.size();i++) {
						//System.out.println("dataList.get(i): "+map);
						org.apache.poi.ss.usermodel.Row newRow=sheet.createRow(i+1);
						for(int j=0;j<columnsList.size();j++) {
							//System.out.println("columnsList.get(j): "+columnsList.get(j));
							if(dataList.get(i).containsKey((columnsList.get(j)))) {
								if(dataList.get(i).get(columnsList.get(j))!=null)
								newRow.createCell(j).setCellValue(dataList.get(i).get(columnsList.get(j)).toString());
							}
							else
								newRow.createCell(j).setCellValue(XSSFCell.CELL_TYPE_BLANK);
						}
					}
					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
					ServletOutputStream out = response.getOutputStream();
					workbook.write(out);
					if (workbook instanceof Closeable) {
						((Closeable) workbook).close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return response;
	   }
	   
	   public HttpServletResponse exportToExcel(HttpServletResponse response, List<String> columnsList, List<LinkedHashMap> dataList){
		   response.setHeader("Content-Disposition", "attachment; filename=MyExcel.xlsx");
			Workbook workbook=new XSSFWorkbook();
			try {
				if(dataList.size()>0) {
					workbook.createSheet();
					Sheet sheet=workbook.getSheetAt(0);
					org.apache.poi.ss.usermodel.Row colsRow=sheet.createRow(0);
					log.info("Co names: "+columnsList);
					for(int i=0;i<columnsList.size();i++) {
						Cell newCell=colsRow.createCell(i);
						newCell.setCellValue(columnsList.get(i));
					}
					for(int i=0;i<dataList.size();i++) {
						org.apache.poi.ss.usermodel.Row newRow=sheet.createRow(i+1);
						for(int j=0;j<columnsList.size();j++) {
							if(dataList.get(i).containsKey((columnsList.get(j)))) {
								if(dataList.get(i).get(columnsList.get(j))!=null)
								newRow.createCell(j).setCellValue(dataList.get(i).get(columnsList.get(j)).toString());
							}
							else
								newRow.createCell(j).setCellValue(XSSFCell.CELL_TYPE_BLANK);
						}
					}
					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
					ServletOutputStream out = response.getOutputStream();
					workbook.write(out);
					if (workbook instanceof Closeable) {
						((Closeable) workbook).close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return response;
	   }
	  
}