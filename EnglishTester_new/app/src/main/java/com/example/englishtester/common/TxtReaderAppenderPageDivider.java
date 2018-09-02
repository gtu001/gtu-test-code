package com.example.englishtester.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gtu001 on 2018/8/11.
 */

public class TxtReaderAppenderPageDivider {

    private static final TxtReaderAppenderPageDivider _INST = new TxtReaderAppenderPageDivider();

    private static final int PAGE_SIZE = 2000;

    private TxtReaderAppenderPageDivider() {
    }

    public static TxtReaderAppenderPageDivider getInst() {
        return _INST;
    }

    public Pair<List<String>, List<String>> getPages(String txtContent) {
        SectionChecker sc = new SectionChecker(txtContent);
        sc.processPages();
        return Pair.of(sc.pageLst, sc.page4TransalteLst);
    }

    private String getTranslateText(String text) {
        TxtReaderAppenderEscaper escaper = new TxtReaderAppenderEscaper(text);
        return escaper.getResult();
    }

    private class SectionChecker {
        private String txtContent;

        List<String> pageLst = new ArrayList<>();
        List<String> page4TransalteLst = new ArrayList<String>();

        private SectionChecker(String txtContent) {
            this.txtContent = txtContent;
        }

        Pattern ptn = Pattern.compile("(\n|\\}\\})", Pattern.DOTALL | Pattern.MULTILINE);

        private void processPages() {
            int startPos = 0;
            Matcher mth = ptn.matcher(txtContent);
            while (mth.find()) {
                if (mth.end() - startPos > PAGE_SIZE) {
                    String tmpContent = txtContent.substring(startPos, mth.end());
                    if (StringUtils.countMatches(tmpContent, "{{") != StringUtils.countMatches(tmpContent, "}}")) {
                        continue;
                    }

                    pageLst.add(tmpContent);
                    page4TransalteLst.add(getTranslateText(tmpContent));

                    startPos = mth.end();
                }
            }
            pageLst.add(txtContent.substring(startPos, txtContent.length()));
            page4TransalteLst.add(getTranslateText(txtContent.substring(startPos, txtContent.length())));
        }
    }
}
