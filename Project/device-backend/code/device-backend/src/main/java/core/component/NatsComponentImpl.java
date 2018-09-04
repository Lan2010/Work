package core.component;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import core.common.Constant;
import core.service.DeviceService;
import core.util.CommonUtils;
import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;
import io.nats.client.Message;
import io.nats.client.MessageHandler;

public class NatsComponentImpl implements NatsComponent{
	@Autowired
	private DeviceService deviceService;

	private static Logger log = LoggerFactory.getLogger(NatsComponent.class);

	private static ConnectionFactory cf = new ConnectionFactory(
			CommonUtils.getPropertiesValue(Constant.APPLICATION_CONFIG_PATH, "io.nats.client.url"));
	
	/**
	 * sub消息处理
	 */
	public void handleMessage() {
		try {
			Connection nc = cf.createConnection();
			// Lambda 表达式写法
			/*
			 * nc.subscribe("foo", m -> { System.out.printf( "收到的消息：%s\n", new
			 * String(m.getData())); });
			 */
			nc.subscribe("oms.subject.device.bind-unbind", new MessageHandler() {
				@Override
				public void onMessage(Message msg) {
					deviceService.updateBind(msg);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception.", e);
		}
	}

	/**
	 * 登录、登出消息
	 * 
	 * @param msg
	 */
//	public void login4oms(String msg) {
//		Connection nc = null;
//		try {
//			nc = cf.createConnection();
//			String subject = "oms.subject.user.login-logout";// 主题名
//			byte[] data = msg.getBytes();// message payload,内容为json格式，转换成字节数组
//			nc.publish(subject, data);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log.error("Exception.", e);
//		} finally {
//			if (!nc.isClosed())
//				nc.close();
//		}
//	}
	/**
	 * 发布消息，提供给运营平台的数据
	 * @param msg
	 * @param subject
	 */
	public void publish4oms(String subject,String msg) {
		Connection nc = null;
		try {
			nc = cf.createConnection();
			byte[] data = msg.getBytes();// message payload,内容为json格式，转换成字节数组
			nc.publish(subject, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("Exception.", e);
		} finally {
			if (!nc.isClosed())
				nc.close();
		}
	}
}
