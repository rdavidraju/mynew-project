package com.nspl.app.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
//import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//import com.dropbox.core.DbxClient;
//import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;
import com.nspl.app.domain.SourceConnectionDetails;
import com.nspl.app.domain.SourceProfileFileAssignments;
import com.nspl.app.domain.SourceProfiles;
import com.nspl.app.repository.SourceConnectionDetailsRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.SourceProfilesRepository;

@Service
public class SourceConnectionDetailsService {
	private final Logger log = LoggerFactory.getLogger(SourceConnectionDetailsService.class);

	@Inject
	FileService fileService;

	@Inject
	SFTPUtilService sftpService;

	@Inject
	SourceProfilesRepository sourceProfilesRepository;

	@Inject
	SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;

	@Inject
	ReadingFileTemplatesService readingFileTemplatesService;

	@Inject
	SourceConnectionDetailsRepository sourceConnectionDetailsRepository;

	@Inject
	DriveService driveService;
	
	@Inject
	PropertiesUtilService propertiesUtilService;




	private String sftpFileTargetFolder;


	/**
	 * Auhtor Shiva
	 * @param sourceConnDetails
	 * @param tenantId
	 * @throws SftpException
	 * @throws DbxException
	 * @throws IOException
	 */
	public void savingFilesToLocalDir(SourceConnectionDetails sourceConnDetails, Long tenantId) throws SftpException, DbxException, IOException
	{
		log.info("** ** ** Service call to save files to the local folder ** ** **");
		log.info("Connecion Name: "+ sourceConnDetails.getName() +", Protocol: "+ sourceConnDetails.getProtocol());
		SourceProfiles sourceProfile = sourceProfilesRepository.findByConnectionIdAndTenantId(sourceConnDetails.getId(), tenantId);
		if(sourceProfile != null)
		{
			log.info("Profile Name: "+ sourceProfile.getSourceProfileName()+", Connection ID: "+sourceConnDetails.getId());
			List<SourceProfileFileAssignments> srcPrflFileAssgnmnt = sourceProfileFileAssignmentsRepository.findBySourceProfileId(sourceProfile.getId());
			if(srcPrflFileAssgnmnt.size()>0)
			{
				for(SourceProfileFileAssignments spfa : srcPrflFileAssgnmnt)
				{
					Long templateId=spfa.getTemplateId();
					LinkedHashMap<String, InputStream> fetchedFilesAsInpStrm = new LinkedHashMap<String, InputStream>();
					fetchedFilesAsInpStrm = fetchFilesFromCloud(sourceConnDetails, spfa);
					if(fetchedFilesAsInpStrm != null)
					{
						if(fetchedFilesAsInpStrm.size()>0)
						{
							File fileDir = new File(spfa.getLocalDirectoryPath()+sourceConnDetails.getName()+"/"+sourceProfile.getSourceProfileName()+"/");
							if (!fileDir.exists()) 
							{
								if(fileDir.mkdirs())
								{
									log.info(spfa.getLocalDirectoryPath()+sourceConnDetails.getName()+"/"+sourceProfile.getSourceProfileName()+"/"+" directory is created.");	
								}
								else
								{
									log.info("Failed to create directory: " + spfa.getLocalDirectoryPath()+sourceConnDetails.getName()+"/"+sourceProfile.getSourceProfileName()+"/"+" directory is created.");
								}
							}
							for(Map.Entry<String, InputStream> entry : fetchedFilesAsInpStrm.entrySet()) {
								String fileName = entry.getKey();	//File name
								InputStream is = entry.getValue();	//inputstream
								/*fileService.fileUpload(is, key, destination);*/
								log.info("Downloading "+ fileName + ". . .");

								// To give the name of file included with time stamp by kiran
								DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss"); 
								String fileWithTimeStamp = fileName.split("[.]")[0] +"_"+ df.format(new Date()) + "."+fileName.split("[.]")[1];
								log.info("TemplateName with time:-"+fileWithTimeStamp);

								File file = new File(spfa.getLocalDirectoryPath()+sourceConnDetails.getName()+"/"+sourceProfile.getSourceProfileName()+"/" + fileWithTimeStamp);
								OutputStream outputStream = new FileOutputStream(file);
								IOUtils.copy(is, outputStream);
								outputStream.close();
								log.info("Downloaded "+ fileWithTimeStamp + ".");

							} 
							if(fileDir!=null && fileDir.length()>0)
							{
								//String result=readingFileTemplatesService.readFileTemplateDetailsBasedOnFileType(templateId, fileDir);
								// Kiran
								//log.info("result:->>"+result);
							}
						}
					}
				}
			}
			else
			{
				log.info("No Source Profile Files Are Assigned TO Source ID: "+sourceProfile.getId());
			}
		}
		else
		{
			log.info("No Profile is Assigned For The Connection ID: "+sourceConnDetails.getId());
		}
	}




