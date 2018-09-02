package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class GetServerMessageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView1 = (TextView) findViewById(R.id.text);
        
        String msg = stringQuery("http://localhost/ServerSendMsg.php");
        textView1.setText("Server message is " + msg);
    }
    
    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                GetServerMessageActivity.this.setResult(RESULT_CANCELED, GetServerMessageActivity.this.getIntent());
                GetServerMessageActivity.this.finish();
            }
        });
    }

    private String stringQuery(String url) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost method = new HttpPost(url);
            HttpResponse response = httpclient.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            } else {
                return "No string.";
            }
        } catch (Exception e) {
            return "Network problem";
        }
    }
}