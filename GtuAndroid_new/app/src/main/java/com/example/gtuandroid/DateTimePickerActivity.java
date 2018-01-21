package com.example.gtuandroid;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class DateTimePickerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datetime_picker);
        back();

        DatePicker datePicker1 = (DatePicker) findViewById(R.id.datePicker1);
        TimePicker timePicker1 = (TimePicker) findViewById(R.id.timePicker1);

        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePicker1.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int myear, int monthOfYear, int dayOfMonth) {
                cal.set(Calendar.YEAR, myear);
                cal.set(Calendar.MONTH, monthOfYear - 1);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDisplay(cal);
            }
        });

        timePicker1.setOnTimeChangedListener(new OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hour, int minute) {
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                updateDisplay(cal);
            }
        });

    }

    void updateDisplay(Calendar cal) {
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        textView1.setText(String.format("%d/%d/%d %d:%d:%d", year, month, day, hour, minute, second));
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                DateTimePickerActivity.this.setResult(RESULT_CANCELED, DateTimePickerActivity.this.getIntent());
                DateTimePickerActivity.this.finish();
            }
        });
    }
}
