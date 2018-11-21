package com.nspl.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.nspl.app.security.TenantInterceptor;


@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

  @Autowired 
  TenantInterceptor customTenantInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
	  registry.addInterceptor(customTenantInterceptor);
    }
  }