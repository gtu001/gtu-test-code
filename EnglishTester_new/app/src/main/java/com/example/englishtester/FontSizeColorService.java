package com.example.englishtester;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import com.example.englishtester.common.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.englishtester.R;

public class FontSizeColorService {
    
    private static final String TAG = FontSizeColorService.class.getSimpleName();

    private ContextThemeWrapper contextWrapper;
    private ViewGroup layout;

    TextView englishLabel;
    TextView englishPronounceLabel;
    Button answerBtn1;
    Button answerBtn2;
    Button answerBtn3;
    Button answerBtn4;
    Button[] answerBtns;

    public FontSizeColorService(ContextThemeWrapper contextWrapper, ViewGroup layout) {
        this.contextWrapper = contextWrapper;
        this.layout = layout;

        englishLabel = (TextView) layout.findViewById(R.id.englishLabel);
        englishPronounceLabel = (TextView) layout.findViewById(R.id.englishPronounceLabel);
        answerBtn1 = (Button) layout.findViewById(R.id.answerBtn1);
        answerBtn2 = (Button) layout.findViewById(R.id.answerBtn2);
        answerBtn3 = (Button) layout.findViewById(R.id.answerBtn3);
        answerBtn4 = (Button) layout.findViewById(R.id.answerBtn4);
        answerBtns = new Button[] { answerBtn1, answerBtn2, answerBtn3, answerBtn4 };
    }

    public void applyColor(boolean isWhiteBackground) {
        Log.v(TAG, "## 白底 = " + isWhiteBackground);

        changeBackGround(isWhiteBackground, layout);

        // 答案按鈕顏色
        resetAnswerBtnColor(isWhiteBackground);

        if (isWhiteBackground) {
            layout.setBackgroundColor(Color.WHITE);
            englishLabel.setTextColor(Color.rgb(91, 0, 2));// 深紫色
            englishPronounceLabel.setTextColor(Color.rgb(48, 4, 164));// 深藍色
        } else {
            layout.setBackgroundColor(Color.BLACK);
            englishLabel.setTextColor(Color.rgb(98, 253, 253));// 淺藍色
            englishPronounceLabel.setTextColor(Color.rgb(145, 254, 188));// 淺藍綠色
        }
    }

    private void changeBackGround(boolean isWhiteBackground, View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewG = (ViewGroup) view;
            for (int ii = 0; ii < viewG.getChildCount(); ii++) {
                View view2 = viewG.getChildAt(ii);
                changeBackGround(isWhiteBackground, view2);
            }
        } else {
            try {
                TextView text = ((TextView) view);
                if (isWhiteBackground) {
                    text.setTextColor(Color.BLACK);
                } else {
                    text.setTextColor(Color.WHITE);
                }
                Log.v(TAG, text.getText().toString());
            } catch (Exception ex) {
                // ex.printStackTrace();
            }
        }
    }

    @TargetApi(16)
    private void resetAnswerBtnColor(boolean isWhiteBackground) {
        // Button[] answerBtns = new Button[] { answerBtn1, answerBtn2,
        // answerBtn3, answerBtn4 };
        if (isWhiteBackground) {
            // final Drawable defaultBtnDrawable =
            // getResources().getDrawable(android.R.drawable.btn_default); //
            // 設定預設按鈕背景
            final Drawable defaultBtnDrawable = contextWrapper.getResources().getDrawable(R.drawable.answer_button_default);
            for (int ii = 0; ii < 4; ii++) {
                answerBtns[ii].setBackground(defaultBtnDrawable);
                // answerBtns[ii].setTextColor(Color.BLACK);
                answerBtns[ii].setTextColor(Color.rgb(15, 0, 202));// 深藍
            }
        } else {
            // final Drawable defaultBtnDrawable =
            // contextWrapper.getResources().getDrawable(R.drawable.answer_button_black);
            final Drawable defaultBtnDrawable = contextWrapper.getResources().getDrawable(R.drawable.answer_button_default);
            for (int ii = 0; ii < 4; ii++) {
                answerBtns[ii].setBackground(defaultBtnDrawable);
                // answerBtns[ii].setTextColor(Color.WHITE);
                answerBtns[ii].setTextColor(Color.rgb(199, 237, 204));// 豆沙綠
            }
        }
    }

    public void changeFontsize() {
        final TextView[] changeSizeComponent = new TextView[] { englishLabel, answerBtn1, answerBtn2, answerBtn3, answerBtn4 };
        final List<String> valList = new ArrayList<String>();
        for (int ii = 40; ii <= 140; ii += 20) {
            valList.add(String.valueOf(ii));
        }
        new AlertDialog.Builder(contextWrapper)//
                .setTitle("字型大小")//
                .setItems(valList.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        float newSizeZ = Float.parseFloat(valList.get(paramInt));
                        for (int ii = 0; ii < changeSizeComponent.length; ii++) {
                            changeSizeComponent[ii].setTextSize(TypedValue.COMPLEX_UNIT_PX, newSizeZ);
                            Log.v(TAG, "字型大小 : " + changeSizeComponent[ii].getTextSize());
                        }
                    }
                })//
                .show();
    }
}
