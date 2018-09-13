package gtu.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerSimpleUtil {

    public static class FreemarkerTestBean {
        String fobValueTwdTotal;

        public String getFobValueTwdTotal() {
            return fobValueTwdTotal;
        }

        public void setFobValueTwdTotal(String fobValueTwdTotal) {
            this.fobValueTwdTotal = fobValueTwdTotal;
        }
    }

    public static void main(String[] args) throws IOException, TemplateException {
        String template = "<#if detail.fobValueTwdTotal! != ''><br/>--------<br/>${detail.fobValueTwdTotal!}</#if>";
        Map<String, Object> root = new HashMap<String, Object>();
        FreemarkerTestBean test = new FreemarkerTestBean();
        test.setFobValueTwdTotal("AAAFFF");
        root.put("detail", test);
        System.out.println(replace(template, root));
        System.out.println("done...");
    }

    public static void replace(File outputFile, final URL srcUrl, Map<String, Object> root) {
        try {
            Configuration cfg = new Configuration();
            URLTemplateLoader urlTemplate = new URLTemplateLoader() {
                @Override
                protected URL getURL(String name) {
                    return srcUrl;
                }
            };
            cfg.setTemplateLoader(urlTemplate);
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            Template temp = cfg.getTemplate("aaa");

            FileWriter out = new FileWriter(outputFile);
            temp.process(root, out);
            out.flush();
        } catch (Exception ex) {
            throw new RuntimeException("replace ERR : " + ex.getMessage(), ex);
        }
    }

    public static String replace(String templateSource, Map<String, Object> root) {
        try {
            Configuration cfg = new Configuration();

            StringTemplateLoader stringTemplatge = new StringTemplateLoader();
            stringTemplatge.putTemplate("aaa", templateSource);

            cfg.setTemplateLoader(stringTemplatge);
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            Template temp = cfg.getTemplate("aaa");

            StringWriter out = new StringWriter();
            temp.process(root, out);
            out.flush();
            return out.getBuffer().toString();
        } catch (Exception ex) {
            throw new RuntimeException("replace ERR : " + ex.getMessage(), ex);
        }
    }

    public static void replace(String templateSource, Map<String, Object> root, OutputStream outputStream) {
        try {
            Configuration cfg = new Configuration();

            StringTemplateLoader stringTemplatge = new StringTemplateLoader();
            stringTemplatge.putTemplate("aaa", templateSource);

            cfg.setTemplateLoader(stringTemplatge);
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            Template temp = cfg.getTemplate("aaa");

            Writer out = new OutputStreamWriter(outputStream);
            temp.process(root, out);
            out.flush();
        } catch (Exception ex) {
            throw new RuntimeException("replace ERR : " + ex.getMessage(), ex);
        }
    }

    public static void replace(String templateSource, Map<String, Object> root, Writer out) {
        try {
            Configuration cfg = new Configuration();

            StringTemplateLoader stringTemplatge = new StringTemplateLoader();
            stringTemplatge.putTemplate("aaa", templateSource);

            cfg.setTemplateLoader(stringTemplatge);
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            Template temp = cfg.getTemplate("aaa");

            temp.process(root, out);
            out.flush();
        } catch (Exception ex) {
            throw new RuntimeException("replace ERR : " + ex.getMessage(), ex);
        }
    }

    public static void replace(File file, Map<String, Object> root, OutputStream outputStream, String encode) {
        try {
            Configuration cfg = new Configuration();

            String defaultEncode = cfg.getEncoding(cfg.getLocale());
            System.out.println("!!!!!defaultEncode == " + defaultEncode);
            if (StringUtils.isNotBlank(encode)) {
                defaultEncode = encode;
            }
            cfg.setEncoding(cfg.getLocale(), defaultEncode);

            if (!file.isFile()) {
                throw new IllegalArgumentException("not a file! : " + file);
            }

            cfg.setDirectoryForTemplateLoading(file.getParentFile());
            cfg.setObjectWrapper(new DefaultObjectWrapper());

            Template temp = cfg.getTemplate(file.getName());

            Writer out = new OutputStreamWriter(outputStream, encode);
            temp.process(root, out);
            out.flush();
        } catch (Exception ex) {
            throw new RuntimeException("replace ERR : " + ex.getMessage(), ex);
        }
    }

    public static void replace(File file, Map<String, Object> root, Writer out) {
        try {
            Configuration cfg = new Configuration();

            if (!file.isFile()) {
                throw new IllegalArgumentException("not a file!");
            }

            cfg.setDirectoryForTemplateLoading(file.getParentFile());
            cfg.setObjectWrapper(new DefaultObjectWrapper());

            Template temp = cfg.getTemplate(file.getName());

            temp.process(root, out);
            out.flush();
        } catch (Exception ex) {
            throw new RuntimeException("replace ERR : " + ex.getMessage(), ex);
        }
    }
}
