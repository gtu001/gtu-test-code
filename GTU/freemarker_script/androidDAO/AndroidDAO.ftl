<#import "/lib.ftl" as my>  

<#function getColParamLst>
        <#local rtn = "">
        <#local lst = []>
        <#list columnLst as col>
                <#local lst = lst + [ getColParam(col) ]>
        </#list>
        <#local rtn = my.listJoin(lst, " , ")>
        <#return rtn>
</#function>
<#function getType col>
        <#return typeDef[col]>
</#function>
<#function getCursorType col>
        <#local rtn = typeDef[col]>
        <#if rtn == "Integer">
                <#local rtn = "Int">
        </#if>
        <#return rtn>
</#function>
<#function getColParam col>
        <#local rtn = my.javaToDbColumn(col)>
        <#local rtn = rtn?upper_case>
        <#return rtn>
</#function>
<#function getCol4Table col>
        <#local rtn = my.javaToDbColumn(col)>
        <#local rtn = rtn?lower_case>
        <#return rtn>
</#function>
<#function getWhereStr>
        <#local rtn = "">
        <#list pkColumns as col>
                <#if !col?is_first>
                        <#local rtn = rtn + " + ">
                </#if>
                <#if !col?is_last>
                        <#local rtn = rtn + className + "Schema." + getColParam(col) + "+ \" = ? and \"" >
                <#else>
                        <#local rtn = rtn + className + "Schema." + getColParam(col) + "+ \" = ? \"" >
                </#if>
        </#list>
        <#return rtn>
</#function>
<#function getWhereCondition>
        <#local rtn = my.fixArry(pkColumns, "vo.", "", false)>
        <#local rtn = my.listJoin(rtn, " , ")>
        <#local rtn = " new String[] { " + rtn + " } ">
        <#return rtn>
</#function>


package com.example.gtu001.qrcodemaker.dao;

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


public class ${className}DAO {

    private static final String TAG = ${className}DAO.class.getSimpleName();

        final DBConnection helper;
    final Context context;

    private final Transformer transferToEntity = new Transformer<Cursor, ${className}DAO.${className}>() {
        public ${className}DAO.${className} transform(Cursor input) {
            return ${className}DAO.this.transferWord(input);
        }
    };
    

    public ${className}DAO(Context context) {
        this.context = context;
        helper = new DBConnection(context);
    }

    //取得相關黨名的書籤
    public List<${className}DAO.${className}> queryBookmarkLikeLst(String fileName, int bookmarkType) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();

        String table = ${className}Schema.TABLE_NAME;
        String[] columns = null;
        String selection = String.format(" %1$s like   '%' and %2$s =  ", ${className}Schema.FILE_NAME, ${className}Schema.BOOKMARK_TYPE);
        String[] selectionArgs = new String[]{fileName, String.valueOf(bookmarkType)};
        String groupBy = null;
        String having = null;
        String orderBy = ${className}Schema.INSERT_DATE + " DESC ";
        String limit = null;

        Cursor c = db.query(table, columns, selection,
                selectionArgs, groupBy, having,
                orderBy, limit);

        return DBUtil.transferToLst(c, db, transferToEntity);
    }

    public int countAll() {
        String sql = String.format("select count() as CNT from %s" , ${className}Schema.TABLE_NAME);
        List<Map<String, Object>> lst = DBUtil.queryBySQL_realType(sql, new String[0], context);
        if (lst.isEmpty()) {
            return -1;
        }
        Integer intVal = (Integer) (lst.get(0).get("CNT"));
        return intVal;
    }

    public String[] queryAllWord() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(${className}Schema.TABLE_NAME, ${className}Schema.FROM, null, null, null, null, null);
        c.moveToFirst();
        String[] list = new String[c.getCount()];
        for (int ii = 0; ii < list.length; ii++) {
            list[ii] = c.getString(0);
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public List<${className}> query(String whereCondition, String[] whereArray) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(${className}Schema.TABLE_NAME, ${className}Schema.FROM, whereCondition, whereArray, null, null, null);
        c.moveToFirst();
        List<${className}> list = new ArrayList<${className}>();
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

    public List<${className}> query__NON_CLOSE(String whereCondition, String[] whereArray) {
        SQLiteDatabase db = null;
        for (; db == null || !db.isOpen(); ) {
            db = DBConnection.getInstance(context).getReadableDatabase();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        Cursor c = db.query(${className}Schema.TABLE_NAME, ${className}Schema.FROM, whereCondition, whereArray, null, null, null);
        c.moveToFirst();
        List<${className}> list = new ArrayList<${className}>();
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

    public List<${className}> queryAll() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(${className}Schema.TABLE_NAME, ${className}Schema.FROM, null, null, null, null, null);
        c.moveToFirst();
        List<${className}> list = new ArrayList<${className}>();
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

    public ${className} findByPk(Long listId) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        String where = ${getWhereStr()};
        String[] condition = ${getWhereCondition()};
        Cursor c = db.query(${className}Schema.TABLE_NAME, ${className}Schema.FROM, where, condition, null, null, null);
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToFirst();
        ${className} vo = transferWord(c);
        c.close();
        return vo;
    }

    private void validate(${className} vo) {
        if (StringUtils.isBlank(vo.fileName)) {
            throw new RuntimeException("fileName不可為空!");
        }
    }

    public long insert(${className} vo) {
        validate(vo);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(vo);
        long result = db.insert(${className}Schema.TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public int updateByVO(${className} vo) {
        validate(vo);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(vo);
        String where = ${getWhereStr()};
        String[] condition = ${getWhereCondition()};
        int result = db.update(${className}Schema.TABLE_NAME, values, where, condition);
        db.close();
        return result;
    }

    public int deleteByPk(String currentId) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        String where = ${getWhereStr()};
        String[] condition = ${getWhereCondition()};
        int result = db.delete(${className}Schema.TABLE_NAME, where, condition);
        db.close();
        return result;
    }

    public int deleteByCondition(String whereCondition, String[] properties) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        int result = db.delete(${className}Schema.TABLE_NAME, whereCondition, properties);
        db.close();
        return result;
    }

    public int deleteAll() {
        throw new UnsupportedOperationException("尚不提供此操作!");
        /*
         SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
         int result = db.delete(${className}Schema.TABLE_NAME, null, null);
         db.close();
         return result;
         */
    }

    
    //關閉資料庫 
    public void close() {
        DBConnection.getInstance(context).close();
    }

    private ${className} transferWord(Cursor c) {
        ${className} vo = new ${className}();
        <#list columnLst as col>
        vo.${col} = c.get${getCursorType(col)}(c.getColumnIndex(${className}Schema.${getColParam(col)}));
        </#list>
        return vo;
    }

    private ContentValues transferWord(${className} vo) {
        ContentValues values = new ContentValues();
        <#list columnLst as col>
        values.put(${className}Schema.${getColParam(col)}, vo.${col});
        </#list>
        return values;
    }

    void showColumnInfo(Cursor c) {
        List<String> columnList = new ArrayList<String>();
        for (int jj = 0; jj < c.getColumnCount(); jj++) {
            columnList.add(jj + " - " + c.getColumnName(jj) + " = " + c.getString(jj));
        }
        Log.v(TAG, columnList.toString());
    }

    public static class ${className} implements Serializable {
        <#list columnLst as col>
        ${getType(col)} ${col};
        </#list>
    }

    public interface ${className}Schema {
        String TABLE_NAME = "${tableName}";
    
        <#list columnLst as col>
        String ${getColParam(col)} = "${getCol4Table(col)}";
        </#list>    

        final String[] FROM = {${getColParamLst()}};
    }
}
