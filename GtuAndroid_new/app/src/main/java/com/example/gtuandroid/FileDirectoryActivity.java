package com.example.gtuandroid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FileDirectoryActivity extends ListActivity {

    private List<String> items = null;
    private List<String> paths = null;
    private String rootPath = "/";
    private TextView mPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_directory);
        mPath = (TextView) findViewById(R.id.textView1);

        getFileDir(rootPath);
    }

    private void getFileDir(String filePath) {
        mPath.setText(filePath);

        items = new ArrayList<String>();
        paths = new ArrayList<String>();

        File f = new File(filePath);
        File[] files = f.listFiles();

        if (!filePath.equals(rootPath)) {
            items.add("Back to " + rootPath);
            paths.add(rootPath);
            items.add("back to ../");
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
                getFileDir(paths.get(position));
            } else {
                new AlertDialog.Builder(this)//
                        .setTitle("Message")//
                        .setMessage("[" + file.getName() + "] is File!")//
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            }
                        }).show();

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
}
