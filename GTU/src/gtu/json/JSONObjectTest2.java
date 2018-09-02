package gtu.json;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.JSONException;

public class JSONObjectTest2 {

    public static void main(String[] args) throws JSONException {
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("aaa", 33);
        Map<String,Object> data2 = new HashMap<String,Object>();
        data.put("bbb", data2);
        data2.put("testStr", "okok");
        data2.put("testInt", 100);
        JSONObject jsonObject = net.sf.json.JSONObject.fromObject(data);
        System.out.println(jsonObject.toString());
        
        net.sf.json.JSONObject fromObj = new net.sf.json.JSONObject();
        fromObj.put("aaa", "ok1");
        fromObj.put("bbb", "ok2");
        JSONObjectTest2_Test test = (JSONObjectTest2_Test)net.sf.json.JSONObject.toBean(fromObj, JSONObjectTest2_Test.class);
        System.out.println(ReflectionToStringBuilder.toString(test));
        
        JSONObject jsonObject2 = net.sf.json.JSONObject.fromObject(test);
        System.out.println(jsonObject2.toString());
    }

    public static class JSONObjectTest2_Test {
        private String aaa;
        private String bbb;
        public String getAaa() {
            return aaa;
        }
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }
        public String getBbb() {
            return bbb;
        }
        public void setBbb(String bbb) {
            this.bbb = bbb;
        }
    }
}
