package com.example.englishtester.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishtester.R;
import com.example.englishtester.RecentTxtMarkDAO;
import com.example.englishtester.TxtReaderActivity;
import com.example.englishtester.common.html.parser.HtmlWordParser;
import com.example.englishtester.common.interf.ITxtReaderActivityDTO;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gtu001 on 2018/8/8.
 */

public class ReaderCommonHelper {

    private static final ReaderCommonHelper _INST = new ReaderCommonHelper();

    private ReaderCommonHelper() {
    }

    public static ReaderCommonHelper getInst() {
        return _INST;
    }

    public static class PaddingAdjuster {
        Display d;
        int padWidth;
        int padHeight;
        int maxWidth;
        int maxHeight;

        public PaddingAdjuster(Context context) {
            d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            padWidth = (int) ((double) d.getWidth() * 0.1 / 2);
            padHeight = (int) ((double) d.getHeight() * 0.1 / 2);

            maxWidth = d.getWidth() - (2 * padWidth);
            maxHeight = d.getHeight() - (2 * padHeight);
        }

        public void applyPadding(TextView textView) {
            textView.setPadding(padWidth, padHeight, padWidth, padHeight);
        }

        public int getMaxWidth() {
            return maxWidth;
        }

        public void setMaxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
        }

        public int getMaxHeight() {
            return maxHeight;
        }

