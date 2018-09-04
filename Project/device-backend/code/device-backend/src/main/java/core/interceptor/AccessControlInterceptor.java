package core.interceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

import core.common.Constant;
import core.common.StatusCode;
import core.pojo.AdminUser;
import core.pojo.ResultMessages;



public class AccessControlInterceptor extends HandlerInterceptorAdapter {
	private static Logger log = LoggerFactory.getLogger(AccessControlInterceptor.class);

	/**
	 * 在request请求之前做拦截处理
	 * 
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			String servletPath =request.getServletPath();
			AdminUser adminUser = null;
			String requestURL = request.getRequestURL().toString();
		//	log.info("request URL："+requestURL+",IP地址:"+Util.getIpAddr(request));
			HttpSession session=request.getSession(false);
			// 放行登录和获取验证码请求
			if(servletPath.startsWith("/api/login") || servletPath.startsWith("/api/validateCode") || servletPath.startsWith("/file")) {
				return true;
			}else if(servletPath.startsWith("/api/")){//返回json
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/plain");
				if(session==null) {
					if(getCookie(request)) {//cookie中存在用户信息，则放行
						return true;
					}else {
						//response.sendRedirect(request.getContextPath()+"/login.jsp");
					    response.getWriter().print(new ResultMessages(StatusCode.NOT_LOGIN, "未登录")); 
						return false;
					}
				}
				adminUser = (AdminUser)session.getAttribute(Constant.SESSION_USER);
				if(adminUser!=null) {//用户已经登录，放行
					return true;
				}else {
			    	//response.sendRedirect(request.getContextPath()+"/login.jsp");//session和cookie中无用户信息，则重定向到登录页
				    response.getWriter().print(new ResultMessages(StatusCode.NOT_LOGIN, "未登录")); 
					return false;
				}
			}else {
				if(session==null) {
					if(getCookie(request)) {//cookie中存在用户信息，则放行
						return true;
					}else {
						response.sendRedirect(request.getContextPath()+"/login.jsp");//重定向到登录页
						return false;
					}
				}else {
					adminUser = (AdminUser)session.getAttribute(Constant.SESSION_USER);
					if(adminUser!=null) {//用户已经登录，放行
						return true;
					}else {
				    	//response.sendRedirect(request.getContextPath()+"/login.jsp");//session和cookie中无用户信息，则重定向到登录页
						response.sendRedirect(request.getContextPath()+"/login.jsp");//重定向到登录页 
						return false;
					}
				}
			}
		
			//log.info("servletPath:{}",servletPath);
			//return isAuthorize(request, response, handler);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return false;
	}
	
	private boolean isAuthorize(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
	private boolean getCookie(HttpServletRequest request) {
		AdminUser adminUser = null;
		Cookie[] cookies = request.getCookies();
		if(cookies!=null) {
			for (Cookie cookie :cookies) {
				if(cookie.getName()!=null && Constant.COOKIE_USER.equals(cookie.getName()) ) {
					adminUser=JSON.parseObject(cookie.getValue(),AdminUser.class);
					if(adminUser!=null) {
						request.getSession(false).setAttribute(Constant.SESSION_USER, adminUser);
						return true;
					}
				}
			}
		}
    	return false;
	}
}
