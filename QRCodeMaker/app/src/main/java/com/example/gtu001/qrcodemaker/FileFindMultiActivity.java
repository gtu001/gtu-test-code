package com.example.gtu001.qrcodemaker;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gtu001.qrcodemaker.common.ExternalStorageV2;
import com.example.gtu001.qrcodemaker.common.Log;

import org.apache.commons.lang3.ObjectUtils;
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

public class FileFindMultiActivity extends ListActivity {

    private static final String TAG = FileFindMultiActivity.class.getSimpleName();

    private List<Map<String, Object>> pathList = null;
    private ArrayList<String> multiFilesLst = new ArrayList<>();
    private TextView mPath;
    private Button multiFilesBtn;

    public static final String BUNDLE_FILE = "file";
    public static final String BUNDLE_FILEs = "file";

    private ExtensionChecker extensionChecker;
    private RootDirHolder rootDirHolder;
    private MyBaseAdapter myBaseAdapter;

    public static final String FILE_PATTERN_KEY = FileFindMultiActivity.class.getName() + "_FILE_PATTERN_KEY";
    public static final String FILE_START_DIRS = FileFindMultiActivity.class.getName() + "_FILE_START_DIRS";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_multi_directory);

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
        multiFilesBtn = (Button) findViewById(R.id.multiFilesBtn);

        multiFilesBtn.setOnClickListener(new View.OnClickListener() {

            private void appendFileToLst(File file, ArrayList<String> multiFilesLst) {
                if (file.isFile()) {
                    if (extensionChecker.isMatch(file)) {
                        multiFilesLst.add(file.getAbsolutePath());
                    }
                } else if (file.isDirectory()) {
                    if (file.listFiles() != null) {
                        for (File f : file.listFiles()) {
                            appendFileToLst(f, multiFilesLst);
                        }
                    }
                }
            }

            @Override
            public void onClick(View v) {
                ArrayList<String> lst = new ArrayList<>();
                for (int ii = 0; ii < multiFilesLst.size(); ii++) {
                    String path = multiFilesLst.get(ii);
                    File file = new File(path);
                    appendFileToLst(file, lst);
                }
                if (!lst.isEmpty()) {
                    returnChoiceFiles(lst);
                }
            }
        });

        initStartDir();
    }

    private void initStartDir() {
        mPath.setText("");
        pathList = new ArrayList<Map<String, Object>>();

        //設定可選目錄
        rootDirHolder.addPathMapItems();

        /*

        SimpleAdapter fileList = new SimpleAdapter(this, pathList,// 資料來源
                R.layout.subview_file_multi_choice, //
                new String[]{"checkBox", "ImageView01", "ItemTitle"}, //
                new int[]{R.id.checkBox, R.id.ImageView01, R.id.ItemTitle});//
*/

        myBaseAdapter = new MyBaseAdapter(this, pathList);
        setListAdapter(myBaseAdapter);

//        this.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private void addPathMap(String name, String path) {
        Map<String, Object> map = new HashMap<String, Object>();
        File file = new File(path);
        map.put("checkBox", "");
        map.put("ItemTitle", name);
        map.put("path", path);
        if (file.isDirectory() || StringUtils.isBlank(path)) {
            map.put("ImageView01", R.drawable.icon_folder);
        } else if (extensionChecker.isMatch(file)) {
            map.put("ImageView01", R.drawable.icon_file);
        } else {
            map.put("ImageView01", null);
        }
        pathList.add(map);
    }

    private List<File> getCurrentDirLst(File dirFile) {
        if (!dirFile.isDirectory()) {
            return new ArrayList<>();
        }
        File[] files = dirFile.listFiles();
        List<File> sortFileList = new ArrayList<File>();
        if (files != null)
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

/*
        SimpleAdapter fileList = new SimpleAdapter(this, pathList,// 資料來源
                R.layout.subview_file_multi_choice, //
                new String[]{"checkBox", "ImageView01", "ItemTitle"}, //
                new int[]{R.id.checkBox, R.id.ImageView01, R.id.ItemTitle});//
                */

        myBaseAdapter = new MyBaseAdapter(this, pathList);
        setListAdapter(myBaseAdapter);
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

    private void returnChoiceFiles(ArrayList<String> multiFilesLst) {
        Log.v(TAG, "↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 取得檔案回傳");
        for (String f : multiFilesLst) {
            Log.v(TAG, "choice file : " + f);
        }
        Log.v(TAG, "↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 取得檔案回傳");
        Intent intent = new Intent();
        intent.putStringArrayListExtra(BUNDLE_FILEs, multiFilesLst);
        this.setResult(RESULT_OK, intent);
        this.finish();
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
        public static List<File> getFiles(Intent data) {
            try {
                List<File> fileLst = new ArrayList<File>();
                ArrayList<String> lst = (ArrayList<String>) data.getExtras().get(BUNDLE_FILEs);
                for (String var : lst) {
                    fileLst.add(new File(var));
                }
                return fileLst;
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
                return null;
            }
        }

        public static List<File> getFiles(Bundle bundle) {
            try {
                List<File> fileLst = new ArrayList<File>();
                ArrayList<String> lst = (ArrayList<String>) bundle.get(BUNDLE_FILEs);
                for (String var : lst) {
                    fileLst.add(new File(var));
                }
                return fileLst;
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
                return null;
            }
        }

        public static File getFile(Intent data) {
            try {
                return (File) data.getExtras().get(BUNDLE_FILE);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
                return null;
            }
        }

        public static File getFile(Bundle bundle) {
            try {
                return (File) bundle.get(BUNDLE_FILE);
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
            Map<String, File> externalLocations = ExternalStorageV2.getAllStorageLocations(FileFindMultiActivity.this);
            Pair<Integer, Integer> pair = ExternalStorageV2.getExternalSdCardRange(FileFindMultiActivity.this);

            sdCard = externalLocations.get(ExternalStorageV2.SD_CARD);
            externalSdCard = externalLocations.get(ExternalStorageV2.EXTERNAL_SD_CARD);

            externalSdCardLst = new ArrayList<>();
            if (ExternalStorageV2.isRangeValid(pair)) {
                for (int ii = pair.getLeft(); ii <= pair.getRight(); ii++) {
                    File f = externalLocations.get(ExternalStorageV2.EXTERNAL_SD_CARD + ii);
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

    private class MyBaseAdapter extends BaseAdapter {
        Context context;
        List<Map<String, Object>> lst;

        MyBaseAdapter(Context context, List<Map<String, Object>> lst) {
            this.context = context;
            this.lst = lst;
        }

        @Override
        public int getCount() {
            return lst.size();
        }

        @Override
        public Object getItem(int position) {
            return lst.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.subview_file_multi_choice, null);
            //}

            final Map<String, Object> map = lst.get(position);

            boolean reset = false;
            if (convertView.getTag() == null) {
                reset = true;
            } else {
                ViewHolder holder = (ViewHolder) convertView.getTag();
                reset = !holder.isEqualMap(map);
            }

            if (reset) {
                ViewHolder holder = new ViewHolder();
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                holder.imageView01 = (ImageView) convertView.findViewById(R.id.ImageView01);
                holder.itemTitle = (TextView) convertView.findViewById(R.id.ItemTitle);
                holder.map = map;

                map.put("holder", holder);

                holder.itemTitle.setText((String) map.get("ItemTitle"));
                holder.itemTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onListItemClick(null, null, position, -1);
                    }
                });

                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String path = (String) map.get("path");
                        if (isChecked) {
                            if (!multiFilesLst.contains(path)) {
                                multiFilesLst.add(path);
                            }
                        } else {
                            multiFilesLst.remove(path);
                        }
                    }
                });

                Integer img = (Integer) map.get("ImageView01");
                if (img != null) {
                    holder.imageView01.setImageResource(img);
                } else {
                    holder.imageView01.setImageDrawable(null);
                }

                convertView.setTag(holder);
            }

            return convertView;
        }
    }

    private static class ViewHolder {
        CheckBox checkBox;
        ImageView imageView01;
        TextView itemTitle;
        Map<String, Object> map;

        private boolean isEqualMap(Map<String, Object> map) {
            if (this.map == map) {
                return true;
            }
            for (String k : this.map.keySet()) {
                if (!ObjectUtils.equals(this.map.get(k), map.get(k))) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String path = mPath.getText().toString();
            File currentDir = new File(path);
            boolean changeDirOk = false;
            if (!rootDirHolder.isRootDir(currentDir)) {
                if (currentDir.exists() && currentDir.isDirectory()) {
                    File parentDir = currentDir.getParentFile();
                    if (parentDir.exists() && parentDir.isDirectory()) {
                        getFileDir(parentDir.getAbsolutePath());
                        changeDirOk = true;
                    }
                }
            } else {
                this.initStartDir();
                changeDirOk = true;
            }
            if (!changeDirOk) {
                onBackPressed();
            }
        }
        return false;
    }
}
