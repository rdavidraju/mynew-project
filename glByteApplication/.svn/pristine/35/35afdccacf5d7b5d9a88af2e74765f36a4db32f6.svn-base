package com.nspl.app.jbpm.util;

//import io.gatling.core.runner.Selection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.task.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.NotificationBatchRepository;
import com.nspl.app.repository.NotificationsRepository;
import com.nspl.app.repository.ReconciliationResultRepository;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

public class EmailNotification implements WorkItemHandler {
	
	
	@Inject
	TaskService taskService;
	
	
	@Inject
	ReconciliationResultRepository reconcilationResultRepository;
	
	@Inject
	DataViewsRepository dataViewsRepository;
	
	
	@Inject
	NotificationBatchRepository notificationBatchRepository;
	
	
	@Inject
	NotificationsRepository notificationsRepository;
	
	
	@Inject
    private Environment env;
    
	private final Logger log = LoggerFactory.getLogger(EmailNotification.class);
	
	public EmailNotification() {
		// TODO Auto-generated constructor stub
		log.info("in EmailNotification constructer ");
	}

	@Override
	public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		
			System.out.println("in executeWorkItem in EmailNotification");
        	 
			Properties props = new Properties();
	    	props.put("mail.smtp.auth", "true");
	    	props.put("mail.smtp.starttls.enable", "true");
	    	
	    	String enableblock=null;
	    	
	    	String emailType = (String) workItem.getParameter("emailType"); 
	    	System.out.println("emailType: "+emailType);
    		
