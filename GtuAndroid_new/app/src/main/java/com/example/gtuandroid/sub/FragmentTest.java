package com.example.gtuandroid.sub;

import com.example.gtuandroid.R;
import com.example.gtuandroid.R.id;
import com.example.gtuandroid.R.layout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentTest extends Fragment {
    private View v;

    public static FragmentTest newInstance(int index) {
        FragmentTest f = new FragmentTest();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_test, container, false);
        TextView text = (TextView) v.findViewById(R.id.text_view);
        text.setText("Page" + getShownIndex());
        return v;
    }

}