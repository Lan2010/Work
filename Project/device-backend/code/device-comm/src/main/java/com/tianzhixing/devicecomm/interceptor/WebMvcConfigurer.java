package com.tianzhixing.devicecomm.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootConfiguration
public class WebMvcConfigurer extends WebMvcConfigurationSupport{
	@Autowired
	private AccessControlInterceptor accessControlInterceptor;
	
	 @Override  
	    public void addInterceptors(InterceptorRegistry registry) {  
	        //注册自定义拦截器，添加拦截路径和排除拦截路径  
	        registry.addInterceptor(accessControlInterceptor).addPathPatterns("/**");  
	    } 
}
