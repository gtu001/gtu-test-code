package gtu.class_;

import gtu.properties.PropertiesUtil;

import java.io.File;
import java.net.URL;


/**
 * @author Troy 2009/06/03
 * 
 */
public class ClassPathUtil {


    public static void main(String[] args) {
    }

    /**
     * 顯示這個instance 所對應的 class檔案的絕對路徑 , 簡單的說就是執行這個 mathod 的 class 檔案路徑
     * 
     * @author Lupin
     */
//    @Test
    public void testPathInfo(Class<?> clz) {
        System.out.println("class Name = " + clz.getName());
        System.out.println("clz.getResource('/').getPath() = " + clz.getResource("/").getPath());
        System.out.println("clz.getResource('').getPath() = " + clz.getResource("").getPath());
        System.out.println("clz.getResource('..').getPath() = " + clz.getResource("..").getPath());
        System.out.println("clz.getResource('.').getPath() = " + clz.getResource(".").getPath());
        System.out.println("clz.getResource('ClassPathUtil.class').getPath() = " + clz.getResource("ClassPathUtil.class").getPath());
        System.out.println("clz.getClassLoader().getResource('').getPath() = " + clz.getClassLoader().getResource("").getPath());
    }

    /**
     * 取得java "原始碼"所在路徑
     * 
     * @param clz
     * @return
     */
    public static String getJavaFilePath(Class<?> clz) {
        String path = System.getProperty("user.dir") + "\\src\\" + clz.getPackage().getName().replace('.', '\\');
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("找不到路徑 = " + file.getAbsolutePath());
        }
        return file.getAbsolutePath() + "\\";
    }

    public static File getWarRootPath(Class<?> clz) {
        URL url = clz.getResource(clz.getSimpleName() + ".class");
        if (url.getProtocol().equals("zip")) {
            int pos = url.getFile().indexOf("WEB-INF");
            if (pos != -1) {
                File file = new File(url.getFile().substring(0, pos));
                return file.getAbsoluteFile();
            }
        }
        return null;
    }

    public static File getJarCurrentPath(Class<?> clz) {
        return PropertiesUtil.getJarCurrentPath(clz);
    }
}
