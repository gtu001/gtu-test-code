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


public class DropboxHtmlDAO {

    private static final String TAG = DropboxHtmlDAO.class.getSimpleName();

    final DBConnection helper;
    final Context context;

    private final Transformer transferToEntity = new Transformer<Cursor, DropboxHtmlDAO.DropboxHtml>() {
        public DropboxHtmlDAO.DropboxHtml transform(Cursor input) {
            return DropboxHtmlDAO.this.transferWord(input);
        }
    };


    public DropboxHtmlDAO(Context context) {
        this.context = context;
        helper = new DBConnection(context);
    }

    //取得相關黨名的書籤
    public List<DropboxHtmlDAO.DropboxHtml> queryBookmarkLikeLst(String fileName, int bookmarkType) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();

        String table = DropboxHtmlSchema.TABLE_NAME;
        String[] columns = null;
        String selection = String.format(" %1$s like   '%' and %2$s =  ", DropboxHtmlSchema.FILE_NAME);
        String[] selectionArgs = new String[]{fileName, String.valueOf(bookmarkType)};
        String groupBy = null;
        String having = null;
        String orderBy = ""; //DropboxHtmlSchema.INSERT_DATE + " DESC ";
        String limit = null;

        Cursor c = db.query(table, columns, selection,
                selectionArgs, groupBy, having,
                orderBy, limit);

        return DBUtil.transferToLst(c, db, transferToEntity);
    }

    public int countAll() {
        String sql = String.format("select count() as CNT from %s", DropboxHtmlSchema.TABLE_NAME);
        List<Map<String, Object>> lst = DBUtil.queryBySQL_realType(sql, new String[0], context);
        if (lst.isEmpty()) {
            return -1;
        }
        Integer intVal = (Integer) (lst.get(0).get("CNT"));
        return intVal;
    }

    public String[] queryAllWord() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(DropboxHtmlSchema.TABLE_NAME, DropboxHtmlSchema.FROM, null, null, null, null, null);
        c.moveToFirst();
        String[] list = new String[c.getCount()];
        for (int ii = 0; ii < list.length; ii++) {
            list[ii] = c.getString(0);
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public List<DropboxHtml> query(String whereCondition, String[] whereArray) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(DropboxHtmlSchema.TABLE_NAME, DropboxHtmlSchema.FROM, whereCondition, whereArray, null, null, null);
        c.moveToFirst();
        List<DropboxHtml> list = new ArrayList<DropboxHtml>();
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

    public List<DropboxHtml> query__NON_CLOSE(String whereCondition, String[] whereArray) {
        SQLiteDatabase db = null;
        for (; db == null || !db.isOpen(); ) {
            db = DBConnection.getInstance(context).getReadableDatabase();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        Cursor c = db.query(DropboxHtmlSchema.TABLE_NAME, DropboxHtmlSchema.FROM, whereCondition, whereArray, null, null, null);
        c.moveToFirst();
        List<DropboxHtml> list = new ArrayList<DropboxHtml>();
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

    public List<DropboxHtml> queryAll() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(DropboxHtmlSchema.TABLE_NAME, DropboxHtmlSchema.FROM, null, null, null, null, null);
        c.moveToFirst();
        List<DropboxHtml> list = new ArrayList<DropboxHtml>();
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

    public DropboxHtml findByPk(String fileName) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        String where = DropboxHtmlSchema.FILE_NAME + " = ? ";
        String[] condition = new String[]{fileName};
        Cursor c = db.query(DropboxHtmlSchema.TABLE_NAME, DropboxHtmlSchema.FROM, where, condition, null, null, null);
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToFirst();
        DropboxHtml vo = transferWord(c);
        c.close();
        return vo;
    }

    private void validate(DropboxHtml vo) {
        if (StringUtils.isBlank(vo.fileName)) {
            throw new RuntimeException("fileName不可為空!");
        }
    }

    public long insert(DropboxHtml vo) {
        validate(vo);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(vo);
        long result = db.insert(DropboxHtmlSchema.TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public int updateByVO(DropboxHtml vo) {
        validate(vo);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(vo);
        String where = DropboxHtmlSchema.FILE_NAME + " = ? ";
        String[] condition = new String[]{vo.fileName};
        int result = db.update(DropboxHtmlSchema.TABLE_NAME, values, where, condition);
        db.close();
        return result;
    }

    public int deleteByPk(String fileName) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        String where = DropboxHtmlSchema.FILE_NAME + " = ? ";
        String[] condition = new String[]{fileName};
        int result = db.delete(DropboxHtmlSchema.TABLE_NAME, where, condition);
        db.close();
        return result;
    }

    public int deleteByFullPath(String fullPath) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        String where = DropboxHtmlSchema.FULL_PATH + " = ? ";
        String[] condition = new String[]{fullPath};
        int result = db.delete(DropboxHtmlSchema.TABLE_NAME, where, condition);
        db.close();
        return result;
    }


    public int deleteByCondition(String whereCondition, String[] properties) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        int result = db.delete(DropboxHtmlSchema.TABLE_NAME, whereCondition, properties);
        db.close();
        return result;
    }

    public int deleteAll() {
        //throw new UnsupportedOperationException("尚不提供此操作!");
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        int result = db.delete(DropboxHtmlSchema.TABLE_NAME, null, null);
        db.close();
        return result;
    }


    //關閉資料庫 
    public void close() {
        DBConnection.getInstance(context).close();
    }

    private DropboxHtml transferWord(Cursor c) {
        DropboxHtml vo = new DropboxHtml();
        vo.fileName = c.getString(c.getColumnIndex(DropboxHtmlSchema.FILE_NAME));
        vo.uploadDate = c.getLong(c.getColumnIndex(DropboxHtmlSchema.UPLOAD_DATE));
        vo.fileSize = c.getLong(c.getColumnIndex(DropboxHtmlSchema.FILE_SIZE));
        vo.fullPath = c.getString(c.getColumnIndex(DropboxHtmlSchema.FULL_PATH));
        return vo;
    }

    private ContentValues transferWord(DropboxHtml vo) {
        ContentValues values = new ContentValues();
        values.put(DropboxHtmlSchema.FILE_NAME, vo.fileName);
        values.put(DropboxHtmlSchema.UPLOAD_DATE, vo.uploadDate);
        values.put(DropboxHtmlSchema.FILE_SIZE, vo.fileSize);
        values.put(DropboxHtmlSchema.FULL_PATH, vo.fullPath);
        return values;
    }

    void showColumnInfo(Cursor c) {
        List<String> columnList = new ArrayList<String>();
        for (int jj = 0; jj < c.getColumnCount(); jj++) {
            columnList.add(jj + " - " + c.getColumnName(jj) + " = " + c.getString(jj));
        }
        Log.v(TAG, columnList.toString());
    }

    public static class DropboxHtml implements Serializable {
        String fileName;
        Long uploadDate;
        Long fileSize;
        String fullPath;
    }

    public interface DropboxHtmlSchema {
        String TABLE_NAME = "Dropbox_Html";

        String FILE_NAME = "file_name";
        String UPLOAD_DATE = "upload_date";
        String FILE_SIZE = "file_size";
        String FULL_PATH = "full_path";

        final String[] FROM = {FILE_NAME, UPLOAD_DATE, FILE_SIZE, FULL_PATH};
    }
}
