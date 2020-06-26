package com.example.englishtester;

import android.content.Context;

import com.example.englishtester.common.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecentBookHistoryService {

    private static final String TAG = RecentBookHistoryService.class.getSimpleName();

    public static final String RECENT_OPEN_BOOK = "RECENT_OPEN_BOOK";

    final RecentBookOpenHistoryDAO dao;
    Context context;

    private static long ONE_MONTH_BEFORE_TIME = 0L;

    public RecentBookHistoryService(Context context) {
        this.context = context;
        this.dao = new RecentBookOpenHistoryDAO(context);
    }

    public void recordOpenBook(File file) {
        RecentBookOpenHistoryDAO.RecentBookOpenHistory vo = this.dao.findByPk(file.getName());
        boolean isUpdate = false;
        if (vo != null) {
            isUpdate = true;
        } else {
            vo = new RecentBookOpenHistoryDAO.RecentBookOpenHistory();
        }
        vo.latestOpenDatetime = System.currentTimeMillis();
        if (vo.openTimes == null) {
            vo.openTimes = 0;
        }
        vo.openTimes += 1;
        vo.filePath = file.getAbsolutePath();
        vo.bookName = file.getName();

        if (file.getName().endsWith(".epub")) {
            vo.subName = "epub";
        } else if (file.getName().endsWith(".pdf")) {
            vo.subName = "pdf";
        } else if (file.getName().endsWith(".mobi")) {
            vo.subName = "mobi";
        } else {
            vo.subName = "NA";
        }

        if (isUpdate) {
            dao.updateByVO(vo);
        } else {
            dao.insert(vo);
        }
    }

    public List<RecentBookOpenHistoryDAO.RecentBookOpenHistory> queryBookList() {
        try {
            return dao.queryBookList();
        } catch (Throwable ex) {
            Log.e(TAG, ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }
}
