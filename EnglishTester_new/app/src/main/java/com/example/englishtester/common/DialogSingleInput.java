package com.example.englishtester.common;

import org.apache.commons.lang3.StringUtils;

import android.app.Dialog;
import android.content.Context;
import com.example.englishtester.common.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.englishtester.R;

public class DialogSingleInput {
    private static final String TAG = DialogSingleInput.class.getSimpleName();

    private Context context;
    private Dialog dialog;

    private DialogSingleInput(Context context) {
        this.context = context;
    }
    
    public static DialogSingleInput builder(Context context){
        return new DialogSingleInput(context); 
    }

    public DialogSingleInput apply(String title, String descriptionStr, String defaultText,final DialogConfirmClickListener dialogConfirmClickListener) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_single_input);
        dialog.setTitle(title);

        final TextView description = (TextView) dialog.findViewById(R.id.description);
        final EditText inputText = (EditText) dialog.findViewById(R.id.inputText);
        final Button confirmBtn = (Button) dialog.findViewById(R.id.confirmBtn);
        final Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);

        if (StringUtils.isNotBlank(descriptionStr)) {
            description.setText(descriptionStr);
        }
        if (StringUtils.isNotBlank(defaultText)) {
            inputText.setText(defaultText);
        }
        
        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirmClickListener.onClick(v, dialog, inputText.getText().toString());
            }
        });

        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return this;
    }
    
    public void show(){
        Log.v(TAG, "dialog = " + dialog);
        dialog.show();
    }
    
    public interface DialogConfirmClickListener {
        public void onClick(View v, Dialog dialog, String inputTextStr);
    }
}
