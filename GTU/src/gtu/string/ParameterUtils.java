package gtu.string;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 將字串轉成各種型態
 * 
 * loginUserId , LOGIN_USER_ID , login-user-id , etc..
 * @author admin
 *
 */
public class ParameterUtils {
	
	public static void main(String[] args){
		ParameterUtils p = new ParameterUtils();
		p.checkEveryChar("#d09喝N");
	}

	public String checkEveryChar(String str) {
		class CharCheck {
			private boolean isFindInRegex(char c, String format) {
				if (Pattern.compile(format).matcher(Character.toString(c)).find(0)) {
					return true;
				}
				return false;
			}
		}
		CharCheck cc = new CharCheck();
		char[] allChar = str.toString().toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int ii = 0; ii < allChar.length; ii++) {
			System.out.println("=========================");
			System.out.println("isLowerCase--"+cc.isFindInRegex(allChar[ii], "[a-z]+") +"...."+allChar[ii]);
			System.out.println("isUpperCase--"+cc.isFindInRegex(allChar[ii], "[A-Z]+") +"...."+allChar[ii]);
			System.out.println("isElseCase--"+cc.isFindInRegex(allChar[ii], "\\p{Punct}+") +"...."+allChar[ii]);
			System.out.println("isNumberCase--"+cc.isFindInRegex(allChar[ii], "[0-9]+") +"...."+allChar[ii]);
		}
		return sb.toString();
	}
}
