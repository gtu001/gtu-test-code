package gtu._work;

import gtu.collection.MapUtil;
import gtu.file.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * 一行一行讀取 先讀key 再讀value 讀到下個key 塞前個key的值
 * 
 * @author Troy
 */
public class PMDReportTextChecker {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        File file = new File(FileUtil.DESKTOP_PATH + "Edit1.txt");
        File file2 = new File(FileUtil.DESKTOP_PATH + "RC_result.txt");

        PMDReportTextChecker tt = new PMDReportTextChecker();

        tt.read(file);
        tt.category();
        tt.write(file2);

        System.out.println("done...");
    }

    List<Violation> array = new ArrayList<Violation>();
    List<Integer> errorArray = new ArrayList<Integer>();
    Map<Map<String, Object>, List<Violation>> categoryMap;

    public void read(File file) throws Exception {
        Field[] fields = Violation.class.getDeclaredFields();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));

        String line = null;

        StringBuilder content = new StringBuilder();
        Map<String, String> contextMap = new HashMap<String, String>();
        String previousField = null;

        while ((line = reader.readLine()) != null) {
            System.out.println("# readLine = " + line);

            line = line.trim();

            String currentField = findMatchField(line, fields);

            // 找到新的field
            if (currentField != null) {
                // 塞值
                if (previousField != null) {
                    contextMap = setFieldContext(previousField, content, contextMap);
                    content = new StringBuilder();
                }
                previousField = currentField;
            } else {
                appendLine(line, content);
            }
        }
        this.tailFlush(previousField, content, contextMap);

        reader.close();
    }

    /**
     * 將掃檔案的結尾有剩的也加上去 , 否則會少資料
     */
    private void tailFlush(String previousField, StringBuilder content, Map<String, String> contextMap)
            throws SecurityException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        if (content.length() != 0 && previousField != null) {
            setFieldContext(previousField, content, contextMap);
        }
        if (contextMap.size() != 0) {
            addBeanToArray(contextMap);
        }
    }

    private Map<String, String> setFieldContext(String field, StringBuilder context, Map<String, String> contextMap)
            throws SecurityException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        if (contextMap.containsKey(field)) {
            // 重複塞到一樣的欄位表示要建立一筆新的
            addBeanToArray(contextMap);
            contextMap = new HashMap<String, String>();
        }
        contextMap.put(field, context.toString());
        System.out.println("\t" + contextMap.size());
        return contextMap;
    }

    private void addBeanToArray(Map<String, String> contextMap) throws SecurityException, InstantiationException,
            IllegalAccessException, NoSuchFieldException {
        Violation vio = MapUtil.mapToBean(contextMap, Violation.class);
        System.out.println("建立新筆 : " + vio);
        vio.validate();
        array.add(vio);
    }

    private String findMatchField(String line, Field[] fields) {
        for (Field field : fields) {
            Content content = field.getAnnotation(Content.class);
            if (content == null) {
                continue;
            }
            if (line.equals(content.label())) {
                return field.getName();
            }
        }
        return null;
    }

    private void appendLine(String line, StringBuilder sb) {
        line = line.trim();
        if (sb.length() == 0) {
            sb.append(line);
        } else {
            sb.append("\r\n" + line);
        }
    }

    public void category() throws Exception {
        // 以method getClz 的值分類
        categoryMap = MapUtil.categoryMapByGetter(array, "getClz");
    }

    public void write(File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));

        final String[] ignore = new String[] { "com.iisigroup.ris.ae", "com.iisigroup.ris.util",
                "com.iisigroup.ris.common", "com.iisigroup.ris.exception" };

        for (Map.Entry<Map<String, Object>, List<Violation>> entry : categoryMap.entrySet()) {

            String clzName = entry.getKey().get("getClz").toString();
            if (StringUtils.indexOfAny(clzName, ignore) != -1) {
                continue;
            }

            writer.write(clzName);
            writer.newLine();
            int index = 0;

            List<Violation> list = entry.getValue();
            Collections.sort(list);
            for (Violation vio : list) {
                writer.write("\t\t ##" + index + "## - " + vio.toString());
                writer.newLine();
                index++;
            }
            writer.newLine();
        }

        writer.newLine();
        writer.newLine();
        writer.write("size = " + categoryMap.size());

        writer.close();
    }

    //
    // SOURCE: annotation retained only in the source file and is discarded
    // during compilation.
    // CLASS: annotation stored in the .class file during compilation, not
    // available in the run time.
    // RUNTIME: annotation stored in the .class file and available in the run
    // time.
    //
    // CLASS: Annotations are to be recorded in the class file by the compiler
    // but need not be retained by the VM at run time.
    // RUNTIME: Annotations are to be recorded in the class file by the compiler
    // and retained by the VM at run time, so they may be read reflectively.
    // SOURCE: Annotations are to be discarded by the compiler.
    //
    // SOURCE, // 編譯器處理完Annotation資訊後就沒事了
    // CLASS, // 編譯器將Annotation儲存於class檔中，預設
    // RUNTIME // 編譯器將Annotation儲存於class檔中，可由VM讀入

    @Target(value = { ElementType.FIELD })
    @Retention(value = RetentionPolicy.RUNTIME)
    @interface Content {
        String label() default "";
    }

    public static class Violation implements Comparable<Violation> {
        @Content(label = "Class")
        String clz;
        @Content(label = "File Name")
        String fileName;
        @Content(label = "Line No")
        String lineNo;
        @Content(label = "Violation")
        String violation;
        @Content(label = "Note")
        String note;
        @Content(label = "Severity")
        String severity;
        @Content(label = "Category")
        String category;

        int lineNoNum;

        boolean validateFailed = false;

        void validate() {
            try {
                Validate.noNullElements(new String[] { clz, fileName, lineNo, violation, note, severity, category });
                lineNoNum = Integer.parseInt(lineNo);
                clz = chkToLong(clz);
                fileName = chkToLong(fileName);
                lineNo = chkToLong(lineNo);
                violation = chkToLong(violation);
                note = chkToLong(note);
                severity = chkToLong(severity);
                category = chkToLong(category);
            } catch (Exception ex) {
                System.err.println(toString());
                validateFailed = true;
            }
        }

        private String chkToLong(String str) {
            if (StringUtils.isNotBlank(str) && str.length() > 300) {
                return "(此欄位太長)" + str.substring(0, 300) + "(後面截掉)";
            }
            return str;
        }

        @Override
        public String toString() {
            return "[lineNo=" + lineNo + ", violation=" + violation + ", note=" + note + ", severity=" + severity
                    + ", category=" + category + "]";
        }

        public String getClz() {
            return clz;
        }

        public void setClz(String clz) {
            this.clz = clz;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getLineNo() {
            return lineNo;
        }

        public void setLineNo(String lineNo) {
            this.lineNo = lineNo;
        }

        public String getViolation() {
            return violation;
        }

        public void setViolation(String violation) {
            this.violation = violation;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int compareTo(Violation paramT) {
            if (this.lineNoNum > paramT.lineNoNum) {
                return 1;
            } else if (this.lineNoNum < paramT.lineNoNum) {
                return -1;
            }
            return 0;
        }
    }
}
