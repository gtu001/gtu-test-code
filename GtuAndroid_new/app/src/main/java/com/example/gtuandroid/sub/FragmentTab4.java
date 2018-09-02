package com.example.gtuandroid.sub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.ViewSwitcher.ViewFactory;

import com.example.gtuandroid.R;

public class FragmentTab4 extends Fragment {

    private int[] image = {//
    R.drawable.cat, R.drawable.flower, R.drawable.hippo,//
            R.drawable.monkey, R.drawable.mushroom, R.drawable.panda,//
            R.drawable.rabbit, R.drawable.raccoon //
    };
    private String[] imgText = {//
    "cat", "flower", "hippo", "monkey", "mushroom", "panda", "rabbit", "raccoon"//
    };//
    private Gallery gallery;
    private ImageSwitcher imageSwitcher;
    private SimpleAdapter simpleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.activity_imageview_switcher, container, false);
        imageSwitcher = (ImageSwitcher) v.findViewById(R.id.image_switcher);
        gallery = (Gallery) v.findViewById(R.id.gallery);
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < image.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("image", image[i]);
            item.put("text", imgText[i]);
            items.add(item);
        }
        simpleAdapter = new SimpleAdapter(getActivity(), //
                items, R.layout.subview_listview_bmp, new String[] { "image", "text" },//
                new int[] { R.id.imageView1, R.id.textView1 });//

        gallery.setAdapter(simpleAdapter);

        imageSwitcher.setFactory(new ViewFactory() {

            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, 180));
                return imageView;
            }

        });
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right));
        gallery.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
                imageSwitcher.setImageResource(image[position]);
            }
        });
        return v;
    }
}