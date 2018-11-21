package com.nspl.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.PrivilegedAction;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import au.com.bytecode.opencsv.CSVReader;

import com.jcraft.jsch.Channel;
import com.nspl.app.domain.ProgParametersSets;
import com.nspl.app.repository.ProgParametersSetsRepository;


@Service
public class ApplicationProgramsService {

	private final Logger log = LoggerFactory.getLogger(ApplicationProgramsService.class);
	
	@Autowired
	Environment env;
	
	@Inject
	SFTPUtilService sftpService;
	
	@Inject
	UserJdbcService userJdbcService;
	
	@Autowired
	Configuration hadoopConfiguration;
	
	@Inject
	ProgParametersSetsRepository progParametersSetsRepository;
	
	/**
	 * Author: Swetha
	 * @param baseDirectory
	 * @param generatedPath
	 */
	public void createPrgmDirectory(String baseDirectory, String generatedPath){
		
		log.info("In createPrgmDirectory with generatedPath: "+generatedPath);
		String directory ="";
		if(baseDirectory.endsWith("/"))
			directory = baseDirectory+generatedPath;
		else
			directory = baseDirectory+"/"+generatedPath;
		log.info("directory: "+directory);
		File file = new File(directory);
		/*file.setExecutable(true);
		 file.setReadable(true);
		 file.setWritable(true);*/
		    boolean b = false;
		    if (!file.exists()) {
		      b = file.mkdirs();
		    }
		    if (b)
		      System.out.println("Directory successfully created");
		    else
		      System.out.println("Directory already exists");
		  }
		
