package gtu._work;

import gtu.file.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class AVPornCheckDuplicate {

    public static void main(String[] args) {
        List<File> fileList = new ArrayList<File>();
        FileUtil.searchFilefind(new File("D:\\迅雷游戏"), ".*", fileList);
        FileUtil.searchFilefind(new File("F:\\AV"), ".*", fileList);
        
        List<AVData> avSet = new ArrayList<AVData>();
        for(File f : fileList){
            if(StringUtils.indexOfAny(FileUtil.getSubName(f).toLowerCase(), new String[]{"jpg","jpeg","rar"}) != -1){
                continue;
            }
            AVData av = getAV(f);
            if(avSet.contains(av)){
                System.out.println(avSet.get(avSet.indexOf(av)).file+"\t==========");
                System.out.println(f);
            }else{
                avSet.add(av);
            }
        }
        System.out.println("done...");
    }
    
    private static AVData getAV(File file){
        String name = FileUtil.getNameNoSubName(file);
        AVData av = new AVData();
        Pattern ptn1 = Pattern.compile("[a-zA-Z]+");
        Matcher mth1 = ptn1.matcher(name);
        while(mth1.find()){
            if(mth1.group().length() == 1){
                continue;
            }
            av.dataList.add(mth1.group().toUpperCase());
        }
        Pattern ptn2 = Pattern.compile("\\d+");
        mth1 = ptn2.matcher(name);
        while(mth1.find()){
            if(mth1.group().length() == 1){
                continue;
            }
            av.numList.add(Long.parseLong(mth1.group()));
        }
        av.file = file;
        return av;
    }
    
    static class AVData {
        List<String> dataList = new ArrayList<String>();
        List<Long> numList = new ArrayList<Long>();
        File file;
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((dataList == null) ? 0 : dataList.hashCode());
            result = prime * result + ((file == null) ? 0 : file.hashCode());
            result = prime * result + ((numList == null) ? 0 : numList.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            AVData av = (AVData)obj;
            boolean endFindOk = false;
            boolean numFindOk = false;
            String[] dList = av.dataList.toArray(new String[0]);
            for(String str : dataList){
                if(StringUtils.indexOfAny(str, dList) != -1){
                    endFindOk = true;
                    break;
                }
            }
            for(long dd : numList){
                for(long yy : av.numList){
                    if(dd == yy){
                        numFindOk = true;
                        break;
                    }
                }
            }
            if(endFindOk && numFindOk){
                return true;
            }
            return false;
        }
    }
}
