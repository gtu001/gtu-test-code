package com.example.englishtester.common;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.englishtester.R;

import java.util.List;
import java.util.Map;

public class WindowSingleInputDialog {

    private static final String TAG = WindowSingleInputDialog.class.getSimpleName();
    WindowManager mWindowManager;
    Context context;
    LinearLayout outterLayout;

    public WindowSingleInputDialog(final WindowManager mWindowManager, Context context) {
        this.mWindowManager = mWindowManager;
        this.context = context;
    }

    public void showItemListDialog(String title, String label, String deafultStr, final WindowSingleInputDialog_DlgConfirm dlgConfirm) {
        LayoutInflater inflater = LayoutInflater.from(context);
        outterLayout = (LinearLayout) inflater.inflate(R.layout.activity_float_single_input_view, null);

        TextView textView = (TextView) outterLayout.findViewById(R.id.textView);
        textView.setText(title);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextColor(Color.BLACK);

        ImageView imageView = (ImageView) outterLayout.findViewById(R.id.imageView);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowManager.removeView(outterLayout);
            }
        });

        final TextView labelText = (TextView) outterLayout.findViewById(R.id.labelText);
        final EditText singleInput = (EditText) outterLayout.findViewById(R.id.singleInput);
        final Button confirmBtn = (Button) outterLayout.findViewById(R.id.confirmBtn);

        labelText.setText(label);
        singleInput.setText(deafultStr);

        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dlgConfirm != null) {
                    dlgConfirm.onConfirm(singleInput.getText().toString(), v, WindowSingleInputDialog.this);
                }
            }
        });

        outterLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    Log.v(TAG, "touch outside");
                    mWindowManager.removeView(outterLayout);
                }

                // 如果沒有得到focus則關閉編輯視窗
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    mWindowManager.removeView(outterLayout);
                }
                return false;
            }
        });

        mWindowManager.addView(outterLayout, WindowItemListDialog.getInitLayoutParams());
    }

    public interface WindowSingleInputDialog_DlgConfirm {
        void onConfirm(String inputStr, View v, WindowSingleInputDialog dlg);
    }

    public void dismiss() {
        if (outterLayout != null) {
            mWindowManager.removeViewImmediate(outterLayout);
        }
        outterLayout = null;
    }
}
