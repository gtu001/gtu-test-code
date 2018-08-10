package com.example.englishtester;

import android.content.Context;
import android.os.Handler;
import com.example.englishtester.common.Log;
import android.widget.Toast;

import com.example.englishtester.RecentSearchDAO.RecentSearch;
import com.example.englishtester.RecentSearchDAO.RecentSearchSchema;
import com.example.englishtester.common.DBUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import gtu._work.etc.EnglishTester_Diectory;

public class RecentSearchService {

    private static final String TAG = RecentSearchService.class.getSimpleName();

    RecentSearchDAO recentSearchDAO;
    EnglishwordInfoDAO englishwordInfoDAO;
    EnglishTester_Diectory diectory = new EnglishTester_Diectory();
    Context context;

    public RecentSearchService(Context context) {
        recentSearchDAO = new RecentSearchDAO(context);
        englishwordInfoDAO = new EnglishwordInfoDAO(context);
        this.context = context;
    }

    /**
     * 紀錄查詢紀錄
     */
    public synchronized void recordRecentSearch(String englishId) {
        RecentSearch recent = recentSearchDAO.queryOneWord(englishId);
        boolean update = false;
        if (recent == null) {
            recent = new RecentSearch();
        } else {
            update = true;
        }
        recent.englishId = englishId;
        recent.insertDate = System.currentTimeMillis();
        recent.searchTime++;
        if (update) {
            recentSearchDAO.updateWord(recent);
        } else {
            recentSearchDAO.insertWord(recent);
        }
        Log.v(TAG, "recent - " + ReflectionToStringBuilder.toString(recent));
    }

    /**
     * 刪除歷史紀錄
     * 保留最後keepSize筆
     */
    public void deleteOldData(int keepSize) {
        long totalSize = recentSearchDAO.totalSize();
        Log.v(TAG, "總筆數 : " + totalSize);

        //刪除超過3個月資料
        if (true) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -3);
            String[] params = new String[]{String.valueOf(cal.getTimeInMillis())};
            int delSizeResult = recentSearchDAO.delete(RecentSearchSchema.INSERT_DATE + " <= ? ", params);
            Log.v(TAG, "刪除歷史紀錄筆數(超過3個月) : " + delSizeResult);
        }

        //刪除舊資料保留最後keepSize筆
        if (false) {
            List<RecentSearch> lst = recentSearchDAO.query("", new String[0], RecentSearchSchema.INSERT_DATE + " desc ");
            List<RecentSearch> lst2 = lst.subList(keepSize + 1, lst.size());
            for (RecentSearch v : lst2) {
                recentSearchDAO.deleteByWord(v.englishId);
            }
        }
    }

    /**
     * 查詢歷史紀錄
     * 最多查rowmax筆
     */
    public List<String> recentSearchHistory(int rowmax) {
        List<String> list = new ArrayList<String>();
        String sql = DBUtil.getRownumRawSql(RecentSearchSchema.TABLE_NAME, RecentSearchSchema.INSERT_DATE, false, " and rownum <= " + rowmax + " ");
        List<Map<String, String>> queryList = DBUtil.queryBySQL(sql, new String[]{}, context);
        for (Map<String, String> m : queryList) {
            String englishId = m.get(RecentSearchSchema.ENGLISH_ID);
            list.add(englishId);
        }
        return list;
    }

    /**
     * 查詢歷史紀錄
     * 最多查rowmax筆
     */
    public List<EnglishwordInfoDAO.EnglishWord> recentSearchHistoryForWord(int rowmax) {
        List<String> list = this.recentSearchHistory(rowmax + 50);
        List<EnglishwordInfoDAO.EnglishWord> rtnList = new ArrayList<>();
        int count = 0;
        for (String word : list) {
            EnglishwordInfoDAO.EnglishWord eng = englishwordInfoDAO.queryOneWord(word);
            if (eng == null) {
                eng = new EnglishwordInfoDAO.EnglishWord();
                EnglishTester_Diectory.WordInfo info = diectory.parseToWordInfo(word, context, null);
                eng.englishId = info.getEnglishId();
                eng.englishDesc = info.getMeaning();
                eng.pronounce = info.getPronounce();
            }
            if (eng != null && StringUtils.isNotBlank(eng.englishDesc)) {
                rtnList.add(eng);
                count++;
            } else {
                Log.v(TAG, "--> 忽略單字 : " + eng.englishId);
            }
            if (count >= rowmax) {
                break;
            }
        }
        Log.v(TAG, "recentSearchHistoryForWord size = " + rtnList.size());
        return rtnList;
    }
}
