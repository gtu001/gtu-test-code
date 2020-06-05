package gtu._work;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import gtu.collection.ListUtil;
import gtu.file.FileReaderWriterUtil;
import gtu.file.FileUtil;
import gtu.html.util.HtmlUtil;

public class CodeFindRelative001 {

    File dir_path;
    String main_find_str;
    String subFileName;
    List<SecondFindDef> second_finds;
    boolean ignoreCase;
    String encoding;
    boolean isAnd;

    private Pair<List<Integer>, Map<SecondFindDef, List<Integer>>> checkFile(File file, String main_find_str, List<SecondFindDef> second_finds, boolean ignoreCase, String encoding) {
        main_find_str = getIgnoreCaseText(main_find_str, ignoreCase);
        main_find_str = StringUtils.trimToEmpty(main_find_str);

        Map<Integer, String> fileContent;
        Map<SecondFindDef, List<Integer>> secondFindsMap = new HashMap<SecondFindDef, List<Integer>>();
        List<Integer> masterLineNumberLst = new ArrayList<Integer>();

        for (int ii = 0; ii < second_finds.size(); ii++) {
            SecondFindDef v = second_finds.get(ii);
            secondFindsMap.put(v, null);
        }

        fileContent = FileUtil.loadFromFile_asMap(file, encoding);

        for (Integer lineNumber : fileContent.keySet()) {
            String line = fileContent.get(lineNumber);
            line = getIgnoreCaseText(line, ignoreCase);
            if (line.contains(main_find_str)) {
                if (second_finds.isEmpty()) {
                    masterLineNumberLst.add(lineNumber);
                } else {
                    for (int ii = 0; ii < second_finds.size(); ii++) {
                        SecondFindDef v = second_finds.get(ii);
                        List<Integer> matchLineLst = checkFileDetail(fileContent, lineNumber, v, ignoreCase);
                        if (!matchLineLst.isEmpty()) {
                            System.out.println("## findMatch : " + file + " --> lineNumber : " + matchLineLst);
                            secondFindsMap.put(v, matchLineLst);
                            masterLineNumberLst.add(lineNumber);
                        }
                    }
                }
            }
        }
        return Pair.of(masterLineNumberLst, secondFindsMap);
    }

    private List<Integer> checkFileDetail(Map<Integer, String> fileContent, int lineNumber, SecondFindDef mSecondFindDef, boolean ignoreCase) {
        List<Integer> matchLineLst = new ArrayList<Integer>();
        for (int num = lineNumber - mSecondFindDef.relativeLineNumber; num <= lineNumber + mSecondFindDef.relativeLineNumber; num++) {
            if (fileContent.containsKey(num)) {
                String line = fileContent.get(num);
                line = getIgnoreCaseText(line, ignoreCase);

                if (!mSecondFindDef.isRegex) {
                    String findStr = getIgnoreCaseText(mSecondFindDef.findStr, ignoreCase);
                    if (line.contains(findStr)) {
                        matchLineLst.add(num);
                    }
                } else {
                    Matcher mth = mSecondFindDef.findPtn.matcher(line);
                    if (mth.find()) {
                        matchLineLst.add(num);
                    }
                }
            }
        }
        return matchLineLst;
    }

    private boolean checkSecondFindsMapCondition(Map<SecondFindDef, List<Integer>> secondFindsMap, boolean isAnd) {
        if (secondFindsMap.isEmpty()) {
            return true;
        }
        List<Boolean> allLst = new ArrayList<Boolean>();
        for (SecondFindDef key : secondFindsMap.keySet()) {
            if (secondFindsMap.containsKey(key) && !secondFindsMap.get(key).isEmpty()) {
                allLst.add(true);
            } else {
                allLst.add(false);
            }
        }
        if (isAnd) {
            if (allLst.contains(false)) {
                return false;
            }
            return true;
        } else {
            if (allLst.contains(true)) {
                return true;
            }
            return false;
        }
    }

