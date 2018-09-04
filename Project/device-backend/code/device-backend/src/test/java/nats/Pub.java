package nats;

import java.io.IOException;
import java.util.Scanner;

import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;
import io.nats.client.Message;

public class Pub {
	public static void main(String[] args) throws IOException {
		ConnectionFactory cf = new ConnectionFactory("nats://192.168.11.18:4222");
		Connection nc = cf.createConnection();
		// 消息
		Message msg = new Message();
		// 设置主题
		msg.setSubject("oms.subject.device.bind-unbind");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("请输入字符串：");
		while (true) {
			String line = scanner.nextLine();
			msg.setData(line.getBytes());
			// 发布消息
			nc.publish(msg);
		}
		
	}

}
