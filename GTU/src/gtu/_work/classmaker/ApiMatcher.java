package gtu._work.classmaker;

import gtu.collection.MapUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class ApiMatcher {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Map<String, String> apiMap = ApiMatcher.newInstance().loadSysIn().execute().getApiMap();
        MapUtil.showMapInfo(apiMap);
    }

    private LoadFrom loadFrom;
    private LoadFrom.Builder builder;
    private Map<String, String> apiMap;

    private ApiMatcher() {
    }

    public static ApiMatcher newInstance() {
        return new ApiMatcher();
    }

    public ApiMatcher execute() throws Exception {
        this.loadApi();
        return this;
    }

    public Map<String, String> getApiMap() {
        return apiMap;
    }

    private void loadApi() throws Exception {
        // TODO
        BufferedReader br = new BufferedReader(loadFrom.apply(builder));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            if (line.equals("exit") && loadFrom == LoadFrom.SYS_IN) {
                break;
            }
            sb.append(" " + line + " ");
        }
        br.close();

        sb = new StringBuilder(sb.toString().trim());
        apiMap = new HashMap<String, String>();

        MethodContent c = new MethodContent();
        c.otherContent = sb.toString();

        findMethodContent(c);
    }

    private void findMethodContent(MethodContent c) {
        Pattern pattern1 = Pattern.compile("(\\s*)(\\w+)(\\s*\\(.*)(.*)");// methodName(xxxx
        Pattern pattern2 = Pattern.compile("(\\))(.*)");// methodName(xxxx

        Matcher matcher = null;
        while (true) {
            matcher = pattern1.matcher(c.otherContent);
            if (matcher.find()) {
                if (StringUtils.isNotBlank(c.methodName)) {
                    int pos = c.otherContent.indexOf(matcher.group(2));
                    String methodContent = c.otherContent.substring(0, pos);
                    apiMap.put(c.methodName, methodContent);
                }
                c.methodName = matcher.group(2);
                c.otherContent = matcher.group(3);
            } else {
                break;
            }
            matcher = pattern2.matcher(c.otherContent);
            if (matcher.find()) {
                int pos = matcher.group(0).indexOf(matcher.group(2));
                c.otherContent = matcher.group(0).substring(pos);
            }
        }
    }

    class MethodContent {
        String methodName;
        String methodContent;
        String otherContent;
    }

    private void showMatchGroup(Matcher matcher) {
        System.out.println("group = " + matcher.group());
        for (int ii = 0; ii <= matcher.groupCount(); ii++) {
            System.out.println(ii + " = " + matcher.group(ii));
        }
    }

    // 舊的處理
    // private void loadApi() throws Exception {
    // Pattern pattern = Pattern.compile("(.*\\s)(\\w+)(\\(.*\\))");
    // Matcher matcher = null;
    // BufferedReader br = new BufferedReader(loadFrom.apply(builder));
    // String line = null;
    // String method = null;
    // StringBuilder sb = new StringBuilder();
    // apiMap = new HashMap<String, String>();
    // while ((line = br.readLine()) != null) {
    // matcher = pattern.matcher(line);
    // if (matcher.find()) {
    // if (sb.length() != 0 && method != null) {
    // apiMap.put(method, sb.toString());
    // }
    // method = matcher.group(2).trim();
    // sb = new StringBuilder();
    // } else if (line.equals("exit") && loadFrom == LoadFrom.SYS_IN) {
    // break;
    // } else {
    // sb.append(line.trim());
    // }
    // }
    // br.close();
    // }

    public ApiMatcher loadSysIn() {
        loadFrom = LoadFrom.SYS_IN;
        builder = new LoadFrom.Builder();
        return this;
    }

    public ApiMatcher loadString(String context) {
        loadFrom = LoadFrom.STRING;
        builder = new LoadFrom.Builder();
        builder.context = context;
        return this;
    }

    private enum LoadFrom {
        SYS_IN {
            @Override
            public Reader apply(Builder builder) throws UnsupportedEncodingException {
                System.out.println("請貼上API...");
                return new InputStreamReader(System.in, "UTF8");
            }
        }, //
        STRING {
            @Override
            public Reader apply(Builder builder) {
                return new StringReader(builder.context);
            }
        }, //
        ;

        static class Builder {
            String context;
        }

        public abstract Reader apply(Builder builder) throws Exception;
    }
}
