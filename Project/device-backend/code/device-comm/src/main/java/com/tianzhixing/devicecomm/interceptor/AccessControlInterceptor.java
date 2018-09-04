package com.tianzhixing.devicecomm.interceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tianzhixing.devicecomm.common.Constant;
import com.tianzhixing.devicecomm.common.StatusCode;
import com.tianzhixing.devicecomm.pojo.ResultMessage;
import com.tianzhixing.devicecomm.redis.RedisCache;

@Component
public class AccessControlInterceptor implements HandlerInterceptor {
	private static Logger log = LoggerFactory.getLogger(AccessControlInterceptor.class);
	@Autowired
	private RedisCache redisCache;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		System.out.println("AccessControlInterceptor preHandle--------");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			String path = request.getServletPath();
			String token = request.getParameter(Constant.TOKEN);
			String value = null;
			if ("/token".equals(path)||path.startsWith("/dev/reply") ){
				return true;
			} else if ("/error".equals(path)) {
				return true;
			}else {
				if (token != null && !"".equals(token.trim())) {// 判断token是否存在，且在缓存中能够找到并匹配正确，返回true
					value = redisCache.getString(token);
					if (value != null && !"".equals(value)) {// redis中存的token是否一致
						redisCache.del(token);//token用过一次即失效，清空redis中token的值
						return true;
					}
					response.getWriter().print(new ResultMessage(StatusCode.INVALID_TOKEN,
							"invalid credential, token is invalid or not latest").toString());
					return false;
				} else {// token不存在，或者不正确，返回false
					response.getWriter().print(new ResultMessage(StatusCode.NO_TOKEN, "缺少必须参数token").toString());
					return false;
				}
			}

		} catch (UnsupportedEncodingException e) {
			log.error("拦截器preHandle方法异常（UnsupportedEncodingException）：{}", e.getMessage());
			return false;
		} catch (IOException e) {
			log.error("拦截器preHandle方法异常 （IOException）：{}", e.getMessage());
			return false;
		} catch (Exception e) {
			log.error("拦截器preHandle方法异常 （IOException）：{}", e.getMessage());
			return false;
		} finally {
			// TODO
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
