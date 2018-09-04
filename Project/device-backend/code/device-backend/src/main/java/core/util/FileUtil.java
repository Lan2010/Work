package core.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileUtil {

	public static void main(String[] args) {
		String name ="libcstl_v2.0.3-1_mips_24kc(1).ipk";
		int a= name.lastIndexOf(".");
		System.out.println(a);
		String form = name.substring(name.lastIndexOf("."));
		System.out.println(form);

	}
	/*
	 * 上传文件并返回相对地址
	 */
	public static String uploadFile(CommonsMultipartFile file, String realUploadPath) throws IOException {
		System.out.println("realUploadPath:" + realUploadPath);
		// 如果目录不存在则创建目录
		File uploadFile = new File(realUploadPath);
		if (!uploadFile.exists()) {
			uploadFile.mkdirs();
		}
		String form = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		System.out.println("文件格式:" + form);
		String fileName = RandomStringUtils.randomAlphanumeric(8) + "_" + System.currentTimeMillis() + form;
		String outputPath = realUploadPath + fileName;
        File newFile=new File(outputPath);
        file.transferTo(newFile);
        return fileName;			
	}

	// 获取上传表单数据
	public static Map<String, Object> getData(List<FileItem> list) {
		Map<String, Object> data = new HashMap<String, Object>();
		for (FileItem item : list) {
			// 获取表单的属性名字
			String name = item.getFieldName();
			// 如果获取的表单信息是普通的文本 信息
			if (item.isFormField()) {
				String value = item.getString();
				data.put(name, value);
			} else {
				data.put("file", item);
			}
		}
		return data;
	}

	

	/**
	 * 从url中获取图片
	 * 
	 * @param destUrl
	 *            目标文件url
	 * @param localPath
	 *            保存到本地的路径
	 */
	public static String saveToFile(String destUrl, String localPath) {
		if(destUrl==null || "".equals(destUrl)) {
			return "";
		}
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		int BUFFER_SIZE = 1024;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		try {
			File file = new File(localPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			// 定义图片名
			String fileName = RandomStringUtils.randomAlphanumeric(8) + "_" + System.currentTimeMillis() + ".jpg";
			url = new URL(destUrl);
			httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());
			fos = new FileOutputStream(file+File.separator+fileName);
			while ((size = bis.read(buf)) != -1) {
				fos.write(buf, 0, size);
			}
			fos.flush();
			return fileName;
		} catch (IOException e) {
			return null;
		} catch (ClassCastException e) {
			return null;
		} finally {
			try {
				fos.close();
				bis.close();
				httpUrl.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}

	}
	
	//获取文件的MD5值
	public static String getMd5ByFile(CommonsMultipartFile file) {
		String md5 = null;
			try {
				InputStream in = file.getInputStream();
				 md5 = DigestUtils.md5Hex(in); 
				 in.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		return md5;
	}


}
