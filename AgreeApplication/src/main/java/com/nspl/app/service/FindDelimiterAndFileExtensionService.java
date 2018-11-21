package com.nspl.app.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.nspl.app.domain.LookUpCode;
import com.nspl.app.repository.DataStagingRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.web.rest.dto.ExcelFileReadingDTO;
import com.nspl.app.web.rest.dto.FileTemplateDataDTO;
import com.nspl.app.web.rest.dto.FileTemplateLinesDTO;
import com.nspl.app.web.rest.dto.MultipleIdentifiersDTO;
import com.nspl.app.web.rest.dto.SampleDataForFTDTO;

@Service
@Transactional
public class FindDelimiterAndFileExtensionService {

	private final Logger log = LoggerFactory.getLogger(FindDelimiterAndFileExtensionService.class);

	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;

	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	FileTemplatesRepository fileTemplatesRepository;

	DataStagingRepository dataStagingRepository;

	@PersistenceContext(unitName="default")
	private EntityManager em;

	/*************
	 * Author Kiran
	 *************/

	/** To get the index of '"' in a string */
	public List<Integer> toGetIndexNumbers(String givenLine)
	{
		List<Integer> listOfInt = new ArrayList<Integer>();

		for( int i=0; i<givenLine.length(); i++ ) 
		{
			if( givenLine.charAt(i) == '"' ) 
			{
				Integer counter=i;
				listOfInt.add(counter);
			} 
		}
		return listOfInt;
	}


	/** To get the count of delimiter in a line*/
	public HashMap<Integer, Integer> toFindDelimiter(String givenLine)
	{
		String specialChars=givenLine.replaceAll("[a-zA-Z0-9]", "");
		//log.info("Special Characters:=> "+specialChars+" and size: "+specialChars.length());
		HashMap<Integer, Integer> occerance = new HashMap<Integer, Integer>() ;

		if(specialChars!="" && specialChars.length()>0)
		{
			for(int m=0;m<specialChars.length();m++)
			{
				char character = specialChars.charAt(m); 
				if(occerance.get((int)character) != null)
				{
					occerance.put((int)character, occerance.get((int)character)+1);
				}
				else{
					occerance.put((int)character, 1);
				}
			}
			//log.info("occerance:=> "+occerance);
		}
		return occerance;
	}


	/** To finalize the delimiter in the file */
	public Integer findTheDelimiter(List<HashMap<Integer, Integer>> occeranceListOfDelimiter, Set<Integer> keys)
	{
		int occurSize = occeranceListOfDelimiter.size();
		log.info("Delimiter OcceranceList in method: "+occeranceListOfDelimiter +" and size "+occurSize);
		List<Integer> listOfKeys = new ArrayList<Integer>();
		for(int k=0;k<occurSize;k++)
		{
			HashMap<Integer, Integer> hmVal = occeranceListOfDelimiter.get(k);
			Map.Entry<Integer, Integer> maxEntry = null;
			for (Map.Entry<Integer, Integer> entry : hmVal.entrySet())
			{
				if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
				{
					maxEntry = entry;
				}
			}
			listOfKeys.add(maxEntry.getKey());
		}

		Set<Integer> uniqueSet = new HashSet<Integer>(listOfKeys);
		List<HashMap<Integer, Integer>> maxList = new ArrayList<HashMap<Integer,Integer>>();
		for (Integer temp : uniqueSet) {
			HashMap<Integer, Integer>  max = new HashMap<Integer, Integer>();
			int count =Collections.frequency(listOfKeys, temp);
			//			log.info("Got the Result as:"+temp + ": " + count);
			max.put(temp, count);
			maxList.add(max);
		}

		int key=0;
		log.info("maxList obt:-> "+maxList);
		Map.Entry<Integer, Integer> maxEntry1 = null;
		for(int k=0;k<maxList.size();k++)
		{

			HashMap<Integer, Integer> keyval = maxList.get(k);

			for (Map.Entry<Integer, Integer> entry : keyval.entrySet())
			{
				if (maxEntry1 == null || entry.getValue().compareTo(maxEntry1.getValue()) > 0)
				{
					maxEntry1 = entry;
				}
				else if(maxEntry1!=null && entry.getValue().compareTo(maxEntry1.getValue()) > 0)
				{
					maxEntry1 = entry;
				}
			}
			//log.info("maxEntry1: "+maxEntry1);
		}
		key=maxEntry1.getKey();
		log.info("Final key value obtained is: "+key);
		int result=0;
		log.info("listOfKeys:->"+listOfKeys+" and size :"+listOfKeys.size());
		if(listOfKeys.size()==occurSize)
		{
			result=key;
		}
		return result;
	}

	/********************************************
	 * To Identify the type and pass through the respective method
	 *********************************************/
	public List<FileTemplateDataDTO> fetchTemplateDefinitionData(MultipartFile file,String multipleIdentifiersList,List<FileTemplateDataDTO> fileTemplateDataDTOList,
			FileTemplateDataDTO fileTemplateData,char delimiterChar, int skipStartRow, int skipEndRow,Long tenantId, String rowIdentifier,boolean multipleIdentifier, String fileType)
			{
		if(multipleIdentifiersList!=null && !multipleIdentifiersList.isEmpty())
		{
			String criteria=null;
			int posBeging=0;
			int posEnd=0;
			ObjectMapper objMapper = new ObjectMapper();
			TypeFactory typeFact = objMapper.getTypeFactory();
			List<MultipleIdentifiersDTO> multipleRowidentifiersList=new ArrayList<MultipleIdentifiersDTO>();
			try {
				multipleRowidentifiersList = objMapper.readValue(multipleIdentifiersList, typeFact.constructCollectionType(List.class,MultipleIdentifiersDTO.class));
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}

			for(MultipleIdentifiersDTO singleRowidentifier:multipleRowidentifiersList)
			{
				log.info("Criteria: "+singleRowidentifier.getCriteria());
				log.info("RowIdentifier: "+singleRowidentifier.getRowIdentifier());
				log.info("positionStart: "+singleRowidentifier.getPositionStart());
				log.info("positionEnd: "+singleRowidentifier.getPositionEnd());

				rowIdentifier=singleRowidentifier.getRowIdentifier();
				criteria = singleRowidentifier.getCriteria();
				if(singleRowidentifier.getPositionStart()!=0)
					posBeging=singleRowidentifier.getPositionStart();
				if(singleRowidentifier.getPositionEnd()!=0)
					posEnd=singleRowidentifier.getPositionEnd();

				/****************************************************************************************
				 * Function Call to get the Sample data and Template header Info for Multiple Row Identifiers
				 *****************************************************************************************/
				fileTemplateData=readingFileAndFetchingDataForMultipleRowIdentifiers(delimiterChar,file,skipStartRow,skipEndRow,tenantId,rowIdentifier,multipleIdentifier,criteria,posBeging,posEnd, fileType);
				fileTemplateData.setMultipleIdentifierFlag(true);
				log.info("fileTemplateData Status: "+fileTemplateData.getStatus());
				if(fileTemplateData!=null )
				{
					fileTemplateDataDTOList.add(fileTemplateData);
				}
			}
			return fileTemplateDataDTOList;
		}
		else
		{
			/********************************************
			 * Function Call to get the Sample data and Template header Info
			 *********************************************/
			fileTemplateData=readingFileAndFetchingTemplateData(delimiterChar, file, skipStartRow, skipEndRow, tenantId, rowIdentifier, fileType);
			log.info("fileTemplateData Status: "+fileTemplateData.getStatus());
			if(fileTemplateData!=null )
			{
				fileTemplateDataDTOList.add(fileTemplateData);
			}
			return fileTemplateDataDTOList;
		}
			}

