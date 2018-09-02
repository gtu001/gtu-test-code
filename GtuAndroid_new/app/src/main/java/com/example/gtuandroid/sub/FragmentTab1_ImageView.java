package com.example.gtuandroid.sub;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gtuandroid.R;

public class FragmentTab1_ImageView extends Fragment {
    private int[] image = { //
    R.drawable.cat, R.drawable.flower, R.drawable.hippo, //
            R.drawable.monkey, R.drawable.mushroom, R.drawable.panda, //
            R.drawable.rabbit, R.drawable.raccoon //
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_imageview_simple, container, false);
        ImageView img = (ImageView) v.findViewById(R.id.image_view);
        img.setImageResource(image[getArguments().getInt("position")]);
        return v;
    }
}