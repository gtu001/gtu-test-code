package com.example.englishtester;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.englishtester.common.Log;

import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.englishtester.common.ExternalStorage;
import com.example.englishtester.common.ExternalStorageV2;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

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

    public static final String FILE_PATTERN_KEY = FileFindActivity.class.getName() + "_FILE_PATTERN_KEY";
    public static final String FILE_START_DIRS = FileFindActivity.class.getName() + "_FILE_START_DIRS";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_directory);

        String defaultPattern = ".*";
        String[] defultCustomDirs = null;
        if (getIntent().getExtras().containsKey(FILE_PATTERN_KEY)) {
            defaultPattern = getIntent().getExtras().getString(FILE_PATTERN_KEY);
        }
        if (getIntent().getExtras().containsKey(FILE_START_DIRS)) {
            defultCustomDirs = getIntent().getExtras().getStringArray(FILE_START_DIRS);
        }

        //init service
        extensionChecker = new ExtensionChecker(defaultPattern);
        rootDirHolder = new RootDirHolder(defultCustomDirs);

        mPath = (TextView) findViewById(R.id.filePathLabel);

        initStartDir();
    }

    private void initStartDir() {
        mPath.setText("");
        pathList = new ArrayList<Map<String, Object>>();

        //設定可選目錄
        rootDirHolder.addPathMapItems();

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
        if (file.isDirectory() || StringUtils.isBlank(path)) {
            map.put("icon", R.drawable.icon_folder);
        } else if (extensionChecker.isMatch(file)) {
            map.put("icon", R.drawable.icon_file);
        } else {
            map.put("icon", null);
        }
        pathList.add(map);
    }

    private List<File> getCurrentDirLst(File dirFile) {
        if (!dirFile.isDirectory()) {
            return new ArrayList<>();
        }
        File[] files = dirFile.listFiles();
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
        return sortFileList;
    }


    private void getFileDir(String filePath) {
        mPath.setText(filePath);

        pathList = new ArrayList<Map<String, Object>>();

        File currentDir = new File(filePath);

        boolean ignoreSubdirLst = false;

        if (StringUtils.isBlank(filePath)) {
            rootDirHolder.addPathMapItems();

            ignoreSubdirLst = true;
        } else {
            addPathMap("回到根目錄", "");
            if (currentDir.getParent() != null) {
                addPathMap("回上一層 ../", currentDir.getParent());
            }
        }

        if (!ignoreSubdirLst) {
            List<File> sortFileList = getCurrentDirLst(currentDir);
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
            if (f.isDirectory() || StringUtils.isBlank((String) map.get("path"))) {
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

        //回到跟目錄
        if (StringUtils.isBlank(path)) {
            getFileDir("");
            return;
        }

        if (file.canRead()) {
            if (file.isDirectory()) {
                getFileDir(path);
            } else {
                // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 取得檔案回傳
                if (extensionChecker.isMatch(file)) {
                    returnChoiceFile(file);
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

    private void returnChoiceFile(File file) {
        Log.v(TAG, "↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 取得檔案回傳");
        Log.v(TAG, "choice file : " + file);
        Log.v(TAG, "↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 取得檔案回傳");
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_FILE, file);
        this.setResult(RESULT_OK, intent);
        this.finish();
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
        List<File> externalSdCardLst;

        String sdCardTitle = "內部記憶體";
        String externalSdCardTitle = "SD Card";

        RootDirHolder(String[] extensionDirs) {
            Map<String, File> externalLocations = ExternalStorageV2.getAllStorageLocations(FileFindActivity.this);
            Pair<Integer, Integer> pair = ExternalStorageV2.getExternalSdCardRange(FileFindActivity.this);

            sdCard = externalLocations.get(ExternalStorage.SD_CARD);
            externalSdCard = externalLocations.get(ExternalStorage.EXTERNAL_SD_CARD);

            externalSdCardLst = new ArrayList<>();
            if (ExternalStorageV2.isRangeValid(pair)) {
                for (int ii = pair.getLeft(); ii <= pair.getRight(); ii++) {
                    File f = externalLocations.get(ExternalStorage.EXTERNAL_SD_CARD + ii);
                    externalSdCardLst.add(f);
                }
            }

            //custom root dirs
            if (extensionDirs != null) {
                for (String dir : extensionDirs) {
                    File $dir = new File(dir);
                    if ($dir.exists() && $dir.isDirectory()) {
                        externalSdCardLst.add($dir);
                    }
                }
            }
        }

        private void addPathMapItems() {
            addPathMap("回到根目錄", "");
            addPathMap("回到 " + sdCardTitle, sdCard.getAbsolutePath());
            if (externalSdCard != null) {
                addPathMap("回到 " + externalSdCardTitle, externalSdCard.getAbsolutePath());
            }

            for (int ii = 0; ii < externalSdCardLst.size(); ii++) {
                if (externalSdCardLst.get(ii) == null) {
                    continue;
                }
                addPathMap("回到 " + externalSdCardTitle + ii, externalSdCardLst.get(ii).getAbsolutePath());
            }
        }

        private boolean isRootDir(File file) {
            if (file.isDirectory()) {
                return file.equals(sdCard) || file.equals(externalSdCard) || externalSdCardLst.contains(file);
            }
            return false;
        }
    }
}
