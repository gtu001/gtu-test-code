package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

public class OptionsMenuActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);

        // 按住超過一秒會顯示contextMenu
        registerForContextMenu(findViewById(R.id.button01));
        // registerForContextMenu(findViewById(R.id.relativeLayout1));//登記兩次menu會double

        
        //Popup Menu ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        final Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(OptionsMenuActivity.this, button2);
                pop.getMenuInflater().inflate(R.menu.activity_main, pop.getMenu());
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getApplicationContext(), item.getTitle() + "-" + item.getItemId(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                pop.show();
            }
        });

        back();
    }

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        System.out.println("# onCreateContextMenu");
        menu.setHeaderTitle("Context Menu");
        menu.add(0, 1, 0, "new").setNumericShortcut('1');
        menu.add(0, 2, 0, "open").setCheckable(true);
        menu.add(0, 3, 0, "close").setIcon(R.drawable.ic_action_search);

        SubMenu subMenu = menu.addSubMenu("Submenu");
        subMenu.add(0, 4, 0, "Add...");
        subMenu.add("Delete...");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        System.out.println("# onContextItemSelected");
        Toast.makeText(getApplicationContext(), item.getTitle() + "-" + item.getItemId(), Toast.LENGTH_SHORT).show();
        return false;
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // context menu
    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("# onCreateOptionsMenu");
        for (OptionsMenuBtn op : OptionsMenuBtn.values()) {
            menu.add(0, op.itemId, 0, op.label).setIcon(R.drawable.ic_action_search).setCheckable(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("# onOptionsItemSelected");
        super.onOptionsItemSelected(item);
        for (OptionsMenuBtn op : OptionsMenuBtn.values()) {
            if (op.itemId == item.getItemId()) {
                Toast.makeText(this, op.label, Toast.LENGTH_SHORT).show();
            }
            if(item.isCheckable()){
                //將核取狀態反轉
                item.setChecked(!item.isChecked());
                Toast.makeText(this, "checked - " + item.isChecked(), Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // options menu
    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // xxxxxxx menu

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // xxxxxxx menu
    enum OptionsMenuBtn {
        OP_ABOUT(Menu.FIRST, "about"), //
        OP_EXIT(Menu.FIRST + 1, "exit"), //
        OP_SEARCH(Menu.FIRST + 2, "search"), //
        OP_ADD(Menu.FIRST + 3, "add"), //
        OP_PLAY(Menu.FIRST + 4, "play"), //
        OP_DELETE(Menu.FIRST + 5, "delete"), //
        OP_OPEN(Menu.FIRST + 6, "open"), //
        OP_NEW(Menu.FIRST + 7, "new"), //
        OP_CLOSE(Menu.FIRST + 8, "close"), //
        ;
        final int itemId;
        final String label;

        OptionsMenuBtn(int itemId, String label) {
            this.itemId = itemId;
            this.label = label;
        }
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                OptionsMenuActivity.this.setResult(RESULT_CANCELED, OptionsMenuActivity.this.getIntent());
                OptionsMenuActivity.this.finish();
            }
        });
    }
}
