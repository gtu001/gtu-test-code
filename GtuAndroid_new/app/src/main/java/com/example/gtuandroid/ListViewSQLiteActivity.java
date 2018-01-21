package com.example.gtuandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.gtuandroid.component.ListViewDBHelper;

public class ListViewSQLiteActivity extends Activity {
    private ListViewDBHelper helper;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private ListView listInput;
    private EditText editText1;
    private Button addButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_listview_sqlite);

        helper = new ListViewDBHelper(getApplicationContext());
        cursor = helper.select();

        listInput = (ListView) findViewById(R.id.listInputText);
        editText1 = (EditText) findViewById(R.id.editText1);
        addButton = (Button) findViewById(R.id.add_button);

        adapter = new SimpleCursorAdapter(//
                this, //
                R.layout.subview_listview_sqlite, //
                cursor, //
                new String[] { "item_text" }, //
                new int[] { R.id.textView }//
        );

        // 添加並且顯示
        listInput.setAdapter(adapter);

        // 添加點擊
        listInput.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int index, long arg3) {
                setTitle("點擊第" + index + "個項目");

                final String[] aryShop = new String[] { "修改", "刪除" };
                new AlertDialog.Builder(ListViewSQLiteActivity.this)//
                        .setTitle("設定")//
                        .setItems(aryShop, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                Toast.makeText(ListViewSQLiteActivity.this, "選了" + aryShop[paramInt], Toast.LENGTH_SHORT).show();

                                if ("修改".equalsIgnoreCase(aryShop[paramInt])) {
                                    LayoutInflater inflater = ListViewSQLiteActivity.this.getLayoutInflater();
                                    View subView = inflater.inflate(R.layout.subview_dialog_singletext, null);
                                    final EditText updateText = (EditText) subView.findViewById(R.id.editText1);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ListViewSQLiteActivity.this);
                                    builder.setView(subView);
                                    builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                            update(index, updateText.getText().toString());
                                        }
                                    }).show();

                                } else if ("刪除".equalsIgnoreCase(aryShop[paramInt])) {
                                    delete(index);
                                }
                            }
                        }).show();
            }
        });

        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
    }

    private void insert() {
        helper.insert(editText1.getText().toString());
        cursor.requery();
        listInput.setAdapter(adapter);
    }

    private void update(int index, String text) {
        cursor.moveToPosition(index);
        helper.update(cursor.getInt(0), text);
        cursor.requery();
    }

    private void delete(int index) {
        cursor.moveToPosition(index);
        helper.delete(cursor.getInt(0));
        cursor.requery();
        listInput.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}
