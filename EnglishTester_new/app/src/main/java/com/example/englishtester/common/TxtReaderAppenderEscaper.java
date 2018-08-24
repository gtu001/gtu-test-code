package com.example.englishtester.common;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.example.englishtester.common.html.image.IImageLoaderCandidate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wistronits on 2018/8/24.
 */

public class TxtReaderAppenderEscaper {

    String orignText;
    String resultText;

    public TxtReaderAppenderEscaper(String orignText) {
        this.orignText = orignText;

        Pattern ptn = Pattern.compile("\\{\\{(.|\n)*?\\}\\}", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher mth = ptn.matcher(orignText);
        StringBuffer sb = new StringBuffer();
        A:
        while (mth.find()) {
            String text = mth.group();
            for (ReplaceBack e : ReplaceBack.values()) {
                if (e.isMatch(text)) {
                    text = e.replace(text);
                    mth.appendReplacement(sb, text);
                    continue A;
                }
            }
        }
        mth.appendTail(sb);

        this.resultText = sb.toString();
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
