package com.example.englishtester.common;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.englishtester.Constant;
import com.example.englishtester.R;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by gtu001 on 2017/10/6.
 * 顯示全版提示畫面
 */
public class FullPageMentionDialog {

    Dialog dialog;

    private FullPageMentionDialog(int drawableResourceId, Context context){
        dialog = new Dialog(context, R.style.helf_translucent);

        FrameLayout layout = getLayout(drawableResourceId, context);
        dialog.setContentView(layout);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }

    public static FullPageMentionDialog builder(int drawableResourceId, Context context){
        return new FullPageMentionDialog(drawableResourceId, context);
    }

    private FrameLayout getLayout(int drawableResourceId, Context context){
        FrameLayout layout = new FrameLayout(context);

//        DisplayMetrics dm = new DisplayMetrics();
//        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int padWidth = dm.widthPixels;
//        int padHeight = dm.heightPixels;

        ImageView imageView = new ImageView(context);
        layout.addView(imageView, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.FILL_PARENT));
        Picasso.with(context).load(drawableResourceId).into(imageView);

        ImageView closeView = new ImageView(context);
        Picasso.with(context).load(R.drawable.close_icon).resize(100, 100).into(closeView);
        closeView.setLeft(0);
        closeView.setTop(0);
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        layout.addView(closeView,//
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        return layout;
    }

    public void showDialog() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 判斷是否已顯示過
     */
    public static boolean isAlreadyFullPageMention(String checkKey, ContextWrapper context) {
        String token = SharedPreferencesUtil.getData(context, //
                FullPageMentionDialog.class.getName(), checkKey);
        if (StringUtils.isBlank(token)) {
            SharedPreferencesUtil.putData(context, //
                    FullPageMentionDialog.class.getName(), checkKey, "done");
            return false;
        }
        return true;
    }
}
