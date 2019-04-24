package com.example.englishtester.common;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

public abstract class RecyclerPagerAdapter<VH extends RecyclerPagerAdapter.ViewHolder> extends PagerAdapter {

    public abstract int getItemCount();

    public abstract void onBindViewHolder(VH holder, int position);

    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    private static final String STATE = RecyclerPagerAdapter.class.getSimpleName();

    private static final String TAG = RecyclerPagerAdapter.class.getSimpleName();

    public static boolean DEBUG = false;

    private SparseArray<RecycleCache> mRecycleTypeCaches = new SparseArray<>();

    private SparseArray<Parcelable> mSavedStates = new SparseArray<>();

    public RecyclerPagerAdapter() {
    }

    @Override
    public void destroyItem(ViewGroup parent, int position, Object object) {
        Log.d(TAG, String.format("destroyItem | position: %d | instanceOfViewHolder: %b", position, object instanceof ViewHolder));
        if (object instanceof ViewHolder) {
            ((ViewHolder) object).detach(parent);
        }
    }

    @Override
    public int getCount() {
        return getItemCount();
    }

    @Override
    public int getItemPosition(Object object) {
        Log.d(TAG, String.format("getItemPosition | instanceOfViewHolder: %b | currentPosition: %d | isAttached: %b",
                object instanceof ViewHolder,
                object instanceof ViewHolder ? ((ViewHolder) object).mPosition : -1,
                object instanceof ViewHolder && ((ViewHolder) object).mIsAttached));
        return POSITION_NONE;
    }

    //gtu001 custom ↓↓↓↓↓↓
    public void releaseAllView(ViewGroup parent) {
        int viewType = getItemViewType(0);
        RecycleCache cache = mRecycleTypeCaches.get(viewType);
        for (int ii = 0; ii < cache.mCaches.size(); ii++) {
            ViewHolder vh = cache.mCaches.get(ii);
            vh.detach(parent);
        }
        cache.mCaches.clear();
    }

    public void getAllViewHolder(ViewGroup parent) {
        int viewType = getItemViewType(0);
        RecycleCache cache = mRecycleTypeCaches.get(viewType);
        List<ViewHolder> lst = new ArrayList<>();
        for (int ii = 0; ii < cache.mCaches.size(); ii++) {
            ViewHolder vh = cache.mCaches.get(ii);
            lst.add(vh);
        }
        cache.mCaches.clear();
    }
    //gtu001 custom ↑↑↑↑↑↑

    @SuppressWarnings("unchecked")
    @Override
    public Object instantiateItem(ViewGroup parent, int position) {
        int viewType = getItemViewType(position);
        if (mRecycleTypeCaches.get(viewType) == null) {
            mRecycleTypeCaches.put(viewType, new RecycleCache(this));
        }
        ViewHolder viewHolder = mRecycleTypeCaches.get(viewType).getFreeViewHolder(parent, viewType);
        viewHolder.attach(parent, position);
        onBindViewHolder((VH) viewHolder, position);
        Log.d(TAG, String.format("instantiateItem | position: %d | viewType: %d | cacheCount: %d",
                position, viewType, mRecycleTypeCaches.get(viewType).mCaches.size()));
        viewHolder.onRestoreInstanceState(mSavedStates.get(getItemId(position)));
        return viewHolder;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object instanceof ViewHolder && ((ViewHolder) object).itemView == view;
    }

    @Override
    public void notifyDataSetChanged() {
        Log.d(TAG, "notifyDataSetChanged");
        super.notifyDataSetChanged();
        for (ViewHolder viewHolder : getAttachedViewHolders()) {
            onNotifyItemChanged(viewHolder);
        }
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            bundle.setClassLoader(loader);
            SparseArray<Parcelable> ss = bundle.containsKey(STATE) ? bundle.getSparseParcelableArray(STATE) : null;
            mSavedStates = ss != null ? ss : new SparseArray<Parcelable>();
        }
        super.restoreState(state, loader);
    }

    @Override
    public Parcelable saveState() {
        Bundle bundle = new Bundle();
        for (ViewHolder viewHolder : getAttachedViewHolders()) {
            mSavedStates.put(getItemId(viewHolder.mPosition), viewHolder.onSaveInstanceState());
        }
        bundle.putSparseParcelableArray(STATE, mSavedStates);
        return bundle;
    }

    public int getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position) {
        return 0;
    }

    protected void onNotifyItemChanged(ViewHolder viewHolder) {
    }

    private List<ViewHolder> getAttachedViewHolders() {
        List<ViewHolder> attachedViewHolders = new ArrayList<>();
        int n = mRecycleTypeCaches.size();
        for (int i = 0; i < n; i++) {
            for (ViewHolder viewHolder : mRecycleTypeCaches.get(mRecycleTypeCaches.keyAt(i)).mCaches) {
                if (viewHolder.mIsAttached) {
                    attachedViewHolders.add(viewHolder);
                }
            }
        }
        return attachedViewHolders;
    }

    private static class RecycleCache {

        private final RecyclerPagerAdapter mAdapter;

        private final List<ViewHolder> mCaches;

        RecycleCache(RecyclerPagerAdapter adapter) {
            mAdapter = adapter;
            mCaches = new ArrayList<>();
        }

        ViewHolder getFreeViewHolder(ViewGroup parent, int viewType) {
            int i = 0;
            ViewHolder viewHolder;
            for (int n = mCaches.size(); i < n; i++) {
                viewHolder = mCaches.get(i);
                if (!viewHolder.mIsAttached) {
                    return viewHolder;
                }
            }
            viewHolder = mAdapter.onCreateViewHolder(parent, viewType);
            mCaches.add(viewHolder);
            return viewHolder;
        }
    }

    public static abstract class ViewHolder {

        private static final String STATE = ViewHolder.class.getSimpleName();

        public final View itemView;

        private boolean mIsAttached;

        private int mPosition;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView should not be null");
            }
            this.itemView = itemView;
        }

        private void attach(ViewGroup parent, int position) {
            mIsAttached = true;
            mPosition = position;
            parent.addView(itemView);
        }

        private void detach(ViewGroup parent) {
            parent.removeView(itemView);
            mIsAttached = false;
        }

        private void onRestoreInstanceState(Parcelable state) {
            if (state instanceof Bundle) {
                Bundle bundle = (Bundle) state;
                SparseArray<Parcelable> ss = bundle.containsKey(STATE) ? bundle.getSparseParcelableArray(STATE) : null;
                if (ss != null) {
                    itemView.restoreHierarchyState(ss);
                }
            }
        }

        private Parcelable onSaveInstanceState() {
            SparseArray<Parcelable> state = new SparseArray<>();
            itemView.saveHierarchyState(state);
            Bundle bundle = new Bundle();
            bundle.putSparseParcelableArray(STATE, state);
            return bundle;
        }
    }
}