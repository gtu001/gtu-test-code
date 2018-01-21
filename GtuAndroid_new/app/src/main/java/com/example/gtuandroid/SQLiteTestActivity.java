package com.example.gtuandroid;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SQLiteTestActivity extends Activity {

    String currentId;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_test);
        back();

        final EditText userNameText = (EditText) findViewById(R.id.editText1);
        final EditText telephoneText = (EditText) findViewById(R.id.editText2);
        final EditText addressText = (EditText) findViewById(R.id.editText3);
        final EditText mailAddressText = (EditText) findViewById(R.id.editText4);
        Button addBtn = (Button) findViewById(R.id.button1);
        Button updateBtn = (Button) findViewById(R.id.button2);
        Button deleteBtn = (Button) findViewById(R.id.button3);
        Button clearBtn = (Button) findViewById(R.id.button4);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);

        final DBConnection helper = new DBConnection(this);

        String[] list = null;
        {
            SQLiteDatabase db = helper.getWritableDatabase();
            //取得所有user_name放置在list上
            Cursor c = db.query(UserSchema.TABLE_NAME, new String[] { UserSchema.USER_NAME }, null, null, null, null, null);
            c.moveToFirst();
            list = new String[c.getCount()];
            for (int ii = 0; ii < list.length; ii++) {
                list[ii] = c.getString(0);
                c.moveToNext();
            }
            c.close();
        }

        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list));
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
                SQLiteDatabase db = helper.getWritableDatabase();
                String userName = paramAdapterView.getSelectedItem().toString();
                Cursor c = db.query(UserSchema.TABLE_NAME, UserSchema.FROM, "user_name='" + userName + "'", null, null, null, null);
                c.moveToFirst();
                currentId = c.getString(0);
                userNameText.setText(c.getString(1));
                telephoneText.setText(c.getString(2));
                addressText.setText(c.getString(3));
                mailAddressText.setText(c.getString(4));
                c.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> paramAdapterView) {
            }
        });

        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(UserSchema.USER_NAME, userNameText.getText().toString());
                values.put(UserSchema.TELEPHONE, telephoneText.getText().toString());
                values.put(UserSchema.ADDRESS, addressText.getText().toString());
                values.put(UserSchema.MAIL_ADDRESS, mailAddressText.getText().toString());
                db.insert(UserSchema.TABLE_NAME, null, values);
                db.close();
                onCreate(savedInstanceState);
            }
        });

        updateBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(UserSchema.USER_NAME, userNameText.getText().toString());
                values.put(UserSchema.TELEPHONE, telephoneText.getText().toString());
                values.put(UserSchema.ADDRESS, addressText.getText().toString());
                values.put(UserSchema.MAIL_ADDRESS, mailAddressText.getText().toString());
                String where = UserSchema.ID + "=" + currentId;
                db.update(UserSchema.TABLE_NAME, values, where, null);
                db.close();
                onCreate(savedInstanceState);
            }
        });

        deleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                SQLiteDatabase db = helper.getWritableDatabase();
                String where = UserSchema.ID + "=" + currentId;
                db.delete(UserSchema.TABLE_NAME, where, null);
                db.close();
                onCreate(savedInstanceState);
            }
        });

        clearBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                userNameText.setText("");
                telephoneText.setText("");
                addressText.setText("");
                mailAddressText.setText("");
            }
        });
    }

    //insert(table, nullColumnHack, values); 
    //=>nullColumnHack 當初使值為空白要設定為null, 因sql不允許新增一筆資料列為空白
    //=>values 對應到一筆資料列項的個別欄位初始直
    //
    //update(table, values, whereClause, whereArgs);
    //=>values 對應到一筆資料列項的個別欄位修訂直
    //=>whereClause 定義sql where語句用來設定查詢條件. 設定為null時表示所有列項都要更新
    //=>whereArgs 定義sql where語句的相關查詢參數
    //
    //delete(table, whereClause, whereArgs);
    //=>whereClause 定義sql where語句用來設定查詢條件. 設定為null時表示所有列項都要刪除
    //=>whereArgs 定義sql where語句的相關查詢參數, 在語句中有?的
    //
    //query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    //=>columns 檢索後回報欄位項目, 設定為null時會回報所有欄位項目, 不建議設為null, 一定要定義要求回報的欄位項目
    //=>selection 設定查詢條件, 和sql where語句相同, 但沒有where字眼. 設定為null表示所有列項都要回報
    //=>selectionArgs 定義sql where語句的相關查詢參數, 在語句中有?的
    //=>groupBy 設定群組條件, 和sql group by的語句相同, 但沒有group by字眼. 設定為null時表示沒有群組
    //=>having 設定哪一個群組被指定, 和sql having語句相同, 但沒有having字眼. 設定為null時表示所有群組都被指定
    //=>orderBy 設定排序的條件, 和sql order by語句相同, 但沒有order by字眼. 設定為null時表示不需要排序
    //=>limit 設定回報數量的條件, 和sql limit語句相同, 但呒有limit字眼. 設定為null表示沒有Limit

    interface UserSchema {
        String TABLE_NAME = "Users";
        String ID = "id";
        String USER_NAME = "user_name";
        String TELEPHONE = "telephone";
        String ADDRESS = "address";
        String MAIL_ADDRESS = "mail_address";
        String[] FROM = { ID, USER_NAME, TELEPHONE, ADDRESS, MAIL_ADDRESS };
    }

    static class DBConnection extends SQLiteOpenHelper {
        static final String DATABASE_NAME = "PhoneBookDB";
        static final int DATABASE_VERSION = 1;

        public DBConnection(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            //context - 用來創建/開啟資料庫
            //name - 資料庫名稱, 設定為null 表示資料庫建立於記憶體
            //factory - 用來建立Cursor所指的目標
            //version - 資料庫版本 , 可用Call Back程式 onUpdate()來更新資料庫
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            StringBuilder sb = new StringBuilder();
            sb.append("  create table Users (                         ");
            sb.append("      id INTEGER primary key autoincrement,    ");
            sb.append("      user_name text not null,                 ");
            sb.append("      telephone text not null,                 ");
            sb.append("      address text not null,                   ");
            sb.append("      mail_address text not null               ");
            sb.append("  );                                           ");
            Log.i("haiyang:createDB=", sb.toString());
            db.execSQL(sb.toString());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists Users");
        }
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                SQLiteTestActivity.this.setResult(RESULT_CANCELED, SQLiteTestActivity.this.getIntent());
                SQLiteTestActivity.this.finish();
            }
        });
    }
}
