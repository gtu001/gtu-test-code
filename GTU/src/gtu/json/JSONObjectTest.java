package gtu.json;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectTest {

    /**
     * @param args
     * @throws JSONException 
     */
    public static void main(String[] args) throws JSONException {
        JSONObject jsonObj = new JSONObject("{dddd:'aaaa'}");
        jsonObj.put("DDD", "EEE");
        System.out.println(jsonObj);
    }

    static class Test {
        public String aaa = "AAA";
        public String bbb = "BBB";
    }
}
