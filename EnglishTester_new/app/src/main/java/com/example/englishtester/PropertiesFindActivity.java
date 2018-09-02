package com.example.englishtester;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.englishtester.common.FileConstantAccessUtil;

public class PropertiesFindActivity extends ListActivity {

    private static final String TAG = PropertiesFindActivity.class.getSimpleName();

    private List<Map<String, Object>> pathList = null;
    private String rootPath = Constant.PropertiesFindActivity_PATH;
    private TextView mPath;

    static final String BUNDLE_FILE = "file";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_directory);

        mPath = (TextView) findViewById(R.id.filePathLabel);

        rootPath = checkRootPath(rootPath);

        getFileDir(rootPath);
    }

    /**
     * 確認是否支援外部儲存空間
     */
    private String checkRootPath(String rootPath){
        File f = FileConstantAccessUtil.getFileDir(this, new File(rootPath));
        return f.getAbsolutePath();
    }

    private void addPathMap(String name, String path) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("file_name", name);
        map.put("path", path);
        File file = new File(path);
        if(file.isDirectory()){
            map.put("icon", R.drawable.icon_folder);
        }else if(file.getName().endsWith(".properties")){
            map.put("icon", R.drawable.icon_file);
        }else{
            map.put("icon", null);
        }
        pathList.add(map);
    }
    
    /**
     * 判斷是否為prop跟目錄
     */
    private boolean isPropRootFolder(String filePath){
        File f = new File(filePath);
        File f2 = new File(rootPath);
        if(f.getAbsoluteFile().equals(f2.getAbsoluteFile())){
            return true;
        }
        return false;
    }

    private void getFileDir(String filePath) {
        mPath.setText(filePath);

        pathList = new ArrayList<Map<String, Object>>();

        File f = new File(filePath);
        File[] files = f.listFiles();
        
        List<File> sortFileList = new ArrayList<File>();
        sortFileList.addAll(Arrays.asList(files));
        Collections.sort(sortFileList, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if(lhs.lastModified() < rhs.lastModified()){
                    return -1;
                }else if(lhs.lastModified() > rhs.lastModified()){
                    return 1;
                }
                return 0;
            }
        });

        if (!isPropRootFolder(filePath)) {
            addPathMap("回到 " + rootPath, rootPath);
            addPathMap("回上一層 ../", f.getParent());
        }

        if (files != null && files.length != 0) {
            for (int ii = 0; ii < sortFileList.size(); ii++) {
                File file = sortFileList.get(ii);
                String fileName = file.getName();
                if (fileName.endsWith(".properties")) {
                    fileName = fileName.replaceAll(".properties", "");
                    if (!fileName.startsWith("new_word_")) {
                        fileName = fileName + "[" + DateFormatUtils.format(file.lastModified(), "yyyyMMdd") + "]";
                    }
                }

                addPathMap(fileName, file.getPath());
            }
        }

        // ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,
        // android.R.layout.simple_dropdown_item_1line, items);
        // setListAdapter(fileList);
        
        pathList = sortPathList(pathList);

        SimpleAdapter fileList = new SimpleAdapter(this, pathList,// 資料來源
                R.layout.subview_propview, //
                new String[] { "icon", "file_name" }, //
                new int[] { R.id.ItemImage, R.id.ItemTitle });

        setListAdapter(fileList);
    }
    
    /**
     * 排序 : 目錄在上, 檔案在下
     */
    private List<Map<String,Object>> sortPathList(List<Map<String,Object>> fileList){
        List<Map<String,Object>> newList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> folderList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> propList = new ArrayList<Map<String,Object>>();
        for(Map<String,Object> map : fileList){
            File f = new File((String)map.get("path"));
            if(f.isDirectory()){
                folderList.add(map);
            }else{
                propList.add(map);
            }
        }
        newList.addAll(folderList);
        newList.addAll(propList);
        return newList;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String path = (String) pathList.get(position).get("path");
        File file = new File(path);
        if (file.canRead()) {
            if (file.isDirectory()) {
                getFileDir(path);
            } else {
                // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 取得檔案回傳
                if (file.getName().endsWith(".properties")) {
                    Intent intent = new Intent();
                    intent.putExtra(BUNDLE_FILE, file);
                    this.setResult(RESULT_OK, intent);
                    this.finish();
                }
                // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 取得檔案回傳
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
