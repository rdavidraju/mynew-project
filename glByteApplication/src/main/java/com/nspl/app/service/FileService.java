package com.nspl.app.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.Properties;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.nspl.app.domain.SourceConnectionDetails;
import com.nspl.app.web.rest.dto.ErrorReport;

@Service
public class FileService {
	
	private final Logger log = LoggerFactory.getLogger(FileService.class);
	
	@Inject
	SFTPUtilService sftpService;
	
	private String sftpFileTargetFolder;
	
	@Inject
	PropertiesUtilService propertiesUtilService;
	
	 @Autowired
		Environment env;
	
	
	/**
	 * Author : Shiva
	 * @param inputStream
	 * @param fileName
	 * @param year
	 * @param companyId
	 * @param unitId
	 * @param fileType (report or templates)
	 * @return String[] which returns upload status
	 */
	/*public void fileUpload(InputStream inputStream, String fileName, DriveInformation sftpInfo)
	{
		log.debug("Uploading the file: "+fileName);
		String destPath = "";
		String sftpFilePath1 = "/var/www/html"; //fpd.getSftpFilePath1();
		String sftpFilePath2 = "/RECON"; //fpd.getSftpFilePath2();
		sftpFileTargetFolder = sftpFilePath1 + sftpFilePath2;
		log.info("sftpFileTargetFolder in fileUpload " +sftpFileTargetFolder);
		
		destPath = sftpFileTargetFolder+sftpInfo.getInboundFolderPath()+"/";

		log.info("destPath:"+destPath);

		String Host = sftpInfo.getHostName();
		int Port = sftpInfo.getPortNumber();
		String UserName = sftpInfo.getUserName();
		String Password = sftpInfo.getPassowrd();

		Channel channel = sftpService.getConnection(Host, Port, UserName, Password);

		try
		{
			sftpService.createDirectory(channel, destPath);
			sftpService.putFile(channel, destPath, fileName,inputStream);
			sftpService.disconnect(channel);
		}
		catch(Exception exp)
		{
			log.error("Error while creating directory");
		}
	}*/
	
	/* Moving files form in to out in server path*/
	
	/** 
	 * Author Shiva
	 * @param inputStream
	 * @param fileName
	 * @param connection
	 */
	public void movingFile(InputStream inputStream, String fileName, SourceConnectionDetails connection)
	{
		log.debug("Moving file from one folder to another folder service called. "+fileName);
		String destPath = "";
		String sftpFilePath1 = "/var/www/html";
		String sftpFilePath2 = "/RECON";
		sftpFileTargetFolder = sftpFilePath1 + sftpFilePath2;
		log.info("sftpFileTargetFolder in fileUpload " +sftpFileTargetFolder);		
		/*destPath = sftpFileTargetFolder+sftpInfo.getOutboundFolderPath()+"/";*/
		destPath = sftpFileTargetFolder+"/drive/out/";
		log.info("destPath:"+destPath);
		String Host = connection.getHost();
		int Port = 22;
		String UserName = connection.getUserName();
		String Password = connection.getPassword();

		Channel channel = sftpService.getConnection(Host, Port, UserName, Password);
		try
		{
			sftpService.createDirectory(channel, destPath);
			sftpService.putFile(channel, destPath, fileName,inputStream);
			sftpService.disconnect(channel);
		}
		catch(Exception exp)
		{
			log.error("Error while creating directory");
		}
	}
	
	
	public String[] fileSave(String fileName){

		
		Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");
		String[] fileDownload = new String[2];
		fileDownload[0] = "success";	
		String Host = props.getProperty("sftpHost");
		int Port = Integer.valueOf(props.getProperty("sftpPort"));
		String UserName = props.getProperty("sftpUserName");
		String Password = props.getProperty("sftpPassword");
		String sourceFolder = props.getProperty("sftpTemplateTargetFolder");
		String destPath = props.getProperty("localTempLocation");
		log.info("destPath:-1"+destPath);
		destPath = destPath+"temp";
		//destPath = destPath+companyId+"\\"+year+"\\"+unitId;//+".xlsx"
		log.info("destPath:-2"+destPath);
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		try {
			log.info("entered");
			JSch jsch = new JSch();
			session = jsch.getSession(UserName, Host, Port);
			session.setPassword(Password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(sourceFolder);
			byte[] buffer = new byte[1024];
			log.info("fileName :"+fileName);
			BufferedInputStream bis = new BufferedInputStream(channelSftp.get(fileName));
			File f=new File(destPath);
			if(!f.exists())
			{
				f.mkdir();
			}
			destPath = destPath+"/"+fileName;
			log.info("destPath before write:"+destPath);
			File newFile = new File(destPath);
			OutputStream os = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			int readCount;
			while ((readCount = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, readCount);
			}
			bis.close();
			bos.close();
			log.info("destPath in her:"+destPath);
			fileDownload[1] =  destPath;
			//}
			return fileDownload;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fileDownload;

	}
	
	
	
	   public ErrorReport downloadCostStatementTemplate(String fileName)
	    {
	    	log.info("method to download static template");
	    	ErrorReport downloadReport = new ErrorReport();
	    	String[] fileDownload = new String[2];
			fileDownload = fileSave(fileName);
			downloadReport.setTaskName("Download Report");
			downloadReport.setTaskStatus(fileDownload[0]);
			downloadReport.setDetails(fileDownload[1]);
	    	return downloadReport;
	    	
	    }
	   
	   
	   
	   public String[] fileUpload(InputStream inputStream, String fileName)
		{
			log.debug("Report fileupload call with file name :"+fileName);
			String[] successMsg = new String[2];
			successMsg[0] = "failure";
			
		//	Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
		log.info("env.getProperty(sftpProperties.sftpHost) :"+env.getProperty("sftpProperties.sftpHost"));
			sftpFileTargetFolder = env.getProperty("sftpProperties.sftpFilePath1") + env.getProperty("sftpProperties.sftpFilePath2");
			log.info("sftpFileTargetFolder in Reports fileUpload " +sftpFileTargetFolder);

			log.info("Reports destPath:"+sftpFileTargetFolder);

			String Host = env.getProperty("sftpProperties.sftpHost");
			log.info("env.getProperty(sftpProperties.sftpPort) :"+env.getProperty("sftpProperties.sftpPort"));
			String sftpPort=env.getProperty("sftpProperties.sftpPort").toString();
			int Port = Integer.valueOf(sftpPort);
			String UserName = env.getProperty("sftpProperties.sftpUserName");
			String Password = env.getProperty("sftpProperties.sftpPassword");

			
			Channel channel = sftpService.getConnection(Host, Port, UserName, Password);

			try
			{
				//sftpService.createDirectory(channel, sftpFileTargetFolder);
				sftpService.putFile(channel, sftpFileTargetFolder, fileName,inputStream);
				successMsg [0]= "success";
				successMsg[1] = env.getProperty("sftpProperties.sftpFilePath2")+fileName;
				sftpService.disconnect(channel);
			}
			catch(Exception exp)
			{
				log.error("Error while creating directory");
			}

			log.info("successMsg :"+successMsg[0]);
			log.info("destPath :"+successMsg[1]);
			return successMsg;
		}
	   
}
