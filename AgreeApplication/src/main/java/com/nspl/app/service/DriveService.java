package com.nspl.app.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dropbox.core.DbxException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.nspl.app.domain.SourceConnectionDetails;
import com.nspl.app.domain.SourceProfileFileAssignments;

@Service
public class DriveService {

	
    private final Logger log = LoggerFactory.getLogger(DriveService.class);
    
    @Inject
    FileService fileService;
    
	@Inject
	SFTPUtilService sftpService;
	
	@Inject
	SourceConnectionDetailsService sourceConnectionDetailsService;
    
	private static final String APPLICATION_NAME = "Google Drive API";
	
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/drive-java-quickstart");
	
	private static FileDataStoreFactory DATA_STORE_FACTORY;
	
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	
	private static HttpTransport HTTP_TRANSPORT;
	
	private static final java.util.Collection<String> SCOPES = DriveScopes.all();
	
	static{
		try {
	            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
	        } catch (Throwable t) {
	            t.printStackTrace();
	            System.exit(1);
	        }
	}
		
	
	/*
	
	 * @throws IOException **/
	public List<String> fetchFilesFromGoogleDrive(SourceConnectionDetails source,SourceProfileFileAssignments sourceProfilePath,SourceConnectionDetails targetConnection) throws IOException
	{
		log.info("Service called for fetching list of files as inputstreams from Google Drive");
		LinkedHashMap<String, InputStream> inputStreams = new LinkedHashMap<String, InputStream>();
		List<String> finalProcessedFiles=new ArrayList<String>();
		 InputStream in =new ByteArrayInputStream(source.getAccessToken().toString().getBytes(StandardCharsets.UTF_8));
		 GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		 log.info("clientSecrets: "+clientSecrets);
		 GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
	                .setDataStoreFactory(DATA_STORE_FACTORY)
	                .setAccessType("offline")
	                .build();
		 log.info("flow :"+flow);
	      Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	      log.info("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());   
	      Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	      log.info("service :"+service);
			//Fetching list of files from drive folder
	      FileList result = service.files().list().execute();
	      log.info("result :"+result);
	      List<File> files = result.getItems();//List of folder in a drive
	      log.info("files :"+files.size());
	      String sourceFolderId = null;
	      List<String> processId=new ArrayList<String>();
	     
	      
	      if (files == null || files.size() == 0) {
	          log.info("No files found.");
	      } else {
	          for (File file : files) {	//looping all folders in a drive
	        	  	log.info("sourceProfilePath.getSourceDirectoryPath() before: "+ sourceProfilePath.getSourceDirectoryPath());
	        	  	log.info("file.getTitle() before "+ file.getTitle());
	            	if(sourceProfilePath.getSourceDirectoryPath().equalsIgnoreCase(file.getTitle()))
	            	{	//Getting Source Folder Name and ID
		        	  	log.info("sourceProfilePath.getSourceDirectoryPath() in if: "+ sourceProfilePath.getSourceDirectoryPath());
		        	  	log.info("file.getTitle() in if "+ file.getTitle());
		        	  	sourceFolderId = file.getId();
		                log.info("source folder id: " + file.getId() + "Folder Name: " + file.getTitle());
	            	}
	           }
	       }
	      
	      if(sourceFolderId != null)
	      {
	    	  // Creating Processed Folder
	    	  File processedFileId =null;

	    	  for (File file : files) {

	    		  if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
	    			  HttpResponse resp = service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl())).execute();
	    			  log.info("resp :"+resp);
	    			  log.info("Downloading " + file.getTitle() + " file..." + " ID: "+file.getId()+"Response Code: "+resp.getStatusCode());
	    			  InputStream is = resp.getContent();

	    			  

	    			  //*** first==>downloading files from client server(44) to local server(145)****//*


	    			  Channel targetchannel = sourceConnectionDetailsService.sftpConnection(targetConnection);
	    			  ChannelSftp targetchannelSftp = (ChannelSftp) targetchannel;
	    			  String targetFolderPath = sourceProfilePath.getLocalDirectoryPath()+"/";
	    			  log.info("targetFolderPath :"+targetFolderPath);
	    			  sftpService.putFile(targetchannelSftp, targetFolderPath, file.getTitle(), is);
	    			  finalProcessedFiles.add(file.getTitle());
	    		  }

	    	  }
	    	  
