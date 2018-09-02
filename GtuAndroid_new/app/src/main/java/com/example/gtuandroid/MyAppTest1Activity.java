package com.example.gtuandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.gtuandroid.bean.MyApp;
import com.example.gtuandroid.bean.Person;

public class MyAppTest1Activity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApp m = (MyApp)getApplication();
        Person myPerson = m.getPerson();
        myPerson.setName("givemepass");
        myPerson.setAge(18);
        startActivity(new Intent().setClass(MyAppTest1Activity.this, MyAppTest2NextActivity.class));
    }
}
