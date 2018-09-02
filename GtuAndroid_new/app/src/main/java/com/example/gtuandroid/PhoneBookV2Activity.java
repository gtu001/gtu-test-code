package com.example.gtuandroid;

import java.io.InputStream;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class PhoneBookV2Activity extends ListActivity {
    
    private ListAdapter mListAdapter;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_builtin);
        
        String[] columns = {ContactsContract.Contacts.DISPLAY_NAME,//
                ContactsContract.Contacts.PHOTO_ID,//
                ContactsContract.Contacts._ID,//
                };
        
        Cursor contactCursor = getContentResolver().query(//
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        
        int c = contactCursor.getCount();
        if(c == 0){
            Toast.makeText(this, "無聯絡人資料", Toast.LENGTH_LONG).show();
        }
        
        startManagingCursor(contactCursor);
        
        int[] entries = {android.R.id.text1, android.R.id.text2};

        mListAdapter = new SimpleCursorAdapter(this,//
                android.R.layout.simple_list_item_2,//
                contactCursor,//
                columns, //
                entries);//

        setListAdapter(mListAdapter);
    }
    
    private Bitmap loadContactPhotp(ContentResolver cr, long id){
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        Log.d("Contact", uri.toString());
        if(input != null){
            return BitmapFactory.decodeStream(input);
        }else{
            Log.d("Contact", "first try failed to load photo");
        }
        return null;
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        //取得點選的cursor
        Cursor c = (Cursor)mListAdapter.getItem(position);
        
        //取得id直
        int contactId = c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID));
        int photoId = c.getInt(c.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
        Log.v("Contact", "photoId = " + photoId);
        
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_imageview_simple);
        if(photoId == 0){
            dialog.setTitle("缺照片");
        }else {
            dialog.setTitle("照片");
        }
        ImageView iv = (ImageView)dialog.findViewById(R.id.image_view);
        Bitmap bitmap = loadContactPhotp(getContentResolver(), contactId);
        iv.setImageBitmap(bitmap);
        
        dialog.show();
        super.onListItemClick(l, iv, position, id);
    }
}
