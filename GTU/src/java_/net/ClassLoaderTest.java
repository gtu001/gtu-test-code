package java_.net;
import gtu.file.FileUtil;
import gtu.reflect.ToStringUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;


public class ClassLoaderTest {

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, UnsupportedEncodingException {

        File file = new File("C:/Jar/iisi/ris3ape-api-0.0.l-SNAPSHOT.jar");
        _URLClassLoader urlLoader = _URLClassLoader.newInstance(new URL[] { FileUtil.fileToURL(file) });

        System.out.println("urlLoader init ...");

        Class clz1 = urlLoader.loadClass("gtu.reflect.ToStringUtil");
        Class clz2 = Class.forName("gtu.reflect.ToStringUtil");

        System.out.println(clz1);
        System.out.println(clz2);
        System.out.println(clz1 == clz2);
        System.out.println(clz1.equals(clz2));

        System.out.println(ToStringUtil.toString("xxxxxx"));
        System.out.println("aaaa=" + (ToStringUtil.class == clz1));
        System.out.println("bbbb=" + (ToStringUtil.class == clz2));
    }
}
