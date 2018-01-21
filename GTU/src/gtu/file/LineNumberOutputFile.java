package gtu.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

public class LineNumberOutputFile {
    
    Pattern fileNamePattern = Pattern.compile("(.*)\\_(\\d+)\\.(\\w+)");
    File file;
    int currentLine = 0;
    BufferedWriter writer;
    int maxLineCount = 2500;
    
    Set<File> outputFileList = new LinkedHashSet<File>();

    public LineNumberOutputFile(File file, int maxLineCount) throws UnsupportedEncodingException, FileNotFoundException {
        Validate.notNull(file, "檔案為null");
        Validate.isTrue(fileNamePattern.matcher(file.getName()).matches(), "檔名格式不符,必須為:.*\\_\\d+\\.\\w+");
        this.file = file;
        this.maxLineCount = maxLineCount;
    }
    
    public LineNumberOutputFile(File file) throws UnsupportedEncodingException, FileNotFoundException {
        this(file, 2500);
    }

    public void writeLine(String message) throws IOException {
        if(writer == null){
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf8"));
            outputFileList.add(file);
        }
        if (StringUtils.isBlank(message)) {
            return;
        }
        int changeLineCount = StringUtils.countMatches(message, "\n");
        if (changeLineCount == 0) {
            changeLineCount = 1;
        }
        if (currentLine > maxLineCount) {
            currentLine = changeLineCount;
            writer.flush();
            writer.close();
            Matcher matcher = fileNamePattern.matcher(file.getName());
            boolean fileNameOk = matcher.matches();
            if (!fileNameOk) {
                throw new RuntimeException("檔名錯誤:" + file.getName());
            }
            String fileName = matcher.group(1) + "_" + (Integer.parseInt(matcher.group(2)) + 1) + "."
                    + matcher.group(3);
            file = new File(file.getParentFile(), fileName);
            outputFileList.add(file);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf8"));
            writer.write(message);
            writer.newLine();
        } else {
            currentLine += changeLineCount;
            writer.write(message);
            writer.newLine();
        }
    }

    public void close() throws IOException {
        if(writer != null){
            writer.flush();
            writer.close();
        }
    }
}