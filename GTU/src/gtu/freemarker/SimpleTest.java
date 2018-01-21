package gtu.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class SimpleTest {

    /**
     * @param args
     * @throws IOException
     * @throws TemplateException
     */
    public static void main(String[] args) throws IOException, TemplateException {
        Configuration cfg = new Configuration();
        // 指定模板文件从何处加载的数据源，这里设置成一个文件目录。 

        File dir = new File(SimpleTest.class.getResource("").getPath());

        cfg.setDirectoryForTemplateLoading(dir);
        // 指定模板如何检索数据模型，这是一个高级的主题了… 
        // 但先可以这么来用： 
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        Template temp = cfg.getTemplate("test.ftl", "BIG5");

        Map<String, Object> root = new HashMap<String, Object>();
        root.put("user", "Big Joe");
        Map<String, Object> latest = new HashMap<String, Object>();
        root.put("latestProduct", latest);
        latest.put("url", "products/greenmouse.html");
        latest.put("name", "green mouse");

        root.put("message", "test!!!!");

        root.put("user1", "AAAA");
        root.put("user2", "xxxxx");//物件不可放null

        root.put("array", new Object[] { new TestObj("哈哈哈", 5), new TestObj("呵呵呵", 10) });//類別必須定義public才可呼叫getter

        Book book = new Book("<b>一本書</b>", "<b>好書</b>");
        book.setComment(Arrays.asList("<b>這是一本好書</b>", "<b>你想看媽</b>", "<b>快去買..</b>"));
        root.put("book", book);

        root.put("date", new Date());

        root.put("testMethod", new TestMethod());//自訂method

        Writer out = new OutputStreamWriter(System.out);
        temp.process(root, out);
        out.flush();

        Environment env = temp.createProcessingEnvironment(root, out);
        env.process(); // 处理模板 
        TemplateModel test = env.getVariable("this is a book"); // 获取参数
        System.out.println("test = " + test);

        System.err.println("done...");
    }

    private static class TestMethod implements TemplateMethodModel {
        @SuppressWarnings("rawtypes")
        public Object exec(List paramList) throws TemplateModelException {
            StringBuilder sb = new StringBuilder();
            for (Object val : paramList) {
                sb.append(val + ",");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return new SimpleScalar(sb.toString());
        }
    }

    public static class TestObj {
        String val;
        int seq;

        public TestObj(String val, int seq) {
            super();
            this.val = val;
            this.seq = seq;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }
    }

    public static class Book {
        String title;
        String description;
        List<String> comment = new ArrayList<String>();

        public Book(String title, String description) {
            super();
            this.title = title;
            this.description = description;
        }

        public Book() {
            super();
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<String> getComment() {
            return comment;
        }

        public void setComment(List<String> comment) {
            this.comment = comment;
        }
    }

    public static class TestModel implements TemplateModel {
        String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }

        @Override
        public String toString() {
            return "TestModel [test=" + test + "]";
        }
    }
}
