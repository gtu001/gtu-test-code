package com.example.gtuandroid.component;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MyProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.gjun.provider.myprovider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/medias");

    // for SQLite
    DBConnection helper;
    SQLiteDatabase db;

    interface MediaSchema {
        String TABLE_NAME = "MediaTable"; // Table Name
        String _ID = "_id"; // ID
        String MEDIA_NAME = "media_name"; // Media Name
        String MIME_TYPE = "mime_type"; // MIME Type
    }

    private static final int MEDIAS = 1;
    private static final int MEDIA_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "medias", MEDIAS);
        uriMatcher.addURI(PROVIDER_NAME, "medias/#", MEDIA_ID);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        deleteAll(); // 歸零
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // ---add a new media---
        SQLiteDatabase db = helper.getWritableDatabase();
        long rowID = db.insert(MediaSchema.TABLE_NAME, null, values);
        db.close();

        if (rowID != -1) { // ---if added successfully---
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        Log.i("MyProvider", CONTENT_URI.toString());
        helper = new DBConnection(this.getContext());
        deleteAll(); // 歸零
        return helper != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(MediaSchema.TABLE_NAME);

        if (uriMatcher.match(uri) == MEDIA_ID) {
            // ---if getting a particular
            // media---
            sqlBuilder.appendWhere(MediaSchema._ID + " = " + uri.getPathSegments().get(1));
        }

        db = helper.getReadableDatabase();
        Cursor c = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // ---register to watch a content URI for changes---
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
        case MEDIAS:
            count = db.update(MediaSchema.TABLE_NAME, values, selection, selectionArgs);
            break;
        case MEDIA_ID:
            count = db.update(//
                    MediaSchema.TABLE_NAME, //
                    values, //
                    MediaSchema._ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), //
                    selectionArgs//
                    );
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public void deleteAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(MediaSchema.TABLE_NAME, null, null);
        db.close();
    }

    // SQLiteOpenHelper-建立資料庫MediaDB和MediaTable
    public static class DBConnection extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "MediaDB";
        private static final int DATABASE_VERSION = 1;

        private DBConnection(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE " + MediaSchema.TABLE_NAME + " (" + //
                    MediaSchema._ID + " INTEGER primary key autoincrement, " + //
                    MediaSchema.MEDIA_NAME + " text not null, " + //
                    MediaSchema.MIME_TYPE + " text not null " + ");";
            db.execSQL(sql);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + MediaSchema.TABLE_NAME);
        }
    }
}