        public void setMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
        }
    }

    public static class AppleFontApplyer {
        Typeface myriadProRegular;

        public AppleFontApplyer(Context context) {
            myriadProRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Myriad Pro Regular.ttf");
        }

        public void apply(TextView view) {
            view.setTypeface(myriadProRegular);
        }
    }

    /**
     * 字型大小修改
     */
    public static class FontSizeApplyer {
        private static final float DEFAULT_FONTSIZE = 48f;

        public float getFontSize(ContextWrapper context, Class clz) {
            if (SharedPreferencesUtil.hasData(context, clz.getSimpleName(), "fontSize")) {
                return Float.parseFloat(SharedPreferencesUtil.getData(context, clz.getSimpleName(), "fontSize"));
            } else {
                return DEFAULT_FONTSIZE;
            }
        }

        public void setFontSize(ContextWrapper context, float size, Class clz) {
            SharedPreferencesUtil.putData(context, clz.getSimpleName(), "fontSize", String.valueOf(size));
        }
    }

    /**
     * 紀錄 scrollView位置
     */
    public static class ScrollViewYHolder {
        private final String TAG = ScrollViewYHolder.class.getSimpleName();

        Context context;

        public ScrollViewYHolder(Context context) {
            this.context = context;
        }


        public void recordY(String currentTitle, ScrollView scrollView1) {
            Log.v(TAG, "[recordY] start ... " + currentTitle);
            if (StringUtils.isNotBlank(currentTitle)) {

                ScrollYService scrollYService = new ScrollYService(currentTitle, context);
                scrollYService.updateCurrentScrollY(scrollView1.getScrollY());
                scrollYService.updateMaxHeight(ScrollViewHelper.getMaxHeight(scrollView1));

                Log.v(TAG, "[recordY][scrollY]   " + currentTitle + " -> " + scrollView1.getScrollY());
                Log.v(TAG, "[recordY][maxHeight] " + currentTitle + " -> " + ScrollViewHelper.getMaxHeight(scrollView1));
            }
        }

        public void restoreY(final String currentTitle, final ScrollView scrollView1) {
            Log.v(TAG, "[restoreY] start ... " + currentTitle);

            if (StringUtils.isBlank(currentTitle)) {
                return;
            }

            ScrollYService scrollYService = new ScrollYService(currentTitle, context);
            final AtomicReference<Integer> posY = new AtomicReference<>();
            posY.set(scrollYService.getScrollYVO_value());
            if (posY.get() == -1) {
                posY.set(0);
            }

            scrollView1.post(new Runnable() {
                @Override
                public void run() {
                    scrollView1.scrollTo(0, posY.get());
                    Log.v(TAG, "[restoreY] : " + currentTitle + " -> " + posY.get());
                }
            });
        }
    }

    /**
     * 取得所有 scrollY紀錄
     */
    public static class ScrollYService {

        private static final String TAG = ScrollYService.class.getSimpleName();

        String fileName;
        RecentTxtMarkDAO recentTxtMarkDAO;

        public ScrollYService(String fileName, Context context) {
            this.fileName = fileName;
            recentTxtMarkDAO = new RecentTxtMarkDAO(context);
        }

        private RecentTxtMarkDAO.RecentTxtMark getVO(int bookmarkType) {
            List<RecentTxtMarkDAO.RecentTxtMark> list = recentTxtMarkDAO.query(//
                    RecentTxtMarkDAO.RecentTxtMarkSchmea.FILE_NAME + "=? and " + //
                            RecentTxtMarkDAO.RecentTxtMarkSchmea.BOOKMARK_TYPE + "=? ", //
                    new String[]{fileName, String.valueOf(bookmarkType)});

            if (!list.isEmpty()) {
                return list.get(0);
            }
            return null;
        }

        private RecentTxtMarkDAO.RecentTxtMark getScrollYVO() {
            final int bookmarkType = RecentTxtMarkDAO.BookmarkTypeEnum.SCROLL_Y_POS.getType();
            return getVO(bookmarkType);
        }

        private RecentTxtMarkDAO.RecentTxtMark getMaxHeightYVO() {
            final int bookmarkType = RecentTxtMarkDAO.BookmarkTypeEnum.SCROLLVIEW_HEIGHT.getType();
            return getVO(bookmarkType);
        }

        public int getScrollYVO_value() {
            RecentTxtMarkDAO.RecentTxtMark vo = getScrollYVO();
            if (vo != null) {
                return vo.getScrollYPos();
            }
            return -1;
        }

        public int getMaxHeightYVO_value() {
            RecentTxtMarkDAO.RecentTxtMark vo = getMaxHeightYVO();
            if (vo != null) {
                return vo.getScrollYPos();
            }
            return -1;
        }

        private RecentTxtMarkDAO.RecentTxtMark createVO(int bookmarkType) {
            RecentTxtMarkDAO.RecentTxtMark recentTxtVo = new RecentTxtMarkDAO.RecentTxtMark();
            recentTxtVo.setFileName(fileName);
            recentTxtVo.setInsertDate(System.currentTimeMillis());
            recentTxtVo.setBookmarkType(bookmarkType);
            recentTxtVo.setMarkEnglish("");
            recentTxtVo.setMarkIndex(-1);
            return recentTxtVo;
        }

        public void updateCurrentScrollY(int currentScrollY) {
            RecentTxtMarkDAO.RecentTxtMark vo1 = getScrollYVO();
            if (vo1 == null) {
                vo1 = createVO(RecentTxtMarkDAO.BookmarkTypeEnum.SCROLL_Y_POS.getType());
                vo1.setScrollYPos(currentScrollY);
                long result = recentTxtMarkDAO.insertWord(vo1);
                Log.v(TAG, "[updateCurrentScrollY] " + (result > 0 ? "[success]" : "[fail]") + ReflectionToStringBuilder.toString(vo1));
            } else {
                vo1.setScrollYPos(currentScrollY);
                long result = recentTxtMarkDAO.updateByVO(vo1);
                Log.v(TAG, "[updateCurrentScrollY] " + (result > 0 ? "[success]" : "[fail]") + ReflectionToStringBuilder.toString(vo1));
            }
        }

        public void updateMaxHeight(int maxHeight) {
            RecentTxtMarkDAO.RecentTxtMark vo1 = getMaxHeightYVO();
            if (vo1 == null) {
                vo1 = createVO(RecentTxtMarkDAO.BookmarkTypeEnum.SCROLLVIEW_HEIGHT.getType());
                vo1.setScrollYPos(maxHeight);
                long result = recentTxtMarkDAO.insertWord(vo1);
                Log.v(TAG, "[updateMaxHeight] " + (result > 0 ? "[success]" : "[fail]") + ReflectionToStringBuilder.toString(vo1));
            } else {
                vo1.setScrollYPos(maxHeight);
                long result = recentTxtMarkDAO.updateByVO(vo1);
                Log.v(TAG, "[updateMaxHeight] " + (result > 0 ? "[success]" : "[fail]") + ReflectionToStringBuilder.toString(vo1));
            }
        }
    }

    /**
     * 移動到下個書籤
     */
    public void moveToNextBookmark(ITxtReaderActivityDTO dto, TextView txtView, final ScrollView scrollView1, final Context context, WindowManager windowManager) {
        if (dto.getBookmarkHolder() == null || dto.getBookmarkHolder().isEmpty()) {
            Toast.makeText(context, "目前沒有書籤紀錄!", Toast.LENGTH_SHORT).show();
            return;
        }

        int currentId = dto.getBookmarkIndexHolder().get();

        List<Integer> lst = new ArrayList<>(new TreeSet<>(dto.getBookmarkHolder().keySet()));
        Collections.reverse(lst);

        if (!lst.contains(currentId)) {
            currentId = lst.get(0);
        } else {
            int tmpId = lst.indexOf(currentId);
            if (tmpId + 1 >= lst.size()) {
                currentId = lst.get(0);
            } else {
                currentId = lst.get(tmpId + 1);
            }
        }
        dto.getBookmarkIndexHolder().set(currentId);

        final TxtReaderAppender.WordSpan spanObject = dto.getBookmarkHolder().get(currentId);
        TxtCoordinateFetcher coordinate = new TxtCoordinateFetcher(txtView, spanObject, windowManager);

        final Rect rect = coordinate.getCoordinate();

        scrollView1.post(new Runnable() {
            @Override
            public void run() {
                int offsetHeight = context.getResources().getDisplayMetrics().heightPixels / 2;
                int newSrollY = scrollView1.getScrollY() + rect.top - offsetHeight;
                scrollView1.scrollTo(rect.left, newSrollY);
                Toast.makeText(context, "移到 : " + spanObject.getWord(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class FreeGoogleTranslateHandler {

        private static final String TAG = FreeGoogleTranslateHandler.class.getSimpleName();

        private List<String> pageLst = new ArrayList<>();
        private String content;
        private Context context;
        private int pageIdx = 1;

        private static final int BUFFER_LENGTH = 3500;

        public FreeGoogleTranslateHandler(Context context) {
            this.context = context;
        }

        public void init(String content) {
            if (StringUtils.equals(content, this.content)) {
                return;
            }

            this.pageIdx = 1;
            this.content = StringUtils.trimToEmpty(content);

            Pattern ptn = Pattern.compile("\n", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptn.matcher(content);

            int startPos = 0;
            while (mth.find()) {
                if (mth.end() - startPos > BUFFER_LENGTH) {
                    String tmpContent = content.substring(startPos, mth.end());
                    pageLst.add(tmpContent);
                    startPos = mth.end();
                }
            }
            pageLst.add(content.substring(startPos, content.length()));
        }

        private void gotoGoogleTranslate(int pageIdx) {
            String content = StringUtils.trimToEmpty(pageLst.get(pageIdx));
            if (StringUtils.isBlank(content)) {
                Toast.makeText(context, "請先輸入內容!", Toast.LENGTH_SHORT).show();
            }
            String url = "https://translate.google.com.tw/?hl=zh-TW#en/zh-TW/";
            try {
                content = URLEncoder.encode(content, "UTF8");
            } catch (UnsupportedEncodingException e) {
            }
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + content));
            context.startActivity(browserIntent);
        }

        public void showDlg() {
            final SingleInputDialog dlg = new SingleInputDialog(context, String.valueOf(pageIdx), "輸入頁碼", "請輸入1-" + (pageLst.size()));
            dlg.confirmButton(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (pageLst.isEmpty()) {
                        Toast.makeText(context, "請先輸入內容!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        int pageIdx = Integer.parseInt(dlg.getEditText(true, true));
                        FreeGoogleTranslateHandler.this.pageIdx = pageIdx;
                        gotoGoogleTranslate(pageIdx - 1);
                    } catch (Exception e) {
                        Toast.makeText(context, "讀取失敗!", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "showDlg ERR : " + e.getMessage(), e);
                    }
                }
            });
            dlg.show();
        }
    }
}
