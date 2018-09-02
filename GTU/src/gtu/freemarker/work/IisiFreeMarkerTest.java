package gtu.freemarker.work;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import tw.gov.moi.ae.codetable.RisCodeComponent;
import tw.gov.moi.ae.codetable.domain.RisCode;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateException;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateTransformModel;
import gtu.freemarker.FreeMarkerSimpleUtil;

@Component
public class IisiFreeMarkerTest {

    static Logger log = LoggerFactory.getLogger(IisiFreeMarkerTest.class);

    static IisiFreeMarkerTest iisiTest;

    @Autowired
    RisCodeConvertModel model;

    //    static File dir = new File("C:/L-CONFIG/SRIS/RIS/share/FTL");
    static File dir = new File("C:/Users/gtu001/Desktop/test");
    static Map<String, Object> root = new HashMap<String, Object>();

    static {
        ApplicationContext context = new ClassPathXmlApplicationContext("gtu/freemarker/work/applicationContext.xml");
        iisiTest = (IisiFreeMarkerTest) context.getBean("iisiFreeMarkerTest");
        root.put("convert", iisiTest.model);
        root.put("compress", new NoSpaceCompress());
        root.put("dto", new TestDTO());
        //        LogbackUtil.setOff(LoggerFactory.getLogger(freemarker.core.Expression.class));
    }

    public static class TestDTO {
    }

    public static void main(String[] args) throws IOException, TemplateException {
        // 203 宗達
        // 202 婷宜
        // 201 逸文

        // full single test ...
        // Map<String, Object> map = new HashMap<String, Object>();

        // single test ...
        //        iisiTest.singleTest("40A1000001", true);

        // all test ...
        iisiTest.allTest();

        System.out.println("done...");
    }

    @Component
    private static class RisCodeConvertModel implements TemplateMethodModel {

        @Autowired
        RisCodeComponent risCodeComponent;

        private static final SimpleScalar ERROR_CONVERT0 = new SimpleScalar("convert_error_illegal_argument");
        private static final SimpleScalar ERROR_CONVERT1 = new SimpleScalar("convert_error_category_not_found");
        private static final SimpleScalar ERROR_CONVERT2 = new SimpleScalar("convert_error_code_not_found");

        @SuppressWarnings("rawtypes")
        public Object exec(List paramList) throws TemplateModelException {
            if (paramList == null || paramList.size() < 2) {
                return ERROR_CONVERT0;
            }
            String category = (String) paramList.get(0);
            String code = (String) paramList.get(1);
            if (StringUtils.isBlank(category) || StringUtils.isBlank(code)) {
                return ERROR_CONVERT0;
            }
            if (!risCodeComponent.getAllCategory().contains(category)) {
                return ERROR_CONVERT1;
            }
            for (RisCode risCode : risCodeComponent.getReferenceMapping(category)) {
                if (risCode.getCode().equals(code)) {
                    return new SimpleScalar(risCode.getName());
                }
            }
            return ERROR_CONVERT2;
        }
    }

    private static class NoSpaceCompress implements TemplateTransformModel {
        private int defaultBufferSize;

        public NoSpaceCompress() {
            this(2048);
        }

        /**
         * @param defaultBufferSize
         *            the default amount of characters to buffer
         */
        public NoSpaceCompress(int defaultBufferSize) {
            this.defaultBufferSize = defaultBufferSize;
        }

        public Writer getWriter(final Writer out, Map args) throws TemplateModelException {
            return new StandardCompressWriter(out, defaultBufferSize, true);
        }

        private static class StandardCompressWriter extends Writer {
            private static final int MAX_EOL_LENGTH = 2; // CRLF is two bytes

            private final Writer out;
            private final char[] buf;

            private int pos = 0;
            private boolean inWhitespace = true;

            public StandardCompressWriter(Writer out, int bufSize, boolean singleLine) {
                this.out = out;
                buf = new char[bufSize];
            }

