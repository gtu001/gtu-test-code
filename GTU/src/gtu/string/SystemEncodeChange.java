package gtu.string;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

/**
 * XXX 重要!!!!
 * 改變系統encode
 * 若不設定 執行時要下指令 java -Dfile.encoding=UTF-8 … com.x.Main
 */
public class SystemEncodeChange {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }
    
    public static String getSystemEncode(){
        return Charset.defaultCharset().toString() + " - " + System.getProperty("file.encoding");
    }
    
    public static void setSystemEncode(String encode){
        try {
            System.setProperty("file.encoding", encode);
            Field charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
