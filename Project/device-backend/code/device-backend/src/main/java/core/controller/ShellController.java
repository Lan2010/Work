package core.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.alibaba.fastjson.JSONObject;

import core.common.Constant;
import core.common.StatusCode;
import core.mapper.DeviceMapper;
import core.mapper.IpkMapper;
import core.pojo.AdminUser;
import core.pojo.Condition;
import core.pojo.DevTaskDetail;
import core.pojo.Page;
import core.pojo.ResultMessages;
import core.pojo.Shell;
import core.pojo.ShellTask;
import core.service.DeviceService;
import core.service.IUserService;
import core.service.IpkService;
import core.service.IpkTaskService;
import core.service.ShellService;
import core.util.FileUtil;

/**
 * 
 * @Description:脚本
 * @author: dev-lan
 * @date: 2018年7月25日
 */
@Controller
public class ShellController {
	private static Logger log = LoggerFactory.getLogger(ShellController.class);
	@Resource
	private IpkTaskService ipkTaskService;
	@Resource
	private IpkService ipkService;
	@Resource
	private ShellService shellService;
	@Resource
	private DeviceService deviceService;

	@Resource
	public DeviceMapper deviceMapper;
	@Resource
	public IpkMapper ipkMapper;
	@Resource
	private IUserService iUserService;

	@RequestMapping(value = "/api/shell/upload", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String uploadIpk(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") CommonsMultipartFile file, @ModelAttribute Shell shell) {
		System.out.println("------uploadShell----");
		HttpSession session = request.getSession(false);
		try {
			// 脚本名称不一样，一样返回报错
			Integer count = shellService.checkExit(shell.getShellName());
			if (count >= 1) {
				return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "该脚本名已存在，请重输!").toString();
			}
			AdminUser user = (AdminUser) session.getAttribute(Constant.SESSION_USER);
			String basePath = request.getServletContext().getRealPath("/"); // linux路径
			// 获取文件需要上传到的路径
			String ipkPath = "file/shell/";
			String path = basePath + ipkPath;
			shell.setShell_md5(FileUtil.getMd5ByFile(file));
			// 1.文件上传
			String fileName = ipkService.uploadIpk(file, path);
			shell.setUser_id(user.getUser_id());
			String dataPath = request.getScheme() + "://" + request.getServerName() + "/" + ipkPath + fileName;
			shell.setShellPath(dataPath);
			shell.setAddTime(new Date());
			shell.setStatus(1);
			System.out.println(shell.toString());
			// 2.信息储存数据库
			Integer result = shellService.saveShell(shell);
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

	@RequestMapping(value = "/api/shell/list", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String listShell(Shell shell, Page page) {
		try {
			List<Shell> shelllist = shellService.listShell(shell, page);
			Integer count = shellService.selectShellCount(shell);
			return new ResultMessages(StatusCode.OK, "请求成功", count, shelllist).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/shell/delete", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteIpk(Integer shellId) {
		try {
			Shell shell = shellService.selectShellById(shellId);
			// 1.删除数据库信息
			Integer result = shellService.deleteShell(shellId);
			if (result <= 0) {
				return new ResultMessages(StatusCode.SQL_EXCEPTION, "删除失败，请稍候再试！").toString();
			}
			// 2.删除服务器硬盘中的文件
			String Path = shell.getShellPath();
			File file = new File(Path);
			if (file.exists()) {
				file.delete();
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

	// 1.新建任务 - 输入多个设备编号
	@RequestMapping(value = "/api/shell/addTaskByDevids", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String addTaskByDevids(HttpServletRequest request, HttpServletResponse response, ShellTask task,
			String devNums) {
		if (devNums == null || devNums.length() <= 0) {
			return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "设备不能为空，请输入设备！").toString();
		}
		HttpSession session = request.getSession(false);
		try {
			AdminUser user = (AdminUser) session.getAttribute(Constant.SESSION_USER);
			task.setUser_id(user.getUser_id());
			task.setAddTime(new Date());
			task.setStatus(-1);

			List<String> devnums = new ArrayList<String>();
			List<String> error_devnums = new ArrayList<String>();

			for (String devnum : devNums.split(",")) {
				Integer count = deviceMapper.checkDevExit(devnum);
				if (count >= 1) {
					devnums.add(devnum);
				} else {
					// 该设备不存在
					error_devnums.add(devnum);
				}
			}
			if (!error_devnums.isEmpty()) {
				JSONObject json = new JSONObject();
				json.put("error_devnums", error_devnums);
				return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "设备输入有误，请检查输入内容！", json).toString();
			}

			task.setDevTotal(devnums.size());
			// 1.任务信息保存数据库
			Integer result = shellService.addTask(task);
			if (result <= 0) {
				return new ResultMessages(StatusCode.SQL_EXCEPTION, "新增失败，请稍候再试！").toString();
			}
			// 2.发送请求到设备
			String result02 = shellService.sendDevTask(task, devnums);

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
	@RequestMapping(value = "/api/shell/addTaskBySearch", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String addIpkTaskBySearch(HttpServletRequest request, HttpServletResponse response, ShellTask task,
			Condition condition) {
		System.out.println(task.toString());
		System.out.println(condition.toString());

		HttpSession session = request.getSession(false);
		try {
			AdminUser user = (AdminUser) session.getAttribute(Constant.SESSION_USER);
			task.setUser_id(user.getUser_id());
			task.setAddTime(new Date());
			task.setStatus(-1);

			// 设备列表反选时，devIds不能为空
			if (condition.getSelectType() == 0
					&& (condition.getDevIds() == null || condition.getDevIds().length() <= 0)) {
				return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "未选择设备，请选择设备！").toString();
			}
			List<Integer> devIds = new ArrayList<Integer>();
			List<Integer> devlist = deviceService.allDevId(condition);
			// 1 全选，devIds为removed;0 反选，devIds为added
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
			task.setDevTotal(devIds.size());
			Integer result = shellService.addTask(task);
			if (result <= 0) {
				return new ResultMessages(StatusCode.SQL_EXCEPTION, "新增失败，请稍候再试！").toString();
			}
			// 2.发送请求到设备
			String result02 = shellService.sendDevTask(task, devIds);

			return new ResultMessages(StatusCode.OK, "请求成功").toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/shell/tasklist", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String listTask(ShellTask task, Page page) {
		try {
			Integer result = shellService.updateCompleteDev();
			List<ShellTask> tasklist = shellService.listTask(task, page);
			Integer count = shellService.selectTaskCount(task);
			return new ResultMessages(StatusCode.OK, "请求成功", count, tasklist).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/shell/shellNamelist", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String shellNamelist() {
		try {
			List<Shell> shellNamelist = shellService.listShellName();
			return new ResultMessages(StatusCode.OK, "请求成功", shellNamelist).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/shell/taskDetailById", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String queryTaskDetailById(Integer taskId) {
		try {
			Integer result = shellService.updateCompleteDev();
			ShellTask task = shellService.queryTaskDetailById(taskId);
			return new ResultMessages(StatusCode.OK, "请求成功", task).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/shell/devDetailByTask", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String devDetailByTask(Integer taskId, Page page) {
		try {
			List<DevTaskDetail> devTaskDetail = shellService.listDevTaskDetail(taskId, page);
			Integer count = shellService.selectDevTaskDetailCount(taskId);
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
