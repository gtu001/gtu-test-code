package gtu.binary;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.log4j.Logger;

public class FucoStringUtil {
	
	private static String magic = "Fcuo@1234";

	private static String AlphaNumber = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	public static SecureRandom sr = null;
	
	private static final Logger logger = Logger.getLogger(FucoStringUtil.class);
	
	public static SecureRandom getSecureRandom() {
		if (null == sr) {
			try {
				sr = SecureRandom.getInstance("SHA1PRNG");
			} catch (NoSuchAlgorithmException e) {
				logger.error("getSecureRandom throw NoSuchAlgorithmException");
				return null;
			}
		}
		return sr;
	}

	public static String randomAlphanumeric(int length) {
		getSecureRandom().setSeed(System.currentTimeMillis());
		byte[] ret = new byte[length];
		for (int i = 0; i < ret.length; i++) {
			int idx = (int) (sr.nextDouble() * AlphaNumber.length());
			ret[i] = (byte) AlphaNumber.charAt(idx);
		}
		return new String(ret);
	}
	
	public static String getDigest(String input, String algorithm) {
		String ret = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] barr = md.digest(input.getBytes()); // 將 byte 陣列加密
			StringBuffer sb = new StringBuffer(); // 將 byte 陣列轉成 16 進制
			for (int i = 0; i < barr.length; i++) {
				sb.append(byte2Hex(barr[i]));
			}
			String hex = sb.toString();
			ret = hex.toUpperCase(); // 一律轉成大寫
		} catch (Exception e) {
			logger.error("getDigest throw Exception");
		}
		return ret;
	}

	private static String byte2Hex(byte b) {
		String[] h = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
		int i = b;
		if (i < 0) {
			i += 256;
		}
		return h[i / 16] + h[i % 16];
	}
	
	public static String concat(String... strs){
		StringBuilder sb = new StringBuilder();
		for(String str: strs){
			sb.append(str);
		}
		return sb.toString();
	}
	
	
	public static String md5(String str) {
		String input = magic + str;
		String md5 = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] barr = md.digest(input.getBytes()); // 將 byte 陣列加密
			StringBuffer sb = new StringBuffer(); // 將 byte 陣列轉成 16 進制
			for (int i = 0; i < barr.length; i++) {
				sb.append(byte2Hex(barr[i]));
			}
			String hex = sb.toString();
			md5 = hex.toUpperCase(); // 一律轉成大寫
		} catch (Exception e) {
			logger.error("md5 throw Exception");
		}
		return md5;
	}

}
