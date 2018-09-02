package com.example.gtuandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class JsonParserActivity extends Activity {

    private static final String TAG = JsonParserActivity.class.getSimpleName();

    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        LinearLayout contentView = createContentView();

        textView = new TextView(this);
        contentView.addView(textView);

        // JSON
        Button button1 = new Button(this);
        button1.setText("JSON");
        contentView.addView(button1);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                parseJson();
            }
        });
    }

    private String getJsonFileStr() {
        try {
            InputStream fileIs = getAssets().open("json_test.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileIs));
            StringBuffer sb = new StringBuffer();
            for (String line = null; (line = reader.readLine()) != null;) {
                sb.append(line + "\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseJson() {
        try {
            StringBuilder sb = new StringBuilder();
            JSONObject jsonRootObject = new JSONObject(getJsonFileStr());
            JSONArray jsonArray = jsonRootObject.optJSONArray("Employee");
            for (int ii = 0; ii < jsonArray.length(); ii++) {
                JSONObject jsonObject = jsonArray.getJSONObject(ii);
                int id = Integer.parseInt(jsonObject.optString("id"));
                String name = jsonObject.optString("name");
                float salary = Float.parseFloat(jsonObject.optString("salary"));
                sb.append("Node" + ii + ", Emp. id=" + id + ", Name=" + name + ", Salary=" + salary + "\n");
            }
            textView.setText(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private LinearLayout createContentView() {
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new LayoutParams(LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }
}
