package nats;

import java.io.IOException;

import core.common.Constant;
import core.util.CommonUtils;
import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;
import io.nats.client.Message;
import io.nats.client.MessageHandler;

public class Sub {
	public static void main(String[] args) throws IOException {
		ConnectionFactory cf = new ConnectionFactory(CommonUtils.getPropertiesValue(Constant.APPLICATION_CONFIG_PATH,"io.nats.client.url"));
		Connection nc = cf.createConnection();
		// Lambda 表达式写法
		/*
		 * nc.subscribe("foo", m -> { System.out.printf( "收到的消息：%s\n", new
		 * String(m.getData())); });
		 */
		nc.subscribe("oms.subject.device.bind-unbind", new MessageHandler() {
			@Override
			public void onMessage(Message msg) {
				System.out.println("收到的消息：" + new String(msg.getData()));
			}
		});
	}
}
