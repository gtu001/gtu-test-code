package com.iisigroup.ris;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RisConfigFinder {

    @Value("${system.id}")
    private String systemId;
    @Value("${prefix}")
    private String prefix;
    @Value("${globle.config.path}")
    private String globleConfigPath;
    @Value("${globle.share.path}")
    private String globleSharePath;
    @Value("${config.path}")
    private String configPath;
    @Value("${share.path}")
    private String sharepath;
    @Value("${data.path}")
    private String dataPath;
    @Value("${temp.path}")
    private String tempPath;

    Set<String> values;

    private Logger log = LoggerFactory.getLogger(getClass());

    Set<File> config = new HashSet<File>();

    File findFile;

    private static final String PROP_KEY = "ris3rl_prefix";

    public String getPrefix() {
        return findFile.getAbsolutePath();
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("com/iisigroup/ris/risConfigFinder_test.xml");
        System.out.println("done...");
    }

    public RisConfigFinder() throws Exception {
        log.debug("# RisConfigFinder ...start");
        File currentFile0 = new File("C:/workspace/backup/config");
        File currentFile1 = new File("").getAbsoluteFile();
        File currentFile2 = new File(this.getClass().getResource("").getFile());
        File currentFile3 = new File(this.getClass().getClassLoader().getResource("").getFile());
        searchFile(currentFile0);
        searchFile(currentFile1);
        searchFile(currentFile2);
        searchFile(currentFile3);
        log.debug("chk Set = " + config);
        List<File> list = new ArrayList<File>(config);
        Collections.sort(list);
        for (File f : list) {
            log.debug("iterator = " + f.getAbsolutePath());
        }
        if (list.size() == 0) {
            log.error("!!!找不到設定檔目錄 !!!");
            log.error("!!!找不到設定檔目錄 !!!");
            log.error("!!!找不到設定檔目錄 !!!");
            log.error("!!!找不到設定檔目錄 !!!");
            log.error("!!!找不到設定檔目錄 !!!");
            list.add(new File("").getAbsoluteFile());
            // throw new Exception("找不到設定檔目錄!!");
        }
        File findFile = list.get(0);
        log.debug("!! set properties : " + PROP_KEY + " == " + findFile);
        System.setProperty(PROP_KEY, findFile.getAbsolutePath());
        log.debug("# RisConfigFinder ...end");
    }

    void searchFile(File file) {
        if (!config.isEmpty()) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            if (file.listFiles().length == 4) {
                boolean config_Ok = false;
                boolean RIS_Ok = false;
                boolean share_Ok = false;
                boolean SP_Ok = false;
                for (File d : file.listFiles()) {
                    if (d.getName().equals("config") && d.isDirectory()) {
                        config_Ok = true;
                    }
                    if (d.getName().equals("RIS") && d.isDirectory()) {
                        RIS_Ok = true;
                    }
                    if (d.getName().equals("share") && d.isDirectory()) {
                        share_Ok = true;
                    }
                    if (d.getName().equals("SP") && d.isDirectory()) {
                        SP_Ok = true;
                    }
                }
                if (config_Ok && RIS_Ok && share_Ok && SP_Ok) {
                    config.add(file);
                }
            }
            for (File f : file.listFiles()) {
                searchFile(f);
            }
        }
    }

    public void showResult() {
        log.debug("# showResult ...");
        log.debug(toString());
        log.debug("!! set properties : " + PROP_KEY + " == " + findFile);
    }

    @Override
    public String toString() {
        return "RisConfigFinder [systemId=" + systemId + ", prefix=" + prefix + ", globleConfigPath=" + globleConfigPath + ", globleSharePath=" + globleSharePath + ", configPath=" + configPath
                + ", sharepath=" + sharepath + ", dataPath=" + dataPath + ", tempPath=" + tempPath + ", values=" + values + ", log=" + log + ", config=" + config + ", findFile=" + findFile + "]";
    }

    public void setValues(Set<String> values) {
        this.values = values;
    }
}