	    	  for (File file : files) {
	    		  processId.add(file.getTitle());
	    		  
	    	  }
	    	  
	    	  
	    	  log.info("processId :"+processId);
	    	  if(!(processId.contains("ProcessedFiles")))
	    	  {
	    		  log.info("folder not exists");
	    		  File fileMetadata = new File();
	    		  fileMetadata.setTitle("ProcessedFiles");
	    		  fileMetadata.setMimeType("application/vnd.google-apps.folder");

	    		  processedFileId = service.files().insert(fileMetadata)
	    				  .setFields("id")
	    				  .execute();
	    	  }
	    	  else
	    	  {
	    		  for (File file : files)
	    		  {
	    			  if(file.getTitle().equalsIgnoreCase("ProcessedFiles"))
	    			  {
	    				  log.info("file.getTitle() :"+file.getTitle());
	    				  processedFileId=file;
	    			  }
	    			 
	    		  }
	    		  
	    	  }
	    	  
	    	//  for(int i=0;i<files.size()-1;i++)
	    	//  {
	    		  Children.List request = service.children().list(sourceFolderId);
	    		  ChildList children = request.execute();
	    	        for (ChildReference child : children.getItems())
	    	        {
	    	        	log.info("child.getId() :"+child.getId());
	    		  log.info("processedFileId :"+processedFileId);
	    		  if(processedFileId!=null)
	    		  {

	    		  // Moving Processed Folder into Source Folder
	    			  log.info("sourceFolderId :"+sourceFolderId);
	    		  service.files().update(child.getId(),null)
	    		  .setAddParents(processedFileId.getId())
	    		  .setRemoveParents(sourceFolderId)
	    		  .setFields("id, parents")
	    		  .execute();
	    		  }
	    	        }
	    	
	    	//  }


	      }
	     
		return finalProcessedFiles;
	}
	
	
	
	/**
	 * Author ravali
	 * @throws IOException
	 * @throws DbxException 
	 * Desc: to check google drive connection details
	 * **/
	public HashMap checkGoogleConnection(SourceConnectionDetails source) 
	{
		HashMap map=new HashMap();
		 InputStream in =new ByteArrayInputStream(source.getAccessToken().toString().getBytes(StandardCharsets.UTF_8));
		 GoogleClientSecrets clientSecrets;
		 GoogleAuthorizationCodeFlow flow;
		try {
			clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
			 log.info("clientSecrets: "+clientSecrets);
			 flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
	            .setDataStoreFactory(DATA_STORE_FACTORY)
	            .setAccessType("online")
	            .build();
			 
	log.info("flow :"+flow);
	Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	log.info("credential :"+credential);
	credential.getClientAuthentication();
	log.info("credential.getClientAuthentication() :"+credential.getClientAuthentication());
	log.info("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath()); 
	Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	
    log.info("service :"+service);
   
    
    About about = service.about().get().execute();
   
    log.info("Current user name: " + about.getName());
    log.info("Root folder ID: " + about.getRootFolderId());
    log.info("Total quota (bytes): " + about.getQuotaBytesTotal());
    log.info("Used quota (bytes): " + about.getQuotaBytesUsed());
   // about.clear();
    log.info("about.remove(service) :"+about.remove(service));
    FileList result = service.files().list().execute();
    log.info("result :"+result);
    //service.
    map.put("status", "Success");
    map.put("message", about.getName());
	return map;
		} 
		 
	      catch (IOException e) {
			// TODO Auto-generated catch block
	    	  
	    	//  log.info(" error if any "+e.toString());
	    	  map.put("status", "failure");
	    	    map.put("message","userName:"+e.getLocalizedMessage() );
			e.printStackTrace();
			return map;
		}
	      
	}

	
	
	
	

}
