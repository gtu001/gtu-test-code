package gtu.properties;

import gtu.collection.ListUtil;
import gtu.reflect.ToStringUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class ResourceBundleTest {

    private Logger log = Logger.getLogger(getClass());

    public static void main(String[] args) throws Exception {
        ResourceBundleTest test = new ResourceBundleTest();
        // test.writeClassInfo();
        test.getAbsolutePathResource();
    }

    public void getAbsolutePathResource() throws Exception {
        // 檔案位於 : C:\workspace\GTU\src\gtu\properties\messages.properties //
        File file = new File("C:\\workspace\\GTU\\src\\gtu\\properties");
        URL[] url = { file.toURL() };
        ClassLoader clzLoader = new URLClassLoader(url);
        ResourceBundle test = ResourceBundle.getBundle("messages", Locale.getDefault(), clzLoader);
        ListUtil.showListInfo(ListUtil.getList(test.getKeys()));
    }

    public void writeClassInfo() throws Exception {
        ResourceBundle.clearCache();
        Locale locale = new Locale("zh", "TW");
        ResourceBundle test = ResourceBundle.getBundle("gtu.properties.Bundle", locale);

        // PUBLIC
        log.debug("\ttest.keySet = " + ToStringUtil.toString(test.keySet()));
        ResourceBundle.clearCache();
        log.debug("\ttest.getKeys = " + ToStringUtil.toString(test.getKeys()));
        log.debug("\ttest.getLocale = " + ToStringUtil.toString(test.getLocale()));
        // -------------------------------------------------------------------------
        log.debug("\ttest.getObject = " + ToStringUtil.toString(test.getObject("test")));
        log.debug("\ttest.containsKey = " + ToStringUtil.toString(test.containsKey("test")));
        log.debug("\ttest.getString = " + ToStringUtil.toString(test.getString("test")));
        // log.debug("\ttest.getStringArray = " +
        // ToStringUtil.toString(test.getStringArray("test")));

        System.out.println("-------------------------------------------------------------------------");

        test = ResourceBundle.getBundle("gtu.properties.Bundle", Locale.US);

        // PUBLIC
        log.debug("\ttest.keySet = " + ToStringUtil.toString(test.keySet()));
        ResourceBundle.clearCache();
        log.debug("\ttest.getKeys = " + ToStringUtil.toString(test.getKeys()));
        log.debug("\ttest.getLocale = " + ToStringUtil.toString(test.getLocale()));
        // -------------------------------------------------------------------------
        log.debug("\ttest.getObject = " + ToStringUtil.toString(test.getObject("test")));
        log.debug("\ttest.containsKey = " + ToStringUtil.toString(test.containsKey("test")));
        log.debug("\ttest.getString = " + ToStringUtil.toString(test.getString("test")));
        // log.debug("\ttest.getStringArray = " +
        // ToStringUtil.toString(test.getStringArray("test")));
    }
}
