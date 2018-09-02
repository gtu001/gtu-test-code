package com.example.gtuandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by gtu001 on 2017/7/10.
 */

public class FragmentTest2Activity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = FragmentTest2Activity.class.getSimpleName();

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_fragment_test2);
        Button btn_add_code = (Button) findViewById(R.id.btn_add_code);
        Button btn_add_xml = (Button) findViewById(R.id.btn_add_xml);
        Button btn_remove = (Button) findViewById(R.id.btn_remove);
        btn_add_code.setOnClickListener(this);
        btn_add_xml.setOnClickListener(this);
        btn_remove.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.parent);
        if (R.id.btn_add_code == v.getId() || R.id.btn_add_xml == v.getId()) {
            if (fragment == null) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.parent, getMyFragment("btn_add_code"), "btn_add_code");
                transaction.commit();
                Log.v(TAG, "btn-----1");
            } else {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.parent, getMyFragment("btn_add_xml"), "btn_add_xml");
                transaction.commit();
                Log.v(TAG, "btn-----2");
            }
        } else if (R.id.btn_remove == v.getId() && fragment != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
            Log.v(TAG, "btn-----3");
        }
    }

    private Fragment getMyFragment(String message) {
        MyXmlFragment fragment = new MyXmlFragment();
        Bundle args = new Bundle();
        args.putString("value", message);
        fragment.setArguments(args);
        return fragment;
    }

    public static class MyXmlFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_test2, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setGravity(Gravity.CENTER);
            String message = getArguments().getString("value");
            textView.setText(message);
//            Toast.makeText(getActivity(), "create fragment - " + message, Toast.LENGTH_SHORT).show();
        }
    }
}
