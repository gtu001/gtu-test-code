package gtu.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * @author Troy 2009/05/26
 * 
 */
public class StringToObjTest {

    public static void main(String[] args) throws Exception {
        // String json = "['aaa','vvvv','eeee']";
        // List map = JsonUtil.getListJsonString(json);
        // System.out.println(map);

        String jmap = "{ORGANIZATION_ID:'6', ITEM_NUMBER:'BMP 903 021/1', SERIAL_NUMBER:'A0800D1MQ3'  };{ORGANIZATION_ID:'6', ITEM_NUMBER:'BMP 903 021/1', SERIAL_NUMBER:'A0800EUD7K'  };{ORGANIZATION_ID:'6', ITEM_NUMBER:'BMP 903 021/1', SERIAL_NUMBER:'A0800BUJN5'  }";
        String[] xxx = jmap.split(";");
        for (String x : xxx) {
            Map mmm = StringToObjTest.getMapJsonString(x);
            System.out.println(mmm);
        }

    }

    /**
     * 將json字串"['aaa','vvvv','eeee']" 解析為 List
     * 
     * @param jsonstr
     * @return
     * @throws JSONException
     */
    private static List<String> getListJsonString(String jsonstr) throws JSONException {
        List<String> list = new ArrayList<String>();
        JSONTokener jtoker = new JSONTokener(jsonstr);
        JSONArray json = new JSONArray();
        while (jtoker.more()) {
            json = (JSONArray) jtoker.nextValue();
        }
        for (int ii = 0; ii < json.length(); ii++) {
            list.add(json.getString(ii));
        }
        return list;
    }

    /**
     * 將json字串"{aaa=bbb,ccc=ddd}" 解析為map物件
     * 
     * @param jsonstr
     * @return
     * @throws JSONException
     */
    private static Map<String, String> getMapJsonString(String jsonstr) throws JSONException {
        Map<String, String> map = new HashMap<String, String>();
        JSONTokener jtoker = new JSONTokener(jsonstr);
        JSONObject json = new JSONObject();
        while (jtoker.more()) {
            json = (JSONObject) jtoker.nextValue();
        }
        Iterator it = json.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            String value = (String) json.get(key);
            map.put(key, value);
        }
        return map;
    }
}
