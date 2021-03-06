package com.example.englishtester;

import android.content.Context;

import com.example.englishtester.common.Log;

import com.example.englishtester.RecentTxtMarkDAO.RecentTxtMark;
import com.example.englishtester.RecentTxtMarkDAO.RecentTxtMarkSchmea;
import com.google.common.reflect.Reflection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public class RecentTxtMarkService {

    private static final String TAG = RecentTxtMarkService.class.getSimpleName();

    RecentTxtMarkDAO recentTxtMarkDAO;

    public RecentTxtMarkService(Context context) {
        recentTxtMarkDAO = new RecentTxtMarkDAO(context);
    }

    /**
     * 查詢所有書籤內容
     */
    public List<RecentTxtMark> queryBookmarkLikeLst(String fileName, int bookmarkType) {
        return recentTxtMarkDAO.queryBookmarkLikeLst(fileName, bookmarkType);
    }

    public RecentTxtMark addOpenBookMark(final String fileName) {
        long currentTime = System.currentTimeMillis();
        RecentTxtMark bo = new RecentTxtMark();
        bo.fileName = fileName;
        bo.insertDate = currentTime;
        bo.markEnglish = "";
        bo.markIndex = -1;
        bo.bookmarkType = RecentTxtMarkDAO.BookmarkTypeEnum.FILE_OPEN.getType();
        bo.pageIndex = -1;

        List<RecentTxtMark> list = recentTxtMarkDAO.query(//
                RecentTxtMarkSchmea.FILE_NAME + "=? and " + //
                        RecentTxtMarkSchmea.BOOKMARK_TYPE + "=? ", //
                new String[]{fileName, String.valueOf(RecentTxtMarkDAO.BookmarkTypeEnum.FILE_OPEN.getType())});

        if (list.isEmpty()) {
            long result = recentTxtMarkDAO.insertWord(bo);
            Log.v(TAG, "insert [" + result + "]" + ReflectionToStringBuilder.toString(bo));
        } else {
            bo = list.get(0);
            bo.insertDate = currentTime;
            int result = recentTxtMarkDAO.updateByVO(bo);
            Log.v(TAG, "update [" + result + "]" + ReflectionToStringBuilder.toString(bo));
        }
        return bo;
    }

    /**
     * 新增查詢單字
     */
    public RecentTxtMark addMarkWord(final String fileName, final String word, final int index, int pageIndex, boolean clickBookmark, String remark) {
//        Log.line(TAG, "fileName = " + fileName + " , word = " + word + " , index = " + index + " , pageIndex = " + pageIndex + " , clickBookmark = " + clickBookmark);
        long currentTime = System.currentTimeMillis();
        RecentTxtMark bo = new RecentTxtMark();
        bo.fileName = fileName;
        bo.insertDate = currentTime;
        bo.markEnglish = word;
        bo.markIndex = index;
        bo.bookmarkType = getBookmarkType(null, clickBookmark);
        bo.pageIndex = pageIndex;
        bo.remark = StringUtils.trimToEmpty(remark);

        List<RecentTxtMark> list = recentTxtMarkDAO.query(//
                RecentTxtMarkSchmea.FILE_NAME + "=? and " + //
                        RecentTxtMarkSchmea.MARK_ENGLISH + "=? and " + //
                        RecentTxtMarkSchmea.MARK_INDEX + "=?", //
                new String[]{fileName, word, String.valueOf(index)});

        if (list.isEmpty()) {
            long result = recentTxtMarkDAO.insertWord(bo);
            Log.v(TAG, "insert [" + result + "]" + ReflectionToStringBuilder.toString(bo));
        } else {
            bo = list.get(0);
            bo.insertDate = currentTime;
            bo.bookmarkType = getBookmarkType(bo.bookmarkType, clickBookmark);
            int result = recentTxtMarkDAO.updateByVO(bo);
            Log.v(TAG, "update [" + result + "]" + ReflectionToStringBuilder.toString(bo));
        }
        Log.v(TAG, "AddMarkWord -- " + ReflectionToStringBuilder.toString(bo, ToStringStyle.MULTI_LINE_STYLE));
        return bo;
    }

    private int getBookmarkType(Integer oldValue, boolean clickBookmarkAction) {
        int none = RecentTxtMarkDAO.BookmarkTypeEnum.NONE.getType();
        int bookmark = RecentTxtMarkDAO.BookmarkTypeEnum.BOOKMARK.getType();

        if (oldValue == null) { //insert
            return clickBookmarkAction ? bookmark : none;
        } else { //update
            if (clickBookmarkAction) {
                return oldValue == none ? bookmark : none;
            } else {
                return none;
            }
        }
    }

    /**
     * 刪除舊資料
     */
    public void deleteOldData() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        long daysBefore = cal.getTimeInMillis();

        int count = recentTxtMarkDAO.deleteByCondition(RecentTxtMarkSchmea.INSERT_DATE + "<?", new String[]{String.valueOf(daysBefore)});
        Log.v(TAG, "deleteOldData count " + count);
    }

    /**
     * 取得檔案mark紀錄
     */
    public synchronized List<RecentTxtMark> getFileMark(String fileName) {
        List<RecentTxtMark> lst = recentTxtMarkDAO.query__NON_CLOSE(//
                RecentTxtMarkSchmea.FILE_NAME + "=? ", //
                new String[]{fileName});
        return lst;
    }

    /**
     * 取得檔案mark紀錄
     */
    public synchronized List<RecentTxtMark> getFileMarkLike(String fileName) {
        List<RecentTxtMark> lst = recentTxtMarkDAO.query__NON_CLOSE(//
                RecentTxtMarkSchmea.FILE_NAME + " like  ? || '%' ", //
                new String[]{fileName});
        return lst;
    }

    /**
     * 計算全部筆數
     */
    public int countAll() {
        return recentTxtMarkDAO.countAll();
    }
}
