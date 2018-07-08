package com.example.englishtester;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.englishtester.common.ExternalStorage;
import com.example.englishtester.common.ExternalStorageV2;
import com.example.englishtester.common.FileConstantAccessUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileFindActivity extends ListActivity {

    private static final String TAG = FileFindActivity.class.getSimpleName();

    private List<Map<String, Object>> pathList = null;
    private TextView mPath;

    public static final String BUNDLE_FILE = "file";

    private ExtensionChecker extensionChecker;
    private RootDirHolder rootDirHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_directory);

        //init service
        extensionChecker = new ExtensionChecker("(txt|htm|html)");
        rootDirHolder = new RootDirHolder();

        mPath = (TextView) findViewById(R.id.filePathLabel);

        initStartDir();
    }

    private void initStartDir() {
        mPath.setText("");
        pathList = new ArrayList<Map<String, Object>>();

        addPathMap(rootDirHolder.sdCardTitle, rootDirHolder.sdCard.getAbsolutePath());
        addPathMap(rootDirHolder.externalSdCardTitle, rootDirHolder.externalSdCard.getAbsolutePath());

        SimpleAdapter fileList = new SimpleAdapter(this, pathList,// 資料來源
                R.layout.subview_propview, //
                new String[]{"icon", "file_name"}, //
                new int[]{R.id.ItemImage, R.id.ItemTitle});

        setListAdapter(fileList);
    }

    private void addPathMap(String name, String path) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("file_name", name);
        map.put("path", path);
        File file = new File(path);
        if (file.isDirectory()) {
            map.put("icon", R.drawable.icon_folder);
        } else if (extensionChecker.isMatch(file)) {
            map.put("icon", R.drawable.icon_file);
        } else {
            map.put("icon", null);
        }
        pathList.add(map);
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
                if (lhs.lastModified() < rhs.lastModified()) {
                    return -1;
                } else if (lhs.lastModified() > rhs.lastModified()) {
                    return 1;
                }
                return 0;
            }
        });

        if (!rootDirHolder.isRootDir(f)) {
            addPathMap("回到 " + rootDirHolder.sdCardTitle, rootDirHolder.sdCard.getAbsolutePath());
            addPathMap("回到 " + rootDirHolder.externalSdCardTitle, rootDirHolder.externalSdCard.getAbsolutePath());
            addPathMap("回上一層 ../", f.getParent());
        }

        if (files != null && files.length != 0) {
            for (int ii = 0; ii < sortFileList.size(); ii++) {
                File file = sortFileList.get(ii);
                String fileName = file.getName();
                addPathMap(fileName, file.getPath());
            }
        }

        // ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,
        // android.R.layout.simple_dropdown_item_1line, items);
        // setListAdapter(fileList);

        pathList = sortPathList(pathList);

        SimpleAdapter fileList = new SimpleAdapter(this, pathList,// 資料來源
                R.layout.subview_propview, //
                new String[]{"icon", "file_name"}, //
                new int[]{R.id.ItemImage, R.id.ItemTitle});

        setListAdapter(fileList);
    }

    /**
     * 排序 : 目錄在上, 檔案在下
     */
    private List<Map<String, Object>> sortPathList(List<Map<String, Object>> fileList) {
        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> folderList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> propList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : fileList) {
            File f = new File((String) map.get("path"));
            if (f.isDirectory()) {
                folderList.add(map);
            } else {
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
                if (extensionChecker.isMatch(file)) {
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

    public static class FileFindActivityStarter {
        public static File getFile(Intent data) {
            try {
                return (File) data.getExtras().get(PropertiesFindActivity.BUNDLE_FILE);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
                return null;
            }
        }

        public static File getFile(Bundle bundle) {
            try {
                return (File) bundle.get(PropertiesFindActivity.BUNDLE_FILE);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
                return null;
            }
        }
    }

    private class ExtensionChecker {
        Pattern ptn;

        ExtensionChecker(String patternStr) {
            ptn = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
        }

        private boolean isMatch(File file) {
            if (file.isFile()) {
                Matcher mth = ptn.matcher(file.getName());
                return mth.find();
            }
            return false;
        }
    }

    private class RootDirHolder {
        File sdCard;
        File externalSdCard;

        String sdCardTitle = "內部記憶體";
        String externalSdCardTitle = "SD Card";

        RootDirHolder() {
            Map<String, File> externalLocations = ExternalStorageV2.getAllStorageLocations(FileFindActivity.this);
            sdCard = externalLocations.get(ExternalStorage.SD_CARD);
            externalSdCard = externalLocations.get(ExternalStorage.EXTERNAL_SD_CARD);
        }

        private boolean isRootDir(File file) {
            if (file.isDirectory()) {
                return file.equals(sdCard) || file.equals(externalSdCard);
            }
            return false;
        }
    }
}
