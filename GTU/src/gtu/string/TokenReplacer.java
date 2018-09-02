package gtu.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenReplacer {

    public static void main(String[] args){
        String str = "_#[2].get(\"bbb\").substring(0,2).toLowerCase()#_ == \"cc\" || _#[2].get(\"bbb\").substring(0,2).toLowerCase()#_ == \"cc\"";
        String rtnVal = TokenReplacer.newInstance().pattern("\\_\\#", "\\#\\_")//
        .replace(str, new TokenReplacerAction() {
            @Override
            public String apply(String finder) {
                return "@@@@@";
            }
        });
        System.out.println("---->" + rtnVal);
        System.out.println("done...");
    }
    
    private TokenReplacer(){}
    
    public static TokenReplacer newInstance() {
        TokenReplacer token = new TokenReplacer();
        return token;
    }
    
    String startPattern;
    String endPattern;
    public TokenReplacer pattern(String startPattern, String endPattern){
        this.startPattern = startPattern;
        this.endPattern = endPattern;
        return this;
    }
    
    public interface TokenReplacerAction {
        String apply(String finder);
    }
    
    public String replace(String parseStr, TokenReplacerAction action){
        Pattern startPtn = Pattern.compile(startPattern);
        Pattern endPtn = Pattern.compile(endPattern);
        StringBuilder sb = new StringBuilder();
        int startPos = 0;
        
        Matcher mth1 = startPtn.matcher(parseStr);
        Matcher mth2 = endPtn.matcher(parseStr);
        for(;;){
            if(mth1.find() && mth2.find()){
                if((mth1.start() < mth2.end())){
                    sb.append(parseStr.substring(startPos, mth1.start()));
                    String tmpStr = parseStr.substring(mth1.start(), mth2.end());
                    startPos = mth2.end();
                    System.out.println(tmpStr);
                    sb.append(action.apply(tmpStr));
                }else{
                    throw new IndexOutOfBoundsException("取得 start = " + mth1.start() + ", end = " + mth2.end() + ", 前大於後錯誤 , parseStr = " + parseStr);
                }
            }else{
                break;
            }
        }
        sb.append(parseStr.substring(startPos));
        return sb.toString();
    }
}
