package com.nspl.app.config;

/*import com.forskolenatet.security.AuthoritiesConstants;*/
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/*import com.nspl.app.security.AuthoritiesConstants;*/

@Configuration
public class WebsocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

  /*   @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
         System.out.println("configureInbound ");
        messages
            .nullDestMatcher().authenticated()
            .simpDestMatchers("/topic/tracker").hasAuthority("ROLE_ADMIN")
            // matches any destination that starts with /topic/
            // (i.e. cannot send messages directly to /topic/)
            // (i.e. cannot subscribe to /topic/messages/* to get messages sent to
            // /topic/messages-user<id>)
           
            .simpDestMatchers("/topic/**").authenticated()
            // message types other than MESSAGE and SUBSCRIBE
          	.simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).denyAll()
            // catch all
            .anyMessage().denyAll();
        System.out.println("messages " + messages);
        System.out.println("messages " + messages.simpDestMatchers("/topic/**").authenticated());
    } */

    /**
     * Disables CSRF for Websockets.
     */
   @Override
    protected boolean sameOriginDisabled() {
        //.csrf().disable()
        return true;
    }
    
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
    }   

}
