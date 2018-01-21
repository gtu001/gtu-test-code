package gtu.regex;

import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class RegexTest {
    
    /*
    正則表示式(Regular expression)
    方法  說明 matche
    .           任一字元
    \d          0~9
    \D          0~9以外字元
    \s          \t,\n,\x0B,\f,\r
    \w          a~z,A~Z,0~9
    \W          a~z,A~Z,0~9以外字元 (Ex:符號)

    表述一個字元
    [abc]                   a,b,c
    [^abc]                  a,b,c以外
    [a-zA-Z]                a-z,A-Z
    [a-dm-p] or [a-d[m-p]]  a-d,m-p
    [a-z&&[def]]            a-z並且是d或e或f ;其實就是d或e或f
    [a-z&&[^bc]]            a-z但不含b,c
    [a-z&&[^m-p]]           a-z但不含m~p

    表述一個字元後 可配合貪婪量詞
    字元(符號)  可出現次數
    ?           0或1
    *           >=0
    +           >=1
    {n}         n
    {n,}        >=n
    {n,m}       >=n,<=m
        */
	
//	public static void main(String[] args) {
//		String password = "aa";
//		Pattern p = Pattern.compile("[A-Z]+");
//		Pattern q = Pattern.compile("[a-z]+");
//		Pattern r = Pattern.compile("[0-9]+");
//		Pattern s = Pattern.compile("\\p{Punct}+");
//		Matcher m1 = p.matcher(password); // 判斷大寫
//		Matcher m2 = q.matcher(password); // 判斷小寫
//		Matcher m3 = r.matcher(password); // 判斷數值
//		Matcher m4 = s.matcher(password); // 判斷特殊字元
//		if (!m1.find(0) || !m2.find(0) || !m3.find(0) || !m4.find(0)) {
//			System.out.println("不符合");
//		}
//	}
    
//    public static void main(String[] args) {
//        String regex = "(\\w+):(\\w+)";
//        String parsestr = "aaaabbb:bbcccc";
//        Pattern p = Pattern.compile(regex);
//        Matcher m1 = p.matcher(parsestr);
//        
//        MatchResult result = m1.toMatchResult();
//        System.out.println("1:"+result.groupCount());
//    }
    public static void main(String[] args) {
        String regex = "(req.eform_%s_)([_A-Za-z0-9]+)(_\\d{1})";
        String parsestr = "req.eform_SP_REPAIR_TICKET_REPAIR_TICKET_ID_1";
        Pattern p = Pattern.compile(String.format(regex, "SP_REPAIR_TICKET"));
        Matcher m1 = p.matcher(parsestr);
        
        boolean ok = m1.find();
        
        System.out.println("groupCount = " + m1.groupCount());
        System.out.println("group 0 = " + m1.group(0));
        System.out.println("group 1 = " + m1.group(1));
        System.out.println("group 2 = " + m1.group(2));
        System.out.println("group 3 = " + m1.group(3));
        System.out.println("find = " + ok);
        System.out.println("matcher = " + m1);
    }
	
	
	/**
	 * 檢核是否為數值 
	 * <br>Ex:134.23 or 452 return true <br> Ex:323bd32 or 2334.3f return false
	 * @param data
	 * @return
	 */
	public static boolean chkNumber(String data){
		if(data==null || data.equals("")) return false;
		return Pattern.matches("(\\d+\\.\\d+|\\d+)",data);
	}
	
	
	
	/**
	 * 傳回符合正則運算式條件的字串list
	 * @param format		正則的pattern
	 * @param checkString	欲檢核的本文
	 * @return
	 */
	public static ArrayList<String> getRegExp(String format,String checkString){
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(checkString);
		ArrayList<String> rtnary = new ArrayList<String>();
		while(matcher.find()){
			rtnary.add(matcher.group());
		}
		return rtnary;
	}
	
	
	/**
	 * 將java的變數轉成 資料庫欄位格式
	 * Ex: loginUserId -> LOGIN_USER_ID
	 * @param javaParameter
	 * @return
	 */
	private static String javaParameterToDbfield1(String javaParameter){
		if(StringUtils.isEmpty(javaParameter)){
			return null;
		}
		char[] regex = javaParameter.toString().toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int ii = 0; ii < regex.length; ii++) {
			if(Character.isUpperCase(regex[ii])){
				sb.append("_"+regex[ii]);
			}else if(Character.isLowerCase(regex[ii])){
				sb.append(Character.toUpperCase(regex[ii]));
			}else{
				sb.append(regex[ii]);
			}
		}
		return sb.toString();
	}
		
	
	
	/**
	 * 將java的變數轉成 資料庫欄位格式
	 * Ex: loginUserId -> LOGIN_USER_ID
	 * @param javaParameter
	 * @return
	 */
	private static String javaParameterToDbfield2(String javaParameter){
		if(StringUtils.isEmpty(javaParameter)){
			return null;
		}
		char[] regex = javaParameter.toString().toCharArray();
		Pattern big = Pattern.compile("[A-Z]+");
		Pattern small = Pattern.compile("[a-z]+");
		Pattern math = Pattern.compile("[0-9]+");
		Pattern symbol = Pattern.compile("\\p{Punct}+");
		
		StringBuilder sb = new StringBuilder();
		for(int ii=0;ii<regex.length;ii++){
			String reg = new String(regex, ii, 1);
			Matcher m1 = big.matcher(reg); // 判斷大寫
			Matcher m2 = small.matcher(reg); // 判斷小寫
			Matcher m3 = math.matcher(reg); // 判斷數值
			Matcher m4 = symbol.matcher(reg); // 判斷特殊字元
			if (m1.find(0)) {
				sb.append("_"+reg);
			}else if (m2.find(0)) {
				sb.append(reg.toUpperCase());
			}else if (m3.find(0)) {
				sb.append(reg);
			}else if (m4.find(0)) {
				//sb.append(reg);
			}
		}
		return sb.toString();
	}
}
