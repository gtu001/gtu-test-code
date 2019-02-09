package com.example.gtu001.qrcodemaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.common.DownloadHelper;
import com.example.gtu001.qrcodemaker.common.LayoutViewHelper;
import com.example.gtu001.qrcodemaker.common.Log;
import com.example.gtu001.qrcodemaker.common.PingUtil;
import com.example.gtu001.qrcodemaker.custom_dialog.UrlPlayerDialog_bg;
import com.example.gtu001.qrcodemaker.dao.YoutubeVideoDAO;
import com.example.gtu001.qrcodemaker.services.JavaYoutubeVideoUrlHandler;
import com.example.gtu001.qrcodemaker.services.YoutubeVideoService;
import com.example.gtu001.qrcodemaker.util.FileUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubePlayerActivity extends Activity {

    private static final String TAG = TvConnActivity.class.getSimpleName();


    private ListView listView;
    private BaseAdapter baseAdapter;
    private InitListViewHandler initListViewHandler;
    private YoutubeVideoService youtubeVideoService;

    public static final String YOUTUBE_KEY = "youtube";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = LayoutViewHelper.createContentView_simple(this);

        Log.v(TAG, "#################################################", 3);
        new PingUtil.NetPing("www.youtube.com").execute();
        Log.v(TAG, "#################################################", 3);

        final TextView downloadDescText = new TextView(this);
        layout.addView(downloadDescText);

        final EditText youtubeIdText = new EditText(this);
        layout.addView(youtubeIdText);

        //取得youtube內容
        if (getIntent().getExtras().containsKey(YoutubePlayerActivity.YOUTUBE_KEY)) {
            youtubeIdText.setText(getIntent().getExtras().getString(YoutubePlayerActivity.YOUTUBE_KEY));
        }

        //初始Btn狀態紐
        Button btn1 = new Button(this);
        btn1.setText("加入youtube ID");
        layout.addView(btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parseId = getYoutubeId(youtubeIdText.getText().toString());
                boolean result = initListViewHandler.add(parseId);
                if (result) {
                    youtubeIdText.setText("");
                }
            }
        });

        //初始Btn狀態紐
        Button btn2 = new Button(this);
        btn2.setText("開啟現在播放");
        layout.addView(btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlPlayerDialog_bg dialog = new UrlPlayerDialog_bg(YoutubePlayerActivity.this);
                Dialog _dialog = dialog.setUrl(null, null, initListViewHandler.getTotalUrlList()).build();
                _dialog.show();
            }
        });

        //初始listView
        listView = new ListView(this);
        layout.addView(listView, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
//        LayoutViewHelper.setViewHeight(listView, 1000);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) listView.getAdapter().getItem(position);
                final YoutubeItem item2 = (YoutubeItem) item.get("item");
                UrlPlayerDialog_bg dialog = new UrlPlayerDialog_bg(YoutubePlayerActivity.this);

                Log.v(TAG, "====================================================================");
                Log.v(TAG, "name = " + item2.name);
                Log.v(TAG, "videoUrl = " + item2.videoUrl);
                Log.v(TAG, "====================================================================");

                Mp3Bean bean = new Mp3Bean();
                bean.setUrl(item2.videoUrl);
                bean.setName(item2.name);

                Dialog _dialog = dialog.setUrl(null, bean, initListViewHandler.getTotalUrlList()).build();
                _dialog.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Map<String, Object> item = (Map<String, Object>) listView.getAdapter().getItem(position);
                final YoutubeItem item2 = (YoutubeItem) item.get("item");

                String[] items = new String[]{"刷新", "刪除", "下載"};

                AlertDialog dlg = new AlertDialog.Builder(YoutubePlayerActivity.this)//
                        .setTitle("選擇操作項目")//
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        initListViewHandler.itemReflash(item2, item);
                                        break;
                                    case 1:
                                        initListViewHandler.delete(item2.id);
                                        break;
                                    case 2:
                                        initListViewHandler.download(item, YoutubePlayerActivity.this);
                                        break;
                                    default:
                                        Toast.makeText(YoutubePlayerActivity.this, "Unknow choice " + which, Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        })//
                        .create();
                dlg.show();
                return true;
            }
        });

        initServices();
    }

    public String getYoutubeId(String strVal) {
        //  https://www.youtube.com/watch?v=VZnHlYwXNms
        //  https://youtu.be/XXXXXXXXXXXX
        strVal = StringUtils.trimToEmpty(strVal);
        Pattern ptn = Pattern.compile("watch\\?v\\=([.\\-]*?)\\&");
        Matcher mth = ptn.matcher(strVal);
        if (mth.find()) {
            return mth.group(1);
        }
        Pattern ptn2 = Pattern.compile("watch\\?v\\=([.\\-]*)");
        Matcher mth2 = ptn2.matcher(strVal);
        if (mth2.find()) {
            return mth2.group(1);
        }
        Pattern ptn3 = Pattern.compile("youtu\\.be\\/([.\\-]*)");
        Matcher mth3 = ptn3.matcher(strVal);
        if (mth3.find()) {
            return mth3.group(1);
        }
        return strVal;
    }


    private void initServices() {
        youtubeVideoService = new YoutubeVideoService(this);
        initListViewHandler = new InitListViewHandler();
        initListViewHandler.initListView();
    }

    private class InitListViewHandler {
        List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
        Handler handler = new Handler();

        public List<Mp3Bean> getTotalUrlList() {
            List<Mp3Bean> lst = new ArrayList<>();
            for (Map<String, Object> m : listItem) {
                Mp3Bean b = new Mp3Bean();
                YoutubeItem y = (YoutubeItem) m.get("item");
                b.setName(y.name);
                b.setUrl(y.videoUrl);
                lst.add(b);
            }
            return lst;
        }

        private void download(Map<String, Object> item, Context context) {
            YoutubeItem item2 = (YoutubeItem) item.get("item");

            String title = item2.name;
            String description = FileUtil.getSizeDescription(item2.fileLength);
            String path = item2.videoUrl;

            DownloadHelper.getInstance().download(title, description, path, "mp4", context);
        }

        private void delete(String id) {
            boolean deleteOk = youtubeVideoService.deleteId(id);
            for (int ii = 0; ii < listItem.size(); ii++) {
                Map<String, Object> vo = listItem.get(ii);
                YoutubeItem vo2 = (YoutubeItem) vo.get("item");
                if (StringUtils.equals(vo2.id, id)) {
                    listItem.remove(vo);
                    ii--;
                }
            }
            Toast.makeText(YoutubePlayerActivity.this, "刪除" + (deleteOk ? "成功" : "失敗"), Toast.LENGTH_SHORT).show();
            if (deleteOk) {
                baseAdapter.notifyDataSetChanged();
            }
        }

        private boolean add(String id) {
            if (StringUtils.isBlank(id)) {
                Toast.makeText(YoutubePlayerActivity.this, "id不可為空!", Toast.LENGTH_SHORT).show();
                return false;
            }

            YoutubeVideoDAO.YoutubeVideo vo = youtubeVideoService.findByPk(id);
            if (vo == null) {
                YoutubeItem item = new YoutubeItem(id);
                String result = item.processSelf();
                if (StringUtils.isNotBlank(result)) {
                    Toast.makeText(YoutubePlayerActivity.this, result, Toast.LENGTH_SHORT).show();
                    return false;
                }
                listItem.add(getItem2Map(item));
                baseAdapter.notifyDataSetChanged();

                //存置資料庫
                vo = getVo(item);
                item.vo = vo;
                youtubeVideoService.insertData(vo);
            } else {
                YoutubeItem item = this.getItemFromVo(vo);
                String result = item.processSelf();
                if (StringUtils.isNotBlank(result)) {
                    Toast.makeText(YoutubePlayerActivity.this, result, Toast.LENGTH_SHORT).show();
                    return false;
                }
                vo.setVideoUrl(item.videoUrl);
                youtubeVideoService.update(vo);
            }
            return true;
        }

        private YoutubeVideoDAO.YoutubeVideo getVo(YoutubeItem item) {
            YoutubeVideoDAO.YoutubeVideo vo = new YoutubeVideoDAO.YoutubeVideo();
            vo.setTitle(item.name);
            vo.setVideoId(item.id);
            vo.setLatestClickDate(System.currentTimeMillis());
            vo.setInsertDate(System.currentTimeMillis());
            vo.setClickTime(1);
            vo.setVideoUrl(item.videoUrl);
            return vo;
        }

        private YoutubeItem getItemFromVo(YoutubeVideoDAO.YoutubeVideo vo) {
            YoutubeItem item = new YoutubeItem(vo.getVideoId());
            item.id = vo.getVideoId();
            item.name = vo.getTitle();
            item.videoUrl = vo.getVideoUrl();
            item.vo = vo;
            return item;
        }

        private Map<String, Object> getItem2Map(YoutubeItem item) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("item_image", null);// 圖像資源的ID
            map.put("item_title", item.name);
            map.put("item_text", item.id);
            map.put("item_image_check", null);
            map.put("ItemTextDesc", null);
            map.put("item", item);
            return map;
        }

        private SimpleAdapter createSimpleAdapter() {
            SimpleAdapter listItemAdapter = new SimpleAdapter(YoutubePlayerActivity.this, listItem,// 資料來源
                    R.layout.subview_listview, //
                    new String[]{"item_title", "item_text", "item_image", "item_image_check", "ItemTextDesc"}, //
                    new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemImage, R.id.ImageView01, R.id.ItemTextDesc}//
            );
            return listItemAdapter;
        }

        private void reloadDBData() {
            listItem.clear();
            List<YoutubeVideoDAO.YoutubeVideo> lst = youtubeVideoService.queryAll();
            for (YoutubeVideoDAO.YoutubeVideo vo : lst) {
                Log.v(TAG, "## vo - " + ReflectionToStringBuilder.toString(vo, ToStringStyle.MULTI_LINE_STYLE));
                listItem.add(getItem2Map(getItemFromVo(vo)));
            }
        }

        private void itemReflash(YoutubeItem item, Map<String, Object> map) {
            String errMsg = item.processSelf();
            if (StringUtils.isNotBlank(errMsg)) {
                Toast.makeText(YoutubePlayerActivity.this, "錯誤 : " + errMsg, Toast.LENGTH_SHORT).show();
                return;
            }
            YoutubeVideoDAO.YoutubeVideo vo = item.vo;
            vo.setVideoUrl(item.videoUrl);
            vo.setTitle(item.name);
            map.put("item_title", item.name);
            map.put("item_text", item.id);
            youtubeVideoService.update(vo);
            baseAdapter.notifyDataSetChanged();
            Toast.makeText(YoutubePlayerActivity.this, "已刷新", Toast.LENGTH_SHORT).show();
        }

        private void initListView() {
            baseAdapter = createSimpleAdapter();
            reloadDBData();
            listView.setAdapter(baseAdapter);
            baseAdapter.notifyDataSetChanged();
        }
    }

    private class YoutubeItem {
        String name;
        String id;
        String videoUrl;
        long fileLength;
        YoutubeVideoDAO.YoutubeVideo vo;

        public YoutubeItem(String id) {
            this.name = name;
            this.id = id;
            this.videoUrl = videoUrl;
        }

        public String processSelf() {
            final ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(1);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JavaYoutubeVideoUrlHandler handler = new JavaYoutubeVideoUrlHandler(id, "", JavaYoutubeVideoUrlHandler.DEFAULT_USER_AGENT);
                        handler.execute();
                        YoutubeItem.this.name = handler.getTitle();
                        List<JavaYoutubeVideoUrlHandler.VideoUrlConfig> videoLst = handler.getVideoFor91Lst();
                        Collections.sort(videoLst, new Comparator<JavaYoutubeVideoUrlHandler.VideoUrlConfig>() {
                            @Override
                            public int compare(JavaYoutubeVideoUrlHandler.VideoUrlConfig t1, JavaYoutubeVideoUrlHandler.VideoUrlConfig t2) {
                                boolean t1Mp4 = t1.getYoutubeData().getFileExtension().toLowerCase().contains("mp4");
                                boolean t2Mp4 = t2.getYoutubeData().getFileExtension().toLowerCase().contains("mp4");
                                if (t1Mp4 && t2Mp4) {
                                    return new Long(t1.getLength()).compareTo(t2.getLength());
                                } else {
                                    if (t1Mp4 && !t2Mp4) {
                                        return -1;
                                    } else if (t2Mp4 && !t1Mp4) {
                                        return 1;
                                    }
                                }
                                return new Long(t1.getLength()).compareTo(t2.getLength());
                            }
                        });
                        if (videoLst.isEmpty()) {
                            queue.offer("沒有取得任何URL");
                        } else {
                            YoutubeItem.this.videoUrl = videoLst.get(0).getUrl();
                            YoutubeItem.this.fileLength = videoLst.get(0).getLength();
                            queue.offer("");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.e(TAG, "processSelf ERR : " + ex.getMessage(), ex);
                        queue.offer(ex.getMessage());
                    }
                }
            }).start();

            try {
                String errMsg = queue.poll(5000, TimeUnit.MILLISECONDS);
                if (StringUtils.isNotBlank(errMsg)) {
                    return errMsg;
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "ERROR : " + e.getMessage(), e);
                return "連線時間逾時!";
            }
            return "";
        }
    }
}

