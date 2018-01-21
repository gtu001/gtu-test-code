package gtu.zcognos;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.Validate;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

public class Simple {

    public static void main(String[] args) throws IOException {
        Simple test = new Simple();
        test.execute();
        System.out.println("done...");
    }

    private Simple() {
    }

    public static Simple getInstance() {
        return INSTANCE;
    }

    public Simple applyParameter(Map<String, Object> map) {
        this.map = map;
        return this;
    }

    public Simple applyDestDir(String destDir) {
        this.destDir = destDir;
        return this;
    }

    Map<String, Object> map;
    String destDir;

    public void execute() throws IOException {
        Validate.notNull(map);
        Validate.notEmpty(destDir);

        Validate.isTrue(map.containsKey("PROJECT_ID"));
        Validate.isTrue(map.containsKey("PACKAGE_ID"));
        Validate.isTrue(map.containsKey("DATATABLE"));

        String modelXml = readFile("model.xml");

        String generateContent = velocityGenerate(modelXml, map);

        this.saveToFile(destDir + "\\model.xml", generateContent.getBytes());
    }

    String readFile(String fileName) throws IOException {
        URL url = this.getClass().getResource(fileName);
        Validate.notNull(url);
        String file = URLDecoder.decode(url.getFile().substring(1), "UTF8");
        System.out.println("file = " + file);
        return loadFromFile(file);
    }

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

    public String velocityGenerate(String templateBody, Map<String, Object> map) {
        try {

            StringResourceRepository vsRepository = StringResourceLoader.getRepository();

            VelocityContext context = new VelocityContext();
            for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                context.put(key, map.get(key));
            }

            StringWriter swriter = new StringWriter();
            swriter.write(templateBody);

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

    String loadFromFile(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\r\n");
        }
        reader.close();
        return sb.toString();
    }

    void saveToFile(String fileName, byte[] data) {
        try {
            java.io.FileOutputStream out = new java.io.FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static final Simple INSTANCE = new Simple();
}
