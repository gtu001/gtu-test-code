package gtu.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;

public class FileUtils_Example {

    /**
     * 讀取檔案為byteArray
     */
    public ByteArrayInputStream getFileToByte(String filePath) throws IOException {
        InputStream tmpIS = new FileInputStream(filePath);
        byte[] tmp = IOUtils.toByteArray(tmpIS);
        return new ByteArrayInputStream(tmp);
    }

    /**
     * 找目錄底下所有檔案
     */
    public Iterator searchFilefind(File file, String[] subNames){
       Iterator iter = FileUtils.iterateFiles(file,subNames, true);
       return iter;
   }
}
