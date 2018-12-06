package gtu.log.finder.etc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import gtu.file.FileUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WebTestJsonUtil {

    private static final WebTestJsonUtil _INST = new WebTestJsonUtil();

    public void saveJson(HttpServletRequest request, String... attrNames) {
        if (attrNames == null || attrNames.length == 0) {
            return;
        }
        for (String attrName : attrNames) {
            Object obj = request.getAttribute(attrName);
            saveJsonToFile(attrName, obj);
        }
    }

    public static WebTestJsonUtil getInstance() {
        return _INST;
    }

    public void saveJsonToFile(String fileName, Object printObj) {
        File todayDir = new File(FileUtil.DESKTOP_DIR, new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis()));
        StackTraceElement srcElement = getPrefix();
        if (srcElement == null) {
            System.out.println("自己不能測試,要有來源!");
            return;
        }
        File jsonFile = new File(todayDir, srcElement.getFileName().replaceAll("\\.java$", "") + File.separator + srcElement.getMethodName() + File.separator + fileName + ".txt");
        jsonFile.getParentFile().mkdirs();
        String data = "";
        if (printObj instanceof Collection) {
            data = JSONArray.fromObject(printObj).toString();
        } else {
            data = JSONObject.fromObject(printObj).toString();
        }
        FileUtil.saveToFile(jsonFile, data, "UTF8");
    }

    private StackTraceElement getPrefix() {
        StackTraceElement[] sks = Thread.currentThread().getStackTrace();
        StackTraceElement currentElement = null;
        boolean findThisOk = false;
        for (int ii = 0; ii < sks.length; ii++) {
            if (StringUtils.equals(sks[ii].getFileName(), WebTestJsonUtil.class.getSimpleName() + ".java")) {
                findThisOk = true;
            }
            if (findThisOk && //
                    !StringUtils.equals(sks[ii].getFileName(), WebTestJsonUtil.class.getSimpleName() + ".java")) {
                currentElement = sks[ii];
                break;
            }
        }
        return currentElement;
    }
}
