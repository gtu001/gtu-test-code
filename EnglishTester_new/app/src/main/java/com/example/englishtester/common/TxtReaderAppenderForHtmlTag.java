package com.example.englishtester.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import com.example.englishtester.R;
import com.example.englishtester.TxtReaderActivity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gtu001 on 2018/7/15.
 */

public class TxtReaderAppenderForHtmlTag {

    private static final String TAG = TxtReaderAppenderForHtmlTag.class.getSimpleName();

    private static final Pattern START = Pattern.compile("\\{\\{", Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern END = Pattern.compile("\\}\\}", Pattern.MULTILINE | Pattern.DOTALL);
    private static final String START_TAG = "{{";
    private static final String END_TAG = "}}";

    private String txtContent;//
    private SpannableString ss;//
    private int maxPicWidth;//
    private List<Pair<Integer, Integer>> normalIgnoreLst;//
    private Bitmap hyperlink;//
    private Context context;
    private TxtReaderActivity.TxtReaderActivityDTO dto;
    private OnlinePicLoader onlinePicLoader;

    public TxtReaderAppenderForHtmlTag(//
                                       String txtContent,//
                                       SpannableString ss,//
                                       int maxPicWidth,//
                                       List<Pair<Integer, Integer>> normalIgnoreLst,//
                                       TxtReaderActivity.TxtReaderActivityDTO dto,//
                                       Context context//
    ) {
        this.txtContent = txtContent;
        this.ss = ss;
        this.maxPicWidth = maxPicWidth;
        this.normalIgnoreLst = normalIgnoreLst;
        this.context = context;
        this.dto = dto;
        this.onlinePicLoader = new OnlinePicLoader(context);
    }

    public void apply() {
        List<Pair<Integer, Integer>> tagLst = new TagPairGetter().get(txtContent);

        for (Pair<Integer, Integer> tag : tagLst) {
            String txt = txtContent.substring(tag.getLeft(), tag.getRight());

            for (ContentDefineEnum e : ContentDefineEnum.values()) {
                if (txt.startsWith(e.startWtih)) {
                    Matcher mth = e.ptn.matcher(txt);
                    if (mth.find()) {
                        e.apply(tag, mth, this);
                    } else {
                        throw new RuntimeException("無法match設定 : " + e + " --> " + txt);
                    }
                }
            }
        }
    }

    private static class __SpecialTagHolder_Pos {
        int start = -1;
        int end = -1;
        String content;

        __SpecialTagHolder_Pos(Pair<Integer, Integer> pair, Matcher mth, int groupIndex) {
            start = pair.getLeft() + mth.start(groupIndex);
            end = pair.getLeft() + mth.end(groupIndex);
            content = mth.group(groupIndex);
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "Pos{" +
                    "start=" + start +
                    ", end=" + end +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

    private enum ContentDefineEnum {
        H_TITLE("{{h ", "h\\ssize\\:(\\d+?),text\\:((?:.|\n)*?)\\}\\}") {
            @Override
            void apply(Pair<Integer, Integer> pair, Matcher mth, TxtReaderAppenderForHtmlTag self) {
                __SpecialTagHolder_Pos text = new __SpecialTagHolder_Pos(pair, mth, 1);
                __SpecialTagHolder_Pos size = new __SpecialTagHolder_Pos(pair, mth, 2);

                log(size);
                log(text);
                float fixSize = getFontSizeScale(size.getContent());

                self.hiddenSpan(self.ss, self.getPairStart(pair), text.getStart());
                self.hiddenSpan(self.ss, text.getEnd(), self.getPairEnd(pair));

                self.ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), text.getStart(), text.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                self.ss.setSpan(new RelativeSizeSpan(fixSize), text.getStart(), text.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        },//
        BOLD("{{b:", "b\\:((?:.|\n)*)\\}\\}") {
            @Override
            void apply(Pair<Integer, Integer> pair, Matcher mth, TxtReaderAppenderForHtmlTag self) {
                __SpecialTagHolder_Pos proc = new __SpecialTagHolder_Pos(pair, mth, 1);
                log(proc);

                self.hiddenSpan(self.ss, self.getPairStart(pair), proc.getStart());
                self.hiddenSpan(self.ss, proc.getEnd(), self.getPairEnd(pair));

                self.ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), proc.getStart(), proc.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                self.ss.setSpan(new RelativeSizeSpan(1.25f), proc.getStart(), proc.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        },//
        ITALIC("{{i:", "i\\:((?:.|\n)*)\\}\\}") {
            @Override
            void apply(Pair<Integer, Integer> pair, Matcher mth, TxtReaderAppenderForHtmlTag self) {
                __SpecialTagHolder_Pos proc = new __SpecialTagHolder_Pos(pair, mth, 1);
                log(proc);

                self.hiddenSpan(self.ss, self.getPairStart(pair), proc.getStart());
                self.hiddenSpan(self.ss, proc.getEnd(), self.getPairEnd(pair));

                self.ss.setSpan(new StyleSpan(Typeface.ITALIC), proc.getStart(), proc.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                self.ss.setSpan(new RelativeSizeSpan(1.25f), proc.getStart(), proc.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        },//
        STRONG("{{strong:", "strong\\:((?:.|\n)*)\\}\\}") {
            @Override
            void apply(Pair<Integer, Integer> pair, Matcher mth, TxtReaderAppenderForHtmlTag self) {
                __SpecialTagHolder_Pos proc = new __SpecialTagHolder_Pos(pair, mth, 1);
                log(proc);

                self.hiddenSpan(self.ss, self.getPairStart(pair), proc.getStart());
                self.hiddenSpan(self.ss, proc.getEnd(), self.getPairEnd(pair));

                self.ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), proc.getStart(), proc.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                self.ss.setSpan(new RelativeSizeSpan(1.1f), proc.getStart(), proc.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        },//
        IMG("{{img ", "img\\ssrc\\:((?:.|\n)*?)\\,alt\\:((?:.|\n)*)\\}\\}") {
            @Override
            void apply(Pair<Integer, Integer> pair, Matcher mth, TxtReaderAppenderForHtmlTag self) {
                __SpecialTagHolder_Pos srcData = new __SpecialTagHolder_Pos(pair, mth, 1);
                __SpecialTagHolder_Pos altData = new __SpecialTagHolder_Pos(pair, mth, 2);

                log("IMG ADD ::: " + pair + " --- " + self.txtContent.substring(pair.getLeft(), pair.getRight()));

//                log("src -> " + srcData);
//                log("alt -> " + altData);

                ImageLoaderCandidate picProcess = self.new ImageLoaderCandidate(srcData.getContent(), altData.getContent());

                self.hiddenSpan(self.ss, self.getPairStart(pair), altData.getStart());
                self.hiddenSpan(self.ss, altData.getEnd(), self.getPairEnd(pair));

                if (!picProcess.isGifFile) {
                    Bitmap smiley = null;
                    try {
                        smiley = OOMHandler.fixPicScaleFixScreenWidth(picProcess.getResult(), self.maxPicWidth);
                    } catch (Throwable ex) {
                        Log.e(TAG, "OOM " + ex.getMessage(), ex);
                        smiley = self.onlinePicLoader.getNotfound404();
                    }
                    self.ss.setSpan(new ImageSpan(self.context, smiley, ImageSpan.ALIGN_BASELINE), altData.getStart(), altData.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (picProcess.localFile != null) {
                    ImageSpan imageSpan = GifSpanCreater.getGifSpan(picProcess.localFile, self.dto.getTxtView(), self.resetScaleAction);
                    self.ss.setSpan(imageSpan, altData.getStart(), altData.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        },//
        LINK("{{link:", "link\\:((?:.|\n)*?),value\\:((?:.|\n)*)\\}\\}") {
            @Override
            void apply(Pair<Integer, Integer> pair, Matcher mth, TxtReaderAppenderForHtmlTag self) {
                final __SpecialTagHolder_Pos linkUrl = new __SpecialTagHolder_Pos(pair, mth, 1);
                final __SpecialTagHolder_Pos linkLabel = new __SpecialTagHolder_Pos(pair, mth, 2);

                TxtReaderAppender.SimpleUrlLinkSpan hrefLinkSpan = new TxtReaderAppender.SimpleUrlLinkSpan(self.context, linkUrl.getContent());
                log(linkUrl);

                //長度太長的link
                if (!self.isHyperLinkTooLong(linkLabel.getContent())) {
                    self.appendNormalIgnoreLst(pair);

                    self.hiddenSpan(self.ss, self.getPairStart(pair), linkLabel.getStart());
                    self.hiddenSpan(self.ss, linkLabel.getEnd(), self.getPairEnd(pair));

                    self.ss.setSpan(hrefLinkSpan, linkLabel.getStart(), linkLabel.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    self.appendNormalIgnoreLst(self.getPairStart(pair), linkLabel.getStart());
                    self.appendNormalIgnoreLst(linkLabel.getEnd(), self.getPairEnd(pair));

                    self.hiddenSpan(self.ss, self.getPairStart(pair), linkUrl.getStart());
                    self.hiddenSpan(self.ss, linkUrl.getEnd(), linkLabel.getStart());
                    self.hiddenSpan(self.ss, linkLabel.getEnd(), self.getPairEnd(pair));

                    self.ss.setSpan(self.createHyperlinkImageSpan(), linkUrl.getStart(), linkUrl.getEnd(), Spannable.SPAN_POINT_POINT);
                    self.ss.setSpan(hrefLinkSpan, linkUrl.getStart(), linkUrl.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        },//
        FONT("{{font ", "font\\ssize\\:((?:.|\n)*?),text\\:((?:.|\n)*)\\}\\}") {
            @Override
            void apply(Pair<Integer, Integer> pair, Matcher mth, TxtReaderAppenderForHtmlTag self) {
                final __SpecialTagHolder_Pos size = new __SpecialTagHolder_Pos(pair, mth, 1);
                final __SpecialTagHolder_Pos text = new __SpecialTagHolder_Pos(pair, mth, 2);

                float fixSize = 1f;
                try {
                    float indicateSize = Float.parseFloat(size.getContent());
                    fixSize = indicateSize / 16f;
                } catch (Exception ex) {
                    Log.e(TAG, "Font Size Reset ERR : " + ex.getMessage(), ex);
                }

                final int LEN = 0;
                self.hiddenSpan(self.ss, self.getPairStart(pair), text.getStart() - LEN);
                self.hiddenSpan(self.ss, text.getEnd() + LEN, self.getPairEnd(pair));

                if (Math.abs(fixSize - 1) > 0.1f) {
                    log("SizeScale : " + fixSize + " - " + text);
                    self.ss.setSpan(new RelativeSizeSpan(fixSize), text.getStart(), text.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    log("Intact !");
                }
            }
        },//
        ;

        final Pattern ptn;
        final String startWtih;

        ContentDefineEnum(String startWtih, String ptnStr) {
            this.startWtih = startWtih;
            ptn = Pattern.compile(ptnStr, Pattern.DOTALL | Pattern.MULTILINE);
        }

        void log(Object message) {
            Log.v(TAG, String.format("[%s] %s", this.name(), message));
        }

        abstract void apply(Pair<Integer, Integer> pair, Matcher mth, TxtReaderAppenderForHtmlTag self);

        float getFontSizeScale(String hrStrContent) {
            float fixSize = 1f;
            switch (hrStrContent) {
                case "1":
                    fixSize = 1.15f;
                    break;
                case "2":
                    fixSize = 1.25f;
                    break;
                case "3":
                    fixSize = 1.35f;
                    break;
                case "4":
                    fixSize = 1.45f;
                    break;
                case "5":
                    fixSize = 1.55f;
                    break;
                case "6":
                    fixSize = 1.65f;
                    break;
            }
            return fixSize;
        }
    }


    private class TagPairGetter {
        private final String TAG = TagPairGetter.class.getSimpleName();

        public List<Pair<Integer, Integer>> get(String txtContent) {
            Stack<Integer> startStk = getPosStack(START, txtContent, true);
            Stack<Integer> endStk = getPosStack(END, txtContent, false);
            List<Pair<Integer, Integer>> lst = new ArrayList<Pair<Integer, Integer>>();
            int errorCount = 0;
            for (int ii = 0; ii < endStk.size(); ii++) {
                int endPos = endStk.get(ii);
                int startPos = findMatchStartTag(startStk, endPos);

                if (startPos < 0 || endPos < 0) {
                    String errorStr = txtContent.substring(fixPos(startPos, txtContent), fixPos(endPos, txtContent));
                    Log.e(TAG, "###### ERROR Pos : " + startPos + ", " + endPos + " -> " + errorStr);
                    errorCount++;
                    continue;
                } else {
//                String errorStr = txtContent.substring(fixPos(startPos, txtContent), fixPos(endPos, txtContent));
//                Log.e(TAG, "###### OK Pos : " + startPos + ", " + endPos + " -> " + errorStr);
                }

                lst.add(Pair.of(startPos, endPos));
            }
            Log.v(TAG, "###### ERROR Count : " + errorCount);
            return lst;
        }

        private int fixPos(int value, String txtContent) {
            if (value < 0) {
                return 0;
            }
            if (value >= txtContent.length()) {
                return txtContent.length() - 1;
            }
            return value;
        }

        private int findMatchStartTag(Stack<Integer> startStk, int endTagPos) {
            int tmpPos = -1;
            for (int ii = 0; ii < startStk.size(); ii++) {
                int startPos = startStk.get(ii);
                if (startPos < endTagPos) {
                    tmpPos = startPos;
                } else {
                    break;
                }
            }
            if (tmpPos != -1) {
                startStk.removeElement(tmpPos);
            }
            return tmpPos;
        }

        private Stack<Integer> getPosStack(Pattern ptn, String txtContent, boolean isStart) {
            Stack<Integer> stk = new Stack<Integer>();
            Matcher mth = ptn.matcher(txtContent);
            while (mth.find()) {
                if (isStart) {
                    stk.push(mth.start());
                } else {
                    stk.push(mth.end());
                }
            }
            return stk;
        }
    }

    private class ImageLoaderCandidate {
        String srcData;
        String altData;
        boolean isGifFile;
        File localFile;

        ImageLoaderCandidate(String srcData, String altData) {
            this.srcData = srcData;
            this.altData = altData;
            this.isGifFile = isGif(srcData);
            try {
                this.localFile = getLocalFile(srcData);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }
        }

        private File getLocalFile(String filename) {
            try {
                String realName = URLDecoder.decode(filename, WordHtmlParser.WORD_HTML_ENCODE);
                File localPicFile = new File(dto.getCurrentHtmlFile().getParentFile(), realName);
                if (localPicFile.exists() && localPicFile.canRead()) {
                    return localPicFile;
                }

                if (realName.contains("/")) {
                    realName = realName.substring(realName.lastIndexOf("/"));
                }
                File dropboxPic = new File(dto.getDropboxPicDir(), realName);
                if (dropboxPic.exists() && dropboxPic.canRead()) {
                    return dropboxPic;
                }

                throw new Exception("查無檔案 : " + filename);
            } catch (Exception ex) {
                throw new RuntimeException("getLocalFile ERR : " + ex.getMessage(), ex);
            }
        }

        private boolean isGif(String srcData) {
            if (StringUtils.isNotBlank(srcData) && srcData.matches("(?i).*\\.gif")) {
                return true;
            }
            return false;
        }

        private Bitmap getResult() {
            Bitmap tmp = null;
            if (localFile != null && //
                    (tmp = OOMHandler.new_decode(localFile)) != null) {
                return tmp;
            } else if (isOnlineImageFromURL(altData) && //
                    (tmp = getPicFromURL(altData)) != null) {
                return tmp;
            }
            return onlinePicLoader.getNotfound404();
        }

        private boolean isOnlineImageFromURL(String url) {
            if (url.matches("https?\\:.*") || //
                    url.matches("www\\..*") || //
                    url.matches("\\w+\\.\\w+.*") //
                    ) {
                return true;
            }
            return false;
        }

        private Bitmap getPicFromURL(String url) {
            return onlinePicLoader.getBitmapFromURL_waiting(url, 10 * 1000);
        }
    }

    private GifSpanCreater.ResetScale resetScaleAction = new GifSpanCreater.ResetScale() {
        @Override
        public int[] giveMeBackWidthAndHeight(int width, int height) {
            int newWidth1 = maxPicWidth;
            float scaleWidth = ((float) newWidth1) / width;
            int newHeight = (int) (scaleWidth * height);
            return new int[]{newWidth1, newHeight};
        }
    };

    private void hiddenSpan(SpannableString ss, int start, int end) {
        ss.setSpan(new RelativeSizeSpan(0f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private boolean isHyperLinkTooLong(String urlContent) {
        return StringUtils.trimToEmpty(urlContent).length() > WordHtmlParser.HYPER_LINK_LABEL_MAX_LENGTH;
    }

    private int getPairStart(Pair<Integer, Integer> pair) {
        return pair.getLeft();
    }

    private int getPairEnd(Pair<Integer, Integer> pair) {
        return pair.getRight() + END_TAG.length();
    }

    private void appendNormalIgnoreLst(Pair pair) {
        int start = getPairStart(pair);
        int end = getPairEnd(pair);
        normalIgnoreLst.add(Pair.of(start, end));
    }

    private void appendNormalIgnoreLst(int start, int end) {
        normalIgnoreLst.add(Pair.of(start, end));
    }

    private ImageSpan createHyperlinkImageSpan() {
        if (hyperlink == null) {
            Bitmap b1 = OOMHandler.new_decode(context, R.drawable.hyperlink);
            hyperlink = OOMHandler.fixPicScale(b1, 100, 100);
        }
        return new ImageSpan(context, hyperlink, ImageSpan.ALIGN_BASELINE);//
    }
}
