package com.tianzhixing.devicecomm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.tomcat.util.codec.binary.Base64;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class MattSocketFactory {

	public static void main(String[] args) {
		
	}
	
	/**
	 * 双向认证
	 * @throws Exception
	 */
	private void init() throws Exception {
		MqttConnectOptions options = new MqttConnectOptions();
		SSLSocketFactory factory = getSSLSocktet("youpath/ca.crt", "youpath/client.crt", "youpath/client.pem",
				"password");

		options.setSocketFactory(factory);
	}

	private SSLSocketFactory getSSLSocktet(String caPath, String crtPath, String keyPath, String password)
			throws Exception {
		// CA certificate is used to authenticate server
		X509Certificate ca = getX509Certificate(caPath);
		KeyStore caKs = KeyStore.getInstance("JKS");
		caKs.load(null, null);
		caKs.setCertificateEntry("ca-certificate", ca);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
		tmf.init(caKs);

		X509Certificate caCert = getX509Certificate(crtPath);
		// client key and certificates are sent to server so it can authenticate
		// us
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		// ks.load(caIn,password.toCharArray());
		ks.load(null, null);
		ks.setCertificateEntry("certificate", caCert);
		ks.setKeyEntry("private-key", getPrivateKey(keyPath), password.toCharArray(), new java.security.cert.Certificate[] { caCert });
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
		kmf.init(ks, password.toCharArray());
		// keyIn.close();

		// finally, create SSL socket factory
		SSLContext context = SSLContext.getInstance("TLSv1");
		context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

		return context.getSocketFactory();
	}

	private X509Certificate getX509Certificate(String path) throws CertificateException, IOException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		FileInputStream crtIn = new FileInputStream(path);
		X509Certificate caCert = (X509Certificate) cf.generateCertificate(crtIn);
		crtIn.close();
		return caCert;
	}
	
	public PrivateKey getPrivateKey(String path) throws Exception {
		Base64 base64 = new Base64();
		byte[] buffer = base64.decode(getPem(path));
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

	}

	private String getPem(String path) throws Exception {
		FileInputStream fin = new FileInputStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(fin));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null) {
			if (readLine.charAt(0) == '-') {
				continue;
			} else {
				sb.append(readLine);
				sb.append('\r');
			}
		}
		fin.close();
		return sb.toString();
	}
}
