package com.example.gtuandroid.sub;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.gtuandroid.R;

public class FragmentV4_2 extends Fragment {
    public static final String TAG = FragmentV4_2.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.onActivityCreated(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_main, null);
        
        Bundle bundle = getArguments();
        Log.v(TAG, "bundle = " + bundle);
        
        final TextView text = (TextView)v.findViewById(R.id.text);
        final Button button = (Button)v.findViewById(R.id.back);
        button.setText("測試");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("這是第二頁");
            }
        });
        
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
