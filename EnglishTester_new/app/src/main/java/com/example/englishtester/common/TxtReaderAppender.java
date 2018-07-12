package com.example.englishtester.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.RemoteException;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishtester.FloatViewService;
import com.example.englishtester.R;
import com.example.englishtester.RecentTxtMarkDAO;
import com.example.englishtester.RecentTxtMarkService;
import com.example.englishtester.TxtReaderActivity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gtu001 on 2018/7/8.
 */

public class TxtReaderAppender {

    private static final String TAG = TxtReaderAppender.class.getSimpleName();

    RecentTxtMarkService recentTxtMarkService;
    TxtReaderActivity activity;
    IFloatServiceAidlInterface mService;
    TxtReaderActivity.TxtReaderActivityDTO dto;
    TextView txtView;

    OnlinePicLoader onlinePicLoader;

    public TxtReaderAppender(TxtReaderActivity activity, RecentTxtMarkService recentTxtMarkService, IFloatServiceAidlInterface mService, TxtReaderActivity.TxtReaderActivityDTO dto, TextView txtView) {
        this.recentTxtMarkService = recentTxtMarkService;
        this.activity = activity;
        this.mService = mService;
        this.dto = dto;
        this.txtView = txtView;
        this.onlinePicLoader = new OnlinePicLoader(activity);
    }

