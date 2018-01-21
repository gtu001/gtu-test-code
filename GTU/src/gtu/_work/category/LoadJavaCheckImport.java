package gtu._work.category;

import gtu.file.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 掃目錄底下所有java檔有import哪些元件 整理成清單
 * 
 * @author Troy
 * 
 *         2012/1/4
 */
public class LoadJavaCheckImport {

    private Map<String, Set<File>> importMap;
    private Map<String, Set<String>> easyModeMappingMap;

    private String dirPath;
    private String exportFilePath;
    private String[] ignoreImports;
    private String exportFileToTarget;
    private String writeLog;
    private String easyModeLog;
    private BufferedWriter logWriter;
    private int totalCount;

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
//        LoadJavaCheckImport test = new LoadJavaCheckImport();
//        test.setDirPath("C:\\workspace\\ris3ape\\");
//        test.setExportFilePath(System.getProperty("user.home") + "\\Desktop\\Edit1.txt");
//        test.setIgnoreImports(new String[] { "com.iisigroup.ris", "java.io.Serializable", "org.slf4j.Logger",
//                "org.slf4j.LoggerFactory", "javax.faces.bean.SessionScoped", "javax.faces.bean.RequestScoped",
//                "javax.faces.bean.ManagedBean" });
//        // test.setExportFileToTarget(System.getProperty("user.home") +
//        // "\\Desktop\\Export\\"); // 若有資料則會出檔案
//        test.setWriteLog(System.getProperty("user.home") + "\\Desktop\\ExportLog.txt");
//        test.setEasyModeLog(System.getProperty("user.home") + "\\Desktop\\EasyModeLog.txt");
//        test.execute();
        LoadJavaCheckImport test = new LoadJavaCheckImport();
        test.setDirPath("C:/workspace/workspace_farEastStone/estore/fet_estore_search_engie_revamp/revamp_source_code");
        test.setExportFilePath(System.getProperty("user.home") + "\\Desktop\\Edit1.txt");
        test.setIgnoreImports(new String[] { });
        // test.setExportFileToTarget(System.getProperty("user.home") +
        // "\\Desktop\\Export\\"); // 若有資料則會出檔案
        test.setWriteLog(System.getProperty("user.home") + "\\Desktop\\ExportLog.txt");
        test.setEasyModeLog(System.getProperty("user.home") + "\\Desktop\\EasyModeLog.txt");
        test.execute();
        System.out.println("done...");
    }

    public void execute() throws IOException {
        importMap = new HashMap<String, Set<File>>();

        if (StringUtils.isNotBlank(writeLog)) {
            logWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeLog), "UTF8"));
        }

        loadCheckDir(new File(this.dirPath));
        
        System.out.println("總java數: " + totalCount);

        if (logWriter != null) {
            logWriter.close();
        }

        if (StringUtils.isNotBlank(this.easyModeLog)) {
            samePackageFilter();
            writeEasyModeMappingLog();
        }

        if (StringUtils.isBlank(exportFileToTarget)) {
            exportToLog();
        } else {
            exportFileToTarget();
        }
    }

    /**
     * 將同package底下的class放在一起
     */
    private void samePackageFilter() {
        Map<String, Set<File>> map = new HashMap<String, Set<File>>();
        Map<String, Set<String>> easyMode = new HashMap<String, Set<String>>();
        String key = null;
        String newKey = null;
        for (Iterator<String> it = importMap.keySet().iterator(); it.hasNext();) {
            key = it.next();
            newKey = key.substring(0, key.lastIndexOf("."));
            Set<File> fileset = importMap.get(key);
            for (Iterator<File> iit = fileset.iterator(); iit.hasNext();) {
                File file = iit.next();
                this.mapPut(newKey, file, map);
                this.mapPut(newKey, key, easyMode);
            }
        }
        importMap = map;
        easyModeMappingMap = easyMode;
    }

    private void writeEasyModeMappingLog() throws IOException {
        if (StringUtils.isEmpty(this.easyModeLog)) {
            return;
        }
        File file = new File(this.easyModeLog);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
        for (Iterator<String> it = this.easyModeMappingMap.keySet().iterator(); it.hasNext();) {
            String key = it.next();
            writer.write("[" + key + "]");
            writer.newLine();
            for (Iterator<String> iit = this.easyModeMappingMap.get(key).iterator(); iit.hasNext();) {
                String detail = iit.next();
                writer.write("\t" + detail);
                writer.newLine();
            }
        }
        writer.close();
    }

    private void exportFileToTarget() throws IOException {
        Set<String> keys = importMap.keySet();

        List<String> keyList = new ArrayList<String>(keys);
        Collections.sort(keyList);

        for (Object key : keyList) {
            String folder = (String) key;
            folder = folder.replace('*', '_');

            File dirFile = new File(exportFileToTarget + "\\" + folder);
            dirFile.mkdirs();

            for (File file : importMap.get((String) key)) {
                byte[] content = FileUtil.loadFromFile(file);
                FileUtil.saveToFile(dirFile.getAbsolutePath() + "\\" + file.getName(), content);
            }
        }
    }

    private void exportToLog() throws IOException {
        Set<String> keys = importMap.keySet();

        List<String> keyList = new ArrayList<String>(keys);
        Collections.sort(keyList);

        BufferedWriter writer = new BufferedWriter(new FileWriter(this.exportFilePath));
        for (Object key : keyList) {
            writer.write("[" + (String) key + "]");
            writer.newLine();

            for (File file : importMap.get((String) key)) {
                writer.write("\t\t" + file.getName());
                writer.newLine();
            }
        }
        writer.close();
    }

    protected void loadCheckDir(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                loadCheckDir(f);
            }
        } else {
            try {
                if (file.getName().toLowerCase().endsWith(".java")) {
                    readJavaFileImportPart(file);
                } else {
                    log("跳過檔案 : " + file.getName());

                }
            } catch (FileNotFoundException e) {
                log("找無檔案 : " + file.getAbsolutePath());
            } catch (IOException e) {
                log("讀檔錯誤 : " + file.getAbsolutePath());
            }
        }
    }

    private void log(String message) {
        try {
            if (logWriter != null) {
                this.logWriter.write(message);
                this.logWriter.newLine();
            }
        } catch (Exception ex) {
        }
        System.out.println(message);
    }

    private void readJavaFileImportPart(File file) throws IOException {
        totalCount ++;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;

        Pattern pattern = Pattern.compile("(public{1})(\\s{1})(class{1})(\\s{1})(\\w+)(.+)(\\{{1})");

        if (this.ignoreImports == null) {
            this.ignoreImports = new String[] {};
        }

        boolean patternMatch = false;
        boolean matchImport = false;
        while ((line = reader.readLine()) != null) {

            if (line.matches("import\\s.+;")) {
                matchImport = true;
                line = line.replaceAll("import", "");
                line = line.replaceAll(";", "");

                boolean notIgnore = StringUtils.indexOfAny(line, this.ignoreImports) == -1;
                if (notIgnore) {
                    this.mapPut(line.trim(), file, importMap);
                } else {
                    log("忽略import = " + line);
                }
            }
            if (pattern.matcher(line).matches()) {
                patternMatch = true;
                break;
            }
        }

        if (!matchImport) {
            log("無import = " + file.getName());
        }

        if (!patternMatch) {
            log("不符合pattern = " + file.getName());
        }

        reader.close();
    }

    private <V, S extends Set<V>> void mapPut(String key, V value, Map<String, Set<V>> map) {
        Set<V> set = new HashSet<V>();
        if (map.containsKey(key)) {
            set = map.get(key);
        }
        set.add(value);
        map.put(key, set);
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getExportFilePath() {
        return exportFilePath;
    }

    public void setExportFilePath(String exportFilePath) {
        this.exportFilePath = exportFilePath;
    }

    public String[] getIgnoreImports() {
        return ignoreImports;
    }

    public void setIgnoreImports(String[] ignoreImports) {
        this.ignoreImports = ignoreImports;
    }

    public String getExportFileToTarget() {
        return exportFileToTarget;
    }

    public void setExportFileToTarget(String exportFileToTarget) {
        this.exportFileToTarget = exportFileToTarget;
    }

    public String getWriteLog() {
        return writeLog;
    }

    public void setWriteLog(String writeLog) {
        this.writeLog = writeLog;
    }

    public String getEasyModeLog() {
        return easyModeLog;
    }

    public void setEasyModeLog(String easyModeLog) {
        this.easyModeLog = easyModeLog;
    }

    public Map<String, Set<File>> getImportMap() {
        return importMap;
    }

    public void setImportMap(Map<String, Set<File>> importMap) {
        this.importMap = importMap;
    }

    public Map<String, Set<String>> getEasyModeMappingMap() {
        return easyModeMappingMap;
    }

    public void setEasyModeMappingMap(Map<String, Set<String>> easyModeMappingMap) {
        this.easyModeMappingMap = easyModeMappingMap;
    }
}
