package gtu.xml.work;

import gtu.file.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;

public abstract class BaseReadXmlToSQL {

    protected static final Logic DEFALUT_LOGIC = new Logic();

    protected BufferedWriter writer;
    protected String tableName = "error_table_name";
    protected String dmvValue = "error_dmv_value";
    protected static String[] destColumns;
    protected static String[] intColumns;
    protected static String[] booleanColumns;

    protected static Map<String, Logic> map = new LinkedHashMap<String, Logic>();

    protected abstract Map<String, String> afterHandleRow(Map<String, String> newRows);

    protected File getDestFile(String dmvValue, File srcFile, String fileName) {
        if (!fileName.toLowerCase().endsWith(".sql")) {
            fileName = fileName + ".sql";
        }
        String newFileName = dmvValue + "_" + fileName;

        File mkdir = new File(FileUtil.DESKTOP_DIR, "CAC前代DB");
        mkdir.mkdirs();
        File outputFile = new File(mkdir, newFileName);
        return outputFile;
    }

    protected void processSql(File srcFile, File outputFile) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));

            final Count count = new Count();
            SAXReader reader = new SAXReader();
            reader.setDefaultHandler(new ElementHandler() {
                @Override
                public void onEnd(ElementPath arg0) {
                    Element e = arg0.getCurrent();
                    if (e.getName().equals("ROW")) {
                        Map<String, String> rows = new LinkedHashMap<String, String>();
                        List<Element> elist = (List<Element>) e.selectNodes("*");
                        for (Element e2 : elist) {
                            rows.put(e2.getName(), e2.getText());
                        }
                        handleRow(rows);
                        count.addOne();
                        e.detach();
                    }
                }

                @Override
                public void onStart(ElementPath arg0) {
                }
            });
            File tmpFile = createCleanXmlFile(srcFile);
            System.out.println("tmpFile:" + tmpFile);
            reader.read(tmpFile);

            writer.flush();
            writer.close();
            System.out.println("產生:" + count.value + "筆");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    protected void handleRow(Map<String, String> rows) {
        String value = null;
        String transferValue = null;
        Logic logic = null;
        Map<String, String> newRows = new LinkedHashMap<String, String>();
        for (String col : rows.keySet()) {
            value = rows.get(col);
            logic = DEFALUT_LOGIC;
            if (map.containsKey(col)) {
                logic = map.get(col);
            }
            transferValue = logic.getValue(value);
            newRows.put(logic.getColumn(col), transferValue);
        }
        newRows = this.afterHandleRow(newRows);
        for (String needCol : destColumns) {
            if (!newRows.containsKey(needCol)) {
                newRows.put(needCol, null);
            }
        }
        for (String col : new HashSet<String>(newRows.keySet())) {
            if (StringUtils.indexOfAny(col, destColumns) == -1) {
                newRows.remove(col);
            }
        }
//        System.out.println(newRows);

        String insertSql = this.createInsertSql(tableName, newRows);
        System.out.println(insertSql);
        try {
            writer.write(insertSql);
            writer.newLine();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected File createCleanXmlFile(File xmlFile) {
        try {
            File outputFile = File.createTempFile("TMP_", xmlFile.getName());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf8"));
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(xmlFile), "utf8"));
            for (String line = null; (line = reader.readLine()) != null;) {
                line = line.replaceAll("[^\\x09\\x0A\\x0D\\x20-\\uD7FF\\uE000-\\uFFFD\\u10000-\\u10FFFF]", "");
                writer.write(line);
                writer.newLine();
            }
            reader.close();
            writer.flush();
            writer.close();
            return outputFile;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    static class Logic {
        protected String getColumn(String column) {
            StringBuilder sb = new StringBuilder();
            char[] cc = column.toCharArray();
            for (int ii = 0; ii < cc.length; ii++) {
                char c = cc[ii];
                if (ii == 0) {
                    sb.append(Character.toLowerCase(c));
                } else if (Character.isUpperCase(c)) {
                    sb.append("_" + Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }

        protected String getValue(String value) {
            if ("(null)".equals(value)) {
                return null;
            }
            return value;
        }
    }

    protected String createInsertSql(String tableName, Map<String, String> valmap) {
        StringBuilder sb = new StringBuilder();
        String value = null;
        sb.append(String.format("INSERT INTO %s  (", tableName));
        for (String key : valmap.keySet()) {
            sb.append(key + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") VALUES ( ");
        for (String key : valmap.keySet()) {
            value = valmap.get(key);
            sb.append(this.getRealValue(key, value) + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("); ");
        return sb.toString();
    }

    private String getRealValue(String key, String value) {
        if (value == null) {
            System.out.println("\t" + key + "\t" + "null");
            return "null";
        } else if (StringUtils.indexOfAny(key, booleanColumns) != -1) {
            if ("0".equals(value)) {
                System.out.println("\t" + key + "\t" + "false");
                return "false";
            } else if ("1".equals(value)) {
                System.out.println("\t" + key + "\t" + "true");
                return "true";
            }
            return "false";
        } else if (StringUtils.indexOfAny(key, intColumns) != -1) {
            System.out.println("\t" + key + "\t" + value);
            return value;
        }
        System.out.println("\t" + key + "\t" + String.format("'%s'", value));
        return String.format("'%s'", value);
    }

    static class Count {
        long value;

        void addOne() {
            value++;
        }
    }
}
