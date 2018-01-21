package com.example.gtuandroid.sub;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gtuandroid.R;

public class SimpleDialog extends Dialog {

    private EditText username;
    private EditText password;
    private Button confirmButton;
    private Button concelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subview_dialog_singin2);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        concelButton = (Button) findViewById(R.id.concelButton);

        confirmButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(//
                        SimpleDialog.this.getContext(), //
                        "你輸入了 :" + username.getText() + "/" + password.getText(),//
                        Toast.LENGTH_LONG//
                ).show();
            }
        });

        concelButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDialog.this.dismiss();
            }
        });
    }

    public SimpleDialog(Context context) {
        super(context, android.R.style.Theme_Light);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // setContentView(R.layout.subview_dialog_singin2);
    }

    public EditText getUsername() {
        return username;
    }

    public void setUsername(EditText username) {
        this.username = username;
    }

    public EditText getPassword() {
        return password;
    }

    public void setPassword(EditText password) {
        this.password = password;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public void setConfirmButton(Button confirmButton) {
        this.confirmButton = confirmButton;
    }

    public Button getConcelButton() {
        return concelButton;
    }

    public void setConcelButton(Button concelButton) {
        this.concelButton = concelButton;
    }
}