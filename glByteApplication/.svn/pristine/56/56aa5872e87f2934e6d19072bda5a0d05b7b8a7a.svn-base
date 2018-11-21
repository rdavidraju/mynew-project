package com.nspl.app.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.LocalDate;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FxRates;
import com.nspl.app.domain.JeLdrDetails;
import com.nspl.app.domain.JeLines;
import com.nspl.app.domain.JournalsHeaderData;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.TemplateDetails;
import com.nspl.app.domain.TenantConfig;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.FxRatesRepository;
import com.nspl.app.repository.JeLdrDetailsRepository;
import com.nspl.app.repository.JeLinesRepository;
import com.nspl.app.repository.JournalsHeaderDataRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.TemplateDetailsRepository;
import com.nspl.app.repository.TenantConfigRepository;
import com.nspl.app.web.rest.JournalsHeaderDataResource;


@Service
public class JeWQService {
	
	
	 private final Logger log = LoggerFactory.getLogger(JournalsHeaderDataResource.class);
	
	
	 @Inject
	    TemplateDetailsRepository templateDetailsRepository;
	    
	    @Inject
	    LookUpCodeRepository lookUpCodeRepository;
	    
	    @Inject
	    JeLinesRepository jeLinesRepository;
	    
	    @Inject
	    JeLdrDetailsRepository jeLdrDetailsRepository;
	    
	    @Inject
	    DataViewsColumnsRepository dataViewsColumnsRepository;
	    
	    
	    @Inject
	    FileService fileService;
	    
	    @Inject
	    JournalsHeaderDataRepository journalsHeaderDataRepository;
	    
	    @Inject
	    ReconciliationResultService reconciliationResultService;
	    
	    @Inject
	    PropertiesUtilService propertiesUtilService;
	    
	    
	    @Inject
	    TenantConfigRepository tenantConfigRepository;
	    
	    @Inject
	    FxRatesRepository fxRatesRepository;
	    
	    @PersistenceContext(unitName="default")
		private EntityManager em;
	    
	    @Autowired
	   	Environment env;
	    
	    
	    
	    /*** not using it**/
	   
	   public HashMap jsonToCsvToExcel(Long tenantId,String batchName,LinkedHashMap map,String arrName,String path)
	   {
		   HashMap status=new HashMap();
	    JSONObject output;
	 //   ObjectMapper mapperObj = new ObjectMapper();
    	try {
    		//String finalJson=mapperObj.writeValueAsString(map);
    	//	log.info("finalJson :"+finalJson);
    		output = new JSONObject(map.toString());
    		JSONArray docs = output.getJSONArray(arrName);
    	//	output=new JSONObject(finalJson);

    	

    		File file=new File(path+batchName.replaceAll("[^a-zA-Z0-9]","").replaceAll("[^a-zA-Z0-9]","")+tenantId+LocalDate.now().toString().replaceAll("\\s+","").replaceAll("[^a-zA-Z0-9]","")+"HeaderLevel.csv");
    		String csv = CDL.toString(docs);
    		FileUtils.writeStringToFile(file, csv);
    	} catch (JSONException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}      


    	try {
    		String csvFileAddress = path+batchName.replaceAll("[^a-zA-Z0-9]","")+tenantId+LocalDate.now().toString().replaceAll("\\s+","").replaceAll("[^a-zA-Z0-9]","")+"HeaderLevel.csv"; //csv file address
    		String xlsxFileAddress = path+batchName.replaceAll("[^a-zA-Z0-9]","")+tenantId+LocalDate.now().toString().replaceAll("\\s+","").replaceAll("[^a-zA-Z0-9]","")+"HeaderLevel.xlsx"; //xlsx file address
    		String fileName=batchName.replaceAll("[^a-zA-Z0-9]","")+tenantId+LocalDate.now().toString().replaceAll("\\s+","").replaceAll("[^a-zA-Z0-9]","")+"HeaderLevel.xlsx";
    		log.info("fileName :"+fileName);
    		XSSFWorkbook workBook = new XSSFWorkbook();
    		XSSFSheet sheet1 = workBook.createSheet("sheet1");
    		String currentLine=null;
    		int RowNum=0;


    		BufferedReader br = new BufferedReader(new FileReader(csvFileAddress));
    		while ((currentLine = br.readLine()) != null) {
    			String str[] = currentLine.split(",");
    			XSSFRow currentRow=sheet1.createRow(RowNum);
    			for(int i=0;i<str.length;i++){
    				currentRow.createCell(i).setCellValue(str[i]);
    				
    			}
    			RowNum++;
    		}

    		FileOutputStream fileOutputStream =  new FileOutputStream(xlsxFileAddress);
    		workBook.write(fileOutputStream);
    		fileOutputStream.close();
    		
    		File file = new File(xlsxFileAddress);
        	InputStream inputStream=new FileInputStream(file);
        	String[] destPath=fileService.fileUpload(inputStream, fileName);
        	
        	if(destPath[0].equalsIgnoreCase("success"))
        	{
        		status.put("status", destPath[0]);
        		String finalFSPath="";
        		TenantConfig fileServerUrl=tenantConfigRepository.findByTenantIdAndKey(tenantId, "File Server Path");
        		
        		if(fileServerUrl!=null)
        			finalFSPath=fileServerUrl.getValue()+destPath[1];
        		else
        		{
        			 fileServerUrl=tenantConfigRepository.findByTenantIdAndKey(0l, "File Server Path");
        			 finalFSPath=fileServerUrl.getValue()+destPath[1];
        		}
        		status.put("destPath", finalFSPath);
        	}
        	else
        	{
        		status.put("status", "failure");
        	}
    		
    		
    		System.out.println("Done");
    	} catch (Exception ex) {
    		System.out.println(ex.getMessage()+"Exception in try");
    	}
		return status;

	    
	   }
	    
