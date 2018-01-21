package com.example.englishtester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PicFindActivity extends ListActivity {
    
    private static final String TAG = PicFindActivity.class.getSimpleName();

    private List<String> items = null;
    private List<String> paths = null;
    private String rootPath = Constant.PicFindActivity_PATH;
    private TextView mPath;

    static final String BUNDLE_FILE = "file";

    private File currentDir;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_file_directory);

        mPath = (TextView) findViewById(R.id.filePathLabel);

        getFileDir(rootPath);
    }

    private void getFileDir(String filePath) {
        mPath.setText(filePath);

        items = new ArrayList<String>();
        paths = new ArrayList<String>();

        File f = new File(filePath);
        File[] files = f.listFiles();

        if (!filePath.equals(rootPath)) {
            items.add("回到 " + rootPath);
            paths.add(rootPath);
            items.add("回上一層 ../");
            paths.add(f.getParent());
        }

        for (int ii = 0; ii < files.length; ii++) {
            File file = files[ii];
            items.add(file.getName());
            paths.add(file.getPath());
        }

        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items);
        setListAdapter(fileList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = new File(paths.get(position));
        if (file.canRead()) {
            if (file.isDirectory()) {
                currentDir = file;
                getFileDir(paths.get(position));
            }
        } else {
            new AlertDialog.Builder(this)//
                    .setTitle("Message")//
                    .setMessage("[" + file.getName() + "] is can't read!")//
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    }).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("# onOptionsItemSelected");
        super.onOptionsItemSelected(item);
        Intent intent = new Intent();
        switch (item.getItemId()) {
        case OP_CHOICE_CURRENTDIR:
            intent.putExtra(BUNDLE_FILE, currentDir);
            this.setResult(RESULT_OK, intent);
            this.finish();
            break;
        }
        return true;
    }

    static final int OP_CHOICE_CURRENTDIR = Menu.FIRST;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("# onCreateOptionsMenu");
        menu.add(0, OP_CHOICE_CURRENTDIR, 0, "選擇此目錄");
        return true;
    }
}
