package com.example.englishtester;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.englishtester.common.ExternalStorage;
import com.example.englishtester.common.ExternalStorageV2;
import com.example.englishtester.common.FileUtilGtu;
import com.example.englishtester.common.Log;
import com.example.englishtester.common.OOMHandler2;
import com.example.englishtester.common.PixelMetricUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

public class FileFind4EpubActivity extends ListActivity {

    private static final String TAG = FileFind4EpubActivity.class.getSimpleName();

    private List<Map<String, Object>> pathList = null;
    private TextView mPath;

    public static final String BUNDLE_FILE = "file";

    private ExtensionChecker extensionChecker;
    private RootDirHolder rootDirHolder;
    private CommonImageHolder commonImageHolder;

    public static final String FILE_PATTERN_KEY = FileFind4EpubActivity.class.getName() + "_FILE_PATTERN_KEY";
    public static final String FILE_START_DIRS = FileFind4EpubActivity.class.getName() + "_FILE_START_DIRS";

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
        commonImageHolder = new CommonImageHolder(this);

        mPath = (TextView) findViewById(R.id.filePathLabel);

        initStartDir();
    }

    private void initStartDir() {
        mPath.setText("");
        pathList = new ArrayList<Map<String, Object>>();

        //設定可選目錄
        rootDirHolder.addPathMapItems();

        /*
        SimpleAdapter fileList = new SimpleAdapter(this, pathList,// 資料來源
                R.layout.subview_propview_epub, //
                new String[]{"icon", "file_name"}, //
                new int[]{R.id.ItemImage, R.id.ItemTitle});

        apply4Bitmap(fileList);

        setListAdapter(fileList);
        */

        setListAdapter(new MyBaseAdapter(this, pathList));
    }

    private static class CommonImageHolder {
        Bitmap folder;
        Bitmap file;
        Bitmap notfound_404;
        Context context;

        CommonImageHolder(Context context) {
            this.context = context;
        }

        public Bitmap getFolder() {
            if (folder == null) {
                folder = OOMHandler2.decodeSampledBitmapFromResource(context.getResources(), R.drawable.icon_folder, 30, 30);
            }
            return folder;
        }

        public Bitmap getFile() {
            if (file == null) {
                file = OOMHandler2.decodeSampledBitmapFromResource(context.getResources(), R.drawable.icon_file, 30, 30);
            }
            return file;
        }

        public Bitmap getNotfound() {
            if (notfound_404 == null) {
                Bitmap b = OOMHandler2.decodeSampledBitmapFromResource(context.getResources(), R.drawable.notfound_404, 100, 141);
                notfound_404 = resetBitmapSize(b, 100, 141);
            }
            return notfound_404;
        }

        // 圖片以畫面寬度縮放比例
        protected Bitmap resetBitmapSize(Bitmap bm, int width, int height) {
            return Bitmap.createScaledBitmap(bm, width, height, false);
        }
    }

    private void addPathMap(String name, String path) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("file_name", name);
        map.put("path", path);
        File file = new File(path);
        if (file.isDirectory() || StringUtils.isBlank(path)) {
            map.put("icon", commonImageHolder.getFolder());
        } else if (extensionChecker.isMatch(file)) {
//            map.put("icon", R.drawable.icon_file);
            EpubCoverImageFetcher e = new EpubCoverImageFetcher(path);
            map.put("icon", e.getImage());
            if (StringUtils.isNotBlank(e.getTitle())) {
                map.put("file_name", e.getTitle());
            }
            map.put("file_name_desc", e.getAuthor());
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
                R.layout.subview_propview_epub, //
                new String[]{"icon", "file_name"}, //
                new int[]{R.id.ItemImage, R.id.ItemTitle});

        SimpleAdapterDecorator.apply4Bitmap(fileList);

        setListAdapter(fileList);
        */

        setListAdapter(new MyBaseAdapter(this, pathList));
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
                Matcher mth = ptn.matcher(FileUtilGtu.getSubName(file.getName()));
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
            Map<String, File> externalLocations = ExternalStorageV2.getAllStorageLocations(FileFind4EpubActivity.this);
            Pair<Integer, Integer> pair = ExternalStorageV2.getExternalSdCardRange(FileFind4EpubActivity.this);

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

    private static class EpubCoverImageFetcher {
        InputStream inputStream;
        String title;
        String author;

        private EpubCoverImageFetcher(String filePath) {
            try {
                EpubReader epubReader = new EpubReader();
                Book book = epubReader.readEpubLazy(filePath, "UTF8");
                if (book != null) {
                    Resource res = book.getCoverImage();
                    if (res != null) {
                        inputStream = res.getInputStream();
                    }

                    title = book.getTitle();
                    author = getFirstAuthor(book);
                }
            } catch (Exception ex) {
                throw new RuntimeException("getBookFirstImage ERR : " + ex.getMessage(), ex);
            }
        }

        public Bitmap getImage() {
            try {
                int width = 21;
                int height = 30;
                if (inputStream != null) {
                    return OOMHandler2.decodeSampledBitmapFromResource(inputStream, width, height, 3);
                }
                return null;
            } catch (Exception ex) {
                Log.line(TAG, "getImage ERR : " + ex.getMessage(), ex);
                return null;
            }
        }

        public static String getFirstAuthor(Book book) {
            if (book == null || book.getMetadata().getAuthors().isEmpty()) {
                return "";
            }

            List<String> authorLst = new ArrayList<>();
            // Loop through authors to get first non-empty one.
            for (Author author : book.getMetadata().getAuthors()) {
                String fName = author.getFirstname();
                String lName = author.getLastname();
                // Skip this author now if it doesn't have a non-null, non-empty name.
                if ((fName == null || fName.isEmpty()) && (lName == null || lName.isEmpty()))
                    continue;

                // Return the name, which might only use one of the strings.
                String authorName = "";
                if (fName == null || fName.isEmpty()) {
                    authorName = lName;
                } else if (lName == null || lName.isEmpty()) {
                    authorName = fName;
                } else {
                    authorName = fName + " " + lName;
                }

                authorLst.add(authorName);
            }

            return StringUtils.join(authorLst, "/");
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
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
            ViewHolder viewHolder = null;
            if (convertView == null || convertView.getTag() == null) {
                viewHolder = new ViewHolder(context);
                convertView = viewHolder.getMainLayout();
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final Map<String, Object> map = lst.get(position);

            String name = (String) map.get("file_name");
            String desc = (String) map.get("file_name_desc");
            Bitmap bitmap = (Bitmap) map.get("icon");
            boolean isEpubFile = StringUtils.defaultString((String) map.get("path")).endsWith(".epub");

            if (StringUtils.isNotBlank(name)) {
                SpannableString s1 = new SpannableString(StringUtils.defaultString(name));
                s1.setSpan(new RelativeSizeSpan(1.1f), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.itemTitle.setText(s1);
            } else {
                viewHolder.itemTitle.setText("");
            }

            if (StringUtils.isNotBlank(desc)) {
                SpannableString s2 = new SpannableString(StringUtils.defaultString(desc));
                s2.setSpan(new StyleSpan(Typeface.ITALIC), 0, desc.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.itemTitle_desc.setText(s2);
            } else {
                viewHolder.itemTitle_desc.setText("");
            }

            if (isEpubFile && bitmap == null) {
                bitmap = commonImageHolder.getNotfound();
            }
            viewHolder.imageView01.setImageBitmap(bitmap);

            viewHolder.resetViewStatus(isEpubFile);

            return convertView;
        }
    }

    private static class ViewHolder {
        TextView itemTitle;
        TextView itemTitle_desc;
        ImageView imageView01;
        LinearLayout mainLayout;

        ViewHolder(Context context) {
            Display d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int currentWidth = d.getWidth();

            mainLayout = new LinearLayout(context);
            mainLayout.setLayoutParams(new LinearLayout.LayoutParams(currentWidth, // 設定與螢幕同寬
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            mainLayout.setOrientation(LinearLayout.HORIZONTAL);
            imageView01 = new ImageView(context);
            imageView01.setLayoutParams(createLP());
            mainLayout.addView(imageView01);

            LinearLayout rightLayout = new LinearLayout(context);
            rightLayout.setLayoutParams(createLP());
            mainLayout.addView(rightLayout);
            rightLayout.setOrientation(LinearLayout.VERTICAL);

            itemTitle = new TextView(context);
            itemTitle_desc = new TextView(context);
            itemTitle.setLayoutParams(createLP());
            itemTitle.setLayoutParams(createLP());
            rightLayout.addView(itemTitle);
            rightLayout.addView(itemTitle_desc);
        }

        private LinearLayout.LayoutParams createLP() {
            return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, // 設定與螢幕同寬
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        public void resetViewStatus(boolean isEpubFile) {
            if (isEpubFile) {
                imageView01.getLayoutParams().width = (int) PixelMetricUtil.convertDpToPixel(100);
                imageView01.getLayoutParams().height = (int) PixelMetricUtil.convertDpToPixel(141);
            } else {
                imageView01.getLayoutParams().width = (int) PixelMetricUtil.convertDpToPixel(30);
                imageView01.getLayoutParams().height = (int) PixelMetricUtil.convertDpToPixel(30);
            }
            imageView01.requestLayout();
        }

        public View getMainLayout() {
            return mainLayout;
        }
    }
}
