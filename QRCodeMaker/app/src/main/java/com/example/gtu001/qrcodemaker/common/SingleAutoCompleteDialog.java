package com.example.gtu001.qrcodemaker.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.gtu001.qrcodemaker.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by gtu001 on 2018/8/29.
 */

public class SingleAutoCompleteDialog {

    private static final String TAG = SingleAutoCompleteDialog.class.getSimpleName();

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private final AutoCompleteTextView editText;

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

    public SingleAutoCompleteDialog(Context context, String defaultInputValue, List<String> dropdownLst, int threshold, String title, String message) {
        View parentView = LayoutInflater.from(context).inflate(R.layout.subview_single_autocomplete, null);
        builder = new AlertDialog.Builder(context);
        builder.setView(parentView);

        this.editText = (AutoCompleteTextView) parentView.findViewById(android.R.id.edit);
        editText.setThreshold(threshold);
        editText.setText(defaultInputValue);
        setEditTextSelectionEnd(editText);
        if (dropdownLst != null && !dropdownLst.isEmpty()) {
            editText.setAdapter(
                    new ArrayAdapter<String>(context,
                            android.R.layout.simple_dropdown_item_1line, dropdownLst));
        }

        builder.setTitle(title);
        builder.setMessage(message);
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

    public String getEditText(boolean isTrimToEmpty, boolean isSingleLine) {
        try {
            String text = editText.getText().toString();
            return getFixText(text, isTrimToEmpty, isSingleLine);
        } catch (Exception ex) {
            throw new RuntimeException("getEditText ERR :" + ex.getMessage(), ex);
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
