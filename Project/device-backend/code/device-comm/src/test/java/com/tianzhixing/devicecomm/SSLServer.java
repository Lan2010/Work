package com.tianzhixing.devicecomm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class SSLServer extends Thread {
	private Socket socket;

	public SSLServer(Socket socket) {
		this.socket = socket;
	}
	
	private static String KEYSTORE_PATH = "D:/keystore/mqtt.keystore";
	private static String KEY_PASS = "123456";
	private static int PORT = 8443;
	
	public void run() {
		PrintWriter writer = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
			//向客户端发消息
			writer.println("welcome!");
			writer.flush();
			//接受客户端消息
			String data = reader.readLine();
			JSONObject json = JSON.parseObject(data);
			String code= (String) json.get("code");
			String msg= (String) json.get("msg");
			System.out.println(code+":"+msg +" is coming");
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("javax.net.ssl.trustStore", KEYSTORE_PATH);
		SSLContext context = SSLContext.getInstance("TLS");

		KeyStore ks = KeyStore.getInstance("jceks");
		ks.load(new FileInputStream(KEYSTORE_PATH), null);
		KeyManagerFactory kf = KeyManagerFactory.getInstance("SunX509");
		kf.init(ks, KEY_PASS.toCharArray());
		context.init(kf.getKeyManagers(), null, null);
		ServerSocketFactory factory = context.getServerSocketFactory();
		ServerSocket _socket = factory.createServerSocket(PORT);
		((SSLServerSocket) _socket).setNeedClientAuth(false);
		SSLServer sSLServer = null;
		while (true) {
			sSLServer = new SSLServer(_socket.accept());
			sSLServer.start();
		}
	}
}
