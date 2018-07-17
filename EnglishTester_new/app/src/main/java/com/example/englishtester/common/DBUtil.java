package com.example.englishtester.common;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.englishtester.DBConnection;
import com.example.englishtester.RecentSearchDAO;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wistronits on 2018/6/27.
 */

public class DBUtil {

    private static final String TAG = DBUtil.class.getSimpleName();

    /**
     * 取得帶有rownum 的查詢 (小筆數可用)
     */
    public static String getRownumRawSql(String tableName, String orderColumn, boolean isAsc, String whereCondition) {
        String _orderType = isAsc ? " <= " : " >= ";
        String _orderType2 = isAsc ? " asc " : " desc ";
        StringBuilder sb = new StringBuilder();
        sb.append(" select * from ( ");
        sb.append("    select (select COUNT(0) ");
        sb.append("            from " + tableName + " t1 ");
        sb.append("            where t1." + orderColumn + " " + _orderType + " t2." + orderColumn + " ");
        sb.append("            ) as 'rownum', ");
        sb.append("    rowid, ");
        sb.append("    t2.* from " + tableName + " t2 ORDER BY " + orderColumn + " " + _orderType2 + " ");
        sb.append(" ) t where 1=1 " + whereCondition);
        return sb.toString();
    }

    /**
     * rawsql查詢
     */
    public static List<Map<String, String>> queryBySQL(String rawSql, String[] whereArray, Context context) {
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

    public static List<Map<String, Object>> queryBySQL_realType(String rawSql, String[] whereArray, Context context) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.rawQuery(rawSql, whereArray);
        c.moveToFirst();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int total = c.getCount();
        if (total == 0) {
            return new ArrayList<Map<String, Object>>();
        }
        for (int ii = 0; ii < total; ii++) {
            Map<String, Object> map = __getRealRowData(c);
            Log.v(TAG, "queryBySQL map - " + map);
            list.add(map);
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }

    public static List<Map<String, String>> getDbSchema(String tableName, Context context) {
        String[] conditions = new String[0];
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT lower(name), sql FROM sqlite_master   ");
        sb.append(" WHERE type = 'table'                  ");
        if (StringUtils.isNotBlank(tableName)) {
            sb.append(" and upper(name) = ? ");
            conditions = new String[]{tableName.toUpperCase()};
        }
        sb.append(" ORDER BY name                         ");
        return queryBySQL(sb.toString(), conditions, context);
    }

    private static Map<String, Object> __getRealRowData(Cursor c) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int jj = 0; jj < c.getColumnCount(); jj++) {
            String columnName = c.getColumnName(jj);
            int colIndex = c.getColumnIndex(columnName);
            int type = c.getType(colIndex);
            Object value = null;
            if (Cursor.FIELD_TYPE_NULL == type) {
            } else if (Cursor.FIELD_TYPE_INTEGER == type) {
                value = c.getInt(colIndex);
            } else if (Cursor.FIELD_TYPE_FLOAT == type) {
                value = c.getFloat(colIndex);
            } else if (Cursor.FIELD_TYPE_BLOB == type) {
                value = c.getBlob(colIndex);
            } else if (Cursor.FIELD_TYPE_STRING == type) {
                value = c.getString(colIndex);
            } else {
                Log.w(TAG, "Unknow DB Column Type : " + type);
                value = c.getString(colIndex);
            }
            map.put(columnName, value);
        }
        return map;
    }
}