	    	if(emailType.equals("ApprovalEmail"))
	    	{
	    		log.info("workItem.getParameters(): "+workItem.getParameters());
	    		enableblock="ApprovalEmail";
	    		String from = (String) workItem.getParameter("From");        
	        	String toLogin = (String) workItem.getParameter("To"); 
	        	String to = null;
	        	String msg = (String) workItem.getParameter("Body");
	        	/*String invNumber = (String) workItem.getParameter("invNumber");
	        	String invDate = (String) workItem.getParameter("invDate");   
	        	String invAmount = (String) workItem.getParameter("invAmount");   
	        	String invHeaderId = (String) workItem.getParameter("invHeaderId"); 
	        	String invoicePath = (String) workItem.getParameter("invPath");*/
	        	String batchId=(String) workItem.getParameter("batchId");
	        	String userId=(String) workItem.getParameter("userId").toString();
	        	String tenantId=(String) workItem.getParameter("tenantId").toString();
	        	String appInitiatedDate = (String) workItem.getParameter("appInitiatedDate");
	        	String batchName=(String) workItem.getParameter("batchName");
	        	String applicationURL=(String) workItem.getParameter("applicationUrl");
	        //	String dataViewName=(String) workItem.getParameter("dataViewName");
	        //	Integer count=(Integer) workItem.getParameter("count");
	        	
	        	log.info("batchName  email :"+batchName);
	       // 	log.info("dataViewName  email :"+dataViewName);
	       // 	log.info("count  email :"+count);
	        	//String userId = (String) workItem.getParameter("invUserId");
	        	List appList=(List) workItem.getParameter("list");
	        	log.info("appList: "+appList);
	        	log.info("appList.get(0): "+appList.get(0));
	        	HashMap appMap=(HashMap) appList.get(0);
	        	List appValList=(List) appMap.get("User");
	        	log.info("appValList: "+appValList);
	        	log.info("appValList.get(0): "+appValList.get(0));
	        	Long toUser=Long.parseLong(appValList.get(0).toString());
	        	log.info("toUser: "+toUser);
	        	String content = "";
	        	System.out.println("batchId: "+batchId+" appInitiatedDate: "+appInitiatedDate);
	      
	        	Long ntBctId=Long.parseLong(batchId);
	        	log.info("ntBctId :"+ntBctId);
	        	System.out.println("from: "+from+" toLogin: "+toLogin+" to: "+to+" msg: "+msg);
	        	BufferedReader br = null;
	
	    		try {
	
	    			String sCurrentLine;
	    			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("approvalNotification.html");
	    			br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	    			while ((sCurrentLine = br.readLine()) != null) {
	    				//log.info(sCurrentLine);
	    				content = content + sCurrentLine;
	    			}
	    			//System.out.println("content: "+content);
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		} finally {
	    			try {
	    				if (br != null)br.close();
	    			} catch (IOException ex) {
	    				ex.printStackTrace();
	    			}
	    		}
	    		log.info("userId: "+userId+" tenantId: "+tenantId+" batchId: "+batchId);
	    		content = content.replaceAll("Approve", "<a href=\""+applicationURL+"/api/tokenGenerationForEmailAprovalTask?"+"userId="+userId+"&tenantId="+tenantId+"&batchId="+batchId+"&action=Approve\" style=\"font-weight:bold;padding:12px 24px;border-radius:5px;background-color:#2ecc71;color:white;margin-right:10px;text-decoration: none !important;\">Approve</a>");
	    		content = content.replaceAll("Reject", "<a href=\""+applicationURL+"/api/tokenGenerationForEmailAprovalTask?"+"userId="+userId+"&tenantId="+tenantId+"&batchId="+batchId+"&action=Reject\" style=\"font-weight:bold;padding:12px 24px;border-radius:5px;background-color:#e74c3c;color:white;text-decoration: none !important;\">Reject</a>");
	    		
	    	//	log.info("env.getProperty(serverName) :"+env.getProperty("spring.datasource.serverName"));
	    	//	content = content.replaceAll("Approve", "<a rel=\"nofollow\" target=\"_blank\" href=\"http://"+env.getProperty("spring.datasource.serverName")+":"+env.getProperty("server.port")+"/api/tokenGenerationForEmailAprovalTask?"+"userId="+userId+"&tenantId="+tenantId+"&batchId="+batchId+"&action=Approve\">Approve</a>");
	    	//	content = content.replaceAll("Reject", "<a rel=\"nofollow\" target=\"_blank\" href=\"http://"+env.getProperty("spring.datasource.serverName")+":"+env.getProperty("server.port")+"/api/tokenGenerationForEmailAprovalTask?"+"userId="+userId+"&tenantId="+tenantId+"&batchId="+batchId+"&action=Reject\">Reject</a>");
	    		
	    		
	    	//	content = content.replaceAll("rejectUrl", "<a rel=\"nofollow\" target=\"_blank\" href=\"http://192.168.0.56:8082/api/tokenGeneration/?"+"userId="+userId+"&tenanId="+tenantId+"&batchId="+batchId+"&action=Approve\">Reject</a>");
	    	//	content = content.replace("DataviewNameHere",dvName.getDataViewName());
	        	content = content.replaceAll("BATCHNAMEHERE",batchName);
	        	content = content.replaceAll("DATAVIEWNAMEHERE",workItem.getParameter("dataViewName").toString());
	        	content = content.replaceAll("AMOUNTHERE"," count "+workItem.getParameter("count"));
	        	content = content.replaceAll("userName",workItem.getParameter("userName").toString());
	    	//	content = content.replace("Number",Integer.toString(reconResList.size()));
		    		
	    		//log.info(""+content);
	    		
	    		//content = content.replaceAll("rejectUrl", "\"http://app.aniya.com:8090/api/rejectTaskEmail/"+from+"/"+invHeaderId+"\"");
	    	   		
	        	String subject = (String) workItem.getParameter("Subject");      
	        
	        	log.info("owner in approval Process "+toLogin);
	        	
	        	String[] toeMail = null ;
	        	if(toLogin.contains("<-->"))
	        	{
	        	 toeMail = new String[toLogin.split("<-->").length];
	        	 toeMail = toLogin.split("<-->");
	        	}
	        	else
	        	{
	        		toeMail = new String[1];
	        		toeMail[0]=toLogin;
	        	}
	        	//to = approvalProcessService.getEmailForUser(toLogin);
	        		
	        		if(!toLogin.equals("noEmail"))
	        		{
	        			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		            	mailSender.setHost("smtp.gmail.com");
		            	mailSender.setPort(587);
		            	mailSender.setUsername("nspl.recon@gmail.com");
		            	mailSender.setPassword("Welcome!23");
		            	mailSender.setJavaMailProperties(props);
		
		            	/**/
		            	MimeMessage mimeMessage = mailSender.createMimeMessage();
		            	MimeMessageHelper helper;
						try {
							helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
							String htmlMsg = "<h3>Hello World!</h3>";
			            	mimeMessage.setContent(content, "text/html");
			            	helper.setTo(toeMail);
			            	helper.setSubject("Approval Notification");
			            	helper.setFrom("nspl.recon@gmail.com");
						} catch (MessagingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	
						try {
							log.info("in mailSender.send(mimeMessage) try block");
							mailSender.send(mimeMessage);
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("e in mailSender.send(mimeMessage) catch block : "+e);
						}
		            	/**/
		            	
		            	log.info("e-mail msg: "+msg+ " is sent *****from "+from +" ** to ** " +to);
	        			/**/
		            	
		            	/*Gmail Smtp Server*/
		            	
		            	  /* String hostSend = "smtp.gmail.com";
			           		
		        		   // Get system properties
		        		   Properties properties = System.getProperties();
		        		   // Setup mail server
		        		   properties.put("mail.smtp.starttls.enable", "true");
		        		   properties.put("mail.smtp.host", hostSend);
		        		   properties.put("mail.smtp.user", "nspl.recon@gmail.com");
		        		   properties.put("mail.smtp.password", "Welcome!23");
		        		   properties.put("mail.smtp.port", "587");
		        		   properties.put("mail.smtp.auth", "true");
		        		   properties.put("mail.smtp.ssl.trust", hostSend);
		        		
		        		   // Get the default Session object.
		        		   Session sessionSend = Session.getDefaultInstance(properties);
		        		
		        		   try{
		        		      // Create a default MimeMessage object.
		        		      Message message = new MimeMessage(sessionSend); //?????????????session is used to original code------swetha
		        		
		        		      // Set From: header field of the header.
		        		      message.setFrom(new InternetAddress("nspl.recon@gmail.com"));
		        		
		        		      // Set To: header field of the header.
		        		      message.addRecipient(Message.RecipientType.TO,
		        		                               new InternetAddress(toeMail[0]));
		        		
		        		      // Set Subject: header field
		        		      message.setSubject("Reply from Aniya: "+subject);
		        		
		        		      // Now set the actual message
		        		      message.setContent(content, "text/html; charset=utf-8");
		        		      // Send message
		        		      Transport transport = sessionSend.getTransport("smtp");
		        		      transport.connect(hostSend, "nspl.recon@gmail.com", "Welcome!23");
		        		      

		        				try {
		        					log.info("in transport.sendMessage try block");
		        					transport.sendMessage(message, message.getAllRecipients());
		        					log.info("Sent message successfully....");
		        					log.info(" to: "+to+" batchId: "+batchId);
		        				} catch (Exception e) {
		        					// TODO Auto-generated catch block
		        					e.printStackTrace();
		        					log.info("in transport.sendMessagetransport.sendMessage catch block : "+e);
		        				}
		        		      //transport.sendMessage(message, message.getAllRecipients());
		        		      transport.close();
		        		      
		        		   }catch (MessagingException mex) {
		        		      mex.printStackTrace();
		        		   }*/
	        		}
	        		else
	        		{
	        			log.info("No email is registered for the user ");
	        		}
	    	}
	    	else if(emailType.equals("RejectionEmail"))
	    	{
	    		log.info("workItem.getParameters(): "+workItem.getParameters());
	    		String from = (String) workItem.getParameter("From");        
	        	String toEmail = (String) workItem.getParameter("To"); 
	        	String msg = (String) workItem.getParameter("Body");
	        	String recDate = (String) workItem.getParameter("reconciledDate");   
	        	String batchId = (String) workItem.getParameter("batchId");
	        	log.info("RejectionEmail content: "+from);
	        	log.info(toEmail);
	        	log.info(msg);
	        	log.info(recDate);
	        	log.info(batchId);
	        	if(!toEmail.equals("noSourceEmail"))
        		{
        			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	            	mailSender.setHost("smtp.mail.yahoo.com");
	            	mailSender.setPort(587);
	            	mailSender.setUsername("nspl.recon@gmail.com");
	            	mailSender.setPassword("welcome123");
	            	mailSender.setJavaMailProperties(props);
	
	            	MimeMessage mimeMessage = mailSender.createMimeMessage();
	            	MimeMessageHelper helper;
					try {
						helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		            	helper.setTo("nspl.recon@gmail.com");
		            	helper.setSubject("Invoice Document Rejected");
		            	helper.setFrom("nspl.recon@gmail.com");
		            	mimeMessage.setContent("<h1>This is actual message</h1>", "text/html" );
					} catch (MessagingException e) {
						e.printStackTrace();
					}
	            	
	            	mailSender.send(mimeMessage);
        		}
	    	}
        	manager.completeWorkItem(workItem.getId(), null);

	}
	
	

}