	/**
	 * Author Shiva
	 * @throws SftpException 
	 * @throws DbxException **/
	public LinkedHashMap<String, InputStream> fetchFilesFromCloud(SourceConnectionDetails connection, SourceProfileFileAssignments sourceProfilePath) throws SftpException, DbxException
	{
		log.info("Service called for fetching list of files as inputstreams from Cloud");
		LinkedHashMap<String, InputStream> inputStreams = new LinkedHashMap<String, InputStream>();
		if(connection.getProtocol().equalsIgnoreCase("sftp"))
		{
			log.info("Establishing connection with SFTP HOST "+ connection.getHost());
			String sftpFilePath1 = "/var/www/html";
			String sftpFilePath2 = "/RECON";
			String sftpFileTargetFolder = sftpFilePath1 + sftpFilePath2;
			String sourceFolderPath = sftpFileTargetFolder+sourceProfilePath.getSourceDirectoryPath()+"/";
			log.info("Remote server files path: "+sourceFolderPath);

			String Host = connection.getHost();
			int Port = 22;//source.getPortNumber();
			String UserName = connection.getUserName();
			String Password = connection.getPassword();
			Channel channel = sftpService.getConnection(Host, Port, UserName, Password);
			ChannelSftp channelSftp = (ChannelSftp) channel;
			channelSftp.cd(sourceFolderPath);
			Vector fileList = channelSftp.ls(sourceFolderPath);
			String completeFilePath = "";

			if(fileList.size()>0)
			{
				for(int i=0; i<fileList.size(); i++)
				{
					LsEntry entry = (LsEntry) fileList.get(i);
					if((!entry.getFilename().toString().startsWith(".")) && entry.getFilename().toString().contains("."))
					{
						InputStream inputStream = sftpService.getFile(channel, sourceFolderPath, entry.getFilename().toString());	//getting file as inputstream
						inputStreams.put(entry.getFilename().toString(), inputStream);

						//moving files in remote server from one folder to another folder
						InputStream inputStream3 = sftpService.getFile(channel, sourceFolderPath, entry.getFilename().toString());
						fileService.movingFile(inputStream3, entry.getFilename().toString(), connection);

						//Deleting file once file has been moved to another folder
						completeFilePath = sourceFolderPath+"/"+entry.getFilename().toString();
						channelSftp.rm(completeFilePath);
					}
				}
			}

			log.info("===>> Service ended for fetching list of files as inputstreams from SFTP Server, Size: "+inputStreams.size());
			return inputStreams;
		}
		else if(connection.getProtocol().equalsIgnoreCase("dropbox"))
		{/*
			log.info("** ** ** **Service called for fetching list of files as inputstreams from dropbox ** ** ** **");
			log.info("Establishing connection with DROPBOX");
			String ACCESS_TOKEN = connection.getClientKey();
			String sourceFolderName = sourceProfilePath.getSourceDirectoryPath();
			DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
			DbxClient client = new DbxClient(config, ACCESS_TOKEN);
			DbxEntry.WithChildren listing = client.getMetadataWithChildren("/"+sourceFolderName);

			if(listing != null)
			{
				for (DbxEntry child : listing.children){
					if(child.isFile())
					{
						InputStream is = client.startGetFile("/"+ sourceFolderName+"/"+child.name, null).body;
						inputStreams.put(child.name, is);

						try{
							client.move("/"+ sourceFolderName +"/"+child.name, "/"+sourceFolderName+"/"+"Siva_Ex/"+child.name);	// Moving files into Ex folder drive
						}catch(Exception e)
						{ //Exception will occure if file is already is exists in Ex folder
							client.delete("/"+sourceFolderName+"/"+"Siva_Ex/"+child.name);
							client.move("/"+ sourceFolderName +"/"+child.name, "/"+sourceFolderName+"/"+"Siva_Ex/"+child.name);	// Moving files into Ex folder drive
						}
					}
				}
			}
			log.info("===>> Service ended for fetching list of files as inputstreams from dropbox, Size: "+inputStreams.size());
			return inputStreams;
		*/
			return null;}
		else if(connection.getProtocol().equalsIgnoreCase("google_drive"))
		{
			log.info("Service called for fetching list of files as inputstreams from google drive");
			log.info("Establishing connection with GOOGLE DRIVE");
			return null;
		}
		else
		{
			return null;
		}

	}

	/********************* Service call to get the files based on spfa Id ***************************/

	/** 
	 * Author Kiran
	 * @param sourceConnDetails
	 * @param SPFAId (File Sync Methods based on id)
	 * @throws IOException 
	 */
	public int savingFilesToLocalDirBasedOnSPFAId(SourceConnectionDetails sourceConnDetails, Long SPFAId, Long tenantId) throws IOException 
	{
		log.info("========== *2* Service call to save files to the local folder BasedOnSPFAId ==========");
		SourceProfileFileAssignments spfa = sourceProfileFileAssignmentsRepository.findOne(SPFAId);
		Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
		String localConnName = props.getProperty("LocalConnectionName");
		SourceConnectionDetails targetConnection=null;
		if(localConnName!=null)
			targetConnection= sourceConnectionDetailsRepository.findByNameAndTenantId(localConnName, tenantId);
		List<String> filesProcessed = new ArrayList<String>();
		if(spfa!=null)
		{
			/** Based on spfa id we are fetching files from sourceDirPath to LocalDirPath using Sftp or DropBox**/

//			List<String> filesProcessed = new ArrayList<String>();

			if(sourceConnDetails.getConnectionType().equalsIgnoreCase("SFTP"))
			{
				filesProcessed = fetchFilesFromCloudBasedOnIdUsinSftp(sourceConnDetails, spfa, targetConnection);
			}
			else if(sourceConnDetails.getConnectionType().equalsIgnoreCase("DROP_BOX"))
			{
				filesProcessed = fetchFilesFromCloudBasedOnIdUsingDropBox(sourceConnDetails, spfa, targetConnection);
			}
			else if(sourceConnDetails.getConnectionType().equalsIgnoreCase("GOOGLE_DRIVE"))
			{
				filesProcessed=driveService.fetchFilesFromGoogleDrive(sourceConnDetails, spfa, targetConnection);
			}

			/** From LocalDirPath we are downloading the files to PC by giving path in file.properties **/

			log.info("Files Transfered from SFTP/DropBox/GooleDrive to Recon"+filesProcessed);
		}
		return filesProcessed.size();

	}


