package core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSONObject;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 加解密工具类
 * @author dev-teng
 * @date 2018年6月11日
 */
public class EncryptUtil {
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 *  3DESECB加密,key必须是长度大于等于 3*8 = 24 位
	 * @param src
	 * @param key
	 * @return
	 * @throws Exception
	 */
    public static String encryptThreeDESECB(final String source, final String key) throws Exception {
        final DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        final SecretKey securekey = keyFactory.generateSecret(dks);

        final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        final byte[] b = cipher.doFinal(source.getBytes());

        final BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b).replaceAll("\r", "").replaceAll("\n", "");

    }

    /**
     *  3DESECB解密,key必须是长度大于等于 3*8 = 24 位
     * @param src
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptThreeDESECB(final String src, final String key) throws Exception {
        // --通过base64,将字符串转成byte数组
        final BASE64Decoder decoder = new BASE64Decoder();
        final byte[] bytesrc = decoder.decodeBuffer(src);
        // --解密的key
        final DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        final SecretKey securekey = keyFactory.generateSecret(dks);

        // --Chipher对象解密
        final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, securekey);
        final byte[] retByte = cipher.doFinal(bytesrc);

        return new String(retByte);
    }
    
    /**
     * md5加密
     * @param source
     * @return
     */
	public static String encodeByMD5(String source) {
		if (source == null) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source.getBytes()); // 通过使用 update 方法处理数据,使指定的 byte数组更新摘要
			byte[] encryptStr = md.digest(); // 获得密文完成哈希计算,产生128 位的长整数
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对每一个字节,转换成 16 进制字符的转换
				byte byte0 = encryptStr[i]; // 取第 i 个字节
				str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
				str[k++] = HEX_DIGITS[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			return new String(str); // 换后的结果转换为字符串
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 微信小程序解密获取手机号码
	 * @param sessionKey
	 * @param ivData
	 * @param encrypData
	 * @return
	 * @throws Exception
	 */
		public static String wxDecrypt(String sessionKey, String ivData, String encrypData)  throws Exception{
			byte[] sessionKeys = Base64.decodeBase64(sessionKey);
			byte[] ivDatas  = Base64.decodeBase64(ivData);
			byte[] encrypDatas =  Base64.decodeBase64(encrypData);
			String json = decrypt(sessionKeys,ivDatas,encrypDatas);
			
			JSONObject jsons = JSONObject.parseObject(json);  
			String cellPhone=jsons.getString("phoneNumber");
			return cellPhone;
		}

		private static String decrypt(byte[] key, byte[] iv, byte[] encData) throws Exception {
			AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv); 
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec); 
			
			//解析解密后的字符串 
			String json = new String(cipher.doFinal(encData),"UTF-8");
			return json;				
		}
    
    public static void main(String[] args) throws Exception {
    	String secretKey = "heallslsooIt43f2ee5332dds";
    	System.out.println(encryptThreeDESECB("luoyiteng", secretKey));
    	System.out.println(decryptThreeDESECB(encryptThreeDESECB("luoyiteng", secretKey),secretKey));
	}
    
    public static String encodeBySHA1(String str) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA1");
			md.update(str.getBytes());
			StringBuffer buf = new StringBuffer();
			byte[] bits = md.digest();
			for (int i = 0; i < bits.length; i++) {
				int a = bits[i];
				if (a < 0)
					a += 256;
				if (a < 16)
					buf.append("0");
				buf.append(Integer.toHexString(a));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
    
    /**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}
}
