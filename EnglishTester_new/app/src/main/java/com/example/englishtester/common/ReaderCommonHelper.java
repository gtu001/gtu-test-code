package com.example.englishtester.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.ActionMode;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishtester.EnglishwordInfoDAO;
import com.example.englishtester.MobiReaderMobiActivity;
import com.example.englishtester.R;
import com.example.englishtester.RecentTxtMarkDAO;
import com.example.englishtester.RecentTxtMarkService;
import com.example.englishtester.ReciteMainActivity;
import com.example.englishtester.ShowWordListActivity;
import com.example.englishtester.common.interf.ITxtReaderActivity;
import com.example.englishtester.common.interf.ITxtReaderActivityDTO;
import com.example.englishtester.common.interf.ITxtReaderFileName;
import com.example.englishtester.common.mobi.base.MobiViewerMainHandler;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.cglib.core.internal.Function;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.content.ContextCompat;

import static java.lang.Thread.sleep;

/**
 * Created by gtu001 on 2018/8/8.
 */

public class ReaderCommonHelper {

    private static final String TAG = ReaderCommonHelper.class.getSimpleName();

    private static final ReaderCommonHelper _INST = new ReaderCommonHelper();

    private ReaderCommonHelper() {
    }

    public static ReaderCommonHelper getInst() {
        return _INST;
    }


    /**
     * 調整行距
     */
    public static class LineSpacingAdjuster {
        float defaultVal = 1.4f;
        TextView tmpTextView;
        Context context;

        public LineSpacingAdjuster(Context context) {
            this.context = context;
        }

        public void apply(TextView v) {
            if (v != null) {
                tmpTextView = v;
            }
            if (tmpTextView != null) {
                tmpTextView.setLineSpacing(10, defaultVal);
            }
        }

