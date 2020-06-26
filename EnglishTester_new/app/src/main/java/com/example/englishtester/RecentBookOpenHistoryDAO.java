package com.example.englishtester;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.englishtester.common.Log;

import com.example.englishtester.common.DBUtil;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RecentBookOpenHistoryDAO {

    private static final String TAG = RecentBookOpenHistoryDAO.class.getSimpleName();

    final DBConnection helper;
    final Context context;

    private final Transformer transferToEntity = new Transformer<Cursor, RecentBookOpenHistoryDAO.RecentBookOpenHistory>() {
        public RecentBookOpenHistoryDAO.RecentBookOpenHistory transform(Cursor input) {
            return RecentBookOpenHistoryDAO.this.transferWord(input);
        }
    };


    public RecentBookOpenHistoryDAO(Context context) {
        this.context = context;
        helper = new DBConnection(context);
    }

    //取得相關黨名的書籤
    public List<RecentBookOpenHistory> queryBookList() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();

        String table = RecentBookOpenHistorySchema.TABLE_NAME;
        String[] columns = null;
        String selection = null;
        String[] selectionArgs = new String[]{};
        String groupBy = null;
        String having = null;
        String orderBy = RecentBookOpenHistorySchema.LATEST_OPEN_DATETIME + " DESC ";
        String limit = null;

        Cursor c = db.query(table, columns, selection,
                selectionArgs, groupBy, having,
                orderBy, limit);

        return DBUtil.transferToLst(c, db, transferToEntity);
    }

    public int countAll() {
        String sql = String.format("select count() as CNT from %s", RecentBookOpenHistorySchema.TABLE_NAME);
        List<Map<String, Object>> lst = DBUtil.queryBySQL_realType(sql, new String[0], context);
        if (lst.isEmpty()) {
            return -1;
        }
        Integer intVal = (Integer) (lst.get(0).get("CNT"));
        return intVal;
    }

    public String[] queryAllWord() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(RecentBookOpenHistorySchema.TABLE_NAME, RecentBookOpenHistorySchema.FROM, null, null, null, null, null);
        c.moveToFirst();
        String[] list = new String[c.getCount()];
        for (int ii = 0; ii < list.length; ii++) {
            list[ii] = c.getString(0);
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public List<RecentBookOpenHistory> query(String whereCondition, String[] whereArray) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(RecentBookOpenHistorySchema.TABLE_NAME, RecentBookOpenHistorySchema.FROM, whereCondition, whereArray, null, null, null);
        c.moveToFirst();
        List<RecentBookOpenHistory> list = new ArrayList<RecentBookOpenHistory>();
        int total = c.getCount();
        if (total == 0) {
            return list;
        }
        for (int ii = 0; ii < total; ii++) {
            list.add(transferWord(c));
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public List<RecentBookOpenHistory> query__NON_CLOSE(String whereCondition, String[] whereArray) {
        SQLiteDatabase db = null;
        for (; db == null || !db.isOpen(); ) {
            db = DBConnection.getInstance(context).getReadableDatabase();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        Cursor c = db.query(RecentBookOpenHistorySchema.TABLE_NAME, RecentBookOpenHistorySchema.FROM, whereCondition, whereArray, null, null, null);
        c.moveToFirst();
        List<RecentBookOpenHistory> list = new ArrayList<RecentBookOpenHistory>();
        int total = c.getCount();
        if (total == 0) {
            return list;
        }
        for (int ii = 0; ii < total; ii++) {
            list.add(transferWord(c));
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public List<RecentBookOpenHistory> queryAll() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(RecentBookOpenHistorySchema.TABLE_NAME, RecentBookOpenHistorySchema.FROM, null, null, null, null, null);
        c.moveToFirst();
        List<RecentBookOpenHistory> list = new ArrayList<RecentBookOpenHistory>();
        int total = c.getCount();
        if (total == 0) {
            return list;
        }
        for (int ii = 0; ii < total; ii++) {
            list.add(transferWord(c));
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public RecentBookOpenHistory findByPk(String bookName) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        String where = RecentBookOpenHistorySchema.BOOK_NAME + " = ? ";
        String[] condition = new String[]{bookName};
        Cursor c = db.query(RecentBookOpenHistorySchema.TABLE_NAME, RecentBookOpenHistorySchema.FROM, where, condition, null, null, null);
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToFirst();
        RecentBookOpenHistory vo = transferWord(c);
        c.close();
        return vo;
    }

    private void validate(RecentBookOpenHistory vo) {
        if (StringUtils.isBlank(vo.bookName)) {
            throw new RuntimeException("bookName不可為空!");
        }
    }

    public long insert(RecentBookOpenHistory vo) {
        validate(vo);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(vo);
        long result = db.insert(RecentBookOpenHistorySchema.TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public int updateByVO(RecentBookOpenHistory vo) {
        validate(vo);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(vo);
        String where = RecentBookOpenHistorySchema.BOOK_NAME + " = ? ";
        String[] condition = new String[]{vo.bookName};
        int result = db.update(RecentBookOpenHistorySchema.TABLE_NAME, values, where, condition);
        db.close();
        return result;
    }

    public int deleteByPk(String bookName) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        String where = RecentBookOpenHistorySchema.BOOK_NAME + " = ? ";
        String[] condition = new String[]{bookName};
        int result = db.delete(RecentBookOpenHistorySchema.TABLE_NAME, where, condition);
        db.close();
        return result;
    }

    public int deleteByCondition(String whereCondition, String[] properties) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        int result = db.delete(RecentBookOpenHistorySchema.TABLE_NAME, whereCondition, properties);
        db.close();
        return result;
    }

    public int deleteAll() {
        throw new UnsupportedOperationException("尚不提供此操作!");
        /*
         SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
         int result = db.delete(RecentBookOpenHistorySchema.TABLE_NAME, null, null);
         db.close();
         return result;
         */
    }


    //關閉資料庫 
    public void close() {
        DBConnection.getInstance(context).close();
    }

    private RecentBookOpenHistory transferWord(Cursor c) {
        RecentBookOpenHistory vo = new RecentBookOpenHistory();
        vo.bookName = c.getString(c.getColumnIndex(RecentBookOpenHistorySchema.BOOK_NAME));
        vo.filePath = c.getString(c.getColumnIndex(RecentBookOpenHistorySchema.FILE_PATH));
        vo.subName = c.getString(c.getColumnIndex(RecentBookOpenHistorySchema.SUB_NAME));
        vo.openTimes = c.getInt(c.getColumnIndex(RecentBookOpenHistorySchema.OPEN_TIMES));
        vo.latestOpenDatetime = c.getLong(c.getColumnIndex(RecentBookOpenHistorySchema.LATEST_OPEN_DATETIME));
        return vo;
    }

    private ContentValues transferWord(RecentBookOpenHistory vo) {
        ContentValues values = new ContentValues();
        values.put(RecentBookOpenHistorySchema.BOOK_NAME, vo.bookName);
        values.put(RecentBookOpenHistorySchema.FILE_PATH, vo.filePath);
        values.put(RecentBookOpenHistorySchema.SUB_NAME, vo.subName);
        values.put(RecentBookOpenHistorySchema.OPEN_TIMES, vo.openTimes);
        values.put(RecentBookOpenHistorySchema.LATEST_OPEN_DATETIME, vo.latestOpenDatetime);
        return values;
    }

    void showColumnInfo(Cursor c) {
        List<String> columnList = new ArrayList<String>();
        for (int jj = 0; jj < c.getColumnCount(); jj++) {
            columnList.add(jj + " - " + c.getColumnName(jj) + " = " + c.getString(jj));
        }
        Log.v(TAG, columnList.toString());
    }

    public static class RecentBookOpenHistory implements Serializable {
        String bookName;
        String filePath;
        String subName;
        Integer openTimes;
        Long latestOpenDatetime;
    }

    public interface RecentBookOpenHistorySchema {
        String TABLE_NAME = "recent_book_open_history";

        String BOOK_NAME = "book_name";
        String FILE_PATH = "file_path";
        String SUB_NAME = "sub_name";
        String OPEN_TIMES = "open_times";
        String LATEST_OPEN_DATETIME = "latest_open_datetime";

        final String[] FROM = {BOOK_NAME, FILE_PATH, SUB_NAME, OPEN_TIMES, LATEST_OPEN_DATETIME};
    }
}