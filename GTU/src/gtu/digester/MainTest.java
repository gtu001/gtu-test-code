package gtu.digester;

import java.net.URL;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class MainTest {

    public static void main(String[] args) {
        try {
            URL rule = MainTest.class.getResource("db-rules.xml");
            URL config = MainTest.class.getResource("db-config.xml");
            Digester digester = DigesterLoader.createDigester(rule);
            DBConfig dbConfig = (DBConfig) digester.parse(config.openStream());
            
            Map<String,DBProvider> dbProviders = dbConfig.getDbProviders();
            for(String key : dbProviders.keySet()){
                DBProvider provider = dbProviders.get(key);
                System.out.println(key + " -- " + ReflectionToStringBuilder.toString(provider));
            } 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
