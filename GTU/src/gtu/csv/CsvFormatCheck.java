package com.ibt.dcs.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CsvFormatCheck {

    public static void main(String[] args) {
        System.out.println(CsvFormatCheck.checkFormat(new File("C:/Users/gtu001_5F/Desktop/O.BNK.B.20160601.005.csv")));
    }
    
    private static final CsvFormatCheck _INST = new CsvFormatCheck();
    public static String checkFormat(File file){
        return _INST.checkFormatMain(file);
    }

    public String checkFormatMain(File file){
        LineNumberReader reader = null;
        try{
            List<String> errList = new ArrayList<String>();
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "utf8"));
            for(String line = null; (line = reader.readLine())!=null;){
                ErrInfo errInfo = checkFormatDetail(defaultString(line));
                if(errInfo!=null){
                    errList.add("第" + reader.getLineNumber() + "行的第" + (errInfo.errIndex + 1) + "個欄位 [" + errInfo.columnValue + "]有誤!");
                }
            }
            if(!errList.isEmpty()){
                StringBuilder sb = new StringBuilder();
                for(String s : errList){
                    sb.append(s + ",");
                }
                sb.deleteCharAt(sb.length()-1);
                return sb.toString();
            }
            return "";
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }finally{
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
    }
    
    private String defaultString(String value){
        if(value == null){
            return "";
        }
        return value;
    }
    
    private ErrInfo checkFormatDetail(String line){
        if(checkSimpleFormat(line) != null){
            ErrInfo errInfo = checkComplexFormat(line);
            if(errInfo != null){
                return errInfo;
            }
        }
        return null;
    }
    
    private ErrInfo checkSimpleFormat(String line){
        String[] arry = line.split(",", -1);
        for(int ii = 0 ; ii < arry.length; ii ++){
            String str = defaultString(arry[ii]);
            if(!str.matches("^\".*\"$")){
                return new ErrInfo(ii, str);
            }
        }
        return null;
    }
    
    private ErrInfo checkComplexFormat(String line){
        Pattern ptn = Pattern.compile("\".*?\"\\,?");
        Matcher mth = ptn.matcher(line);
        String tmpValue = null;
        int colIndex = 0;
        while(mth.find()){
            if(mth.start() != 0){
                if(tmpValue == null){
                    try{
                        tmpValue = line.substring(0, line.indexOf("\"") + 1);
                    }catch(Exception ex){
                    }
                }
                return new ErrInfo(colIndex, tmpValue);
            }
            colIndex ++;
            tmpValue = line.substring(mth.start(), mth.end());
            line = line.substring(mth.end(), line.length() -1);
            mth = ptn.matcher(line);
        }
        if(line != null && line.length() != 0){
            return new ErrInfo(colIndex, line);
        }
        return null;
    }
    
    private static class ErrInfo {
        int errIndex;
        String columnValue;
        ErrInfo(int errIndex, String columnValue){
            this.errIndex = errIndex;
            this.columnValue = columnValue;
        }
    }
}
