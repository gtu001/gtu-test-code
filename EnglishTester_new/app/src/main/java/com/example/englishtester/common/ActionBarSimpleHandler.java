package com.example.englishtester.common;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by gtu001 on 2018/8/18.
 */

public class ActionBarSimpleHandler {

    private ActionBarSimpleHandler() {
    }

    public static final ActionBarSimpleHandler newInstance() {
        return new ActionBarSimpleHandler();
    }

    TextView tv;

    public ActionBarSimpleHandler init(Activity activity, Integer color) {
        ActionBar ab = activity.getActionBar();

        // Create a TextView programmatically.
        tv = new TextView(activity);

        // Create a LayoutParams for TextView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView

        // Apply the layout parameters to TextView widget
        tv.setLayoutParams(lp);

        // Set text to display in TextView
        tv.setText(ab.getTitle()); // ActionBar title text

        // Set the text color of TextView to black
        // This line change the ActionBar title text color
        if (color == null) {
            color = Color.WHITE;
        }
        tv.setTextColor(color);

        // Set the TextView text size in dp
        // This will change the ActionBar title text size
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        // Set the ActionBar display option
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        // Finally, set the newly created TextView as ActionBar custom view
        ab.setCustomView(tv);

        return this;
    }

    public void setText(String title) {
        tv.setText(title);

        int length = StringUtils.length(title);
        if (length < 60) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        } else {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        }
    }
}
