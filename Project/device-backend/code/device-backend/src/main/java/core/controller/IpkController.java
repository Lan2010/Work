package core.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import core.common.Constant;
import core.common.StatusCode;
import core.mapper.DeviceMapper;
import core.mapper.IpkMapper;
import core.pojo.AdminUser;
import core.pojo.Condition;
import core.pojo.DevTaskDetail;
import core.pojo.Device;
import core.pojo.Ipk;
import core.pojo.IpkName;
import core.pojo.IpkTask;
import core.pojo.Page;
import core.pojo.ResultMessages;
import core.service.IUserService;
import core.service.IpkService;
import core.service.IpkTaskService;
import core.util.FileUtil;

/**
 * 
 * @Description:插件
 * @author: dev-lan
 * @date: 2018年7月18日
 */
@Controller
public class IpkController {
	private static Logger log = LoggerFactory.getLogger(IpkController.class);
	@Resource
	private IpkTaskService ipkTaskService;
	@Resource
	private IpkService ipkService;
	@Resource
	public DeviceMapper deviceMapper;
	@Resource
	public IpkMapper ipkMapper;
	@Resource
	private IUserService iUserService;

	// 所有插件名称列表
	@RequestMapping(value = "/api/ipk/ipkNamelist", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String listIpkName() {
		try {
			List<IpkName> ipkNamelist = ipkService.listIpkName();
			return new ResultMessages(StatusCode.OK, "请求成功", ipkNamelist).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/ipk/upload", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String uploadIpk(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") CommonsMultipartFile file, @ModelAttribute Ipk ipk) {
		System.out.println("------uploadIpk----");
		HttpSession session = request.getSession(false);
		try {
			Integer count = ipkService.checkExit(ipk);
			if (count >= 1) {
				return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "该插件型号已存在该版本，请重输版本号!").toString();
			}
			AdminUser user = (AdminUser) session.getAttribute(Constant.SESSION_USER);
			String basePath = request.getServletContext().getRealPath("/"); // linux路径
			// 获取文件需要上传到的路径
			String ipkPath = "file/ipk/";
			String path = basePath + ipkPath;

			ipk.setIpk_md5(FileUtil.getMd5ByFile(file));
			// 1.文件上传
			String fileName = ipkService.uploadIpk(file, path);
			ipk.setUser_id(user.getUser_id());
			String dataPath = request.getScheme() + "://" + request.getServerName() + "/" + ipkPath + fileName;
			ipk.setIpkPath(dataPath);
			ipk.setAddTime(new Date());
			ipk.setStatus(1);
			System.out.println(ipk.toString());
			Integer result = ipkService.saveIpk(ipk);
			if (result <= 0) {
				return new ResultMessages(StatusCode.SQL_EXCEPTION, "系统繁忙，请稍候再试！").toString();
			}
			return new ResultMessages(StatusCode.OK, "请求成功").toString();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessages(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessages(StatusCode.IO_EXCEPTION, "系统繁忙~").toString();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙，请稍后再试").toString();

		}
	}

	@RequestMapping(value = "/api/ipk/list", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String listIpk(Ipk ipk, Page page) {
		try {
			List<Ipk> ipklist = ipkService.listIpk(ipk, page);
			Integer count = ipkService.selectIpkCount(ipk);
			return new ResultMessages(StatusCode.OK, "请求成功", count, ipklist).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/ipk/delete", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteIpk(Integer ipkId) {
		try {
			Ipk ipk = ipkService.selectIpkById(ipkId);
			// 1.删除数据库信息
			Integer result = ipkService.deleteIpk(ipkId);
			if (result <= 0) {
				return new ResultMessages(StatusCode.SQL_EXCEPTION, "删除失败，请稍候再试！").toString();
			}
			// 2.删除服务器硬盘中的文件
			String ipkPath = ipk.getIpkPath();
			if (ipkPath == null || ipkPath.length() <= 0) {
				return new ResultMessages(StatusCode.OK, "请求成功").toString();
			}
			File ipkfile = new File(ipkPath);
			if (ipkfile.exists()) {
				ipkfile.delete();
			}
			return new ResultMessages(StatusCode.OK, "请求成功").toString();

		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	// 某个插件的所有版本
	@RequestMapping(value = "/api/ipk/queryVersion", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String queryVersion(HttpServletRequest request, HttpServletResponse response, Ipk ipk) {
		try {

			List<String> ipkVersionlist = ipkService.queryVersion(ipk.getIpkNameId());
			JSONArray ja = new JSONArray();
			for (String version : ipkVersionlist) {
				JSONObject json = new JSONObject();
				json.put("version", version);
				ja.add(json);
			}
			return new ResultMessages(StatusCode.OK, "请求成功", ja).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	// 某个插件的所有设备
	@RequestMapping(value = "/api/ipk/queryDevList", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String queryDevList(Ipk ipk, Page page, Device device, Integer operateType) {
		try {
			List<Device> devlist = new ArrayList<Device>();
			Integer count;
			if (operateType == 0) {
				// 安装插件
				devlist = ipkService.listDev(ipk, page, device);
				count = ipkService.selectDevCount(ipk, device);
			} else {
				// 卸载插件，查找以前安装过该插件且成功，并且未卸载过
				devlist = ipkService.listDevByRemove(ipk, page, device);
				count = ipkService.selectDevCountByRemove(ipk, device);
				if(count<=0) {
					return new ResultMessages(StatusCode.DEVICE_IS_NULL, "未有安装过该插件的设备！").toString();
				}
			}
			return new ResultMessages(StatusCode.OK, "请求成功", count, devlist).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	// 1.新建任务 - 输入多个设备编号
	@RequestMapping(value = "/api/ipk/addIpkTaskByDevids", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String addIpkTaskByDevids(HttpServletRequest request, HttpServletResponse response, IpkTask ipkTask,
			String devNums) {
		if (devNums == null || devNums.length() <= 0) {
			return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "设备不能为空，请输入设备！").toString();
		}
		HttpSession session = request.getSession(false);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ipkNameId", ipkTask.getIpkNameId());
			map.put("version", ipkTask.getVersion());

			AdminUser user = (AdminUser) session.getAttribute(Constant.SESSION_USER);
			ipkTask.setUser_id(user.getUser_id());
			ipkTask.setAddTime(new Date());

			List<String> devnums = new ArrayList<String>();
			List<String> NoPath_devnums = new ArrayList<String>();
			List<String> error_devnums = new ArrayList<String>();

			for (String devnum : devNums.split(",")) {
				Integer count = deviceMapper.checkDevExit(devnum);
				if (count >= 1) {
					map.put("devNum", devnum);
					if (ipkTask.getOperateType() == 0) {
						Ipk ipk = ipkMapper.selectIpkByDevNum(map);
						if (ipk == null) {
							NoPath_devnums.add((String) devnum);
						} else {
							devnums.add(devnum);
						}
					} else {
						// 卸载插件不考虑数据库真实性
						devnums.add(devnum);
					}
				} else {
					// 该设备不存在
					error_devnums.add(devnum);
				}
			}

			if (!error_devnums.isEmpty() || !NoPath_devnums.isEmpty()) {
				JSONObject json = new JSONObject();
				json.put("error_devnums", error_devnums);
				json.put("NoPath_devnums", NoPath_devnums);
				return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "设备输入有误，请检查输入内容！", json).toString();
			}
			ipkTask.setDevTotal(devnums.size());
			// 1.任务信息保存数据库
			Integer result = ipkService.addIpkTask(ipkTask);
			if (result <= 0) {
				return new ResultMessages(StatusCode.SQL_EXCEPTION, "新增失败，请稍候再试！").toString();
			}
			// 2.发送请求到设备
			String result02 = ipkService.sendDevTask(ipkTask, devnums);

			return new ResultMessages(StatusCode.OK, "请求成功").toString();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "设备集合输入有误，请检查输入字符是否正确！").toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	// 2.新建任务 - 条件搜索设备选择设备
	@RequestMapping(value = "/api/ipk/addIpkTaskBySearch", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String addIpkTaskBySearch(HttpServletRequest request, HttpServletResponse response, IpkTask ipkTask,
			Condition condition) {
		HttpSession session = request.getSession(false);
		try {
			AdminUser user = (AdminUser) session.getAttribute(Constant.SESSION_USER);
			ipkTask.setUser_id(user.getUser_id());
			ipkTask.setAddTime(new Date());

			// 设备列表反选时，devIds不能为空
			if (condition.getSelectType() == 0
					&& (condition.getDevIds() == null || condition.getDevIds().length() <= 0)) {
				return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "未选择设备，请选择设备！").toString();
			}
			List<Integer> devIds = new ArrayList<Integer>();
			List<Integer> devlist = new ArrayList<Integer>();

			if (ipkTask.getOperateType() == 0) {
				devlist = ipkService.AllDev(ipkTask, condition);
			} else {
				devlist = ipkService.AllDevByRemove(ipkTask, condition);
			}
			// 全选，devIds为removed;反选，devIds为added
			if (condition.getDevIds() == null || condition.getDevIds().length() <= 0) {
				devIds = devlist;
			} else {
				for (String s : condition.getDevIds().split(",")) {
					devIds.add(Integer.valueOf(s));
				}
				// 全选,升级设备= 刷选设备-devIds
				if (condition.getSelectType() == 1) {
					devlist.removeAll(devIds);
					devIds = devlist;
				}
			}
			ipkTask.setDevTotal(devIds.size());
			Integer result = ipkService.addIpkTask(ipkTask);
			if (result <= 0) {
				return new ResultMessages(StatusCode.SQL_EXCEPTION, "新增失败，请稍候再试！").toString();
			}
			// 2.发送请求到设备
			String result02 = ipkService.sendDevTask(ipkTask, devIds);

			return new ResultMessages(StatusCode.OK, "请求成功").toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/ipk/ipkTasklist", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String listIpkTask(HttpServletRequest request, HttpServletResponse response, IpkTask ipkTask, Page page) {
		System.out.println("---------listIpkTask---------");
		try {
			System.out.println(ipkTask.toString());
			Integer result = ipkTaskService.updateCompleteDev();
			List<IpkTask> ipkTasklist = ipkTaskService.listIpkTask(ipkTask, page);
			Integer count = ipkTaskService.selectIpkTaskCount(ipkTask);
			return new ResultMessages(StatusCode.OK, "请求成功", count, ipkTasklist).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/ipk/ipkTaskDetailById", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String queryIpkTaskById(HttpServletRequest request, HttpServletResponse response, Integer ipkTaskId) {
		System.out.println("---------ipkTaskDetail---------");
		try {
			Integer result = ipkTaskService.updateCompleteDev();
			IpkTask ipkTask = ipkTaskService.queryIpkTaskById(ipkTaskId);
			return new ResultMessages(StatusCode.OK, "请求成功", ipkTask).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/ipk/devDetailByTask", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String devDetailByTask(Integer ipkTaskId, Page page) {
		try {
			List<DevTaskDetail> devTaskDetail = ipkTaskService.listDevTaskDetail(ipkTaskId, page);
			Integer count = ipkTaskService.selectDevTaskDetailCount(ipkTaskId);
			return new ResultMessages(StatusCode.OK, "请求成功", count, devTaskDetail).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

}
