package com.example.gtuandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PhoneBookActivity extends ListActivity {

    List<Map<String, String>> bookData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_builtin);

        bookData = getPhoneBookData();

        SimpleAdapter adapter = new SimpleAdapter(this,//
                bookData,//
                android.R.layout.simple_list_item_2,//
                // new String[] { "name", "number" }, //
                new String[] { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER }, //
                new int[] { android.R.id.text1, android.R.id.text2 });//

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, String> data = bookData.get(position);
        String name = data.get("name");
        String number = data.get("number");
        Toast.makeText(this, "[撥打電話]姓名:" + name + ",電話:" + number, Toast.LENGTH_LONG).show();

        // 撥出電話
        Intent call = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number));
        startActivity(call);

        // 開啟聯絡人
        // Intent intent = new Intent(Intent.ACTION_VIEW);
        // intent.setData(Uri.parse("content://contacts/people/"));
        // startActivity(intent);

        // 開啟指定聯絡人
        // long phoneId =
        // Long.parseLong(data.get(ContactsContract.Contacts._ID));
        // Intent intent = new Intent(Intent.ACTION_VIEW);
        // intent.setData(Uri.parse("content://contacts/people/" + phoneId));
        // startActivity(intent);

        super.onListItemClick(l, v, position, id);
    }

    private List<Map<String, String>> getPhoneBookData() {
        List<Map<String, String>> contactsArrayList = new ArrayList<Map<String, String>>();
        Cursor contacts_name = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (contacts_name.moveToNext()) {
            Map<String, String> contactsMap = new HashMap<String, String>();
            String phoneNumber = "";
            long id = contacts_name.getLong(contacts_name.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor contacts_number = getContentResolver().query(//
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, //
                    null, //
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + Long.toString(id), //
                    null, null);

            while (contacts_number.moveToNext()) {
                phoneNumber = contacts_number.getString(//
                        contacts_number.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)//
                        );
            }
            contacts_number.close();
            String name = contacts_name.getString(contacts_name.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // contactsMap.put("name", name);
            // contactsMap.put("number", phoneNumber);
            contactsMap.put(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, name);
            contactsMap.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
            contactsArrayList.add(contactsMap);
        }
        return contactsArrayList;
    }

    private void insert(String name, String number) {
        ContentValues values = new ContentValues();

        Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);

        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
        values.put(StructuredName.GIVEN_NAME, name);
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        values.put(Phone.NUMBER, number);
        values.put(Phone.TYPE, Phone.TYPE_MOBILE);
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
    }

    private void delete(String name) {
        Cursor c = getContentResolver().query(//
                ContactsContract.Contacts.CONTENT_URI, null, //
                ContactsContract.Contacts.DISPLAY_NAME + "=" + "'" + name + "'", null, null);
        c.moveToFirst();
        String id = c.getString((c.getColumnIndex("_id")));
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newDelete(//
                ContactsContract.RawContacts.CONTENT_URI)//
                .withSelection(Data.CONTACT_ID + "=?", new String[] { id })//
                .build());
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }
}
