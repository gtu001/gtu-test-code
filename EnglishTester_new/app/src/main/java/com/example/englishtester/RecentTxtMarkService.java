package com.example.englishtester;

import android.content.Context;
import android.util.Log;

import com.example.englishtester.RecentTxtMarkDAO.RecentTxtMark;
import com.example.englishtester.RecentTxtMarkDAO.RecentTxtMarkSchmea;
import com.example.englishtester.common.DBUtil;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.time.DateFormatUtils;

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
     * 新增查詢單字
     */
    public RecentTxtMark addMarkWord(final String fileName, final String word, final int index, boolean clickBookmark) {
        long currentTime = System.currentTimeMillis();
        RecentTxtMark bo = new RecentTxtMark();
        bo.fileName = fileName;
        bo.insertDate = currentTime;
        bo.markEnglish = word;
        bo.markIndex = index;
        bo.bookmarkType = getBookmarkType(null, clickBookmark);

        Callable<List<RecentTxtMark>> fetchLst = new Callable<List<RecentTxtMark>>() {
            public List<RecentTxtMark> call() throws Exception {
                List<RecentTxtMark> list = recentTxtMarkDAO.query(//
                        RecentTxtMarkSchmea.FILE_NAME + "=? and " + //
                                RecentTxtMarkSchmea.MARK_ENGLISH + "=? and " + //
                                RecentTxtMarkSchmea.MARK_INDEX + "=?", //
                        new String[]{fileName, word, String.valueOf(index)});
                return list;
            }
        };

        List<RecentTxtMark> list = Collections.emptyList();
        try {
            list = fetchLst.call();
        } catch (Exception ex) {
        }

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

        //debug ↓↓↓↓↓↓↓↓↓↓
        try {
            List<RecentTxtMark> lst2 = fetchLst.call();
            return lst2.get(0);
        } catch (Exception ex) {
            return null;
        }
        //debug ↑↑↑↑↑↑↑↑↑↑
    }

    private int getBookmarkType(Integer oldValue, boolean clickBookmarkAction) {
        if (oldValue == null) { //insert
            return clickBookmarkAction ? //
                    RecentTxtMarkDAO.BookmarkTypeEnum.BOOKMARK.getType() : //
                    RecentTxtMarkDAO.BookmarkTypeEnum.NONE.getType();
        } else { //update
            if (clickBookmarkAction) {
                if (oldValue == RecentTxtMarkDAO.BookmarkTypeEnum.NONE.getType()) {
                    return RecentTxtMarkDAO.BookmarkTypeEnum.BOOKMARK.getType();
                } else if (oldValue == RecentTxtMarkDAO.BookmarkTypeEnum.BOOKMARK.getType()) {
                    return RecentTxtMarkDAO.BookmarkTypeEnum.NONE.getType();
                }
            } else {
                return RecentTxtMarkDAO.BookmarkTypeEnum.NONE.getType();
            }
        }
        throw new RuntimeException("無法判斷的 BookmarkType : " + oldValue + " , " + clickBookmarkAction);
    }

    /**
     * 取得檔案查字清單
     */
    public List<RecentTxtMark> getFileMark(String fileName) {
        List<RecentTxtMark> list = recentTxtMarkDAO.query(//
                RecentTxtMarkSchmea.FILE_NAME + "=?", new String[]{fileName});
        return list;
    }

    /**
     * 刪除舊資料
     */
    public void deleteOldData() {
        if (true) {
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        for (int ii = 0; ii < 10; ii++)
            Log.v(TAG, "DELETE : " + DateFormatUtils.format(cal, "yyyy/MM/dd"));
        long daysBefore = cal.getTimeInMillis();

//        int count = recentTxtMarkDAO.deleteByCondition(RecentTxtMarkSchmea.INSERT_DATE + "<?", new String[]{String.valueOf(daysBefore)});
//        Log.v(TAG, "deleteOldData count " + count);

        for (RecentTxtMark vo : recentTxtMarkDAO.queryAll()) {
            if (vo.getInsertDate() < daysBefore) {
                recentTxtMarkDAO.deleteByListId(String.valueOf(vo.getListId()));
            }
        }
    }

    /**
     * 取得卷軸位置
     */
    public int getScrollViewYPos(String fileName) {
        List<RecentTxtMark> list = getFileMark(fileName);
        if (list.isEmpty()) {
            return 0;
        }
        RecentTxtMark vo = list.get(0);
        return vo.getScrollYPos();
    }

    /**
     * 更新卷軸位置
     */
    public boolean updateScrollViewYPos(String fileName, int y) {
        List<RecentTxtMark> list = getFileMark(fileName);
        if (list.isEmpty()) {
            return false;
        }
        RecentTxtMark vo = list.get(0);
        vo.setScrollYPos(y);
        return recentTxtMarkDAO.updateByVO(vo) > 0;
    }
}
