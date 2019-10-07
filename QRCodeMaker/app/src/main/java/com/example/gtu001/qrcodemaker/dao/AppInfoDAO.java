

package com.example.gtu001.qrcodemaker.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AppInfoDAO {

    private static final String TAG = AppInfoDAO.class.getSimpleName();

    final DBConnection helper;
    final Context context;

    private final Transformer transferToEntity = new Transformer<Cursor, AppInfoDAO.AppInfo>() {
        public AppInfoDAO.AppInfo transform(Cursor input) {
            return AppInfoDAO.this.transferWord(input);
        }
    };


    public AppInfoDAO(Context context) {
        this.context = context;
        helper = new DBConnection(context);
    }

    //取得相關黨名的書籤
    /*
    public List<AppInfoDAO.AppInfo> queryBookmarkLikeLst(String fileName, int bookmarkType) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();

        String table = AppInfoSchema.TABLE_NAME;
        String[] columns = null;
        String selection = String.format(" %1$s like   '%' and %2$s =  ", AppInfoSchema.FILE_NAME, AppInfoSchema.BOOKMARK_TYPE);
        String[] selectionArgs = new String[]{fileName, String.valueOf(bookmarkType)};
        String groupBy = null;
        String having = null;
        String orderBy = AppInfoSchema.INSERT_DATE + " DESC ";
        String limit = null;

        Cursor c = db.query(table, columns, selection,
                selectionArgs, groupBy, having,
                orderBy, limit);

        return DBUtil.transferToLst(c, db, transferToEntity);
    }
    */

    public int countAll() {
        String sql = String.format("select count() as CNT from %s", AppInfoSchema.TABLE_NAME);
        List<Map<String, Object>> lst = DBUtil.queryBySQL_realType(sql, new String[0], context);
        if (lst.isEmpty()) {
            return -1;
        }
        Integer intVal = (Integer) (lst.get(0).get("CNT"));
        return intVal;
    }

    public String[] queryAllWord() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(AppInfoSchema.TABLE_NAME, AppInfoSchema.FROM, null, null, null, null, null);
        c.moveToFirst();
        String[] list = new String[c.getCount()];
        for (int ii = 0; ii < list.length; ii++) {
            list[ii] = c.getString(0);
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public List<AppInfo> query(String whereCondition, String[] whereArray) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(AppInfoSchema.TABLE_NAME, AppInfoSchema.FROM, whereCondition, whereArray, null, null, null);
        c.moveToFirst();
        List<AppInfo> list = new ArrayList<AppInfo>();
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

    public List<AppInfo> query__NON_CLOSE(String whereCondition, String[] whereArray) {
        SQLiteDatabase db = null;
        for (; db == null || !db.isOpen(); ) {
            db = DBConnection.getInstance(context).getReadableDatabase();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        Cursor c = db.query(AppInfoSchema.TABLE_NAME, AppInfoSchema.FROM, whereCondition, whereArray, null, null, null);
        c.moveToFirst();
        List<AppInfo> list = new ArrayList<AppInfo>();
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

    public List<AppInfo> queryAll() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(AppInfoSchema.TABLE_NAME, AppInfoSchema.FROM, null, null, null, null, null);
        c.moveToFirst();
        List<AppInfo> list = new ArrayList<AppInfo>();
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

    public AppInfo findByPk(String installedPackage) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        String where = AppInfoSchema.INSTALLED_PACKAGE + " = ? ";
        String[] condition = new String[]{installedPackage};
        Cursor c = db.query(AppInfoSchema.TABLE_NAME, AppInfoSchema.FROM, where, condition, null, null, null);
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToFirst();
        AppInfo vo = transferWord(c);
        c.close();
        return vo;
    }

    private void validate(AppInfo vo) {
//        if (StringUtils.isBlank(vo.fileName)) {
//            throw new RuntimeException("fileName不可為空!");
//        }
    }

    public long insert(AppInfo vo) {
        validate(vo);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(vo);
        long result = db.insert(AppInfoSchema.TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public int updateByVO(AppInfo vo) {
        validate(vo);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(vo);
        String where = AppInfoSchema.INSTALLED_PACKAGE + " = ? ";
        String[] condition = new String[]{vo.installedPackage};
        int result = db.update(AppInfoSchema.TABLE_NAME, values, where, condition);
        db.close();
        return result;
    }

    public int deleteByPk(String currentId) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        String where = AppInfoSchema.INSTALLED_PACKAGE + " = ? ";
        String[] condition = new String[]{currentId};
        int result = db.delete(AppInfoSchema.TABLE_NAME, where, condition);
        db.close();
        return result;
    }

    public int deleteByCondition(String whereCondition, String[] properties) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        int result = db.delete(AppInfoSchema.TABLE_NAME, whereCondition, properties);
        db.close();
        return result;
    }

    public int deleteAll() {
        throw new UnsupportedOperationException("尚不提供此操作!");
        /*
         SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
         int result = db.delete(AppInfoSchema.TABLE_NAME, null, null);
         db.close();
         return result;
         */
    }


    //關閉資料庫 
    public void close() {
        DBConnection.getInstance(context).close();
    }

    private AppInfo transferWord(Cursor c) {
        AppInfo vo = new AppInfo();
        vo.installedPackage = c.getString(c.getColumnIndex(AppInfoSchema.INSTALLED_PACKAGE));
        vo.sourceDir = c.getString(c.getColumnIndex(AppInfoSchema.SOURCE_DIR));
        vo.label = c.getString(c.getColumnIndex(AppInfoSchema.LABEL));
        return vo;
    }

    private ContentValues transferWord(AppInfo vo) {
        ContentValues values = new ContentValues();
        values.put(AppInfoSchema.INSTALLED_PACKAGE, vo.installedPackage);
        values.put(AppInfoSchema.SOURCE_DIR, vo.sourceDir);
        values.put(AppInfoSchema.LABEL, vo.label);
        return values;
    }

    void showColumnInfo(Cursor c) {
        List<String> columnList = new ArrayList<String>();
        for (int jj = 0; jj < c.getColumnCount(); jj++) {
            columnList.add(jj + " - " + c.getColumnName(jj) + " = " + c.getString(jj));
        }
//        Log.v(TAG, columnList.toString());
    }

    public static class AppInfo implements Serializable {
        String installedPackage;
        String sourceDir;
        String label;

        public String getInstalledPackage() {
            return installedPackage;
        }

        public void setInstalledPackage(String installedPackage) {
            this.installedPackage = installedPackage;
        }

        public String getSourceDir() {
            return sourceDir;
        }

        public void setSourceDir(String sourceDir) {
            this.sourceDir = sourceDir;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    public interface AppInfoSchema {
        String TABLE_NAME = "App_Info";

        String INSTALLED_PACKAGE = "installed_package";
        String SOURCE_DIR = "source_dir";
        String LABEL = "label";

        final String[] FROM = {INSTALLED_PACKAGE, SOURCE_DIR, LABEL};
    }
}