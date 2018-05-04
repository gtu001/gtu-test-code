package com.example.englishtester;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class EnglishwordInfoDAO {

    private static final String TAG = EnglishwordInfoDAO.class.getSimpleName();

    //    final DBConnection helper;
    final Context context;
    private static QueryAllKeeper qryAllKeeper;

    public EnglishwordInfoDAO(Context context) {
        this.context = context;
//        helper = new DBConnection(context);
    }

    public int sizeOfWords() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) as CNT from " + EnglishWordSchema.TABLE_NAME, new String[0]);
        c.moveToFirst();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        int total = c.getCount();
        if (total == 0) {
            return 0;
        }
        String columnName = c.getColumnName(0);
        int val = c.getInt(c.getColumnIndex(columnName));
        c.close();
        db.close();
        return val;
    }

    private String[] _queryAllWord() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(EnglishWordSchema.TABLE_NAME, EnglishWordSchema.FROM, null, null, null, null, null);
        c.moveToFirst();
        String[] list = new String[c.getCount()];
        for (int ii = 0; ii < list.length; ii++) {
            list[ii] = c.getString(0);
            c.moveToNext();
        }
        c.close();
        return list;
    }

    private List<EnglishWord> _queryAll() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(EnglishWordSchema.TABLE_NAME, EnglishWordSchema.FROM, null, null, null, null, null);
        c.moveToFirst();
        List<EnglishWord> list = new ArrayList<EnglishWord>();
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

    public String[] queryAllWord(boolean renew) {
        String[] orign = getQryAllKeeper().getAllEngList(renew);
        String[] ary = new String[orign.length];
        System.arraycopy(orign, 0, ary, 0, orign.length);
        return ary;
    }

    public List<EnglishWord> queryAll(boolean renew) {
        return (List<EnglishWord>) new ArrayList<EnglishWord>(getQryAllKeeper().getAllList(renew)).clone();
    }

    /*
    * 從資料備份檔匯入
     */
    void importData(List<EnglishWord> englishList, boolean doImportAsNewWord) {
//        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        try {
            db.beginTransaction();
            int count = 0;
            for (EnglishWord word : englishList) {
                if (doImportAsNewWord) {
                    initNewWord(word);
                }
                validationWord(word);
                ContentValues values = this.transferWord(word);
                long result = -1;
                try {
                    result = db.insert(EnglishWordSchema.TABLE_NAME, null, values);
                    Log.v(TAG, "insert - " + result);
                } catch (android.database.sqlite.SQLiteConstraintException ex) {
                    Log.v(TAG, "insert data exists!");
                } finally {
                    if (result <= 0) {
                        long result2 = db.update(EnglishWordSchema.TABLE_NAME, values, EnglishWordSchema.ENGLISH_ID + " = ? ", new String[]{word.englishId});
                        Log.v(TAG, "update - " + result2);
                    }
                }
                count++;
            }
            db.setTransactionSuccessful();
            Log.v(TAG, "DAO匯入成功, 總筆數 : " + count);
        } catch (Exception ex) {
            //若呼叫endTransaction無setSuccessful則會自動rollback
            Log.e(TAG, "ERR : " + ex.getMessage(), ex);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 初始化單字
     */
    private void initNewWord(EnglishWord word) {
        word.browserTime = 0;
        word.examTime = 0;
        word.failTime = 0;
        //word.insertDate = 0;
        word.examDate = 0;
        word.lastbrowerDate = 0;
        word.lastDuring = 0;
        word.lastResult = LastResultEnum.NOANSWER.val;
    }

    List<EnglishWord> query(String whereCondition, String[] whereArray) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(EnglishWordSchema.TABLE_NAME, EnglishWordSchema.FROM, whereCondition, whereArray, null, null, null);
        c.moveToFirst();
        List<EnglishWord> list = new ArrayList<EnglishWord>();
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


    private EnglishWord _queryOneWord(String currentId) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(EnglishWordSchema.TABLE_NAME, EnglishWordSchema.FROM, EnglishWordSchema.ENGLISH_ID + "=?", new String[]{currentId}, null, null, null);
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToFirst();
        EnglishWord word = transferWord(c);
        c.close();
        return word;
    }

    public EnglishWord queryOneWord(String currentId) {
        List<EnglishWord> allLst = getQryAllKeeper().getAllList(false);
        if (!allLst.isEmpty()) {
            for (EnglishWord e : allLst) {
                if (StringUtils.equalsIgnoreCase(e.englishId, currentId)) {
                    return e;
                }
            }
        }
        return _queryOneWord(currentId);
    }

    private void validationWord(EnglishWord word) {
        if (StringUtils.isBlank(word.englishId)) {
            throw new RuntimeException("單字不可為空!");
        }
    }

    long insertWord(EnglishWord word) {
        validationWord(word);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(word);
        long result = db.insert(EnglishWordSchema.TABLE_NAME, null, values);
        db.close();
        return result;
    }

    int updateWord(EnglishWord word) {
        validationWord(word);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(word);
        String where = EnglishWordSchema.ENGLISH_ID + "=?";
        int result = db.update(EnglishWordSchema.TABLE_NAME, values, where, new String[]{word.englishId});
        db.close();
        return result;
    }

    int deleteByWord(String currentId) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        String where = EnglishWordSchema.ENGLISH_ID + "=?";
        int result = db.delete(EnglishWordSchema.TABLE_NAME, where, new String[]{currentId});
        db.close();
        return result;
    }

    int deleteAll() {
        throw new UnsupportedOperationException("尚不提供此操作!");
//        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
//        int result = db.delete(EnglishWordSchema.TABLE_NAME, null, null);
//        db.close();
//        return result;
    }

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ queryAllKeeper
    private class QueryAllKeeper {
        private List<EnglishWord> allList;
        private String[] allEngList;

        private List<EnglishWord> getAllList(boolean renew) {
            if (allList == null || allList.isEmpty() || renew) {
                Log.v(TAG, "#REAL_QUERY - _queryAll");
                allList = Collections.unmodifiableList(_queryAll());
            }
            return allList;
        }

        private String[] getAllEngList(boolean renew) {
            if (allEngList == null || allEngList.length == 0 || renew) {
                Log.v(TAG, "#REAL_QUERY - _queryAllWord");
                allEngList = _queryAllWord();
            }
            return allEngList;
        }
    }

    private QueryAllKeeper getQryAllKeeper() {
        if (qryAllKeeper == null) {
            qryAllKeeper = new QueryAllKeeper();
        }
        return qryAllKeeper;
    }
    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ queryAllKeeper

    /**
     * 關閉資料庫
     */
    public void close() {
        DBConnection.getInstance(context).close();
    }

    private EnglishWord transferWord(Cursor c) {
        EnglishWord word = new EnglishWord();
        word.englishId = c.getString(c.getColumnIndex(EnglishWordSchema.ENGLISH_ID));
        word.englishDesc = c.getString(c.getColumnIndex(EnglishWordSchema.ENGLISH_DESC));
        word.pronounce = c.getString(c.getColumnIndex(EnglishWordSchema.PRONOUNCE));
        word.browserTime = c.getInt(c.getColumnIndex(EnglishWordSchema.BROWSER_TIME));
        word.examTime = c.getInt(c.getColumnIndex(EnglishWordSchema.EXAM_TIME));
        word.failTime = c.getInt(c.getColumnIndex(EnglishWordSchema.FAIL_TIME));
        word.insertDate = c.getLong(c.getColumnIndex(EnglishWordSchema.INSERT_DATE));
        word.examDate = c.getLong(c.getColumnIndex(EnglishWordSchema.EXAM_DATE));
        word.lastbrowerDate = c.getLong(c.getColumnIndex(EnglishWordSchema.LASTBROWSER_DATE));
        word.lastDuring = c.getLong(c.getColumnIndex(EnglishWordSchema.LAST_DURING));
        word.lastResult = c.getInt(c.getColumnIndex(EnglishWordSchema.LAST_RESULT));
        return word;
    }

    private ContentValues transferWord(EnglishWord word) {
        ContentValues values = new ContentValues();
        values.put(EnglishWordSchema.ENGLISH_ID, word.englishId);
        values.put(EnglishWordSchema.ENGLISH_DESC, word.englishDesc);
        values.put(EnglishWordSchema.PRONOUNCE, word.pronounce);
        values.put(EnglishWordSchema.BROWSER_TIME, word.browserTime);
        values.put(EnglishWordSchema.EXAM_TIME, word.examTime);
        values.put(EnglishWordSchema.FAIL_TIME, word.failTime);
        values.put(EnglishWordSchema.INSERT_DATE, word.insertDate);
        values.put(EnglishWordSchema.EXAM_DATE, word.examDate);
        values.put(EnglishWordSchema.LASTBROWSER_DATE, word.lastbrowerDate);
        values.put(EnglishWordSchema.LAST_DURING, word.lastDuring);
        values.put(EnglishWordSchema.LAST_RESULT, word.lastResult);
        return values;
    }

    void showColumnInfo(Cursor c) {
        List<String> columnList = new ArrayList<String>();
        for (int jj = 0; jj < c.getColumnCount(); jj++) {
            columnList.add(jj + " - " + c.getColumnName(jj) + " = " + c.getString(jj));
        }
        Log.v(TAG, columnList.toString());
    }

    public static class EnglishWord implements Serializable {
        private static final long serialVersionUID = -358966271246450347L;

        String englishId;
        String englishDesc;
        String pronounce;
        int browserTime;
        int examTime;
        int failTime;
        long insertDate;
        long examDate;
        long lastbrowerDate;
        long lastDuring;
        int lastResult;
        String btnAppendix;//按鈕附加資訊

        public String getEnglishId() {
            return englishId;
        }

        public void setEnglishId(String englishId) {
            this.englishId = englishId;
        }

        public String getEnglishDesc() {
            return englishDesc;
        }

        public void setEnglishDesc(String englishDesc) {
            this.englishDesc = englishDesc;
        }

        public String getPronounce() {
            return pronounce;
        }

        public void setPronounce(String pronounce) {
            this.pronounce = pronounce;
        }

        public int getBrowserTime() {
            return browserTime;
        }

        public void setBrowserTime(int browserTime) {
            this.browserTime = browserTime;
        }

        public int getExamTime() {
            return examTime;
        }

        public void setExamTime(int examTime) {
            this.examTime = examTime;
        }

        public int getFailTime() {
            return failTime;
        }

        public void setFailTime(int failTime) {
            this.failTime = failTime;
        }

        public long getInsertDate() {
            return insertDate;
        }

        public void setInsertDate(long insertDate) {
            this.insertDate = insertDate;
        }

        public long getExamDate() {
            return examDate;
        }

        public void setExamDate(long examDate) {
            this.examDate = examDate;
        }

        public long getLastbrowerDate() {
            return lastbrowerDate;
        }

        public void setLastbrowerDate(long lastbrowerDate) {
            this.lastbrowerDate = lastbrowerDate;
        }

        public long getLastDuring() {
            return lastDuring;
        }

        public void setLastDuring(long lastDuring) {
            this.lastDuring = lastDuring;
        }

        public int getLastResult() {
            return lastResult;
        }

        public void setLastResult(int lastResult) {
            this.lastResult = lastResult;
        }

        public String getBtnAppendix() {
            return btnAppendix;
        }

        public void setBtnAppendix(String btnAppendix) {
            this.btnAppendix = btnAppendix;
        }

        @Override
        public String toString() {
            return "EnglishWord [englishId=" + englishId + ", englishDesc=" + englishDesc + ", pronounce=" + pronounce
                    + ", browserTime=" + browserTime + ", examTime=" + examTime + ", failTime=" + failTime
                    + ", insertDate=" + insertDate + ", examDate=" + examDate + ", lastbrowerDate=" + lastbrowerDate
                    + ", lastDuring=" + lastDuring + ", lastResult=" + lastResult + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((englishId == null) ? 0 : englishId.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            EnglishWord other = (EnglishWord) obj;
            if (englishId == null) {
                if (other.englishId != null)
                    return false;
            } else if (!englishId.equals(other.englishId))
                return false;
            return true;
        }
    }

    interface EnglishWordSchema {
        String TABLE_NAME = "English";
        String ENGLISH_ID = "english_id";
        String ENGLISH_DESC = "english_desc";
        String PRONOUNCE = "pronounce";
        String BROWSER_TIME = "browser_time";
        String EXAM_TIME = "exam_time";
        String FAIL_TIME = "fail_time";
        String INSERT_DATE = "insert_date";
        String EXAM_DATE = "exam_date";
        String LASTBROWSER_DATE = "lastbrower_date";
        String LAST_DURING = "last_during";
        String LAST_RESULT = "last_result";
        final String[] FROM = {ENGLISH_ID, ENGLISH_DESC, PRONOUNCE, BROWSER_TIME, EXAM_TIME, FAIL_TIME, INSERT_DATE, EXAM_DATE, LASTBROWSER_DATE, LAST_DURING, LAST_RESULT};
    }

    enum LastResultEnum {
        CORRECT(1, "正確"), WRONG(2, "錯誤"), NOANSWER(0, "無作答");
        final int val;
        final String result;

        LastResultEnum(int val, String result) {
            this.val = val;
            this.result = result;
        }

        static String getStatus(int val) {
            for (LastResultEnum e : LastResultEnum.values()) {
                if (val == e.val) {
                    return e.result;
                }
            }
            return "";
        }
    }
}
