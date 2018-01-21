package com.example.gtuandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.widget.SimpleExpandableListAdapter;

public class ExpandableListViewActivity extends ExpandableListActivity {

    private String[] lunch = {//
    "飯類", "麵類", "小菜"//
    };//
    private String[][] lunch_class = {//
    { "滷肉飯", "雞腿飯", "排骨飯", "雞排飯", "豬腳飯" },//
            { "陽春麵", "榨菜肉絲麵", "餛飩麵", "牛肉麵", "乾麵" },//
            { "豆干", "豆腐", "豬皮", "海帶", "滷蛋" } //
    };//

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_listview_expenable_builtin);
        
        final String NAME = "name";

        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();

        for (int i = 0; i < lunch.length; i++) {
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(NAME, lunch[i]);

            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for (int j = 0; j < lunch_class[i].length; j++) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(NAME, lunch_class[i][j]);
            }
            childData.add(children);
        }

        SimpleExpandableListAdapter mAdapter = new SimpleExpandableListAdapter(
        this,//
                groupData,//
                android.R.layout.simple_expandable_list_item_1,//
                new String[] { NAME },//
                new int[] { android.R.id.text1 },//
                childData,//
                android.R.layout.simple_expandable_list_item_1,//
                new String[] { NAME },//
                new int[] { android.R.id.text1 }//
        );
        
        setListAdapter(mAdapter);
    }
}