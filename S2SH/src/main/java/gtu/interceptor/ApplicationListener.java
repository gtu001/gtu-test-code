
package gtu.interceptor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;


public final class ApplicationListener implements ServletContextListener {

    private ServletContext context = null;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    public void contextDestroyed(ServletContextEvent event) {
//        context.removeAttribute(DATABASE_KEY);
//        context.removeAttribute(PROTOCOLS_KEY);
        context = null;
    }

    public void contextInitialized(ServletContextEvent event) {
        log.debug("", null);
        this.context = event.getServletContext();
//        context.setAttribute(DATABASE_KEY, database);
    }

    private String calculatePath() throws Exception {
        String pathname = "";
        
        // Can we access the database via file I/O?
        String path = context.getRealPath(pathname);
        if (path != null) {
            return (path);
        }

        // Does a copy of this file already exist in our temporary directory
        File dir = (File)
                context.getAttribute("javax.servlet.context.tempdir");
        File file = new File(dir, "struts-example-database.xml");
        if (file.exists()) {
            return (file.getAbsolutePath());
        }

        // Copy the static resource to a temporary file and return its path
        InputStream is =
                context.getResourceAsStream(pathname);
        BufferedInputStream bis = new BufferedInputStream(is, 1024);
        FileOutputStream os =
                new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        byte buffer[] = new byte[1024];
        while (true) {
            int n = bis.read(buffer);
            if (n <= 0) {
                break;
            }
            bos.write(buffer, 0, n);
        }
        bos.close();
        bis.close();
        return (file.getAbsolutePath());
    }
}
