package com.tianzhixing.devicecomm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class SSLClient {
	private static String KEYSTORE = "D:/keystore/mqtt.keystore";
	private static char[] PASSWORD = "123456".toCharArray();
	private static int PORT = 8883;

	public static void main(String[] args) {
		SSLSocket sSLSocket = null;
		PrintWriter writer = null;
		try {
			sSLSocket = createSocket("192.168.11.11", PORT);
			System.out.println("连接成功");
			writer = new PrintWriter(sSLSocket.getOutputStream());
			// 接受服务器消息
			BufferedReader reader = new BufferedReader(new InputStreamReader(sSLSocket.getInputStream()));
			System.out.println(reader.readLine());
			// 向服务器发送消息
			writer.println("{\"msg\":\"luo yi teng\",\"code\":\"0\"}");
			writer.flush();
		} catch (UnknownHostException e) { 
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
			try {
				if (sSLSocket != null)
					sSLSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private static SSLSocket createSocket(String url, int port) throws UnknownHostException, IOException {
		SSLContext context = initSSLContext();
		SSLSocketFactory ssf = context.getSocketFactory();
		SSLSocket sSLSocket = (SSLSocket) ssf.createSocket(url, port);
		return sSLSocket;
	}

	private static SSLContext initSSLContext() {
		SSLContext context = null;
		try {
			KeyStore ts = KeyStore.getInstance("jceks");
			ts.load(new FileInputStream(KEYSTORE), PASSWORD);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ts);
			TrustManager[] tm = tmf.getTrustManagers();
			context = SSLContext.getInstance("TLS");
			context.init(null, tm, null);
			return context;
		} catch (Exception e) { // 省略捕获的异常信息
			e.printStackTrace();
			return null;
		}
	}
}
