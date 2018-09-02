package gtu._work;

import gtu.collection.MapUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.DateFormatUtils;

public class JarFinder {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        File file = new File("C:/jar");
//        file = new File("C:/Program Files/Java/activemq-5.10/lib");
//        file = new File("C:/Users/gtu001/.m2/repository");
//        file = new File("C:/Oracle/Middleware/user_projects/domains/base_domain/lib");
        Map<String, Collection<String>> maps = JarFinder.newInstance().setDir(file)//
                .pattern("org/springframework/dao/DataAccessResourceFailureException").execute();
        for (String key : maps.keySet()) {
            File f = new File(key);
            System.out.println(key + "\t" + DateFormatUtils.format(f.lastModified(), "yyyyMMdd") + "\t" + f.length());
            for (String val : maps.get(key)) {
                System.out.println("\t-->" + val);
            }
        }
        
        List<File> list = new ArrayList<File>();
        for (String key : maps.keySet()) {
            File f = new File(key);
            list.add(f);
        }
        Collections.sort(list, new Comparator<File>() {
            public int compare(File o1, File o2) {
                return new Long(o1.lastModified()).compareTo(o2.lastModified());
            }
        });
        System.out.println("==========================================================");
        for (File f : list) {
            System.out.println(f + "\t" + DateFormatUtils.format(f.lastModified(), "yyyyMMdd") + "\t" + f.length());
        }
        
        Runtime.getRuntime().exec(String.format("cmd call /c \"%s\"", "D:/_桌面/apps/jd-gui-0.3.1.windows/jd-gui.exe"));
        System.out.println("done...");
    }

    private Pattern pattern;
    private Matcher matcher;
    private File dir;
    private List<File> fileLst;
    private IfMatch ifMatch;
    private CurrentSearch currentSearch;

    private Map<String, Collection<String>> matchMap;

    public static JarFinder newInstance() {
        return new JarFinder();
    }

    private JarFinder() {
        matchMap = new HashMap<String, Collection<String>>();
    }

    public JarFinder ifMatchEvent(IfMatch ifMatch) {
        this.ifMatch = ifMatch;
        return this;
    }

    public JarFinder currentSearchEvent(CurrentSearch currentSearch) {
        this.currentSearch = currentSearch;
        return this;
    }

    public JarFinder pattern(String pattern) {
        this.pattern = Pattern.compile(pattern);
        return this;
    }

    public Map<String, Collection<String>> getMap() {
        return matchMap;
    }

    public JarFinder setDir(File file) {
        this.dir = file;
        return this;
    }
    
    public JarFinder setFileLst(List<File> fileLst) {
        this.fileLst = fileLst;
        return this;
    }

    public JarFinder clear() {
        pattern = null;
        matchMap.clear();
        return this;
    }

    public Map<String, Collection<String>> execute() {
        Validate.notNull(pattern);
        if(dir == null && fileLst == null) {
            throw new RuntimeException("必須輸入dir或fileLst");
        }
        if(dir != null && dir.exists()) {
            find(this.dir);
        }
        if(fileLst != null && !fileLst.isEmpty()) {
            find(this.fileLst);
        }
        return matchMap;
    }

    private void find(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                this.find(f);
            }
        } else {
            if (file.getName().endsWith(".jar")
                    && StringUtils.indexOfAny(file.getName(), new String[] { "sources.jar", "javadoc.jar" }) == -1) {
                findZip(file.getAbsolutePath());
            }
        }
    }
    
    private void find(List<File> fileLst) {
        for(File f : fileLst) {
            findZip(f.getAbsolutePath());
        }
    }

    /**
     * 若找到符合的會執行
     */
    public interface IfMatch {
        void apply(String matchJarFilePath, String matchClassPath, Map<String, Collection<String>> matchMap);
    }

    /**
     * 顯示尋找中的檔案
     */
    public interface CurrentSearch {
        void apply(String filename);
    }

    private boolean findZip(String filename) {
        if (currentSearch != null) {
            currentSearch.apply(filename);
        }
        boolean done = false;
        ZipFile zf = null;
        try {
            // System.out.println(filename);
            zf = new ZipFile(filename);
            ZipEntry target = null;
            Enumeration<?> enumeration = zf.entries();
            while (enumeration.hasMoreElements()) {
                try{
                    target = (ZipEntry) enumeration.nextElement();
                }catch(Exception ex){
                    if(!ex.getMessage().contains("MALFORMED")){
                        ex.printStackTrace();
                        throw ex;
                    }
                }
                // System.out.println("target.name = " + target.getName());
                matcher = this.pattern.matcher(target.getName());
                if (matcher.find()) {
                    MapUtil.putAsCollection(filename, target.getName(), matchMap);
                    if (ifMatch != null) {
                        ifMatch.apply(filename, target.getName(), matchMap);
                    }
                    // System.out.println(filename + "\t" + target.getName());
                }
            }
            done = true;
        } catch (java.util.zip.ZipException e){
            if(e.getMessage().contains("invalid CEN header (bad signature)")){
                System.err.println("invalid CEN header (bad signature) , fileName : " + filename);
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                zf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return done;
    }
}
