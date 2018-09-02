package gtu.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;


public class RemoveErrorCharFromXml {

    public static String cleanInvalidXmlChars(String text) {
        // From xml spec valid chars:
        // #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
        // any Unicode character, excluding the surrogate blocks, FFFE, and FFFF.
        return text.replaceAll("[^\\x09\\x0A\\x0D\\x20-\\uD7FF\\uE000-\\uFFFD\\u10000-\\u10FFFF]", "");
    }
    
    public static File createCleanXmlFile(File xmlFile){
        try{
            File outputFile = File.createTempFile("TMP_", xmlFile.getName());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf8"));
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(xmlFile), "utf8"));
            for(String line = null; (line = reader.readLine())!= null ; ){
                line = line.replaceAll("[^\\x09\\x0A\\x0D\\x20-\\uD7FF\\uE000-\\uFFFD\\u10000-\\u10FFFF]", "");
                writer.write(line);
                writer.newLine();
            }
            reader.close();
            writer.flush();
            writer.close();
            return outputFile;
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
