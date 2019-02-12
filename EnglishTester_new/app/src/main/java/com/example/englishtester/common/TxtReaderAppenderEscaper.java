package com.example.englishtester.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cglib.core.internal.Function;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.util.StringUtil_;

/**
 * Created by wistronits on 2018/8/24.
 */

public class TxtReaderAppenderEscaper {

    private static final String TAG = TxtReaderAppenderEscaper.class.getSimpleName();

    String orignText;
    String resultText;
    TreeMap<Pair<Integer, Integer>, String> groupMap = new TreeMap<Pair<Integer, Integer>, String>();
    char[] orignTextArry;

    public TxtReaderAppenderEscaper(String orignText) {
        this.orignText = orignText;
        this.orignTextArry = this.orignText.toCharArray();

        Pattern ptn = Pattern.compile("\\{\\{(.|\n)*?\\}\\}", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher mth = ptn.matcher(orignText);
        StringBuffer sb = new StringBuffer();
        A:
        while (mth.find()) {
            String text = mth.group();
            for (ReplaceBack e : ReplaceBack.values()) {
                if (e.isMatch(text)) {
                    text = e.replace(text);
                    text = StringUtil_.appendReplacementEscape(text);
                    mth.appendReplacement(sb, text);
                    groupMap.put(Pair.of(mth.start(), mth.end()), text);
                    continue A;
                }
            }
        }
        mth.appendTail(sb);

        this.resultText = sb.toString();
    }

    public String getSentance(int start, int end) {
        LinkedList<String> prefixLst = new LinkedList<String>();

        Function<Integer, Pair<Integer, Integer>> cal = new Function<Integer, Pair<Integer, Integer>>() {
            @Override
            public Pair<Integer, Integer> apply(Integer integer) {
                int pairLength = Integer.MAX_VALUE;
                Pair<Integer, Integer> rtnPair = null;
                for (Pair<Integer, Integer> p : groupMap.keySet()) {
                    if (integer >= p.getLeft() && integer <= p.getRight()) {
                        int tmpLength = p.getRight() - p.getLeft();
                        if (tmpLength <= pairLength) {
                            pairLength = tmpLength;
                            rtnPair = p;
                        }
                    }
                }
                return rtnPair;
            }
        };

        A:
        for (int ii = (start > 0 ? start - 1 : start); ii > 0; ii--) {
            boolean isMatch = false;
            /*
            for (Pair<Integer, Integer> p : groupMap.keySet()) {
                if (ii >= p.getLeft() && ii <= p.getRight()) {
                    isMatch = true;
                    prefixLst.push(groupMap.get(p));
                    ii = p.getLeft();
                }
            }
            */
            Pair matchPair = cal.apply(ii);
            if(matchPair != null){
                prefixLst.push(groupMap.get(matchPair));
                ii = (Integer)matchPair.getLeft();
                isMatch = true;
            }
            if (!isMatch) {
                if (orignTextArry[ii] == '.') {
                    break A;
                }
                prefixLst.push(String.valueOf(orignTextArry[ii]));
            }
        }

        List<String> suffixLst = new ArrayList<String>();
        A:
        for (int ii = end; ii < orignTextArry.length; ii++) {
            boolean isMatch = false;
            /*
            for (Pair<Integer, Integer> p : groupMap.keySet()) {
                if (ii >= p.getLeft() && ii <= p.getRight()) {
                    isMatch = true;
                    suffixLst.add(groupMap.get(p));
                    ii = p.getRight();
                }
            }
            */
            Pair matchPair = cal.apply(ii);
            if(matchPair != null){
                prefixLst.push(groupMap.get(matchPair));
                ii = (Integer)matchPair.getRight();
                isMatch = true;
            }
            if (!isMatch) {
                if (orignTextArry[ii] == '.') {
                    break A;
                }
                suffixLst.add(String.valueOf(orignTextArry[ii]));
            }
        }

        String group = StringUtils.substring(orignText, start, end);
        return new StringBuilder(StringUtils.join(prefixLst, "")).append(group).append(StringUtils.join(suffixLst, "")).toString();
    }

    private enum ReplaceBack {
        H_TITLE("{{h ", "h\\ssize\\:(\\d+?),text\\:((?:.|\n)*?)\\}\\}") {
            public String replace(String text, Matcher mth) {
                return mth.group(2);
            }
        },//
        BOLD("{{b:", "b\\:((?:.|\n)*)\\}\\}") {
            public String replace(String text, Matcher mth) {
                return mth.group(1);
            }
        },//
        ITALIC("{{i:", "i\\:((?:.|\n)*)\\}\\}") {
            public String replace(String text, Matcher mth) {
                return mth.group(1);
            }
        },//
        STRONG("{{strong:", "strong\\:((?:.|\n)*)\\}\\}") {
            public String replace(String text, Matcher mth) {
                return mth.group(1);
            }
        },//
        IMG("{{img ", "img\\ssrc\\:((?:.|\n)*?)\\,alt\\:((?:.|\n)*)\\}\\}") {
            public String replace(String text, Matcher mth) {
                return "";
            }
        },//
        LINK("{{link:", "link\\:((?:.|\n)*?),value\\:((?:.|\n)*)\\}\\}") {
            public String replace(String text, Matcher mth) {
                return mth.group(2);
            }
        },//
        FONT("{{font ", "font\\ssize\\:((?:.|\n)*?),text\\:((?:.|\n)*)\\}\\}") {
            public String replace(String text, Matcher mth) {
                return mth.group(2);
            }
        },//
        PRE("{{pre:", "pre\\:((?:.|\n)*)\\}\\}") {
            public String replace(String text, Matcher mth) {
                return mth.group(1);
            }
        },//
        CODE("{{code:", "code\\:((?:.|\n)*)\\}\\}") {
            public String replace(String text, Matcher mth) {
                return mth.group(1);
            }
        },//
        ;

        final String startWith;
        final String patternStr;

        ReplaceBack(String startWith, String patternStr) {
            this.startWith = startWith;
            this.patternStr = patternStr;
        }

        public String replace(String text) {
            Pattern ptn = Pattern.compile(patternStr, Pattern.MULTILINE | Pattern.DOTALL);
            Matcher mth = ptn.matcher(text);
            if (mth.find()) {
                return this.replace(text, mth);
            }
            return text;
        }

        abstract String replace(String text, Matcher mth);

        public boolean isMatch(String text) {
            if (StringUtils.trimToEmpty(text).startsWith(startWith)) {
                return true;
            }
            return false;
        }
    }

    public String getResult() {
        return this.resultText;
    }
}
