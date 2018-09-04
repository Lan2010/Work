package com.tianzhixing.devicecomm.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianzhixing.devicecomm.common.Constant;
import com.tianzhixing.devicecomm.common.nats.NatsComponent;
import com.tianzhixing.devicecomm.dao.DevOnlineLogDao;
import com.tianzhixing.devicecomm.mapper.DeviceMapper;
import com.tianzhixing.devicecomm.pojo.Device;
import com.tianzhixing.devicecomm.pojo.Task;
import com.tianzhixing.devicecomm.redis.RedisCache;
import com.tianzhixing.devicecomm.service.DeviceService;
import com.tianzhixing.devicecomm.util.CommonUtil;

@Service
public class DeviceServiceImpl implements DeviceService {

	private static Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private DevOnlineLogDao devOnlineLogDao;
	@Autowired
	private RedisCache redisCache;
	@Autowired
	private NatsComponent natsComponent;

	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
	private static ScheduledFuture<?> future = null;

	@Override
	public void dosign(Message<?> message) {
		Map<String, Object> map = new HashMap<String, Object>();
		String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
		long timestamp = (long) message.getHeaders().get("timestamp");
		Date time = new Date(timestamp); // 时间戳转换成时间
		String[] d = topic.split("/");
		String devNum = d[2];
		String data = (String) message.getPayload();
		System.out.println("data:" + data);
		JSONObject resultJSON = JSONObject.parseObject(data);
		int status = (int) resultJSON.get("status");
		map.put("devNum", devNum);
		map.put("time", time);
		map.put("status", status);

		// 设备上下线状态
		try {
			int result0 = deviceMapper.online(map);
			Device dev = deviceMapper.getDevByNum(devNum);
			if (dev == null || dev.getIsOnlined() != 1) {
				int result1 = deviceMapper.updateDev(map);
			}
			// 设备上下线记录
			devOnlineLogDao.insertLog(map);
			// 推送上下线消息到nats消息队列，为运营平台提供
			publish4oms(dev, time, status);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * 推送上下线消息到nats消息队列，为运营平台提供
	 * 
	 * @param dev
	 * @param time
	 * @param status
	 */
	private void publish4oms(Device dev, Date time, Integer status) {
		if (dev != null) {
			JSONObject json = new JSONObject();
			json.put("id", CommonUtil.randomID(8));
			json.put("createTime", System.currentTimeMillis());
			json.put("platformFrom", Constant.PLATFORM_NAME);
			json.put("deviceId", dev.getNumber());
			json.put("deviceType", "TZX");
			json.put("deviceMac", dev.getMac());
			json.put("deviceModel", dev.getModel());
			json.put("operationTime", time.getTime());
			json.put("operationType", status);
			json.put("lng", "");
			json.put("lat", "");
			json.put("bindStatus", "");// 绑定状态
			natsComponent.publish4oms("oms.subject.device.on-off", json.toJSONString());
		}
	}

	@Override
	public void getSetReply(Message<?> message) {
		System.out.println("--------enter getSetReply--------");
		String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
		long timestamp = (long) message.getHeaders().get("timestamp");
		String[] d = topic.split("/");
		String devNum = d[2];
		String taskId = d[5];
		JSONObject resultJSON = JSONObject.parseObject((String) message.getPayload());
		int status = (int) resultJSON.get("ret"); // 0代表成功，其他代表失败
		Task task = new Task(taskId, devNum, status);
		task.setReplayTime(new Date(timestamp));
		System.out.println("task:" + task.toString());
		try {
			int result = deviceMapper.replayTask(task);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@Override
	public void getGetReply(Message<?> message) {
		String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
		long timestamp = (long) message.getHeaders().get("timestamp");
		String[] d = topic.split("/");
		String devNum = d[2];
		String taskId = d[5];
		String data = (String) message.getPayload();
		System.out.println("data:" + data);
		JSONObject resultJSON = JSONObject.parseObject(data);
		int result = (int) resultJSON.get("ret");
		JSONArray conf = resultJSON.getJSONArray("conf");
		Task task = new Task(taskId, devNum, result);
		task.setReplayTime(new Date(timestamp));
		System.out.println("task:" + task.toString());
		try {
			int results = deviceMapper.replayTask(task);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		try {
			switch (result) {
			case 0:
				// 返回成功
				redisCache.set(taskId, "0", null);
				redisCache.set(devNum + "_config", conf.toJSONString(), null);
				break;
			default:
				// 返回失败
				redisCache.set(taskId, Integer.toString(result), null);
				break;
			}
		} catch (Exception e) {
			System.out.println("空");
		}
	}

	@Override
	public void getInstallIpkReply(Message<?> message) {
		System.out.println("--------enter getInstallIpkReply--------");
		String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
		long timestamp = (long) message.getHeaders().get("timestamp");
		String[] d = topic.split("/");
		String devNum = d[2];
		String taskId = d[5];
		JSONObject resultJSON = JSONObject.parseObject((String) message.getPayload());
		int status = (int) resultJSON.get("ret"); // 0代表成功，其他代表失败
		Task task = new Task(taskId, devNum, status);
		task.setReplayTime(new Date(timestamp));
		System.out.println("task:" + task.toString());
		try {
			int result = deviceMapper.replayTask(task);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@Override
	public void getRemoveIpkReply(Message<?> message) {
		System.out.println("--------enter getRemoveIpkReply--------");
		String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
		long timestamp = (long) message.getHeaders().get("timestamp");
		String[] d = topic.split("/");
		String devNum = d[2];
		String taskId = d[5];
		JSONObject resultJSON = JSONObject.parseObject((String) message.getPayload());
		int status = (int) resultJSON.get("ret"); // 0代表成功，其他代表失败
		Task task = new Task(taskId, devNum, status);
		task.setReplayTime(new Date(timestamp));
		System.out.println("task:" + task.toString());
		try {
			int result = deviceMapper.replayTask(task);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@Override
	public void getInfoIpkReply(Message<?> message) {
		System.out.println("--------enter getInfoIpkReply--------");
		String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
		long timestamp = (long) message.getHeaders().get("timestamp");
		String[] d = topic.split("/");
		String devNum = d[2];
		String taskId = d[5];
		JSONObject resultJSON = JSONObject.parseObject((String) message.getPayload());
		// 获取插件信息
		String info = (String) resultJSON.get("ret");

		// 保存回复任务状态
		int status = (int) resultJSON.get("ret"); // 0代表成功，其他代表失败
		Task task = new Task(taskId, devNum, status);
		task.setReplayTime(new Date(timestamp));
		System.out.println("task:" + task.toString());
		try {
			int result = deviceMapper.replayTask(task);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@Override
	public void getRebootReply(Message<?> message) {
		String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
		long timestamp = (long) message.getHeaders().get("timestamp");
		String[] d = topic.split("/");
		String devNum = d[2];
		String taskId = d[5];
		JSONObject resultJSON = JSONObject.parseObject((String) message.getPayload());
		int status = (int) resultJSON.get("ret"); // 0代表成功，其他代表失败
		Task task = new Task(taskId, devNum, status);
		task.setReplayTime(new Date(timestamp));
		System.out.println("task:" + task.toString());

		try {
			int result = deviceMapper.replayTask(task);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@Override
	public void getShellReply(Message<?> message) {
		System.out.println("--------enter getShellReply--------");
		String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
		long timestamp = (long) message.getHeaders().get("timestamp");
		String[] d = topic.split("/");
		String devNum = d[2];
		String taskId = d[5];
		JSONObject resultJSON = JSONObject.parseObject((String) message.getPayload());
		int status = (int) resultJSON.get("ret"); // 0代表成功，其他代表失败
		Task task = new Task(taskId, devNum, status);
		task.setReplayTime(new Date(timestamp));
		System.out.println("task:" + task.toString());
		try {
			int result = deviceMapper.replayTask(task);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@Override
	public void getfirmwareReply(Message<?> message) {
		System.out.println("--------enter getfirmwareReply--------");
		String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
		long timestamp = (long) message.getHeaders().get("timestamp");
		String[] d = topic.split("/");
		String devNum = d[2];
		String taskId = d[5];
		JSONObject resultJSON = JSONObject.parseObject((String) message.getPayload());
		int status = (int) resultJSON.get("ret"); // 0代表成功，其他代表失败
		Task task = new Task(taskId, devNum, status);
		task.setReplayTime(new Date(timestamp));
		System.out.println("task:" + task.toString());
		try {
			int result = deviceMapper.replayTask(task);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@Override
	public void getSetPasswdReply(Message<?> message) {
		System.out.println("--------enter getSetPasswdReply--------");
		String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
		long timestamp = (long) message.getHeaders().get("timestamp");
		String[] d = topic.split("/");
		String devNum = d[2];
		String taskId = d[5];
		JSONObject resultJSON = JSONObject.parseObject((String) message.getPayload());
		int status = (int) resultJSON.get("ret"); // 0代表成功，其他代表失败
		Task task = new Task(taskId, devNum, status);
		task.setReplayTime(new Date(timestamp));
		System.out.println("task:" + task.toString());
		try {
			int result = deviceMapper.replayTask(task);
			redisCache.set(taskId, Integer.toString(status), null);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (Exception e) {
			System.out.println("空");
		}

	}

	@Override
	public String getConf(String taskId, String devNum) {
		final Map<String, Future> futures = new HashMap<>();
		// 每隔0.1秒进行轮询
		future = service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				System.out.println("-------run------");
				String task = redisCache.getString(taskId);
				if (task != null && task.length() != 0) {
					Future future = futures.get(devNum);
					if (future != null) {
						futures.remove(devNum);
						future.cancel(true);
						// service.shutdown();
					}
				}
			}
		}, 0, 100, TimeUnit.MILLISECONDS);
		try {
			futures.put(devNum, future);
			// 设置超时时间
			future.get(4, TimeUnit.SECONDS);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
			log.error("InterruptedException:" + e2.getMessage());
		} catch (CancellationException e2) {
			log.info("CancellationException:" + e2);
		} catch (ExecutionException e2) {
			e2.printStackTrace();
			log.error("ExecutionException:" + e2.getMessage());
		} catch (TimeoutException e2) {
			log.info("TimeoutException:" + e2.getMessage());
		} finally {
			future.cancel(true);
			// service.shutdownNow();
		}
		if (!redisCache.isExitKey(devNum + "_config")) {
			return null;
		}
		return redisCache.getString(devNum + "_config");
	}

	@Override
	public Integer getSetPasswdReply(String taskId, String devNum) {
		final Map<String, Future> futures = new HashMap<>();
		// 每隔0.1秒进行轮询
		future = service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				System.out.println("-------run------");
				String task = redisCache.getString(taskId);
				if (task != null && task.length() != 0) {
					Future future = futures.get(devNum);
					if (future != null) {
						futures.remove(devNum);
						future.cancel(true);
						// service.shutdown();
					}
				}
			}
		}, 0, 100, TimeUnit.MILLISECONDS);
		try {
			futures.put(devNum, future);
			// 设置超时时间
			future.get(3, TimeUnit.SECONDS);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
			log.error("InterruptedException:" + e2.getMessage());
		} catch (CancellationException e2) {
			log.info("CancellationException:" + e2);
		} catch (ExecutionException e2) {
			e2.printStackTrace();
			log.error("ExecutionException:" + e2.getMessage());
		} catch (TimeoutException e2) {
			log.info("TimeoutException:" + e2.getMessage());
		} finally {
			future.cancel(true);
			// service.shutdownNow();
		}
		String reply = redisCache.getString(taskId);
		if (reply != null && reply.length() != 0) {
			return Integer.valueOf(redisCache.getString(taskId));
		}
		return 404;
	}

}
