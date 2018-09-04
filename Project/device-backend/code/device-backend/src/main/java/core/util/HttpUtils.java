package core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http工具类
 * @author dev-teng
 * @date 2018年6月11日
 */
public class HttpUtils {
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	/**
	 * post 请求
	 * 
	 * @param url
	 * @return
	 */
	public static String post(String url) {
		return post(url, "");
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public static String post(String url, String data) {
		return httpPost(url, data);
	}

	/**
	 * 发送http post请求
	 * 
	 * @param url
	 *            url
	 * @param instream
	 *            post数据的字节流
	 * @return
	 */
	public static String post(String url, InputStream instream) {
		try {
			HttpEntity entity = Request.Post(url).bodyStream(instream, ContentType.create("text/html", Consts.UTF_8))
					.execute().returnResponse().getEntity();
			return entity != null ? EntityUtils.toString(entity) : null;
		} catch (Exception e) {
			logger.error("post请求异常，" + e.getMessage() + "\n post url:" + url);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * post 请求
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	private static String httpPost(String url, String data) {
		try {
			HttpEntity entity = Request.Post(url).bodyString(data, ContentType.create("text/html", Consts.UTF_8))
					.execute().returnResponse().getEntity();
			return entity != null ? EntityUtils.toString(entity) : null;
		} catch (Exception e) {
			logger.error("post请求异常，" + e.getMessage() + "\n post url:" + url);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * POST方式提交JSON字符串数据
	 * 
	 * @param url
	 *            请求URL
	 * @param data
	 *            请求JSON字符串
	 * @return
	 */
	public static String httpPostJSON(String url, String data) {
		try {
			HttpEntity entity = Request.Post(url)
					.bodyString(data, ContentType.create("applicateion/json", Consts.UTF_8)).execute().returnResponse()
					.getEntity();
			return entity != null ? EntityUtils.toString(entity) : null;
		} catch (Exception e) {
			logger.error("post请求异常，" + e.getMessage() + "\n post url:" + url);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 上传文件
	 * 
	 * @param url
	 *            URL
	 * @param file
	 *            需要上传的文件
	 * @return
	 */
	public static String postFile(String url, File file) {
		return postFile(url, null, file);
	}

	/**
	 * 上传文件
	 * 
	 * @param url
	 *            URL
	 * @param name
	 *            文件的post参数名称
	 * @param file
	 *            上传的文件
	 * @return
	 */
	public static String postFile(String url, String name, File file) {
		try {
			HttpEntity reqEntity = MultipartEntityBuilder.create().addBinaryBody(name, file).build();
			Request request = Request.Post(url);
			request.body(reqEntity);
			HttpEntity resEntity = request.execute().returnResponse().getEntity();
			return resEntity != null ? EntityUtils.toString(resEntity) : null;
		} catch (Exception e) {
			logger.error("postFile请求异常，" + e.getMessage() + "\n post url:" + url);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 下载文件
	 * 
	 * @param url
	 *            URL
	 * @return 文件的二进制流，客户端使用outputStream输出为文件
	 */
	public static byte[] getFile(String url) {
		try {
			Request request = Request.Get(url);
			HttpEntity resEntity = request.execute().returnResponse().getEntity();
			return EntityUtils.toByteArray(resEntity);
		} catch (Exception e) {
			logger.error("postFile请求异常，" + e.getMessage() + "\n post url:" + url);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从指定URL获取文件流写入到OutputStream
	 * 
	 * @param url
	 * @param outstream
	 */
	public static void copyStream(String url, OutputStream outstream) {
		try {
			Request request = Request.Get(url);
			HttpEntity resEntity = request.execute().returnResponse().getEntity();
			if (resEntity.isStreaming() == true) {
				resEntity.writeTo(outstream);
			}
		} catch (IOException e) {
			logger.error("IO异常，" + e.getMessage() + "\n post url:" + url);
			e.printStackTrace();
		}
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		return httpGet(url);
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return
	 */
	private static String httpGet(String url) {
		try {
			HttpEntity entity = Request.Get(url).execute().returnResponse().getEntity();
			return entity != null ? EntityUtils.toString(entity) : null;
		} catch (Exception e) {
			logger.error("get请求异常，" + e.getMessage() + "\n get url:" + url);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param url
	 * @param file
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static void downloadFile(String url, File file)
			throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {
		InputStream inputStream = null;
		URL reqURL = null;
		HttpsURLConnection httpsConn = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			reqURL = new URL(url);
			httpsConn = (HttpsURLConnection) reqURL.openConnection();
			httpsConn.setRequestMethod("GET");
			httpsConn.setDoInput(true);
			httpsConn.setDoOutput(true);
			httpsConn.connect();
			inputStream = httpsConn.getInputStream();
			bis = new BufferedInputStream(inputStream);
			bos = new BufferedOutputStream(FileUtils.openOutputStream(file));
			byte[] buf = new byte[2048];
			int length = bis.read(buf);
			while (length != -1) {
				bos.write(buf, 0, length);
				length = bis.read(buf);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (httpsConn != null)
					httpsConn.disconnect();
				if (bos != null)
					bos.close();
				if (bis != null)
					bis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 发送https get 请求
	 */
	public static String httpsGet(String urlString) {
		String ResponseStr = "";
		InputStreamReader isr = null;
		try {
			URL reqURL = new URL(urlString);
			HttpsURLConnection httpsConn = (HttpsURLConnection) reqURL.openConnection();
			httpsConn.setReadTimeout(5 * 1000);
			httpsConn.setRequestMethod("GET");
			httpsConn.connect();
			isr = new InputStreamReader(httpsConn.getInputStream(), "utf-8");

			char[] chars = new char[1024];
			ResponseStr = "";
			int len;
			while ((len = isr.read(chars)) != -1) {
				ResponseStr += new String(chars, 0, len);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (isr != null)
					isr.close();
			} catch (IOException e) {
				logger.error("InputStreamReader关闭异常");
				throw new RuntimeException();
			}
		}
		return ResponseStr;
	}

	/**
	 * 发送https post 请求
	 */
	public static String httpsPost(String urlString, String xmlData) {
		String responseStr = "";
		if (xmlData == null || xmlData.length() == 0) {
			return responseStr;
		}
		try {
			// 创建一个url
			URL reqURL = new URL(urlString);
			// 拿取链接
			HttpsURLConnection httpsConn = (HttpsURLConnection) reqURL.openConnection();
			httpsConn.setReadTimeout(5 * 1000);
			httpsConn.setDoOutput(true);
			httpsConn.setRequestMethod("POST");
			if (xmlData.contains("<xml>") && xmlData.contains("</xml>")) {
				httpsConn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
				httpsConn.setRequestProperty("Content-Length", String.valueOf(xmlData.getBytes().length));
			}
			httpsConn.connect();
			// 取得该连接的输出流，以读取响应内容
			OutputStreamWriter osr = new OutputStreamWriter(httpsConn.getOutputStream());
			// 往输出流里面写入数据
			osr.write(xmlData);
			osr.close(); // 这里关闭的仅仅是输出流，连接并未关闭
			// 返回结果
			InputStreamReader isr = new InputStreamReader(httpsConn.getInputStream());
			// 读取服务器的响应内容并显示
			char[] chars = new char[1024];
			int len;
			while ((len = isr.read(chars)) != -1) {
				responseStr += new String(chars, 0, len);
			}
			System.out.println("返回结果:" + responseStr);
			isr.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return responseStr;
	}

}
