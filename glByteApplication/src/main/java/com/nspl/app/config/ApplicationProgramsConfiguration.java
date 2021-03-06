package com.nspl.app.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import au.com.bytecode.opencsv.CSVReader;

import com.nspl.app.domain.ApplicationPrograms;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.service.ApplicationProgramsService;

@Configuration
public class ApplicationProgramsConfiguration {

	private final Logger log = LoggerFactory.getLogger(ApplicationProgramsConfiguration.class);
	
	@Inject
	ApplicationProgramsService applicationProgramsService;
	
	@Inject
	ApplicationProgramsRepository applicationProgramsRepository;
	
	@Inject
	Environment env;
	
	@Bean
	public String ConfiguringApplicationPrograms1()
	{
		
		Long tenantId=0L;
		Long userId=3L;
		String status="success";
		log.info("Configuring Application Program Paths on Application StartUp with tenant:"+tenantId+" and userId:- "+userId);
		
		InputStreamReader inputStream = new  InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("jsonFile/ApplicationProgram.csv"));
		if(inputStream!=null)
		{
			CSVReader csvReader = new CSVReader(inputStream, ',' , '"');
			int progNameIndx = 0;
			int prgmDesIndx = 0;
			int generatedPathIndx = 0;
			int targetPathIndx = 0;
			int progPathIndx = 0;
			int prgmOrClasNameIndx = 0;
			int prgmTypeIndx = 0;
			int jhiEnableIndx = 0;
			int startDtIndx = 0;
			int endDtIndx = 0;

			List<String[]> allRows;
			try {
				allRows = csvReader.readAll();
				csvReader.close();
				log.info("Rows Size in ApplicationPrograms File: "+ allRows.size());

				if(allRows.size()>1)
				{
					String[] header = allRows.get(0);

					for(int i=0; i<header.length; i++)
					{
						if("prgm_name".equalsIgnoreCase(header[i].toString()))
							progNameIndx = i;
						else if("prgm_description".equalsIgnoreCase(header[i].toString()))
							prgmDesIndx = i;
						else if("generated_path".equalsIgnoreCase(header[i].toString()))
							generatedPathIndx = i;
						else if("target_path".equalsIgnoreCase(header[i].toString()))
							targetPathIndx = i;
						else if("prgm_path".equalsIgnoreCase(header[i].toString()))
							progPathIndx = i;
						else if("prgm_or_class_name".equalsIgnoreCase(header[i].toString()))
							prgmOrClasNameIndx = i;
						else if("prgm_type".equalsIgnoreCase(header[i].toString()))
							prgmTypeIndx = i;
						else if("jhi_enable".equalsIgnoreCase(header[i].toString()))
							jhiEnableIndx = i;
						else if("start_date".equalsIgnoreCase(header[i].toString()))
							startDtIndx = i;
						else if("end_date".equalsIgnoreCase(header[i].toString()))
							endDtIndx = i;
					}

					log.info("Indexes: prgm_name["+progNameIndx+"], prgm_description["+prgmDesIndx+"], generated_path["+generatedPathIndx+"], target_path["+targetPathIndx+"], prgm_path["+
							progPathIndx+"], prgm_or_class_name["+prgmOrClasNameIndx+"], prgm_type["+prgmTypeIndx+"], jhi_enable["+jhiEnableIndx+"], start_date["
							+startDtIndx+"], endDtIndx["+endDtIndx+"]");
				}

				if(allRows.size()>1)
				{
					// Reading data
					List<ApplicationPrograms> appPrgms = new ArrayList<ApplicationPrograms>();
					for(int j=1; j<allRows.size(); j++)
					{
						String[] row = allRows.get(j);
						ApplicationPrograms app = new ApplicationPrograms();
						app.setEnable(true);
						String prgmName="";
						String generatedPath="";
						String targetPath="";
						String prgmType="";
						String prgmPath="";
						app.setTenantId(tenantId);
						if("NULL".equalsIgnoreCase(row[progNameIndx].toString()) || (row[progNameIndx].toString().isEmpty()))
							app.setPrgmName(null);
						else{
							prgmName=row[progNameIndx].toString();
							app.setPrgmName(row[progNameIndx].toString());
						}
						if("NULL".equalsIgnoreCase(row[prgmDesIndx].toString()) || (row[prgmDesIndx].toString().isEmpty()))
							app.setPrgmDescription(null);
						else
							app.setPrgmDescription(row[prgmDesIndx].toString());
						if("NULL".equalsIgnoreCase(row[generatedPathIndx].toString()) || (row[generatedPathIndx].toString().isEmpty()))
							app.setGeneratedPath(null);
						else{
							generatedPath=row[generatedPathIndx].toString();
							app.setGeneratedPath(row[generatedPathIndx].toString());
						}
						if("NULL".equalsIgnoreCase(row[targetPathIndx].toString()) || (row[targetPathIndx].toString().isEmpty()))
							app.setTargetPath(null);
						else{
							targetPath=row[targetPathIndx].toString();
							app.setTargetPath(row[targetPathIndx].toString());
						}
						if("NULL".equalsIgnoreCase(row[progPathIndx].toString()) || (row[progPathIndx].toString().isEmpty()))
							app.setPrgmPath(null);
						else{
							prgmPath=row[progPathIndx].toString();
							app.setPrgmPath(row[progPathIndx].toString());
						}
						if("NULL".equalsIgnoreCase(row[prgmOrClasNameIndx].toString()) || (row[prgmOrClasNameIndx].toString().isEmpty()))
							app.setPrgmOrClassName(null);
						else
							app.setPrgmOrClassName(row[prgmOrClasNameIndx].toString());
						if("NULL".equalsIgnoreCase(row[prgmTypeIndx].toString()) || (row[prgmTypeIndx].toString().isEmpty()))
							app.setPrgmType(null);
						else{
							prgmType=row[prgmTypeIndx].toString();
							app.setPrgmType(row[prgmTypeIndx].toString());
						}
						//app.isEnable(Boolean.valueOf(lineSplit[jhiEnableIndx]));
						log.info("row[startDtIndx].toString() :"+row[startDtIndx].toString());
						if("NULL".equalsIgnoreCase(row[startDtIndx].toString()) || (row[startDtIndx].toString().isEmpty()))
							app.setStartDate(null);
						else
							app.setStartDate(ZonedDateTime.parse(row[startDtIndx].toString()));
						if("NULL".equalsIgnoreCase(row[endDtIndx].toString()) || (row[endDtIndx].toString().isEmpty()))
							app.setEndDate(null);
						else
							app.setEndDate(ZonedDateTime.parse(row[endDtIndx].toString()));
						app.setCreatedBy(userId);
						app.setCreationDate(ZonedDateTime.now());
						app.setLastUpdatedBy(userId);
						app.setLastUpdationDate(ZonedDateTime.now());
						
						log.info("prgmName: "+prgmName);
						ApplicationPrograms ap=new ApplicationPrograms();
						if(prgmName!=null && !(prgmName.isEmpty()) && !(prgmName.equalsIgnoreCase(""))){
						ap=applicationProgramsRepository.findByPrgmNameAndTenantId(prgmName,tenantId);
						log.info("ap: "+ap);
						}
						if(ap!=null){
							
						}
						else ap=applicationProgramsRepository.save(app);

						/* Create Directories for Generated, Target, Program Paths */
						String hadoopBaseDirectory=env.getProperty("baseDirectories.hadoopBaseDir");
						String linuxBaseDirectory=env.getProperty("baseDirectories.linuxBaseDir");
						String hadoopRootUser=env.getProperty("oozie.hadoopRootUser");
						
						if(prgmName!=null && !(prgmName.isEmpty()) && !(prgmName.equalsIgnoreCase(""))){
								if(generatedPath!=null && !(generatedPath.isEmpty()) && !(generatedPath.equalsIgnoreCase("")))
									applicationProgramsService.createPrgmDirectory(linuxBaseDirectory,generatedPath);
						
							if(hadoopRootUser!=null){
								if(targetPath!=null && !(targetPath.isEmpty()) && !(targetPath.equalsIgnoreCase(""))){
									applicationProgramsService.createDirectoryInHdfs(hadoopBaseDirectory, "targetPath",targetPath, hadoopRootUser,null ,null);
							}
								if(prgmPath!=null && !(prgmPath.isEmpty()) && !(prgmPath.equalsIgnoreCase(""))){
									if(prgmType.equalsIgnoreCase("java")){
									applicationProgramsService.createDirectoryInHdfs(hadoopBaseDirectory,  "progarmPath",prgmPath, hadoopRootUser,prgmType,"lib" );
									}
									else {
										applicationProgramsService.createDirectoryInHdfs(hadoopBaseDirectory,  "progarmPath",prgmPath, hadoopRootUser,prgmType, "lib" );
										applicationProgramsService.createDirectoryInHdfs(hadoopBaseDirectory,  "progarmPath",prgmPath, hadoopRootUser,prgmType, "shell" );
									}
								}
							}
							
							if(prgmName.equalsIgnoreCase("Reporting")){
								applicationProgramsService.createPrgmDirectory(linuxBaseDirectory,"Reporting");
								applicationProgramsService.createDirectoryInHdfs(hadoopBaseDirectory, "reportsOutputPath",prgmPath, hadoopRootUser,null,null );
								applicationProgramsService.createDirectoryInHdfs(hadoopBaseDirectory, "reportsLocalPath",targetPath, hadoopRootUser,null,null );
							}

							if((prgmName.equalsIgnoreCase("DataExtraction")) || (prgmName.equalsIgnoreCase("DataTransformation")))
							{
								applicationProgramsService.createDirectoryInHdfs(hadoopBaseDirectory, "transformationLocalPath",targetPath, hadoopRootUser,null,null );
							}
						}
						applicationProgramsService.configuringParameterSets(ap.getId(),row[progNameIndx].toString(), userId);

						System.out.println("J Value: "+j+"--> row[progNameIndx].toString():- "+row[progNameIndx].toString());
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return status;

	}
}
