package com.example.englishtester.common;

import org.apache.commons.lang3.StringUtils;

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

    public List<String> getPages(String txtContent) {
        SectionChecker sc = new SectionChecker(txtContent);
        sc.processPages();
        return sc.pageLst;
    }

    private class SectionChecker {
        private String txtContent;

        List<String> pageLst = new ArrayList<>();

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
                    startPos = mth.end();
                }
            }
            pageLst.add(txtContent.substring(startPos, txtContent.length()));
        }
    }
}
