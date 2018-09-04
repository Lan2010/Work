package com.tianzhixing.devicecomm.mqtt;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import javax.net.ssl.SSLSocketFactory;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.tianzhixing.devicecomm.common.Constant;
import com.tianzhixing.devicecomm.common.ssl.SSLClient;
import com.tianzhixing.devicecomm.config.MqttConfiguration;
import com.tianzhixing.devicecomm.service.DeviceService;

/**
 * MQTT消费端
 * 
 * @author Administrator
 *
 */
@Configuration
public class MqttInboundConfiguration {

	@Autowired
	private MqttConfiguration mqttConfiguration;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private SSLClient sSLClient;
	
	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		String[] array = mqttConfiguration.getUrl().split(",");
		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(array);
		options.setUserName(mqttConfiguration.getUsername());
		options.setPassword(mqttConfiguration.getPassword().toCharArray());
		// 接受离线消息
		options.setCleanSession(false);
		try {
			SSLSocketFactory sSLSocketFactory = sSLClient.getSSLSocketFactory(mqttConfiguration);
			options.setSocketFactory(sSLSocketFactory);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		factory.setConnectionOptions(options);
		return factory;
	}

	@Bean
	public MessageProducer inbound() {
		String[] inboundTopics = mqttConfiguration.getTopics().split(",");
		String[] clientIds = mqttConfiguration.getClientId().split(",");
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				clientIds[0], mqttClientFactory(), inboundTopics);
		adapter.setCompletionTimeout(5000);
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {

		return new MessageHandler() {
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				System.out.println("-----------02----------");
				System.out.println("message:" + message);
				String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
				// 设备上下线
				if (Pattern.compile("^s/report/" + Constant.pattern + "/rctl/onoff$").matcher(topic).find()) {
					deviceService.dosign(message);
				}
				// 设备端回复•设置配置
				else if (Pattern.compile("^s/report/" + Constant.pattern + "/rctl/set_config/" + Constant.taskId)
						.matcher(topic).find()) {
					deviceService.getSetReply(message);
				}
				// 设备端回复•获取配置
				else if (Pattern.compile("^s/report/" + Constant.pattern + "/rctl/get_config/" + Constant.taskId)
						.matcher(topic).find()) {
					deviceService.getGetReply(message);
				}
				// 设备端回复•安装插件
				else if (Pattern.compile("^s/report/" + Constant.pattern + "/rctl/install_ipk/" + Constant.taskId)
						.matcher(topic).find()) {
					deviceService.getInstallIpkReply(message);
				}
				// 设备端回复•移除插件
				else if (Pattern.compile("^s/report/" + Constant.pattern + "/rctl/remove_ipk/" + Constant.taskId)
						.matcher(topic).find()) {
					deviceService.getRemoveIpkReply(message);
				}
				// 设备端回复•获取插件信息
				else if (Pattern.compile("^s/report/" + Constant.pattern + "/rctl/info_ipk/" + Constant.taskId)
						.matcher(topic).find()) {
					deviceService.getInfoIpkReply(message);
				}
				// 设备端回复•系统重启
				else if (Pattern.compile("^s/report/" + Constant.pattern + "/rctl/reboot/" + Constant.taskId)
						.matcher(topic).find()) {
					deviceService.getRebootReply(message);
				}
				// 设备端回复•执行短脚本
				else if (Pattern.compile("^s/report/" + Constant.pattern + "/rctl/shell/" + Constant.taskId)
						.matcher(topic).find()) {
					deviceService.getShellReply(message);
				}
				// 设备端回复•执行短脚本
				else if (Pattern.compile("^s/report/" + Constant.pattern + "/rctl/firmware/" + Constant.taskId)
						.matcher(topic).find()) {
					deviceService.getfirmwareReply(message);
				}
				// 设备端回复•更改终端登录密码
				else if (Pattern.compile("^s/report/" + Constant.pattern + "/rctl/set_passwd/" + Constant.taskId)
						.matcher(topic).find()) {
					deviceService.getSetPasswdReply(message);
				}
			}
		};
	}
}
