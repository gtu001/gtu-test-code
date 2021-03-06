package gtu.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import gtu.file.FileUtil;

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
        FileWriter out = null;
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

            out = new FileWriter(outputFile);
            temp.process(root, out);
            out.flush();
        } catch (Exception ex) {
            throw new RuntimeException("replace ERR : " + ex.getMessage(), ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static String replace(String templateSource, Map<String, Object> root) {
        StringWriter out = null;
        try {
            Configuration cfg = new Configuration();

            StringTemplateLoader stringTemplatge = new StringTemplateLoader();
            stringTemplatge.putTemplate("aaa", templateSource);

            cfg.setTemplateLoader(stringTemplatge);
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            Template temp = cfg.getTemplate("aaa");

            out = new StringWriter();
            temp.process(root, out);
            out.flush();
            return out.getBuffer().toString();
        } catch (Exception ex) {
            throw new RuntimeException("replace ERR : " + ex.getMessage(), ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static String replace(File projectDir, String templateSource, Map<String, Object> root) {
        StringWriter out = null;
        try {
            Configuration cfg = new Configuration();

            StringTemplateLoader stringTemplatge = new StringTemplateLoader();
            stringTemplatge.putTemplate("aaa", templateSource);

            List<TemplateLoader> tempLst = new ArrayList<TemplateLoader>();
            tempLst.add(stringTemplatge);
            if (projectDir != null && projectDir.exists()) {
                FileTemplateLoader ftl = new FileTemplateLoader(projectDir);
                tempLst.add(ftl);
            }
            MultiTemplateLoader mtl = new MultiTemplateLoader(tempLst.toArray(new TemplateLoader[0]));
            cfg.setTemplateLoader(mtl);
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            Template temp = cfg.getTemplate("aaa");

            out = new StringWriter();
            temp.process(root, out);
            out.flush();
            return out.getBuffer().toString();
        } catch (Exception ex) {
            throw new RuntimeException("replace ERR : " + ex.getMessage(), ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
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
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                }
            }
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
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static void replace(File projectBaseDir, File ftlFile, Map<String, Object> root, OutputStream outputStream, String encode) {
        Writer out = null;
        try {
            Configuration cfg = new Configuration();

            String defaultEncode = cfg.getEncoding(cfg.getLocale());
            System.out.println("!!!!!defaultEncode == " + defaultEncode);
            if (StringUtils.isNotBlank(encode)) {
                defaultEncode = encode;
            }
            cfg.setEncoding(cfg.getLocale(), defaultEncode);

            if (!ftlFile.isFile()) {
                throw new IllegalArgumentException("not a file! : " + ftlFile);
            }

            String ftlTemplateName = "";
            if (projectBaseDir == null) {
                projectBaseDir = ftlFile.getParentFile();
                ftlTemplateName = ftlFile.getName();
            } else {
                Pattern pathPtn = Pattern.compile("\\Q" + projectBaseDir.getAbsolutePath() + "\\E(.*)");
                Matcher mth = pathPtn.matcher(ftlFile.getAbsolutePath());
                if (mth.find()) {
                    ftlTemplateName = mth.group(1);
                    ftlTemplateName = FileUtil.fixPath(ftlTemplateName, true);
                    System.out.println("fix ftlTemplateName = " + ftlTemplateName);
                }
            }
            cfg.setDirectoryForTemplateLoading(projectBaseDir);
            cfg.setObjectWrapper(new DefaultObjectWrapper());

            Template temp = cfg.getTemplate(ftlTemplateName);

            out = new OutputStreamWriter(outputStream, encode);
            temp.process(root, out);
            out.flush();
        } catch (Exception ex) {
            throw new RuntimeException("replace ERR : " + ex.getMessage(), ex);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                }
            }
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
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
