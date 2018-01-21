package gtu._work;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 計算目錄大硬碟空間大小
 */
public class DiskSpaceCheck {

    MyFileWriter writer;
    File rootFile;
    int dirLayerLimit = -1;
            
    public static void main(String[] args) {
        
        DiskSpaceCheck test = new DiskSpaceCheck();
        test.rootFile = new File("C:\\");
        
//        test.writer = new MyFileWriter(new File("C:/Users/gtu001/Desktop/spaceUse.csv"));
//        
//        test.calcLayerLimit();
//        
//        test.searchFilefind(test.rootFile);
//        
//        test.writer.close();
        
        
        File dir = new File("c:/");
        for(File f : dir.listFiles()){
            if(f.isDirectory()){
                try{
                    long mb = (FileUtils.sizeOfDirectory(f) / (1024 * 1024));
                    System.out.println(f.getName() + "..." + mb);
                }catch(Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
    
    private void calcLayerLimit(){
        dirLayerLimit = StringUtils.countMatches(rootFile.getAbsolutePath(), "\\") + 3;
    }

    private void searchFilefind(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory() && file.listFiles() != null) {
            for (File f : file.listFiles()) {
                searchFilefind(f);
            }
            if(rootFile == file){
                return;
            }
            if(StringUtils.countMatches(file.getAbsolutePath(), "\\") <= dirLayerLimit){
                System.out.println(file);
                try{
                    long mb = (FileUtils.sizeOfDirectory(file) / (1024 * 1024));
                    if(mb > 10){
                        System.out.println(file + "\t" + mb + "mb");
                        writer.writeLine("\"" + file + "\",\"" + mb + "\"");
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        } else {
            // file
        }
    }
    
    private static class MyFileWriter {
        BufferedWriter writer;
        MyFileWriter(File file){
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        private void writeLine(String line){
            try {
                writer.write(line);
                writer.newLine();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        private void close(){
            try {
                writer.flush();
                writer.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
