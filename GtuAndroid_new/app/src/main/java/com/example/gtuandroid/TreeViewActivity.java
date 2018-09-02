package com.example.gtuandroid;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.gtuandroid.sub.TreeView;
import com.example.gtuandroid.sub.TreeView.LastLevelItemClickListener;
import com.example.gtuandroid.sub.TreeView.TreeElement;
import com.example.gtuandroid.sub.TreeView.TreeViewAdapter;

public class TreeViewActivity extends Activity {
	/** Called when the activity is first created. */
	
	@SuppressLint("ShowToast")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tree_view_main);
		TreeView treeView = (TreeView) findViewById(R.id.tree_view);
		
		List<TreeElement> treeElements = getTreeList();		

		LastLevelItemClickListener itemClickCallBack = new LastLevelItemClickListener() {// 创建节点点击事件监听
			@Override
			public void onLastLevelItemClick(int position,
					TreeViewAdapter adapter) {
				TreeElement element = (TreeElement) adapter.getItem(position);
				Toast.makeText(getApplicationContext(), element.getTitle(), 300)
						.show();
			}
		};
		treeView.initData(TreeViewActivity.this, treeElements);// 初始化数据
		treeView.setLastLevelItemClickCallBack(itemClickCallBack);// 设置节点点击事件监听
	}
	
	private List<TreeElement> getTreeList(){
	    List<TreeElement> list = new ArrayList<TreeElement>(); 
	    
	    TreeElement root = new TreeElement();
	    root.setId("root");
	    root.setTitle("root");
	    root.setHasChild(true);
	    root.setLevel(1);
	    list.add(root);
	    
	    TreeElement elem1 = new TreeElement();
	    elem1.setId("elem1");
	    elem1.setTitle("elem1");
	    elem1.setParentId("root");
	    elem1.setLevel(2);
        list.add(elem1);
        
        TreeElement elem2 = new TreeElement();
        elem2.setId("elem2");
        elem2.setTitle("elem2");
        elem2.setParentId("root");
        elem2.setHasChild(true);
        elem2.setLevel(2);
        list.add(elem2);
        
        TreeElement elem3 = new TreeElement();
        elem3.setId("elem3");
        elem3.setTitle("elem3");
        elem3.setParentId("elem2");
        elem3.setLevel(3);
        list.add(elem3);
	    
	    return list;
	}
}