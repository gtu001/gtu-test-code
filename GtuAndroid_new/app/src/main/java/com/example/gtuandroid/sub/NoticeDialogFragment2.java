package com.example.gtuandroid.sub;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gtuandroid.R;
import com.example.gtuandroid.sub.NoticeDialogFragment.NoticeDialogListener;

public class NoticeDialogFragment2 extends DialogFragment {

    private static final String TAG = NoticeDialogFragment2.class.getSimpleName();

    private TextView textView;
    private EditText editText;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，
        // 则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        setCancelable(true);
        // 可以设置dialog的显示风格
        // setStyle(style,theme);
    }

    private static final String DATA_KEY = "input data";

    private NoticeDialogFragment2_CallBack callBack;

    interface NoticeDialogFragment2_CallBack {
        void click(NoticeDialogFragment2 view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence(DATA_KEY, editText.getText());
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // PS：从生命周期的顺序而言，先执行onCreateDialog()，后执行onCreateView() 兩者擇一實作
        View view = inflater.inflate(R.layout.subview_dialog_text_button, container, false);

        TextView titleView = (TextView) getDialog().findViewById(android.R.id.title);
        titleView.setText("自訂title");

        textView = (TextView) view.findViewById(R.id.id_label);
        editText = (EditText) view.findViewById(R.id.id_txt);
        button = (Button) view.findViewById(R.id.id_button);

        // 手機改變方向的時候取回之前的狀態 XXX
        if (savedInstanceState != null) {
            CharSequence text = savedInstanceState.getCharSequence(DATA_KEY);
            editText.setText(text == null ? "" : text);
        }

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你輸入 : " + editText.getText(), Toast.LENGTH_SHORT).show();
                if (callBack != null) {
                    callBack.click(NoticeDialogFragment2.this);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // onAttach()是合适的早期阶段进行检查MyActivity是否真的实现了接口。
        // 采用接口的方式，dialog无需详细了解MyActivity，只需了解其所需的接口函数，这是真正项目中应采用的方式。
        // 在此注入CallBack method XXX
        if (!(activity instanceof NoticeDialogFragment2_CallBack)) {
            Log.v(TAG, "未實作callback!");
            // throw new UnsupportedOperationException("未實作callback!");
        }
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}