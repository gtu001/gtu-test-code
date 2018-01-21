package gtu.log.finder;

import gtu.date.DateUtil;
import gtu.file.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;

public class LogFinder {

    public static void main(String args[]) throws Exception {
        LogFinder test = new LogFinder();
        List<File> fileList = new ArrayList<File>();
        File file = new File("C:/Users/gtu001/Desktop/allFile.txt");
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "big5"));
        for(String line = null ; (line = reader.readLine())!= null ;){
            fileList.add(new File(line));
        }
        reader.close();
        
        for(File f : fileList){
            test.execute(f);
        }
        System.out.println("done...");
    }
    
    int beforeLine = 5;
    int afterLine = 5;
    String patternStr;
    File outputLogFile = new File(FileUtil.DESKTOP_DIR, "logFinder_"+DateUtil.getCurrentDateTime(false)+".log");
    String readLogEncode = "utf8";
    String writeLogEncode = "utf8";
    
    public LogFinder(){
    }
    
    public LogFinder(int beforeLine, int afterLine, String patternStr, File outputLogFile){
        this.beforeLine = beforeLine;
        this.afterLine = afterLine;
        this.patternStr = patternStr;
        this.outputLogFile = outputLogFile;
        Validate.notBlank(patternStr);
        Validate.notNull(this.outputLogFile);
        if(!this.outputLogFile.exists()){
            throw new RuntimeException("outputLogFile檔案不存在 : " + outputLogFile.getAbsolutePath());
        }
    }
    
    public LogFinder(int beforeLine, int afterLine, String patternStr, File outputLogFile, String readLogEncode, String writeLogEncode){
        this.beforeLine = beforeLine;
        this.afterLine = afterLine;
        this.patternStr = patternStr;
        this.outputLogFile = outputLogFile;
        this.readLogEncode = readLogEncode;
        this.writeLogEncode = writeLogEncode;
        Validate.notBlank(patternStr);
        Validate.notBlank(this.readLogEncode);
        Validate.notBlank(this.writeLogEncode);
        Validate.notNull(this.outputLogFile);
        if(!this.outputLogFile.exists()){
            throw new RuntimeException("outputLogFile檔案不存在 : " + outputLogFile.getAbsolutePath());
        }
    }
    
    public void execute(File file){
        try {
            Pattern checkLockPtn = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
            Matcher matcher = null;
            LineNumberReader reader1 = new LineNumberReader(new InputStreamReader(new FileInputStream(file), readLogEncode));
            Set<Integer> matchLineList = new HashSet<Integer>();
            for(String line = null ; (line = reader1.readLine())!= null ;){
                matcher = checkLockPtn.matcher(line);
                if(matcher.find()){
                    matchLineList.add(reader1.getLineNumber());
                }
            }
            reader1.close();
            List<Integer> matchLineListCopy = new ArrayList<Integer>(matchLineList);
            
            List<Integer> newLineList = new ArrayList<Integer>();
            for(int lineNum : matchLineList){
                for(int ii = lineNum - beforeLine; ii <= lineNum + afterLine ; ii ++){
                    newLineList.add(ii);
                }
            }
            matchLineList.addAll(newLineList);
            
            int tmpLineNumber = -1;
            int sectionNumber = 0;
            boolean firstPrintOk = false;
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputLogFile), writeLogEncode));
            writer.write(file.getName());
            writer.newLine();
            LineNumberReader reader2 = new LineNumberReader(new InputStreamReader(new FileInputStream(file), readLogEncode));
            for(String line = null ; (line = reader2.readLine())!= null ;){
                int lineNumb = reader2.getLineNumber();
                if(matchLineList.contains(lineNumb)){
                    if(tmpLineNumber + 1 != lineNumb){
                        if(!firstPrintOk){
                            firstPrintOk = true;
                        }else{
                            writer.write("區段"+sectionNumber+"↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
                            writer.newLine();
                        }
                        writer.write("區段"+(++sectionNumber)+"↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
                        writer.newLine();
                    }
                    if(matchLineListCopy.contains(lineNumb)){
                        writer.write("("+lineNumb+"):"+line);
                        writer.newLine();
                    }else{
                        writer.write(lineNumb+":"+line);
                        writer.newLine();
                    }
                    tmpLineNumber = lineNumb;
                }
            }
            if(firstPrintOk){
                writer.write("區段"+sectionNumber+"↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
                writer.newLine();
            }
            reader2.close();
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
