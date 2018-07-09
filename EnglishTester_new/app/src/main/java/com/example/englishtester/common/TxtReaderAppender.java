package com.example.englishtester.common;

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
import com.example.englishtester.RecentTxtMarkDAO;
import com.example.englishtester.RecentTxtMarkService;
import com.example.englishtester.TxtReaderActivity;

import org.apache.commons.lang3.StringUtils;

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

    public TxtReaderAppender(TxtReaderActivity activity, RecentTxtMarkService recentTxtMarkService, IFloatServiceAidlInterface mService, TxtReaderActivity.TxtReaderActivityDTO dto, TextView txtView) {
        this.recentTxtMarkService = recentTxtMarkService;
        this.activity = activity;
        this.mService = mService;
        this.dto = dto;
        this.txtView = txtView;
    }

    private void hiddenSpan(SpannableString ss, int start, int end) {
        ss.setSpan(new RelativeSizeSpan(0f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public SpannableString getAppendTxt_HtmlFromWord(String txtContent) {
        SpannableString ss = new SpannableString(txtContent);

        // Word 產生 HTML 處理
        ss = getAppendTxt_HtmpFromWord_RuleAppender(txtContent, ss);

        // 一般流程
        ss = getAppendTxt(txtContent, ss);
        return ss;
    }

    private class __SpecialTagHolder_Pos {
        int show_start = -1;
        int show_end = -1;
        String content;

        __SpecialTagHolder_Pos(Matcher mth, String txtContent, int groupIndex) {
            int start = mth.start();
            int end = mth.end();

            String txtNow = txtContent.substring(start, end);
            content = mth.group(groupIndex);

            show_start = start + txtNow.indexOf(content);
            show_end = show_start + content.length();
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
    }

    private SpannableString getAppendTxt_HtmpFromWord_RuleAppender(String txtContent, SpannableString ss) {
        // 設定標題
        Pattern ptnTitle = Pattern.compile("\\{\\{title\\:(.*?)\\}\\}");
        Matcher mth = ptnTitle.matcher(txtContent);
        while (mth.find()) {
            int start = mth.start();
            int end = mth.end();

            __SpecialTagHolder_Pos proc = new __SpecialTagHolder_Pos(mth, txtContent, 1);

            hiddenSpan(ss, start, proc.getStart());
            hiddenSpan(ss, proc.getEnd(), end);

            ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), proc.getStart(), proc.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new RelativeSizeSpan(1.2f), proc.getStart(), proc.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 設定圖片
        Pattern ptnImg = Pattern.compile("\\{\\{img\\:(.*?)\\}\\}");
        Matcher mth2 = ptnImg.matcher(txtContent);
        while (mth2.find()) {
            int start = mth.start();
            int end = mth.end();

            __SpecialTagHolder_Pos proc = new __SpecialTagHolder_Pos(mth, txtContent, 1);

            hiddenSpan(ss, start, proc.getStart());
            hiddenSpan(ss, proc.getEnd(), end);

            Bitmap smiley = OOMHandler.getBitmapFromURL_waiting(proc.getContent(), 10 * 1000);
            ss.setSpan(new ImageSpan(activity, smiley, ImageSpan.ALIGN_BASELINE), proc.getStart(), proc.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 設定Url
        Pattern ptnHref = Pattern.compile("\\{\\{link\\:(.*?),value\\:(.*?)\\}\\}");
        Matcher mth3 = ptnHref.matcher(txtContent);
        while (mth3.find()) {
            int start = mth.start();
            int end = mth.end();

            final __SpecialTagHolder_Pos linkUrl = new __SpecialTagHolder_Pos(mth, txtContent, 1);
            final __SpecialTagHolder_Pos linkLabel = new __SpecialTagHolder_Pos(mth, txtContent, 2);

            WordSpan hrefLinkSpan = new WordSpan() {
                @Override
                public void onClick(View view) {
                    Log.v(TAG, "click " + " - " + linkUrl.getContent());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl.getContent()));
                    activity.startActivity(browserIntent);
                }
            };

            hiddenSpan(ss, start, linkUrl.getStart());
            hiddenSpan(ss, linkUrl.getEnd(), linkLabel.getStart());
            hiddenSpan(ss, linkLabel.getStart(), end);

            ss.setSpan(hrefLinkSpan, linkUrl.getStart(), linkUrl.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    /**
     * 建立可點擊文件
     */
    public SpannableString getAppendTxt(String txtContent) {
        SpannableString ss = new SpannableString(txtContent);
        return getAppendTxt(txtContent, ss);
    }


    private SpannableString getAppendTxt(String txtContent, SpannableString ss) {
        Pattern ptn = Pattern.compile("[a-zA-Z\\-]+");
        Matcher mth = ptn.matcher(txtContent);
        final String txtContent_ = txtContent;

        List<RecentTxtMarkDAO.RecentTxtMark> qList = recentTxtMarkService.getFileMark(dto.getFileName().toString());
        Log.v(TAG, "recentTxtMark fileName = " + dto.getFileName());
        Log.v(TAG, "recentTxtMark list size = " + qList.size());

        int index = 0;
        while (mth.find()) {
            final int start = mth.start();
            final int end = mth.end();

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
        return ss;
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
