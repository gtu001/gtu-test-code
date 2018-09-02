package com.gtu.example.common;

import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

public class HttpFormToBean {
    private String reqString;
    private Map<String, String> formMap = new LinkedHashMap<String, String>();

    public static HttpFormToBean newInstance(String reqString) {
        return new HttpFormToBean(reqString);
    }

    private HttpFormToBean(String reqString) {
        this.reqString = (StringUtils.trimToEmpty(reqString));
        this.load();
    }

    private void load() {
        String[] arrys = this.reqString.split("\\&", -1);
        for (String keyVal : arrys) {
            String[] kv = keyVal.split("\\=", -1);
            if (kv.length == 0) {
                continue;
            } else if (kv.length == 1) {
                formMap.put(kv[0], "");
                continue;
            }
            try {
                formMap.put(kv[0], StringUtils.trimToEmpty(URLEncoder.encode(kv[1], "UTF8")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void apply(Object bean) {
        try {
            BeanUtils.populate(bean, formMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getReqString() {
        return reqString;
    }

    public Map<String, String> getFormMap() {
        return formMap;
    }
}