	/*public void savingFilesToLocalDirBasedOnSPFAId(SourceConnectionDetails sourceConnDetails, Long SPFAId) 
	{
		log.info("** == ** ==  Service call to save files to the local folder BasedOnSPFAId == ** == **\n");
		log.info("Connecion Name: "+ sourceConnDetails.getName() +", Protocol: "+ sourceConnDetails.getProtocol());

		SourceProfiles sourceProfile = sourceProfilesRepository.findByConnectionId(sourceConnDetails.getId());//1
		if(sourceProfile != null)
		{
			log.info("Profile Name: "+ sourceProfile.getSourceProfileName()+", Connection ID: "+sourceConnDetails.getId());
			SourceProfileFileAssignments spfa = sourceProfileFileAssignmentsRepository.findOne(SPFAId);
			SourceConnectionDetails targetConnection= sourceConnectionDetailsRepository.findByName("Agree Application Document Storage");
			if(spfa!=null)
			{
				Properties props = getPropertiesFromClasspath("File.properties");
				String localPath = props.getProperty("path");

	 *//** Based on spfa id we are fetching files from sourceDirPath to LocalDirPath using Sftp or DropBox**//*

				List<String> filesProcessed = new ArrayList<String>();

				if(sourceConnDetails.getProtocol().equalsIgnoreCase("SFTP"))
				{
					filesProcessed = fetchFilesFromCloudBasedOnIdUsinSftp(sourceConnDetails, spfa, targetConnection);
					log.info("filesProcessed :->"+filesProcessed);
				}
				else if(sourceConnDetails.getProtocol().equalsIgnoreCase("Dropbox"))
				{
					filesProcessed = fetchFilesFromCloudBasedOnIdUsingDropBox(sourceConnDetails, spfa, targetConnection);
					log.info("filesProcessed :->"+filesProcessed);
				}

	  *//** From LocalDirPath we are downloading the files to PC by giving path in file.properties **//*

				List<String> downlodedFiles=downloadFilesToLocalDirPCLevel(targetConnection,spfa,localPath);

	   *//** Here we are giving the LocalDirPath and reading the files which are downloaded **//*

				Long tenantId=sourceConnDetails.getTenantId();
				log.info("Parameters localpath and tenantId:-"+localPath+"---"+tenantId);
				List<HashMap<String, String>> resultsList=readingTheFiles(localPath,tenantId,downlodedFiles);
				log.info("Results List:-"+resultsList);

	    *//** Here we are moving the successfully read files to the processed files folder in local server **//*

				movingFilesToProcessedFilesDir(targetConnection,spfa,resultsList);
//				log.info("******************************* Success Reading and moving Files *********************************");
			}
		}
		else
		{
			log.info("No Profile is Assigned For The Connection ID: "+sourceConnDetails.getId());
		}
	}*/



	/********************** DropBox ***************************/

