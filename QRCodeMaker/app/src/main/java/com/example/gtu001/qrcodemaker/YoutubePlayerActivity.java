package com.example.gtu001.qrcodemaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.common.LayoutViewHelper;
import com.example.gtu001.qrcodemaker.custom_dialog.UrlPlayerDialog_bg;
import com.example.gtu001.qrcodemaker.dao.YoutubeVideoDAO;
import com.example.gtu001.qrcodemaker.services.JavaYoutubeVideoUrlHandler;
import com.example.gtu001.qrcodemaker.services.YoutubeVideoService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class YoutubePlayerActivity extends Activity {

    private static final String TAG = TvConnActivity.class.getSimpleName();


    private ListView listView;
    private BaseAdapter baseAdapter;
    private InitListViewHandler initListViewHandler;
    private YoutubeVideoService youtubeVideoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = LayoutViewHelper.createContentView(this);

        final EditText youtubeIdText = new EditText(this);
        layout.addView(youtubeIdText);

        //初始Btn狀態紐
        Button btn1 = new Button(this);
        btn1.setText("加入youtube ID");
        layout.addView(btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = initListViewHandler.add(youtubeIdText.getText().toString());
                if (result) {
                    youtubeIdText.setText("");
                }
            }
        });

        //初始listView
        listView = new ListView(this);
        layout.addView(listView, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        LayoutViewHelper.setViewHeight(listView, 1000);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) listView.getAdapter().getItem(position);
                final YoutubeItem item2 = (YoutubeItem) item.get("item");
//                UrlPlayerDialog dialog = new UrlPlayerDialog(YoutubePlayerActivity.this);
                UrlPlayerDialog_bg dialog = new UrlPlayerDialog_bg(YoutubePlayerActivity.this);

                Log.v(TAG, "====================================================================");
                Log.v(TAG, "name = " + item2.name);
                Log.v(TAG, "videoUrl = " + item2.videoUrl);
                Log.v(TAG, "====================================================================");

                Dialog _dialog = dialog.setUrl(item2.name, item2.videoUrl).build();
                _dialog.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) listView.getAdapter().getItem(position);
                final YoutubeItem item2 = (YoutubeItem) item.get("item");

                String[] items = new String[]{"刷新", "刪除"};

                AlertDialog dlg = new AlertDialog.Builder(YoutubePlayerActivity.this)//
                        .setTitle("選擇操作項目")//
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        initListViewHandler.itemReflash(item2);
                                        break;
                                    case 1:
                                        initListViewHandler.delete(item2.id);
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


    private void initServices() {
        youtubeVideoService = new YoutubeVideoService(this);
        initListViewHandler = new InitListViewHandler();
        initListViewHandler.initListView();
    }

    private class InitListViewHandler {
        List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();

        private void delete(String id) {
            boolean deleteOk = youtubeVideoService.deleteId(id);
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
                boolean result = item.processSelf();
                if (!result) {
                    Toast.makeText(YoutubePlayerActivity.this, "無法取得url!", Toast.LENGTH_SHORT).show();
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
                item.processSelf();
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
            map.put("item", item);
            return map;
        }

        private SimpleAdapter createSimpleAdapter() {
            SimpleAdapter listItemAdapter = new SimpleAdapter(YoutubePlayerActivity.this, listItem,// 資料來源
                    R.layout.subview_listview, //
                    new String[]{"item_title", "item_text", "item_image", "item_image_check"}, //
                    new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemImage, R.id.ImageView01}//
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

        private void itemReflash(YoutubeItem item) {
            item.processSelf();
            YoutubeVideoDAO.YoutubeVideo vo = item.vo;
            vo.setVideoUrl(item.videoUrl);
            vo.setTitle(item.name);
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
        YoutubeVideoDAO.YoutubeVideo vo;

        public YoutubeItem(String id) {
            this.name = name;
            this.id = id;
            this.videoUrl = videoUrl;
        }

        public boolean processSelf() {
            final ArrayBlockingQueue queue = new ArrayBlockingQueue(1);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    JavaYoutubeVideoUrlHandler handler = new JavaYoutubeVideoUrlHandler(id, "", JavaYoutubeVideoUrlHandler.DEFAULT_USER_AGENT);
                    handler.execute();
                    YoutubeItem.this.name = handler.getTitle();
                    TreeMap<Long, String> urlMap = new TreeMap<Long, String>();
                    for (JavaYoutubeVideoUrlHandler.VideoUrlConfig vo : handler.getVideoFor91Lst()) {
                        urlMap.put(vo.getLength(), vo.getUrl());
                    }
                    String url = urlMap.get(urlMap.keySet().iterator().next());
                    YoutubeItem.this.videoUrl = url;
                    queue.offer(new Object());
                }
            }).start();

            try {
                queue.poll(5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Log.e(TAG, "ERROR : " + e.getMessage(), e);
                return false;
            }
            return true;
        }
    }
}

