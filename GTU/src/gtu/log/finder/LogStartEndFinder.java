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
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;

public class LogStartEndFinder {

    public static void main(String args[]) throws Exception {
        LogStartEndFinder test = new LogStartEndFinder();
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
    
    String startPatternStr;
    String endPatternStr;
    File outputLogFile = new File(FileUtil.DESKTOP_DIR, "logStartEndFinder_"+DateUtil.getCurrentDateTime(false)+".log");
    String readLogEncode = "utf8";
    String writeLogEncode = "utf8";
    
    public LogStartEndFinder(){
    }
    
    public LogStartEndFinder(String startPatternStr, String endPatternStr, File outputLogFile){
        this.outputLogFile = outputLogFile;
        this.startPatternStr = startPatternStr;
        this.endPatternStr = endPatternStr;
        Validate.notBlank(startPatternStr);
        Validate.notBlank(endPatternStr);
        Validate.notNull(this.outputLogFile);
        if(!this.outputLogFile.exists()){
            throw new RuntimeException("outputLogFile檔案不存在 : " + outputLogFile.getAbsolutePath());
        }
    }
    
    static class PairStartEnd {
        int startLine;
        int endLine;
    }
    
    public void execute(File file){
        try {
            Pattern startPattern = Pattern.compile(startPatternStr, Pattern.CASE_INSENSITIVE);
            Pattern endPattern = Pattern.compile(endPatternStr, Pattern.CASE_INSENSITIVE);
            Matcher matcher = null;
            LineNumberReader reader1 = new LineNumberReader(new InputStreamReader(new FileInputStream(file), readLogEncode));
            Set<PairStartEnd> pairPosList = new HashSet<PairStartEnd>();
            PairStartEnd tempPair = null;
            for(String line = null ; (line = reader1.readLine())!= null ;){
                matcher = startPattern.matcher(line);
                if(matcher.find()){
                    tempPair = new PairStartEnd();
                    tempPair.startLine = reader1.getLineNumber();
                }
                matcher = endPattern.matcher(line);
                if(tempPair != null && matcher.find()){
                    tempPair.endLine = reader1.getLineNumber();
                    pairPosList.add(tempPair);
                    tempPair = null;
                }
            }
            reader1.close();
            if(tempPair != null){
                tempPair.endLine = tempPair.startLine;
                pairPosList.add(tempPair);
            }
            Set<Integer> matchLineList = new TreeSet<Integer>();
            Set<Integer> matchLineListCopy = new TreeSet<Integer>();
            for(PairStartEnd pse : pairPosList){
                matchLineListCopy.add(pse.startLine);
                matchLineListCopy.add(pse.endLine);
                for(int ii = pse.startLine; ii <= pse.endLine ; ii ++){
                    matchLineList.add(ii);
                }
            }
            
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
