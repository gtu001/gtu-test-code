

package com.example.gtu001.qrcodemaker.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gtu001.qrcodemaker.common.Log;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class YoutubeVideoDAO {

    private static final String TAG = YoutubeVideoDAO.class.getSimpleName();

    final DBConnection helper;
    final Context context;

    private final Transformer transferToEntity = new Transformer<Cursor, YoutubeVideoDAO.YoutubeVideo>() {
        public YoutubeVideoDAO.YoutubeVideo transform(Cursor input) {
            return YoutubeVideoDAO.this.transferWord(input);
        }
    };


    public YoutubeVideoDAO(Context context) {
        this.context = context;
        helper = new DBConnection(context);
    }

    //取得相關黨名的書籤
    @Deprecated
    public List<YoutubeVideoDAO.YoutubeVideo> queryBookmarkLikeLst(String fileName, int bookmarkType) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();

        String table = YoutubeVideoSchema.TABLE_NAME;
        String[] columns = null;
        String selection = String.format(" %1$s like   '%' and %2$s =  ", YoutubeVideoSchema.TITLE, YoutubeVideoSchema.VIDEO_ID);
        String[] selectionArgs = new String[]{fileName, String.valueOf(bookmarkType)};
        String groupBy = null;
        String having = null;
        String orderBy = YoutubeVideoSchema.INSERT_DATE + " DESC ";
        String limit = null;

        Cursor c = db.query(table, columns, selection,
                selectionArgs, groupBy, having,
                orderBy, limit);

        return DBUtil.transferToLst(c, db, transferToEntity);
    }

    public int countAll() {
        String sql = String.format("select count() as CNT from %s", YoutubeVideoSchema.TABLE_NAME);
        List<Map<String, Object>> lst = DBUtil.queryBySQL_realType(sql, new String[0], context);
        if (lst.isEmpty()) {
            return -1;
        }
        Integer intVal = (Integer) (lst.get(0).get("CNT"));
        return intVal;
    }

    public String[] queryAllWord() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(YoutubeVideoSchema.TABLE_NAME, YoutubeVideoSchema.FROM, null, null, null, null, null);
        c.moveToFirst();
        String[] list = new String[c.getCount()];
        for (int ii = 0; ii < list.length; ii++) {
            list[ii] = c.getString(0);
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public List<YoutubeVideo> query(String whereCondition, String[] whereArray) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(YoutubeVideoSchema.TABLE_NAME, YoutubeVideoSchema.FROM, whereCondition, whereArray, null, null, null);
        c.moveToFirst();
        List<YoutubeVideo> list = new ArrayList<YoutubeVideo>();
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

    public List<YoutubeVideo> query__NON_CLOSE(String whereCondition, String[] whereArray) {
        SQLiteDatabase db = null;
        for (; db == null || !db.isOpen(); ) {
            db = DBConnection.getInstance(context).getReadableDatabase();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        Cursor c = db.query(YoutubeVideoSchema.TABLE_NAME, YoutubeVideoSchema.FROM, whereCondition, whereArray, null, null, null);
        c.moveToFirst();
        List<YoutubeVideo> list = new ArrayList<YoutubeVideo>();
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

    public List<YoutubeVideo> queryAll() {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        Cursor c = db.query(YoutubeVideoSchema.TABLE_NAME, YoutubeVideoSchema.FROM, null, null, null, null, null);
        c.moveToFirst();
        List<YoutubeVideo> list = new ArrayList<YoutubeVideo>();
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

    public YoutubeVideo findByPk(String videoId) {
        SQLiteDatabase db = DBConnection.getInstance(context).getReadableDatabase();
        String where = YoutubeVideoSchema.VIDEO_ID + " = ? ";
        String[] condition = new String[]{videoId};
        Cursor c = db.query(YoutubeVideoSchema.TABLE_NAME, YoutubeVideoSchema.FROM, where, condition, null, null, null);
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToFirst();
        YoutubeVideo vo = transferWord(c);
        c.close();
        return vo;
    }

    private void validate(YoutubeVideo vo) {
        if (StringUtils.isBlank(vo.videoId)) {
            throw new RuntimeException("videoId不可為空!");
        }
    }

    public long insert(YoutubeVideo vo) {
        validate(vo);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(vo);
        long result = db.insert(YoutubeVideoSchema.TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public int updateByVO(YoutubeVideo vo) {
        validate(vo);
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        ContentValues values = this.transferWord(vo);
        String where = YoutubeVideoSchema.VIDEO_ID + " = ? ";
        String[] condition = new String[]{vo.videoId};
        int result = db.update(YoutubeVideoSchema.TABLE_NAME, values, where, condition);
        db.close();
        return result;
    }

    public int deleteByPk(String videoId) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        String where = YoutubeVideoSchema.VIDEO_ID + " = ? ";
        String[] condition = new String[]{videoId};
        int result = db.delete(YoutubeVideoSchema.TABLE_NAME, where, condition);
        db.close();
        return result;
    }

    public int deleteByCondition(String whereCondition, String[] properties) {
        SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
        int result = db.delete(YoutubeVideoSchema.TABLE_NAME, whereCondition, properties);
        db.close();
        return result;
    }

    public int deleteAll() {
        throw new UnsupportedOperationException("尚不提供此操作!");
        /*
         SQLiteDatabase db = DBConnection.getInstance(context).getWritableDatabase();
         int result = db.delete(YoutubeVideoSchema.TABLE_NAME, null, null);
         db.close();
         return result;
         */
    }


    //關閉資料庫
    public void close() {
        DBConnection.getInstance(context).close();
    }

    private YoutubeVideo transferWord(Cursor c) {
        YoutubeVideo vo = new YoutubeVideo();
        vo.videoId = c.getString(c.getColumnIndex(YoutubeVideoSchema.VIDEO_ID));
        vo.title = c.getString(c.getColumnIndex(YoutubeVideoSchema.TITLE));
        vo.videoUrl = c.getString(c.getColumnIndex(YoutubeVideoSchema.VIDEO_URL));
        vo.clickTime = c.getInt(c.getColumnIndex(YoutubeVideoSchema.CLICK_TIME));
        vo.insertDate = c.getLong(c.getColumnIndex(YoutubeVideoSchema.INSERT_DATE));
        vo.latestClickDate = c.getLong(c.getColumnIndex(YoutubeVideoSchema.LATEST_CLICK_DATE));
        return vo;
    }

    private ContentValues transferWord(YoutubeVideo vo) {
        ContentValues values = new ContentValues();
        values.put(YoutubeVideoSchema.VIDEO_ID, vo.videoId);
        values.put(YoutubeVideoSchema.TITLE, vo.title);
        values.put(YoutubeVideoSchema.VIDEO_URL, vo.videoUrl);
        values.put(YoutubeVideoSchema.CLICK_TIME, vo.clickTime);
        values.put(YoutubeVideoSchema.INSERT_DATE, vo.insertDate);
        values.put(YoutubeVideoSchema.LATEST_CLICK_DATE, vo.latestClickDate);
        return values;
    }

    void showColumnInfo(Cursor c) {
        List<String> columnList = new ArrayList<String>();
        for (int jj = 0; jj < c.getColumnCount(); jj++) {
            columnList.add(jj + " - " + c.getColumnName(jj) + " = " + c.getString(jj));
        }
        Log.v(TAG, columnList.toString());
    }

    public static class YoutubeVideo implements Serializable {
        String videoId;
        String title;
        String videoUrl;
        Integer clickTime;
        Long insertDate;
        Long latestClickDate;

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public Integer getClickTime() {
            return clickTime;
        }

        public void setClickTime(Integer clickTime) {
            this.clickTime = clickTime;
        }

        public Long getInsertDate() {
            return insertDate;
        }

        public void setInsertDate(Long insertDate) {
            this.insertDate = insertDate;
        }

        public Long getLatestClickDate() {
            return latestClickDate;
        }

        public void setLatestClickDate(Long latestClickDate) {
            this.latestClickDate = latestClickDate;
        }
    }

    public interface YoutubeVideoSchema {
        String TABLE_NAME = "Youtube_Video";

        String VIDEO_ID = "video_id";
        String TITLE = "title";
        String VIDEO_URL = "video_url";
        String CLICK_TIME = "click_time";
        String INSERT_DATE = "insert_date";
        String LATEST_CLICK_DATE = "latest_click_date";

        final String[] FROM = {VIDEO_ID, TITLE, VIDEO_URL, CLICK_TIME, INSERT_DATE, LATEST_CLICK_DATE};
    }
}
