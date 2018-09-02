package gtu.string;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class UnicodeByteUtil {
	public static void main(String[] args) {
		UnicodeByteUtil instance = new UnicodeByteUtil();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String line;

		try {
			while ((line = reader.readLine()) != null) {
				if (line.trim().equals("q"))
					System.exit(0);
				String s = instance.getBytes(line);
				System.out.println("bytes:" + s);
				// System.out.println("line:"+);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getBytes(String s) {
		try {
			StringBuffer out = new StringBuffer("");
			byte[] bytes = s.getBytes("unicode");
			for (int i = 0; i < bytes.length; i++)
				System.out.println(bytes[i]);
			for (int i = 2; i < bytes.length - 1; i += 2) { // *
				out.append("\\u");
				String str = Integer.toHexString(bytes[i + 1] & 0xff);// **
				for (int j = str.length(); j < 2; j++) {
					out.append("0");// ***
				}
				String str1 = Integer.toHexString(bytes[i] & 0xff);
				out.append(str);
				out.append(str1);
			}
			return out.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String loadConvert(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);

		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':case '1':case '2':case '3':case '4':case '5':
						case '6':case '7':case '8':case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':case 'b':case 'c':case 'd':case 'e':case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':case 'B':case 'C':case 'D':case 'E':case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')		aChar = '\t';
					else if (aChar == 'r')	aChar = '\r';
					else if (aChar == 'n')	aChar = '\n';
					else if (aChar == 'f')	aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}
}