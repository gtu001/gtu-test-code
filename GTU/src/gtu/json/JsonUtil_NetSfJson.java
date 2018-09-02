package gtu.json;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class JsonUtil_NetSfJson {

    public static JSONObject toJsonObject(Object obj) {
        JsonConfig config = new JsonConfig();

        // 加入此 annotation的 getter 會被忽略
        config.addIgnoreFieldAnnotation(javax.persistence.Transient.class);

        return JSONObject.fromObject(obj, config);
    }
}