	/**
	 * Author Kiran
	 * @param sourceConnection
	 * @param spfa
	 * @param targetConnection
	 * @return
	 */
	public List<String> fetchFilesFromCloudBasedOnIdUsingDropBox(SourceConnectionDetails sourceConnection, SourceProfileFileAssignments spfa, SourceConnectionDetails targetConnection) 
	{/*
		log.info("========== *3D* fetching Files FromCloud Based On Id Using DropBox ==========");
		List<String> listofFiles = new ArrayList<String>();

		String ACCESS_TOKEN = sourceConnection.getAccessToken();
		log.info("ACCESS_TOKEN: "+ACCESS_TOKEN);
		String sourceFolderName = spfa.getSourceDirectoryPath();
		log.info("sourceFolderName: "+sourceFolderName);
		DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
		DbxClient client = new DbxClient(config, ACCESS_TOKEN);
		DbxEntry.WithChildren listing;

		String targetFolderPath=spfa.getLocalDirectoryPath();
		log.info("targetFolderPath: "+ targetFolderPath);
		Channel targetchannel = sftpConnection(targetConnection);
		ChannelSftp targetchannelSftp = (ChannelSftp) targetchannel;

		try {
			listing = client.getMetadataWithChildren("/"+sourceFolderName);

			if(listing != null)
			{
				for (DbxEntry child : listing.children){
					if(child.isFile())
					{
						
						String fileNameFormat = null;
						try{
							fileNameFormat = spfa.getFileNameFormat();
							if(fileNameFormat.contains("*"))
							{
								fileNameFormat = fileNameFormat.replaceAll("\\*", "(.*)");
							}
						}
						catch(Exception exp)
						{
							log.debug("fileNameFormat cannot be null");
						}
						
						String fileName = child.name;
						log.info("fileName in 3D:- "+fileName);
						InputStream inputStream = client.startGetFile("/"+ sourceFolderName+"/"+fileName, null).body;
						listofFiles.add(fileName);

						try{

							*//** To get the file name with time stamp (to avoid duplicates)*//*
							DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss"); 
							String filename1 ="";
							String filename2 ="";
							if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
							{
								int val=fileName.lastIndexOf(".");
								 filename1 = fileName.substring(0, val);
								 filename2 = fileName.substring(val, fileName.length());
							}
							String fileWithTimeStamp = filename1 +"_"+ df.format(new Date()) +filename2;
							log.info("TemplateName with time in 3D:- "+fileWithTimeStamp);

							if(!fileName.matches(fileNameFormat))
							{
								continue;
							}
							else{
								InputStream inputStream = client.startGetFile("/"+ sourceFolderName+"/"+fileName, null).body;
								listofFiles.add(fileName);
								
								sftpService.putFile(targetchannelSftp, targetFolderPath, fileName, inputStream);
								client.move("/"+ sourceFolderName +"/"+fileName, "/"+sourceFolderName+"/"+"ProcessedFiles/"+fileName);	
								//sftpService.putFile(targetchannelSftp, targetFolderPath, fileWithTimeStamp, inputStream);
							}
							
							

						}catch(Exception e)
						{ //Exception will occure if file is already is exists in Ex folder
							//client.delete("/"+sourceFolderName+"/"+"Siva_Ex/"+child.name);
							//client.move("/"+ sourceFolderName +"/"+child.name, "/"+sourceFolderName+"/"+"Siva_Ex/"+child.name);	// Moving files into Ex folder drive
						}
					}
				}
			}
		} catch (DbxException e1) 
		{
			e1.printStackTrace();
		}
		log.info("======  Service ended for fetching list of files from dropbox, Size: "+listofFiles.size());*/
		return null;
	}




	/********************* SFTPCode ***************************/

	/**
	 * Author Kiran
	 * @param sourceConnDetails
	 * @param spfa
	 * @param targetConnection
	 * @return (File Sync Methods based on Id)
	 */
	private List<String> fetchFilesFromCloudBasedOnIdUsinSftp(SourceConnectionDetails sourceConnDetails,SourceProfileFileAssignments spfa,SourceConnectionDetails targetConnection) 
	{
		log.info("========== *3S* Service call to transfer files from source to target in server ========== \n");

		/*** Source Connection ***/

		//log.info("Establishing Source connection with SFTP HOST "+ sourceConnDetails.getHost());
		String sourceFolderPath = spfa.getSourceDirectoryPath()+"/";
		log.info("Remote server files path: "+sourceFolderPath);

		String fileNameFormat = null;
		try{
			fileNameFormat = spfa.getFileNameFormat();
			if(fileNameFormat.contains("*"))
			{
				fileNameFormat = fileNameFormat.replaceAll("\\*", "(.*)");
			}
		}
		catch(Exception exp)
		{
			log.debug("fileNameFormat cannot be null");
		}
		Channel channel = sftpConnection(sourceConnDetails);
		ChannelSftp channelSftp = (ChannelSftp) channel;

		/*** target connection ***/

		//log.info("Establishing target connection with SFTP HOST "+ targetConnection.getHost());
		String targetFolderPath = spfa.getLocalDirectoryPath()+"/";
		log.info("Remote target files path: "+targetFolderPath);

		Channel targetchannel = sftpConnection(targetConnection);
		ChannelSftp targetchannelSftp = (ChannelSftp) targetchannel;

		List<String> filesProcessed = new ArrayList<String>();
		try {
			//log.info("Is connected: "+channelSftp.isConnected());
			channelSftp.cd(sourceFolderPath);
			Vector<ChannelSftp.LsEntry> fileList;
			fileList = channelSftp.ls(sourceFolderPath);
//			log.info("fileList:-> "+fileList.size());
//			log.info("and fileNameFormat:-> "+fileNameFormat);
			if(fileList.size()>0)
			{
//				log.info("List of Files processed: "+fileList);
				for(int i=0; i<fileList.size(); i++)
				{
					LsEntry entry = (LsEntry) fileList.get(i);
//					log.info("Entry: "+entry);
					if(entry!=null)
					{
						String fileName=entry.getFilename().toString();
						/*Code to find files based on file format @Author JagannadhaRaju*/
						if(!fileName.matches(fileNameFormat))
						{
							continue;
						}
						/**/
//						log.info("fileName:-> "+fileName+" and fileNameFormat:-> "+fileNameFormat);
						if((!fileName.startsWith(".")) && fileName.contains(".") && fileName!=null && !fileName.equals(""))
						{
							log.info("Strated Loading fileName in 3S:- "+fileName+" StartTime: "+ZonedDateTime.now());
							

							/** To get the file name with time stamp (to avoid duplicates)*/
							/*DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss"); 
							String filename1 ="";
							String filename2 ="";
							if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
							{
								int val=fileName.lastIndexOf(".");
								 filename1 = fileName.substring(0, val);
								 filename2 = fileName.substring(val, fileName.length());
							}
							String fileWithTimeStamp = filename1 +"_"+ df.format(new Date()) +filename2;
							log.info("TemplateName with time in 3S:- "+fileWithTimeStamp);*/

							InputStream inputStream = sftpService.getFile(channel, sourceFolderPath, fileName);	
							sftpService.putFile(targetchannelSftp, targetFolderPath, fileName, inputStream);
							filesProcessed.add(fileName);

							String targetServerPath =sourceFolderPath+"processedFiles";
							Channel channel1 = sftpConnection(sourceConnDetails);
							InputStream inputStream2 = sftpService.getFile(channel, sourceFolderPath, fileName); //If we change this to channel1 it is taking much time to transfer

							ChannelSftp channelSftp1 = (ChannelSftp) channel1;

							try {
								channelSftp1.lstat(targetServerPath);
								sftpService.putFile(channelSftp1, targetServerPath, fileName, inputStream2);
								log.info("Successfully moved files to folder: "+fileName);
							} 
							catch (SftpException e1)
							{
								try {
									log.info("Dir Not exists"+targetServerPath);
									channelSftp1.mkdir(targetServerPath);
									sftpService.putFile(channelSftp1, targetServerPath, fileName, inputStream2);
									log.info("Successfully moved files to newly created folder:"+fileName);
								} catch (SftpException e2) {
									e2.printStackTrace();
								}
							}

							/** To remove file from the source path */
							channelSftp1.rm(sourceFolderPath+fileName);

							try {
								if(inputStream2!=null)
									inputStream2.close();
							} catch (IOException e) 
							{
								e.printStackTrace();
							}
							try {
								if(inputStream!=null)
									inputStream.close();
							} catch (IOException e) 
							{
								e.printStackTrace();
							}
							sftpService.disconnect(channelSftp1);
							sftpService.disconnect(channel1);
							log.info("End of Loading fileName in 3S:- "+fileName+" StartTime: "+ZonedDateTime.now());
						}
					}
				}
			}
			sftpService.disconnect(channel);
			sftpService.disconnect(targetchannel);
			sftpService.disconnect(channelSftp);
			sftpService.disconnect(targetchannelSftp);
		} catch (SftpException e) {
			e.printStackTrace();
		}
		log.info("List of file processed from one Channel to another Channel:- "+filesProcessed);
		return filesProcessed;
	}

