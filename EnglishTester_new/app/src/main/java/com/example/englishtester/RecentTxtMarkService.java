package com.example.englishtester;

import android.content.Context;
import android.util.Log;

import com.example.englishtester.RecentTxtMarkDAO.RecentTxtMark;
import com.example.englishtester.RecentTxtMarkDAO.RecentTxtMarkSchmea;
import com.google.common.reflect.Reflection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

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
    public List<RecentTxtMark> getFileMark(String fileName) {
        List<RecentTxtMark> lst = recentTxtMarkDAO.query(//
                RecentTxtMarkSchmea.FILE_NAME + "=? ", //
                new String[]{fileName});
        return lst;
    }

    /**
     * 取得所有 scrollY紀錄
     */
    public static class ScrollYService {

        String fileName;
        RecentTxtMarkService service;

        public ScrollYService(String fileName, RecentTxtMarkService service) {
            this.fileName = fileName;
            this.service = service;
        }

        private RecentTxtMark getVO(int bookmarkType) {
            List<RecentTxtMark> list = service.recentTxtMarkDAO.query(//
                    RecentTxtMarkSchmea.FILE_NAME + "=? and " + //
                            RecentTxtMarkSchmea.BOOKMARK_TYPE + "=? ", //
                    new String[]{fileName, String.valueOf(bookmarkType)});

            if (!list.isEmpty()) {
                return list.get(0);
            }
            return null;
        }

        private RecentTxtMark getScrollYVO() {
            final int bookmarkType = RecentTxtMarkDAO.BookmarkTypeEnum.SCROLL_Y_POS.getType();
            return getVO(bookmarkType);
        }

        private RecentTxtMark getMaxHeightYVO() {
            final int bookmarkType = RecentTxtMarkDAO.BookmarkTypeEnum.SCROLLVIEW_HEIGHT.getType();
            return getVO(bookmarkType);
        }

        public int getScrollYVO_value() {
            RecentTxtMark vo = getScrollYVO();
            if (vo != null) {
                return vo.getScrollYPos();
            }
            return -1;
        }

        public int getMaxHeightYVO_value() {
            RecentTxtMark vo = getMaxHeightYVO();
            if (vo != null) {
                return vo.getScrollYPos();
            }
            return -1;
        }

        private RecentTxtMark createVO(int bookmarkType) {
            RecentTxtMark recentTxtVo = new RecentTxtMark();
            recentTxtVo.setFileName(fileName);
            recentTxtVo.setInsertDate(System.currentTimeMillis());
            recentTxtVo.setBookmarkType(bookmarkType);
            recentTxtVo.setMarkEnglish("");
            recentTxtVo.setMarkIndex(-1);
            return recentTxtVo;
        }

        public void updateCurrentScrollY(int currentScrollY) {
            RecentTxtMark vo1 = getScrollYVO();
            if (vo1 == null) {
                vo1 = createVO(RecentTxtMarkDAO.BookmarkTypeEnum.SCROLL_Y_POS.getType());
                vo1.setScrollYPos(currentScrollY);
                long result = service.recentTxtMarkDAO.insertWord(vo1);
                Log.v(TAG, "[updateCurrentScrollY] " + (result > 0 ? "[success]" : "[fail]") + ReflectionToStringBuilder.toString(vo1));
            } else {
                vo1.setScrollYPos(currentScrollY);
                long result = service.recentTxtMarkDAO.updateByVO(vo1);
                Log.v(TAG, "[updateCurrentScrollY] " + (result > 0 ? "[success]" : "[fail]") + ReflectionToStringBuilder.toString(vo1));
            }
        }

        public void updateMaxHeight(int maxHeight) {
            RecentTxtMark vo1 = getMaxHeightYVO();
            if (vo1 == null) {
                vo1 = createVO(RecentTxtMarkDAO.BookmarkTypeEnum.SCROLLVIEW_HEIGHT.getType());
                vo1.setScrollYPos(maxHeight);
                long result = service.recentTxtMarkDAO.insertWord(vo1);
                Log.v(TAG, "[updateMaxHeight] " + (result > 0 ? "[success]" : "[fail]") + ReflectionToStringBuilder.toString(vo1));
            } else {
                vo1.setScrollYPos(maxHeight);
                long result = service.recentTxtMarkDAO.updateByVO(vo1);
                Log.v(TAG, "[updateMaxHeight] " + (result > 0 ? "[success]" : "[fail]") + ReflectionToStringBuilder.toString(vo1));
            }
        }
    }

    /**
     * 計算全部筆數
     */
    public int countAll() {
        return recentTxtMarkDAO.countAll();
    }
}
