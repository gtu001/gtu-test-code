package com.example.englishtester;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RecentSearchDAO {

    private static final String TAG = RecentSearchDAO.class.getSimpleName();

    //    final DBConnection helper;
    final Context context;

    public RecentSearchDAO(Context context) {
        this.context = context;
//        helper = new DBConnection(context);
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

    List<RecentSearch> queryAll() {
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

    /**
     * 取得帶有rownum 的查詢
     */
    public String getRownumRawSql(String tableName, String orderColumn, String orderType, String whereCondition) {
        String compareType = "asc".equalsIgnoreCase(orderType) ? " <= " : " >= ";
        StringBuilder sb = new StringBuilder();
        sb.append(" select * from ( ");
        sb.append(" select (select COUNT(0) ");
        sb.append(" from " + tableName + " t1 ");
        sb.append(" where t1." + orderColumn + " " + compareType + " t2." + orderColumn + " ");
        sb.append(" ) as 'rownum', rowid, t2.* from " + tableName + " t2 ORDER BY " + RecentSearchSchema.INSERT_DATE + " desc ");
        sb.append(" ) t where 1=1 " + whereCondition);
        return sb.toString();
    }

    /**
     * rawsql查詢
     */
    public List<Map<String, String>> queryBySQL(String rawSql, String[] whereArray) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.rawQuery(rawSql, whereArray);
        c.moveToFirst();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        int total = c.getCount();
        if (total == 0) {
            return new ArrayList<Map<String, String>>();
        }
        for (int ii = 0; ii < total; ii++) {
            Map<String, String> map = new LinkedHashMap<String, String>();
            for (int jj = 0; jj < c.getColumnCount(); jj++) {
                String columnName = c.getColumnName(jj);
                String val = c.getString(c.getColumnIndex(columnName));
                map.put(columnName, val);
            }
            Log.v(TAG, "queryBySQL map - " + map);
            list.add(map);
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

    int updateWord(RecentSearch word) {
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

    int deleteAll() {
        throw new UnsupportedOperationException("尚不提供此操作!");
        // SQLiteDatabase db = helper.getWritableDatabase();
        // int result = db.delete(RecentSearchSchema.TABLE_NAME, null, null);
        // db.close();
        // return result;
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
        return word;
    }

    private ContentValues transferWord(RecentSearch word) {
        ContentValues values = new ContentValues();
        values.put(RecentSearchSchema.ENGLISH_ID, word.englishId);
        values.put(RecentSearchSchema.INSERT_DATE, word.insertDate);
        values.put(RecentSearchSchema.SEARCH_TIME, word.searchTime);
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
        long insertDate;
        int searchTime;
    }

    interface RecentSearchSchema {
        String TABLE_NAME = "recent_search";
        String ENGLISH_ID = "english_id";
        String INSERT_DATE = "insert_date";
        String SEARCH_TIME = "search_time";
        final String[] FROM = {ENGLISH_ID, INSERT_DATE, SEARCH_TIME};
    }
}