	/********************* Common for both Sftp and DropBox ***************************/


	/**
	 * Author Kiran
	 * @param sourceConnDetails
	 * @param spfa
	 * To download the files to local(Computer) From local Dir Path (File Sync Methods based on Id)
	 */


	public List<String> downloadFilesToLocalDirPCLevel(SourceConnectionDetails sourceConnDetails,SourceProfileFileAssignments spfa, String localPath)
	{
		if(spfa!=null & sourceConnDetails!=null)
		{
			log.info("========== *2* Downloading the list of files from the server to PC From ["+spfa.getLocalDirectoryPath()+"] ==========");
			Channel sourceChannel = sftpConnection(sourceConnDetails);
			ChannelSftp channelSftp = (ChannelSftp) sourceChannel;
			String fileNameFormat = null;
			try{
				fileNameFormat = spfa.getFileNameFormat();
				if(fileNameFormat.contains("*"))
				{
					fileNameFormat = fileNameFormat.replaceAll("\\*", "(.*)");
				}
			}
			catch(Exception exp)
			{
				log.debug("fileNameFormat cannot be null");
			}

			String sourceFolderPath = spfa.getLocalDirectoryPath()+"/";
			List<String> downloadedFilesList = new ArrayList<String>();
			try {
				channelSftp.cd(sourceFolderPath);
				Vector fileList;
				fileList = channelSftp.ls(sourceFolderPath);
				if(fileList.size()>0)
				{
					for(int i=0; i<fileList.size(); i++)
					{
						LsEntry entry = (LsEntry) fileList.get(i);
						if(entry!=null)
						{
							String fileName=entry.getFilename().toString();
							/*Code to fetch files qualifying the format @Author Jagannadh Rahu V*/
							if(!fileName.matches(fileNameFormat))
							{
								continue;
							}
							/**/
							if((!fileName.startsWith(".")) && fileName.contains("."))
							{
								log.info("Dowloading fileName"+fileName);
								downloadedFilesList.add(fileName);
								InputStream inputStream = sftpService.getFile(sourceChannel, sourceFolderPath, fileName);	
								//log.info("Local Path to download: "+localPath);
								try {
									File file = new File(localPath+fileName);
									OutputStream outputStream = new FileOutputStream(file);
									try 
									{
										IOUtils.copy(inputStream, outputStream);
										outputStream.close();
									} 
									catch (IOException e) {
										e.printStackTrace();
									}

								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
								try {
									if(inputStream!=null)
										inputStream.close();
								} catch (IOException e) 
								{
									e.printStackTrace();
								}
							}
						}
					}
				}
				sftpService.disconnect(channelSftp);
				sftpService.disconnect(sourceChannel);
			}
			catch (SftpException e) {
				e.printStackTrace();
			}
			log.info("Return DownloadedFilesList"+downloadedFilesList+" and time stamp"+ZonedDateTime.now());
			return downloadedFilesList;
		}
		return null;

	}


	/**
	 * Author Kiran
	 * @param localPath
	 * @param templateId
	 * @param downlodedFiles
	 * @return to get the list of successfully read files status (File Sync Methods based on Id)
	 */

	public List<HashMap<String, String>> readingTheFiles(String localPath, Long templateId,List<String> downlodedFiles, SourceProfileFileAssignments spfaDetails) 
	{
		log.info("========== *3* To get list of files and to read files for file tempalteId: "+templateId+" ==========");
		File folder = new File(localPath);
		File[] listOfFiles = folder.listFiles();
		List<HashMap<String, String>> resList= new ArrayList<HashMap<String,String>>();
		if(folder!=null && folder.length()>0)
		{
			//log.info("listOfFiles in folder length :-"+listOfFiles.length);
			//log.info("downlodedFiles size:"+downlodedFiles.size());
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
								//log.info("FileName in Folder:"+filename+" and path :"+path);
								String result = null;
								log.info("4.Accessing and Reading start for templateId: "+ templateId+" at "+ZonedDateTime.now());
								result = readingFileTemplatesService.readFileTemplateDetailsBasedOnFileType(templateId, path, spfaDetails);
								log.info("4.Accessing and Reading end for templateId: "+ templateId+" at "+ZonedDateTime.now());
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
	}


	/** 
	 * Author Kiran
	 * @param sourceConnDetails
	 * @param spfa
	 * @param resultsList
	 */
	public void movingFilesToProcessedFilesDir(SourceConnectionDetails sourceConnDetails, SourceProfileFileAssignments spfa, List<HashMap<String, String>> resultsList)
	{
		log.info("========== *5* Moving Files from Recon folder to processed Files Folder  =========="+resultsList);

		if(spfa!=null & sourceConnDetails!=null)
		{
			Channel sourceChannel = sftpConnection(sourceConnDetails);
			ChannelSftp channelSftp = (ChannelSftp) sourceChannel;
			String sourceFolderPath = spfa.getLocalDirectoryPath()+"/";
			try {
				channelSftp.cd(sourceFolderPath);
				Vector fileList;
				fileList = channelSftp.ls(sourceFolderPath);
				if(fileList.size()>0)
				{
					for(int i=0; i<fileList.size(); i++)
					{
						LsEntry entry = (LsEntry) fileList.get(i);
						if(entry!=null)
						{
							String fileName=entry.getFilename().toString();
							if((!fileName.startsWith(".")) && fileName.contains("."))
							{
								String filName=null;
								String status = "";
								for(int h=0;h<resultsList.size();h++)
								{
									HashMap<String, String> nameAndStatus = resultsList.get(h);
									String file=nameAndStatus.keySet().toString();

									filName=file.substring(1, file.length()-1);
									status = nameAndStatus.get(filName);
									//log.info("fileName in 5"+filName +" and status :"+status);
									if(filName!=null && status.equalsIgnoreCase("Successfully saved data"))
									{
										if(fileName.equalsIgnoreCase(filName))
										{
											log.info("Moving the file with name "+fileName);
											String targetServerPath =sourceFolderPath+"processedFiles";
											Channel channel1 = sftpConnection(sourceConnDetails);
											InputStream inputStream2 = sftpService.getFile(sourceChannel, sourceFolderPath, fileName);
											ChannelSftp channelSftp1 = (ChannelSftp) channel1;
											try {
												channelSftp1.lstat(targetServerPath);
												sftpService.putFile(channelSftp1, targetServerPath, fileName, inputStream2);
											} 
											catch (SftpException e1)
											{
												try {
													log.info("Directory Not exists"+targetServerPath);
													channelSftp.mkdir(targetServerPath);
													sftpService.putFile(channelSftp1, targetServerPath, fileName, inputStream2);
												} catch (SftpException e2) {
													e2.printStackTrace();
												}
											}
											channelSftp.rm(sourceFolderPath+fileName);

											try {
												if(inputStream2!=null)
													inputStream2.close();
											} catch (IOException e) 
											{
												e.printStackTrace();
											}
											sftpService.disconnect(channel1);
											sftpService.disconnect(channelSftp1);
										}
									}
								}
							}
						}
					}
				}
				sftpService.disconnect(channelSftp);
				sftpService.disconnect(sourceChannel);
			}
			catch (SftpException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Author Kiran
	 * @param connDetails
	 * @return (File Sync Methods based on Id)
	 */
	public Channel sftpConnection(SourceConnectionDetails connDetails)
	{
		Channel channel = null ;
		String Host = connDetails.getHost();
		String prt = connDetails.getPort();
		log.info("port:"+prt);
		int Port = 0;
		Port = Integer.parseInt(prt);
		String UserName = connDetails.getUserName();
		String Password = connDetails.getPassword();
		channel = sftpService.getConnection(Host, Port, UserName, Password);
		return channel;
	}


	/**
	 * Author Kiran
	 * @param propFileName
	 * @return to get the properties (File Sync Methods based on Id)
	 */
	/*public  Properties getPropertiesFromClasspath(String propFileName)
	{
		Properties props = new Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStream == null)
		{
			try {
				throw new FileNotFoundException("property file '" + propFileName
						+ "' not found in the classpath");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		try {
			props.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}*/


	/* code to move files from processed files folder before rading the data*/
	/*public List<String> downloadFilesToLocalDirPCLevel(SourceConnectionDetails sourceConnDetails,SourceProfileFileAssignments spfa, String localPath)
	{
		if(spfa!=null & sourceConnDetails!=null)
		{
			log.info("** ** ** ** Downloading the list of files from the server to PC From ["+spfa.getLocalDirectoryPath()+"]");
			Channel sourceChannel = sftpConnection(sourceConnDetails);
			ChannelSftp channelSftp = (ChannelSftp) sourceChannel;
			String sourceFolderPath = spfa.getLocalDirectoryPath()+"/";
			List<String> downloadedFilesList = new ArrayList<String>();
			try {
				channelSftp.cd(sourceFolderPath);
				Vector fileList;
				fileList = channelSftp.ls(sourceFolderPath);
				if(fileList.size()>0)
				{
					for(int i=0; i<fileList.size(); i++)
					{
						LsEntry entry = (LsEntry) fileList.get(i);
						if(entry!=null)
						{
							String fileName=entry.getFilename().toString();
							if((!fileName.startsWith(".")) && fileName.contains("."))
							{
								log.info("Dowloading fileName"+fileName);
								downloadedFilesList.add(fileName);
								InputStream inputStream = sftpService.getFile(sourceChannel, sourceFolderPath, fileName);	
								log.info("Local Path to download: "+localPath);
								try {
									File file = new File(localPath+fileName);
									OutputStream outputStream = new FileOutputStream(file);
									try 
									{
										IOUtils.copy(inputStream, outputStream);
										outputStream.close();
									} 
									catch (IOException e) {
										e.printStackTrace();
									}

								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
								String targetServerPath =sourceFolderPath+"processedFiles";
								Channel channel1 = sftpConnection(sourceConnDetails);
								InputStream inputStream2 = sftpService.getFile(sourceChannel, sourceFolderPath, fileName);
								ChannelSftp channelSftp1 = (ChannelSftp) channel1;
								try {
									channelSftp1.lstat(targetServerPath);
									sftpService.putFile(channelSftp1, targetServerPath, fileName, inputStream2);
								} 
								catch (SftpException e1)
								{
									try {
										log.info("Directory Not exists"+targetServerPath);
										channelSftp.mkdir(targetServerPath);
										sftpService.putFile(channelSftp1, targetServerPath, fileName, inputStream2);
									} catch (SftpException e2) {
										e2.printStackTrace();
									}
								}
								channelSftp.rm(sourceFolderPath+"/"+fileName);
								try {
									if(inputStream!=null)
										inputStream.close();
								} catch (IOException e) 
								{
									e.printStackTrace();
								}

								try {
									if(inputStream2!=null)
										inputStream2.close();
								} catch (IOException e) 
								{
									e.printStackTrace();
								}
								sftpService.disconnect(channel1);
								sftpService.disconnect(channelSftp1);
							}
						}
					}
				}
				sftpService.disconnect(channelSftp);
				sftpService.disconnect(sourceChannel);
			}
			catch (SftpException e) {
				e.printStackTrace();
			}
			log.info("return downloadedFilesList"+downloadedFilesList);
			return downloadedFilesList;
		}
		return null;

	}*/

	/*public List<String> processingTheFiles(String localPath, Long templateId,List<String> downlodedFiles) 
	{
		log.info("** ** ** ** To get list of files and to read files from Pc path: "+localPath);
		File folder = new File(localPath);
		File[] listOfFiles = folder.listFiles();
		List<String> resList= new ArrayList<String>();
		if(folder!=null && folder.length()>0)
		{
			log.info("listOfFiles length :-"+listOfFiles.length);
			for (int i = 0; i < listOfFiles.length; i++) 
			{
				if(downlodedFiles.size()>0)
				{
					for(int k = 0; k < downlodedFiles.size(); k++)
					{
						if (listOfFiles[i].isFile()) 
						{
							String filename=downlodedFiles.get(k);
							String file = listOfFiles[i].getName();
							if(filename.equalsIgnoreCase(file))
							{
								String path = localPath+filename;
								log.info("FileName in Folder:"+filename+" and path :"+path);
								String result = null;
								try {
									result = readingFileTemplatesService.readFileTemplateDetailsBasedOnFileType(templateId, path);
									log.info("result:->>"+result);
								} catch (IOException e) 
								{
									e.printStackTrace();
								}
								resList.add(result);
							}
						} 
						else if (listOfFiles[i].isDirectory()) {
							//		        System.out.println("Directory " + listOfFiles[i].getName());
						}
					}
				}
			}
		}
		return resList;
	}*/

	/*public void savingFilesToLocalDirBasedOnSPFAId(SourceConnectionDetails sourceConnDetails, Long tenantId, Long SPFAId) throws SftpException, DbxException, IOException
	{
		log.info("** ** ** Service call to save files to the local folder BasedOnSPFAId** ** **");
		log.info("Connecion Name: "+ sourceConnDetails.getName() +", Protocol: "+ sourceConnDetails.getProtocol());

		SourceProfiles sourceProfile = sourceProfilesRepository.findByConnectionIdAndTenantId(sourceConnDetails.getId(), tenantId);//1
		if(sourceProfile != null)
		{
			log.info("Profile Name: "+ sourceProfile.getSourceProfileName()+", Connection ID: "+sourceConnDetails.getId());
			SourceProfileFileAssignments spfa = sourceProfileFileAssignmentsRepository.findOne(SPFAId);
			if(spfa!=null)
			{
				Long templateId=spfa.getTemplateId();
				LinkedHashMap<String, InputStream> fetchedFilesAsInpStrm = new LinkedHashMap<String, InputStream>();
				fetchedFilesAsInpStrm = fetchFilesFromCloud(sourceConnDetails, spfa); // Service call
				if(fetchedFilesAsInpStrm != null)
				{
					if(fetchedFilesAsInpStrm.size()>0)
					{
						File fileDir = new File(spfa.getLocalDirectoryPath()+sourceConnDetails.getName()+"/"+sourceProfile.getSourceProfileName()+"/");
						if (!fileDir.exists()) 
						{
							if(fileDir.mkdirs())
							{
								log.info(spfa.getLocalDirectoryPath()+sourceConnDetails.getName()+"/"+sourceProfile.getSourceProfileName()+"/"+" directory is created.");	
							}
							else
							{
								log.info("Failed to create directory: " + spfa.getLocalDirectoryPath()+sourceConnDetails.getName()+"/"+sourceProfile.getSourceProfileName()+"/"+" directory is created.");
							}
						}
						for(Map.Entry<String, InputStream> entry : fetchedFilesAsInpStrm.entrySet()) {
							String fileName = entry.getKey();	//File name
							InputStream is = entry.getValue();	//inputstream
							fileService.fileUpload(is, key, destination);
							log.info("Downloading "+ fileName + ". . .");

							// To give the name of file included with time stamp by kiran
							DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss"); 
							String fileWithTimeStamp = fileName.split("[.]")[0] +"_"+ df.format(new Date()) + "."+fileName.split("[.]")[1];
							log.info("TemplateName with time:-"+fileWithTimeStamp);

							File file = new File(spfa.getLocalDirectoryPath()+sourceConnDetails.getName()+"/"+sourceProfile.getSourceProfileName()+"/" + fileWithTimeStamp);
							OutputStream outputStream = new FileOutputStream(file);
							IOUtils.copy(is, outputStream);
							outputStream.close();
							log.info("Downloaded "+ fileWithTimeStamp + ".");

						} 
						if(fileDir!=null && fileDir.length()>0)
						{
							String result=readingFileTemplatesService.readFileTemplateDetailsBasedOnFileType(templateId, fileDir);

							log.info("result:->>"+result);
						}
					}
				}

			}
			else
			{
				log.info("No Source Profile Files Are Assigned TO Source ID: "+sourceProfile.getId());
			}
		}
		else
		{
			log.info("No Profile is Assigned For The Connection ID: "+sourceConnDetails.getId());
		}
	}*/



	/*public void movingFilesToProcessedFilesDir123(SourceConnectionDetails sourceConnDetails, SourceProfileFileAssignments spfa, List<HashMap<String, String>> resultsList)
	{
		log.info("** ** ** Moving Files from Recon folder to processed Files Folder ** ** **"+resultsList);

		Channel sourceChannel = sftpConnection(sourceConnDetails);
		ChannelSftp channelSftp = (ChannelSftp) sourceChannel;
		String sourceFolderPath = spfa.getLocalDirectoryPath()+"/";

		String targetServerPath =sourceFolderPath+"processedFiles";
		Channel channel1 = sftpConnection(sourceConnDetails);

		//		resultsList.

		try {
			channelSftp.cd(sourceFolderPath);
			Vector fileList;
			fileList = channelSftp.ls(sourceFolderPath);
			if(fileList.size()>0)
			{
				for(int i=0; i<fileList.size(); i++)
				{
					LsEntry entry = (LsEntry) fileList.get(i);
					if(entry!=null)
					{
						String fileNme=entry.getFilename().toString();
						if((!fileNme.startsWith(".")) && fileNme.contains("."))
						{
							String fileName=null;
							String status = "";
							for(int h=0;h<resultsList.size();h++)
							{
								HashMap<String, String> nameAndStatus = resultsList.get(h);
								String file=nameAndStatus.keySet().toString();

								fileName=file.substring(1, file.length()-1);
								status = nameAndStatus.get(fileName);
								log.info("fileName"+fileName +" and status :"+status);
								if(fileName!=null && status.equalsIgnoreCase("Successfully saved data"))
								{
									if(fileNme.equalsIgnoreCase(fileName))
									{

										InputStream inputStream2 = sftpService.getFile(sourceChannel, sourceFolderPath, fileName);
										ChannelSftp channelSftp1 = (ChannelSftp) channelSftp;
										try {
											channelSftp1.lstat(targetServerPath);
											sftpService.putFile(channel1, targetServerPath, fileName, inputStream2);
										} 
										catch (SftpException e1)
										{
											try {
												log.info("Directory Not exists"+targetServerPath);
												channelSftp1.mkdir(targetServerPath);
												sftpService.putFile(channel1, targetServerPath, fileName, inputStream2);
											} catch (SftpException e2) {
												e2.printStackTrace();
											}
										}
										try
										{
											channelSftp.rm(sourceFolderPath+"/"+fileName);
										} catch (SftpException e) {
											e.printStackTrace();
										}
										try {
											if(inputStream2!=null)
												inputStream2.close();
										} catch (IOException e) 
										{
											e.printStackTrace();
										}
										sftpService.disconnect(channel1);
										sftpService.disconnect(channelSftp1);
									}
								}
							}
						}
					}
				}
			}
			sftpService.disconnect(channelSftp);
			sftpService.disconnect(sourceChannel);
		}
		catch (SftpException e) {
			e.printStackTrace();
		}
	}*/

}
