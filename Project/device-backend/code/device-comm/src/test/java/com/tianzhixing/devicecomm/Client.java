package com.tianzhixing.devicecomm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class Client {
	private SSLSocket socket = null;

	public Client() throws IOException, NoSuchAlgorithmException {
		// 通过套接字工厂，获取一个客户端套接字
		SSLContext context = SSLContext.getInstance("TLS");;
		SSLSocketFactory socketFactory = context.getSocketFactory();
		socket = (SSLSocket) socketFactory.createSocket("192.168.11.11", 8443);
	}

	public void connect() {
		try {
			// 获取客户端套接字输出流
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			// 将用户名和密码通过输出流发送到服务器端
			String userName = "principal";
			output.println(userName);
			String password = "credential";
			output.println(password);
			output.flush();

			// 获取客户端套接字输入流
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 从输入流中读取服务器端传送的数据内容，并打印出来
			String response = input.readLine();
			response += "\n " + input.readLine();
			System.out.println(response);

			// 关闭流资源和套接字资源
			output.close();
			input.close();
			
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
	}

	public static void main(String args[]) throws IOException, NoSuchAlgorithmException {
		new Client().connect();
	}
}
