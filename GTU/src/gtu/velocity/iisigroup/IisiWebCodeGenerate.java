package gtu.velocity.iisigroup;

import gtu.file.FileUtil;
import gtu.velocity.iisigroup.XhtmlMaker.Xhtml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class IisiWebCodeGenerate {

    private static final String SRC_DIR = "C:/Users/gtu001/Desktop/workspace/GTU/src/gtu/velocity/iisigroup/";//不要動

    static String basePathReferenceId;

    static String getBasePath() {
        return FileUtil.DESKTOP_PATH + "\\iisi_code_" + basePathReferenceId + "\\";
    }

    private static final String BASE_ID = "Rl04240";//不要動
    private static final String MAVEN_PROJ = "sris-rl";

    public static void main(String[] args) {
        IisiWebCodeGenerate test = new IisiWebCodeGenerate();

        Map<String, Object> map = new HashMap<String, Object>();

        final String domain = "rl";//rl
        final String lowerId = "rl0k400";//rl04110
        final String upperId = "Rl0k400";//Rl04110
        final String pkgName = "rl0k400";//rl04100
        String chnName = "外籍(含大陸)配偶資料查詢";

        //        test.removeAllFile(domain, pkgName, upperId);//刪除建立的檔案

        map.put("chineseName", chnName);
        map.put("chineseShortName", chnName);
        map.put("yourName", "");

        map.put("PACKAGE", pkgName);
        map.put("DOMAIN", domain);
        map.put("LOWER_ID", lowerId);
        map.put("UPPER_ID", upperId);

        map.put("CONTROLL_METHODS", Arrays.asList("print"));
        map.put("SERVICE_METHODS", ImmutableMap.builder()//
                .put("doPrint", "byte[]")//
                .put("query", "List<?>")//
                .build());

        test.addMapFieldInfoList(map, ImmutableList.<FieldInfo> builder()//
                .add(new FieldInfo("reportType", "String", "報表格式", Xhtml.REPORT_REPORT_TYPE))//
                .add(new FieldInfo("dataYear", "String", "資料日期(年)", Xhtml.INPUT_YEAR_MONTH))//
                .add(new FieldInfo("dataMonth", "String", "資料日期(月)", Xhtml.SINGLE_CALENDAR))//
                .build());

        basePathReferenceId = lowerId;//不要動

        List<String> newStuff = new ArrayList<String>();
        for (Source s : Source.values()) {
            if (s == Source.NEW_STUFF) {
                continue;
            }
            String fileName = s.getTargetFileName(upperId);
            if (fileName.endsWith(".java")) {
                newStuff.add(fileName.replaceAll(".java", ".class"));
            }
            if (fileName.endsWith(".xhtml")) {
                newStuff.add("// " + fileName);
            }
        }
        map.put("NEW_STUFF", newStuff);
        map.put("XHTML_URL", String.format("<li><a href=\"#{request.contextPath}/faces/pages/tmp/%s/%s\">%s</a></li>", domain.toUpperCase(), Source.XHTML.getTargetFileName(upperId), Source.XHTML
                .getTargetFileName(upperId).replaceAll(".xhtml", "")));

        Map<Source, String> userDefineName = new HashMap<Source, String>();
        // userDefineName.put(Source.XHTML, "xxxxx.xhtml");

        test.execute(map, userDefineName);
        //        test.removeAllFile(domain, pkgName, upperId);

        System.out.println("done...");
    }

    void validate(Map<String, Object> map) {
        Validate.notNull(map.get("PACKAGE"));
        Validate.notNull(map.get("DOMAIN"));
        Validate.notNull(map.get("LOWER_ID"));
        Validate.notNull(map.get("UPPER_ID"));
        Validate.notNull(map.get("chineseName"));
        Validate.notNull(map.get("chineseShortName"));
        Validate.notNull(map.get("yourName"));
        Validate.notNull(map.get("CONTROLL_METHODS"));
        Validate.notNull(map.get("SERVICE_METHODS"));
        Validate.notNull(map.get("DTO_FIELDS"));
        Validate.notNull(map.get("COLUMN"));
        Validate.notNull(map.get("COLUMNLIST"));
        Validate.notNull(map.get("DATATABLE"));
        Validate.notNull(map.get("NEW_STUFF"));
        Validate.notNull(map.get("XHTML_URL"));
        Validate.isTrue(((List<?>) map.get("COLUMNLIST")).size() == ((List<?>) map.get("COLUMN")).size());
    }

    public void execute(Map<String, Object> map, Map<Source, String> userDefineName) {
        validate(map);

        String pkgName = (String) map.get("PACKAGE");
        String domain = (String) map.get("DOMAIN");
        String lowerId = (String) map.get("LOWER_ID");
        String upperId = (String) map.get("UPPER_ID");

        if (userDefineName == null) {
            userDefineName = new HashMap<Source, String>();
        }

        String destDir = null;
        String destPathName = null;
        for (Source s : Source.values()) {
            if (!s.allowCreate) {
                continue;
            }

            destDir = s.getTagetDir(domain, pkgName, lowerId);
            destPathName = destDir + StringUtils.defaultString(userDefineName.get(s), s.getTargetFileName(upperId));

            checkDir(destDir);
            generateFile(SRC_DIR + s.fileName, destPathName, map, s);
        }
    }

    static class XhtmlDataTable {
        List<List<String>> list = new ArrayList<List<String>>();

        XhtmlDataTable add(String title, String field) {
            list.add(Arrays.asList(title, field));
            return this;
        }

        List<List<String>> get() {
            return list;
        }
    }

    List<String> xhtmlColumnList(Xhtml... column) {
        List<String> list = new ArrayList<String>();
        for (int ii = 0; ii < column.length; ii++) {
            list.add(column[ii].tag.replaceAll("#INDEX#", String.valueOf(ii)));
        }
        return list;
    }

    // xxxxxxxxxxxxxxxxxxxxxxxxxx
    // ## map 用法
    // map.put("TESTLIST", ImmutableMap.builder().put("aaa", "AAA").put("bbb",
    // "BBB").build());
    // ${TESTLIST.get(\"aaa\")} , ${TESTLIST.get(\"bbb\")}
    // xxxxxxxxxxxxxxxxxxxxxxxxxx
    // ## list 用法
    // map.put("TESTLIST", Arrays.asList("AAA", "BBB");
    // ${TESTLIST.get(0)} , ${TESTLIST.get(1)}
    // xxxxxxxxxxxxxxxxxxxxxxxxxx

    enum Source {
        NEW_STUFF("NewStuff.java", //
                getBasePath() + "\\" + MAVEN_PROJ + "-web/src_new_stuff/", 1, false) {
            String getTagetDir(String domain, String packageName, String lowerId) {
                return targetPath;
            }

            String getTargetFileName(String upperId) {
                return "NewStuff_" + upperId + ".java";
            }
        },
        CONTROLLER("Rl04240Controller.java", //
                getBasePath() + "\\" + MAVEN_PROJ + "-web\\src\\main\\java\\tw\\gov\\moi\\%s\\%s\\web\\", 1, true) {
        },
        DTO("Rl04240DTO.java", //
                getBasePath() + "\\" + MAVEN_PROJ + "-api\\src\\main\\java\\tw\\gov\\moi\\%s\\domain\\", 1, true) {
            String getTagetDir(String domain, String packageName, String lowerId) {
                return String.format(targetPath, domain);
            }
        },
        SERVICE("Rl04240Service.java", //
                getBasePath() + "\\" + MAVEN_PROJ + "-api\\src\\main\\java\\tw\\gov\\moi\\%s\\%s\\service\\", 1, true) {
        },
        SERVICE_IMPL("Rl04240ServiceImpl.java", //
                getBasePath() + "\\" + MAVEN_PROJ + "-core\\src\\main\\java\\tw\\gov\\moi\\%s\\%s\\service\\", 1, true) {
        },
        XHTML("rl04240.xhtml", //
                getBasePath() + "\\" + MAVEN_PROJ + "-web\\src\\main\\webapp\\pages\\func\\%s\\", 2, true) {
            String getTagetDir(String domain, String packageName, String lowerId) {
                return String.format(targetPath, lowerId);
            }

            String getTargetFileName(String upperId) {
                return fileName.replaceFirst(BASE_ID.toLowerCase(), upperId.toLowerCase());
            }
        },
        ;

        final String fileName;
        final String targetPath;
        final int margeTime;
        final boolean allowCreate;

        Source(String fileName, String targetPath, int margeTime, boolean allowCreate) {
            this.fileName = fileName;
            this.targetPath = targetPath;
            this.margeTime = margeTime;
            this.allowCreate = allowCreate;
        }

        String getTagetDir(String domain, String packageName, String lowerId) {
            return String.format(targetPath, domain, packageName);
        }

        String getTargetFileName(String upperId) {
            return fileName.replaceFirst(BASE_ID, upperId);
        }
    }

    public void generateFile(String srcPathName, String destPathName, Map<String, Object> map, Source srcType) {
        try {
            // initial Velocity Properties
            Properties props = new Properties();
            props.setProperty("resource.loader", "string");
            props.setProperty("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
            props.setProperty("string.resource.loader.repository.class", "org.apache.velocity.runtime.resource.util.StringResourceRepositoryImpl");

            Velocity.init(props);
            StringResourceRepository vsRepository = StringResourceLoader.getRepository();

            VelocityContext context = new VelocityContext();
            for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                context.put(key, map.get(key));
            }

            String templateBody = loadFromFile(srcPathName);

            StringWriter swriter = new StringWriter();
            swriter.write(templateBody);

            for (int ii = 0; ii < srcType.margeTime; ii++) {
                vsRepository.putStringResource("aabbcc", swriter.getBuffer().toString());
                Template template = Velocity.getTemplate("aabbcc", "UTF-8");
                swriter = new StringWriter();
                template.merge(context, swriter);
            }

            System.out.println("create = " + destPathName);

            saveToFile(destPathName, swriter.getBuffer().toString().getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void checkDir(String dirPath) {
        // System.out.println("dirPath = " + dirPath);
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    String loadFromFile(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\r\n");
        }
        reader.close();
        return sb.toString();
    }

    void saveToFile(String fileName, byte[] data) {
        try {
            java.io.FileOutputStream out = new java.io.FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    static class FieldInfo {
        String fieldName1;
        String fieldType1;
        String fieldChn1;

        String fieldName2;
        String fieldType2;
        String fieldChn2;

        Xhtml xhtml;

        public FieldInfo(String fieldName, String fieldType, String fieldChn, String fieldName1, String fieldType1, String fieldChn1, Xhtml xhtml) {
            super();
            this.fieldName1 = fieldName;
            this.fieldType1 = fieldType;
            this.fieldChn1 = fieldChn;
            fieldName2 = fieldName1;
            fieldType2 = fieldType1;
            fieldChn2 = fieldChn1;
            this.xhtml = xhtml;
        }

        public FieldInfo(String fieldName, String fieldType, String fieldChn, Xhtml xhtml) {
            super();
            this.fieldName1 = fieldName;
            this.fieldType1 = fieldType;
            this.fieldChn1 = fieldChn;
            this.xhtml = xhtml;
        }
    }

    void addMapFieldInfoList(Map<String, Object> map, List<FieldInfo> list) {
        List<List<String>> alist = new ArrayList<List<String>>();
        List<Map<String, String>> blist = new ArrayList<Map<String, String>>();
        List<String> clist = new ArrayList<String>();
        List<List<String>> dlist = new ArrayList<List<String>>();

        int index = 0;
        for (FieldInfo f : list) {
            alist.add(Arrays.asList(f.fieldName1, f.fieldType1, f.fieldChn1));
            if (StringUtils.isNotEmpty(f.fieldName2) && StringUtils.isNotEmpty(f.fieldChn2) && StringUtils.isNotEmpty(f.fieldType2)) {
                alist.add(Arrays.asList(f.fieldName2, f.fieldType2, f.fieldChn2));
            }

            f.fieldChn2 = StringUtils.defaultString(f.fieldChn2);
            f.fieldName2 = StringUtils.defaultString(f.fieldName2);

            blist.add(ImmutableMap.<String, String> builder()//
                    .put("TITLE1", f.fieldChn1).put("TITLE2", f.fieldChn2)//
                    .put("FIELD1", f.fieldName1).put("FIELD2", f.fieldName2)//
                    .build());

            clist.add(f.xhtml.tag.replaceAll("#INDEX#", String.valueOf(index)));

            dlist.add(Arrays.asList(f.fieldChn1, f.fieldName1));
            if (StringUtils.isNotEmpty(f.fieldName2) && StringUtils.isNotEmpty(f.fieldChn2)) {
                dlist.add(Arrays.asList(f.fieldChn2, f.fieldName2));
            }
            index++;
        }

        map.put("DTO_FIELDS", alist);
        map.put("COLUMN", blist);
        map.put("COLUMNLIST", clist);
        map.put("DATATABLE", dlist);

        Validate.isTrue(((List<?>) map.get("COLUMNLIST")).size() == ((List<?>) map.get("COLUMN")).size());
    }
}
