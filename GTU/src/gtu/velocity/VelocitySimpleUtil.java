package gtu.velocity;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

public class VelocitySimpleUtil {

    static {
        Properties props = new Properties();
        props.setProperty("resource.loader", "string");
        props.setProperty("string.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
        props.setProperty("string.resource.loader.repository.class",
                "org.apache.velocity.runtime.resource.util.StringResourceRepositoryImpl");
        try {
            Velocity.init(props);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String replace(String content, Map<String, Object> map) {
        try {
            StringResourceRepository vsRepository = StringResourceLoader.getRepository();

            VelocityContext context = new VelocityContext();
            for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                context.put(key, map.get(key));
            }

            StringWriter swriter = new StringWriter();
            swriter.write(content);

            for (int ii = 0; ii < 1; ii++) {
                vsRepository.putStringResource("aabbcc", swriter.getBuffer().toString());
                Template template = Velocity.getTemplate("aabbcc", "UTF-8");
                swriter = new StringWriter();
                template.merge(context, swriter);
            }

            return swriter.getBuffer().toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

}
