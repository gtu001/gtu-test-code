package com.example.gtuandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SystemPicPathActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        back();

        TextView textView1 = (TextView) findViewById(R.id.text);
        StringBuilder sb = new StringBuilder();
        for(String str : getGalleryPath()){
            sb.append(str + "\n");
        }
        textView1.setText(sb.toString());
    }

    private List<String> getGalleryPath(){
        List<String> pathList = new ArrayList<String>();
        try {
            final String[] columns = {MediaStore.Images.Media.DATA,MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED;
            Cursor imagecursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                    null, null, orderBy + " DESC");
            if (imagecursor != null) {
                while (imagecursor.moveToNext()) {
                    int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    String picPath = imagecursor.getString(dataColumnIndex);
                    if(picPath != null){
                        pathList.add(picPath);
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return pathList;
    }
    
    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                SystemPicPathActivity.this.setResult(RESULT_CANCELED, SystemPicPathActivity.this.getIntent());
                SystemPicPathActivity.this.finish();
            }
        });
    }
}
