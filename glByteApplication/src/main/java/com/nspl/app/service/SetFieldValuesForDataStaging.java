package com.nspl.app.service;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

//import OracleBia2Functions.*;








import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import OracleBia2Functions.*;






import com.nspl.app.domain.DataStaging;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.FileTemplates;
import com.nspl.app.domain.SourceProfileFileAssignments;
import com.nspl.app.repository.DataStagingRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.FileTemplatesRepository;

@Service
@Transactional
public class SetFieldValuesForDataStaging {

	private final Logger log = LoggerFactory.getLogger(SetFieldValuesForDataStaging.class);

	@Inject
	DataStagingRepository dataStagingRepository;
	
	@Inject
	Bia2FunctionsService bia2FunctionsService;


	@Inject
	FileTemplatesRepository fileTemplatesRepository;

	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;
	@Inject
	PropertiesUtilService propertiesUtilService;

	/**
	 * Author Kiran
	 * @param line
	 * To set the field values in dataStagging table
	 */

	public DataStaging DataStagingFields(String[] line, Long templateId, SourceProfileFileAssignments spfaDetails, List<FileTemplateLines> fileTemplateLines, Long tenantId)
	{
		if(line!=null)
		{
			//SavingFilesDataDTO savingData = new SavingFilesDataDTO();
			
			DataStaging Dtdata = new DataStaging();
			if(fileTemplateLines!=null && fileTemplateLines.size()>0)
			{
				if(line.length>1)
				{
					//log.info("4(4)(2)LoopStart to add data to object based file template lines: "+ZonedDateTime.now());
					for(int k=0;(k<fileTemplateLines.size() && k<line.length);k++)
					{
						FileTemplateLines fileTempLine=fileTemplateLines.get(k);
						int ColNumber = (fileTempLine.getColumnNumber()-1);

						String recColName= fileTempLine.getMasterTableReferenceColumn();
						
						String value=null;
						if(ColNumber<line.length)
							value=line[ColNumber];
						
						
						/*String strDate=value;
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
						try {
							Date varDate=dateFormat.parse(strDate);
							dateFormat=new SimpleDateFormat("yyyy-MM-dd");
							log.info("Date :"+dateFormat.format(varDate));
							value=dateFormat.format(varDate);
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}*/
						
						/**
						 * Code to save data based on date*/
						
						String dateFrmt=null;
						if(fileTempLine.getDateFormat()!=null)
						{
							Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
							String dateFrmtToSave = props.getProperty("applicationDateFormat");
							
							dateFrmt=fileTempLine.getDateFormat();
							//log.info("Given date format :"+dateFrmt);
							
							SimpleDateFormat dateFormat = new SimpleDateFormat(dateFrmt);
							try {
								Date varDate=dateFormat.parse(value);
								dateFormat=new SimpleDateFormat(dateFrmtToSave.toString());
								//log.info("Date :"+dateFormat.format(varDate));
								value=dateFormat.format(varDate);
								//log.info("After date formatted: "+value);
							}catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
						/**
						 * Code to save data using enclosed char in the field or data
						 */
						
						String enclosedChar=null;
						if(fileTempLine.getEnclosedChar()!=null)
						{
							enclosedChar=fileTempLine.getEnclosedChar();
							if(value.contains(enclosedChar))
							{
								value = value.replaceAll(enclosedChar, "");
								log.info("value: "+value);
							}
						}
						
						
						
						// Working code (jar file required)
						String formula = fileTempLine.getFormula();
						if(formula!=null)
						{
							value = bia2FunctionsService.callFunction(formula, value);
						}

						String skipColumn = fileTempLine.getSkipColumn();
						if(skipColumn!=null && skipColumn.equalsIgnoreCase("yes"))
						{
							log.info("Skip Column value:"+skipColumn);
							continue;
						}
						else
						{
							switch(recColName)
							{
							case "FIELD_01" : Dtdata.setField01(value); break;
							case "FIELD_02" : Dtdata.setField02(value); break;
							case "FIELD_03" : Dtdata.setField03(value); break;
							case "FIELD_04" : Dtdata.setField04(value); break;
							case "FIELD_05" : Dtdata.setField05(value); break;
							case "FIELD_06" : Dtdata.setField06(value); break;
							case "FIELD_07" : Dtdata.setField07(value); break;
							case "FIELD_08" : Dtdata.setField08(value); break;
							case "FIELD_09" : Dtdata.setField09(value); break;
							case "FIELD_10" : Dtdata.setField10(value); break;
							case "FIELD_11" : Dtdata.setField11(value); break;
							case "FIELD_12" : Dtdata.setField12(value); break;
							case "FIELD_13" : Dtdata.setField13(value); break;
							case "FIELD_14" : Dtdata.setField14(value); break;
							case "FIELD_15" : Dtdata.setField15(value); break;
							case "FIELD_16" : Dtdata.setField16(value); break;
							case "FIELD_17" : Dtdata.setField17(value); break;
							case "FIELD_18" : Dtdata.setField18(value); break;
							case "FIELD_19" : Dtdata.setField19(value); break;
							case "FIELD_20" : Dtdata.setField20(value); break;
							case "FIELD_21" : Dtdata.setField21(value); break;
							case "FIELD_22" : Dtdata.setField22(value); break;
							case "FIELD_23" : Dtdata.setField23(value); break;
							case "FIELD_24" : Dtdata.setField24(value); break;
							case "FIELD_25" : Dtdata.setField25(value); break;
							case "FIELD_26" : Dtdata.setField26(value); break;
							case "FIELD_27" : Dtdata.setField27(value); break;
							case "FIELD_28" : Dtdata.setField28(value); break;
							case "FIELD_29" : Dtdata.setField29(value); break;
							case "FIELD_30" : Dtdata.setField30(value); break;
							case "FIELD_31" : Dtdata.setField31(value); break;
							case "FIELD_32" : Dtdata.setField32(value); break;
							case "FIELD_33" : Dtdata.setField33(value); break;
							case "FIELD_34" : Dtdata.setField34(value); break;
							case "FIELD_35" : Dtdata.setField35(value); break;
							case "FIELD_36" : Dtdata.setField36(value); break;
							case "FIELD_37" : Dtdata.setField37(value); break;
							case "FIELD_38" : Dtdata.setField38(value); break;
							case "FIELD_39" : Dtdata.setField39(value); break;
							case "FIELD_40" : Dtdata.setField40(value); break;
							case "FIELD_41" : Dtdata.setField41(value); break;
							case "FIELD_42" : Dtdata.setField42(value); break;
							case "FIELD_43" : Dtdata.setField43(value); break;
							case "FIELD_44" : Dtdata.setField44(value); break;
							case "FIELD_45" : Dtdata.setField45(value); break;
							case "FIELD_46" : Dtdata.setField46(value); break;
							case "FIELD_47" : Dtdata.setField47(value); break;
							case "FIELD_48" : Dtdata.setField48(value); break;
							case "FIELD_49" : Dtdata.setField49(value); break;
							case "FIELD_50" : Dtdata.setField50(value); break;
							case "FIELD_51" : Dtdata.setField51(value); break;
							case "FIELD_52" : Dtdata.setField52(value); break;
							case "FIELD_53" : Dtdata.setField53(value); break;
							case "FIELD_54" : Dtdata.setField54(value); break;
							case "FIELD_55" : Dtdata.setField55(value); break;
							case "FIELD_56" : Dtdata.setField56(value); break;
							case "FIELD_57" : Dtdata.setField57(value); break;
							case "FIELD_58" : Dtdata.setField58(value); break;
							case "FIELD_59" : Dtdata.setField59(value); break;
							case "FIELD_60" : Dtdata.setField60(value); break;
							case "FIELD_61" : Dtdata.setField61(value); break;
							case "FIELD_62" : Dtdata.setField62(value); break;
							case "FIELD_63" : Dtdata.setField63(value); break;
							case "FIELD_64" : Dtdata.setField64(value); break;
							case "FIELD_65" : Dtdata.setField65(value); break;
							case "FIELD_66" : Dtdata.setField66(value); break;
							case "FIELD_67" : Dtdata.setField67(value); break;
							case "FIELD_68" : Dtdata.setField68(value); break;
							case "FIELD_69" : Dtdata.setField69(value); break;
							case "FIELD_70" : Dtdata.setField70(value); break;
							case "FIELD_71" : Dtdata.setField71(value); break;
							case "FIELD_72" : Dtdata.setField72(value); break;
							case "FIELD_73" : Dtdata.setField73(value); break;
							case "FIELD_74" : Dtdata.setField74(value); break;
							case "FIELD_75" : Dtdata.setField75(value); break;
							case "FIELD_76" : Dtdata.setField76(value); break;
							case "FIELD_77" : Dtdata.setField77(value); break;
							case "FIELD_78" : Dtdata.setField78(value); break;
							case "FIELD_79" : Dtdata.setField79(value); break;
							case "FIELD_80" : Dtdata.setField80(value); break;
							case "FIELD_81" : Dtdata.setField81(value); break;
							case "FIELD_82" : Dtdata.setField82(value); break;
							case "FIELD_83" : Dtdata.setField83(value); break;
							case "FIELD_84" : Dtdata.setField84(value); break;
							case "FIELD_85" : Dtdata.setField85(value); break;
							case "FIELD_86" : Dtdata.setField86(value); break;
							case "FIELD_87" : Dtdata.setField87(value); break;
							case "FIELD_88" : Dtdata.setField88(value); break;
							case "FIELD_89" : Dtdata.setField89(value); break;
							case "FIELD_90" : Dtdata.setField90(value); break;
							case "FIELD_91" : Dtdata.setField91(value); break;
							case "FIELD_92" : Dtdata.setField92(value); break;
							case "FIELD_93" : Dtdata.setField93(value); break;
							case "FIELD_94" : Dtdata.setField94(value); break;
							case "FIELD_95" : Dtdata.setField95(value); break;
							case "FIELD_96" : Dtdata.setField96(value); break;
							case "FIELD_97" : Dtdata.setField97(value); break;
							case "FIELD_98" : Dtdata.setField98(value); break;
							case "FIELD_99" : Dtdata.setField99(value); break;
							case "FIELD_100" : Dtdata.setField100(value); break;
							}
						}
					}
					//log.info("4(4)(2)LoopEnd to add data to object based file template lines: "+ZonedDateTime.now());
					Dtdata.setProfileId(spfaDetails.getSourceProfileId());
					Dtdata.setTemplateId(templateId);
					Dtdata.setTenantId(tenantId);
					Dtdata.setFileDate(ZonedDateTime.now());
					Dtdata.setCreatedDate(ZonedDateTime.now());
					//savingData.setDataMaster(dataMaster);
					return Dtdata;
					
				}
			}
		}
		return null;

	}
}

	
	
	
	
	
	
	
	
	
	
	
	/*public DataStaging DataStagingFields(String[] line, Long templateId, SourceProfileFileAssignments spfaDetails, List<FileTemplateLines> fileTemplateLines, Long tenantId)
	{
		//log.info("** ** ** ** Service call to save the field for Data Staging ** ** ** **");
		//log.info("Line Contains values: "+Arrays.toString(line));
		if(line!=null)
		{
			DataStaging Dtdata = new DataStaging();
			log.info("4(4)(1)Fetching details of file template lines: "+ZonedDateTime.now());
			List<FileTemplateLines> fileTemplateLines = fileTemplateLinesRepository.findByTemplateIdAndRecordTYpe(templateId,"Row Data");
			log.info("4(4)(1)Finished getting details of file template lines: "+ZonedDateTime.now());
			if(fileTemplateLines!=null && fileTemplateLines.size()>0)
			{
				//				log.info("fileTemplateLines and size:"+fileTemplateLines.size() +" and records :"+fileTemplateLines);
				//log.info("length:-"+line.length);
				if(line.length>1)
				{
//					log.info("DataStaging size before saving:- "+dtList.size());
					
//					List<DataStaging> DtList1 = new ArrayList<DataStaging>();
					
					log.info("4(4)(2)LoopStart to add data to object based file template lines: "+ZonedDateTime.now());
					for(int k=0;(k<fileTemplateLines.size() && k<line.length);k++)
					{
						FileTemplateLines fileTempLine=fileTemplateLines.get(k);
						//						log.info("master col name:-> "+fileTempLine.getMasterTableReferenceColumn());


						int ColNumber = (fileTempLine.getColumnNumber()-1);
						//log.info("colNumber:"+csvColNumber);

						String recColName= fileTempLine.getMasterTableReferenceColumn();
						//log.info("recColName1:"+recColName);
						String value=null;
						if(ColNumber<line.length)
							value=line[ColNumber];
						//log.info("Csv File Elements at "+(ColNumber) +" is " + value);


						// Working code (jar file required)
						//String formula = fileTempLine.getFormula();
						if(formula!=null)
						{
							value = DecodeFunctions.callFunction(formula, value);
						}

						String skipColumn = fileTempLine.getSkipColumn();
						//						log.info("skipColumn:-> "+skipColumn);
						if(skipColumn!=null && skipColumn.equalsIgnoreCase("yes"))
						{
							log.info("Skip Column value:"+skipColumn);
							continue;
						}
						else
						{
							//log.info("recColName2:"+recColName);
							switch(recColName)
							{
							case "FIELD_01" : Dtdata.setField01(value); break;
							case "FIELD_02" : Dtdata.setField02(value); break;
							case "FIELD_03" : Dtdata.setField03(value); break;
							case "FIELD_04" : Dtdata.setField04(value); break;
							case "FIELD_05" : Dtdata.setField05(value); break;
							case "FIELD_06" : Dtdata.setField06(value); break;
							case "FIELD_07" : Dtdata.setField07(value); break;
							case "FIELD_08" : Dtdata.setField08(value); break;
							case "FIELD_09" : Dtdata.setField09(value); break;
							case "FIELD_10" : Dtdata.setField10(value); break;
							case "FIELD_11" : Dtdata.setField11(value); break;
							case "FIELD_12" : Dtdata.setField12(value); break;
							case "FIELD_13" : Dtdata.setField13(value); break;
							case "FIELD_14" : Dtdata.setField14(value); break;
							case "FIELD_15" : Dtdata.setField15(value); break;
							case "FIELD_16" : Dtdata.setField16(value); break;
							case "FIELD_17" : Dtdata.setField17(value); break;
							case "FIELD_18" : Dtdata.setField18(value); break;
							case "FIELD_19" : Dtdata.setField19(value); break;
							case "FIELD_20" : Dtdata.setField20(value); break;
							case "FIELD_21" : Dtdata.setField21(value); break;
							case "FIELD_22" : Dtdata.setField22(value); break;
							case "FIELD_23" : Dtdata.setField23(value); break;
							case "FIELD_24" : Dtdata.setField24(value); break;
							case "FIELD_25" : Dtdata.setField25(value); break;
							case "FIELD_26" : Dtdata.setField26(value); break;
							case "FIELD_27" : Dtdata.setField27(value); break;
							case "FIELD_28" : Dtdata.setField28(value); break;
							case "FIELD_29" : Dtdata.setField29(value); break;
							case "FIELD_30" : Dtdata.setField30(value); break;
							case "FIELD_31" : Dtdata.setField31(value); break;
							case "FIELD_32" : Dtdata.setField32(value); break;
							case "FIELD_33" : Dtdata.setField33(value); break;
							case "FIELD_34" : Dtdata.setField34(value); break;
							case "FIELD_35" : Dtdata.setField35(value); break;
							case "FIELD_36" : Dtdata.setField36(value); break;
							case "FIELD_37" : Dtdata.setField37(value); break;
							case "FIELD_38" : Dtdata.setField38(value); break;
							case "FIELD_39" : Dtdata.setField39(value); break;
							case "FIELD_40" : Dtdata.setField40(value); break;
							case "FIELD_41" : Dtdata.setField41(value); break;
							case "FIELD_42" : Dtdata.setField42(value); break;
							case "FIELD_43" : Dtdata.setField43(value); break;
							case "FIELD_44" : Dtdata.setField44(value); break;
							case "FIELD_45" : Dtdata.setField45(value); break;
							case "FIELD_46" : Dtdata.setField46(value); break;
							case "FIELD_47" : Dtdata.setField47(value); break;
							case "FIELD_48" : Dtdata.setField48(value); break;
							case "FIELD_49" : Dtdata.setField49(value); break;
							case "FIELD_50" : Dtdata.setField50(value); break;
							case "FIELD_51" : Dtdata.setField51(value); break;
							case "FIELD_52" : Dtdata.setField52(value); break;
							case "FIELD_53" : Dtdata.setField53(value); break;
							case "FIELD_54" : Dtdata.setField54(value); break;
							case "FIELD_55" : Dtdata.setField55(value); break;
							case "FIELD_56" : Dtdata.setField56(value); break;
							case "FIELD_57" : Dtdata.setField57(value); break;
							case "FIELD_58" : Dtdata.setField58(value); break;
							case "FIELD_59" : Dtdata.setField59(value); break;
							case "FIELD_60" : Dtdata.setField60(value); break;
							case "FIELD_61" : Dtdata.setField61(value); break;
							case "FIELD_62" : Dtdata.setField62(value); break;
							case "FIELD_63" : Dtdata.setField63(value); break;
							case "FIELD_64" : Dtdata.setField64(value); break;
							case "FIELD_65" : Dtdata.setField65(value); break;
							case "FIELD_66" : Dtdata.setField66(value); break;
							case "FIELD_67" : Dtdata.setField67(value); break;
							case "FIELD_68" : Dtdata.setField68(value); break;
							case "FIELD_69" : Dtdata.setField69(value); break;
							case "FIELD_70" : Dtdata.setField70(value); break;
							case "FIELD_71" : Dtdata.setField71(value); break;
							case "FIELD_72" : Dtdata.setField72(value); break;
							case "FIELD_73" : Dtdata.setField73(value); break;
							case "FIELD_74" : Dtdata.setField74(value); break;
							case "FIELD_75" : Dtdata.setField75(value); break;
							case "FIELD_76" : Dtdata.setField76(value); break;
							case "FIELD_77" : Dtdata.setField77(value); break;
							case "FIELD_78" : Dtdata.setField78(value); break;
							case "FIELD_79" : Dtdata.setField79(value); break;
							case "FIELD_80" : Dtdata.setField80(value); break;
							case "FIELD_81" : Dtdata.setField81(value); break;
							case "FIELD_82" : Dtdata.setField82(value); break;
							case "FIELD_83" : Dtdata.setField83(value); break;
							case "FIELD_84" : Dtdata.setField84(value); break;
							case "FIELD_85" : Dtdata.setField85(value); break;
							case "FIELD_86" : Dtdata.setField86(value); break;
							case "FIELD_87" : Dtdata.setField87(value); break;
							case "FIELD_88" : Dtdata.setField88(value); break;
							case "FIELD_89" : Dtdata.setField89(value); break;
							case "FIELD_90" : Dtdata.setField90(value); break;
							case "FIELD_91" : Dtdata.setField91(value); break;
							case "FIELD_92" : Dtdata.setField92(value); break;
							case "FIELD_93" : Dtdata.setField93(value); break;
							case "FIELD_94" : Dtdata.setField94(value); break;
							case "FIELD_95" : Dtdata.setField95(value); break;
							case "FIELD_96" : Dtdata.setField96(value); break;
							case "FIELD_97" : Dtdata.setField97(value); break;
							case "FIELD_98" : Dtdata.setField98(value); break;
							case "FIELD_99" : Dtdata.setField99(value); break;
							case "FIELD_100" : Dtdata.setField100(value); break;
							}
						}

//						DtList1.add(Dtdata);

					}
					log.info("4(4)(2)LoopEnd to add data to object based file template lines: "+ZonedDateTime.now());
					//log.info("***** While Loop ends here *****");

//					FileTemplates fileTemplates = fileTemplatesRepository.findOne(templateId);

//					String fName = spfaDetails.getFileNameFormat();
//					String replace = fName.replace("(.*)", "");
					Dtdata.setProfileId(spfaDetails.getSourceProfileId());
//					Dtdata.setFileName(replace);
//					Dtdata.setTenantId(tenantId);
					Dtdata.setTemplateId(templateId);
					//Dtdata.setCreatedBy(fileTemplates.getTenantId());
					Dtdata.setCreatedDate(ZonedDateTime.now());

					//log.info("DtData Saving: "+Dtdata);
					
//					DataStaging dt =dataStagingRepository.save(Dtdata);
//					log.info("DataStaging size after saving:- "+dtList.size());
//					log.info("Dt saving:- "+dt);
//					return "File Uploaded Success";
					return Dtdata;
					
				}
			}
		}
		return null;

	}
}
*/
