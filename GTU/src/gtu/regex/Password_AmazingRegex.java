package gtu.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Password_AmazingRegex {

    public static void main(String[] args) {
        //1.要有特殊符號,2.要有大寫字母,3.要有小寫字母,4.要有數字,5.要超過6馬
        Pattern ptn = Pattern.compile("^(?=.*[^a-zA-Z0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{6,}$");
        String str = "1234aA*";
        Matcher mth = ptn.matcher(str);
        boolean findOk = mth.matches();
        System.out.println(findOk);
        for(int ii = 0 ; ii <= mth.groupCount() ; ii ++){
            System.out.println(ii + "..." + mth.group(ii));
        }
    }
}
