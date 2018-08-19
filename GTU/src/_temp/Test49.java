package _temp;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import gtu.file.FileUtil;
import gtu.string.StringUtil_;

public class Test49 {

    public static void main(String[] args) throws InterruptedException, IOException {
        Test49 t = new Test49();

        String content = FileUtil.loadFromFile(new File("C:/Users/gtu00/OneDrive/Desktop/vvvvvvvvvvvvvvvvvv.txt"), "UTF8");
        String result = t._step1_fontSize_Indicate_4PTag(content, false);

        System.out.println(result);
        System.out.println("done...");
    }

    protected String _step1_fontSize_Indicate_4PTag(String content, boolean isPure) {
        Pattern ptn = Pattern.compile("\\<p(?:.|\n)*?\\<\\/p\\>", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher mth = ptn.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (mth.find()) {
            String spanContent = mth.group();
            spanContent = FontSizeIndicateEnum.P001.apply(spanContent, isPure);
            spanContent = StringUtil_.appendReplacementEscape(spanContent);
            mth.appendReplacement(sb, spanContent);
        }
        mth.appendTail(sb);
        return sb.toString();
    }

    protected enum FontSizeIndicateEnum {
        SPAN001("\\<span(?:.|\n)*?font\\-size\\:([\\d\\.]+)pt\\;(?:.|\n)*?\\>((?:.|\n)*?)\\<\\/span\\>"), //
        P001("\\<p(?:.|\\n)*?font\\-size\\:\\s*?([\\d\\.]+)(?:px|pt)(?:.|\\n)*?\\>(.*?)\\<\\/p\\>"),//
        ;

        final Pattern ptn;

        FontSizeIndicateEnum(String ptnStr) {
            ptn = Pattern.compile(ptnStr, Pattern.DOTALL | Pattern.MULTILINE);
        }

        String apply(String content, boolean isPure) {
            StringBuffer sb = new StringBuffer();
            Matcher mth = ptn.matcher(content);
            while (mth.find()) {
                String size = mth.group(1);
                String text = mth.group(2);
                String tmpVal = "";
                if (StringUtils.isNotBlank(size) && StringUtils.isNotBlank(text)) {
                    tmpVal = "{{font size:" + size + ",text:" + StringUtil_.appendReplacementEscape(text) + "}}";
                    if (isPure) {
                        tmpVal = StringUtil_.appendReplacementEscape(text);
                    }
                }
                mth.appendReplacement(sb, tmpVal);
            }
            mth.appendTail(sb);
            return sb.toString();
        }
    }
}