    public void execute(File dir_path, String main_find_str, String subFileName, List<SecondFindDef> second_finds, boolean ignoreCase, String encoding, boolean isAnd, int range_gap) {
        {
            this.dir_path = dir_path;
            this.main_find_str = main_find_str;
            this.subFileName = subFileName;
            this.second_finds = second_finds;
            this.ignoreCase = ignoreCase;
            this.encoding = encoding;
            this.isAnd = isAnd;
            CodeFindRelative001.range_gap = range_gap;
        }
        
        String filename = CodeFindRelative001.class.getSimpleName() + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".htm";
        File file = new File(FileUtil.DESKTOP_DIR, filename);
        log = FileReaderWriterUtil.WriterZ.newInstance(file, "UTF8");
        log.init();

        String filePattern = ".*";
        if (StringUtils.isNotBlank(subFileName)) {
            filePattern = ".*\\." + subFileName;
        }
        System.out.println("#### filePattern : " + filePattern);

        List<File> fileLst = new ArrayList<File>();
        FileUtil.searchFileMatchs(dir_path, filePattern, fileLst);

        log.writeLine("<html>");
        log.writeLine("<table border=0>");

        for (File f : fileLst) {
            System.out.println("start " + f);
            Pair<List<Integer>, Map<SecondFindDef, List<Integer>>> result = checkFile(f, main_find_str, second_finds, ignoreCase, encoding);
            List<Integer> masterLineNumberLst = result.getLeft();
            Map<SecondFindDef, List<Integer>> secondFindsMap = result.getRight();

            if (masterLineNumberLst.isEmpty()) {
                continue;
            }

            if (checkSecondFindsMapCondition(secondFindsMap, isAnd)) {
                // log.writeLine(f, secondFindsMap);
                log.writeLine("<tr><td colspan=10>&nbsp;</td></tr>");
                log.writeLine("<tr><td colspan=10><font style='background-color:#c7edcc'>" + f + "</font><font style='background-color:#e4c67b'>" + masterLineNumberLst + secondFindsMap.values()
                        + "</font></td></tr>");

                // # 紀錄檔案該行
                writeFilesRelativeLines(f, masterLineNumberLst, secondFindsMap, log, encoding);
            }
        }

        log.writeLine("</table>");
        log.writeLine("</html>");
        close();
    }

    private void close() {
        log.flush();
        log.close();
    }

    private void writeFilesRelativeLines(File file, List<Integer> masterLineNumberLst, Map<SecondFindDef, List<Integer>> secondFindsMap, FileReaderWriterUtil.WriterZ log, String encoding) {
        List<String> contentLst = FileUtil.loadFromFile_asList(file, encoding);
        TreeSet<Integer> showLineNumbers = new TreeSet<Integer>();
        Map<Integer, String> prefixMap = new HashMap<Integer, String>();
        for (int lineNumber : masterLineNumberLst) {
            List<Integer> lineNumberArry = ListUtil.range(lineNumber - range_gap, lineNumber + range_gap);
            showLineNumbers.addAll(lineNumberArry);
        }
        for (SecondFindDef k : secondFindsMap.keySet()) {
            List<Integer> lineLst = secondFindsMap.get(k);
            for (int lineNumber : lineLst) {
                List<Integer> lineNumberArry = ListUtil.range(lineNumber - range_gap, lineNumber + range_gap);
                showLineNumbers.addAll(lineNumberArry);
            }
        }
        for (int lineNumber : showLineNumbers) {
            if (masterLineNumberLst.contains(lineNumber)) {
                prefixMap.put(lineNumber, "M");// 主
            } else {
                for (SecondFindDef k : secondFindsMap.keySet()) {
                    List<Integer> lineLst = secondFindsMap.get(k);
                    if (lineLst.contains(lineNumber)) {
                        prefixMap.put(lineNumber, "S");// 次
                    }
                }
            }
        }
        for (int lineNumber : showLineNumbers) {
            if (lineNumber - 1 >= 0 && lineNumber - 1 < contentLst.size()) {
                String line = contentLst.get(lineNumber - 1);
                String prefix = StringUtils.trimToEmpty(prefixMap.get(lineNumber));
                if (StringUtils.isBlank(prefix)) {
                    prefix = " ";
                }
                // log.writeLine("\t" + prefix + "[" + lineNumber + "]" + line);
                log.writeLine("<tr><td>" + prefix + "</td><td>" + lineNumber + "</td><td>" + transferToHtml(prefix, line) + "</td></tr>");
            }
        }
    }

