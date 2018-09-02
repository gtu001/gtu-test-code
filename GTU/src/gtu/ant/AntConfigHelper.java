package gtu.ant;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.Project;

public class AntConfigHelper {
    Project project;
    
    public static AntConfigHelper of(Project project) {
        return new AntConfigHelper(project);
    }

    private AntConfigHelper(Project project) {
        this.project = project;
    }
    
    public String getParseAfterValue(String valueStr) {
        while(valueStr.contains("${") && valueStr.contains("}")) {
            valueStr = _parseAfterValue(valueStr);
        }
        return valueStr;
    }
    
    private String _parseAfterValue(String valueStr) {
        Hashtable<String,String> properties = project.getProperties();
        StringBuffer sb = new StringBuffer();
        Pattern ptn = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher matcher = ptn.matcher(valueStr);
        while(matcher.find()) {
            String key = matcher.group(1);
            String value = (String)properties.get(key);
            value = value.replaceAll("\\\\", "\\\\\\\\");
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        String rtnVal = sb.toString();
//        System.out.println("rtnVal = " + rtnVal);
        return rtnVal;
    }
}
