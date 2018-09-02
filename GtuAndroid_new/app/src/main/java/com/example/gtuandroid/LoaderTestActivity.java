package com.example.gtuandroid;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;

/**
 * Created by gtu001 on 2017/7/10.
 */

public class LoaderTestActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = LoaderTestActivity.class.getSimpleName();

    private SimpleCursorAdapter adapter;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_fragment_loader);

        adapter = new SimpleCursorAdapter(this,//
                android.R.layout.simple_list_item_1, null,//
                new String[]{"title"},//
                new int[]{android.R.id.text1}, 0);//

        ListView listView = (ListView)findViewById(android.R.id.list);
        listView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(0, saveInstanceState, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Uri.parse("content://browser/bookmarks"), null, null, null, null);//Browser.BOOKMARKS_URI
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
