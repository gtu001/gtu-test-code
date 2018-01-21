package com.example.gtuandroid.listfragmgnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gtuandroid.R;

public class ItemDetailFragment extends Fragment {
	public static final String ARG_ITEM_ID = "item_id";
	
	private static final String TAG = ItemDetailFragment.class.getSimpleName();

	private DummyContent.DummyItem mItem;

	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    Log.v(TAG, "onCreate");
	    Log.v(TAG, "1:" + ARG_ITEM_ID + "--" + getArguments().getString(ARG_ITEM_ID));
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    Log.v(TAG, "onCreateView");
	    Log.v(TAG, "2:" + ARG_ITEM_ID + "--" + getArguments().getString(ARG_ITEM_ID));
		View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.content);
		}
		return rootView;
	}
}
