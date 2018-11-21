package com.nspl.app.service;

import java.util.Collections;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import com.nspl.app.web.rest.MyStompSessionHandler;
import com.nspl.app.web.rest.NotificationsResource;



@Service
public class WebSocketService {


	@Autowired
	Environment env;


	private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

	private final Logger log = LoggerFactory.getLogger(NotificationsResource.class);

	public void webSocketConnectionService(LinkedHashMap LHM,String sendTo) {

		String URL = "ws://"+env.getProperty("spring.datasource.serverName")+":"+env.getProperty("server.port")+"/websocket/tracker";

		Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
		List<Transport> transports = Collections.singletonList(webSocketTransport);
		SockJsClient sockJsClient = new SockJsClient(transports);

		sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		org.springframework.util.concurrent.ListenableFuture<StompSession> session=stompClient.connect(URL, headers, new MyHandler());
		try {
			StompSession liveSession=session.get();

			JSONObject obj = new JSONObject();
			obj.putAll(LHM);
			String msg=obj.toJSONString();
			log.info("msg :"+msg);
			//subscribeGreetings(liveSession);
			liveSession.send("/websocket/"+sendTo, msg.getBytes());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}
	
	/*public void subscribeGreetings(StompSession stompSession) throws ExecutionException, InterruptedException {
		log.info("stompSession "+stompSession.getSessionId());
        stompSession.subscribe("/websocket/request", new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                log.info("Received greeting " + new String((byte[]) o));
            }
        });
    }*/
	
	 private class MyHandler extends StompSessionHandlerAdapter {
	        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
	        	log.info("Now connected");
	        }
	    }

}