	/********************************************
	 * To get the Sample data and Template header Info
	 *********************************************/
	public FileTemplateDataDTO readingFileAndFetchingTemplateData(Character delimiter, MultipartFile file, int skipStartRow, int skipEndRow,Long tenantId, String rowIdentifier, String fileType) 
	{
		FileTemplateDataDTO fileTemplateData = new FileTemplateDataDTO();
		String fileName=file.getOriginalFilename();

		if(rowIdentifier!=null && !rowIdentifier.equals("") && rowIdentifier.length()>=7 && !(rowIdentifier.startsWith("H")) && !(rowIdentifier.startsWith("R")) && fileName.endsWith(".dfr"))
		{
			log.info("To get header and row info");
			fileTemplateData.setRowIdentifier(rowIdentifier);
			fileTemplateData=readingFileAndFetchingDataForMultipleRowIdentifiers(delimiter, file, skipStartRow, skipEndRow,tenantId,rowIdentifier,true,"RECORD_START_ROW",0,0,fileType);
		}
		else{
			int count=0, linesSize=0, headerRow=-1;

			if(rowIdentifier==null || rowIdentifier.isEmpty())
				rowIdentifier="";
			log.info("rowIdentifier: "+rowIdentifier);
			String strLineData="";
			BufferedReader bufferReader = null;
			List<String> listOfSampledataLines=new ArrayList<String>();
			List<String> headersList = new ArrayList<String>(); // To represent the header columns
			List<List<String[]>> extractedDataRowsForOthers = new ArrayList<List<String[]>>();
			List<HashMap<String, String>> listOfRows = new ArrayList<HashMap<String,String>>(); // To repersent sample data of 10 rows based on headers
			List<FileTemplateLinesDTO> fileTemplateLinesDTO = new ArrayList<FileTemplateLinesDTO>(); // To Save the data into file template lines table
			HashMap<String, List<List<String[]>>> extractedDataRows = new HashMap<String, List<List<String[]>>>();
			List<HashMap<String, List<List<String[]>>>> extractedDataRowsList = new ArrayList<HashMap<String, List<List<String[]>>>>();

			try 
			{
				InputStream inputStream = file.getInputStream();
				bufferReader=new BufferedReader(new InputStreamReader(inputStream));
				List<String> strLineDataList=new ArrayList<String>();
				while((strLineData=bufferReader.readLine())!=null)
				{
					strLineDataList.add(strLineData);
				}
				log.info("strLineDataList Size: "+strLineDataList.size());
				if(bufferReader!=null)
					bufferReader.close();
				if(inputStream!=null)
					inputStream.close();
				String delimiterChar=delimiter.toString();

				for(int indx_i=0;indx_i<skipStartRow && indx_i<strLineDataList.size();indx_i++)
				{
					log.info("Skip row: "+strLineDataList.get(indx_i));
					strLineDataList.remove(0);
				}
				linesLoop:for(int indx_i=0;indx_i<strLineDataList.size() && linesSize<11;indx_i++)
				{
					String lineInLoop=strLineDataList.get(indx_i);
					String[] arrayOfLineData = null;
					linesSize=+1;
					if(delimiterChar.equals("|")){
						arrayOfLineData = lineInLoop.split("\\|");
					}
					else
						arrayOfLineData = lineInLoop.split(delimiterChar);

					if(rowIdentifier!=null && !rowIdentifier.equals("") && !rowIdentifier.equalsIgnoreCase(arrayOfLineData[0]))
					{
						continue linesLoop;
					}
					else
					{
						if(listOfRows.size()<=10 && count<=strLineDataList.size())
						{
							List<String[]> rowData = new ArrayList<String[]>();
							HashMap<String, String> row = new HashMap<String, String>();
							if(fileName.contains(".dfr") && rowIdentifier!=null  && !rowIdentifier.equals(""))
							{
								JSONParser jsonParser = new JSONParser();
								List<Object> jsonObjectList;
								try 
								{
									jsonObjectList = (List<Object>) jsonParser.parse(new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("jsonFile/dfrColumnIdentifier.json"))));

									for(Object object : jsonObjectList)
									{
										JSONObject jsonObject = (JSONObject) object;
										String recordIdentifier = (String) jsonObject.get("identifierType");
										if(recordIdentifier.equalsIgnoreCase(rowIdentifier))
										{
											JSONArray columnNames = (JSONArray) jsonObject.get("columns");
											for(int i = 0; i < columnNames.size();i++) 
											{
												JSONObject innerJsonObject = (JSONObject) columnNames.get(i);
												String columnNum= (String) innerJsonObject.get("columnNumber");
												String columnHeader= (String) innerJsonObject.get("columnHeader");
												String recordType= (String) innerJsonObject.get("recordType");
												String dateFormat=null;
												if(innerJsonObject.get("dateFormat")!=null)
												{
													dateFormat=(String) innerJsonObject.get("dateFormat");
												}

												FileTemplateLinesDTO fileTempData= new FileTemplateLinesDTO();
												fileTempData.setColumnNumber(Integer.valueOf(columnNum));
												if(i<9)
												{
													fileTempData.setMasterTableReferenceColumn("FIELD_0"+(columnNum));
												}
												else{
													fileTempData.setMasterTableReferenceColumn("FIELD_"+(columnNum));
												}
												fileTempData.setRecordTYpe(recordType);
												fileTempData.setRecordIdentifier(recordIdentifier);
												fileTempData.setColumnHeader(columnHeader);
												fileTempData.setRecordStartRow(rowIdentifier);
												fileTempData.setDateFormat(dateFormat);
												headersList.add(columnHeader);

												if(i == (columnNames.size()-1))
												{
													fileTempData.setLastMasterTableRefCol(true);
													fileTempData.setLastColNumber(true);
												}
												fileTemplateData.setRowIdentifier(rowIdentifier);
												fileTemplateLinesDTO.add(fileTempData);
											}

											for(int indx_s=0;indx_s<fileTemplateLinesDTO.size();indx_s++)
											{
												String[] strArr = new String[2];
												String colName=fileTemplateLinesDTO.get(indx_s).getColumnHeader();

												strArr[0] = colName;
												/** To avoid ArrayIndex Out of bond exception*/
												if(indx_s<arrayOfLineData.length)
												{
													row.put(colName, arrayOfLineData[indx_s]);
													strArr[1] = arrayOfLineData[indx_s];
												}
												rowData.add(strArr);
											}
											extractedDataRowsForOthers.add(rowData);
											listOfRows.add(row);
											listOfSampledataLines.add(lineInLoop);
										}
									}
								}
								catch(ParseException e){
									e.printStackTrace();
								}
							}
							else if(headerRow==-1)
							{
								fileTemplateData.setLastLineNumber(arrayOfLineData.length);
								for(int indx_r=0;indx_r<arrayOfLineData.length;indx_r++)
								{
									FileTemplateLinesDTO fileTempData= new FileTemplateLinesDTO();
									String colheading = arrayOfLineData[indx_r];
									int num=indx_r+1;
									//									log.info("====>> "+indx_r+" Header: "+colheading);

									for(int indx_k=0;indx_k<indx_r;indx_k++)
									{
										if(colheading.equalsIgnoreCase(arrayOfLineData[indx_k]))
										{
											colheading=colheading+"_"+(num);
											//											log.info("changed column heading to avoid Duplicates: "+colheading);
										}
									}
									fileTempData.setColumnNumber(num);
									fileTempData.setLineNumber(num);
									if(indx_r<9)
									{
										fileTempData.setMasterTableReferenceColumn("FIELD_0"+(num));
									}
									else{
										fileTempData.setMasterTableReferenceColumn("FIELD_"+(num));
									}
									fileTempData.setColumnHeader(colheading);
									headersList.add(colheading);
									if(indx_r == arrayOfLineData.length-1 )
									{
										fileTempData.setLastMasterTableRefCol(true);
										fileTempData.setLastColNumber(true);
									}
									fileTemplateLinesDTO.add(fileTempData);
								}
								log.info("Header List from the given file: "+headersList);
								headerRow=0;
							}
							else{
								for(int indx_s=0;indx_s<fileTemplateLinesDTO.size();indx_s++)
								{
									String[] strArr = new String[2];
									String colName=fileTemplateLinesDTO.get(indx_s).getColumnHeader();

									strArr[0] = colName;
									/** To avoid ArrayIndex Out of bond exception*/
									if(indx_s<arrayOfLineData.length)
									{
										row.put(colName, arrayOfLineData[indx_s]);
										strArr[1] = arrayOfLineData[indx_s];
									}
									rowData.add(strArr);
								}
								extractedDataRowsForOthers.add(rowData);
								listOfRows.add(row);
								listOfSampledataLines.add(lineInLoop);
							}
							count=count+1;
						}
					}
				}
				extractedDataRows.put(rowIdentifier, extractedDataRowsForOthers);
				extractedDataRowsList.add(extractedDataRows);

				fileTemplateData.setFileType(fileType);
				fileTemplateData.setTemplateLines(fileTemplateLinesDTO);


				String delimeter="DELIMITER";
				int val=(int)delimiter;
				log.info("Delimiter Value: "+String.valueOf(val));
				LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId(delimeter,String.valueOf(val),tenantId);
				if(lookUpCode!=null && lookUpCode.getDescription()!=null)
				{
					fileTemplateData.setDelimiter(lookUpCode.getLookUpCode());
					fileTemplateData.setDelimeterDescription(lookUpCode.getDescription());
				}
				fileTemplateData.setListOfSampledataLines(listOfSampledataLines);
				fileTemplateData.setExtractedData(extractedDataRowsList);
				fileTemplateData.setData(listOfRows);
				fileTemplateData.setHeaders(headersList);
				if(fileTemplateData.getDelimiter() != null && !fileTemplateData.getDelimiter().isEmpty() && !fileTemplateData.getDelimiter().equals(""))
				{
					fileTemplateData.setStatus("Success");
				}else{
					fileTemplateData.setStatus("Failed");
				}
				log.info("Ascii value of Delimiter:"+fileTemplateData.getDelimiter());
			}
			catch(IOException f){
				f.printStackTrace();
			}
		}
		return fileTemplateData;
	}

	/********************************************
	 * To get the Sample data and Template header Info for Multiple Row Identifiers
	 *********************************************/
	@SuppressWarnings("unchecked")
	public FileTemplateDataDTO readingFileAndFetchingDataForMultipleRowIdentifiers(Character delimiter, 
			MultipartFile file, 
			int skipStartRow, 
			int skipEndRow,
			Long tenantId,
			String givenRowIdentifier,
			boolean multipleIdentifier,
			String criteria,
			int posBeging,
			int posEnd,
			String fileType)
			//List<HashMap<String, String>> multipleIdentifiersList) 
	{
		log.info("Fetching Sample Data of 10 Lines from "+file.getOriginalFilename());
		log.info("Delimiter: "+delimiter+",Given Row identifier: "+givenRowIdentifier+",MultipleIdentifier: "+multipleIdentifier+", skipStartRow: "+skipStartRow+", skipEndRow: "+skipEndRow);
		//		String fileName=file.getOriginalFilename();
		String[] line;
		List<String> headerColumnList = new ArrayList<String>();
		if(delimiter!=null && !delimiter.equals("") && givenRowIdentifier!=null && !givenRowIdentifier.equals(""))
		{
			List<Object> objList = null;
			List<String> headersList = new ArrayList<String>();
			FileTemplateDataDTO fileData = new FileTemplateDataDTO();
			SampleDataForFTDTO sampleDataDTOValues = new SampleDataForFTDTO();
			List<FileTemplateLinesDTO> fileTemp = new ArrayList<FileTemplateLinesDTO>();
			List<HashMap<String, String>> rows = new ArrayList<HashMap<String,String>>(); // To represent sample data of 10 rows based on headers
			List<HashMap<String, List<List<String[]>>>> extractedDataRows12 = new ArrayList<HashMap<String, List<List<String[]>>>>();
			HashMap<String, List<List<String[]>>> extractedDataRows = new HashMap<String, List<List<String[]>>>();
			List<String> listOfSampledataLines =new ArrayList<String>();


			fileData.setRowIdentifier(givenRowIdentifier);
			try 
			{
				InputStream inputStream = file.getInputStream();
				String readingline;
				//				String totalLindes;
				BufferedReader bufferReader = null;
				try 
				{
					bufferReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}	

				int count=1;
				int headerRow=-1;
				int tempValue = 0;
				String delimiterChar=delimiter.toString();

				/*** Here we are skipping the Start rows*/
				for(int i=0;i<skipStartRow;i++)
				{
					bufferReader.readLine();
				}

				String duplicateRowIdentifier=givenRowIdentifier;

				if(givenRowIdentifier!=null  && !givenRowIdentifier.equals("") && headerRow==-1)
				{
					JSONParser parser = new JSONParser();
					try {
						objList = (List<Object>) parser.parse(new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("jsonFile/dfrColumnIdentifier.json"))));
						log.info("Row Identifiers Size in Json File: "+objList.size());
						int k=0;
						for(Object obj : objList)
						{
							JSONObject jsonObject = (JSONObject) obj;
							String identifierTypeInJson = (String) jsonObject.get("identifierType");
							//log.info("recordIdentifier in Mi: "+recordIdentifier+" and givenRowIdentifier: "+givenRowIdentifier);

							if(givenRowIdentifier.length()>=7 && !identifierTypeInJson.startsWith("R"))
							{
								char ch=givenRowIdentifier.charAt(givenRowIdentifier.length()-1);
								if(Character.isLetter(ch) && (ch=='S' || ch=='D'))
								{
									givenRowIdentifier=givenRowIdentifier.substring(0, givenRowIdentifier.length()-1);
									//	rowIdentifier=	rowIdentifier.replace(rowIdentifier.substring(rowIdentifier.length()-1), "");
									log.info("RowIdentifier for Detail or Summary: "+givenRowIdentifier);
								}
							}
							else
								givenRowIdentifier=duplicateRowIdentifier;


							if(givenRowIdentifier.length()>=7 && givenRowIdentifier.charAt(0)!='R' || givenRowIdentifier.charAt(0)!='H')
							{
								String recordStartRow=identifierTypeInJson.substring(1);
								if(recordStartRow.equalsIgnoreCase(givenRowIdentifier))
								{
									log.info("recordStartRow in Mi: "+recordStartRow+" equals rowIdentifier: "+givenRowIdentifier);
									JSONArray columnNames = (JSONArray) jsonObject.get("columns");
									for(int i = 0; i < columnNames.size();i++) 
									{
										JSONObject innerObj = (JSONObject) columnNames.get(i);
										String colNum= (String) innerObj.get("columnNumber");
										String colHeader= (String) innerObj.get("columnHeader");
										String recordType= (String) innerObj.get("recordType");
										String dateFormat=null;
										if(innerObj.get("dateFormat")!=null)
										{
											dateFormat=(String) innerObj.get("dateFormat");
										}

										FileTemplateLinesDTO fileTempData= new FileTemplateLinesDTO();

										int num=k+1;
										fileTempData.setColumnNumber(num);
										if(k<9)
										{
											fileTempData.setMasterTableReferenceColumn("FIELD_0"+(num));
										}
										else{
											fileTempData.setMasterTableReferenceColumn("FIELD_"+(num));
										}
										fileTempData.setRecordTYpe(recordType);
										fileTempData.setRecordIdentifier(identifierTypeInJson);
										fileTempData.setColumnHeader(colHeader);
										fileTempData.setRecordStartRow(identifierTypeInJson);
										fileTempData.setDateFormat(dateFormat);
										headerColumnList.add(colHeader);

										//	log.info("columnNames.size(): "+columnNames.size()+" and i: "+i);
										if(i == (columnNames.size()-1))
										{
											fileTempData.setLastMasterTableRefCol(true);
											fileTempData.setLastColNumber(true);
										}
										fileTemp.add(fileTempData);
										log.info("===> key12: "+colNum+" Value: "+colHeader);
										k++;
									}
									headersList.addAll(headerColumnList);
								}
							}
							else if(identifierTypeInJson.equalsIgnoreCase(givenRowIdentifier))
							{
								JSONArray columnNames = (JSONArray) jsonObject.get("columns");
								for(int i = 0; i < columnNames.size();i++) 
								{
									JSONObject innerObj = (JSONObject) columnNames.get(i);
									String colNum= (String) innerObj.get("columnNumber");
									String colHeader= (String) innerObj.get("columnHeader");
									String recordType= (String) innerObj.get("recordType");
									String dateFormat=null;
									if(innerObj.get("dateFormat")!=null)
									{
										dateFormat=(String) innerObj.get("dateFormat");
									}

									FileTemplateLinesDTO fileTempData= new FileTemplateLinesDTO();

									int num=i+1;
									fileTempData.setColumnNumber(num);
									if(i<9)
									{
										fileTempData.setMasterTableReferenceColumn("FIELD_0"+(num));
									}
									else{
										fileTempData.setMasterTableReferenceColumn("FIELD_"+(num));
									}
									fileTempData.setRecordTYpe(recordType);
									fileTempData.setRecordIdentifier(identifierTypeInJson);
									fileTempData.setColumnHeader(colHeader);
									fileTempData.setRecordStartRow(identifierTypeInJson);
									fileTempData.setDateFormat(dateFormat);
									headersList.add(colHeader);

									//	log.info("columnNames.size(): "+columnNames.size()+" and i: "+i);
									if(i == (columnNames.size()-1))
									{
										fileTempData.setLastMasterTableRefCol(true);
										fileTempData.setLastColNumber(true);
									}


									fileTemp.add(fileTempData);

									//log.info("headerList: "+headersList);


									log.info("===> key: "+colNum+" Value: "+colHeader);
								}
							}
						}
					} catch (Exception e) 
					{
						e.printStackTrace();
					}
					headerRow=0;
				}
				else {
					log.info("Row Identifier is not given..!!");
				}

				while ((readingline = bufferReader.readLine())!=null && (tempValue)<11)  
				{

					String[] dataRowOrLine = null;
					if(delimiterChar.equals("|"))
					{
						dataRowOrLine = readingline.split("\\|");
					}
					else{
						dataRowOrLine = readingline.split(delimiterChar);
					}

					if(givenRowIdentifier.length()>7 && dataRowOrLine[0].startsWith("H"))// && sampleDataDTOValues.getSavingSampleData().size()==0)
					{
						char ch=givenRowIdentifier.charAt(givenRowIdentifier.length()-1);
						if(Character.isLetter(ch) && (ch=='S' || ch=='D'))
						{
							givenRowIdentifier=givenRowIdentifier.substring(0, givenRowIdentifier.length()-1);
							//log.info("RowIdentifier for Detail or Summary: "+givenRowIdentifier);
						}
					}
					else
						givenRowIdentifier=duplicateRowIdentifier;


					if(givenRowIdentifier!=null && !givenRowIdentifier.equals("") && givenRowIdentifier.length()>=7 && !givenRowIdentifier.equalsIgnoreCase(dataRowOrLine[0].substring(1)))
					{
						continue;
					}
					else if(givenRowIdentifier!=null && !givenRowIdentifier.equals("") &&!givenRowIdentifier.equalsIgnoreCase(dataRowOrLine[0].substring(1)))
					{
						continue;
					}
					else if(givenRowIdentifier!=null)
					{ 
						if((readingline.length()>0)&& rows.size()<=10)
						{

							log.info("Count Starting in Mi:"+count+" delimiterChar : "+delimiterChar+", givenRowIdentifier: "+givenRowIdentifier);
							List<String[]> rowData = new ArrayList<String[]>();
							List<List<String[]>> rowDataList = new ArrayList<List<String[]>>();
							HashMap<String, String> row = new HashMap<String, String>();

							if((line = readingline.split("\\|"))!=null && line != null && line.length> 0 && !(multipleIdentifier) && delimiterChar.equals("|") )
							{
								for(int f=0;f<fileTemp.size();f++)
								{
									String[] strArr = new String[2];
									/**Code to set the key value pairs of header and data to display*/
									String colName=fileTemp.get(f).getColumnHeader();

									strArr[0] = colName;
									/** To avoid ArrayIndex Out of bond exception*/
									if(f<line.length)
									{
										row.put(colName, line[f]); // row is used to set sample data in table
										strArr[1] = line[f];
									}
									rowData.add(strArr);
									rowDataList.add(rowData);
									extractedDataRows.put(givenRowIdentifier, rowDataList);
								}

							}
							else if(multipleIdentifier)
							{
								//log.info("RowIdentifier used to fetch Sample Data: "+givenRowIdentifier);
								if(objList!=null)
									sampleDataDTOValues=CreatingSampleDataNew(objList,readingline,givenRowIdentifier,criteria,posBeging,posEnd);
								extractedDataRows=sampleDataDTOValues.getExtractedDataRows();
								row=sampleDataDTOValues.getSampleData();

							}
							else{

								if((line = readingline.split(delimiterChar))!=null && line != null && line.length> 0)
								{
									for(int f=0;f<fileTemp.size();f++)
									{
										String[] strArr = new String[2];
										/**Code to set the key value pairs of header and data to display*/
										String colName=fileTemp.get(f).getColumnHeader();
										//log.info("Line obt"+Arrays.toString(line));
										strArr[0] = colName;
										/** To avoid ArrayIndex Out of bond exception*/
										if(f<line.length)
										{
											row.put(colName, line[f]);
											strArr[1] = line[f];
										}
										rowData.add(strArr);
									}
								}
							}

							if(extractedDataRows12.size()>1 && extractedDataRows12.get(1).containsKey("R"+givenRowIdentifier))
							{
								//								log.info("In If part:-");
								List<List<String[]>> linesList = new   ArrayList<List<String[]>>();
								linesList = extractedDataRows12.get(1).get("R"+givenRowIdentifier);
								linesList.addAll(extractedDataRows.get("R"+givenRowIdentifier));
								extractedDataRows12.get(1).put("R"+givenRowIdentifier, linesList);
							}
							else{
								log.info("In Else part:-");
								extractedDataRows12.add(extractedDataRows);
							}
							listOfSampledataLines.add(readingline);
							fileData.setListOfSampledataLines(listOfSampledataLines);
							rows.add(row);
							count=count+1;
						}
					}
					tempValue++;
				}
				if(inputStream!=null)
					inputStream.close();
				if(bufferReader!=null)
					bufferReader.close();
				fileData.setFileType(fileType);
				fileData.setTemplateLines(fileTemp);


				String delimeter="DELIMITER";
				int val=(int)delimiter;
				log.info("Delimiter val in MI,tenantId:-> "+String.valueOf(val)+" and "+tenantId);
				LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId(delimeter,String.valueOf(val),tenantId);
				if(lookUpCode!=null && lookUpCode.getDescription()!=null)
				{
					fileData.setDelimiter(lookUpCode.getLookUpCode());
					fileData.setDelimeterDescription(lookUpCode.getDescription());
				}
				fileData.setExtractedData(extractedDataRows12);
				fileData.setData(rows);
				fileData.setHeaders(headersList);
				if(fileData.getDelimiter() != null && !fileData.getDelimiter().isEmpty() && !fileData.getDelimiter().equals("") && givenRowIdentifier!=null)
				{
					fileData.setStatus("Success");
				}
				else{
					fileData.setStatus("Failed");
				}
				log.info("Ascii value of Delimiter in Mi:"+fileData.getDelimiter());
				return fileData;
			}
			catch (IOException e) {
				log.info("Exception Came heree");
				e.printStackTrace();
			}
		}
		log.info("Delimiter not found !! ");
		return null;
	}


	public List<String[]> settingValues(String[] line1, List<String> headers, String[] settingValues,  List<String[]> settingValuesList)
	{
		log.info("Size 01: "+line1.length+" and :"+headers.size());
		if(headers.size()>=line1.length)
		{
			for(int i=0;i<headers.size();i++)
			{
				settingValues=new String[2];
				try
				{
					settingValues[0] = headers.get(i);
					settingValues[1] = line1[i];
					//	log.info("headers.get(i): "+headers.get(i)+" line1[i]: "+line1[i]);
					settingValuesList.add(settingValues);
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					settingValues[0] = headers.get(i);
					settingValues[1] = "";
					//log.info("headers.get(i): "+headers.get(i)+" line1[i]: empty");
					settingValuesList.add(settingValues);

				}
			}
		}
		else{
			log.info("Header size less than line");
		}
		return settingValuesList;
	}


	public SampleDataForFTDTO simpleFormatedSampleDataHeader(List<List<String[]>> extractedDataRows,List<Object> objList,String rId,HashMap<String, List<List<String[]>>> newHmap,String[] line1, String[] settingValues,  List<String[]> settingValuesList)
	{
		log.info("----> Header RowID: "+rId);

		SampleDataForFTDTO sampleDataHeader = new SampleDataForFTDTO();
		settingValuesList=new ArrayList<String[]>();
		extractedDataRows = new ArrayList<List<String[]>>();
		String identifier="H"+rId;

		List<String> headers=new ArrayList<String>();

		// To get the list of header for dfr file
		for(int a=0;a<objList.size();a++)
		{
			Object obj=objList.get(a);
			JSONObject jsonObject = (JSONObject) obj;
			String recordIdentifier = (String) jsonObject.get("identifierType");
			JSONArray columnNames = (JSONArray) jsonObject.get("columns");
			if(identifier.equalsIgnoreCase(recordIdentifier))
			{
				for(int c=0;c<columnNames.size();c++)
				{
					JSONObject innerObj = (JSONObject) columnNames.get(c);
					String colHeader= (String) innerObj.get("columnHeader");
					headers.add(colHeader);
				}
			}
		}

		HashMap<String, String> row = new HashMap<String, String>();
		//headers=methodJson(objList,("H"+rId));
		//	log.info("headers.size(): "+headers.size()+" headers01: "+headers);
		//settingValuesList=settingValues(line1, headers,settingValues, settingValuesList);

		//log.info("headers.size(): "+headers.size()+" andline1.length: "+line1.length);
		// To Set the line values to the header columns as string[]
		if(headers.size()>=line1.length)
		{
			for(int i=0;i<headers.size();i++)
			{
				settingValues=new String[2];
				try
				{
					settingValues[0] = headers.get(i);
					settingValues[1] = line1[i];
					row.put(headers.get(i), line1[i]);
					//	log.info("headers.get(i): "+headers.get(i)+" line1[i]: "+line1[i]);
					settingValuesList.add(settingValues);
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					settingValues[0] = headers.get(i);
					settingValues[1] = "";
					row.put(headers.get(i), "");
					//log.info("headers.get(i): "+headers.get(i)+" line1[i]: empty");
					settingValuesList.add(settingValues);
				}
			}
		}
		else{
			log.info("Header size less than line");
		}
		extractedDataRows.add(settingValuesList);
		newHmap.put("H"+rId, extractedDataRows);
		//log.info("newHmap Header: "+newHmap);
		sampleDataHeader.setExtractedDataRows(newHmap);
		sampleDataHeader.setSampleData(row);
		return sampleDataHeader;
	}


	public SampleDataForFTDTO simpleFormatedSampleDataRows(List<List<String[]>> extractedDataRows,List<Object> objList,String rId,HashMap<String, List<List<String[]>>> newHmap,String[] line1, String[] settingValues,  List<String[]> settingValuesList)
	{
		//		log.info("Row RID: "+rId);
		SampleDataForFTDTO sampleDataHeader = new SampleDataForFTDTO();
		settingValuesList=new ArrayList<String[]>();
		extractedDataRows = new ArrayList<List<String[]>>();
		String identifier="R"+rId;

		List<String> headers=new ArrayList<String>();

		// To get the list of header for dfr file
		for(int a=0;a<objList.size();a++)
		{
			Object obj=objList.get(a);
			JSONObject jsonObject = (JSONObject) obj;
			String recordIdentifier = (String) jsonObject.get("identifierType");
			JSONArray columnNames = (JSONArray) jsonObject.get("columns");
			if(identifier.equalsIgnoreCase(recordIdentifier))
			{
				for(int c=0;c<columnNames.size();c++)
				{
					JSONObject innerObj = (JSONObject) columnNames.get(c);
					String colHeader= (String) innerObj.get("columnHeader");
					headers.add(colHeader);
				}
			}
		}

		HashMap<String, String> row = new HashMap<String, String>();
		//headers=methodJson(objList,("H"+rId));
		//	log.info("headers.size(): "+headers.size()+" headers01: "+headers);
		//settingValuesList=settingValues(line1, headers,settingValues, settingValuesList);

		// To Set the line values to the header columns as string[]
		//log.info("headers.size(): "+headers.size()+" andline1.length: "+line1.length);
		if(headers.size()>=line1.length)
		{
			for(int i=0;i<headers.size();i++)
			{
				settingValues=new String[2];
				try
				{
					settingValues[0] = headers.get(i);
					settingValues[1] = line1[i];
					row.put(headers.get(i), line1[i]);
					//log.info("headers.get(i): "+headers.get(i)+" line1[i]: "+line1[i]);
					settingValuesList.add(settingValues);
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					settingValues[0] = headers.get(i);
					settingValues[1] = "";
					row.put(headers.get(i), "");
					//	log.info("headers.get(i): "+headers.get(i)+" line1[i]: empty");
					settingValuesList.add(settingValues);
				}
			}
		}
		else{
			log.info("Header size less than line");
		}
		extractedDataRows.add(settingValuesList);
		newHmap.put("R"+rId, extractedDataRows);

		sampleDataHeader.setExtractedDataRows(newHmap);
		sampleDataHeader.setSampleData(row);
		return sampleDataHeader;

	}


	public SampleDataForFTDTO CreatingSampleDataNew( List<Object> objList,String line, String rId,String criteria,int posBeging,int posEnd) //throws JSONException
	{
		int count=1;
		String[] line1;
		SampleDataForFTDTO sampleData=new SampleDataForFTDTO();

		List<HashMap<String, List<List<String[]>>>> newHMApList = new ArrayList<HashMap<String,List<List<String[]>>>>(); // To present sample Data
		List<HashMap<String, String>> rows = new ArrayList<HashMap<String,String>>(); // To present sample data to save to table
		HashMap<String, List<List<String[]>>> newHmap = new HashMap<String, List<List<String[]>>>();
		HashMap<String, String> row = new HashMap<String,String>();
		List<List<String[]>> extractedDataRows = null;
		List<String[]> settingValuesList=null;
		String[] settingValue=null;
		SampleDataForFTDTO sampleDataHeader=null;
		SampleDataForFTDTO sampleDataRows=null;

		//		log.info("Row Identifier :"+rId+" Criteria :-"+criteria);
		if(criteria.equalsIgnoreCase("contains") && line.contains(rId+"|") )
		{
			line1=line.split("\\|");
			log.info("Array of Line in criteria->Contains :"+Arrays.toString(line1));
			count=count+1;
			if(line.contains("H"+rId))
			{
				sampleDataHeader=	simpleFormatedSampleDataHeader(extractedDataRows,objList,rId,newHmap,line1, settingValue, settingValuesList);
				newHmap=sampleDataHeader.getExtractedDataRows();
				row=sampleDataHeader.getSampleData();

			}
			else if(line.contains("R"+rId))
			{
				sampleDataRows=	simpleFormatedSampleDataRows(extractedDataRows,objList,rId,newHmap,line1, settingValue, settingValuesList);
				newHmap=sampleDataRows.getExtractedDataRows();
				row=sampleDataRows.getSampleData();
			}
			newHMApList.add(newHmap);
			rows.add(row);
		}
		else if(criteria.equalsIgnoreCase("position") )
		{
			if((posEnd!=0) &&((line.substring(posBeging-1, posEnd)).equalsIgnoreCase(rId)) )//|| ((line.substring(posBeging, posEnd)).equalsIgnoreCase("R"+rId))))
			{
				line1=line.split("\\|");
				log.info("Array of Line in criteria->Position :"+Arrays.toString(line1));
				count=count+1;
				if(posBeging!=0)
				{
					if(posBeging==1)
					{
						posBeging= posBeging-1;
					}
					else if(posBeging==2)
					{
						posBeging= posBeging-2;
					}
				}
				log.info("Position Begin: "+posBeging);
				if((line.substring((posBeging), posEnd)).equalsIgnoreCase("H"+rId))
				{
					sampleDataHeader=	simpleFormatedSampleDataHeader(extractedDataRows,objList,rId,newHmap,line1, settingValue, settingValuesList);
					newHmap=sampleDataHeader.getExtractedDataRows();
					row=sampleDataHeader.getSampleData();
				}
				else if((line.substring((posBeging), posEnd)).equalsIgnoreCase("R"+rId))
				{
					sampleDataRows=	simpleFormatedSampleDataRows(extractedDataRows,objList,rId,newHmap,line1, settingValue, settingValuesList);
					newHmap=sampleDataRows.getExtractedDataRows();
					row=sampleDataRows.getSampleData();
				}
			}
			newHMApList.add(newHmap);
			rows.add(row);
		}
		else if(criteria.equalsIgnoreCase("RECORD_START_ROW"))
		{
			line1=line.split("\\|");

			if((line1.length>0) && (line1[0].substring(1).equalsIgnoreCase(rId)  || (line1[0].substring(1).equalsIgnoreCase(rId))) )
			{
				line1=line.split("\\|");
				log.info("Array of Line in criteria->RECORD_START_ROW :"+Arrays.toString(line1));
				count=count+1;
				if(line1[0].equalsIgnoreCase("H"+rId))
				{
					sampleDataHeader=	simpleFormatedSampleDataHeader(extractedDataRows,objList,rId,newHmap,line1, settingValue, settingValuesList);
					newHmap=sampleDataHeader.getExtractedDataRows();
					row=sampleDataHeader.getSampleData();
				}
				else if(line1[0].equalsIgnoreCase("R"+rId))
				{
					sampleDataRows=	simpleFormatedSampleDataRows(extractedDataRows,objList,rId,newHmap,line1, settingValue, settingValuesList);
					newHmap=sampleDataRows.getExtractedDataRows();
					row=sampleDataRows.getSampleData();
				}
			}
			newHMApList.add(newHmap);
			rows.add(row);
		}
		//		log.info("newHMApList size: "+newHMApList.size());
		sampleData.setExtractedDataRows(newHmap);
		sampleData.setSampleData(row);
		sampleData.setExtractedDataRowsList(newHMApList);
		sampleData.setSavingSampleData(rows);
		return sampleData;
	}


	public String moveLinesFromStagingToMaster(@RequestParam Long srcFileInbId)
	{

		log.info("REST request to moveLinesFromStagingToMaster for srcFileInbId= "+srcFileInbId);

		String result="Failed Moving Data";
		String strMasterAndStagingInsertFields="tenantId, profileId, templateId, fileName, fileDate, lineContent";

		for(int in=1;in<=100;in++)
		{
			if(in<10)
				strMasterAndStagingInsertFields =strMasterAndStagingInsertFields+", field0"+in;
			else
				strMasterAndStagingInsertFields =strMasterAndStagingInsertFields+", field"+in;
		}
		strMasterAndStagingInsertFields=strMasterAndStagingInsertFields+", createdBy, createdDate, lastUpdatedBy, lastUpdatedDate, srcFileInbId";
		String dataMaster="DataMaster";
		String dataStaging="DataStaging";

		int distinctList=em.createQuery("insert into "+dataMaster+" ("+strMasterAndStagingInsertFields+") select "+strMasterAndStagingInsertFields +" from "+dataStaging+" where src_file_inb_id="+srcFileInbId).executeUpdate();// and template_id= "+te;);
		log.info("distinctList : "+distinctList);
		result="Success Moving Data";
		return result;
	}

	/***********************
	 * Read Excel File and to present Template lines and sample data
	 **********************/
	public FileTemplateDataDTO readExcelFile(ExcelFileReadingDTO fileReadDetails, String fileType) throws Exception
	{
		log.info("Service call to read Excel file");
		ArrayList<String> colNames = fileReadDetails.getColNames();
		//		ArrayList<String> colNames=new ArrayList<String>();
		//		colNames.add("Account");
		//		colNames.add("Account description");
		fileReadDetails.setUnMerge(true);

		MultipartFile file=null;
		//		String extension =null;
		InputStream inputStream = null;
		Workbook workbook =null;
		FileTemplateDataDTO fileTemplateData = new FileTemplateDataDTO();
		fileTemplateData.setStatus("Failed");
		List<String> headersList = new ArrayList<String>(); // To represent the header columns
		List<FileTemplateLinesDTO> fileTemp = new ArrayList<FileTemplateLinesDTO>();
		List<List<String[]>> extractedDataRowsForOthers = new ArrayList<List<String[]>>();

		if(fileReadDetails.getFilePath()!=null){
			file=fileReadDetails.getFilePath();
			inputStream=file.getInputStream();
		}
		else{
			return fileTemplateData;
		}
		//		if(file!=null)
		//			extension = FilenameUtils.getExtension(file.getOriginalFilename());
		//		if(colNames!=null && colNames.size()<1  || (fileReadDetails.getEndConditionsList()!=null && fileReadDetails.getEndConditionsList().size()<1) || !((extension!=null) && (extension.equalsIgnoreCase(".xls")|| extension.equalsIgnoreCase(".xlsx"))))
		////			return "Failed";
		//		return null;

		if(inputStream!=null)
			workbook = WorkbookFactory.create(inputStream);		// Reading excel file from path we can update it
		else{
			return fileTemplateData;
		}
		Sheet sheet = workbook.getSheetAt(0);														// creating first sheet object
		DataFormatter dataFormatter = new DataFormatter();											//DataFormatter to format and get each cell's value as String
		if(fileReadDetails.getUnMerge() !=null&&fileReadDetails.getUnMerge()==true){				// Unmerging cells based on condition	
			for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
				sheet.removeMergedRegion(i);
			}
		}
		ArrayList<ArrayList<String>> finalList = new ArrayList<ArrayList<String>>();
		ArrayList<String> colsList=new ArrayList<String>();
		Boolean readFlag=false, isSkipRow=false, endFlag=false;
		//for(int i=0, rowsLen=sheet.getPhysicalNumberOfRows(); i<rowsLen;i++)			// To loop through all rows in used range
		for(int i=0, rowsLen=sheet.getPhysicalNumberOfRows(); i<rowsLen;i++)
		{
			org.apache.poi.ss.usermodel.Row row=sheet.getRow(i);
			if(readFlag==false){
				int k=0;
				for(;k<colNames.size();k++){														// Checking is current row columns row or not
					if(!dataFormatter.formatCellValue(row.getCell(k)).equalsIgnoreCase(colNames.get(k))){
						break;
					}
				}
				if(k==colNames.size()){
					if(colsList.size()==0){															// If columns row occured for first time 
						for(int j=0, colsLen=row.getLastCellNum();j<colsLen;j++){
							if(row.getCell(j).getCellType() == Cell.CELL_TYPE_BLANK){				// If column row cell value is blank
								if(j==0){															// If empty cell is first cell
									colsList.add("Unknown");
									continue;
								}else{
									colsList.add(colsList.get(j-1)+"_1");
									continue;
								}
							}
							String val=dataFormatter.formatCellValue(row.getCell(j));				// Format cell value to string
							colsList.add(val);
						}
						finalList.add(colsList);
					}
					readFlag=true;
					continue;
				}
			}
			if(fileReadDetails.getEndConditionsList()!=null)
			{
				for(Map<String,String> eachEndConditionMap: fileReadDetails.getEndConditionsList()){
					endFlag=false;
					switch (eachEndConditionMap.get("operator").toString()) {
					case "CONTAINS":
						if ((dataFormatter.formatCellValue(row.getCell(colsList.indexOf(eachEndConditionMap.get("columnName")))).replaceAll("\'", "")).contains(eachEndConditionMap.get("value").toString().replaceAll("\'", ""))) {
							endFlag=true;
						}
						break;
					case "BEGINS_WITH":
						if ((dataFormatter.formatCellValue(row.getCell(colsList.indexOf(eachEndConditionMap.get("columnName")))).replaceAll("\'", "")).startsWith(eachEndConditionMap.get("value").toString().replaceAll("\'", ""))) {
							endFlag=true;
						}
						break;
					case "ENDS_WITH":
						if ((dataFormatter.formatCellValue(row.getCell(colsList.indexOf(eachEndConditionMap.get("columnName")))).replaceAll("\'", "")).endsWith(eachEndConditionMap.get("value").toString().replaceAll("\'", ""))) {
							endFlag=true;
						}
						break;
					case "EQUALS":
						if ((dataFormatter.formatCellValue(row.getCell(colsList.indexOf(eachEndConditionMap.get("columnName")))).replaceAll("\'", "")).equals(eachEndConditionMap.get("value").toString().replaceAll("\'", ""))) {
							endFlag=true;
						}
						break;
					case "NOT_EQUALS":
						if ((!(dataFormatter.formatCellValue(row.getCell(colsList.indexOf(eachEndConditionMap.get("columnName")))).replaceAll("\'", "")).equals(eachEndConditionMap.get("value").toString().replaceAll("\'", "")))) {
							endFlag=true;
						}
						break;
					case "IN":
						List valuesList = Arrays.asList((eachEndConditionMap.get("value").toString()).split(","));
						if (valuesList.contains(row.getCell(colsList.indexOf(eachEndConditionMap.get("columnName"))))) {
							endFlag=true;
						}
						break;
					}
					if((eachEndConditionMap.get("logicalOperator") != null && eachEndConditionMap.get("logicalOperator").toString().equals("OR")&&endFlag==true) || (eachEndConditionMap.get("logicalOperator") != null && eachEndConditionMap.get("logicalOperator").toString().equals("AND")&&endFlag==false))
						break;
				}
			}
			if(readFlag==false || endFlag==true){
				readFlag=false;
				continue;
			}
			if(fileReadDetails.getSkipConditions()!=null)
			{
				for(Map<String,String> eachMap: fileReadDetails.getSkipConditions()){				// To check current row eligible to skip or not
					if (colsList.contains(eachMap.get("columnName"))) {
						isSkipRow=false;
						switch (eachMap.get("operator").toString()) {
						case "CONTAINS":
							if ((dataFormatter.formatCellValue(row.getCell(colsList.indexOf(eachMap.get("columnName")))).replaceAll("\'", "")).contains(eachMap.get("value").toString().replaceAll("\'", ""))) {
								isSkipRow=true;
							}
							break;
						case "BEGINS_WITH":
							if ((dataFormatter.formatCellValue(row.getCell(colsList.indexOf(eachMap.get("columnName")))).replaceAll("\'", "")).startsWith(eachMap.get("value").toString().replaceAll("\'", ""))) {
								isSkipRow=true;
							}
							break;
						case "ENDS_WITH":
							if ((dataFormatter.formatCellValue(row.getCell(colsList.indexOf(eachMap.get("columnName")))).replaceAll("\'", "")).endsWith(eachMap.get("value").toString().replaceAll("\'", ""))) {
								isSkipRow=true;
							}
							break;
						case "EQUALS":
							if ((dataFormatter.formatCellValue(row.getCell(colsList.indexOf(eachMap.get("columnName")))).replaceAll("\'", "")).equals(eachMap.get("value").toString().replaceAll("\'", ""))) {
								isSkipRow=true;
							}
							break;
						case "NOT_EQUALS":
							if ((!(dataFormatter.formatCellValue(row.getCell(colsList.indexOf(eachMap.get("columnName")))).replaceAll("\'", "")).equals(eachMap.get("value").toString().replaceAll("\'", "")))) {
								isSkipRow=true;
							}
							break;
						case "IN":
							List valuesList = Arrays.asList((eachMap.get("value").toString()).split(","));
							if (valuesList.contains(row.getCell(colsList.indexOf(eachMap.get("columnName"))))) {
								isSkipRow=true;
							}
							break;
						}
						if((eachMap.get("logicalOperator") != null && eachMap.get("logicalOperator").toString().equals("OR")&&isSkipRow==true)||(eachMap.get("logicalOperator") != null && eachMap.get("logicalOperator").toString().equals("AND")&&isSkipRow==false))
							break;
					}
					if(isSkipRow)
						break;
				}
			}
			if(isSkipRow)
				continue;
			ArrayList<String> eachRow=new ArrayList<String>();
			for(int j=0, colsLen=row.getLastCellNum();j<colsLen;j++){								// Read all cell values of current row in to one arraylist
				eachRow.add(dataFormatter.formatCellValue(row.getCell(j)));
			}
			finalList.add(eachRow);
			if(finalList.size() > 25)
				break;
		}
		log.info("finalList.size(): "+finalList.size());
		if(finalList.size()>0)
		{
			String headerRow = String.join(",", finalList.get(0));
			String[] headerLine = null;

			headerLine = headerRow.split(",");

			log.info("headerLine:"+Arrays.toString(headerLine));
			fileTemplateData.setLastLineNumber(headerLine.length);
			log.info("headerLine length :"+headerLine.length);
			for(int headerIndx=0;headerIndx<headerLine.length;headerIndx++)
			{
				FileTemplateLinesDTO fileTempData= new FileTemplateLinesDTO();
				int num=headerIndx+1;
				fileTempData.setColumnNumber(num);
				fileTempData.setLineNumber(num);
				if(headerIndx<9)
				{
					fileTempData.setMasterTableReferenceColumn("FIELD_0"+(num));
				}
				else{
					fileTempData.setMasterTableReferenceColumn("FIELD_"+(num));
				}
				fileTempData.setColumnHeader(headerLine[headerIndx]);
				headersList.add(headerLine[headerIndx]);
				if(headerIndx == headerLine.length-1 )
				{
					fileTempData.setLastMasterTableRefCol(true);
					fileTempData.setLastColNumber(true);
				}
				//fileTempData.setColumnNumber(num);
				//fileTempData.setEdit(true);
				fileTemp.add(fileTempData);
			}
			log.info("headerList: "+headersList);
			//			headerRow=0;
			fileTemplateData.setFileType(fileType);
			fileTemplateData.setTemplateLines(fileTemp);
			fileTemplateData.setStatus("Success");
			fileTemplateData.setHeaders(headersList);

		}


		String[] line;
		List<HashMap<String, List<List<String[]>>>> extractedDataRows12 = new ArrayList<HashMap<String, List<List<String[]>>>>();
		HashMap<String, List<List<String[]>>> extractedDataRows = new HashMap<String, List<List<String[]>>>();
		List<HashMap<String, String>> rows = new ArrayList<HashMap<String,String>>(); // To repersent sample data of 10 rows based on headers
		List<String> listOfSampledataLines =new ArrayList<String>();
		for(int sampleLineIndex=1;sampleLineIndex<finalList.size();sampleLineIndex++)
		{
			List<String[]> rowData = new ArrayList<String[]>();
			HashMap<String, String> row = new HashMap<String, String>();
			for(int f=0;f<fileTemp.size();f++)
			{
				String[] strArr = new String[2];
				/**Code to set the key value pairs of header and data to display*/
				String colName=fileTemp.get(f).getColumnHeader();
				line=finalList.get(sampleLineIndex).toArray(new String[finalList.get(sampleLineIndex).size()]);//finalList.get(g);

				if(line != null && line.length> 0)
				{
					//log.info("Line obt"+Arrays.toString(line));
					strArr[0] = colName;
					/** To avoid ArrayIndex Out of bond exception*/
					if(f<line.length)
					{
						row.put(colName, line[f]);
						strArr[1] = line[f];
					}
					rowData.add(strArr);
				}
			}
			extractedDataRowsForOthers.add(rowData);
			String strline = String.join(",", finalList.get(sampleLineIndex));
			listOfSampledataLines.add(strline);
			fileTemplateData.setListOfSampledataLines(listOfSampledataLines);
			rows.add(row);
			if(sampleLineIndex>=10)
			{
				break;
			}

		}
		extractedDataRows.put("", extractedDataRowsForOthers);
		extractedDataRows12.add(extractedDataRows);
		fileTemplateData.setExtractedData(extractedDataRows12);
		fileTemplateData.setData(rows);
		if(inputStream!=null)
			inputStream.close();
		return fileTemplateData;
	}


	public  String[] SplitDataLineUsingDelimiter(String delimiter, String dataLine, String en)
	{
		String[] arrayOfDataLine;
		//		log.info("dataLine: "+dataLine+" and split: "+Arrays.toString(dataLine.split(","))+" and len: "+dataLine.split(",").length);

		if(delimiter.equals(","))
		{
			arrayOfDataLine=dataLine.split(",(?=([^"+en+"]*"+en+"[^"+en+"]*"+en+")*[^"+en+"]*$)",-1);
			//System.out.println("Array of lIne01: "+Arrays.toString(line1));
		}
		else if(delimiter.equals("|"))
		{
			arrayOfDataLine=dataLine.split("\\|(?=([^"+en+"]*"+en+"[^"+en+"]*"+en+")*[^"+en+"]*$)",-1);
			//System.out.println("Array of lIne001: "+Arrays.toString(line1));
		}
		else if(delimiter.equals("	"))
		{
			if(dataLine!=null && dataLine.contains("\""))
			{
				long charCount = dataLine.chars().filter(ch -> ch == '\"').count();
				if(charCount>0 && (charCount<=1 || charCount % 2 != 0))
					dataLine=dataLine.replace("\"", "");
			}
			arrayOfDataLine=dataLine.split("\"?(	|$)(?=([^"+en+"]*"+en+"[^"+en+"]*"+en+")*[^"+en+"]*$)",-1);
			//System.out.println("Array of lIne0001: "+Arrays.toString(line1));
		}
		else if(delimiter.equals(" "))
		{
			arrayOfDataLine=dataLine.split("\"?( |$)(?=([^"+en+"]*"+en+"[^"+en+"]*"+en+")*[^"+en+"]*$)",-1);
			//System.out.println("Array of lIne0001: "+Arrays.toString(line1));
		}
		else if(delimiter.equals(";"))
		{
			arrayOfDataLine=dataLine.split(";(?=([^"+en+"]*"+en+"[^"+en+"]*"+en+")*[^"+en+"]*$)",-1);
			//System.out.println("Array of lIne0001: "+Arrays.toString(line1));
		}
		else{
			arrayOfDataLine=dataLine.split(delimiter);
		}
		//log.info("arrayOfDataLine len: "+arrayOfDataLine.length+" arrayOfDataLine: "+Arrays.toString(arrayOfDataLine));
		return arrayOfDataLine;
	}


	private String convertObjectArrayToString(Object[] arr, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : arr){
			if(obj!=null)
				sb.append(obj.toString()).append(delimiter);
			else
				sb.append("").append(delimiter);}
		return sb.substring(0, sb.length() - 1);
	}


	public HashMap<Integer, Integer> getOccuranceOfDelimiters(String lineData)
	{
		List<Integer> listInt = toGetIndexNumbers(lineData); 
		for(int k=0;k<(listInt.size()/2);k++)
		{
			List<Integer> listIntInside=toGetIndexNumbers(lineData);
			int start =listIntInside.get(0);
			int end = (listIntInside.get(1)+1);

			StringBuffer buf = new StringBuffer(lineData);
			String afterRemoving = buf.replace(start, end, "").toString(); 
			lineData = afterRemoving;
		}
		HashMap<Integer, Integer> occerance = toFindDelimiter(lineData);
		return occerance;
	}


	/*********************
	 * To fetch the sample data when refreshed
	 *********************/
	public List<FileTemplateDataDTO> refreshSampleData(List<FileTemplateDataDTO> fileTemplateDataDTOList, String delimiter, Long tenantId)
	{
		List<FileTemplateDataDTO> fileTemplateDataDTOListNew=new ArrayList<FileTemplateDataDTO>();

		//		List<HashMap<String, String>> listOfRows = new ArrayList<HashMap<String,String>>(); // To repersent sample data of 10 rows based on headers

		if(delimiter!=null)
		{
			//			int asciiOfDelimiter=0;
			//			try{
			//				asciiOfDelimiter=Integer.valueOf(delimiter);}
			//			catch (NumberFormatException e) 
			//			{
			//				log.info("NumberFormatException for asciiOfDelimiter: " + e.getMessage());
			//				return fileTemplateDataDTOList;
			//			}
			int a=Integer.valueOf(delimiter);
			char delimiterChar = (char) a;
			delimiter=String.valueOf(delimiterChar);
		}
		else
			delimiter=",";

		for(int indx_k=0;indx_k<fileTemplateDataDTOList.size();indx_k++)
		{
			List<String> listOfSampledataLines=new ArrayList<String>();
			List<HashMap<String, String>> listOfRows = new ArrayList<HashMap<String,String>>(); // To repersent sample data of 10 rows based on headers
			log.info("********** To Refresh Sample Data Method Size "+fileTemplateDataDTOList.size()+", delimiter: "+delimiter);
			FileTemplateDataDTO fileTemplateDataDTO = fileTemplateDataDTOList.get(indx_k); 
			log.info("FileTemplateDataDTO File type: "+fileTemplateDataDTO.getFileType());
			String enclosedChar="\"",rowidentifier="";


			//			if(delimiter!=null)
			//			{
			//				int asciiOfDelimiter=0;
			//				try{
			//					asciiOfDelimiter=Integer.valueOf(delimiter);}
			//				catch (NumberFormatException e) 
			//				{
			//					log.info("NumberFormatException for asciiOfDelimiter: " + e.getMessage());
			//					return fileTemplateDataDTOList;
			//				}
			//				char delimiterChar = (char) asciiOfDelimiter;
			//				 delimiter=String.valueOf(delimiter);
			//			}
			//			else
			//				delimiter=",";
			//			if(fileTemplateDataDTO.getDelimiter()!=null) 
			//			{
			//				int delimiterVal = Integer.valueOf(fileTemplateDataDTO.getDelimiter());
			//				char digit=(char) delimiterVal;
			//				delimiter = Character.toString(digit);
			//			}

			Boolean headerRow=false;
			//			if(delimiter.equals("|"))
			//				delimiter="\\|";

			if(fileTemplateDataDTO.getRowIdentifier()!=null && !fileTemplateDataDTO.getRowIdentifier().isEmpty() && !fileTemplateDataDTO.getRowIdentifier().equals("")){
				rowidentifier=fileTemplateDataDTO.getRowIdentifier();
			}

			List<String> sampleDataLines= fileTemplateDataDTO.getListOfSampledataLines();
			List<List<String[]>> extractedDataRowsForOthers = new ArrayList<List<String[]>>();
			List<FileTemplateLinesDTO> templatesLinesDto=fileTemplateDataDTO.getTemplateLines();
			List<HashMap<String, List<List<String[]>>>> extractedDataRowsList = new ArrayList<HashMap<String, List<List<String[]>>>>();
			int loopSize=templatesLinesDto.size(), Hlength=0;
			List<String[]> arrayOfLineList = new ArrayList<String[]>();

			/*********************************************
			 * To Get String Array based on Enclosed Character (if given)
			 *********************************************/
			loop_1:for(int indx_r=0;indx_r<loopSize;indx_r++)
			{
				if(templatesLinesDto.get(indx_r).getEnclosedChar()!=null)
				{
					enclosedChar=templatesLinesDto.get(indx_r).getEnclosedChar();
					break loop_1;
				}
			}
			log.info("Delimiter: '"+delimiter+"', rowidentifier- '"+rowidentifier+"', enclosedChar: '"+enclosedChar+"', loopSize: "+loopSize);
			for(String duplicateSampleLine:sampleDataLines)
			{
				String[] arrayOfLine=SplitDataLineUsingDelimiter(delimiter,duplicateSampleLine,enclosedChar);
				arrayOfLineList.add(arrayOfLine);
			}

			log.info("arrayOfLineList Size: "+arrayOfLineList.size());
			for(int indx_i=0;indx_i<arrayOfLineList.size();indx_i++)
			{
				HashMap<String, String> row = new HashMap<String, String>();
				String[] arrayOfLine=arrayOfLineList.get(indx_i);//SplitDataLineUsingDelimiter(delimiter,sampleLine,"\"");
				//				log.info(indx_i+" arrayOfLine: "+Arrays.toString(arrayOfLine));
				int indx_r1=0;
				String dateFrmt=null, timeFrmt=null,zeroFill=null;//,skipCol=null;

				if(arrayOfLine.length>0 && arrayOfLine[0].startsWith("H") && (rowidentifier!="" || fileTemplateDataDTO.getMultipleIdentifierFlag()))
				{
					loopSize=arrayOfLine.length;
					Hlength=arrayOfLine.length;
					headerRow=true;
				}
				else if(arrayOfLine.length>0 && arrayOfLine[0].startsWith("R") && (rowidentifier!="" || fileTemplateDataDTO.getMultipleIdentifierFlag()))
				{
					indx_r1=Hlength;
					loopSize=Hlength+arrayOfLine.length;
				}
				//				log.info(indx_r1+" loopSize: "+loopSize);
				List<String[]> rowData = new ArrayList<String[]>();

				for(int indx_r=0;indx_r<arrayOfLine.length && indx_r1<loopSize;indx_r++,indx_r1++)
				{
					FileTemplateLinesDTO fileTemplateLine=templatesLinesDto.get(indx_r1);
					//										log.info("arrayOfLine: "+Arrays.toString(arrayOfLine));
					String fieldValue=arrayOfLine[indx_r];
					//					log.info("fieldValue: "+fieldValue);

					if(fileTemplateLine.getDateFormat()!=null){
						dateFrmt=fileTemplateLine.getDateFormat();
						if(dateFrmt!=null)
						{
							try{

								if(dateFrmt.length()==4 && fieldValue.length()==4)
								{
									SimpleDateFormat sdfSource = new SimpleDateFormat(dateFrmt);
									Date date = sdfSource.parse(fieldValue);
									SimpleDateFormat sdfDestination = new SimpleDateFormat("MM-dd");
									fieldValue = sdfDestination.format(date);
									log.info("fieldValue in date: "+fieldValue);
								}
								else{
									SimpleDateFormat sdfSource = new SimpleDateFormat(dateFrmt);
									Date date = sdfSource.parse(fieldValue);
									SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");
									fieldValue = sdfDestination.format(date);
									log.info("fieldValue in date: "+fieldValue);
								}
							}
							catch(java.text.ParseException pe){
								System.out.println("Parse Exception : " + pe);
							}
						}
					}
					if(fileTemplateLine.getTimeFormat()!=null){
						timeFrmt=fileTemplateLine.getTimeFormat();
						if(timeFrmt!=null){
							try{
								SimpleDateFormat sdfSource = new SimpleDateFormat(timeFrmt);
								Date date = sdfSource.parse(fieldValue);
								SimpleDateFormat sdfDestination = new SimpleDateFormat("hh:mm:ss");
								fieldValue = sdfDestination.format(date);
								log.info("fieldValue in time: "+fieldValue);
							}
							catch(java.text.ParseException pe){
								pe.printStackTrace();
							}
						}
					}
					if(fileTemplateLine.getZeroFill()!=null)
					{
						zeroFill=fileTemplateLine.getZeroFill();
						log.info(indx_r+" ZeroFill Value: "+fieldValue);
						for(int i=0;i<(Integer.valueOf(zeroFill));i++)
						{
							fieldValue="0"+fieldValue;
						}	
						log.info("fieldValue in zeroFill: "+fieldValue);

					}
					if(fileTemplateLine.getSkipColumn()!=null)
					{
						//						log.info(indx_r+" Skip Column Yes "+fieldValue);
						//skipCol=fileTemplateLine.getSkipColumn();
						fieldValue="";
						log.info("fieldValue in skipCol: "+fieldValue);

					}
					arrayOfLine[indx_r]=fieldValue;
					//					log.info("End ArrayOfLine: "+Arrays.toString(arrayOfLine));

					//					HashMap<String, String> row = new HashMap<String, String>();
					//					List<String[]> 
					//					rowData = new ArrayList<String[]>();

					String[] strArr = new String[2];
					String colName=templatesLinesDto.get(indx_r1).getColumnHeader();

					strArr[0] = colName;
					/** To avoid ArrayIndex Out of bond exception*/
					if(indx_r<arrayOfLine.length)
					{
						row.put(colName, arrayOfLine[indx_r]);
						strArr[1] = arrayOfLine[indx_r];
					}
					//					log.info("strArr: "+Arrays.toString(strArr));
					rowData.add(strArr);
					//extractedDataRowsForOthers.add(rowData);

				}
				listOfRows.add(row);
				//				if(delimiter.equals("\\|")){
				//					delimiter="|";
				//				}

				String line=convertObjectArrayToString(arrayOfLine, delimiter);
				listOfSampledataLines.add(line);
				extractedDataRowsForOthers.add(rowData);
				//				log.info("ArrayOfLine: "+Arrays.toString(arrayOfLine));
			}

			HashMap<String, List<List<String[]>>> extractedDataRows = new HashMap<String, List<List<String[]>>>();

			//			HashSet<List<String[]>> hashset = new HashSet<List<String[]>>();
			//			hashset.addAll(extractedDataRowsForOthers);
			//			extractedDataRowsForOthers.clear();
			//			extractedDataRowsForOthers.addAll(hashset);


			log.info("extractedDataRowsForOthers Size: "+extractedDataRowsForOthers.size());
			if(extractedDataRowsForOthers.size()>0)
			{
				if(headerRow)
				{
					List<List<String[]>> firstLineList=new ArrayList<List<String[]>>();
					for(int indx_a=0;indx_a<1;indx_a++)
					{
						List<String[]> firstLine=extractedDataRowsForOthers.get(indx_a);
						firstLineList.add(firstLine);
						extractedDataRowsForOthers.remove(indx_a);
					}
					log.info("firstLineList: "+firstLineList.size()+" and extractedDataRowsForOthers: "+extractedDataRowsForOthers.size());
					if(firstLineList!=null && firstLineList.size()>0){
						HashMap<String, List<List<String[]>>> extractedDataRows1 = new HashMap<String, List<List<String[]>>>();
						extractedDataRows1.put("H"+rowidentifier, firstLineList);
						extractedDataRowsList.add(extractedDataRows1);
					}
					if(extractedDataRowsForOthers.size()>0){
						extractedDataRows.put("R"+rowidentifier, extractedDataRowsForOthers);
					}
				}
				else if(rowidentifier!=""){
					extractedDataRows.put(rowidentifier, extractedDataRowsForOthers);
				}
				else{
					extractedDataRows.put(rowidentifier, extractedDataRowsForOthers);
				}
				extractedDataRowsList.add(extractedDataRows);
				fileTemplateDataDTO.setData(listOfRows);
				fileTemplateDataDTO.setExtractedData(extractedDataRowsList);

				String delimeter="DELIMITER";
				int val=(int)delimiter.charAt(0);
				log.info("Delimiter val in MI,tenantId:-> "+String.valueOf(val)+" and "+tenantId);
				LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId(delimeter,String.valueOf(val),tenantId);
				if(lookUpCode!=null && lookUpCode.getDescription()!=null)
				{
					fileTemplateDataDTO.setDelimiter(lookUpCode.getLookUpCode());
					fileTemplateDataDTO.setDelimeterDescription(lookUpCode.getDescription());
				}
			}
			if(listOfSampledataLines!=null){
				fileTemplateDataDTO.setListOfSampledataLines(listOfSampledataLines);
			}
			//fileTemplateDataDTOList.remove(indx_k);
			fileTemplateDataDTOListNew.add(fileTemplateDataDTO);
		}
		if(fileTemplateDataDTOListNew.size()>0)
			log.info("Extracted Data Size: "+fileTemplateDataDTOListNew.get(0).getExtractedData().size());
		return fileTemplateDataDTOListNew;
	}
}
