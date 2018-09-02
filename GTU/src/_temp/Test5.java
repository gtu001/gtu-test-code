package _temp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;



public class Test5 {
    @SuppressWarnings("unchecked")
    public static final Map<String, String> CO_TYPE_MAP = ArrayUtils.toMap(new Object[] {//
            new String[] { "LH", "續約單通話" }, //
                    new String[] { "LC", "續約搭手機" }, //
                    new String[] { "DA_HANDSET", "單買手機" }, //
                    new String[] { "DA_ACCESSORY", "單買配件" }, //
                    new String[] { "D1", "單買手機" }, //
                    new String[] { "D2", "單買配件" }, //
            });

    public static void main(String[] args) throws InterruptedException{
//        System.out.println(CO_TYPE_MAP);
        List<String> xxx  =new ArrayList<String>();
        xxx.add("aaa  aa");
        xxx.add("bbbb");
        xxx.add("cc  cc");
        xxx.add("ddddd");
        System.out.println(xxx.toString().replaceAll("^\\[|\\]$", " ").replaceAll(" ", "^").replaceAll(",", "','").replaceAll("\\^", " "));
        
    }
}
