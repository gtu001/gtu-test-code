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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecentSearchDAO {

    private static final String TAG = RecentSearchDAO.class.getSimpleName();

    //    final DBConnection helper;
    final Context context;

    private final Transformer transferToEntity = new Transformer<Cursor, RecentSearch>() {
        public RecentSearch transform(Cursor input) {
            return RecentSearchDAO.this.transferWord(input);
        }
    };

    public RecentSearchDAO(Context context) {
        this.context = context;
//        helper = new DBConnection(context);
    }

    /**
     * 查詢總筆數
     */
    long totalSize() {
        List<Map<String, String>> lst = DBUtil.queryBySQL("select count(*) as CNT from " + RecentSearchSchema.TABLE_NAME, new String[0], context);
        long result = Long.parseLong(lst.get(0).get("CNT"));
        return result;
    }

    String[] queryAllWord() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(RecentSearchSchema.TABLE_NAME, RecentSearchSchema.FROM, null, null, null, null, null);
        c.moveToFirst();
        String[] list = new String[c.getCount()];
        for (int ii = 0; ii < list.length; ii++) {
            list[ii] = c.getString(0);
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }

    List<RecentSearch> query(String whereCondition, String[] whereArray, String orderBy) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(RecentSearchSchema.TABLE_NAME, RecentSearchSchema.FROM, whereCondition, whereArray, null, null, orderBy);
        c.moveToFirst();
        List<RecentSearch> list = new ArrayList<RecentSearch>();
        int total = c.getCount();
        if (total == 0) {
            return list;
        }
        for (int ii = 0; ii < total; ii++) {
            list.add(transferWord(c));
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }

    List<RecentSearch> query(String whereCondition, String[] whereArray) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(RecentSearchSchema.TABLE_NAME, RecentSearchSchema.FROM, whereCondition, whereArray, null, null, null);
        c.moveToFirst();
        List<RecentSearch> list = new ArrayList<RecentSearch>();
        int total = c.getCount();
        if (total == 0) {
            return list;
        }
        for (int ii = 0; ii < total; ii++) {
            list.add(transferWord(c));
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }

    public List<RecentSearch> queryAll() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(RecentSearchSchema.TABLE_NAME, RecentSearchSchema.FROM, null, null, null, null, null);
        c.moveToFirst();
        List<RecentSearch> list = new ArrayList<RecentSearch>();
        int total = c.getCount();
        if (total == 0) {
            return list;
        }
        for (int ii = 0; ii < total; ii++) {
            list.add(transferWord(c));
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }

    RecentSearch queryOneWord(String currentId) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(RecentSearchSchema.TABLE_NAME, RecentSearchSchema.FROM, RecentSearchSchema.ENGLISH_ID + "=?", new String[]{currentId}, null, null, null);
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToFirst();
        RecentSearch word = transferWord(c);
        c.close();
        db.close();
        return word;
    }

    private void validationWord(RecentSearch word) {
        if (StringUtils.isBlank(word.englishId)) {
            throw new RuntimeException("單字不可為空!");
        }
    }

    long insertWord(RecentSearch word) {
        validationWord(word);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(word);
        long result = db.insert(RecentSearchSchema.TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public int updateWord(RecentSearch word) {
        validationWord(word);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(word);
        String where = RecentSearchSchema.ENGLISH_ID + "=?";
        int result = db.update(RecentSearchSchema.TABLE_NAME, values, where, new String[]{word.englishId});
        db.close();
        return result;
    }

    int deleteByWord(String currentId) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        String where = RecentSearchSchema.ENGLISH_ID + "=?";
        int result = db.delete(RecentSearchSchema.TABLE_NAME, where, new String[]{currentId});
        db.close();
        return result;
    }

    int delete(String whereCondition, String[] whereParams) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        int result = db.delete(RecentSearchSchema.TABLE_NAME, whereCondition, whereParams);
        db.close();
        return result;
    }

    int deleteAll() {
        throw new UnsupportedOperationException("尚不提供此操作!");
        // SQLiteDatabase db = helper.getWritableDatabase();
        // int result = db.delete(RecentSearchSchema.TABLE_NAME, null, null);
        // db.close();
        // return result;
    }

    //取得n比需上傳的單字
    public int queryNeedUploadSize() {
        String selection = String.format(" %1$s is null or %1$s = '' ", RecentSearchSchema.UPLOAD_TYPE);
        String[] selectionArgs = new String[0];

        String sql = "select count(*) as CNT from " + RecentSearchSchema.TABLE_NAME + //
                " where " + selection;

        List<Map<String, String>> lst = DBUtil.queryBySQL(sql, selectionArgs, context);
        int result = Integer.parseInt(lst.get(0).get("CNT"));
        return result;
    }

    //取得n比需上傳的單字
    public List<RecentSearch> queryNeedUpload(int limitSize) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();

        String table = RecentSearchSchema.TABLE_NAME;
        String[] columns = null;
        String selection = String.format(" %1$s is null or %1$s = '' ", RecentSearchSchema.UPLOAD_TYPE);
        String[] selectionArgs = new String[0];
        String groupBy = null;
        String having = null;
        String orderBy = RecentSearchSchema.INSERT_DATE + " DESC ";
        String limit = limitSize <= 0 ? null : String.valueOf(limitSize);

        Cursor c = db.query(table, columns, selection,
                selectionArgs, groupBy, having,
                orderBy, limit);

        return DBUtil.transferToLst(c, db, transferToEntity);
    }

    //取得n比需上傳的單字
    public List<RecentSearch> queryNeedUpload_byRegisterTime(long registerTime) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();

        String table = RecentSearchSchema.TABLE_NAME;
        String[] columns = null;
        String selection = String.format(" (%1$s is null or %1$s = '') and %2$s >= ? ", RecentSearchSchema.UPLOAD_TYPE, RecentSearchSchema.INSERT_DATE);
        String[] selectionArgs = new String[]{String.valueOf(registerTime)};
        String groupBy = null;
        String having = null;
        String orderBy = RecentSearchSchema.INSERT_DATE + " DESC ";
        String limit = null;

        Cursor c = db.query(table, columns, selection,
                selectionArgs, groupBy, having,
                orderBy, limit);

        return DBUtil.transferToLst(c, db, transferToEntity);
    }

    //取得查詢歷史紀錄
    public List<RecentSearch> queryRecentSearchHistory(int limitSize) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();

        String table = RecentSearchSchema.TABLE_NAME;
        String[] columns = null;
        String selection = "";
        String[] selectionArgs = new String[0];
        String groupBy = null;
        String having = null;
        String orderBy = RecentSearchSchema.INSERT_DATE + " DESC ";
        String limit = limitSize <= 0 ? null : String.valueOf(limitSize);

        Cursor c = db.query(table, columns, selection,
                selectionArgs, groupBy, having,
                orderBy, limit);

        return DBUtil.transferToLst(c, db, transferToEntity);
    }

    /**
     * 關閉資料庫
     */
    public void close() {
        DBConnection.getInstance(context).close();
    }

    private RecentSearch transferWord(Cursor c) {
        RecentSearch word = new RecentSearch();
        word.englishId = c.getString(c.getColumnIndex(RecentSearchSchema.ENGLISH_ID));
        word.insertDate = c.getLong(c.getColumnIndex(RecentSearchSchema.INSERT_DATE));
        word.searchTime = c.getInt(c.getColumnIndex(RecentSearchSchema.SEARCH_TIME));
        word.uploadType = c.getString(c.getColumnIndex(RecentSearchSchema.UPLOAD_TYPE));
        word.sentance = c.getString(c.getColumnIndex(RecentSearchSchema.SENTANCE));
        return word;
    }

    private ContentValues transferWord(RecentSearch word) {
        ContentValues values = new ContentValues();
        values.put(RecentSearchSchema.ENGLISH_ID, word.englishId);
        values.put(RecentSearchSchema.INSERT_DATE, word.insertDate);
        values.put(RecentSearchSchema.SEARCH_TIME, word.searchTime);
        values.put(RecentSearchSchema.UPLOAD_TYPE, word.uploadType);
        values.put(RecentSearchSchema.SENTANCE, word.sentance);
        return values;
    }

    void showColumnInfo(Cursor c) {
        List<String> columnList = new ArrayList<String>();
        for (int jj = 0; jj < c.getColumnCount(); jj++) {
            columnList.add(jj + " - " + c.getColumnName(jj) + " = " + c.getString(jj));
        }
        Log.v(TAG, columnList.toString());
    }

    public static class RecentSearch implements Serializable {
        private static final long serialVersionUID = -8975363169799885680L;
        String englishId;
        String sentance;
        long insertDate;
        int searchTime;
        String uploadType;

        public String getEnglishId() {
            return englishId;
        }

        public void setEnglishId(String englishId) {
            this.englishId = englishId;
        }

        public long getInsertDate() {
            return insertDate;
        }

        public void setInsertDate(long insertDate) {
            this.insertDate = insertDate;
        }

        public int getSearchTime() {
            return searchTime;
        }

        public void setSearchTime(int searchTime) {
            this.searchTime = searchTime;
        }

        public String getUploadType() {
            return uploadType;
        }

        public void setUploadType(String uploadType) {
            this.uploadType = uploadType;
        }

        public String getSentance() {
            return sentance;
        }

        public void setSentance(String sentance) {
            this.sentance = sentance;
        }
    }

    public interface RecentSearchSchema {
        String TABLE_NAME = "recent_search";
        String ENGLISH_ID = "english_id";
        String INSERT_DATE = "insert_date";
        String SEARCH_TIME = "search_time";
        String UPLOAD_TYPE = "upload_type";
        String SENTANCE = "sentance";
        final String[] FROM = {ENGLISH_ID, INSERT_DATE, SEARCH_TIME, UPLOAD_TYPE, SENTANCE};
    }
}
