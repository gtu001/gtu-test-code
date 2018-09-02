package com.example.englishtester.common;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class NonUnderlinedClickableSpan extends ClickableSpan {
    @Override
    public void updateDrawState(TextPaint ds) {
//        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false); // set to false to remove underline
    }

    @Override
    public void onClick(View widget) {
        // TODO Auto-generated method stub
    }
}
