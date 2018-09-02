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

public class ExceptionLogFinder {

    public static void main(String args[]) throws Exception {
        ExceptionLogFinder test = new ExceptionLogFinder();
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
    File outputLogFile = new File(FileUtil.DESKTOP_DIR, "errorFinder_"+DateUtil.getCurrentDateTime(false)+".log");
    String readLogEncode = "utf8";
    String writeLogEncode = "utf8";
    
    public ExceptionLogFinder(){
    }
    
    public ExceptionLogFinder(int beforeLine, File outputLogFile){
        this.beforeLine = beforeLine;
        this.outputLogFile = outputLogFile;
        Validate.notNull(this.outputLogFile);
        if(!this.outputLogFile.exists()){
            throw new RuntimeException("outputLogFile檔案不存在 : " + outputLogFile.getAbsolutePath());
        }
    }
    
    public ExceptionLogFinder(int beforeLine, File outputLogFile, String readLogEncode, String writeLogEncode){
        this.beforeLine = beforeLine;
        this.outputLogFile = outputLogFile;
        this.readLogEncode = readLogEncode;
        this.writeLogEncode = writeLogEncode;
        Validate.notBlank(this.readLogEncode);
        Validate.notBlank(this.writeLogEncode);
        Validate.notNull(this.outputLogFile);
        if(!this.outputLogFile.exists()){
            throw new RuntimeException("outputLogFile檔案不存在 : " + outputLogFile.getAbsolutePath());
        }
    }
    
    public void execute(File file){
        try {
            Pattern exceptionPattern = Pattern.compile("\\tat\\s.*");
            Pattern checkLockPtn = Pattern.compile("exception", Pattern.CASE_INSENSITIVE);
            
            Matcher matcher = null;
            LineNumberReader reader1 = new LineNumberReader(new InputStreamReader(new FileInputStream(file), readLogEncode));
            Set<Integer> matchLineList = new HashSet<Integer>();
            int newAddLineNumber = -1;
            for(String line = null ; (line = reader1.readLine())!= null ;){
                matcher = checkLockPtn.matcher(line);
                int currentLineNumber = reader1.getLineNumber();
                if(matcher.find()){
                    matchLineList.add(currentLineNumber);
                    newAddLineNumber = currentLineNumber;
                }
                if(newAddLineNumber + 1 == currentLineNumber){
                    matcher = exceptionPattern.matcher(line);
                    if(matcher.find()){
                        matchLineList.add(currentLineNumber);
                        newAddLineNumber = currentLineNumber;
                    }
                }
            }
            reader1.close();
            List<Integer> matchLineListCopy = new ArrayList<Integer>(matchLineList);
            
            List<Integer> newLineList = new ArrayList<Integer>();
            for(int lineNum : matchLineList){
                for(int ii = lineNum - beforeLine; ii <= lineNum + beforeLine; ii ++){
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
