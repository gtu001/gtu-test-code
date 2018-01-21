package com.example.gtuandroid.sub;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gtuandroid.R;

public class Fragment2 extends Fragment {

    // onAttach方法:Fragment和Activity建立關聯的時候調用。
    // onCreateView方法:為Fragment加載布局時調用。
    // onActivityCreated方法:當Activity中的onCreate方法執行完後調用。
    // onDestroyView方法:Fragment中的布局被移除時調用。
    // onDetach方法:Fragment和Activity解除關聯的時候調用。

    public static final String TAG = "Fragment2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment2, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 取得fragment1的文字顯示出來
        Button button = (Button) getActivity().findViewById(R.id.fragment_button);
        if (button != null) {
            button.setOnClickListener(new OnClickListener() {

                private String getTextViewStr(){
                    try{
                        TextView textView = (TextView) getActivity().findViewById(R.id.fragment1_text);
                        String textViewStr = textView.getText().toString();
                        return textViewStr;
                    }catch (Exception ex){
                        return "empty string";
                    }
                }

                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), getTextViewStr(), Toast.LENGTH_LONG).show();
                }
            });
        }
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }
}