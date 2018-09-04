package core.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import core.common.Constant;
import core.pojo.AdminUser;

/**
 * 页面入口控制类
 * @author dev-jin
 * @date 2018年7月5日
 */
@Controller
public class MainController {
	
	/**
	 * 首页逻辑处理，如果登录过，直接显示后台
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/")
	public String main(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session=request.getSession(false);
		if(session!=null) {
			AdminUser userInfo = (AdminUser)session.getAttribute(Constant.SESSION_USER);
			if(userInfo!=null) {
				return "index";
			}
		}
		try {
			System.out.println(request.getContextPath());
			response.sendRedirect(request.getContextPath()+"/login.jsp");//重定向到登录页 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
}
