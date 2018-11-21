package com.nspl.app.web.rest;

import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.nspl.app.config.WebsocketConfiguration;
import com.nspl.app.domain.SchedulerDetails;
import com.nspl.app.web.rest.dto.SampleMessageDTO;



@Controller
public class WebSocketTestResource {
	private final Logger log = LoggerFactory.getLogger(WebsocketConfiguration.class);
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@MessageMapping("/websocket/comments/{tenantId}")
//    @SendTo("/websocket/greetings")
    public SampleMessageDTO greeting( @DestinationVariable("tenantId") String tenantId , SampleMessageDTO message) throws Exception {
		log.debug("Called greeting :"+message);
		this.simpMessagingTemplate.convertAndSend("/websocket/subscribecomments/" + tenantId, message);
        Thread.sleep(100); // simulated delay
        return message;
    }
	
	
	@MessageMapping("/websocket/jobs/{tenantId}")
   //  @SendTo("/websocket/notifications")
    public LinkedHashMap jobSubmission( @DestinationVariable("tenantId") String tenantId , LinkedHashMap schDetails) throws Exception {
		log.debug("Called notifications :"+schDetails);
		this.simpMessagingTemplate.convertAndSend("/websocket/notifications/" + tenantId, schDetails);
        Thread.sleep(100); // simulated delay
        return schDetails;
    }



}