    private void hiddenSpan(SpannableString ss, int start, int end) {
        ss.setSpan(new RelativeSizeSpan(0f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private class __SpecialTagHolder_Pos {
        int show_start = -1;
        int show_end = -1;
        String content;

        __SpecialTagHolder_Pos(Matcher mth, String txtContent, int groupIndex) {
            int start = mth.start();
            int end = mth.end();

            content = mth.group(groupIndex);

            show_start = mth.start(groupIndex);
            show_end = mth.end(groupIndex);
        }

        public int getStart() {
            return show_start;
        }

        public int getEnd() {
            return show_end;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "__SpecialTagHolder_Pos{" +
                    "show_start=" + show_start +
                    ", show_end=" + show_end +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

    private class TxtAppenderProcess {

        String txtContent;
        boolean isWordHtml;
        SpannableString ss;
        int maxPicWidth;
        List<Pair<Integer, Integer>> normalIgnoreLst = new ArrayList<>();
        Bitmap hyperlink;

        TxtAppenderProcess(String txtContent, boolean isWordHtml, int maxPicWidth) {
            this.txtContent = txtContent;
            this.isWordHtml = isWordHtml;
            this.ss = new SpannableString(txtContent);
            this.maxPicWidth = maxPicWidth;
        }

        public SpannableString getResult() {
            if (isWordHtml) {
                wordHtmlProcess();
            }

            normalTxtProcess();

            return ss;
        }

        private void appendNormalIgnoreLst(Matcher mth) {
            normalIgnoreLst.add(Pair.of(mth.start(), mth.end()));
        }

        private void appendNormalIgnoreLst(int start, int end) {
            normalIgnoreLst.add(Pair.of(start, end));
        }

        private boolean isNormalIgnore(Matcher mth) {
            if (normalIgnoreLst.isEmpty()) {
                return false;
            }
            int start = mth.start();
            int end = mth.end();
            for (Pair<Integer, Integer> p : normalIgnoreLst) {
                if (p.getLeft() <= start && p.getRight() >= end) {
                    return true;
                }
            }
            return false;
        }

        private ImageSpan createHyperlinkImageSpan() {
            if (hyperlink == null) {
                Bitmap b1 = OOMHandler.new_decode(activity, R.drawable.hyperlink);
                hyperlink = OOMHandler.fixPicScale(b1, 100, 100);
            }
            return new ImageSpan(activity, hyperlink, ImageSpan.ALIGN_BASELINE);//
        }

        private boolean isHyperLinkToLong(String urlContent) {
            return StringUtils.trimToEmpty(urlContent).length() > WordHtmlParser.HYPER_LINK_LABEL_MAX_LENGTH;
        }

        private void wordHtmlProcess() {
            // 設定標題
            Pattern ptnTitle = Pattern.compile("\\{\\{title\\:(.*?)\\}{2,4}", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptnTitle.matcher(txtContent);
            while (mth.find()) {
                int start = mth.start();
                int end = mth.end();
//                this.appendNormalIgnoreLst(mth);

                __SpecialTagHolder_Pos proc = new __SpecialTagHolder_Pos(mth, txtContent, 1);

                hiddenSpan(ss, start, proc.getStart());
                hiddenSpan(ss, proc.getEnd(), end);

                ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), proc.getStart(), proc.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new RelativeSizeSpan(1.2f), proc.getStart(), proc.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // 設定圖片
            Pattern ptnImg = Pattern.compile("\\{\\{img\\ssrc\\:(.*?)\\,alt\\:(.*?)\\}\\}", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth2 = ptnImg.matcher(txtContent);
            while (mth2.find()) {
                int start = mth2.start();
                int end = mth2.end();
//                this.appendNormalIgnoreLst(mth2);

                __SpecialTagHolder_Pos srcData = new __SpecialTagHolder_Pos(mth2, txtContent, 1);
                __SpecialTagHolder_Pos altData = new __SpecialTagHolder_Pos(mth2, txtContent, 2);

                ImageLoaderCandidate picProcess = new ImageLoaderCandidate(srcData.getContent(), altData.getContent());

//                hiddenSpan(ss, start, srcData.getStart());
//                hiddenSpan(ss, srcData.getEnd(), altData.getStart());
//                hiddenSpan(ss, altData.getEnd(), end);

                if (!picProcess.isGifFile) {
                    Bitmap smiley = OOMHandler.fixPicScaleFixScreenWidth(picProcess.getResult(), maxPicWidth);
                    ss.setSpan(new ImageSpan(activity, smiley, ImageSpan.ALIGN_BASELINE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (picProcess.localFile != null) {
                    ImageSpan imageSpan = GifSpanCreater.getGifSpan(picProcess.localFile, dto.getTxtView(), resetScaleAction);
                    ss.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            // 設定Url
            Pattern ptnHref = Pattern.compile("\\{\\{link\\:(.*?),value\\:(.*?)\\}\\}", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth3 = ptnHref.matcher(txtContent);
            while (mth3.find()) {
                int start = mth3.start();
                int end = mth3.end();

                final __SpecialTagHolder_Pos linkUrl = new __SpecialTagHolder_Pos(mth3, txtContent, 1);
                final __SpecialTagHolder_Pos linkLabel = new __SpecialTagHolder_Pos(mth3, txtContent, 2);

                SimpleUrlLinkSpan hrefLinkSpan = new SimpleUrlLinkSpan(activity, linkUrl.getContent());

                //長度太長的link
                if (!isHyperLinkToLong(linkLabel.getContent())) {

                    this.appendNormalIgnoreLst(mth3);

                    hiddenSpan(ss, start, linkLabel.getStart());
                    hiddenSpan(ss, linkLabel.getEnd(), end);

                    Log.v(TAG, "Lbl : " + linkLabel);

                    ss.setSpan(hrefLinkSpan, linkLabel.getStart(), linkLabel.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {

                    this.appendNormalIgnoreLst(linkUrl.getStart(), linkUrl.getEnd());

                    hiddenSpan(ss, start, linkUrl.getStart());
                    hiddenSpan(ss, linkUrl.getEnd(), linkLabel.getStart());
                    hiddenSpan(ss, linkLabel.getEnd(), end);

                    Log.v(TAG, "Lbl : " + linkLabel);

                    ss.setSpan(createHyperlinkImageSpan(), linkUrl.getStart(), linkUrl.getEnd(), Spannable.SPAN_POINT_POINT);
                    ss.setSpan(hrefLinkSpan, linkUrl.getStart(), linkUrl.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        GifSpanCreater.ResetScale resetScaleAction = new GifSpanCreater.ResetScale() {
            @Override
            public int[] giveMeBackWidthAndHeight(int width, int height) {
                int newWidth1 = maxPicWidth;
                float scaleWidth = ((float) newWidth1) / width;
                int newHeight = (int) (scaleWidth * height);
                return new int[]{newWidth1, newHeight};
            }
        };

        private void normalTxtProcess() {
            Pattern ptn = Pattern.compile("[a-zA-Z\\-]+", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptn.matcher(txtContent);
            final String txtContent_ = txtContent;

            List<RecentTxtMarkDAO.RecentTxtMark> qList = recentTxtMarkService.getFileMark(dto.getFileName().toString());
            Log.v(TAG, "recentTxtMark fileName = " + dto.getFileName());
            Log.v(TAG, "recentTxtMark list size = " + qList.size());

            int index = 0;
            while (mth.find()) {
                final int start = mth.start();
                final int end = mth.end();

                if (isNormalIgnore(mth)) {
                    continue;
                }

                final String txtNow = txtContent_.substring(start, end);

                WordSpan clickableSpan = new WordSpan(index) {

                    private void checkFloatServiceOn() {
                        if (!ServiceUtil.isServiceRunning(activity, FloatViewService.class)) {
                            activity.doOnoffService(true);
                        }
                    }

                    @Override
                    public void onClick(View view) {
                        Log.v(TAG, "click " + this.id + " - " + txtNow);

                        //myService.searchWordForActivity(txtNow);
                        try {
                            checkFloatServiceOn();

                            mService.searchWord(txtNow);
                        } catch (RemoteException e) {
                            Log.e(TAG, e.getMessage(), e);
                            Toast.makeText(activity, "查詢失敗!", Toast.LENGTH_SHORT).show();
                        }

                        setMarking(true);
                        view.invalidate();
                        txtView.invalidate();

                        // 新增單字
                        recentTxtMarkService.addMarkWord(dto.getFileName().toString(), txtNow, this.id);
                    }
                };

                // 設定單字為以查詢狀態
                for (RecentTxtMarkDAO.RecentTxtMark bo : qList) {
                    if (bo.getMarkIndex() == clickableSpan.id && StringUtils.equals(bo.getMarkEnglish(), txtNow)) {
                        Log.v(TAG, "remark : " + txtNow + " - " + clickableSpan.id);
                        clickableSpan.setMarking(true);
                    }
                }

                Log.v(TAG, "setSpan - " + clickableSpan.id + " - " + txtNow);
                index++;
                ss.setSpan(clickableSpan, start, end, Spanned.SPAN_COMPOSING);// SPAN_EXCLUSIVE_EXCLUSIVE
            }
        }
    }

    /**
     * 建立可點擊文件
     */
    public SpannableString getAppendTxt(String txtContent) {
        TxtAppenderProcess appender = new TxtAppenderProcess(txtContent, false, -1);
        return appender.getResult();
    }

    /**
     * 建立可點擊文件
     */
    public SpannableString getAppendTxt_HtmlFromWord(String txtContent, int maxPicWidth) {
        TxtAppenderProcess appender = new TxtAppenderProcess(txtContent, true, maxPicWidth);
        return appender.getResult();
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

    public static class SimpleUrlLinkSpan extends ClickableSpan {
        Context context;
        String url;

        public SimpleUrlLinkSpan(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.BLUE);
            ds.setUnderlineText(true);
        }

        @Override
        public void onClick(View view) {
            Log.v(TAG, "click " + " - " + url);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        }
    }

    public static class WordSpan extends ClickableSpan {
        int id = -1;
        private boolean marking = false;

        public WordSpan() {
        }

        public WordSpan(int id) {
            this.id = id;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            // ds.bgColor = Color.WHITE;
            ds.setColor(Color.BLACK);
            ds.setUnderlineText(false);
            if (marking) {
                ds.setTypeface(Typeface.create("新細明體", Typeface.BOLD));
            }
        }

        @Override
        public void onClick(View v) {
        }

        public void setMarking(boolean m) {
            marking = m;
        }
    }
}
