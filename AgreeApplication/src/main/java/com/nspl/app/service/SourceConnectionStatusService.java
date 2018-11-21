package com.nspl.app.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.security.auth.login.CredentialNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxAPIException;
import com.box.sdk.BoxUser;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.NetworkIOException;
//import com.dropbox.core.DbxException;
//import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.About;
import com.jcraft.jsch.*;
import com.nspl.app.domain.SourceConnectionDetails;
import com.nspl.app.repository.SourceConnectionDetailsRepository;
import com.nspl.app.web.rest.dto.DriveConnectionDTO;
import com.nspl.app.web.rest.dto.SourceConcAndSrcProfileDto;

/**
 * Author Kiran
 * @author nspl-004
 *
 */
@Service
@Transactional
public class SourceConnectionStatusService {

	private static final Logger log = LoggerFactory.getLogger(SourceConnectionStatusService.class);

	@Inject
	SourceConnectionDetailsRepository sourceConnectionDetailsRepository;

	//	private static final String APPLICATION_NAME = "GoogleDriveAPI";

	//	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/drive_credentials2");

	private static FileDataStoreFactory DATA_STORE_FACTORY;

	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static HttpTransport HTTP_TRANSPORT;

	private static final java.util.Collection<String> SCOPES = DriveScopes.all();

	//	static{
	//		try {
	//			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	//			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
	//		} catch (Throwable t) {
	//			t.printStackTrace();
	//			System.exit(1);
	//		}
	//	}

