package gtu.svn;
import gtu.file.FileUtil;
import gtu.xml.XmlInfoUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class TortoiseMergeTest {

    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TortoiseMergeTest.class);
    
    public static void main(String[] args) throws IOException {
        String base = "C:/workspace/backup/config/RIS/share/FTL/201000001.ftl";
        String theirs = "C:/workspace/backup/config/RIS/share/FTL/202000001.ftl";


        //        /base:path to base file
        //        /theirs:path to their file
        //        /mine:path to your file
        //        /merged:path to resulting merged file
        //        /diff:path to unified diff file
        //        /patchpath:path to folder

        String commandFormat = "cmd /c call TortoiseMerge.exe /base:\"%s\" /theirs:\"%s\"";
        String command = String.format(commandFormat, base, theirs);

        System.out.println(command);

        Runtime.getRuntime().exec(command);
    }
    

    public void test() throws Exception {

        long start = System.currentTimeMillis();

        File file = FileUtil.getFileInDefaultDir("xxxx.xml");
        SAXReader sax = new SAXReader();
        Document doc = sax.read(file);
        Element root = doc.getRootElement();
        
        //TODO
        
        System.out.println(XmlInfoUtil.createTagClass(root));

        long end = System.currentTimeMillis() - start;

        log.debug(""+end);

        log.debug("done...");
    }

    File loadSvnStatus() throws IOException {
        Process process = Runtime.getRuntime().exec("cmd /c svn status -v --xml C:/workspace/GTU");
        File temp = File.createTempFile("svn_list_", null);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp)));
        for (String line = null; (line = reader.readLine()) != null;) {
            log.debug(line);
            writer.write(line);
            writer.newLine();
        }
        writer.flush();
        writer.close();
        reader.close();
        return temp;
    }

}
