package gtu.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONTokener;

public class StringToObjTest2 {
    
    public static void main(String[] args) throws JSONException {
        StringToObjTest2 pc = new StringToObjTest2();
        System.out.println(pc.getMapJsonString("'aaaa'"));
    }
    
    private List<Object> getMapJsonString(String jsonstr) throws JSONException {
        JSONTokener jtoker = new JSONTokener(jsonstr);
        List<Object> list = new ArrayList<Object>();
        while (jtoker.more()) {
            Object json = jtoker.nextValue();
            list.add(this.parseJsonAll(json));
        }
        return list;
    }
    
    private Object parseJsonAll(Object json) throws JSONException {
        if(json == null) {
            return null;
        }
        if(json instanceof JSONObject) {
            return this.parseJsonObject((JSONObject)json);
        }
        if(json instanceof JSONArray) {
            return this.parseJsonArray((JSONArray)json);
        }
        if(json instanceof JSONString) {
            System.out.println("jstring");
        }
        System.out.println(json+"....."+json.getClass());
        return json;
    }
    
    private List<Object> parseJsonArray(JSONArray json) throws JSONException{
        List<Object> list = new ArrayList<Object>();
        for(int ii = 0 ; ii < json.length() ; ii++) {
            list.add(parseJsonAll(json.get(ii)));
        }
        return list;
    }
    
    private Map<String,Object> parseJsonObject(JSONObject json) throws JSONException{
        Map<String,Object> map = new HashMap<String,Object>();
        Iterator<String> it = json.keys();
        while (it.hasNext()) {
            String key = it.next();
            Object value = json.get(key);
            map.put(key, parseJsonAll(value));
        }
        return map;
    }
}
