package com.example.englishtester.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.englishtester.R;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by gtu001 on 2019/11/4.
 */

public class SingleSliderbarDialog {

    private static final String TAG = SingleSliderbarDialog.class.getSimpleName();

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private final EditText editText;
    private final SeekBar seekBar;

    private float defaultVal;
    private int currValue;
    private int levelCount;
    private float diffVal;

    private DialogInterface.OnClickListener defaultCancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                if (dialog != null) {
                    dialog.dismiss();
                }
            } catch (Exception ex) {
            }
        }
    };

    private void setEditTextSelectionEnd(final EditText editText) {
        try {
            editText.setSelection(editText.getText().length());
        } catch (Exception ex) {
        }
    }

    public SingleSliderbarDialog(Context context, int levelCount, int currValue, float diffVal, float defaultVal, boolean readonly) {
        View parentView = LayoutInflater.from(context).inflate(R.layout.subview_single_sliderbar, null);
        builder = new AlertDialog.Builder(context);
        builder.setView(parentView);

        this.editText = (EditText) parentView.findViewById(android.R.id.edit);
        this.seekBar = (SeekBar) parentView.findViewById(R.id.seek_bar);

        this.seekBar.setMax(levelCount);
//        seekBar.setMin(0);
        seekBar.setProgress(currValue);

        this.defaultVal = defaultVal;
        this.currValue = currValue;
        this.levelCount = levelCount;
        this.diffVal = diffVal;

        editText.setText(String.valueOf(defaultVal));
        setEditTextSelectionEnd(editText);
        if (readonly) {
            editText.setEnabled(false);
        }

        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float val = getProgressFromAllParameter(i);
                editText.setText(String.valueOf(val));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private float getProgressFromAllParameter(int i) {
        int addVal = i - this.currValue;
        float resultVal = defaultVal + (float) (addVal * diffVal);
        return resultVal;
    }

    public static String getFixText(String fromText, boolean isTrimToEmpty, boolean isSingleLine) {
        try {
            String text = fromText;
            if (isTrimToEmpty) {
                text = StringUtils.trimToEmpty(text);
            }
            if (isSingleLine) {
                text = StringUtils.defaultString(text).replaceAll("[\r\n]", "");
            }
            return text;
        } catch (Exception ex) {
            throw new RuntimeException("getEditText ERR :" + ex.getMessage(), ex);
        }
    }

    public float getEditTextValue() {
        try {
            String text = editText.getText().toString();
            return Float.valueOf(text);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public void confirmButton(DialogInterface.OnClickListener confirmListener) {
        builder.setPositiveButton("確定", confirmListener);
    }

    public void cancelButton(DialogInterface.OnClickListener cancelListener) {
        if (cancelListener == null) {
            cancelListener = defaultCancelListener;
        }
        builder.setNegativeButton("取消", cancelListener);
    }

    public void show() {
        dialog = builder.create();
        dialog.show();
    }
}
