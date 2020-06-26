package com.example.englishtester;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecentBookHistoryActivity extends ListActivity {

    private static final String TAG = RecentBookHistoryActivity.class.getSimpleName();

    private RecentBookHistoryService mRecentBookHistoryService;
    private List<Map<String, Object>> pathList;

    private EditText searchTextView;
    private TextView filePathLabel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recent_book_list);

        searchTextView = (EditText) findViewById(R.id.searchTextView);
        filePathLabel = (TextView) findViewById(R.id.filePathLabel);

        initServices();

        initListView();
    }

    private void initServices() {
        mRecentBookHistoryService = new RecentBookHistoryService(this);
    }

    private void initListView() {
        pathList = new ArrayList<>();

        List<RecentBookOpenHistoryDAO.RecentBookOpenHistory> lst = mRecentBookHistoryService.queryBookList();
        if (lst == null) {
            lst = new ArrayList<>();
        }

        for (RecentBookOpenHistoryDAO.RecentBookOpenHistory vo : lst) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("file_name", vo.bookName);
            map.put("file_detail", "次數：" + vo.openTimes + ",時間：" + DateFormatUtils.format(vo.latestOpenDatetime, "yyyy/MM/dd HH:mm:ss"));
            map.put("vo", vo);
            pathList.add(map);
        }

        SimpleAdapter fileList = new SimpleAdapter(this, pathList,// 資料來源
                R.layout.subview_propview_subdetail, //
                new String[]{"icon", "file_name", "file_detail"}, //
                new int[]{R.id.ItemImage, R.id.ItemTitle, R.id.ItemDetail});

        setListAdapter(fileList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        RecentBookOpenHistoryDAO.RecentBookOpenHistory vo = (RecentBookOpenHistoryDAO.RecentBookOpenHistory) pathList.get(position).get("vo");
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putString(RecentBookHistoryService.RECENT_OPEN_BOOK, vo.filePath);

        switch (StringUtils.trimToEmpty(vo.subName)) {
            case "epub":
                intent.setClass(RecentBookHistoryActivity.this, EpubReaderEpubActivity.class);
                break;
            case "pdf":
                intent.setClass(RecentBookHistoryActivity.this, PdfReaderPdfActivity.class);
                break;
            case "mobi":
                intent.setClass(RecentBookHistoryActivity.this, MobiReaderMobiActivity.class);
                break;
            default:
                intent.setClass(RecentBookHistoryActivity.this, TxtReaderBufferActivity.class);
                break;
        }

        intent.putExtras(bundle);
        startActivityForResult(intent, 9999);
    }
}
