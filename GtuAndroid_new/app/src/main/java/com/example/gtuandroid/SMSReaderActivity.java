package com.example.gtuandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SMSReaderActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        List<SmsContent> smsList = getSmsContent();
        setContentView(makeMayList(smsList));
        setTitle(getTitle() + ":共" + smsList.size() + "則簡訊");
    }
    
    private class SmsContent {
        String phoneNo;
        String msgBody;
    }
    
    private List<SmsContent> getSmsContent(){
        List<SmsContent> list = new ArrayList<SmsContent>();
        Uri uri = Uri.parse("content://sms/inbox");//系統存放簡訊收件夾的位置
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        if(c != null){
            SmsContent bean = new SmsContent();
            for(boolean hasData = c.moveToFirst(); hasData; hasData = c.moveToNext()){
                bean.phoneNo = c.getString(c.getColumnIndex("address"));
                bean.msgBody = c.getString(c.getColumnIndexOrThrow("body"));
                list.add(bean);
            }
            c.close();
        }
        return list;
    }
    
    private ScrollView makeMayList(List<SmsContent> msgList){
        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll, //
                new LayoutParams(LayoutParams.FILL_PARENT, //設定與螢幕同寬
                LayoutParams.WRAP_CONTENT));//高度隨內容而定
        for(int i = 0 ; i <msgList.size() ; i++){
            SmsContent msg = msgList.get(i);
            TextView tv = new TextView(this);
            tv.setText(i + "號碼:" + msg.phoneNo);
            tv.setTextSize(20f);
            tv.setBackgroundColor(Color.GRAY);
            ll.addView(tv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            
            TextView tv1 = new TextView(this);
            tv1.setText("內容:" + msg.msgBody);
            tv1.setTextSize(20f);
            tv1.setBackgroundColor(Color.BLUE);
            ll.addView(tv1, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        }
        return sv;
    }
}
