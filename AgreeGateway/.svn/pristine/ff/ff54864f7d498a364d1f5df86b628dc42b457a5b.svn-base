package com.nspl.app.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


@Component
public class TenantInterceptor extends HandlerInterceptorAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(TenantInterceptor.class.getName());
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
			logger.info("Calling interceptor pre Handle");
			String domainName = request.getServerName();
			logger.info("domainName "+domainName);
			TenantContext.setCurrentTenant(domainName);
        return true;
    }
	
	@Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
			logger.info("Calling interceptor post Handle "+ TenantContext.getCurrentTenant());
			TenantContext.clear();
	}

}