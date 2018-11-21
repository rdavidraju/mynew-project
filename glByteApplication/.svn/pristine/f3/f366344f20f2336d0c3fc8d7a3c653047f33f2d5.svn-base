package com.nspl.app.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import liquibase.util.csv.opencsv.CSVReader;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



//import OracleBia2Functions.*;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.DataStaging;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.FileTemplates;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.SourceProfileFileAssignments;
import com.nspl.app.repository.DataStagingRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;


//import OracleBia2Functions.*;
@Service
@Transactional
public class ReadingFileTemplatesService {

	private final Logger log = LoggerFactory.getLogger(ReadingFileTemplatesService.class);

	@Inject
	FileTemplatesRepository fileTemplatesRepository;

	@Inject
	SetFieldValuesForDataStaging setFieldValuesForDataStaging;

	@Inject
	SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;

	@Inject
	ExcelReadAndValidationService excelReadAndValidationService;

	@Inject
	DataStagingRepository dataStagingRepository;

	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;

	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	@Inject
	Bia2FunctionsService bia2FunctionsService;


	/**
	 * Author Kiran
	 * @param localPath
	 * @param templateId
	 * @param downlodedFiles
	 * @return to get the list of successfully read files status (File Sync Methods based on Id)
	 *//*

	public List<HashMap<String, String>> readingTheFiles(String localPath, Long templateId,List<String> downlodedFiles) 
	{
		log.info("========== *5* To get list of files and to read files from Pc path: "+localPath+" ==========");
		File folder = new File(localPath);
		File[] listOfFiles = folder.listFiles();
		List<HashMap<String, String>> resList= new ArrayList<HashMap<String,String>>();
		if(folder!=null && folder.length()>0)
		{
			log.info("listOfFiles in folder length :-"+listOfFiles.length);
			log.info("downlodedFiles size:"+downlodedFiles.size());
			for(int i=0;i<downlodedFiles.size();i++)
			{
				log.info("downlodedFiles names:"+downlodedFiles.get(i));
			}

			for (int i = 0; i < listOfFiles.length; i++) 
			{
				if(downlodedFiles.size()>0)
				{
					for(int k = 0; k < downlodedFiles.size(); k++)
					{
						HashMap<String,String> filesAndStatus = new HashMap<String, String>();
						if (listOfFiles[i].isFile()) 
						{
							String filename=downlodedFiles.get(k);
							String file = listOfFiles[i].getName();
							if(filename.equalsIgnoreCase(file))
							{
								String path = localPath+filename;
								log.info("FileName in Folder:"+filename+" and path :"+path);
								String result = null;
								result = readFileTemplateDetailsBasedOnFileType(templateId, path);
								log.info("FileName: "+filename+" and result:->>"+result);
								filesAndStatus.put(filename, result);
								resList.add(filesAndStatus);
							}
						} 
						else if (listOfFiles[i].isDirectory()) {
						}
					}
				}
			}
		}
		log.info("resList:"+resList);
		return resList;
	}*/


	/** 
	 * Author Kiran
	 * @param templateId
	 * @param filePath
	 * @return
	 * @throws IOException
	 */

