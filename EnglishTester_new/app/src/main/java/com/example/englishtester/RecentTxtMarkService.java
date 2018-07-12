package com.example.englishtester;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.example.englishtester.RecentTxtMarkDAO.RecentTxtMark;
import com.example.englishtester.RecentTxtMarkDAO.RecentTxtMarkSchmea;

import android.content.Context;
import android.util.Log;

public class RecentTxtMarkService {

    private static final String TAG = RecentTxtMarkService.class.getSimpleName();

    RecentTxtMarkDAO recentTxtMarkDAO;

    public RecentTxtMarkService(Context context) {
        recentTxtMarkDAO = new RecentTxtMarkDAO(context);
    }

    /**
     * 新增查詢單字
     */
    public void addMarkWord(String fileName, String word, int index) {
        long currentTime = System.currentTimeMillis();
        RecentTxtMark bo = new RecentTxtMark();
        bo.fileName = fileName;
        bo.insertDate = currentTime;
        bo.markEnglish = word;
        bo.markIndex = index;
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
            int result = recentTxtMarkDAO.updateByVO(bo);
            Log.v(TAG, "update [" + result + "]" + ReflectionToStringBuilder.toString(bo));
        }
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
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        long daysBefore = cal.getTimeInMillis();
        int count = recentTxtMarkDAO.deleteByCondition(RecentTxtMarkSchmea.INSERT_DATE + "<?", new String[]{String.valueOf(daysBefore)});
        Log.v(TAG, "deleteOldData count " + count);
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
