package com.example.englishtester.common;

import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wistronits on 2018/7/10.
 */

public class ClickableSpanMethodCreater {

    public static <T extends ClickableSpan> MovementMethod createMovementMethod(Context context, final Class<T>[] clzs) {
        final GestureDetector detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return true;
            }
        });

        return new ScrollingMovementMethod() {

            @Override
            public boolean canSelectArbitrarily() {
                return true;
            }

            @Override
            public void initialize(TextView widget, Spannable text) {
                Selection.setSelection(text, text.length());
            }

            @Override
            public void onTakeFocus(TextView view, Spannable text, int dir) {
                if ((dir & (View.FOCUS_FORWARD | View.FOCUS_DOWN)) != 0) {
                    if (view.getLayout() == null) {
                        // This shouldn't be null, but do something sensible if
                        // it is.
                        Selection.setSelection(text, text.length());
                    }
                } else {
                    Selection.setSelection(text, text.length());
                }
            }

            @Override
            public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
                // check if event is a single tab
                boolean isClickEvent = detector.onTouchEvent(event);

                // detect span that was clicked
                if (isClickEvent) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    x -= widget.getTotalPaddingLeft();
                    y -= widget.getTotalPaddingTop();

                    x += widget.getScrollX();
                    y += widget.getScrollY();

                    Layout layout = widget.getLayout();
                    int line = layout.getLineForVertical(y);
                    int off = layout.getOffsetForHorizontal(line, x);

                    for (Class<T> clickableClz : clzs) {
                        boolean triggerResult = triggerOnClickForClickableSpan(buffer, off, event, widget, clickableClz);
                        if (triggerResult) {
                            break;
                        }
                    }
                }

                // let scroll movement handle the touch
                return super.onTouchEvent(widget, buffer, event);
            }

            private <T extends ClickableSpan> boolean triggerOnClickForClickableSpan(Spannable buffer, int off, MotionEvent event, TextView widget, Class<T> spanClz) {
                T[] link = buffer.getSpans(off, off, spanClz);
                if (link.length != 0) {
                    // execute click only for first clickable span
                    // can be a for each loop to execute every one
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                    }
                    return true;
                }
                return false;
            }
        };
    }
}
