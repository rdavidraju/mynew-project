package com.nspl.app.web.rest;


import java.lang.reflect.Type;
import java.util.HashMap;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.nspl.app.web.rest.dto.SampleMessageDTO;




@Configuration
public class MyStompSessionHandler extends StompSessionHandlerAdapter {
	
	@Inject
	WebSocketTestResource webSocketTestResource;
	
	private final Logger logger = LoggerFactory.getLogger(MyStompSessionHandler.class);
	// private Logger logger = LogManager.getLogger(MyStompSessionHandler.class);

	    @Override
	    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
	    	logger.info("after connecting web socket");
	    	logger.info("connectedHeaders :"+connectedHeaders);
	    	
	        logger.info("New session established : " + session.getSessionId());
	       
	        //session.subscribe("/websocket/greetings", this);
	        //logger.info("Subscribed to /websocket/greetings");
	       // connectedHeaders.setDestination("/websocket/hello");
	     
					//try {
						logger.info("before sending");
						session.send("/websocket/hello", getSampleMessage());
					//	logger.info("sending to /websocket/hi");
					/*} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				
			
	
	        logger.info("Message sent to websocket server");
	    }

	   /* @Override
	    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
	        logger.error("Got an exception", exception);
	    }

	    @Override
	    public Type getPayloadType(StompHeaders headers) {
	        return SampleMessageDTO.class;
	    }

	    @Override
	    public void handleFrame(StompHeaders headers, Object payload) {
	    	
	    	SampleMessageDTO msg = (SampleMessageDTO) payload;
	        logger.info("Received : " + msg.getMessage() + " from : " + msg.getUsername());
	    }
*/
	    /**
	     * A sample message instance.
	     * @return instance of <code>Message</code>
	     */
	    private SampleMessageDTO getSampleMessage() {
	    	
	    	
	    	SampleMessageDTO message=new SampleMessageDTO();
	    	message.setMessageBody("hai nicky");
	    	message.setUserName("rahul");
	        return message;
	    }
}
