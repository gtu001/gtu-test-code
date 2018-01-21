package com.iisigroup.ris;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

    static Properties prop = new Properties();

    static String fileName = "config.properties";
    static File config = new File("C:/Users/Troy/Desktop/" + fileName);

    static Logger log = LoggerFactory.getLogger(Config.class);

    static final String ECLIPSE_HOME;
    static final String ECLIPSE_COMPANY;
    static final String JD_EXE;
    static final String NOTE_PAD;
    static final String[] SRC_DIR;
    static final String[] IGNORE_FILE_ENDS_WITH;
    static final long RECENT_TIME;
    static final String SVN_ACCOUNT;

    static {
        try {
            if (config.exists()) {
                prop.load(new FileInputStream(config));
            } else {
                prop.load(Config.class.getResource(fileName).openStream());
            }
        } catch (Exception ex) {
            log.error("config init error!", ex);
        }

        ECLIPSE_HOME = getValue("eclipse_home");
        ECLIPSE_COMPANY = getValue("eclipse_company");
        JD_EXE = getValue("jd");
        NOTE_PAD = getValue("note_pad");
        IGNORE_FILE_ENDS_WITH = getValue("ignore_file_ends_with").split(",");
        RECENT_TIME = getRecentTime();
        SVN_ACCOUNT = getValue("svn_account");
        
        Set<String> srcDir = new HashSet<String>();
        for (Object key : prop.keySet()) {
            if (key.toString().matches("src_dir.*")) {
                srcDir.add(getValue((String) key));
            }
        }
        SRC_DIR = (String[]) srcDir.toArray(new String[srcDir.size()]);
    }
    
    static long getRecentTime(){
        long recentDiff = Long.parseLong(StringUtils.defaultIfEmpty(prop.getProperty("recent_time"), "-1"));
        if(recentDiff == -1){
            recentDiff = (24 * 60 * 60 * 1000) * 3; //預設三天內算近期檔案
        }
        return recentDiff;
    }

    static String getValue(String key) {
        if (prop.containsKey(key)) {
            return StringUtils.defaultString(prop.getProperty(key), "");
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(ECLIPSE_HOME);
        System.out.println(ECLIPSE_COMPANY);
        System.out.println(JD_EXE);
        System.out.println(NOTE_PAD);
        System.out.println(Arrays.toString(SRC_DIR));
        System.out.println("done...");
    }
}
