package gtu._work;

import gtu.file.FileUtil;
import gtu.file.LineNumberOutputFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class SqlGroup {

    public static void main(String[] args) {
        SqlGroup test = new SqlGroup();
        test.execute(new File("d:/sql/dir"));
    }
    
    public void execute(File sqlDir){
        try{
            List<File> fileList = new ArrayList<File>();
            if(sqlDir.isDirectory() && sqlDir.listFiles() != null){
                for(File f : sqlDir.listFiles()){
                    fileList.add(f);
                }
            }else{
                fileList.add(sqlDir);
            }
            
            File dir = new File(FileUtil.DESKTOP_PATH, "BACKUP_SQL");
            if(!dir.exists()){
                dir.mkdirs();
            }
            LineNumberOutputFile insertFile = new LineNumberOutputFile(new File(dir, "insert_0.sql"));
            LineNumberOutputFile beforeUpdateDelete = new LineNumberOutputFile(new File(dir, "beforeUpdateDelete_0.sql"));
            LineNumberOutputFile updateFile = new LineNumberOutputFile(new File(dir, "update_0.sql"));
            LineNumberOutputFile deleteFile = new LineNumberOutputFile(new File(dir, "delete_0.sql"));
            LineNumberOutputFile errorFile = new LineNumberOutputFile(new File(dir, "error_0.sql"));
            
            for(File file : fileList){
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf8"));
                for(String line = null; (line = reader.readLine())!= null ;){
                    if(line.startsWith("insert")){
                        insertFile.writeLine(line);
                    }else if(line.startsWith("update")){
                        updateFile.writeLine(line);
                    }else if(line.startsWith("delete")){
                        if(StringUtils.countMatches(line, "=''") > 1){
                            System.out.println(line);
                        }else{
                            if(line.endsWith("--U")){
                                line = line.replaceAll("--U", "");
                                beforeUpdateDelete.writeLine(line);
                            }else{
                                deleteFile.writeLine(line);
                            }
                        }
                    }else{
                        errorFile.writeLine(line);
                    }
                }
                reader.close();
            }
            
            insertFile.close();
            beforeUpdateDelete.close();
            updateFile.close();
            deleteFile.close();
            errorFile.close();
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
