package gtu.velocity.gemtek;

import org.apache.velocity.Template;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

/**
 * @author Troy 2009/06/15
 * 
 */
public class Test {

    public static void main(String[] args) {

        Template template = new Template();

        ResourceLoader resourceLoader = new FileResourceLoader();

        // FileResourceLoader resourceLoader = new FileResourceLoader();
        // ExtendedProperties extendedProperties = new ExtendedProperties();
        // extendedProperties.addProperty("", value)
        // resourceLoader.init(arg0)

        template.setResourceLoader(resourceLoader);
    }
}
