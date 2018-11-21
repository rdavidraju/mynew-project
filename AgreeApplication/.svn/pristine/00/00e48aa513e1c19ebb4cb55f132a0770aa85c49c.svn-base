package com.nspl.app.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/*
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

    private final Logger log = LoggerFactory.getLogger(WebsocketConfiguration.class);

    public static final String IP_ADDRESS = "IP_ADDRESS";

    private final JHipsterProperties jHipsterProperties;

    public WebsocketConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	log.info("inside websocke config java ");
       // String[] allowedOrigins = Optional.ofNullable(jHipsterProperties.getCors().getAllowedOrigins()).map(origins -> origins.toArray(new String[0])).orElse(new String[0]);
        registry.addEndpoint("/websocket/notificationTracker")
        	.setAllowedOrigins("*")    
        	.setHandshakeHandler(defaultHandshakeHandler())
            .withSockJS()
            .setInterceptors(httpSessionHandshakeInterceptor());
    }

    @Bean
    public HandshakeInterceptor httpSessionHandshakeInterceptor() {
        return new HandshakeInterceptor() {

            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                    attributes.put(IP_ADDRESS, servletRequest.getRemoteAddress());
                }
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

            }
        };
    }

    private DefaultHandshakeHandler defaultHandshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                Principal principal = request.getPrincipal();
                if (principal == null) {
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
                    principal = new AnonymousAuthenticationToken("WebsocketConfiguration", "anonymous", authorities);
                }
                return principal;
            }
        };
    }
}
*/




/*@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfiguration implements WebSocketMessageBrokerConfigurer {
	private final Logger log = LoggerFactory.getLogger(WebsocketConfiguration.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	log.debug("Configuring Web Socket Server 1");
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	log.debug("Configuring Web Socket Server");
        registry.addEndpoint("/glbyte-websocket").withSockJS();
    }

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addReturnValueHandlers(
			List<HandlerMethodReturnValueHandler> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean configureMessageConverters(List<MessageConverter> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration arg0) {
		// TODO Auto-generated method stub
		
	}

}*/





import java.security.Principal;
import java.util.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.*;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.nspl.app.security.AuthoritiesConstants;

import io.github.jhipster.config.JHipsterProperties;

@Configuration
@EnableWebSocketMessageBroker
@Controller
public class WebsocketConfiguration implements WebSocketMessageBrokerConfigurer {
	
	private final Logger log = LoggerFactory.getLogger(WebsocketConfiguration.class);

    public static final String IP_ADDRESS = "IP_ADDRESS";

    private final JHipsterProperties jHipsterProperties;

    public WebsocketConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	log.debug("Configuring Web Socket Server 1");
        config.enableSimpleBroker("/websocket");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	log.debug("Configuring Web Socket Server 1");
    	String[] allowedOrigins = {"http://localhost:8080","http://localhost1:8080","https://localhost:8080","https://tenant2.glbyte.com","https://beta2t2.glbyte.com","https://beta2t3.glbyte.com"};
    	
    	
       // String[] allowedOrigins = Optional.ofNullable(jHipsterProperties.getCors().getAllowedOrigins()).map(origins -> origins.toArray(new String[0])).orElse(new String[0]);
        registry.addEndpoint("/websocket/tracker")
            .setHandshakeHandler(defaultHandshakeHandler())
//            .setAllowedOrigins("http://localhost:8080")
            .setAllowedOrigins(allowedOrigins)
            .withSockJS()
            .setInterceptors(httpSessionHandshakeInterceptor());
    }

    @Bean
    public HandshakeInterceptor httpSessionHandshakeInterceptor() {
        return new HandshakeInterceptor() {

            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                    attributes.put(IP_ADDRESS, servletRequest.getRemoteAddress());
                }
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

            }
        };
    }

    private DefaultHandshakeHandler defaultHandshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                Principal principal = request.getPrincipal();
                if (principal == null) {
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
                    principal = new AnonymousAuthenticationToken("WebsocketConfiguration", "anonymous", authorities);
                }
                return principal;
            }
        };
    }

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addReturnValueHandlers(
			List<HandlerMethodReturnValueHandler> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean configureMessageConverters(List<MessageConverter> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration arg0) {
		// TODO Auto-generated method stub
		arg0.setMessageSizeLimit(1024000 * 1024);
		arg0.setSendBufferSizeLimit(1024000 * 1024);
		
	}
}