        /**
         * 開啟改變行距Dialog
         */
        public void openSpacingSizeDialog(final TextView v) {
            if (v == null) {
                Toast.makeText(context, "TextView is NULL", Toast.LENGTH_SHORT).show();
            }
            final SingleSliderbarDialog dlg = new SingleSliderbarDialog(context, 30, 15, 0.3f, 1.4f, true);
            dlg.confirmButton(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    float val = dlg.getEditTextValue();
                    if (val > 0) {
                        defaultVal = val;
                        apply(v);
                    }
                }
            });
            dlg.show();
        }
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
        private static enum TypeFaceEnum {
            T1("consolas.ttf", "Consolas"),//
            T2("Didot-HTF-M24-Medium.otf", "Didot"),//
            T3("helvetica_45W.ttf", "Helvetica"),//
            T4("Myriad Pro Regular.ttf", "Myriad Pro"),//
            T5("PT_FuturaFuturis_Light_Cyrillic.ttf", "Futura"),//
            ;

            final String fileName;
            final String label;

            TypeFaceEnum(String fileName, String label) {
                this.fileName = fileName;
                this.label = label;
            }

            @Override
            public String toString() {
                return label;
            }
        }

        EnumMap<TypeFaceEnum, Typeface> typefaceMap = new EnumMap<>(TypeFaceEnum.class);
        TypeFaceEnum currentFace = TypeFaceEnum.T4;
        Context context;
        String[] items;

        public AppleFontApplyer(Context context) {
            this.context = context;
            List<String> itemLst = new ArrayList<String>();
            for (TypeFaceEnum e : TypeFaceEnum.values()) {
                Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/" + e.fileName);
                this.typefaceMap.put(e, face);
                itemLst.add(e.toString());
            }
            this.items = itemLst.toArray(new String[0]);
        }

        public void choiceTypeface(final TextView... views) {
            new AlertDialog.Builder(this.context)//
                    .setTitle("選擇字型")//
//                    .setMessage("請選擇字型")//
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (TypeFaceEnum e : TypeFaceEnum.values()) {
                                if (e.ordinal() == which) {
                                    currentFace = e;
                                    break;
                                }
                            }
                            for (TextView v : views) {
                                apply(v);
                            }
                        }
                    }).show();
        }

        public void apply(TextView view) {
            try {
                view.setTypeface(typefaceMap.get(currentFace));
            } catch (Exception ex) {
                Toast.makeText(context, "使用字型失敗 " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
        final Handler handler = new Handler();

        public ScrollViewYHolder(Context context) {
            this.context = context;
        }


        public void recordY(String currentTitle, ScrollView scrollView1) {
            Log.v(TAG, "[recordY] start ... " + currentTitle);
            if (StringUtils.isNotBlank(currentTitle)) {

                ScrollYService scrollYService = new ScrollYService(currentTitle, context);
                scrollYService.updateCurrentScrollY(scrollView1.getScrollY());
                scrollYService.updateMaxHeight(ScrollViewHelper.getMaxHeight(scrollView1));

//                Toast.makeText(context, "CH : " + scrollView1.getScrollY() + " -- " + currentTitle, Toast.LENGTH_SHORT).show();

                Log.v(TAG, "[recordY][scrollY]   " + currentTitle + " -> " + scrollView1.getScrollY());
                Log.v(TAG, "[recordY][maxHeight] " + currentTitle + " -> " + ScrollViewHelper.getMaxHeight(scrollView1));
            }
        }

        private void scrollToY(final ScrollView scrollView1, final int yPos) {
            scrollView1.post(new Runnable() {
                @Override
                public void run() {
                    if (yPos > ScrollViewHelper.getMaxHeight(scrollView1)) {
                        Toast.makeText(context, "Y pos 大過最大值 !! : " + yPos + " / " + ScrollViewHelper.getMaxHeight(scrollView1), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    doScrollToY(scrollView1, yPos);

                    if (scrollView1.getScrollY() != yPos) {
//                        Toast.makeText(context, "Y pos 未成功 : " + yPos + "/" + ScrollViewHelper.getMaxHeight(scrollView1), Toast.LENGTH_SHORT).show();
                        scrollToY(scrollView1, yPos);
                    } else {
//                        Log.line(TAG, "成功回復Y pos !! : " + yPos);
//                        Toast.makeText(context, "成功回復Y pos !! : " + yPos, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        public void doScrollToY(final ScrollView scrollView1, final int yPos) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView1.scrollTo(0, yPos);
                        }
                    });
                }
            }).start();
        }

        public void restoreY(final String currentTitle, final ScrollView scrollView1) {
            Log.v(TAG, "[restoreY] start ... " + currentTitle);

            if (StringUtils.isBlank(currentTitle)) {
                return;
            }

            ScrollYService scrollYService = new ScrollYService(currentTitle, context);
            final AtomicReference<Integer> posY = new AtomicReference<>();
            posY.set(scrollYService.getScrollYVO_value());

            Log.v(TAG, "restoreY : " + currentTitle + " , " + posY.get());

            if (posY.get() == -1) {
                posY.set(0);
            }

            //回復scrollY
            scrollToY(scrollView1, posY.get());
        }
    }

    /**
     * 取得所有 scrollY紀錄
     */
    public static class ScrollYService {

        private static final String TAG = ScrollYService.class.getSimpleName();

        private static LRUMap CURRENT_Y_HOLDER = new LRUMap(50);
        private static LRUMap MAX_Y_HOLDER = new LRUMap(50);

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
            RecentTxtMarkDAO.RecentTxtMark vo1 = null;
            if (CURRENT_Y_HOLDER.containsKey(fileName)) {
                vo1 = (RecentTxtMarkDAO.RecentTxtMark) CURRENT_Y_HOLDER.get(fileName);
            } else {
                vo1 = getScrollYVO();
            }
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
            CURRENT_Y_HOLDER.put(fileName, vo1);
        }

        public void updateMaxHeight(int maxHeight) {
            RecentTxtMarkDAO.RecentTxtMark vo1 = null;
            if (MAX_Y_HOLDER.containsKey(fileName)) {
                vo1 = (RecentTxtMarkDAO.RecentTxtMark) MAX_Y_HOLDER.get(fileName);
            } else {
                vo1 = getMaxHeightYVO();
            }
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
            MAX_Y_HOLDER.put(fileName, vo1);
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

        final TxtReaderAppenderSpanClass.WordSpan spanObject = dto.getBookmarkHolder().get(currentId);
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

            pageLst.clear();

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

    /**
     * 這整塊沒啥用,但留下紀錄用
     */
    public static void applyCustomSelectionAction(final TextView txtView, final Context context) {
        txtView.setTextIsSelectable(true);

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            txtView.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.selected_state_selector));
        } else {
            txtView.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_state_selector));
        }

        txtView.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.txtview_translation_menu, menu);
                return true;//返回false则不会显示弹窗
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                String text = StringUtils.substring(txtView.getText().toString(), txtView.getSelectionStart(), txtView.getSelectionEnd());
                //根据item的ID处理点击事件
                switch (item.getItemId()) {
                    case R.id.Informal22:
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                        mode.finish();//收起操作菜单
                        break;
                }
                return false;//返回true则系统的"复制"、"搜索"之类的item将无效，只有自定义item有响应
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
    }

    public static class FloatViewServiceOpenStatusReceiverHelper {

        //通知開啟或關閉
        public static void sendOpenStatusMessage(boolean isOpen, Context context) {
            Intent in = new Intent(FloatViewServiceOpenStatusReceiverHelper.class.getName());
            Bundle extras = new Bundle();
            extras.putString("openStatus", isOpen ? "true" : "false");
            in.putExtras(extras);
            context.sendBroadcast(in);
        }

        private BroadcastReceiver broadcastReceiver;

        public void registerReceiver(Context context, final Function<Boolean, Boolean> procCall) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String isOpenStr = intent.getStringExtra("openStatus");
                    if (StringUtils.isBlank(isOpenStr)) {
                        return;
                    }
                    procCall.apply(Boolean.parseBoolean(isOpenStr));
                }
            };
            context.registerReceiver(broadcastReceiver, new IntentFilter(FloatViewServiceOpenStatusReceiverHelper.class.getName()));
        }

        public void onStop(Context context) {
            if (broadcastReceiver != null) {
                context.unregisterReceiver(broadcastReceiver);
            }
        }
    }

    //========================================================================================================================

    public static class BookmarkShowWordListHandler {
        RecentTxtMarkService recentTxtMarkService;
        EnglishwordInfoDAO mEnglishwordInfoDAO;
        Activity activity;
        ITxtReaderFileName mITxtReaderFileName;

        public BookmarkShowWordListHandler(Context context, ITxtReaderFileName mITxtReaderFileName, Activity activity) {
            recentTxtMarkService = new RecentTxtMarkService(context);
            mEnglishwordInfoDAO = new EnglishwordInfoDAO(context);
            this.mITxtReaderFileName = mITxtReaderFileName;
            this.activity = activity;
        }

        public void showMenu(boolean isSinglePage) {
            List<RecentTxtMarkDAO.RecentTxtMark> qList = Collections.emptyList();
            if (isSinglePage) {
                qList = recentTxtMarkService.getFileMark(mITxtReaderFileName.getDtoFileName());//
            } else {
                qList = recentTxtMarkService.getFileMarkLike(mITxtReaderFileName.getFileName());//
            }
            Map<String, EnglishwordInfoDAO.EnglishWord> englishProp = new LinkedHashMap<>();
            for (RecentTxtMarkDAO.RecentTxtMark txt : qList) {
                String word = txt.getMarkEnglish();
                if (StringUtils.isNotBlank(word)) {
                    EnglishwordInfoDAO.EnglishWord eng = mEnglishwordInfoDAO.queryOneWord(word);
                    englishProp.put(word, eng);
                }
            }
            ShowWordListActivity.startShowWordListActivitiy(englishProp, 9988, activity);
        }

        public void showQuestions(boolean isSinglePage) {
            List<RecentTxtMarkDAO.RecentTxtMark> qList = Collections.emptyList();
            if (isSinglePage) {
                qList = recentTxtMarkService.getFileMark(mITxtReaderFileName.getDtoFileName());//
            } else {
                qList = recentTxtMarkService.getFileMarkLike(mITxtReaderFileName.getFileName());//
            }
            List<String> wordLst = new ArrayList<>();
            for (RecentTxtMarkDAO.RecentTxtMark txt : qList) {
                String word = txt.getMarkEnglish();
                if (StringUtils.isNotBlank(word)) {
                    wordLst.add(word);
                }
            }
            ReciteMainActivity.startReciteMainActivity(wordLst, 9987, activity);
        }
    }


    public static class MoveToNextBookmarkHandler implements DialogInterface.OnClickListener {

        private List<Row> lst = new ArrayList<>();
        private List<Map<String, Object>> lst4Adapter = new ArrayList<>();
        private Context context;
        private ITxtReaderActivity mITxtReaderActivity;
        private Handler handler = new Handler();

        public MoveToNextBookmarkHandler(ITxtReaderActivity mITxtReaderActivity, Context context) {
            this.context = context;
            this.mITxtReaderActivity = mITxtReaderActivity;
        }

        private class Row {
            String file_name;
            int bookmark_type;
            long insert_date;
            int page_index;
            String remark;
            String file_name_fix;

            private String getFixNameFix(String file_name) {
                Pattern ptn = Pattern.compile("^.*(\\[.*?\\])$");
                Matcher mth = ptn.matcher(file_name);
                if (mth.find()) {
                    return "頁" + mth.group(1);
                }
                return file_name;
            }

            private Row(Map<String, Object> map) {
//                {file_name=Everybody Lies Big Data, New Data, and What the Internet - Seth Stephens-Davidowitz[4], bookmark_type=2, insert_date=496398149}
                file_name = (String) map.get("file_name");
                bookmark_type = (Integer) map.get("bookmark_type");
                insert_date = (Integer) map.get("insert_date");
                page_index = (Integer) map.get("page_index");
                remark = (String) map.get("remark");
                file_name_fix = getFixNameFix(file_name);

                String remark = "";
                if (StringUtils.isNotBlank(this.remark)) {
                    remark = "  「" + StringUtils.trimToEmpty(this.remark) + "」";
                }
                file_name_fix += remark;
            }

            private Map<String, Object> toAdapterMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("ItemTitle", file_name_fix);
                map.put("ItemDetail", "page " + (page_index + 1));
                map.put("ItemDetailRight", DateFormatUtils.format(insert_date, "yyyy/MM/dd HH:mm:ss"));
                map.put("ItemDetail2", "");
                return map;
            }
        }

        public void initReference() {
            String fileName = this.mITxtReaderActivity.getFileName();
            fileName = fileName.replaceAll("'", "''");

            StringBuilder sb = new StringBuilder();
            sb.append(" select file_name , bookmark_type, max(insert_date) as insert_date, page_index , remark                ");
            sb.append(" from recent_txt_mark                                                              ");
            sb.append(" where bookmark_type in (%s) and file_name like '%s%%'  and page_index != -1       ");
            sb.append("  group by file_name, bookmark_type, remark, page_index                            ");
            sb.append("  order by 3 desc                                                                  ");

            String bookmarkTypeStr = StringUtils.join(Arrays.asList(RecentTxtMarkDAO.BookmarkTypeEnum.BOOKMARK.getType(), RecentTxtMarkDAO.BookmarkTypeEnum.SCROLL_Y_POS.getType()), ",");

            String sql = String.format(sb.toString(), bookmarkTypeStr, fileName);
            List<Map<String, Object>> lst = DBUtil.queryBySQL_realType(sql, new String[0], context);
            for (Map<String, Object> map : lst) {
                Row row = new Row(map);
                this.lst.add(row);
                this.lst4Adapter.add(row.toAdapterMap());
            }
        }

        public void showDlg() {
            if (lst4Adapter.isEmpty()) {
                Toast.makeText(context, "尚無書籤紀錄", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleAdapter aryAdapter = new SimpleAdapter(context, lst4Adapter,// 資料來源
                    R.layout.subview_dropboxlist, //
                    new String[]{"ItemTitle", "ItemDetail", "ItemDetailRight", "ItemDetail2"}, //
                    new int[]{R.id.ItemTitle, R.id.ItemDetail, R.id.ItemDetailRight, R.id.ItemDetail2}//
            );

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("請選擇書籤");
            builder.setAdapter(aryAdapter, this);
            AlertDialog alert = builder.create();
            alert.show();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            final Row row = lst.get(which);
            Pattern ptn = Pattern.compile("^.*\\[(.*?)\\]$");
            Matcher mth = ptn.matcher(row.file_name);

            int pos = 0;
            int dtlPos = 0;

            if (mth.find()) {

                String pageInfo = mth.group(1);

                if (pageInfo.matches("\\d+")) {
                    pos = Integer.parseInt(pageInfo);
                } else {
                    Matcher mth2 = Pattern.compile("(\\d+)\\s\\((\\d+)").matcher(pageInfo);
                    if (mth2.find()) {
                        pos = Integer.parseInt(mth2.group(1));
                        dtlPos = Integer.parseInt(mth2.group(1));
                    } else {
                        throw new RuntimeException("無法取得 SpinePos : " + pageInfo);
                    }
                }
            }

            Toast.makeText(context, "跳至章節 " + pos + " : " + dtlPos + " : Page : " + (row.page_index + 1), Toast.LENGTH_SHORT).show();
            mITxtReaderActivity.gotoViewPagerPosition(row.page_index);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mITxtReaderActivity.setTitle(mITxtReaderActivity.getCurrentTitle(row.page_index));
                }
            }, 1000L);
        }
    }
}