            public void write(char[] cbuf, int off, int len) throws IOException {
                for (;;) {
                    // Need to reserve space for the EOL potentially left in the
                    // state machine
                    int room = buf.length - pos - MAX_EOL_LENGTH;
                    if (room >= len) {
                        writeHelper(cbuf, off, len);
                        break;
                    } else if (room <= 0) {
                        flushInternal();
                    } else {
                        writeHelper(cbuf, off, room);
                        flushInternal();
                        off += room;
                        len -= room;
                    }
                }
            }

            private void writeHelper(char[] cbuf, int off, int len) {
                for (int i = off, end = off + len; i < end; i++) {
                    char c = cbuf[i];
                    if (Character.isWhitespace(c)) {
                        inWhitespace = true;
                    } else if (inWhitespace) {
                        inWhitespace = false;
                        buf[pos++] = replaceSpecialChar(c);
                    } else {
                        buf[pos++] = replaceSpecialChar(c);
                    }
                }
            }

            private char replaceSpecialChar(char c) {
                switch (c) {
                case '1':
                    c = '１';
                    break;
                case '2':
                    c = '２';
                    break;
                case '3':
                    c = '３';
                    break;
                case '4':
                    c = '４';
                    break;
                case '5':
                    c = '５';
                    break;
                case '6':
                    c = '６';
                    break;
                case '7':
                    c = '７';
                    break;
                case '8':
                    c = '８';
                    break;
                case '9':
                    c = '９';
                    break;
                case '0':
                    c = '０';
                    break;
                }
                return c;
            }

            private void flushInternal() throws IOException {
                out.write(buf, 0, pos);
                pos = 0;
            }

            public void flush() throws IOException {
                flushInternal();
                out.flush();
            }

            public void close() throws IOException {
                flushInternal();
            }
        }
    }

    void showBeanName(ApplicationContext context) {
        for (String b : context.getBeanDefinitionNames()) {
            System.out.println("bean ==== " + b);
        }
    }

    void showRisCodeInfo(String category, ApplicationContext context) {
        RisCodeComponent risCodeComponent = (RisCodeComponent) context.getBean("risCodeComponent");
        for (RisCode risCode : risCodeComponent.getReferenceMapping(category)) {
            System.out.println("\t" + risCode.getCode() + "..." + risCode.getName());
        }
    }

    void singleTest(String name, Map<String, Object> youMap, boolean openFile) throws IOException, TemplateException {
        File ftlFile = new File(dir, name + ".ftl");
        if (openFile) {
            Runtime.getRuntime().exec("cmd /c call \"" + ftlFile + "\"");
        }
        youMap.putAll(root);
        FreeMarkerSimpleUtil.replace(ftlFile, youMap, System.out);
        System.out.println();
    }

    void singleTest(String name, boolean openFile) throws IOException, TemplateException {
        File ftlFile = new File(dir, name + ".ftl");
        if (openFile) {
            Runtime.getRuntime().exec("cmd /c call \"" + ftlFile + "\"");
        }
        FreeMarkerSimpleUtil.replace(ftlFile, root, System.out);
        System.out.println();
    }

    void allTest() {
        allTest(".*");
    }

    void allTest(String regex) {
        Set<String> errorSet = new HashSet<String>();
        StringWriter out = new StringWriter();
        for (File f : dir.listFiles()) {
            if (!f.getName().replaceAll(Pattern.quote(".ftl"), "").matches(regex)) {
                continue;
            }
            try {
                out.write(f.getName() + " === ");
                FreeMarkerSimpleUtil.replace(f, root, out);
                out.write("\n");
            } catch (Exception ex) {
                ex.printStackTrace();
                errorSet.add(f.getName() + " : " + ex);
            }
        }

        System.out.println(out.getBuffer().toString());
        for (String err : errorSet) {
            System.out.println("### 有錯誤!! === {{" + err + "}}");
        }
        if (errorSet.isEmpty()) {
            System.out.println("### 無錯誤!! ###");
        }
    }
}
