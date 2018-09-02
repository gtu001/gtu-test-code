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

public class Dimension {

    private Dimension() {
    }

    public static Dimension getInstance() {
        return INSTANCE;
    }

    public Dimension applyParameter(Map<String, Object> map) {
        this.map = map;
        return this;
    }

    public Dimension applyDestDir(String destDir) {
        this.destDir = destDir;
        return this;
    }

    Map<String, Object> map;
    String destDir;

    public void execute() throws IOException {
        Validate.notNull(map);
        Validate.notEmpty(destDir);

        //        String modelXml = readFile("model2.xml");
        String modelXml = readFile2("model2.xml");

        String generateContent = create(modelXml, map);

        System.out.println("dest = " + destDir + "\\model.xml");

        this.saveToFile(destDir + "\\model.xml", generateContent.getBytes("UTF8"));
    }

    String readFile2(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResource(fileName)
                .openStream(), "UTF8"));
        StringBuilder sb = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null;) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
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

    public String create(String templateBody, Map<String, Object> map) {
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

    private static final Dimension INSTANCE = new Dimension();
}
