package com.example.englishtester.common;

import android.app.Dialog;
import android.content.Context;
import com.example.englishtester.common.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.englishtester.R;

import java.util.List;

/**
 * 改變TextView字體大小
 */
public class DialogFontSizeChange {
    private static final String TAG = DialogFontSizeChange.class.getSimpleName();

    private Context context;
    private Dialog dialog;
    private TextView fontSizeTextView;
    private List<TextView> viewList;
    private static final int ADD_SIZE = 2;

    public DialogFontSizeChange(Context context) {
        this.context = context;
    }

    public interface ApplyFontSize {
        void applyFontSize(float fontSize);
    }
    
    public static DialogFontSizeChange builder(Context context){
        return new DialogFontSizeChange(context);
    }

    public DialogFontSizeChange apply(float defaultSize, List<TextView> viewList, final ApplyFontSize applyInterface) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_fontsize_change);
        dialog.setTitle("字體大小");

        this.viewList = viewList;

        fontSizeTextView = (TextView) dialog.findViewById(R.id.fontSizeTextView);
        fontSizeTextView.setText("模擬文字大小");
        setTextViewFontSize(defaultSize);

        final Button addBtn = (Button) dialog.findViewById(R.id.addButton);
        final Button substractBtn = (Button) dialog.findViewById(R.id.substractButton);

        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float size = fontSizeTextView.getTextSize();
                size += ADD_SIZE;
                setTextViewFontSize(size);
                if(applyInterface!=null){
                    applyInterface.applyFontSize(size);
                }
            }
        });

        substractBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float size = fontSizeTextView.getTextSize();
                size -= ADD_SIZE;
                setTextViewFontSize(size);
                if(applyInterface!=null){
                    applyInterface.applyFontSize(size);
                }
            }
        });
        return this;
    }

    private void setTextViewFontSize(float size){
        fontSizeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        Log.v(TAG, "currentSize = " + size);
        if(viewList != null && !viewList.isEmpty()){
            for(TextView v : viewList){
                v.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            }
        }
    }
    
    public void show(){
        dialog.show();
    }
    
    public interface DialogConfirmClickListener {
        public void onClick(View v, Dialog dialog, String inputTextStr);
    }
}
