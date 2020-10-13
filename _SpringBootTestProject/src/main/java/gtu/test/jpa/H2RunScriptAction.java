package gtu.test.jpa;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class H2RunScriptAction {

    @Autowired
    private Environment environment;

    @Value("${spring.datasource.url}")
    private String h2JdbcURL;
    @Value("${spring.datasource.password}")
    private String h2Password;
    @Value("${spring.datasource.username}")
    private String h2Username;
    @Value("${spring.h2.jar.position}")
    private String h2JarPosition;
    @Value("${spring.h2.script}")
    private String h2ScriptSql;

    @PostConstruct
    public void init() {
        File h2File = new File(h2ScriptSql);
        runScript(h2File);
    }

    public void runScript(File file) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(" java -cp ");//
            sb.append(h2JarPosition);//
            sb.append(" org.h2.tools.RunScript ");//
            sb.append(" -url ");//
            sb.append(h2JdbcURL);//
            appendSb(" -user ", h2Username, sb);
            appendSb(" -password ", h2Password, sb);
            sb.append(" -script ");//
            sb.append("\"" + file + "\"");//
            System.out.println("## RunScript : " + sb);
            Process proc = Runtime.getRuntime().exec(sb.toString());

            byte[] inputArry = this.processInputStreamAsync(proc, "input");
            byte[] errorArry = this.processInputStreamAsync(proc, "error");

            String inputStr = new String(inputArry, "UTF8");
            String errorStr = new String(errorArry, "UTF8");

            if (StringUtils.isNotBlank(errorStr)) {
                throw new Exception(errorStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendSb(String prefix, String value, StringBuilder sb) {
        if (StringUtils.isNotBlank(value)) {
            sb.append(prefix);
            sb.append(value);
        }
    }

    private byte[] processInputStreamAsync(Process process, final String type) {
        final ArrayBlockingQueue<byte[]> waitingQueue = new ArrayBlockingQueue<byte[]>(1);
        // linux 不work
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedInputStream bis = null;
                ByteArrayOutputStream baos = null;
                try {
                    InputStream is = null;
                    if ("input".equals(type)) {
                        is = process.getInputStream();
                    } else if ("error".equals(type)) {
                        is = process.getErrorStream();
                    }
                    bis = new BufferedInputStream(is);
                    baos = new ByteArrayOutputStream();
                    byte[] content = new byte[1024];
                    int pos = -1;
                    while ((pos = bis.read(content)) != -1) {
                        baos.write(content, 0, pos);
                    }
                    baos.flush();
                    byte[] arry = baos.toByteArray();
                    if ("input".equals(type)) {
                        waitingQueue.add(arry);
                        System.out.println("# input stream done!!!");
                    } else if ("error".equals(type)) {
                        waitingQueue.add(arry);
                        System.out.println("# error stream done!!!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        bis.close();
                    } catch (Exception e) {
                    }
                    try {
                        baos.close();
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
        byte[] resultArry = new byte[0];
        try {
            resultArry = waitingQueue.take();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultArry;
    }
}
