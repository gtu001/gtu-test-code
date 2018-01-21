package com.example.gtuandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends Activity {

    private static final String TAG = EditActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        back();

        EditText editText1 = (EditText) findViewById(R.id.editText1);
        editText1.setFilters(getInputFilter());
        editText1.setHint("只允許輸入Email格式");

        AutoCompleteTextView autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        AutoCompleteTextView autoCompleteTextView2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        MultiAutoCompleteTextView multiAutoCompleteTextView1 = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView1);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);

        //取得id的方法 XXX
        int ediText1_id = getResources().getIdentifier("editText1", "id", getPackageName());
        Log.v(TAG, "editText1 的 id是否一致 = " + (ediText1_id == R.id.editText1));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        autoCompleteTextView1.setThreshold(1);//設定輸入幾個字觸發
        autoCompleteTextView1.setAdapter(adapter);

        multiAutoCompleteTextView1.setThreshold(1);//設定輸入幾個字觸發
        multiAutoCompleteTextView1.setAdapter(adapter);
        multiAutoCompleteTextView1.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(R.layout.subview_dropdown_checked);
        adapter3.add("red");
        adapter3.add("green");
        adapter3.add("blue");
        spinner1.setAdapter(adapter3);

        List<String> colorList = new ArrayList<String>();
        colorList.add("1-紅色");
        colorList.add("2-藍色");
        colorList.add("3-綠色");
        SuggestAdapter adapter2 = new SuggestAdapter(this, colorList);
        autoCompleteTextView2.setAdapter(adapter2);
        autoCompleteTextView2.setThreshold(1);
    }

    /**
     * 設定過濾器
     */
    private InputFilter[] getInputFilter(){
        InputFilter inputFilter = new InputFilter(){
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.toString().matches("^[0-9a-zA-Z@\\.\\_\\-]+$")){
                    return source;
                }else{
                    return "";
                }
            }
        };
        InputFilter[] filters = new InputFilter[]{inputFilter};
        return filters;
    }

    /**
     * 設定focus時全選
     * android:selectAllOnFocus="true"
     */
    public static void setEditTextFocusAllSelection(EditText editText){
        String str = editText.getText().toString();
        if(str != null && str.length() > 0){
            editText.setSelection(0, str.length() - 1);
        }
        editText.setSelectAllOnFocus(true);
    }

    private class SuggestAdapter extends ArrayAdapter<String> implements SpinnerAdapter {
        private LayoutInflater mInflator;
        private List<String> mItems;

        public SuggestAdapter(Context context, List<String> objects) {
            super(context, R.layout.subview_listview_simple, objects);
            mInflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mItems = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = mInflator.inflate(R.layout.subview_listview_simple, null, false);
                holder = new ViewHolder();
                holder.textView = (TextView)convertView.findViewById(R.id.textView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            //設定建議字詞候補
            holder.textView.setText(mItems.get(position));
            return convertView;
        }

        private class ViewHolder {
            TextView textView;
        }
    }

    /**
    * 取消focus狀態
     */
    private void clearFoucs(View view) {
        view.clearFocus();
        InputMethodManager inputManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = this.getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 顯示鍵盤
     */
    private void requestFocus(View view){
        view.requestFocus();
        InputMethodManager imm = ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE));
        int softInputAnchor = view.getId();
        imm.toggleSoftInput(softInputAnchor, 0);
        //下方為顯示虛擬鍵盤
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                EditActivity.this.setResult(RESULT_CANCELED, EditActivity.this.getIntent());
                EditActivity.this.finish();
            }
        });
    }
}
