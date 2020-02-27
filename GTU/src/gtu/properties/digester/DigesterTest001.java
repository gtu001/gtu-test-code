package gtu.properties.digester;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

public class DigesterTest001 {

    private static Properties config = new Properties();

    public static void main(String[] args) throws IOException, SAXException {
        File file = new File("D:/workstuff/gtu-test-code/GTU/src/gtu/properties/digester/application-config.xml");
        InputStream is = new FileInputStream(file);

        Digester d = new Digester();
        d.addObjectCreate("application-config-here", DigesterTest001.class);
        d.addCallMethod("application-config-here/properties/set-property", "addConfig", 2);
        d.addCallParam("application-config-here/properties/set-property", 0, "property");
        d.addCallParam("application-config-here/properties/set-property", 1, "value");

        d.parse(is);

        for (Enumeration<?> enu = config.keys(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            String value = config.getProperty(key);
            System.out.println("\tkey:" + key + ", value:" + value);
        }
        System.out.println("done...");
    }

    public static void addConfig(String property, String value) {
        config.put(property, value);
    }
}
