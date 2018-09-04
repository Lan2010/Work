package com.tianzhixing.devicecomm.common.ssl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tianzhixing.devicecomm.config.MqttConfiguration;

@Component
public class SSLClient {
	private static Logger log = LoggerFactory.getLogger(SSLClient.class);
//	private static String KEYSTORE = "D:/keystore/mqtt.keystore";
//	private static char[] PASSWORD = "123456".toCharArray();

	public SSLSocketFactory getSSLSocketFactory(MqttConfiguration mqttConfiguration) throws UnknownHostException, IOException {
		SSLContext context = initSSLContext(mqttConfiguration);
		SSLSocketFactory ssf = context.getSocketFactory();
		return ssf;
	}

	private SSLContext initSSLContext(MqttConfiguration mqttConfiguration) {
		SSLContext context = null;
		TrustManagerFactory tmf;
		try {
			tmf = TrustManagerFactory.getInstance("SunX509");
			KeyStore ts = KeyStore.getInstance("jceks");
			ts.load(new FileInputStream(mqttConfiguration.getKeystorePath()), mqttConfiguration.getKeystorePass().toCharArray());
			tmf.init(ts);
			TrustManager[] tm = tmf.getTrustManagers();
			context = SSLContext.getInstance("TLS");
			context.init(null, tm, null);
			return context;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("NoSuchAlgorithmException.",e);
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("KeyStoreException.",e);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("KeyManagementException.",e);
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("CertificateException.",e);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("FileNotFoundException.",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("IOException.",e);
		}catch(NullPointerException e) {
			e.printStackTrace();
			log.error("NullPointerException.",e);
		}
		return null;
	}

}
