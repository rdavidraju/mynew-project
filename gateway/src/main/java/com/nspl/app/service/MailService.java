package com.nspl.app.service;

import com.nspl.app.domain.TenantDetails;
import com.nspl.app.domain.User;
import com.nspl.app.repository.TenantDetailsRepository;
import com.nspl.app.security.TenantContext;

import io.github.jhipster.config.JHipsterProperties;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;

import java.util.Locale;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;
    
    private final TenantDetailsRepository tenantDetailsRepository;
    
    private final Environment env;
    
    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
            MessageSource messageSource, SpringTemplateEngine templateEngine, TenantDetailsRepository tenantDetailsRepository, Environment env) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.tenantDetailsRepository =tenantDetailsRepository;
        this.env = env;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey, Integer port) {
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        String portStr = "";
        if(port>0)
        {
        	portStr = ":"+port;
        }
        if(templateName.equals("userActivationValidation") || templateName.equals("userActivationConfirmation") || templateName.equals("passwordResetEmail"))
        {
        	TenantDetails tenantDetails = tenantDetailsRepository.findOne(user.getTenantId());
        	log.info("TenantContext.getCurrentPort() "+TenantContext.getCurrentPort());
        	String sslProtocol = "http";
        	if(env.getProperty("server.ssl.enabled")!=null)
        	{
        		if(env.getProperty("server.ssl.enabled").toString().equals("true"))
        		{
        			sslProtocol = "https";
        		}
        	}
        	context.setVariable(BASE_URL, sslProtocol+"://"+tenantDetails.getDomainName()+portStr);
        }
        else
        {
        	context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        }
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);

    }

    @Async
    public void sendActivationConfirmationEmail(User user, Integer port) {
        log.debug("Sending activation confirmation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "userActivationConfirmation", "email.activation.title",port);
    }
    
    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "activationEmail", "email.activation.title",0);
    }

    @Async
    public void sendActivationValidationEmail(User user,Integer port) {
        log.debug("Sending Activation Validation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "userActivationValidation", "email.validation.title",port);
    }
    
    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "creationEmail", "email.activation.title",0);
    }

    @Async
    public void sendPasswordResetMail(User user,Integer port) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "passwordResetEmail", "email.reset.title",port);
    }

	public void sendPasswordConfirmationEmail(User user, Integer port) {
		// TODO Auto-generated method stub
		 log.debug("Sending Password Reset Conirmation email to '{}'", user.getEmail());
	        sendEmailFromTemplate(user, "passwordResetConfirmation", "email.resetconfirmation.title",port);
	}
}
