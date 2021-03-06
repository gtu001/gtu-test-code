package gtu.properties;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Properties;

public class PropertiesUtil {

    public static void main(String[] args) throws IOException, Exception {
        System.out.println("done...");
    }

    private PropertiesUtil() {
    }

    public static Properties loadAskeys(InputStream in) throws IOException {
        Properties prop = new Properties();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF8"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() != 0) {
                prop.put(line, line);
            }
        }
        reader.close();
        return prop;
    }

    public static Properties loadAskeys(File file) throws IOException {
        return loadAskeys(new FileInputStream(file));
    }

    public static void storeAsKeys(File file, Properties prop) throws IOException {
        storeAsKeys(new FileOutputStream(file), prop);
    }

    public static void storeAsKeys(OutputStream out, Properties prop) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF8"));
        for (Object key : prop.keySet()) {
            writer.write((String) key);
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }

    public static File getJarCurrentPath(Class<?> clz) {
        URL url = clz.getResource(clz.getSimpleName() + ".class");
        String protocal = url.getProtocol();
        String filepath = url.getFile();
        //        System.out.println("protocal = " + protocal);
        //        System.out.println("url = " + url);
        //        System.out.println("file = " + filepath);
        String path = null;
        if (protocal.equals("jar")) {
            path = filepath.replaceFirst("file:", "");
            path = path.substring(0, path.lastIndexOf("!"));
            File file = new File(path).getParentFile();
            System.err.println("use jar : " + file);
            return file;
        }
        if (protocal.equals("file")) {
            path = filepath.startsWith("/") ? filepath.substring(1) : filepath;
            File file = new File(path).getParentFile();
            System.err.println("use file : " + file);
            return file;
        }
        File defaultFile = new File(clz.getResource("").getFile()).getAbsoluteFile();
        System.err.println("use default : " + defaultFile);
        return defaultFile;
    }
}