    private String transferToHtml(String prefix, String line) {
        if (StringUtils.isBlank(prefix)) {
            return line;
        }
        if ("M".equals(prefix)) {
            StringBuffer sb = new StringBuffer();
            Pattern ptn = Pattern.compile(Pattern.quote(main_find_str), Pattern.CASE_INSENSITIVE);
            Matcher mth = ptn.matcher(HtmlUtil.transferSpaceAndTab(line));
            while (mth.find()) {
                mth.appendReplacement(sb, "<font style='background-color:yellow'>" + mth.group() + "</font>");
            }
            mth.appendTail(sb);
            return sb.toString();
        } else {
            for (SecondFindDef sec : second_finds) {
                StringBuffer sb = new StringBuffer();
                if (sec.findPtn != null) {
                    Matcher mth = sec.findPtn.matcher(HtmlUtil.transferSpaceAndTab(line));
                    boolean findOk = false;
                    while (mth.find()) {
                        mth.appendReplacement(sb, "<font style='background-color:yellow'>" + mth.group() + "</font>");
                        findOk = true;
                    }
                    mth.appendTail(sb);
                    if (findOk) {
                        return sb.toString();
                    }
                } else {
                    Pattern ptn = Pattern.compile(Pattern.quote(sec.findStr), Pattern.CASE_INSENSITIVE);
                    Matcher mth = ptn.matcher(HtmlUtil.transferSpaceAndTab(line));
                    boolean findOk = false;
                    while (mth.find()) {
                        mth.appendReplacement(sb, "<font style='background-color:yellow'>" + mth.group() + "</font>");
                        findOk = true;
                    }
                    mth.appendTail(sb);
                    if (findOk) {
                        return sb.toString();
                    }
                }
            }
        }
        return line;
    }

    private String getIgnoreCaseText(String strVal, boolean ignoreCase) {
        if (ignoreCase) {
            return strVal.toLowerCase();
        }
        return strVal;
    }

    public static class SecondFindDef {
        String findStr;
        int relativeLineNumber;
        boolean isRegex;
        Pattern findPtn;

        public SecondFindDef(String findStr, int relativeLineNumber, boolean isRegex, boolean ignoreCase) {
            this.findStr = findStr;
            this.relativeLineNumber = relativeLineNumber;
            this.isRegex = isRegex;
            if (isRegex) {
                int flag = 0;
                if (ignoreCase) {
                    flag |= Pattern.CASE_INSENSITIVE;
                }
                findPtn = Pattern.compile(findStr, flag);
            }
        }
    }

    private static FileReaderWriterUtil.WriterZ log;
    private static int range_gap = 2;

    public static void main(String[] args) {
        CodeFindRelative001 t = new CodeFindRelative001();

        File dir_path = new File("D:\\workstuff\\gtu-test-code");
        String main_find_str = "log";
        String subFileName = "(java|sql)";
        boolean ignoreCase = true;
        range_gap = 2;

        List<SecondFindDef> second_finds = new ArrayList<SecondFindDef>();
        second_finds.add(new SecondFindDef("file", 3, false, ignoreCase));

        String encoding = "UTF8";
        boolean isAnd = true;

        t.execute(dir_path, main_find_str, subFileName, second_finds, ignoreCase, encoding, isAnd, range_gap);
        System.out.println("done...");
    }

}