	public LinkedHashMap journalHeaderAndLineDetails(Long batchId,String desc) throws SQLException
	{
		LinkedHashMap finalMap=new LinkedHashMap();
		JournalsHeaderData jHd=journalsHeaderDataRepository.findOne(batchId);
		TemplateDetails tempDet=templateDetailsRepository.findOne(jHd.getJeTempId());
		/*finalMap.put("batchName", jHd.getJeBatchName());
    	finalMap.put("glDate", jHd.getGlDate());
    	finalMap.put("source", jHd.getSource());
    	finalMap.put("conversionType", jHd.getConversionType());
    	finalMap.put("ledger",jHd.getLedgerName());
    	finalMap.put("period", jHd.getPeriod());
    	finalMap.put("category", jHd.getCategory());
    	finalMap.put("tenantId", jHd.getTenantId());*/
		//	Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
		//String currencyFormat = props.getProperty("currencyFormat");
		
		
		List<LinkedHashMap> attributeNames=new ArrayList<LinkedHashMap>();
		List<String> attributes=new ArrayList<String>();
		List<JeLdrDetails> jeLdrDetailsColumns=jeLdrDetailsRepository.findByJeTempId(jHd.getJeTempId());
		for(JeLdrDetails jeLdrDetails:jeLdrDetailsColumns)
		{
			if(jeLdrDetails.getColType().equalsIgnoreCase("System")){
				LookUpCode lookUpCode=lookUpCodeRepository.findOne(jeLdrDetails.getColValue());
				attributes.add(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""));

			}
			else if(jeLdrDetails.getColType().equalsIgnoreCase("Line"))
			{
				log.info("in else");
				DataViewsColumns dvc=dataViewsColumnsRepository.findOne(jeLdrDetails.getColValue());
				attributes.add(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""));

			}
		}

		log.info("attributes :"+attributes);

		List<LinkedHashMap> jelineDetailsList=new ArrayList<LinkedHashMap>();
		if(desc!=null && desc.equalsIgnoreCase("Details"))
		{
			log.info("if api to fetch Details of records");
			List<JeLines> jeLinesList=jeLinesRepository.findByJeHeaderId(batchId);
			int Count =0;
			log.info("jeLinesList :"+jeLinesList.size());

			for(JeLines jeLines:jeLinesList)
			{
				LinkedHashMap jelineDetails=new LinkedHashMap();

				Count =Count+1;
				jelineDetails.put("Line", Count);
				jelineDetails.put("Template Name", tempDet.getTemplateName());
				jelineDetails.put("Batch Name", jHd.getJeBatchName());
				jelineDetails.put("Gl Date", jHd.getGlDate());
				jelineDetails.put("Source", jHd.getSource());
				if(jHd.getConversionType()!=null && !jHd.getConversionType().isEmpty())
				{
					FxRates fxRates=fxRatesRepository.findOne(Long.valueOf(jHd.getConversionType()));
					if(fxRates!=null && fxRates.getConversionType()!=null)
						jelineDetails.put("Conversion Type", fxRates.getConversionType());
				}
				else
					jelineDetails.put("Conversion Type", "");
				jelineDetails.put("Ledger",jHd.getLedgerName());
				jelineDetails.put("Period", jHd.getPeriod());
				jelineDetails.put("Category", jHd.getCategory());
				//jelineDetails.put("lineNo", Count);
				jelineDetails.put("Code Combination", jeLines.getCodeCombination());
				if(jeLines.getAccountedDebit()!=null)
				{
					//String actDeb = reconciliationResultService.getAmountInFormat(jeLines.getAccountedDebit().toString(),currencyFormat);

					jelineDetails.put("Accounted Debit", jeLines.getAccountedDebit());
				}
				else
					jelineDetails.put("Accounted Debit", 0);
				if(jeLines.getAccountedCredit()!=null)
				{
					//String actCred = reconciliationResultService.getAmountInFormat(jeLines.getAccountedCredit().toString(),currencyFormat);
					jelineDetails.put("Accounted Credit", jeLines.getAccountedCredit());
				}
				else
					jelineDetails.put("Accounted Credit", 0);
				
				
				if(jeLines.getDebitAmount()!=null)
				{
					//String deb = reconciliationResultService.getAmountInFormat(jeLines.getDebitAmount().toString(),currencyFormat);
					jelineDetails.put("Entered Debit", jeLines.getDebitAmount().toString());
				}
				else
					jelineDetails.put("Entered Debit", 0);
				//if(attributes.contains("enteredCredit"))
				if(jeLines.getCreditAmount()!=null)
				{
					//	String cred = reconciliationResultService.getAmountInFormat(jeLines.getCreditAmount().toString(),currencyFormat);
					jelineDetails.put("Entered Credit", jeLines.getCreditAmount().toString());
				}
				else
					jelineDetails.put("Entered Credit", 0);

				if(jeLines.getAttrRef1Type()!=null)
				{
					if(jeLines.getAttrRef1Type().split("_")[0].equalsIgnoreCase("LookUp")){
						LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLines.getAttrRef1Type().split("_")[1]));
						jelineDetails.put(lookUpCode.getMeaning(), jeLines.getAttributeRef1());
					}
					else if(jeLines.getAttrRef1Type().split("_")[0].equalsIgnoreCase("column"))
					{
						DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLines.getAttrRef1Type().split("_")[1]));
						jelineDetails.put(dvc.getColumnName(), jeLines.getAttributeRef1());

					}
				}
				if(jeLines.getAttrRef2Type()!=null)
				{
					if(jeLines.getAttrRef2Type().split("_")[0].equalsIgnoreCase("LookUp")){
						LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLines.getAttrRef2Type().split("_")[1]));
						jelineDetails.put(lookUpCode.getMeaning(), jeLines.getAttributeRef2());
					}
					else if(jeLines.getAttrRef2Type().split("_")[0].equalsIgnoreCase("column"))
					{
						DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLines.getAttrRef2Type().split("_")[1]));
						jelineDetails.put(dvc.getColumnName(), jeLines.getAttributeRef2());

					}
				}
				if(jeLines.getAttrRef3Type()!=null)
				{
					if(jeLines.getAttrRef3Type().split("_")[0].equalsIgnoreCase("LookUp")){
						LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLines.getAttrRef3Type().split("_")[1]));
						jelineDetails.put(lookUpCode.getMeaning(), jeLines.getAttributeRef3());
					}
					else if(jeLines.getAttrRef3Type().split("_")[0].equalsIgnoreCase("column"))
					{
						DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLines.getAttrRef3Type().split("_")[1]));
						jelineDetails.put(dvc.getColumnName(), jeLines.getAttributeRef3());

					}
				}
				if(jeLines.getAttrRef4Type()!=null)
				{
					if(jeLines.getAttrRef4Type().split("_")[0].equalsIgnoreCase("LookUp")){
						LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLines.getAttrRef4Type().split("_")[1]));
						jelineDetails.put(lookUpCode.getMeaning(), jeLines.getAttributeRef4());
					}
					else if(jeLines.getAttrRef4Type().split("_")[0].equalsIgnoreCase("column"))
					{
						DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLines.getAttrRef4Type().split("_")[1]));
						jelineDetails.put(dvc.getColumnName(), jeLines.getAttributeRef4());

					}
				}
				if(jeLines.getAttrRef5Type()!=null)
				{
					if(jeLines.getAttrRef5Type().split("_")[0].equalsIgnoreCase("LookUp")){
						LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLines.getAttrRef5Type().split("_")[1]));
						jelineDetails.put(lookUpCode.getMeaning(), jeLines.getAttributeRef5());
					}
					else if(jeLines.getAttrRef5Type().split("_")[0].equalsIgnoreCase("column"))
					{
						DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLines.getAttrRef5Type().split("_")[1]));
						jelineDetails.put(dvc.getColumnName(), jeLines.getAttributeRef5());

					}
				}
				if(jeLines.getAttrRef6Type()!=null)
				{

					if(jeLines.getAttrRef6Type().split("_")[0].equalsIgnoreCase("LookUp")){
						LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLines.getAttrRef6Type().split("_")[1]));
						jelineDetails.put(lookUpCode.getMeaning(), jeLines.getAttributeRef6());
					}
					else if(jeLines.getAttrRef6Type().split("_")[0].equalsIgnoreCase("column"))
					{
						DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLines.getAttrRef6Type().split("_")[1]));
						jelineDetails.put(dvc.getColumnName(), jeLines.getAttributeRef6());

					}
				}
				//if(attributes.contains("enteredDebit"))
				
				//}
				jelineDetailsList.add(jelineDetails);


			}
		}
		else
		{


			Connection conn = null;
			Statement stmt = null;
			Statement stmtCt = null;
			ResultSet result2=null;
			ResultSet resultCount=null;
			//conn = DriverManager.getConnection(dbUrl, userName, password);
			try
			{
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				stmt = conn.createStatement();
				stmtCt = conn.createStatement();

				log.info("if api to fetch Summary of records");
				List<JeLdrDetails> jeLdrDetailsList=jeLdrDetailsRepository.findByJeTempIdAndRefColumnNotNull(jHd.getJeTempId());
				int Count=0;

				String subQry1="select code_combination,round(sum(accounted_debit),2) as actDeb,round(sum(accounted_credit),2) as actCred,round(sum(debit_amount),2) as deb,round(sum(credit_amount),2) as cred,";
				String subQry2=" from t_je_lines  where je_header_id="+batchId+" group by code_combination,";
				String subQry3="attribute_ref_1,attr_ref_1_type,attribute_ref_2,attr_ref_2_type,attribute_ref_3,attr_ref_3_type,attribute_ref_4,attr_ref_4_type,attribute_ref_5,attr_ref_5_type,attribute_ref_6,attr_ref_6_type";
				/*	HashMap attrbuteMap=new HashMap();
				for(JeLdrDetails jeLdrDetails:jeLdrDetailsList)
				{
					subQry3=subQry3+"attribute_Ref_"+jeLdrDetails.getRefColumn()+",";

					if(jeLdrDetails.getColType().equalsIgnoreCase("System")){
						LookUpCode lookUpCode=lookUpCodeRepository.findOne(jeLdrDetails.getColValue());


						if(jeLdrDetails.getRefColumn()==1)
							attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),lookUpCode.getMeaning());
						if(jeLdrDetails.getRefColumn()==2)
							attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),lookUpCode.getMeaning());
						if(jeLdrDetails.getRefColumn()==3)
							attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),lookUpCode.getMeaning());
						if(jeLdrDetails.getRefColumn()==4)
							attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),lookUpCode.getMeaning());
						if(jeLdrDetails.getRefColumn()==5)
							attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),lookUpCode.getMeaning());
						if(jeLdrDetails.getRefColumn()==6)
							attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),lookUpCode.getMeaning());

					}
					else if(jeLdrDetails.getColType().equalsIgnoreCase("Line"))
					{
						DataViewsColumns dvc=dataViewsColumnsRepository.findOne(jeLdrDetails.getColValue());

						if(dvc.getColumnName().equalsIgnoreCase("currency"))
    				{
    					subQry3=subQry3+"currency,";
    				}
						if(jeLdrDetails.getRefColumn()==1)
							attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),dvc.getColumnName());
						if(jeLdrDetails.getRefColumn()==2)
							attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),dvc.getColumnName());
						if(jeLdrDetails.getRefColumn()==3)
							attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),dvc.getColumnName());
						if(jeLdrDetails.getRefColumn()==4)
							attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),dvc.getColumnName());
						if(jeLdrDetails.getRefColumn()==5)
							attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),dvc.getColumnName());
						if(jeLdrDetails.getRefColumn()==6)
							attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),dvc.getColumnName());

					}

				}*/

				log.info("subQry3 :"+subQry3);

				String finalQuery=subQry1+subQry3+subQry2+subQry3;
				/*if(subQry3!=null && !subQry3.isEmpty())
				{
					String subque=subQry3.substring(0, subQry3.length() - 1);
					log.info("subQry3 :"+subque);
					finalQuery=subQry1+subque+subQry2+subque;
					log.info("QUERY:"+subQry1+subque+subQry2+subque);
				}
				else
				{

					String subQry4="select code_combination,round(sum(accounted_debit),2) as actDeb,round(sum(accounted_credit),2) as actCred,round(sum(debit_amount),2) as deb,round(sum(credit_amount),2) as cred";
					String subQry5=" from t_je_lines  where je_header_id="+batchId+" group by code_combination";
					finalQuery=subQry4+subQry5;
					log.info("QUERY:"+finalQuery);
				}*/

				result2=stmt.executeQuery(finalQuery);
				log.info("****query executed*****");
				ResultSetMetaData rsmd2 = result2.getMetaData();
				int columnCount = rsmd2.getColumnCount();


				/*resultCount=stmtCt.executeQuery(finalQuery);
    		int totalCount=0;
    		while(resultCount.next())
    		{
    			totalCount =totalCount+1;
    		}*/

				/*   
    		    for (int i = 1; i <= columnCount; i++) {
    		        if(rsmd2.getColumnName(i).equalsIgnoreCase("currency")) {
    		        	currency=true;

    		        }*/




				while(result2.next())
				{
					LinkedHashMap map=new LinkedHashMap();
					Count =Count+1;
					map.put("Line", Count);
					map.put("Template Name", tempDet.getTemplateName());
					map.put("Batch Name", jHd.getJeBatchName());
					map.put("Gl Date", jHd.getGlDate());
					map.put("Source", jHd.getSource());
					if(jHd.getConversionType()!=null && !jHd.getConversionType().isEmpty())
					{
						FxRates fxRates=fxRatesRepository.findOne(Long.valueOf(jHd.getConversionType()));
						if(fxRates!=null && fxRates.getConversionType()!=null)
							map.put("Conversion Type", fxRates.getConversionType());
					}
					else
						map.put("Conversion Type", "");
					map.put("Ledger",jHd.getLedgerName());
					map.put("Period", jHd.getPeriod());
					map.put("Category", jHd.getCategory());
					//jelineDetails.put("lineNo", Count);

					for (int i = 1; i <= columnCount; i++ ) {
						String name = rsmd2.getColumnName(i); 

						//log.info("result2.getString(code_combination) :"+result2.getString("code_combination"));
						//log.info("result2.getString(currency) :"+result2.getString("currency"));
						//map.put("tempId", jHd.getJeTempId());
						map.put("Code Combination", result2.getString("code_combination"));

						/*if(name.equalsIgnoreCase("currency"))
    					map.put("currency", result2.getString("currency"));*/

						if(result2.getString("actDeb")!=null)
						{
							//String actDeb = reconciliationResultService.getAmountInFormat(result2.getString("actDeb"),currencyFormat);
							map.put("Accounted Debit", Double.valueOf(result2.getString("actDeb")));
						}
						else
							map.put("Accounted Debit", 0d);
						if(result2.getString("actCred")!=null)
						{
							//String actCred = reconciliationResultService.getAmountInFormat(result2.getString("actCred"),currencyFormat);
							map.put("Accounted Credit", Double.valueOf(result2.getString("actCred")));
						}
						else
							map.put("Accounted Credit", 0d);
						
						
						
						if(result2.getString("deb")!=null)
						{
							//String deb = reconciliationResultService.getAmountInFormat(result2.getString("deb"),currencyFormat);
							map.put("Entered Debit", Double.valueOf(result2.getString("deb")));
						}
						else
							map.put("Entered Debit", 0d);
						//}
						//if(attributes.contains("enteredCredit"))
						//{
						if(result2.getString("cred")!=null)
						{
							//String cred = reconciliationResultService.getAmountInFormat(result2.getString("cred"),currencyFormat);
							map.put("Entered Credit",Double.valueOf(result2.getString("cred")));
						}
						else
							map.put("Entered Credit",0d);

						if(result2.getString("attr_ref_1_type")!=null)
						{
							String column=result2.getString("attr_ref_1_type");
							if(column.split("_")[0].equalsIgnoreCase("LookUp")){
								LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
								map.put(lookUpCode.getMeaning(), result2.getString("attribute_Ref_1"));
							}
							else if(column.split("_")[0].equalsIgnoreCase("column"))
							{
								DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
								map.put(dvc.getColumnName(), result2.getString("attribute_Ref_1"));

							}
						}
						if(result2.getString("attr_ref_2_type")!=null)
						{
							String column=result2.getString("attr_ref_2_type");
							if(column.split("_")[0].equalsIgnoreCase("LookUp")){
								LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
								map.put(lookUpCode.getMeaning(), result2.getString("attribute_Ref_2"));
							}
							else if(column.split("_")[0].equalsIgnoreCase("column"))
							{
								DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
								map.put(dvc.getColumnName(), result2.getString("attribute_Ref_2"));

							}
						}
						if(result2.getString("attr_ref_3_type")!=null)
						{
							String column=result2.getString("attr_ref_3_type");
							if(column.split("_")[0].equalsIgnoreCase("LookUp")){
								LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
								map.put(lookUpCode.getMeaning(), result2.getString("attribute_Ref_3"));
							}
							else if(column.split("_")[0].equalsIgnoreCase("column"))
							{
								DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
								map.put(dvc.getColumnName(), result2.getString("attribute_Ref_3"));

							}
						}
						if(result2.getString("attr_ref_4_type")!=null)
						{
							String column=result2.getString("attr_ref_4_type");
							if(column.split("_")[0].equalsIgnoreCase("LookUp")){
								LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
								map.put(lookUpCode.getMeaning(), result2.getString("attribute_Ref_4"));
							}
							else if(column.split("_")[0].equalsIgnoreCase("column"))
							{
								DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
								map.put(dvc.getColumnName(), result2.getString("attribute_Ref_4"));

							}
						}
						if(result2.getString("attr_ref_5_type")!=null)
						{
							String column=result2.getString("attr_ref_5_type");
							if(column.split("_")[0].equalsIgnoreCase("LookUp")){
								LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
								map.put(lookUpCode.getMeaning(), result2.getString("attribute_Ref_5"));
							}
							else if(column.split("_")[0].equalsIgnoreCase("column"))
							{
								DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
								map.put(dvc.getColumnName(), result2.getString("attribute_Ref_5"));

							}
						}
						if(result2.getString("attr_ref_6_type")!=null)
						{
							String column=result2.getString("attr_ref_6_type");
							if(column.split("_")[0].equalsIgnoreCase("LookUp")){
								LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
								map.put(lookUpCode.getMeaning(), result2.getString("attribute_Ref_6"));
							}
							else if(column.split("_")[0].equalsIgnoreCase("column"))
							{
								DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
								map.put(dvc.getColumnName(), result2.getString("attribute_Ref_6"));

							}
						}

						//if(attributes.contains("enteredDebit"))
						//{
					
						//}

					}
					jelineDetailsList.add(map);
				}
			}
			catch(Exception e)
			{
				log.info("exception while getting journalHeaderDetailOrSummaryInfo in excel");
				e.printStackTrace();
			}

			finally
			{
				if(result2!=null)
					result2.close();
				if(resultCount!=null)
					resultCount.close();
				if(stmt!=null)
					stmt.close();
				if(stmtCt!=null)
					stmtCt.close();
				if(conn!=null)
					conn.close();
			}



		}
		//log.info("jelineDetailsList :"+jelineDetailsList);
		finalMap.put("jelineDetailsList", jelineDetailsList);
		return finalMap;


	}
	
	
	
	
	
 	public LinkedHashMap journalHeaderDetailsForExcel( HashMap journalHeaderDetails, Long tenantId)
 	{
 		log.info("request rest to fetch ledgerNames and periods");

 		Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
 		String currencyFormat = props.getProperty("currencyFormat");
 		List<JournalsHeaderData> jHDList=new ArrayList<JournalsHeaderData>();
 		LinkedHashMap finalLhmap=new LinkedHashMap();
 		List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
    	if(journalHeaderDetails.get("jeTempId")!=null && !journalHeaderDetails.get("jeTempId").toString().equalsIgnoreCase("0") )
    	{
    		TemplateDetails tempId=templateDetailsRepository.findByTenantIdAndIdForDisplay(tenantId, journalHeaderDetails.get("jeTempId").toString());
    		journalHeaderDetails.put("jeTempId", tempId.getId());
    	}
 		if((journalHeaderDetails.get("jeTempId")!=null && Long.valueOf(journalHeaderDetails.get("jeTempId").toString())!= 0) && (journalHeaderDetails.get("jeLedger")!=null && journalHeaderDetails.get("jeLedger")!="" && !journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All"))&& 
 				(journalHeaderDetails.get("jePeriod")!="" && journalHeaderDetails.get("jePeriod")!=null && !journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))

 		{

 			if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
 			{
 				log.info("1.if 5 paremeters are given");

 				ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
 				ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());

 				jHDList=journalsHeaderDataRepository.findByjeTempIdAndLedgerNameAndPeriodAndTenantIdAndJeBatchDateBetweenOrderByIdDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
 						journalHeaderDetails.get("jeLedger").toString(),journalHeaderDetails.get("jePeriod").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
 			}
 			else
 			{
 				log.info("2.if 3 parameters are given "+journalHeaderDetails);
 				jHDList=journalsHeaderDataRepository.findByjeTempIdAndLedgerNameAndPeriodAndTenantIdOrderByIdDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
 						journalHeaderDetails.get("jeLedger").toString(),journalHeaderDetails.get("jePeriod").toString(),tenantId);
 			}
 		}
 		else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())== 0) && (journalHeaderDetails.get("jeLedger")!="" && journalHeaderDetails.get("jeLedger")!=null && !journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
 				(journalHeaderDetails.get("jePeriod")!=null && journalHeaderDetails.get("jePeriod")!="" && !journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
 		{

 			List<BigInteger> tempIds=templateDetailsRepository.fetchByTenantIdAndActive(tenantId);
 			log.info("tempIds :"+tempIds);
 			if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
 			{
 				log.info("3.journalHeaderDetails :"+journalHeaderDetails);
 				ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
 				ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


 				jHDList=journalsHeaderDataRepository.findByjeTempIdInAndLedgerNameAndPeriodAndTenantIdAndJeBatchDateBetween(tempIds,
 						journalHeaderDetails.get("jeLedger").toString(),journalHeaderDetails.get("jePeriod").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
 			}	
 			else
 			{
 				log.info("3.journalHeaderDetails :"+journalHeaderDetails);

 				jHDList=journalsHeaderDataRepository.findByjeTempIdInAndLedgerNameAndPeriodAndTenantId(tempIds,
 						journalHeaderDetails.get("jeLedger").toString(),journalHeaderDetails.get("jePeriod").toString(),tenantId);
 			}

 		}
 		else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())!= 0) && (journalHeaderDetails.get("jeLedger")=="" || journalHeaderDetails.get("jeLedger")==null || journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
 				(journalHeaderDetails.get("jePeriod")!=null && journalHeaderDetails.get("jePeriod")!="" && !journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
 		{


 			if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
 			{
 				log.info("4.journalHeaderDetails :"+journalHeaderDetails);
 				ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
 				ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


 				jHDList=journalsHeaderDataRepository.findByjeTempIdAndPeriodAndTenantIdAndJeBatchDateBetweenOrderByIdDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
 						journalHeaderDetails.get("jePeriod").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
 			}	
 			else
 			{
 				log.info("4.journalHeaderDetails :"+journalHeaderDetails);

 				jHDList=journalsHeaderDataRepository.findByjeTempIdInAndPeriodAndTenantIdOrderByIdDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString())
 						,journalHeaderDetails.get("jePeriod").toString(),tenantId);
 			}

 		}
 		else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())== 0) && (journalHeaderDetails.get("jeLedger")!="" && journalHeaderDetails.get("jeLedger")!=null && !journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
 				(journalHeaderDetails.get("jePeriod")==null || journalHeaderDetails.get("jePeriod")=="" || journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
 		{

 			List<BigInteger> tempIds=templateDetailsRepository.fetchByTenantIdAndActive(tenantId);
 			log.info("tempIds :"+tempIds);
 			if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
 			{
 				log.info("5.journalHeaderDetails :"+journalHeaderDetails);
 				ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
 				ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


 				jHDList=journalsHeaderDataRepository.findByjeTempIdInAndLedgerNameAndJeBatchDateBetween(tempIds,
 						journalHeaderDetails.get("jeLedger").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
 			}	
 			else
 			{
 				log.info("5.journalHeaderDetails :"+journalHeaderDetails);

 				jHDList=journalsHeaderDataRepository.findByjeTempIdInAndLedgerName(tempIds,
 						journalHeaderDetails.get("jeLedger").toString(),tenantId);
 			}

 		}
 		else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())!= 0) && (journalHeaderDetails.get("jeLedger")=="" || journalHeaderDetails.get("jeLedger")==null || journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
 				(journalHeaderDetails.get("jePeriod")==null || journalHeaderDetails.get("jePeriod")=="" || journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
 		{


 			if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
 			{
 				log.info("6.journalHeaderDetails :"+journalHeaderDetails);
 				ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
 				ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


 				jHDList=journalsHeaderDataRepository.findByjeTempIdAndTenantIdAndJeBatchDateBetweenOrderByIdDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
 						tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
 			}	
 			else
 			{
 				log.info("6.journalHeaderDetails :"+journalHeaderDetails);

 				jHDList=journalsHeaderDataRepository.findByjeTempIdAndTenantIdOrderByIdDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),tenantId);

 			}

 		}
 		else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())== 0) && (journalHeaderDetails.get("jeLedger")=="" || journalHeaderDetails.get("jeLedger")==null || journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
 				(journalHeaderDetails.get("jePeriod")!=null && journalHeaderDetails.get("jePeriod")!="" && !journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
 		{

 			List<BigInteger> tempIds=templateDetailsRepository.fetchByTenantIdAndActive(tenantId);
 			log.info("tempIds :"+tempIds);
 			if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
 			{
 				log.info("7.journalHeaderDetails :"+journalHeaderDetails);
 				ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
 				ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


 				jHDList=journalsHeaderDataRepository.findByjeTempIdInAndPeriodAndTenantIdAndJeBatchDateBetween(tempIds,
 						journalHeaderDetails.get("jePeriod").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
 			}	
 			else
 			{
 				log.info("7.journalHeaderDetails :"+journalHeaderDetails);

 				jHDList=journalsHeaderDataRepository.findByjeTempIdInAndPeriod(tempIds,
 						journalHeaderDetails.get("jePeriod").toString(),tenantId);
 			}

 		}
 		else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())!= 0) && (journalHeaderDetails.get("jeLedger")!="" && journalHeaderDetails.get("jeLedger")!=null && !journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
 				(journalHeaderDetails.get("jePeriod")==null || journalHeaderDetails.get("jePeriod")=="" || journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
 		{


 			if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
 			{
 				log.info("8.journalHeaderDetails :"+journalHeaderDetails);
 				ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
 				ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


 				jHDList=journalsHeaderDataRepository.findByjeTempIdAndLedgerNameAndTenantIdAndJeBatchDateBetweenOrderByIdDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
 						journalHeaderDetails.get("jeLedger").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
 			}	
 			else
 			{
 				log.info("8.journalHeaderDetails :"+journalHeaderDetails);

 				jHDList=journalsHeaderDataRepository.findByjeTempIdAndLedgerNameAndTenantIdOrderByIdDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString())
 						,journalHeaderDetails.get("jeLedger").toString(),tenantId);
 			}

 		}
 		else 
 		{
 			log.info("9 :in else");
 			List<BigInteger> tempIds=templateDetailsRepository.fetchByTenantIdAndActive(tenantId);
 			log.info("tempIds :"+tempIds);

 			if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
 			{
 				log.info("9.journalHeaderDetails :"+journalHeaderDetails);
 				ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
 				ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


 				jHDList=journalsHeaderDataRepository.findByByJeTempIdInAndTenantIdAndJeBatchDateBetween(tempIds,
 						tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
 			}

 			else
 			{
 				log.info("9.journalHeaderDetails :"+journalHeaderDetails);
 				jHDList=journalsHeaderDataRepository.findByJeTempIdInAndTenantId(tempIds,tenantId);
 			}

 		}
 		log.info("jHDList :"+jHDList.size());
 		for(JournalsHeaderData jHD:jHDList)
 		{
 			LinkedHashMap map=new LinkedHashMap();
 			if(jHD.getJeTempId()!=null)
 			{
 				map.put("jeTempId", jHD.getJeTempId());
 				TemplateDetails temp=templateDetailsRepository.findOne(jHD.getJeTempId());
 				if(temp!=null && temp.getTemplateName()!=null)
 					map.put("Template Name", temp.getTemplateName());
 			}
 			map.put("Batch Name", jHD.getJeBatchName().replaceAll("\\:", ""));
 		//	map.put("batchName", jHD.getJeBatchName());
 			map.put("Batch Date", jHD.getJeBatchDate().toString());
 			map.put("GL Date", jHD.getGlDate());
 			map.put("Ledger", jHD.getLedgerName());
 			map.put("Source", jHD.getSource());
 			map.put("Category", jHD.getCategory());
 			map.put("Period", jHD.getPeriod());
 			BigDecimal drAmount=jeLinesRepository.fetchDebitAmountJeHeaderId(jHD.getId());
 			if(drAmount!=null)
 			{
 				//String deb = reconciliationResultService.getAmountInFormat(drAmount.toString(),currencyFormat);
 				map.put("Dr Amount", drAmount);
 			}
 			else
 				map.put("Dr Amount", 0);
 			BigDecimal crAmount=jeLinesRepository.fetchDebitAmountJeHeaderId(jHD.getId());
 			if(crAmount!=null)
 			{
 				//String cred = reconciliationResultService.getAmountInFormat(crAmount.toString(),currencyFormat);
 				map.put("Cr Amount", crAmount);
 			}
 			else
 				map.put("Cr Amount", 0);
 			map.put("Reference", jHD.getJobReference());

 			finalMap.add(map);

 		}
 		
 	//	log.info("map  :"+finalMap);
 		finalLhmap.put("map", finalMap);
 		return finalLhmap;

 	}
    
}
