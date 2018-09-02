package gtu.freemarker.work03;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import freemarker.template.TemplateException;
import gtu.console.SystemInUtil;
import gtu.file.FileUtil;
import gtu.freemarker.FreeMarkerSimpleUtil;

public class HpMethodCreater {
    
    public static void main(String[] args) throws IOException, TemplateException {
        HpMethodCreater test = new HpMethodCreater();
        test.execute();
    }
    
    public void execute() throws IOException, TemplateException{
        String content = SystemInUtil.readContent();
        
        Map<String,Object> root = new HashMap<String,Object>();
        root.put("sqlList", getSqlList(content));
        root.put("paramList", getParamList(content));
        root.put("sql", content);
        
//        InputStream is = this.getClass().getResource("otherMethod.txt").openStream();
//        String template = FileUtil.loadFromStream(is, "utf8");
        
        String template = FileUtil.loadFromFile(new File("D:/workstuff/workspace/gtu-test-code/GTU/src/gtu/freemarker/work03/otherMethod.txt"), "utf8");
        String result = FreeMarkerSimpleUtil.replace(template, root);
        
        System.out.println(result);
    }
    
    private List<String> getSqlList(String content){
        List<String> list = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new StringReader(content));
            for(String line = null; (line = reader.readLine())!= null;){
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    private Set<String> getParamList(String sql){
        Pattern ptn = Pattern.compile("\\:(\\w+)");
        Matcher mth = ptn.matcher(sql);
        Set<String> columnList = new LinkedHashSet<String>();
        while (mth.find()) {
            String key = mth.group(1);
            columnList.add(key);// debug用
        }
        return columnList;
    }
    
}
