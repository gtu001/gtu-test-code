package com.example.gtuandroid.sub;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gtuandroid.R;

/**
 * 类名：TreeView.java 类描述：实现树形结构的view
 * 
 * @author wader 创建时间：2011-11-03 16:32
 */
public class TreeView extends ListView implements OnItemClickListener {
    String TAG = "TreeView";
    List<TreeElement> treeElements = null;// 所有节点集合
    List<TreeElement> currentElements = null;// 当前显示的节点集合
    List<TreeElement> tempElements = null;// 用于临时存储
    List<TreeElement> treeElementsToDel; // 临时存储待删除的节点
    TreeViewAdapter adapter = null;// 用于数据填充
    LastLevelItemClickListener itemClickCallBack;// 用户点击事件回调

    public TreeView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "create with TreeView(Context context, AttributeSet attrs)");
        treeElements = new ArrayList<TreeElement>();
        currentElements = new ArrayList<TreeElement>();

        adapter = new TreeViewAdapter(context, currentElements);
        this.setAdapter(adapter);
        itemClickCallBack = new LastLevelItemClickListener() {
            @Override
            public void onLastLevelItemClick(int position, TreeViewAdapter adapter) {
                Log.d(TAG, "last level element " + currentElements.get(position).getTitle() + " is clicked");
                Toast.makeText(context, currentElements.get(position).getTitle(), 200).show();
            }
        };
        this.setOnItemClickListener(this);
    }

    public void initData(Context context, List<TreeElement> treeElements) {
        this.treeElements = treeElements;
        getFirstLevelElements(context);
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置点击事件回调接口
     *
     * @param itemClickCallBack
     */

    public void setLastLevelItemClickCallBack(LastLevelItemClickListener itemClickCallBack) {
        this.itemClickCallBack = itemClickCallBack;
    }

    /**
     * 初始化树形结构列表数据,把第一层级的数据添加到currentElements中
     */
    public void getFirstLevelElements(Context context) {
        Log.d(TAG, "initCurrentElements");
        int size = treeElements.size();
        Log.d(TAG, "tree elements num is: " + size);
        if (currentElements == null) {
            currentElements = new ArrayList<TreeElement>();
        }
        currentElements.clear();
        for (int i = 0; i < size; i++) {
            if (treeElements.get(i).getLevel() == 1) {
                currentElements.add(treeElements.get(i));
                Log.d(TAG, "find a first level element: " + treeElements.get(i));

            }
        }
    }

    /**
     * 从所有节点集合中获取某父节点的子节点集合
     *
     * @param parentId
     * @return
     */
    private List<TreeElement> getChildElementsFromAllById(String parentId) {
        if (parentId == null) {
            throw new RuntimeException("parentId不可為空");
        }

        tempElements = new ArrayList<TreeElement>();
        int size = treeElements.size();

        for (int i = 0; i < size; i++) {
            String currentParentId = treeElements.get(i).getParentId();
            if (parentId.equalsIgnoreCase(currentParentId)) {
                tempElements.add(treeElements.get(i));
                Log.d(TAG, "find a child element： " + treeElements.get(i));
            }
        }
        return tempElements;
    }

    /**
     * 从当前显示的节点集合中获取某父节点的子节点集合
     *
     * @param parentId
     * @return
     */
    private List<TreeElement> getChildElementsFromCurrentById(String parentId) {
        if (parentId == null) {
            throw new RuntimeException("parentId不可為空");
        }

        Log.d(TAG, "getChildElementsFromCurrentById    parentId： " + parentId);
        if (tempElements == null) {
            tempElements = new ArrayList<TreeElement>();
        } else {
            tempElements.clear();
        }

        int size = currentElements.size();
        for (int i = 0; i < size; i++) {
            String currentParentId = treeElements.get(i).getParentId();
            if (parentId.equalsIgnoreCase(currentParentId)) {
                tempElements.add(currentElements.get(i));
                Log.d(TAG, "find a child element to delete： " + currentElements.get(i));
            }
        }

        return tempElements;
    }

    /**
     * 删除某父节点的所有子节点集合
     *
     * @param parentId
     * @return
     */
    private synchronized boolean delAllChildElementsByParentId(String parentId) {
        Log.e(TAG, "delAllChildElementsByParentId: " + parentId);
        int size;
        TreeElement tempElement = currentElements.get(getElementIndexById(parentId));
        List<TreeElement> childElments = getChildElementsFromCurrentById(parentId);
        if (treeElementsToDel == null) {
            treeElementsToDel = new ArrayList<TreeElement>();
        } else {
            treeElementsToDel.clear();
        }
        size = childElments.size();
        Log.e(TAG, "childElments size : " + size);
        for (int i = 0; i < size; i++) {
            tempElement = childElments.get(i);

            if (tempElement.hasChild && tempElement.fold) {
                treeElementsToDel.add(tempElement);
            }
        }
        size = treeElementsToDel.size();
        Log.e(TAG, "treeElementsToDel size : " + size);

        for (int i = size - 1; i >= 0; i--) {
            delAllChildElementsByParentId(treeElementsToDel.get(i).getId());
        }
        delDirectChildElementsByParentId(parentId);
        return true;
    }

    /**
     * 删除某父节点的直接子节点集合
     *
     * @param parentId
     * @return
     */
    private synchronized boolean delDirectChildElementsByParentId(String parentId) {
        if (parentId == null) {
            throw new RuntimeException("parentId不可為空");
        }
        
        Log.d(TAG, "delDirectChildElementsByParentId(): " + parentId);
        boolean success = false;
        if (currentElements == null || currentElements.size() == 0) {
            Log.d(TAG, "delChildElementsById() failed,currentElements is null or it's size is 0");
            return success;
        }
        synchronized (currentElements) {
            int size = currentElements.size();
            Log.d(TAG, "begin delete");
            for (int i = size - 1; i >= 0; i--) {
                String currentParentId = treeElements.get(i).getParentId();
                if (parentId.equalsIgnoreCase(currentParentId)) {
                    currentElements.get(i).fold = false;// 记得隐藏子节点时把展开状态设为false
                    currentElements.remove(i);
                }
            }
        }
        success = true;
        return success;
    }

    /**
     * 根据id查下标
     *
     * @param id
     * @return
     */
    private int getElementIndexById(String id) {
        int num = currentElements.size();
        for (int i = 0; i < num; i++) {
            if (currentElements.get(i).getId().equalsIgnoreCase(id)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View convertView, int position, long id) {
        TreeElement element = currentElements.get(position);
        if (element.isHasChild()) {// 当前节点有子节点时只进行数据显示或隐藏等操作
            if (!element.isFold()) {// 当前父节点为未展开状态
                currentElements.addAll(position + 1, this.getChildElementsFromAllById(element.getId()));
            } else if (element.fold) {// 当前父节点为展开状态
                boolean success = this.delAllChildElementsByParentId(element.getId());
                // boolean success =
                // this.delDirectChildElementsByParentId(element
                // .getId());
                Log.d(TAG, "delete child state: " + success);
                if (!success) {
                    return;
                }
            }
            // 调试信息
            // Log.d(TAG, "elements in currentElements:\n");
            // for (int i = 0; i < currentElements.size(); i++) {
            // Log.d(TAG + i, currentElements.get(i) + "\n");
            // }

            element.setFold(!element.isFold());// 设置反状态
            adapter.notifyDataSetChanged();// 刷新数据显示
        } else {// 当前节点有子节点时只进行用户自定义操作
            itemClickCallBack.onLastLevelItemClick(position, adapter);
        }
    }

    /**
     * 自定义内部接口，用于用户点击最终节点时的事件回调
     */
    public interface LastLevelItemClickListener {
        void onLastLevelItemClick(int position, TreeViewAdapter adapter);
    }

    public static class TreeElement {
        String id = null;// 当前节点id
        String title = null;// 当前节点文字
        boolean hasParent = false;// 当前节点是否有父节点
        boolean hasChild = false;// 当前节点是否有子节点
        boolean childShow = false;// 如果子节点，字节当当前是否已显示
        String parentId = null;// 父节点id
        int level = -1;// 当前界面层级
        boolean fold = false;// 是否处于展开状态

        public boolean isChildShow() {
            return childShow;
        }

        public void setChildShow(boolean childShow) {
            this.childShow = childShow;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isHasParent() {
            return hasParent;
        }

        public void setHasParent(boolean hasParent) {
            this.hasParent = hasParent;
        }

        public boolean isHasChild() {
            return hasChild;
        }

        public void setHasChild(boolean hasChild) {
            this.hasChild = hasChild;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public boolean isFold() {
            return fold;
        }

        public void setFold(boolean fold) {
            this.fold = fold;
        }

        @Override
        public String toString() {
            return "id:" + this.id + "-level:" + this.level + "-title:" + this.title + "-fold:" + this.fold + "-hasChidl:" + this.hasChild + "-hasParent:" + this.hasParent + "-parentId:"
                    + this.parentId;
        }
    }

    public static class TreeViewAdapter extends BaseAdapter {

        class ViewHolder {
            ImageView icon;
            TextView title;
        }

        Context context;
        ViewHolder holder;
        LayoutInflater inflater;

        List<TreeElement> elements;

        public TreeViewAdapter(Context context, List<TreeElement> elements) {
            this.context = context;
            this.elements = elements;
        }

        @Override
        public int getCount() {
            return elements.size();
        }

        @Override
        public Object getItem(int position) {
            return elements.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /**
             * ---------------------- get holder------------------------
             */
            if (convertView == null) {
                if (inflater == null) {
                    inflater = LayoutInflater.from(context);
                }
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.tree_view_main_item, null);
                holder.icon = (ImageView) convertView.findViewById(R.id.tree_view_item_icon);
                holder.title = (TextView) convertView.findViewById(R.id.tree_view_item_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            /**
             * ---------------------- set holder--------------------------
             */
            if (elements.get(position).isHasChild()) {// 有子节点，要显示图标
                if (elements.get(position).isFold()) {
                    holder.icon.setImageResource(R.drawable.tree_view_icon_open);
                } else if (!elements.get(position).isFold()) {
                    holder.icon.setImageResource(R.drawable.tree_view_icon_close);
                }
                holder.icon.setVisibility(View.VISIBLE);
            } else {// 没有子节点，要隐藏图标
                holder.icon.setImageResource(R.drawable.tree_view_icon_close);
                holder.icon.setVisibility(View.INVISIBLE);
            }
            holder.icon.setPadding(25 * (elements.get(position).getLevel()), 0, 0, 0);// 根据层级设置缩进
                                                                                      // 得到层级，进行设置图片的缩进.
            holder.title.setText(elements.get(position).getTitle()); // ----------这里设置相应的空格的文字即可.
                                                                     // 和进行设置相应的padding-left即可.
            // holder.title.setTextSize(40 - elements.get(position).getLevel() *
            // 5); // 根据层级设置字体大小 lwl这里使用一致大小字体.

            return convertView;
        }
    }
}