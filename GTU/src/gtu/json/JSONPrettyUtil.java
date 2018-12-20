package gtu.json;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.sf.json.util.JSONUtils;

public class JSONPrettyUtil {

    public static String formatByGSON(String uglyJSONString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

    public static String formatByJackson(Object jsonObject) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("formatByJackson ERR : " + e.getMessage(), e);
        }
    }

    public static String formatByNetSf(Object jsonObject) {
        return JSONUtils.valueToString(jsonObject, 8, 4);
    }

    public static String formatByJDK17(String jsonStringNoWhitespace) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine scriptEngine = manager.getEngineByName("JavaScript");
            scriptEngine.put("jsonString", jsonStringNoWhitespace);
            scriptEngine.eval("result = JSON.stringify(JSON.parse(jsonString), null, 2)");
            String prettyPrintedJson = (String) scriptEngine.get("result");
            return prettyPrintedJson;
        } catch (Exception e) {
            throw new RuntimeException("formatByJDK17 ERR : " + e.getMessage(), e);
        }
    }

    public static String formatByOrgJson(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);// Convert text to
                                                         // object
            return json.toString(4); // Print it with specified indentation
        } catch (Exception e) {
            throw new RuntimeException("formatByOrgJson ERR : " + e.getMessage(), e);
        }
    }
}