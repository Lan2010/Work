package core.controller;

import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import core.common.StatusCode;
import core.pojo.ResultMessages;
import core.service.StatisticsService;

@Controller
public class StatisticsController {
	@Resource
	private StatisticsService statisticsService;
	
	@RequestMapping(value = "/api/statistics/device", produces = "application/json; charset=utf-8",method = RequestMethod.GET)
	@ResponseBody
	public String deviceStatistics(HttpServletRequest request, HttpServletResponse response) {
		JSONObject data = new JSONObject();
		try {
			JSONObject all = statisticsService.countAll();
			JSONObject countBind = statisticsService.countBind();
			JSONObject countUnbound = statisticsService.countUnbound();
			data.put("all", all);
			data.put("bind", countBind);
			data.put("unbound", countUnbound);
			return new ResultMessages(StatusCode.OK, "请求成功", data).toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResultMessages(StatusCode.SQL_EXCEPTION, "系统繁忙", data).toString();
		}
	}
}
