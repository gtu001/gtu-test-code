package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.gtuandroid.sub.Fragment1;
import com.example.gtuandroid.sub.Fragment2;

public class FragmentDynamicActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_dynamic);

        Display display = getWindowManager().getDefaultDisplay();  
        if (display.getWidth() > display.getHeight()) {  
            Fragment1 fragment1 = new Fragment1();  
            getFragmentManager().beginTransaction().replace(R.id.fragment_dynamic, fragment1).commit();  
            Log.v("change", "fragment change 1");
        } else {  
            Fragment2 fragment2 = new Fragment2();  
            getFragmentManager().beginTransaction().replace(R.id.fragment_dynamic, fragment2).commit();  
            Log.v("change", "fragment change 2");
        } 
    }
}
