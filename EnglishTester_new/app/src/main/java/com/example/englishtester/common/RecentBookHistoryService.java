package com.example.englishtester.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.englishtester.MainActivity;

public class RecentBookHistoryService  {

    public View.OnClickListener createOnClickListener(final Class<?> clz, final Bundle bundle, final Integer requestCode) {
        View.OnClickListener onClickListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };
        return onClickListner;
    }
}
