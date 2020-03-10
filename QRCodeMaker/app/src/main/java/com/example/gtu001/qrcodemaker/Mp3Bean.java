package com.example.gtu001.qrcodemaker;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Mp3Bean {
    String name;
    String url;
    String lastPosition;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLastPosition(String lastPosition) {
        this.lastPosition = lastPosition;
    }

    public String getLastPosition() {
        return lastPosition;
    }

    public int getLastPositionInt() {
        try {
            return Integer.parseInt(lastPosition);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static Mp3Bean valueOf(Map map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Mp3Bean bean = new Mp3Bean();
        bean.setName((String) map.get("name"));
        bean.setUrl((String) map.get("url"));
        bean.setLastPosition(StringUtils.defaultIfBlank((String) map.get("lastPosition"), "0"));
        return bean;
    }

    public Map<String, String> toMap() {
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("name", getName());
        rtnMap.put("url", getUrl());
        rtnMap.put("lastPosition", getLastPosition());
        return rtnMap;
    }
}