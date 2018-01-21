package gtu.binary;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;


public class CodeConversion {

	public void stringToBig5(String args[])
			throws java.io.UnsupportedEncodingException {
		String query = "今天天氣很好";
		String unicode_query = "";
		for (int i = 0; i <= query.length() - 1; i++) {
			if (query.charAt(i) >= 0x100)
				unicode_query += Integer.toHexString(query.charAt(i)) + " ";
			else
				unicode_query += query.charAt(i);
		}
		byte[] big5_stream = query.getBytes("big5");
		// java.io.UnsupportedEncodingException
		String big5_query = "";
		for (int i = 0; i <= big5_stream.length - 1; i++) {
			int b1, b2;
			b1 = big5_stream[i];
			b2 = (i + 1 <= big5_stream.length - 1) ? big5_stream[i + 1] : 0;
			b1 = (b1 >= 0) ? b1 : 0x100 + b1;
			b2 = (b2 >= 0) ? b2 : 0x100 + b2;
			// out.println("b1="+b1+",b2="+b2);
			if ((b1 >= 0xa1 && b1 <= 0xf9)
					&& ((b2 >= 0x40 && b2 <= 0x7e) || (b2 >= 0xa1 && b2 <= 0xfe))) {
				big5_query += Integer.toHexString(b1) + Integer.toHexString(b2)
						+ " ";
				i++;
			} else
				big5_query += (char) big5_stream[i];
		}
		System.out.println("查詢字串: " + query);
		System.out.println("查詢字串的統一碼 : " + unicode_query);
		System.out.println("查詢字串的大五碼 : " + big5_query);
	}

	/**
	 * 中文转unicode
	 * 
	 * @param str
	 * @return 反回unicode编码
	 */
	public String chinaToUnicode(String str) {
		String result = null;
		for (int i = 0; i < str.length(); i++) {
			int chr1 = (char) str.charAt(i);
			result += "\\u" + Integer.toHexString(chr1);
		}
		return result;
	}

	/**
	 * unicode转中文
	 * 
	 * @param str
	 * @return 中文
	 */
	public void unicodeToChinese(String str) {

		for (char c : str.toCharArray())

			System.out.print(c);

	}
}