	/**
	 * Author: Swetha
	 * Function to created directories in Hdfs
	 * @param baseDirectory
	 * @param nodeName
	 * @param programName
	 * @param rootUser
	 * @throws IOException
	 */
	public void createDirectoryInHdfs(String baseDirectory,String pathType, String path,String rootUser, String prgmType,String val) throws IOException{
		
		log.info("In createDirectoryInHdfs with pathType: "+pathType+", prgmType: "+prgmType+", path: "+path+", val: "+val);
		//Configuration config = new Configuration();
		//config.se
		 String uri = hadoopConfiguration.get("fs.defaultFS")+baseDirectory;
		 System.out.println("uri: "+uri);
		 String dir="";
		FileSystem fs = FileSystem.get(URI.create(uri), hadoopConfiguration);
		if(pathType.equalsIgnoreCase("targetPath")){
			dir = uri+path;
			//log.info("dir: "+dir);
		}
		else if(pathType.equalsIgnoreCase("progarmPath")){
			 int index=path.lastIndexOf("/");
			 path=path.substring(0, index);
			 log.info("path: "+path);
			 if(val!=null && val.equalsIgnoreCase("lib") && prgmType.equalsIgnoreCase("shell") ){
				 if(path.contains("shell"))
					 path=path.replace("shell", "lib");
			 dir = uri+path;
			 }
			 else if(val!=null && val.equalsIgnoreCase("lib") && prgmType.equalsIgnoreCase("java") || prgmType.equalsIgnoreCase("spark")){
			 dir = uri+path;
			 }
			 
			 else if(val!=null && val.equalsIgnoreCase("shell")){
				 /*if(path.contains("lib"))
				 path=path.replace("lib", "shell");*/
				  dir = uri+path;
			 }
			//log.info("dir: "+dir);	 
		}
		else if(pathType.equalsIgnoreCase("reportsOutputPath")){
			int index=path.lastIndexOf("/");
			 path=path.substring(0, index);
			 path=path.replace("lib", "generatedReports");
			 dir = uri+path;
			 //log.info("dir: "+dir);
		}
		else if(pathType.equalsIgnoreCase("reportsLocalPath")){
			int index=path.lastIndexOf("/");
			 path=path.substring(0, index);
			 path=path.replace("XML", "reportsTempPath");
			 dir = uri+path;
			 //log.info("dir: "+dir);
		}
		else if(pathType.equalsIgnoreCase("transformationLocalPath")){
			int index=path.lastIndexOf("/");
			 path=path.substring(0, index);
			 path=path.replace("XML", "etlFilesRepository");
			 dir = uri+path;
			 //log.info("dir: "+dir);
		}
		
		String directory =dir;
		  System.out.println("directory: "+directory);
		  
		  
		  UserGroupInformation ugi
	        = UserGroupInformation.createRemoteUser(rootUser);
		  hadoopConfiguration.set("hadoop.job.ugi", hadoopConfiguration.get("fs.defaultFS"));
			
			 ugi.doAs(new PrivilegedAction<Void>() {

				@Override
				public Void run(){
					// TODO Auto-generated method stub
					/*Configuration conf = new Configuration();
					conf.set("hadoop.job.ugi", rootUser);*/
				
					FileSystem file = null;
					try {
						file = FileSystem.get(URI.create(uri),hadoopConfiguration);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//File f=new File(directory);
					try {
						boolean isCreated = fs.mkdirs(new Path(directory));
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					try {
						file.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
			 });

	}
	
	/**
	 * Author: Swetha
	 * Function to create directory using sftp
	 * @param tenantId
	 */
	public void createDirectoryWithSftp(Long tenantId){
		
		log.info("In createDirectoryWithSftp with tenantId: "+tenantId);
		String host = env.getProperty("dataExtraction.host");
		String sftpPort=env.getProperty("dataExtraction.port");
		String userName = env.getProperty("dataExtraction.user");
		String password = env.getProperty("dataExtraction.password");
		String basePath= env.getProperty("dataExtraction.basePath");
		
		//log.info("host: "+host+" userName: "+userName+" password: "+password+" basePath: "+basePath);
		//log.info("sftpPort: "+sftpPort);
		Channel channel = sftpService.getConnection(host, Integer.parseInt(sftpPort.toString()), userName, password);
		String path=basePath+"/"+tenantId;
		log.info("path: "+path);
		sftpService.createDirectory(channel, path);
		
	}
	
	/**
	 * Author: Kiran
	 */
		public String configuringParameterSets(Long programId, String programName, Long userId)
		{
			log.info("Setting Parameter Set values for Program Id:- "+programId+" and programName:-"+programName);
			InputStreamReader inputStream = new  InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("jsonFile/ProgramParameterSets.csv"));
			if(inputStream!=null)
			{
				CSVReader csvReader = new CSVReader(inputStream, ',' , '"');

				int progNameIndx = 0;
				int pramNameIndx = 0;
				int startDtIndx = 0;
				int endDtIndx = 0;
				int entityNameIndx = 0;
				int entityColIndx = 0;
				int bindValIndx = 0;
				int MandatoryIndx = 0;
				int dependencyIndx = 0;
				int requestFormIndx = 0;


				List<String[]> allRows;
				try {
					allRows = csvReader.readAll();
					csvReader.close();
					log.info("All Rows Size in configuringParameterSets: "+ allRows.size());

					if(allRows.size()>1)
					{
						String[] header = allRows.get(0);
						for(int i=0; i<header.length; i++)
						{
							if("program_name".equalsIgnoreCase(header[i].toString()))
								progNameIndx = i;
							else if("parameter_name".equalsIgnoreCase(header[i].toString()))
								pramNameIndx = i;
							else if("start_date".equalsIgnoreCase(header[i].toString()))
								startDtIndx = i;
							else if("end_date".equalsIgnoreCase(header[i].toString()))
								endDtIndx = i;
							else if("entity_name".equalsIgnoreCase(header[i].toString()))
								entityNameIndx = i;
							else if("entity_column".equalsIgnoreCase(header[i].toString()))
								entityColIndx = i;
							else if("bind_value".equalsIgnoreCase(header[i].toString()))
								bindValIndx = i;
							else if("mandatory".equalsIgnoreCase(header[i].toString()))
								MandatoryIndx = i;
							else if("dependency".equalsIgnoreCase(header[i].toString()))
								dependencyIndx = i;
							else if("request_form".equalsIgnoreCase(header[i].toString()))
								requestFormIndx = i;

						}

						log.info("Indexes: program_name["+progNameIndx+"], parameter_name["+pramNameIndx+"], start_date["+startDtIndx+"], end_date["+endDtIndx+"], entity_name["+
								entityNameIndx+"], entityColIndx["+entityColIndx+"], bind_value["+bindValIndx+"], MandatoryIndx["+MandatoryIndx+"], dependency["
								+dependencyIndx+"], requestFormIndx["+requestFormIndx+"]");
					}


					if(allRows.size()>1)
					{
						// Reading data
						List<ProgParametersSets> prgmParamsSetList = new ArrayList<ProgParametersSets>();
						for(int j=1; j<allRows.size(); j++)
						{
							String[] row = allRows.get(j);
							ProgParametersSets prgmParamsSet=new ProgParametersSets();

							if((!row[progNameIndx].toString().isEmpty()) && row[progNameIndx].toString().equalsIgnoreCase(programName))
							{
								prgmParamsSet.setProgramId(programId);
								prgmParamsSet.setStatus("ACTIVE");


								if("NULL".equalsIgnoreCase(row[pramNameIndx].toString()) || (row[pramNameIndx].toString().isEmpty()))
									prgmParamsSet.setParameterName(null);
								else
									prgmParamsSet.setParameterName(row[pramNameIndx].toString());

								if("NULL".equalsIgnoreCase(row[startDtIndx].toString()) || (row[startDtIndx].toString().isEmpty()))
									prgmParamsSet.setStartDate(null);
								else
									prgmParamsSet.setStartDate(ZonedDateTime.parse(row[startDtIndx].toString()));

								if("NULL".equalsIgnoreCase(row[endDtIndx].toString()) || (row[endDtIndx].toString().isEmpty()))
									prgmParamsSet.setEndDate(null);
								else
									prgmParamsSet.setEndDate(ZonedDateTime.parse(row[endDtIndx].toString()));

								if("NULL".equalsIgnoreCase(row[entityNameIndx].toString()) || (row[entityNameIndx].toString().isEmpty()))
									prgmParamsSet.setEntityName(null);
								else
									prgmParamsSet.setEntityName(row[entityNameIndx].toString());

								if("NULL".equalsIgnoreCase(row[entityColIndx].toString()) || (row[entityColIndx].toString().isEmpty()))
									prgmParamsSet.setEntityColumn(null);
								else
									prgmParamsSet.setEntityColumn(row[entityColIndx].toString());

								if("NULL".equalsIgnoreCase(row[bindValIndx].toString()) || (row[bindValIndx].toString().isEmpty()))
									prgmParamsSet.setBindValue(null);
								else
									prgmParamsSet.setBindValue(row[bindValIndx].toString());

								if("NULL".equalsIgnoreCase(row[MandatoryIndx].toString()) || (row[MandatoryIndx].toString().isEmpty()))
									prgmParamsSet.setMandatory(null);
								else
									prgmParamsSet.setMandatory(Boolean.parseBoolean(row[MandatoryIndx]));

								if("NULL".equalsIgnoreCase(row[dependencyIndx].toString()) || (row[dependencyIndx].toString().isEmpty()))
									prgmParamsSet.setDependency(null);
								else
									prgmParamsSet.setDependency(row[dependencyIndx].toString());
								
								if("NULL".equalsIgnoreCase(row[requestFormIndx].toString()) || (row[requestFormIndx].toString().isEmpty()))
									prgmParamsSet.setRequestForm(null);
								else
									prgmParamsSet.setRequestForm(Boolean.parseBoolean(row[requestFormIndx]));


								prgmParamsSet.setCreatedBy(userId);
								prgmParamsSet.setLastUpdatedBy(null);
								prgmParamsSet.setCreationDate(ZonedDateTime.now());
								prgmParamsSet.setLastUpdationDate(null);

								prgmParamsSetList.add(prgmParamsSet);
								//progParametersSetsRepository.save(prgmParamsSet);
							}
						}
						if(prgmParamsSetList.size()>0)
							progParametersSetsRepository.save(prgmParamsSetList);
					}

				}catch(IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
			return programName;

		}
	
}
