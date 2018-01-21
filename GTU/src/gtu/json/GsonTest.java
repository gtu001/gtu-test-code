package gtu.json;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GsonTest {

    public static void main(String[] args) throws IOException {
        GsonTest test = new GsonTest();
        
        File jsonFile = new File("D:/workstuff/workspace/gtu-test-code/GTU/src/gtu/json/GsonTestResources.json");
        String jsonStr = FileUtils.readFileToString(jsonFile);
        
        Gson gson = new GsonBuilder().create();
        java.lang.reflect.Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        
        Map<String,Object> jsonMap = gson.fromJson(jsonStr, mapType);
        for(String key : jsonMap.keySet()){
            System.out.println("key == " + key);
            System.out.println("value == " + jsonMap.get(key));
        }
        
        CashRechargeMethodInfo parseObj1 = gson.fromJson((String) jsonMap.get("CashRechargeMethodInfo"), CashRechargeMethodInfo.class);
        RPL9AethosChannelInfo parseObj2 = gson.fromJson((String) jsonMap.get("RPL9AethosChannelInfo"), RPL9AethosChannelInfo.class);
        
        System.out.println(ReflectionToStringBuilder.toString(parseObj1));
        System.out.println(ReflectionToStringBuilder.toString(parseObj2));
    }
    
    private static class CashRechargeMethodInfo {
        private String amount;
        private String currency;
    }
    
    private static class RPL9AethosChannelInfo {
        private String referenceId;
        private String requestDateTime;
        private String bulkTransaction;
    }
}
