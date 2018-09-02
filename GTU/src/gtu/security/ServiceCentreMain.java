package gtu.security;

import java.io.FilePermission;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;

public class ServiceCentreMain {

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        Class<?> clz = null;
        //URL[] urls = new URL[] { new URL("file:E:/workspace/ServiceCentre/testService.jar") };
        URL[] urls = new URL[] { new URL("file:C:/workspace/GTU/bin/gtu/security/ServiceCentreMain$TestService.class") };
        URLClassLoader urlLoader = new URLClassLoader(urls);
        clz = urlLoader.loadClass("gtu.security.ServiceCentreMain$TestService");
        Object obj = clz.newInstance();

        //        try {
        //            clz.getMethod("doService1", new Class[0]).invoke(obj, new Object[0]);
        //        } catch (Exception ex) {
        //            ex.printStackTrace();
        //        }
        try {
            clz.getMethod("doService2", new Class[0]).invoke(obj, new Object[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static class TestService {
        public void doService1() {
            //因为是远程代码，直接访问本地资源，是没有权限的，所以会出现异常
            doFileOperation();
        }

        public void doService2() {
            //照道理這個藥可以過...不知為何??
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                public Object run() {
                    doFileOperation();
                    return null;
                }
            });
        }

        private void doFileOperation() {
            Permission perm = new FilePermission("C:\\Users\\Troy\\Desktop\\吃.txt", "read");
            AccessController.checkPermission(perm);
            System.out.println(" TestService has permission ");
        }
    }
}
