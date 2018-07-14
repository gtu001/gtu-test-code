package com.example.englishtester;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RecentTxtMarkDAO {

    private static final String TAG = RecentTxtMarkDAO.class.getSimpleName();

    //    final DBConnection helper;
    final Context context;

    public RecentTxtMarkDAO(Context context) {
        this.context = context;
//        helper = new DBConnection(context);
    }

    String[] queryAllWord() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(RecentTxtMarkSchmea.TABLE_NAME, RecentTxtMarkSchmea.FROM, null, null, null, null, null);
        c.moveToFirst();
        String[] list = new String[c.getCount()];
        for (int ii = 0; ii < list.length; ii++) {
            list[ii] = c.getString(0);
            c.moveToNext();
        }
        c.close();
        return list;
    }

    List<RecentTxtMark> query(String whereCondition, String[] whereArray) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(RecentTxtMarkSchmea.TABLE_NAME, RecentTxtMarkSchmea.FROM, whereCondition, whereArray, null, null, null);
        c.moveToFirst();
        List<RecentTxtMark> list = new ArrayList<RecentTxtMark>();
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

    List<RecentTxtMark> queryAll() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(RecentTxtMarkSchmea.TABLE_NAME, RecentTxtMarkSchmea.FROM, null, null, null, null, null);
        c.moveToFirst();
        List<RecentTxtMark> list = new ArrayList<RecentTxtMark>();
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

    RecentTxtMark queryByListId(Long listId) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(RecentTxtMarkSchmea.TABLE_NAME, RecentTxtMarkSchmea.FROM, RecentTxtMarkSchmea.LIST_ID + "=?", new String[]{String.valueOf(listId)}, null, null, null);
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToFirst();
        RecentTxtMark word = transferWord(c);
        c.close();
        return word;
    }

    private void validationWord(RecentTxtMark word) {
        if (StringUtils.isBlank(word.fileName)) {
            throw new RuntimeException("fileName不可為空!");
        }
        if (word.markIndex == -1) {
            throw new RuntimeException("markIndex不可為空!");
        }
        if (StringUtils.isBlank(word.markEnglish)) {
            throw new RuntimeException("markEnglish不可為空!");
        }
        if (word.insertDate == -1) {
            throw new RuntimeException("insertDate不可為空!");
        }
    }

    long insertWord(RecentTxtMark word) {
        validationWord(word);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(word);
        long result = db.insert(RecentTxtMarkSchmea.TABLE_NAME, null, values);
        db.close();
        return result;
    }

    int updateByVO(RecentTxtMark word) {
        validationWord(word);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(word);
        String where = RecentTxtMarkSchmea.LIST_ID + "=?";
        int result = db.update(RecentTxtMarkSchmea.TABLE_NAME, values, where, new String[]{String.valueOf(word.listId)});
        db.close();
        return result;
    }

    int deleteByListId(String currentId) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        String where = RecentTxtMarkSchmea.LIST_ID + "=?";
        int result = db.delete(RecentTxtMarkSchmea.TABLE_NAME, where, new String[]{currentId});
        db.close();
        return result;
    }

    int deleteByCondition(String whereCondition, String[] properties) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        int result = db.delete(RecentTxtMarkSchmea.TABLE_NAME, whereCondition, properties);
        db.close();
        return result;
    }

    int deleteAll() {
        throw new UnsupportedOperationException("尚不提供此操作!");
        // SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        // int result = db.delete(RecentTxtMarkSchmea.TABLE_NAME, null, null);
        // db.close();
        // return result;
    }

    /**
     * 關閉資料庫
     */
    public void close() {
        DBConnection.getInstance(context).close();
    }

    private RecentTxtMark transferWord(Cursor c) {
        RecentTxtMark word = new RecentTxtMark();
        word.listId = c.getLong(c.getColumnIndex(RecentTxtMarkSchmea.LIST_ID));
        word.fileName = c.getString(c.getColumnIndex(RecentTxtMarkSchmea.FILE_NAME));
        word.markIndex = c.getInt(c.getColumnIndex(RecentTxtMarkSchmea.MARK_INDEX));
        word.markEnglish = c.getString(c.getColumnIndex(RecentTxtMarkSchmea.MARK_ENGLISH));
        word.insertDate = c.getLong(c.getColumnIndex(RecentTxtMarkSchmea.INSERT_DATE));
        word.scrollYPos = c.getInt(c.getColumnIndex(RecentTxtMarkSchmea.SCROLL_Y_POS));
        word.bookmarkType = c.getInt(c.getColumnIndex(RecentTxtMarkSchmea.BOOKMARK_TYPE));
        return word;
    }

    private ContentValues transferWord(RecentTxtMark word) {
        ContentValues values = new ContentValues();
        values.put(RecentTxtMarkSchmea.LIST_ID, word.listId);
        values.put(RecentTxtMarkSchmea.FILE_NAME, word.fileName);
        values.put(RecentTxtMarkSchmea.MARK_INDEX, word.markIndex);
        values.put(RecentTxtMarkSchmea.MARK_ENGLISH, word.markEnglish);
        values.put(RecentTxtMarkSchmea.INSERT_DATE, word.insertDate);
        values.put(RecentTxtMarkSchmea.SCROLL_Y_POS, word.scrollYPos);
        values.put(RecentTxtMarkSchmea.BOOKMARK_TYPE, word.bookmarkType);
        return values;
    }

    void showColumnInfo(Cursor c) {
        List<String> columnList = new ArrayList<String>();
        for (int jj = 0; jj < c.getColumnCount(); jj++) {
            columnList.add(jj + " - " + c.getColumnName(jj) + " = " + c.getString(jj));
        }
        Log.v(TAG, columnList.toString());
    }

    public static class RecentTxtMark implements Serializable {
        private static final long serialVersionUID = -8975363169799885680L;
        Long listId;
        String fileName;
        int markIndex = -1;
        String markEnglish;
        long insertDate = -1L;
        int scrollYPos = 0;
        int bookmarkType;//0:不適書籤 | 1:是書籤

        public Long getListId() {
            return listId;
        }

        public void setListId(Long listId) {
            this.listId = listId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public int getMarkIndex() {
            return markIndex;
        }

        public void setMarkIndex(int markIndex) {
            this.markIndex = markIndex;
        }

        public String getMarkEnglish() {
            return markEnglish;
        }

        public void setMarkEnglish(String markEnglish) {
            this.markEnglish = markEnglish;
        }

        public long getInsertDate() {
            return insertDate;
        }

        public void setInsertDate(long insertDate) {
            this.insertDate = insertDate;
        }

        public int getScrollYPos() {
            return scrollYPos;
        }

        public void setScrollYPos(int scrollYPos) {
            this.scrollYPos = scrollYPos;
        }

        public int getBookmarkType() {
            return bookmarkType;
        }

        public void setBookmarkType(int bookmarkType) {
            this.bookmarkType = bookmarkType;
        }
    }

    interface RecentTxtMarkSchmea {
        String TABLE_NAME = "recent_txt_mark";
        String LIST_ID = "list_id";
        String FILE_NAME = "file_name";
        String MARK_INDEX = "mark_index";
        String MARK_ENGLISH = "mark_english";
        String INSERT_DATE = "insert_date";
        String SCROLL_Y_POS = "scroll_y_pos";
        String BOOKMARK_TYPE = "bookmark_type";
        final String[] FROM = {LIST_ID, FILE_NAME, MARK_INDEX, MARK_ENGLISH, INSERT_DATE, SCROLL_Y_POS, BOOKMARK_TYPE};
    }

    public enum BookmarkTypeEnum {
        NONE(0),//
        BOOKMARK(1),//
        ;
        final int type;

        BookmarkTypeEnum(int type) {
            this.type = type;
        }

        public boolean isMatch(int value) {
            return value == type;
        }

        public int getType() {
            return type;
        }
    }
}
