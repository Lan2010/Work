package com.tianzhixing.devicecomm.filter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import com.alibaba.druid.support.http.WebStatFilter;
/**
 * 配置监控过滤器
 * @author dev-teng
 * @date 2018年6月26日
 */
@WebFilter(filterName="druidWebStatFilter",    
urlPatterns="/*",    
initParams={    
    @WebInitParam(name="exclusions",value="*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*"),// 忽略资源
}) 
public class DruidStatFilter extends WebStatFilter{
	
}