	public String readFileTemplateDetailsBasedOnFileType(Long templateId, String filePath, SourceProfileFileAssignments spfaDetails) 
	{
		log.info("========== *4* Rest Request to read the data from file for template ID: "+templateId);
		FileTemplates fileTemplates = fileTemplatesRepository.findOne(templateId);
		String result ="Failed Saving Data";
		if(fileTemplates!=null)
		{
			String fileType=null;
			//String delimiter=null;
			String source=null;
			int skipRowStart=0;
			int skipRowEnd=0;

			source=filePath.toString();
			//log.info("Source: "+source);

			Long tenantId =fileTemplates.getTenantId();

			int BeforeCount =0;
			log.info("4(1)Fetching details of records in data staging: "+ZonedDateTime.now());
			List<DataStaging> beforeSavingData= dataStagingRepository.findBytemplateIdAndTenantId(templateId,tenantId);
			log.info("4(1)Finished getting details: "+ZonedDateTime.now());
			if(beforeSavingData!=null)
			{
				BeforeCount = beforeSavingData.size();
				//log.info("BeforeCount"+BeforeCount);
			}

			if(fileTemplates.getSkipRowsStart()!=null)
				skipRowStart=fileTemplates.getSkipRowsStart();
			if(fileTemplates.getSkipRowsEnd()!=null)
				skipRowEnd=fileTemplates.getSkipRowsEnd();
			fileType=fileTemplates.getFileType();
			if(fileType!=null && source!=null)
			{
				String file=filePath.toString();
				//log.info("File:- "+file);
				log.info("4(2)Fetching details related to row identifier"+ZonedDateTime.now());
				List<FileTemplateLines> fileTempHeader = fileTemplateLinesRepository.findByTemplateIdAndRecordTYpe(templateId,"Row Identifier");
				log.info("4(2)Finished getting details"+ZonedDateTime.now());
				log.info("fileTempHeader:-> "+fileTempHeader);

				/**
				 * Reading the file based on delimiter
				 **/
				List<DataStaging> dtSaving = new ArrayList<DataStaging>();
				if(fileType.equalsIgnoreCase("delimiter"))
				{
					//File folder = new File(source);
					String delimitr=fileTemplates.getDelimiter();
					/*//log.info("Delimiter from FT:"+delimitr+" and "+tenantId);
					log.info("4(3)Fetching details related to lookup code of delimiter: "+ZonedDateTime.now());
					LookUpCode lookupCode = lookUpCodeRepository.findByTenantIdAndLookUpTypeAndDescription(tenantId,"DELIMITER",delimitr);
					log.info("4(3)Finished getting lookup code: "+ZonedDateTime.now());
					//log.info("lookupCode: "+lookupCode);
					int val=Integer.valueOf(lookupCode.getLookUpCode());*/

					int val = Integer.valueOf(delimitr);
					char delimiter = (char) val;

					//delimiter = String.valueOf(val);
					//log.info("DelimiterObtained From lookUp Codes"+delimiter);


					//log.info("CSV file");
					CSVReader reader = null;
					CSVReader reader1 = null;
					try 
					{
						if(delimiter!= 'y')
						{
							//log.info("Reading Csv using Delimiter: "+delimiter);
							String[] line;
							String[] line1;
							int count=0;
							//FileTemplateLines fileTempHeader = fileTemplateLinesRepository.findByTemplateIdAndRecordTYpe(templateId,"Header");
							/** List of Fields to set while saving in data staging*/
							List<FileTemplateLines> fileTemplateLines = fileTemplateLinesRepository.findByTemplateIdAndRecordTYpe(templateId,"Row Data");
							DataStaging result1=new DataStaging();

							reader = new CSVReader(new FileReader(file), (delimiter));

							int totalRows=0;
							int rowEnd=0;
							String rowidentifier=null;
							String recordIdentifier=null;
							if(fileTempHeader!=null && fileTempHeader.size()!=0 && !fileTempHeader.equals(""))
							{
								recordIdentifier=fileTempHeader.get(0).getRecordIdentifier();
								if(recordIdentifier!=null)
								{
									while((line=reader.readNext())!=null)
									{
										rowidentifier=line[0];
										if(recordIdentifier.equalsIgnoreCase(rowidentifier))
										{
											totalRows=totalRows+1;
										}
									}
									rowEnd=totalRows-skipRowEnd;
									log.info("Row End:-> "+rowEnd);
								}								
							}
							else
							{
								while ((line = reader.readNext()) != null)
								{
									totalRows=totalRows+1;
								}
								rowEnd=totalRows-skipRowEnd;
								log.info("Row End:-> "+rowEnd);
							}

							log.info("totalRows "+totalRows+" and rowEnd: "+rowEnd);
							log.info("skipRowStart: "+skipRowStart  +" and skipRowEnd:"+skipRowEnd);
							if((skipRowStart!=0 && skipRowStart<=totalRows)|| (skipRowEnd!=0 && skipRowEnd<=totalRows))
							{
								reader1 = new CSVReader(new FileReader(file), (delimiter));
								line1=reader1.readNext();
								while ((line1)!=null && line1.length>0 && count<totalRows) 
								{
									log.info("count: "+count+" and skipRowStart: "+skipRowStart+" and rowEnd: "+rowEnd+" and line1.length: "+line1.length);
									if(count<skipRowStart || rowEnd<=count)
									{
										log.info("Count1: "+count);
										if(!fileTempHeader.isEmpty())
										{
											recordIdentifier=fileTempHeader.get(0).getRecordIdentifier();
											if(recordIdentifier!=null)
											{
												if(line1!=null && line1.length>0)
													rowidentifier=line1[0];
												if(rowidentifier!=null && rowidentifier.equalsIgnoreCase(recordIdentifier))
												{
													count=count+1;
													//log.info("line to skip in if: "+Arrays.toString(line1));
												}
											}
											else{
												count=count+1;
												log.info("line to skip in else for row identifier: "+Arrays.toString(line1));
											}
										}
										else{
												count=count+1;
												log.info("line to skip in else for csv, tsv: "+Arrays.toString(line1));
										}

									}
									/*else if(!fileTempHeader.isEmpty())
									{
										log.info("Count2: "+count);
										recordIdentifier=fileTempHeader.get(0).getRecordIdentifier();
										//										log.info("recordIdentifier: "+recordIdentifier);
										if(recordIdentifier!=null)
										{
											if(line1!=null && line1.length>0)
												rowidentifier=line1[0];
											//log.info("recordIdentifier: "+recordIdentifier+"rowidentifier: "+rowidentifier);
											if(rowidentifier!=null && rowidentifier.equalsIgnoreCase(recordIdentifier))
											{
												//log.info("recordIdentifier: "+recordIdentifier);
												result1=setFieldValuesForDataStaging.DataStagingFields(line1, templateId, spfaDetails,fileTemplateLines,tenantId); 
												if(fileTemplates.getSdFilename()!=null)
													result1.setFileName(fileTemplates.getSdFilename());
												if(result1!=null)
												{
													dtSaving.add(result1);
													count=count+1;
													//log.info("<<=========No of records added to list:- "+count);
												}
											}
										}

									}*/
									else{
										log.info("Count12: "+count);
										result1=setFieldValuesForDataStaging.DataStagingFields(line1, templateId, spfaDetails,fileTemplateLines,tenantId); 
										if(fileTemplates.getSdFilename()!=null)
											result1.setFileName(fileTemplates.getSdFilename());
										if(result1!=null)
										{
											dtSaving.add(result1);
											count=count+1;
											//log.info("<<=========No of records added to list:- "+count);
										}

									}
									line1 = reader1.readNext();
									//log.info("Count: "+count);
								}
								//log.info("count: "+count+" and dtSaving size: "+dtSaving.size());
							}
							else
							{
								log.info("In else when Skip Start and end row is not given: "+skipRowStart+" and "+skipRowEnd);
								reader = new CSVReader(new FileReader(file), (delimiter));
								//log.info("4(4)Extraction of data and saving to CollectionList: "+ZonedDateTime.now());	
								while((line = reader.readNext())!=null && line.length>0 && !line.equals(""))
								{
									if(fileTempHeader!=null && fileTempHeader.size()!=0 && !fileTempHeader.equals(""))
									{
										String constVal=fileTempHeader.get(0).getRecordIdentifier();
										if(constVal!=null)
										{
											if(line!=null && line.length>0)
												rowidentifier=line[0];
											if(rowidentifier!=null && rowidentifier.equalsIgnoreCase(constVal))
											{
												//log.info("rowidentifier:->"+rowidentifier+" and fieldOne:->"+constVal);
												//log.info("line to add:-"+Arrays.toString(line));
												result1=setFieldValuesForDataStaging.DataStagingFields(line, templateId, spfaDetails,fileTemplateLines,tenantId); 
												result1.setFileName(fileTemplates.getSdFilename());
												//log.info("result_1:-"+result1);
												if(result1!=null)
												{
													dtSaving.add(result1);
													count=count+1;
													//log.info("<<=========No of records added to list:- "+count);
												}
											}
										}
									}
									else{
										result1=setFieldValuesForDataStaging.DataStagingFields(line, templateId, spfaDetails,fileTemplateLines,tenantId);
										//log.info("TenantId:-> "+tenantId);
										//result1.setFileName(fileTemplates.getSdFilename());
										if(result1!=null)
										{
											dtSaving.add(result1);
											count=count+1;
											//log.info("<<=========No of records added to list:- "+count);
										}

										//										log.info("result_2:-"+result2);
									}
									//log.info("<<=========No of records added to list:- "+count);
								}
								//log.info("4(4)Finished saving data to CollectionList: "+ZonedDateTime.now());
							}
							log.info("File contails:"+count);
						}
						else{
							log.info("Delimitar is empty or null");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}


				/**
				 * Reading the file based on Position
				 **/
				// Working code(Jar file required)
				else if(fileType.equalsIgnoreCase("position"))
				{
					//log.info("============= 001: "+fileTempHeader);
					//	List<FileTemplateLines> fileTempDetails = fileTemplateLinesRepository.findByTemplateId(templateId);

					BufferedReader br = null;
					FileReader fr = null;

					try {

						//br = new BufferedReader(new FileReader(FILENAME));
						fr = new FileReader(file);
						br = new BufferedReader(fr);

						String currentLine;

						while ((currentLine = br.readLine()) != null && currentLine.length()>0) 
						{
							//log.info("present Line:-> "+currentLine);
							List<FileTemplateLines> fileTempDetails1 = fileTemplateLinesRepository.findByTemplateIdAndRecordTYpe(templateId, "Row Data");

							for(int j=0; j<fileTempHeader.size();j++)
							{
								FileTemplateLines fileTmpLine=fileTempHeader.get(j);
								//log.info("============= 002: "+fileTmpLine);
								String record=currentLine.substring((fileTmpLine.getPositionBegin()-1),fileTmpLine.getPositionEnd());
								//log.info("============= 003 Record:-> "+record);
								String recordType=bia2FunctionsService.callFunction(fileTmpLine.getFormula(), record);
								//	log.info("============= 004 Record Type obtained:-> "+recordType);

								if(recordType!=null)
								{
									//									List<FileTemplateLines> fileTempDetails1 = fileTemplateLinesRepository.findByConstantValueAndRecordTYpe(recordType,"Row Data");
									if(fileTempDetails1!=null && fileTempDetails1.size()>0)
									{
										List<FileTemplateLines> fileTempDetails =null;
										templateId=fileTempDetails1.get(0).getTemplateId();
										if(recordType.equalsIgnoreCase("roc"))
										{
											//	templateId=fileTempDetails1.get(0).getTemplateId();
											fileTempDetails = fileTemplateLinesRepository.findByConstantValueAndRecordTYpeAndTemplateId(recordType, "Row Data", templateId);
										}
										else if(recordType.equalsIgnoreCase("cbk"))
										{
											fileTempDetails = fileTemplateLinesRepository.findByConstantValueAndRecordTYpeAndTemplateId(recordType, "Row Data", templateId);
										}
										else if(recordType.equalsIgnoreCase("soc"))
										{
											fileTempDetails = fileTemplateLinesRepository.findByConstantValueAndRecordTYpeAndTemplateId(recordType, "Row Data", templateId);
										}

										if(fileTempDetails!=null && fileTempDetails.size()>0)
										{
											//	log.info("============= 00 fileTempDetails"+fileTempDetails);
											ArrayList<String> listOfString = new ArrayList<String>();
											for(int in=0;in<fileTempDetails.size();in++)
											{
												FileTemplateLines ftl=fileTempDetails.get(in);
												//log.info("============= 006 ftl:-> "+ftl);
												String fieldData=currentLine.substring((ftl.getPositionBegin()-1),ftl.getPositionEnd());
												//											log.info("fieldData:-> "+fieldData);
												listOfString.add(fieldData);
											}
											//log.info("data Row Obtained:-> "+listOfString);
											String[] stringArray = listOfString.toArray(new String[listOfString.size()]);

											DataStaging result1=setFieldValuesForDataStaging.DataStagingFields(stringArray, templateId, spfaDetails, fileTempDetails, tenantId);
											//	result1.setFileName(fileTemplates.getSdFilename());
											//log.info("============= 007 result1:-> "+result1);
											if(result1!=null)
												dtSaving.add(result1);
										}

									}
								}
							}
						}

					} catch (IOException e) {

						e.printStackTrace();

					}
				}



				//Bkp 7 oct
				/**
				 * Reading the file based on Position
				 **//*
				// Working code(Jar file required)
				else if(fileType.equalsIgnoreCase("position") && fileTempHeader!=null)
				{
					log.info("============= 001: "+fileTempHeader);
					//List<FileTemplateLines> fileTempDetails = fileTemplateLinesRepository.findByTemplateId(templateId);

					BufferedReader br = null;
					FileReader fr = null;

					try {

						//br = new BufferedReader(new FileReader(FILENAME));
						fr = new FileReader(file);
						br = new BufferedReader(fr);

						String currentLine;

						while ((currentLine = br.readLine()) != null && currentLine.length()>0) 
						{
							log.info("present Line:-> "+currentLine);

							for(int j=0; j<fileTempHeader.size();j++)
							{
								FileTemplateLines fileTmpLine=fileTempHeader.get(j);
								log.info("============= 002: "+fileTmpLine);
								String record=currentLine.substring((fileTmpLine.getPositionBegin()-1),fileTmpLine.getPositionEnd());
								log.info("============= 003 Record:-> "+record);
								String recordType=DecodeFunctions.callFunction(fileTmpLine.getFormula(), record);
								log.info("============= 004 Record Type obtained:-> "+recordType);

								if(recordType!=null)
								{
									List<FileTemplateLines> fileTempDetails = fileTemplateLinesRepository.findByTemplateIdAndConstantValueAndRecordTYpe(templateId,recordType,"Row Data");
									log.info("============= 005 Record Type obtained:-> "+fileTempDetails);
									if(fileTempDetails!=null)
									{
										ArrayList<String> listOfString = new ArrayList<String>();
										for(int in=0;in<fileTempDetails.size();in++)
										{
											FileTemplateLines ftl=fileTempDetails.get(in);
											log.info("============= 006 Record Type obtained:-> "+ftl);
											String fieldData=currentLine.substring((ftl.getPositionBegin()-1),ftl.getPositionEnd());
//											log.info("fieldData:-> "+fieldData);
											listOfString.add(fieldData);
										}
										log.info("data Row Obtained:-> "+listOfString);
										String[] stringArray = listOfString.toArray(new String[listOfString.size()]);

										DataStaging result1=setFieldValuesForDataStaging.DataStagingFields(stringArray, templateId, spfaDetails, fileTempDetails, tenantId);
										log.info("============= 007 Result Obtained:-> "+result1);
										if(result1!=null)
											dtSaving.add(result1);

									}
								}
							}
						}

					} catch (IOException e) {

						e.printStackTrace();

					}
				}*/




				/*else if(fileType.equalsIgnoreCase("position") && fileTempHeader!=null)
				{
					String constValue= fileTempHeader.getConstantValue();
					log.info("Constant Value obtained:-> "+constValue);
					List<FileTemplateLines> fileTempDetails = fileTemplateLinesRepository.findByTemplateId(templateId);

					if(constValue!=null)
					{
						if(constValue.equalsIgnoreCase("roc"))
						{
							BufferedReader br = null;
							FileReader fr = null;

							try {

								//br = new BufferedReader(new FileReader(FILENAME));
								fr = new FileReader(file);
								br = new BufferedReader(fr);

								String currentLine;

								while ((currentLine = br.readLine()) != null && currentLine.length()>0) 
								{
									log.info("present Line:-> "+currentLine);
									ArrayList<String> listOfString = new ArrayList<String>();
									for(int in=0;in<fileTempDetails.size();in++)
									{
										FileTemplateLines ftl=fileTempDetails.get(in);
										String fieldData=currentLine.substring((ftl.getPositionBegin()-1),ftl.getPositionEnd());
										log.info("fieldData:-> "+fieldData);
										listOfString.add(fieldData);
									}
									log.info("data Row Obtained:-> "+listOfString);
									String[] stringArray = listOfString.toArray(new String[listOfString.size()]);

									String result1=setFieldValuesForDataStaging.DataStagingFields(stringArray, templateId, spfaDetails);
									log.info("Result Obtained:-> "+result1);
								}

							} catch (IOException e) {

								e.printStackTrace();

							}

						}
						else if(constValue.equalsIgnoreCase("cbk"))
						{

						}
					}
				}*/
				else{

					/**
					 * Code if there is no skip rows are given
					 */
				}
				log.info("Count of list contains:"+dtSaving.size());
				log.info("4(5)Saving List of data to Data Staging: "+ZonedDateTime.now());
				dataStagingRepository.save(dtSaving);

				log.info("4(5)Finished saving List of data to Data Staging: "+ZonedDateTime.now());
			}
			//	}
			int afterCount=0;
			log.info("4(6)Fetching details of records in data staging: "+ZonedDateTime.now());
			List<DataStaging> afterSavingData= dataStagingRepository.findBytemplateIdAndTenantId(templateId,fileTemplates.getTenantId());
			log.info("4(6)Finished getting details of records in data staging: "+ZonedDateTime.now());
			if(afterSavingData!=null)
			{
				afterCount = afterSavingData.size();
			}
			log.info("Before And After Size:"+BeforeCount+" and "+afterCount);
			if(afterCount>BeforeCount)
			{	result = "Successfully Saved Data";
			return result;
			}
			else
				return result;
		}
		else{
			log.info("Invalid Tenant Id");
		}
		return result;

	}


	// Bkp 28-08
	/*public String readFileTemplateDetailsBasedOnFileType(Long templateId, String filePath, SourceProfileFileAssignments spfaDetails) 
		{
		log.info("========== *4* Rest Request to read the data from file for template ID: "+templateId);
		FileTemplates fileTemplates = fileTemplatesRepository.findOne(templateId);
		String result ="File Saving Failed";
		if(fileTemplates!=null)
		{
			String fileType=null;
			//String delimiter=null;
			String source=null;
			int skipRowStart=0;
			int skipRowEnd=0;

			source=filePath.toString();
			//log.info("Source: "+source);

			Long tenantId =fileTemplates.getTenantId();

			int BeforeCount =0;
			List<DataStaging> beforeSavingData= dataStagingRepository.findBytemplateIdAndTenantId(templateId,tenantId);
			if(beforeSavingData!=null)
			{
				BeforeCount = beforeSavingData.size();
				//log.info("BeforeCount"+BeforeCount);
			}


			//File folder = new File(source);
			fileType=fileTemplates.getFileType();
			String delimitr=fileTemplates.getDelimiter();
		//	log.info("Delimiter from FT:"+delimitr);
			LookUpCode lookupCode = lookUpCodeRepository.findByTenantIdAndLookUpTypeAndDescription(tenantId,"DELIMITER",delimitr);

			//log.info("lookupCode: "+lookupCode);
			int val=Integer.valueOf(lookupCode.getLookUpCode());


			char delimiter = (char) val;

			//delimiter = String.valueOf(val);
			log.info("DelimiterObtained From lookUp Codes"+delimiter);

			if(fileTemplates.getSkipRowsStart()!=null)
				skipRowStart=fileTemplates.getSkipRowsStart();
			if(fileTemplates.getSkipRowsEnd()!=null)
				skipRowEnd=fileTemplates.getSkipRowsEnd();

			if(fileType!=null && source!=null)
			{
				String file=filePath.toString();
				//log.info("File:- "+file);
				if(fileType.equalsIgnoreCase("delimiter"))
				{
					//log.info("CSV file");
					CSVReader reader = null;
					CSVReader reader1 = null;
					try 
					{
						if(delimiter!= 'y')
						{
							log.info("Reading Csv using Delimiter: "+delimiter);
							String[] line;
							String[] line1;
							int count=1;
							log.info("skipRowStart and skipRowEnd:- "+skipRowStart  +" and "+skipRowEnd);
							FileTemplateLines fileTempHeader = fileTemplateLinesRepository.findByTemplateIdAndRecordTYpe(templateId,"Header");
							if(skipRowStart>=0 && skipRowEnd!=0 && skipRowStart<=skipRowEnd)
							{
								reader = new CSVReader(new InputStreamReader(new FileInputStream(file), "UTF-8"),(delimiter));
								reader1 = new CSVReader(new InputStreamReader(new FileInputStream(file), "UTF-8"),(delimiter));

//								reader = new CSVReader(new FileReader(file), (delimiter));
//								reader1 = new CSVReader(new FileReader(file), (delimiter));


								int totalRows=0;
								while ((line = reader.readNext()) != null)
								{
									totalRows=totalRows+1;
								}
								log.info("totalRows:-> "+totalRows);
								int rowEnd=totalRows-skipRowEnd;
								//log.info("Row End:-> "+rowEnd);

								line1 = reader1.readNext();
								while (line1!=null && line1.length>0 && count<=totalRows) 
								{

									if(count<=skipRowStart || rowEnd<=count)
									{
										count=count+1;
										log.info("line to skip :-"+Arrays.toString(line1));
									}
									else if(fileTempHeader!=null)
									{
										String constVal=fileTempHeader.getConstantValue();
										if(constVal!=null)
										{
											String rowidentifier=null;
											if(line1!=null && line1.length>0)
												rowidentifier=line1[0];
											if(rowidentifier!=null && rowidentifier.equalsIgnoreCase(constVal))
											{
												//log.info("rowidentifier:->"+rowidentifier+" and fieldOne:->"+constVal);
												//log.info("line to add:-"+Arrays.toString(line1));
												String result1=setFieldValuesForDataStaging.DataStagingFields(line1, templateId, spfaDetails); 
												//log.info("result_1:-"+result1);
												count=count+1;
											}
										}
									}
									else
									{
										//log.info("line to add:-"+Arrays.toString(line1));
										String result1=setFieldValuesForDataStaging.DataStagingFields(line1, templateId, spfaDetails); 
										//log.info("result_1:-"+result1);
										count=count+1;
									}
									line1 = reader1.readNext();
								}
							}
							else
							{
								log.info("In else when Skip Start and end row is not given: "+skipRowStart+" and "+skipRowEnd);
								reader = new CSVReader(new FileReader(file), (delimiter));
								while ((line = reader.readNext()) != null) 
								{
									if(fileTempHeader!=null)
									{
										String constVal=fileTempHeader.getConstantValue();
										if(constVal!=null)
										{
											String rowidentifier=null;
											if(line!=null && line.length>0)
												rowidentifier=line[0];
											if(rowidentifier!=null && rowidentifier.equalsIgnoreCase(constVal))
											{
												//log.info("rowidentifier:->"+rowidentifier+" and fieldOne:->"+constVal);
												//log.info("line to add:-"+Arrays.toString(line));
												String result1=setFieldValuesForDataStaging.DataStagingFields(line, templateId, spfaDetails); 
												//log.info("result_1:-"+result1);
												count=count+1;
											}
										}
									}
									else{
										String result2=setFieldValuesForDataStaging.DataStagingFields(line, templateId, spfaDetails);
										//log.info("result_2:-"+result2);
									}
								}
							}
						}
						else{
							log.info("Delimitar is empty or null");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			int afterCount=0;
			List<DataStaging> afterSavingData= dataStagingRepository.findBytemplateIdAndTenantId(templateId,fileTemplates.getTenantId());
			if(afterSavingData!=null)
			{
				afterCount = afterSavingData.size();
			}
			log.info("Before And After Size:"+BeforeCount+" and "+afterCount);
			if(afterCount>BeforeCount)
			{	result = "Successfully saved data";
			return result;
			}
			else
				return result;
		}
		else{
			log.info("Invalid Tenant Id");
		}
		return result;
		}*/



}
