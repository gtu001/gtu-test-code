package gtu.apache;

import org.apache.commons.codec.digest.DigestUtils;

public abstract class MD5Utils {
	
//	一般設計的系統用戶的密碼都是採用MD5摘要資訊保存到資料庫,
//	但是現在網路有很多MD5庫,如果你不做任何處理就直接使用
//	MD5標準演算法提取摘要,最後很有可能導致用戶的密碼不幸已經
//	存在在現有MD5庫中.所以一般使用以下策略來避免這種情況:
//	使用一串隨機字串和明文組合成新密碼,然後提取這個新的密碼的MD5摘要
//	作為摘要保存在資料庫. 

	
	
//	在明文之前增加隨機字串,來產生特殊MD5的摘要
	private final static String MD5_RANDOM = " FC2F056F1D8E4D59BD95AE15EED9C9C0 ";

	/**
	 * 提取密碼摘要 
	 * @param password		密碼明文 
	 * @return				MD5摘要
	 */
	public static String encode(String password) {
		return DigestUtils.md5Hex(MD5_RANDOM + password);
	}
	
	public static void main(String[] args) {
	    String encodeVal = encode("AAA");
	    System.out.println(encodeVal);
	}
}