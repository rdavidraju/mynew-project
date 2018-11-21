package com.nspl.app.web.rest;

import com.nspl.app.domain.TenantDetails;
import com.nspl.app.repository.TenantDetailsRepository;
import com.nspl.app.repository.UserRepository;
import com.nspl.app.security.TenantContext;
import com.nspl.app.security.jwt.JWTConfigurer;
import com.nspl.app.security.jwt.TokenProvider;
import com.nspl.app.web.rest.vm.LoginVM;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.Collections;
import java.util.List;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final Logger log = LoggerFactory.getLogger(UserJWTController.class);

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;
    
    private final UserRepository userRepository;
  
    private final TenantDetailsRepository tenantDetailsRepository;
    
    public UserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager, UserRepository userRepository, TenantDetailsRepository tenantDetailsRepository) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tenantDetailsRepository = tenantDetailsRepository;
    }

    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity authorize(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response) {

    	  String domainName = TenantContext.getCurrentTenant();
    	  List<TenantDetails> tenantDetails = tenantDetailsRepository.findByDomainName(domainName);
    	  
    	  if(tenantDetails !=null && tenantDetails.size()==1)
    	  {
    		  log.info("tenantDetails "+tenantDetails.get(0).toString());
    	  }
    	  else if(tenantDetails !=null && tenantDetails.size()>1)
    	  {
    		  log.info("One or More tenatns have same domain name");
    		  return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",
    	                "Domain "+domainName+" doesn't exist"), HttpStatus.NOT_FOUND);
    	  }
    	  else
    	  {
    		  log.info("tenantDetails not found");
    		  return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",
    	                "Domain "+domainName+" doesn't exist"), HttpStatus.NOT_FOUND);
    	  }
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        log.info("In JWT authentication");
        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
            Long tenantId=userRepository.findOneByEmail(loginVM.getUsername()).get().getTenantId();
            if(tenantId != tenantDetails.get(0).getId()){
            	return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",
                        "Wrong credentials"), HttpStatus.UNAUTHORIZED);
            }
            Long userId=userRepository.findOneByEmail(loginVM.getUsername()).get().getId();
            String jwt = tokenProvider.createToken(authentication, rememberMe,tenantId,userId);
            response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return ResponseEntity.ok(new JWTToken(jwt));
        } catch (AuthenticationException ae) {
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",
                ae.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