	private static HttpRequestInitializer setHttpTimeout(final HttpRequestInitializer requestInitializer) {
		return new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest httpRequest) throws IOException {
				requestInitializer.initialize(httpRequest);
				httpRequest.setConnectTimeout(8 * 80000);  // 4 minutes connect timeout
				httpRequest.setReadTimeout(8 * 80000);  // 4 minutes read timeout
				log.info("Handling Sockect Time Exception");
			}
		};
	}



	public static HashMap<String, String> checkSFTConnection(SourceConnectionDetails sourceConn)
	{
		log.info("<==== Checking Sftp Connection Status");
		HashMap<String, String> hMap=new HashMap<String, String>();
		com.jcraft.jsch.Session session = null;
		Channel channel = null;
		try 
		{
			JSch jsch = new JSch();

			int port = 0;
			try{
				port = Integer.parseInt(sourceConn.getPort());}
			catch(NumberFormatException ex)
			{

				hMap.put("status", "failure");
				hMap.put("message", "Port Number Exception");
				log.info("NumberFormat Exception for port: "+(sourceConn.getPort()));
				//System.exit(0);
			}

			session = jsch.getSession(sourceConn.getUserName(), sourceConn.getHost(), port);
			session.setPassword(sourceConn.getPassword());
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			log.info("==========config========="+config);
			session.connect();
			channel = session.openChannel("sftp");
			log.info("==========channel=========="+channel);
			channel.connect(360000);
			log.info("success connection");
			hMap.put("status", "Sftp Connection - Success");
			hMap.put("message", "Username: "+sourceConn.getUserName());
			log.info("Success Connection");
		} 
		catch(JSchException j)
		{
			hMap.put("status", "SFTP Connection - Failed");
			hMap.put("message", "In-valid Connection Parameters");
			j.printStackTrace();
		}
		catch (Exception ex) 
		{
			hMap.put("status", "SFTP Connection - Failed");
			hMap.put("message", ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		if(session!=null)
		{
			session.disconnect();
		}
		if(channel!=null)
		{
			channel.disconnect();
		}
		return hMap ;
	}



	public static GoogleClientSecrets getClientSecrets(String clientId, String clientSecrete)
	{
		GoogleClientSecrets secrets = new GoogleClientSecrets();
		Details details = new GoogleClientSecrets.Details();
		details.setClientId(clientId);
		details.setClientSecret(clientSecrete);
		secrets.setWeb(details);
		return secrets;
	}



	public static HashMap<String, String> checkGoogleConnection(SourceConnectionDetails sourceConn, Long userId) 
	{

		String credientialsPath="drive_credentials_"+userId;
		String APPLICATION_NAME = "GoogleDriveAPI";
		log.info("Checking Google Drive Connection Status and credientialsPath: "+credientialsPath);

		HashMap<String, String> map=new HashMap<String, String>();
		String ACCESS_TOKEN=null;

		if(sourceConn.getAccessToken()!=null && !sourceConn.getAccessToken().isEmpty())
		{
			ACCESS_TOKEN=sourceConn.getAccessToken();
		}
		else
		{
			map.put("status", "Google Drive Connection - Failed");
			map.put("message","Access Token is Not given" );
			return map;
		}
		ObjectMapper objMapper = new ObjectMapper();
		Credential credential=null;
		Drive driveService = null;
		DriveConnectionDTO diveDto=new DriveConnectionDTO();
		log.info("ACCESS_TOKEN: "+ACCESS_TOKEN);
		try 
		{

			java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/"+credientialsPath);

			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);

			diveDto = objMapper.readValue(ACCESS_TOKEN, DriveConnectionDTO.class);
			log.info("Client_id: "+diveDto.getClient_id()+"\nClient_secret: "+diveDto.getClient_secret());

			String clientId=diveDto.getClient_id();
			String clientSecrete=diveDto.getClient_secret();

			log.info("clientId: "+clientId);
			log.info("clientSecrete: "+clientSecrete);


			log.info("\n <==== Checking Google Drive Connection Status using client id");


			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, getClientSecrets(clientId,clientSecrete), SCOPES)
			.setDataStoreFactory(DATA_STORE_FACTORY)
			.setAccessType("offline")
			.build();
			log.info("flow CredentialDataStore:- "+flow.getCredentialDataStore());
			credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
			log.info("credential: "+credential.getAccessToken());
		}
		catch(CredentialNotFoundException c)
		{
			log.info("Credential Not Found Exception");
			map.put("status", "Google Drive Connection - Failed");
			map.put("message","Credential Not Found" );
		}
		catch(UnrecognizedPropertyException u)
		{
			log.info("Credential Not Found Exception");
			map.put("status", "Google Drive Connection - Failed");
			map.put("message","In-valid Credentials Json" );
		}
		catch(JsonMappingException j){
			log.info("JsonMappingException");
			map.put("status", "Google Drive Connection - Failed");
			map.put("message","In-valid Json" );
			j.printStackTrace();
		}
		catch(Exception e)
		{
			log.info("Error while getting Client Secret");
			map.put("status", "Google Drive Connection - Failed");
			map.put("message","Network not reachable" );
			e.printStackTrace();
		}

		if(credential!=null)
			driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, setHttpTimeout(credential)).setApplicationName(APPLICATION_NAME).build();

		About about;

		try 
		{
			if(driveService!=null)
			{
				log.info("driveService:- "+driveService.about().get());
				about = driveService.about().get().execute();
				log.info("Current user name: " + about.getName());

				map.put("status", "Google Drive Connection - Success");
				map.put("message", "Username: "+about.getName());
				log.info("Success Connection..");
			}
		} 
		catch(UnknownHostException ex)
		{
			map.put("status", "Google Drive Connection - Failed");
			map.put("message","Network not available" );
			ex.printStackTrace();
		}
		catch (NullPointerException e) 
		{
			map.put("status", "Google Drive Connection - Failed");
			map.put("message","Access Token is Not Given "+e.getLocalizedMessage() );
			e.printStackTrace();
			//return map;
		}
		catch(TokenResponseException t){
			log.info("TokenResponseException Unauthorized");
			map.put("status", "Google Drive Connection - Failed");
			map.put("message","Unauthorized Token" );
		}
		catch (IOException e) 
		{
			log.info("IOException");
			map.put("status", "Google Drive Connection - Failed");
			map.put("message","Network not reachable" );
			e.printStackTrace();
			//return map;
		}
		return map;
	}


	public static HashMap<String, String> checkDropBoxConnection(SourceConnectionDetails sourceConn)
	{
		log.info("<==== Checking Drop Box Connection Status");

		HashMap<String, String> hMap= new HashMap<String, String>();
		String ACCESS_TOKEN=null;
		if(sourceConn.getAccessToken()!=null && !sourceConn.getAccessToken().isEmpty())
		{
			ACCESS_TOKEN=sourceConn.getAccessToken();
		}
		DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
		DbxClientV2 client = null;
		if(ACCESS_TOKEN!=null)
			client = new DbxClientV2(config, ACCESS_TOKEN);

		FullAccount account;
		try 
		{
			account = client.users().getCurrentAccount();
			log.info("Connected with drop box using account: "+account.getName().getDisplayName());
			hMap.put("status", "Drop Box Connection - Success");
			hMap.put("message", "Username: "+account.getName().getDisplayName());
			log.info("Success Connection");
		} 
		catch (NullPointerException e) 
		{
			hMap.put("status", "Drop Box Connection - Failed");
			hMap.put("message", "Access Token is Not Given");
			e.printStackTrace();
		}
		catch(NetworkIOException n)
		{
			hMap.put("status", "Drop Box Connection - Failed");
			hMap.put("message", "Network not available");
			n.printStackTrace();
		}
		catch (DbxException e) 
		{
			hMap.put("status", "Drop Box Connection - Failed");
			hMap.put("message", "Cannot access");
			e.printStackTrace();
		}
		return hMap;
	}


	public static HashMap<String, String> checkBoxConnection(SourceConnectionDetails sourceConn)
	{
		log.info("<==== Checking Box Connection Status");
		HashMap<String, String> hMap= new HashMap<String, String>();
		String ACCESS_TOKEN=null;
		if(sourceConn.getAccessToken()!=null && !sourceConn.getAccessToken().isEmpty())
		{
			ACCESS_TOKEN=sourceConn.getAccessToken();
		}
		try
		{
			BoxAPIConnection api = new BoxAPIConnection(ACCESS_TOKEN);
			//			api.setAutoRefresh(true);
			if(BoxUser.getCurrentUser(api)!=null)
			{
				BoxUser user =BoxUser.getCurrentUser(api);
				BoxUser.Info info = user.getInfo();
				String in = info.getLogin();
				log.info("Connected using "+in);

				hMap.put("status", "Box Connection - Success");
				hMap.put("message", "Account: "+in);
				log.info("Success Connection");
			}
			log.info("Folder: "+api.getBaseUploadURL());
		}
		catch(BoxAPIException e)
		{
			hMap.put("status", "Box Connection - Failed");
			hMap.put("message", "In-Valid Access Token");
			e.printStackTrace();
		}
		return hMap;
	}

	public static HashMap<String, String> checkEmailConnection(SourceConnectionDetails sourceConn)
	{
		log.info("<==== Checking Email Connection Status");
		HashMap<String, String> hMap= new HashMap<String, String>();

		String emailType="gmail";

		String from = "carat.nspl@gmail.com";
		String pass ="Welcome!23";
		// Recipient's email ID needs to be mentioned.
		String to = "nishanthteam@gmail.com";

		String host =null;
		if(emailType.equals("yahoo"))
			host = "smtp.mail.yahoo.com";
		else if(emailType.equals("gmail"))
			host = "smtp.gmail.com";

		log.info("host: "+host);
		// Get system properties
		Properties properties = System.getProperties();
		// Setup mail server
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.user", from);
		properties.put("mail.smtp.password", pass);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		//   session.


		try{
			// Create a default MimeMessage object.
			/* MimeMessage message = new MimeMessage(session);

	      // Set From: header field of the header.
	      message.setFrom(new InternetAddress(from));

	      // Set To: header field of the header.
	      message.addRecipient(Message.RecipientType.TO,
	                               new InternetAddress(to));

	      // Set Subject: header field
	      message.setSubject("This is the Subject Line!");

	      // Now set the actual message
	      message.setText("This is actual message");*/

			// Send message
			Transport transport = session.getTransport("smtp");
			log.info("From: "+from+" pass: "+pass);
			transport.connect(host, from, pass);
			// transport.isConnected();
			if(transport.isConnected())
			{
				hMap.put("status", "Email Connection - Success");
				hMap.put("message", "Account Name: "+transport.getURLName().getUsername());
				log.info("Success Connection");
			}

			//  transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			//log.info("Sent message successfully....");
		}
		catch(AuthenticationFailedException a)
		{
			log.info("Sorry your credentials are incorrect");
			hMap.put("status", "Email Connection - Failed");
			hMap.put("message", "Credentials are incorrect");
			a.printStackTrace();
		}
		catch (MessagingException mex) {
			mex.printStackTrace();
		}

		return hMap;
	}


	public static SourceConnectionDetails saveSourceConnectionDetails(SourceConcAndSrcProfileDto sourceConcAndSrcProfileDto, SourceConnectionDetails sourceConnectionDetails, Long userId, Long tenantId)
	{
		//		SourceConnectionDetails sourceConnectionDetails=new SourceConnectionDetails();
		if(sourceConcAndSrcProfileDto.getId() != null)
			sourceConnectionDetails.setId(sourceConcAndSrcProfileDto.getId());
		if(sourceConcAndSrcProfileDto.getIdForDisplay() != null)
			sourceConnectionDetails.setIdForDisplay(sourceConcAndSrcProfileDto.getIdForDisplay());
		if(sourceConcAndSrcProfileDto.getName()!=null && !sourceConcAndSrcProfileDto.getName().isEmpty())
			sourceConnectionDetails.setName(sourceConcAndSrcProfileDto.getName());
		if(sourceConcAndSrcProfileDto.getDescription()!=null && !sourceConcAndSrcProfileDto.getDescription().isEmpty())
			sourceConnectionDetails.setDescription(sourceConcAndSrcProfileDto.getDescription());
		if(sourceConcAndSrcProfileDto.getProtocol()!=null && !sourceConcAndSrcProfileDto.getProtocol().isEmpty())
			sourceConnectionDetails.setProtocol(sourceConcAndSrcProfileDto.getProtocol());
		if(sourceConcAndSrcProfileDto.getClientKey()!=null && !sourceConcAndSrcProfileDto.getClientKey().isEmpty())
			sourceConnectionDetails.setClientKey(sourceConcAndSrcProfileDto.getClientKey());
		if(sourceConcAndSrcProfileDto.getClientKey()!=null && !sourceConcAndSrcProfileDto.getClientKey().isEmpty())
			sourceConnectionDetails.setClientSecret(sourceConcAndSrcProfileDto.getClientKey());
		if(sourceConcAndSrcProfileDto.getAuthEndpointUrl()!=null && !sourceConcAndSrcProfileDto.getAuthEndpointUrl().isEmpty())
			sourceConnectionDetails.setAuthEndpointUrl(sourceConcAndSrcProfileDto.getAuthEndpointUrl());
		if(sourceConcAndSrcProfileDto.getTokenEndpointUrl()!=null && !sourceConcAndSrcProfileDto.getTokenEndpointUrl().isEmpty())
			sourceConnectionDetails.setTokenEndpointUrl(sourceConcAndSrcProfileDto.getTokenEndpointUrl());
		if(sourceConcAndSrcProfileDto.getCallBackUrl()!=null && !sourceConcAndSrcProfileDto.getCallBackUrl().isEmpty())
			sourceConnectionDetails.setCallBackUrl(sourceConcAndSrcProfileDto.getCallBackUrl());
		if(sourceConcAndSrcProfileDto.getHost()!=null && !sourceConcAndSrcProfileDto.getHost().isEmpty())
			sourceConnectionDetails.setHost(sourceConcAndSrcProfileDto.getHost());
		if(sourceConcAndSrcProfileDto.getUserName()!=null && !sourceConcAndSrcProfileDto.getUserName().isEmpty())
			sourceConnectionDetails.setUserName(sourceConcAndSrcProfileDto.getUserName());
		if(sourceConcAndSrcProfileDto.getPassword()!=null && !sourceConcAndSrcProfileDto.getPassword().isEmpty())
			sourceConnectionDetails.setPassword(sourceConcAndSrcProfileDto.getPassword());
		if(sourceConcAndSrcProfileDto.getUrl()!=null && !sourceConcAndSrcProfileDto.getUrl().isEmpty())
			sourceConnectionDetails.setUrl(sourceConcAndSrcProfileDto.getUrl());
		if(sourceConcAndSrcProfileDto.getConnectionType()!=null && !sourceConcAndSrcProfileDto.getConnectionType().isEmpty())
			sourceConnectionDetails.setConnectionType(sourceConcAndSrcProfileDto.getConnectionType());
		if(sourceConcAndSrcProfileDto.getAccessToken()!=null && !sourceConcAndSrcProfileDto.getAccessToken().isEmpty())
			sourceConnectionDetails.setAccessToken(sourceConcAndSrcProfileDto.getAccessToken());
		if(sourceConcAndSrcProfileDto.getPort()!=null && !sourceConcAndSrcProfileDto.getPort().isEmpty())
			sourceConnectionDetails.setPort(sourceConcAndSrcProfileDto.getPort());
		if(sourceConcAndSrcProfileDto.getEnabledFlag()!=null)
			sourceConnectionDetails.setEnabledFlag(sourceConcAndSrcProfileDto.getEnabledFlag());
		sourceConnectionDetails.setCreatedDate(ZonedDateTime.now());
		sourceConnectionDetails.setLastUpdatedDate(ZonedDateTime.now());
		sourceConnectionDetails.setCreatedBy(userId);
		sourceConnectionDetails.setLastUpdatedBy(userId);
		sourceConnectionDetails.setTenantId(tenantId);
		if(sourceConcAndSrcProfileDto.getStartDate()!=null)
			sourceConnectionDetails.setStartDate(sourceConcAndSrcProfileDto.getStartDate());
		if(sourceConcAndSrcProfileDto.getEndDate()!=null)
			sourceConnectionDetails.setEndDate(sourceConcAndSrcProfileDto.getEndDate());
		//SourceConnectionDetails result = sourceConnectionDetailsRepository.save(sourceConnectionDetails);
		return sourceConnectionDetails;
	}

}
