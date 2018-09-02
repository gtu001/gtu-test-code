package gtu.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class LoaderTest {

    /**
     * @param args
     * @throws IOException
     * @throws TemplateException
     */
    public static void main(String[] args) throws IOException, TemplateException {
        Configuration cfg = new Configuration();

        File dir = new File(SimpleTest.class.getResource("").getPath());

        FileTemplateLoader ftl = new FileTemplateLoader(dir);
        ClassTemplateLoader ctl = new ClassTemplateLoader(LoaderTest.class, "");

        TemplateLoader[] loaders = { ftl, ctl };
        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
        cfg.setTemplateLoader(mtl);

        Template temp = cfg.getTemplate("test.ftl", Locale.FRENCH);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("testDate", "");

        Writer out = new OutputStreamWriter(System.out);
        temp.process(model, out);
        out.flush();
        out.close();
    }

}
